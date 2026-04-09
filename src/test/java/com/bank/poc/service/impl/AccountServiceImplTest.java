package com.bank.poc.service.impl;

import com.bank.poc.common.exception.BizException;
import com.bank.poc.dto.request.OpenAccountRequest;
import com.bank.poc.dto.response.AccountDetailResponse;
import com.bank.poc.dto.response.OpenAccountResponse;
import com.bank.poc.entity.AccountEntity;
import com.bank.poc.entity.CustomerEntity;
import com.bank.poc.entity.ProductEntity;
import com.bank.poc.repository.AccountRepository;
import com.bank.poc.repository.CustomerRepository;
import com.bank.poc.repository.ProductRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    private AutoCloseable closeable;

    @BeforeMethod
    public void setUp() {
        // 初始化 Mockito 注解
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        // 清理 Mockito 资源
        closeable.close();
    }

    // ==================== 开户测试 (openAccount) ====================

    @Test(description = "开户失败-客户不存在", expectedExceptions = BizException.class)
    public void testOpenAccount_CustomerNotFound() {
        // Given
        OpenAccountRequest request = new OpenAccountRequest();
        request.setCustomerId("C_NOT_EXIST");
        request.setProductCode("SAV001");

        when(customerRepository.findByCustomerId("C_NOT_EXIST")).thenReturn(null);

        // When & Then
        try {
            accountService.openAccount(request);
        } catch (BizException e) {
            assertEquals(e.getCode(), "CUST404");
            throw e; // 重新抛出以匹配 expectedExceptions
        }
    }

    @Test(description = "开户失败-产品不存在", expectedExceptions = BizException.class)
    public void testOpenAccount_ProductNotFound() {
        // Given
        OpenAccountRequest request = new OpenAccountRequest();
        request.setCustomerId("C001");
        request.setProductCode("P_NOT_EXIST");

        CustomerEntity mockCustomer = CustomerEntity.builder().customerId("C001").customerName("张三").build();
        when(customerRepository.findByCustomerId("C001")).thenReturn(mockCustomer);
        when(productRepository.findByProductCode("P_NOT_EXIST")).thenReturn(null);

        // When & Then
        try {
            accountService.openAccount(request);
        } catch (BizException e) {
            assertEquals(e.getCode(), "CUST404");
            throw e;
        }
    }

    @Test(description = "开户失败-产品当前不可售", expectedExceptions = BizException.class)
    public void testOpenAccount_ProductNotOnSale() {
        // Given
        OpenAccountRequest request = new OpenAccountRequest();
        request.setCustomerId("C001");
        request.setProductCode("SAV003"); // SAV003在data.sql中是OFF_SALE

        CustomerEntity mockCustomer = CustomerEntity.builder().customerId("C001").customerName("张三").build();
        ProductEntity mockProduct = ProductEntity.builder().productCode("SAV003").saleStatus("OFF_SALE").build();

        when(customerRepository.findByCustomerId("C001")).thenReturn(mockCustomer);
        when(productRepository.findByProductCode("SAV003")).thenReturn(mockProduct);

        // When & Then
        try {
            accountService.openAccount(request);
        } catch (BizException e) {
            assertEquals(e.getCode(), "CUST404");
            throw e;
        }
    }

    @Test(description = "开户失败-该客户已存在一类户", expectedExceptions = BizException.class)
    public void testOpenAccount_AlreadyHasClassOneAccount() {
        // Given
        OpenAccountRequest request = new OpenAccountRequest();
        request.setCustomerId("C001");
        request.setProductCode("SAV001"); // SAV001属于CLASS_I

        CustomerEntity mockCustomer = CustomerEntity.builder().customerId("C001").customerName("张三").build();
        ProductEntity mockProduct = ProductEntity.builder().productCode("SAV001").saleStatus("ON_SALE").accountLevel("CLASS_I").build();

        when(customerRepository.findByCustomerId("C001")).thenReturn(mockCustomer);
        when(productRepository.findByProductCode("SAV001")).thenReturn(mockProduct);
        when(accountRepository.existsActiveClassOneAccount("C001")).thenReturn(true); // 模拟已有一类户

        // When & Then
        try {
            accountService.openAccount(request);
        } catch (BizException e) {
            assertEquals(e.getCode(), "CUST404");
            throw e;
        }
    }

    @Test(description = "开户成功-二类户")
    public void testOpenAccount_Success_ClassTwo() {
        OpenAccountRequest request = new OpenAccountRequest();
        request.setCustomerId("C001");
        request.setProductCode("SAV002");
        request.setBranchCode("BR001");

        CustomerEntity mockCustomer = CustomerEntity.builder().customerId("C001").customerName("李四").build();
        ProductEntity mockProduct = ProductEntity.builder()
                .productCode("SAV002").saleStatus("ON_SALE").accountLevel("CLASS_II")
                .productType("CURRENT").currency("CNY").build();

        when(customerRepository.findByCustomerId("C001")).thenReturn(mockCustomer);
        when(productRepository.findByProductCode("SAV002")).thenReturn(mockProduct);
        when(accountRepository.existsActiveClassOneAccount("C001")).thenReturn(false);

        OpenAccountResponse response = accountService.openAccount(request);

        assertNotNull(response);
        assertEquals(response.getCustomerId(), "C001");
        assertEquals(response.getAccountName(), "李四");
        assertEquals(response.getAccountType(), "CURRENT");
        assertEquals(response.getCurrency(), "CNY");
        assertEquals(response.getBalance(), BigDecimal.ZERO);
        assertEquals(response.getAccountStatus(), "ACTIVE");
        assertTrue(response.getAccountNo().startsWith("62"));

        verify(accountRepository, times(1)).save(any(AccountEntity.class));
    }
    // ==================== 查询账户详情测试 (getAccountDetail) ====================

    @Test(description = "查询详情失败-账户不存在", expectedExceptions = BizException.class)
    public void testGetAccountDetail_AccountNotFound() {
        // Given
        when(accountRepository.findByAccountNo("NOT_EXIST_NO")).thenReturn(null);

        // When & Then
        try {
            accountService.getAccountDetail("NOT_EXIST_NO");
        } catch (BizException e) {
            assertEquals(e.getCode(), "ACCT404");
            throw e;
        }
    }

    @Test(description = "查询详情失败-客户不存在", expectedExceptions = BizException.class)
    public void testGetAccountDetail_CustomerNotFound() {
        // Given
        AccountEntity mockAccount = AccountEntity.builder().accountNo("62001").customerId("C_NULL").build();
        when(accountRepository.findByAccountNo("62001")).thenReturn(mockAccount);
        when(customerRepository.findByCustomerId("C_NULL")).thenReturn(null);

        // When & Then
        try {
            accountService.getAccountDetail("62001");
        } catch (BizException e) {
            assertEquals(e.getCode(), "ACCT404");
            throw e;
        }
    }

    @Test(description = "查询详情失败-产品不存在", expectedExceptions = BizException.class)
    public void testGetAccountDetail_ProductNotFound() {
        // Given
        AccountEntity mockAccount = AccountEntity.builder().accountNo("62001").customerId("C001").productCode("P_NULL").build();
        CustomerEntity mockCustomer = CustomerEntity.builder().customerId("C001").build();

        when(accountRepository.findByAccountNo("62001")).thenReturn(mockAccount);
        when(customerRepository.findByCustomerId("C001")).thenReturn(mockCustomer);
        when(productRepository.findByProductCode("P_NULL")).thenReturn(null);

        // When & Then
        try {
            accountService.getAccountDetail("62001");
        } catch (BizException e) {
            assertEquals(e.getCode(), "ACCT404");
            throw e;
        }
    }

    public void testGetAccountDetail_Success() {
        LocalDateTime now = LocalDateTime.now();
        AccountEntity mockAccount = AccountEntity.builder()
                .accountNo("62001").customerId("C001")
                .accountName("王五").accountType("CURRENT").productCode("SAV001")
                .currency("CNY").balance(new BigDecimal("100.50"))
                .accountStatus("ACTIVE").branchCode("BR001")
                .openDate(now).createdTime(now).updatedTime(now).build();

        CustomerEntity mockCustomer = CustomerEntity.builder().customerId("C001").customerName("王五").build();
        ProductEntity mockProduct = ProductEntity.builder().productCode("SAV001").productName("一类户").build();

        // ✅ 优化：尽量使用精确参数匹配，而不是 any()，这样能确保逻辑走对的分支
        when(accountRepository.findByAccountNo("62001")).thenReturn(mockAccount);
        when(customerRepository.findByCustomerId("C001")).thenReturn(mockCustomer);
        when(productRepository.findByProductCode("SAV001")).thenReturn(mockProduct);

        AccountDetailResponse response = accountService.getAccountDetail("62001");

        assertNotNull(response);
        assertEquals(response.getAccountNo(), "62001");
        assertEquals(response.getCustomerName(), "王五");
        assertEquals(response.getProductName(), "一类户");
        assertEquals(response.getBalance(), new BigDecimal("100.50"));
    }
}