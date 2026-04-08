# retail-bank-aicoding-poc

一个基于 **Java 17 + Spring Boot 3 + JPA + H2** 的零售银行接口研发 POC 工程，用于演示 **AI Coding 插件** 在需求分析、数据建模、代码生成、接口开发与联调中的辅助能力。

---

## 1. 项目简介

本项目是一个轻量级的银行业务后端样例，当前聚焦以下核心链路：

- 创建客户
- 开立账户
- 查询账户详情

项目目标不是构建完整银行核心系统，而是作为一个 **可运行、可扩展、适合 AI 插件持续生成代码** 的演示工程。

---

## 2. 技术栈

- **Java 17**
- **Spring Boot 3.3.2**
- **Spring Web**
- **Spring Validation**
- **Spring Data JPA**
- **H2 Database（文件模式）**
- **Lombok**
- **springdoc-openapi / Swagger UI**
- **Maven**

---

## 3. 当前已实现功能

### 3.1 基础能力
- Spring Boot 工程初始化
- H2 文件数据库接入
- 启动自动执行 `schema.sql`、`data.sql`
- Swagger UI 接口文档
- H2 Console 控制台
- 全局异常处理
- 统一返回结构

### 3.2 已实现业务接口
- 健康检查
- 创建客户
- 开立账户
- 查询账户详情

---

## 4. 项目结构

```text
retail-bank-aicoding-poc
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   ├── java/com/bank/poc
│   │   │   ├── RetailBankPocApplication.java
│   │   │   ├── common
│   │   │   │   ├── api
│   │   │   │   │   └── ApiResponse.java
│   │   │   │   └── exception
│   │   │   │       └── BizException.java
│   │   │   ├── config
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── controller
│   │   │   │   ├── HealthController.java
│   │   │   │   ├── CustomerController.java
│   │   │   │   └── AccountController.java
│   │   │   ├── dto
│   │   │   │   ├── request
│   │   │   │   └── response
│   │   │   ├── entity
│   │   │   │   ├── CustomerEntity.java
│   │   │   │   ├── ProductEntity.java
│   │   │   │   ├── AccountEntity.java
│   │   │   │   ├── TransactionEntity.java
│   │   │   │   └── IdempotentRecordEntity.java
│   │   │   ├── repository
│   │   │   │   ├── CustomerRepository.java
│   │   │   │   ├── ProductRepository.java
│   │   │   │   ├── AccountRepository.java
│   │   │   │   ├── TransactionRepository.java
│   │   │   │   └── IdempotentRecordRepository.java
│   │   │   └── service
│   │   │       ├── CustomerService.java
│   │   │       ├── AccountService.java
│   │   │       └── impl
│   │   │           ├── CustomerServiceImpl.java
│   │   │           └── AccountServiceImpl.java
│   │   └── resources
│   │       ├── application.yml
│   │       ├── schema.sql
│   │       └── data.sql
│   └── test
```

---

## 5. 分层架构说明

项目采用典型的分层结构：

```text
Controller -> Service -> Repository -> Database
```

### 5.1 Controller
负责：
- 暴露 REST 接口
- 接收请求参数
- 调用 Service
- 返回统一响应

### 5.2 Service
负责：
- 核心业务逻辑处理
- 业务校验
- 实体组装
- 调用 Repository 持久化

### 5.3 Repository
负责：
- 数据访问
- 使用 Spring Data JPA 完成 CRUD 和简单查询

### 5.4 Entity
负责：
- 数据库表结构映射

### 5.5 Common / Config
负责：
- 统一返回结构
- 异常处理
- 公共异常定义

---

## 6. 数据库设计

当前包含 5 张核心表：

### 6.1 `t_customer`
客户主数据表

主要字段：
- `customer_id`
- `customer_name`
- `cert_type`
- `cert_no`
- `mobile`
- `customer_status`
- `risk_level`

### 6.2 `t_product`
产品配置表

主要字段：
- `product_code`
- `product_name`
- `product_type`
- `currency`
- `sale_status`
- `account_level`

### 6.3 `t_account`
账户主数据表

主要字段：
- `account_no`
- `customer_id`
- `account_name`
- `account_type`
- `product_code`
- `currency`
- `balance`
- `account_status`
- `branch_code`

### 6.4 `t_transaction`
交易流水表

主要字段：
- `txn_id`
- `request_id`
- `debit_account_no`
- `credit_account_no`
- `txn_type`
- `txn_status`
- `amount`

### 6.5 `t_idempotent_record`
幂等记录表

主要字段：
- `request_id`
- `business_type`
- `business_key`
- `process_status`
- `response_code`
- `response_message`

---

## 7. 初始化数据

启动后会自动向 `t_product` 初始化以下产品：

