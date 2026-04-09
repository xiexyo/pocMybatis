# 案例F - 外围依赖 Mock 接入详细设计

## 1. 文档目的

本文档用于指导 AI 在当前工程架构下，以最小改动方式新增外围依赖抽象与 Mock 实现，用于演示银行研发中“外围系统未就绪、联调环境不足”的痛点场景。

实现本案例前，AI 必须先读取：
- `docs/09-00-全局约束与工程基线.md`

建议先完成：
- `docs/09-02-案例B-行内转账接口详细设计.md`

---

## 2. 目标

在不推翻当前工程架构的前提下：

1. 新增外围依赖抽象层
2. 新增 Mock 风控服务
3. 在转账逻辑中接入风控校验
4. 体现 AI 可同时生成主业务代码与外围 mock
5. 保持当前 MyBatis 工程架构不变

---

## 3. 包结构要求

新增包：

```text
com.bank.poc.client
com.bank.poc.client.mock
```

说明：
- 仅作为轻量扩展
- 不引入 Feign，不引入真实 RPC
- 使用 interface + Spring Bean Mock 实现
- 不改变当前 `Controller -> Service -> Mapper -> H2` 架构

---

## 4. 第一优先级外围服务：RiskControlClient

## 4.1 接口定义
新增：
```java
package com.bank.poc.client;

import java.math.BigDecimal;

public interface RiskControlClient {

    boolean checkTransferRisk(String debitAccountNo, String creditAccountNo, BigDecimal amount);
}
```

---

## 4.2 Mock 实现
新增：
```java
package com.bank.poc.client.mock;

@Component
public class MockRiskControlClient implements RiskControlClient
```

### Mock规则
- 当 `amount > 50000` 时，返回 `false`
- 否则返回 `true`

### 说明
该规则仅用于 POC 演示，便于体现：
- 主业务接入外围依赖
- 无真实风控系统时可快速联调

---

## 5. 转账逻辑联动修改

修改：
- `src/main/java/com/bank/poc/service/impl/TransactionServiceImpl.java`

在以下位置插入风控校验：
- 在完成账户存在、状态、币种校验之后
- 在余额校验或余额更新之前

### 校验逻辑
```java
if (!riskControlClient.checkTransferRisk(debitAccountNo, creditAccountNo, amount)) {
    throw new BizException("RISK001", "风险校验未通过");
}
```

---

## 6. 接入要求

## 6.1 Service 注入方式
在 `TransactionServiceImpl` 中通过构造器注入：

```java
private final RiskControlClient riskControlClient;
```

说明：
- 使用 Spring 注入 Mock 实现
- 不引入新的配置中心、远程调用框架

## 6.2 异常处理要求
当风控拒绝时：
- 抛出 `BizException("RISK001", "风险校验未通过")`
- 若当前接口已接入幂等，则应按既有规则处理幂等失败状态

---

## 7. 可选增强外围服务（非必做）

## 7.1 ProductCenterClient
```java
public interface ProductCenterClient {
    boolean isProductOnSale(String productCode);
}
```

### Mock 实现
- 可直接基于当前 `t_product` 返回结果
- 若实现该能力，数据库访问仍通过 Mapper + XML 完成

## 7.2 CustomerCenterClient
```java
public interface CustomerCenterClient {
    boolean isCustomerNormal(String customerId);
}
```

### Mock 实现
- 可默认返回 true
- 或基于 `t_customer.customer_status` 判断
- 若实现数据库校验，必须调用 Mapper，不得在 Client 中写 SQL

说明：
- 本案例优先只实现 `RiskControlClient`
- Product/Customer 可作为扩展展示，不强制实现

---

## 8. 演示规则建议

### 转账场景
- 小额转账（如 1000）-> 风控通过
- 大额转账（如 60000）-> 风控拒绝

### 对外表现
- 小额转账正常完成
- 大额转账返回：
  - 错误码：`RISK001`
  - 错误信息：`风险校验未通过`

---

## 9. 涉及文件

### 新增文件
- `src/main/java/com/bank/poc/client/RiskControlClient.java`
- `src/main/java/com/bank/poc/client/mock/MockRiskControlClient.java`

### 修改文件
- `src/main/java/com/bank/poc/service/impl/TransactionServiceImpl.java`

---

## 10. AI 输出要求

AI 在实现本案例时必须：
1. 保持当前工程主架构不变
2. 仅新增 `client` 与 `client.mock` 包
3. 不引入新的外部依赖
4. 通过 Spring 注入 mock 实现
5. 只在转账逻辑中接入风控
6. 保持业务异常仍使用 `BizException`
7. 不得引入 Feign、Dubbo、OpenFeign、RestTemplate 封装等额外远程调用框架
8. 若涉及数据库访问，仍必须走 Mapper + XML
```