- `SAV001`：个人人民币一类活期
- `SAV002`：个人人民币二类活期
- `SAV003`：个人美元活期（停售示例）

---

## 8. 环境要求

- JDK 17
- Maven 3.8+
- IntelliJ IDEA（推荐）
- 已安装 Lombok 插件
- 已开启 Annotation Processing

IDEA 设置路径：

```text
Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors
```

勾选：

```text
Enable annotation processing
```

---

## 9. 本地启动方式

### 9.1 直接启动
运行启动类：

```java
com.bank.poc.RetailBankPocApplication
```

### 9.2 Maven 启动

```bash
mvn clean spring-boot:run
```

### 9.3 打包运行

```bash
mvn clean package
java -jar target/retail-bank-aicoding-poc-1.0.0-SNAPSHOT.jar
```

---

## 10. 关键配置说明

当前数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:h2:file:./data/retail-bank-poc
    driver-class-name: org.h2.Driver
    username: sa
    password:
```

说明：
- 使用 H2 文件模式
- 数据库存储在项目根目录的 `data/` 下
- 重启应用后数据仍保留

如需清空数据重新初始化：
1. 停止应用
2. 删除 `data/` 目录下数据库文件
3. 重启项目

---

## 11. 可访问地址

### 11.1 Swagger UI
```text
http://localhost:8080/swagger-ui.html
```

### 11.2 H2 Console
```text
http://localhost:8080/h2-console
```

H2 Console 登录信息：

- JDBC URL

```text
jdbc:h2:file:./data/retail-bank-poc
```

- User Name

```text
sa
```

- Password  
  留空

### 11.3 健康检查
```text
http://localhost:8080/health
```

---

## 12. 当前接口列表

### 12.1 健康检查

**URL**
```http
GET /health
```

**响应示例**
```json
{
  "code": "B0000",
  "message": "success",
  "data": {
    "service": "retail-bank-aicoding-poc"
  }
}
```

---

### 12.2 创建客户

**URL**
```http
POST /api/v1/customers
```

**请求示例**
```json
{
  "customerName": "张三",
  "certType": "ID_CARD",
  "certNo": "110101199001011234",
  "mobile": "13800000000",
  "riskLevel": "R2"
}
```

**响应示例**
```json
{
  "code": "B0000",
  "message": "success",
  "data": {
    "customerId": "C202508261430001234",
    "customerName": "张三",
    "certType": "ID_CARD",
    "certNo": "110101199001011234",
    "mobile": "13800000000",
    "customerStatus": "ACTIVE",
    "riskLevel": "R2",
    "createdTime": "2025-08-26T14:30:00"
  }
}
```

---

### 12.3 开立账户

**URL**
```http
POST /api/v1/accounts/open
```

**请求示例**
```json
{
  "customerId": "C202508261430001234",
  "productCode": "SAV001",
  "branchCode": "310001"
}
```

**响应示例**
```json
{
  "code": "B0000",
  "message": "success",
  "data": {
    "accountNo": "6220250826143000123456",
    "customerId": "C202508261430001234",
    "accountName": "张三",
    "accountType": "CURRENT",
    "productCode": "SAV001",
    "currency": "CNY",
    "balance": 0,
    "accountStatus": "ACTIVE",
    "branchCode": "310001",
    "openDate": "2025-08-26T14:35:00"
  }
}
```

---

### 12.4 查询账户详情

**URL**
```http
GET /api/v1/accounts/{accountNo}
```

**示例**
```http
GET /api/v1/accounts/6220250826143000123456
```

**响应示例**
```json
{
  "code": "B0000",
  "message": "success",
  "data": {
    "accountNo": "6220250826143000123456",
    "customerId": "C202508261430001234",
    "customerName": "张三",
    "accountName": "张三",
    "accountType": "CURRENT",
    "productCode": "SAV001",
    "productName": "个人人民币一类活期",
    "currency": "CNY",
    "balance": 0,
    "accountStatus": "ACTIVE",
    "branchCode": "310001",
    "openDate": "2025-08-26T14:35:00",
    "createdTime": "2025-08-26T14:35:00",
    "updatedTime": "2025-08-26T14:35:00"
  }
}
```

---

## 13. 返回结构规范

所有接口统一返回：

```json
{
  "code": "B0000",
  "message": "success",
  "data": {}
}
```

### 返回码约定
- `B0000`：成功
- `P0001`：参数校验失败
- `S9999`：系统异常
- 其他：业务异常码，如 `CUST404`、`PROD404`、`ACCT001`

---

## 14. 异常处理机制

项目统一使用：

- `BizException`：业务异常
- `GlobalExceptionHandler`：全局异常处理器

规范：
- Service 中抛出业务异常
- Controller 不手工拼装错误响应
- 所有异常统一转换为 `ApiResponse`

---

## 15. 当前业务规则

### 15.1 创建客户
- 证件号不能重复
- 默认客户状态：`ACTIVE`
- 默认风险等级：`R2`

### 15.2 开户
- 客户必须存在
- 产品必须存在
- 产品状态必须为 `ON_SALE`
- 若产品是一类户（`CLASS_I`），则同一客户只能开立一个有效一类户
- 账户初始余额为 `0`
- 账户状态默认 `ACTIVE`

---

## 16. 当前编码约束

### 16.1 包规范
统一包前缀：

```java
com.bank.poc
```

### 16.2 命名规范
- Entity：`XXXEntity`
- Repository：`XXXRepository`
- Service：`XXXService`
- Service 实现：`XXXServiceImpl`
- 请求 DTO：`XXXRequest`
- 响应 DTO：`XXXResponse`
- Controller：`XXXController`

### 16.3 依赖规范
Spring Boot 3 统一使用：
- `jakarta.persistence.*`
- `jakarta.validation.*`

不要使用：
- `javax.persistence.*`
- `javax.validation.*`

### 16.4 返回规范
统一使用：

```java
ApiResponse<T>
```

### 16.5 异常规范
业务错误统一抛：

```java
throw new BizException("业务码", "错误描述");
```

---

## 17. 适合 AI Coding 插件继续扩展的方向

当前工程已经非常适合作为 AI 插件的持续开发上下文，推荐后续继续实现：

### 17.1 入金接口
目标：
- 更新账户余额
- 写入交易流水

### 17.2 行内转账接口
目标：
- 借方扣款
- 贷方加款
- 记录交易流水

### 17.3 交易明细查询
目标：
- 按账号、交易类型、时间范围查询流水

### 17.4 幂等控制
目标：
- 引入 `t_idempotent_record`
- 避免重复开户、重复入金、重复转账

### 17.5 账户冻结 / 解冻
目标：
- 更新账户状态
- 展示新增接口的完整开发流程

---

## 18. 推荐给 AI 插件的上下文描述

后续可以将以下内容直接作为插件提示词的一部分：

> 当前项目是一个基于 Java 17 + Spring Boot 3.3.2 + Spring Data JPA + H2 的零售银行接口研发 POC。  
> 项目采用分层架构：Controller -> Service -> Repository -> H2。  
> 已有 5 张表：t_customer、t_product、t_account、t_transaction、t_idempotent_record。  
> 已完成客户创建、账户开立、账户详情查询功能。  
> 公共返回结构为 ApiResponse<T>，业务异常使用 BizException，全局异常处理器为 GlobalExceptionHandler。  
> DTO 分为 dto.request 和 dto.response，Entity 与表结构一一对应。  
> 所有新代码请遵循现有包结构和命名风格，并优先复用已有 Repository 和 Service 风格。  
> 使用 jakarta 包，不使用 javax。  
> 若涉及数据库字段变更，请同步更新 schema.sql、Entity、DTO、Service、Controller。

---

## 19. 推荐给 AI 插件的任务模板

### 模板一：新增接口
> 请基于当前工程新增“账户入金接口”，要求：  
> 1）新增请求 DTO 和响应 DTO  
> 2）在 AccountService 或 TransactionService 中新增入金方法  
> 3）更新账户余额  
> 4）写入 t_transaction 流水  
> 5）新增 Controller 对外接口  
> 6）遵循现有 ApiResponse、BizException、GlobalExceptionHandler 风格  
> 7）复用现有 Repository，必要时补充新的查询方法

### 模板二：字段变更
> 请基于当前工程，为 t_account 增加 freeze_reason 字段，并同步更新 schema.sql、AccountEntity、DTO、Service、Controller，保持现有代码风格。

### 模板三：新增查询
> 请基于当前工程新增“交易流水查询接口”，支持按 accountNo、txnType、startTime、endTime 查询。请补充 DTO、Service、Controller、Repository 查询方法，并保持当前项目分层结构。

---

## 20. 后续规划

建议开发顺序：

1. 入金接口
2. 转账接口
3. 交易查询接口
4. 幂等控制
5. 账户冻结 / 解冻接口
6. 单元测试与集成测试

---

## 21. 注意事项

- 当前主键生成规则为时间戳 + 随机数，仅适合 POC
- 当前状态字段使用字符串，后续可演进为枚举
- 当前未引入 Mapper 层，DTO 组装主要在 Service 中完成
- 当前未实现幂等逻辑，仅预留了表结构与 Repository
- 如需重新初始化数据库，请删除项目根目录下 `data/` 中的 H2 文件

---
