# 案例F - 外围依赖 Mock 接入详细设计（基于当前真实工程）

## 1. 文档目的

本文档用于指导在**当前真实工程代码基线**下，以最小改动方式新增外围依赖抽象与 Mock 实现，用于演示银行研发中“外围系统未就绪、联调环境不足、主业务先行开发”的典型场景。

本案例重点体现 AI 编码助手在以下方面的能力：

- 基于现有工程做增量扩展，而不是推翻重构
- 同时生成主业务改造代码与外围 Mock 代码
- 在不引入真实远程调用框架的情况下完成接口抽象
- 支持主流程与 mock 联调并行推进
- 支持后续将 mock 平滑替换为真实实现

实现本案例前，必须先读取：

- `docs/09-00-全局约束与工程基线.md`

建议先完成：

- `docs/09B-行内转账接口详细设计.md`

特别说明：

- 当前真实工程中**尚未存在** `TransactionServiceImpl`
- 当前真实工程中也**尚未存在** `client`、`client.mock` 包
- 因此本案例本质上是：  
  **在案例B已落地“行内转账接口”的基础上，对转账主流程增加一个外围风险控制 Mock 依赖**

如果案例B尚未落地，则本案例可以先实现外围抽象与 Mock Bean，但无法完整展示“转账主流程接入风控”的最终效果。

---

## 2. 目标

在**不推翻当前工程架构**的前提下，实现以下能力：

1. 新增外围依赖抽象层
2. 新增 Mock 风控服务
3. 在转账逻辑中接入风控校验
4. 体现 AI 可同时生成主业务代码与外围 mock
5. 保持当前 MyBatis 工程架构不变
6. 不引入新的远程调用框架
7. 为后续真实外围系统接入保留清晰扩展点
8. 满足 AI 编码插件 POC 中“场景化模板、多文件协同生成、Mock 联调”的演示要求

---

## 3. 当前工程适配说明

## 3.1 必须遵循的真实工程风格

当前真实工程采用的分层结构为：

```text
Controller -> Service -> Repository -> Mapper -> MyBatis XML -> H2 Database
```

本案例新增外围依赖后，整体仍应保持为：

```text
Controller -> Service -> Repository -> Mapper -> MyBatis XML -> H2
                      \-> Client -> Mock Implementation
```

说明：

- `client` 只是对外围依赖的轻量抽象
- `client.mock` 只是本地 Spring Bean Mock 实现
- 不改变当前主业务链路
- 不新增复杂中间层
- 不引入 RPC / Feign / Dubbo / MQ 等额外基础设施

---

## 3.2 与当前真实工程的关系

当前真实工程中：

- 已存在 `AccountServiceImpl`、`CustomerServiceImpl`
- 已存在 Repository / Mapper / XML 分层
- 尚未存在：
  - `TransactionController`
  - `TransactionService`
  - `TransactionServiceImpl`
  - `IdempotentService`
  - `RiskControlClient`

因此本案例依赖场景分为两类：

### A. 当前真实工程可先落地的内容
1. 新增 `client` 包
2. 新增 `client.mock` 包
3. 新增 `RiskControlClient`
4. 新增 `MockRiskControlClient`

### B. 案例B落地后才能完整展示的内容
1. 在 `TransactionServiceImpl#internalTransfer` 中调用风控
2. 风控拒绝时返回 `RISK001`
3. 风控失败时与幂等失败状态联动

---

## 4. 包结构要求

新增包：

```text
com.bank.poc.client
com.bank.poc.client.mock
```

说明：

- 仅作为轻量扩展
- 不引入新的架构层次
- 不改变当前单体工程结构
- 不引入真实 RPC 框架
- 使用 `interface + Spring Bean` 的方式实现 mock
- 与全局基线文档允许的扩展包结构一致

---

## 5. 第一优先级外围服务：RiskControlClient

## 5.1 设计目标

本案例优先实现一个最简单、最容易演示价值的外围依赖：

- 转账前风险校验

该能力在银行场景中具备较强代表性，适合展示：

- 主流程调用外围系统
- 外围未就绪时使用 Mock 替代
- AI 同时生成主业务与 mock
- 后续可平滑替换为真实系统调用

---

## 5.2 接口定义

新增：

`src/main/java/com/bank/poc/client/RiskControlClient.java`

建议定义如下：

```java
package com.bank.poc.client;

import java.math.BigDecimal;

public interface RiskControlClient {

    boolean checkTransferRisk(String debitAccountNo, String creditAccountNo, BigDecimal amount);
}
```

说明：

- 当前 POC 阶段使用最简接口，返回 `boolean`
- 不额外引入复杂的请求响应对象
- 后续若真实接入需返回更丰富信息，可再增量扩展 DTO

---

## 5.3 Mock 实现

新增：

`src/main/java/com/bank/poc/client/mock/MockRiskControlClient.java`

类定义建议：

```java
package com.bank.poc.client.mock;

import com.bank.poc.client.RiskControlClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MockRiskControlClient implements RiskControlClient {
    @Override
    public boolean checkTransferRisk(String debitAccountNo, String creditAccountNo, BigDecimal amount) {
        return amount != null && amount.compareTo(new BigDecimal("50000")) <= 0;
    }
}
```

---

## 5.4 Mock 规则

POC 阶段采用固定规则：

- 当 `amount > 50000` 时，返回 `false`
- 否则返回 `true`

说明：

- 该规则仅用于 POC 演示
- 规则简单、可预期，便于现场演示“通过 / 拒绝”两种结果
- 无需依赖真实风控系统和外部联调环境

---

## 5.5 编码规范要求

在 Mock 实现中应注意：

1. 不打印完整账号
2. 不输出敏感数据明文
3. 不在异常中暴露敏感字段
4. 不在 mock 中写数据库 SQL
5. 不在 mock 中编排主业务逻辑

说明：

- Mock Client 只负责模拟“外围决策结果”
- 不应侵入主业务领域逻辑

---

## 6. 转账逻辑联动修改

## 6.1 修改前提

本章节以**案例B已落地**为前提。

即当前项目中应已存在：

- `TransactionService`
- `TransactionServiceImpl`
- `TransactionController`
- 行内转账接口
- 幂等控制逻辑
- 双账户加锁逻辑

若案例B尚未落地，则本章节内容作为后续接入要求保留。

---

## 6.2 修改文件

### 新增文件
- `src/main/java/com/bank/poc/client/RiskControlClient.java`
- `src/main/java/com/bank/poc/client/mock/MockRiskControlClient.java`

### 修改文件
- `src/main/java/com/bank/poc/service/impl/TransactionServiceImpl.java`

说明：

- 若后续需要在接口文档、单元测试中体现风控校验，也可能联动修改：
  - `TransactionService.java`
  - 测试类
  - 详细设计文档

---

## 6.3 Service 注入方式

在 `TransactionServiceImpl` 中通过构造器注入：

```java
private final RiskControlClient riskControlClient;
```

说明：

- 继续沿用当前工程 `@RequiredArgsConstructor` 风格
- 使用 Spring 自动注入 Mock 实现
- 不引入配置中心、远程调用工厂或额外代理框架

---

## 6.4 风控校验接入位置

在 `internalTransfer` 处理中，建议在以下位置插入风控校验：

### 推荐接入顺序
1. 校验金额 > 0
2. 校验转出转入账户不能相同
3. 校验幂等
4. 查询并锁定两个账户
5. 校验账户存在
6. 校验账户状态
7. 校验币种一致
8. 校验余额充足
9. **调用风控校验**
10. 计算余额
11. 更新账户
12. 写交易流水
13. 更新幂等成功状态

---

## 6.5 为什么风控放在这里

建议将风控校验放在：

- **基础业务校验都通过之后**
- **资金真正扣减之前**

原因如下：

1. 避免对明显非法请求调用外围依赖
2. 保证风控只处理“具备执行条件的转账请求”
3. 避免已经改余额后再被风控拒绝
4. 便于后续真实接入时与业务规则分层更清晰

---

## 6.6 校验逻辑

在 `TransactionServiceImpl#internalTransfer` 中新增：

```java
if (!riskControlClient.checkTransferRisk(debitAccountNo, creditAccountNo, amount)) {
    throw new BizException("RISK001", "风险校验未通过");
}
```

说明：

- 使用统一业务异常 `BizException`
- 错误码使用全局预留：
  - `RISK001`
- 不单独定义新的异常机制

---

## 6.7 幂等与风控拒绝的联动要求

若案例B已按修订后的详细设计实现幂等逻辑，则当风控拒绝时应遵循既有规则：

1. 若本次请求已完成幂等初始化
2. 风控校验返回 `false`
3. 则应先执行：

```java
idempotentService.markFailed(requestId, "INTERNAL_TRANSFER", "RISK001", "风险校验未通过");
```

4. 再抛出：

```java
throw new BizException("RISK001", "风险校验未通过");
```

说明：

- 这样可避免同一个 `requestId` 被反复重试进入主流程
- 与案例A/B中的幂等失败处理风格保持一致

---

## 6.8 异常处理要求

当风控拒绝时：

- 返回错误码：`RISK001`
- 返回错误信息：`风险校验未通过`

由 `GlobalExceptionHandler` 统一处理，不在 Controller 中手工捕获。

---

## 7. 对案例B转账流程的增量修改说明

若案例B已按详细设计完成，本案例相当于对 `internalTransfer` 的流程做一次“需求增量”。

## 7.1 原流程中的关键步骤

案例B原关键流程一般包括：

1. 金额校验
2. 同账户校验
3. 幂等校验
4. 双账户加锁
5. 账户存在性校验
6. 账户状态校验
7. 币种校验
8. 余额校验
9. 更新余额
10. 写流水
11. 更新幂等成功状态

---

## 7.2 本案例新增步骤

在原流程中新增：

9. 风控校验
10. 更新余额
11. 写流水
12. 更新幂等成功状态

即本案例核心是：

- **主流程逻辑不推翻**
- **只做一个清晰的增量插入点**
- **验证 AI 对现有代码做定点改造的能力**

---

## 8. 可选增强外围服务（非必做）

本案例优先只实现 `RiskControlClient`。  
以下能力可作为扩展演示，不作为当前必须项。

---

## 8.1 ProductCenterClient（可选）

接口示例：

```java
public interface ProductCenterClient {
    boolean isProductOnSale(String productCode);
}
```

### Mock 实现建议
- 可直接基于当前 `t_product` 返回结果
- 若要访问数据库，必须调用：
  - Repository
  - Mapper + XML
- 不得在 Client 中直接写 SQL

### 适用场景
- 演示“外围产品中心未就绪，但本地先模拟”

---

## 8.2 CustomerCenterClient（可选）

接口示例：

```java
public interface CustomerCenterClient {
    boolean isCustomerNormal(String customerId);
}
```

### Mock 实现建议
- 可默认返回 `true`
- 或基于 `t_customer.customer_status` 判断
- 若访问数据库，仍必须走 Mapper + XML

### 适用场景
- 演示“客户中心校验逻辑”的外围依赖抽象

---

## 8.3 扩展原则

即使扩展 Product / Customer Client，也必须遵守：

1. 不引入新框架
2. 不改变主工程架构
3. 不在 Client 中编排主业务流程
4. 若访问数据库，必须走现有数据访问规范

---

## 9. 演示规则建议

## 9.1 转账场景建议

### 小额转账
例如：

- 金额：`1000`

预期：

- 风控通过
- 转账正常完成

### 大额转账
例如：

- 金额：`60000`

预期：

- 风控拒绝
- 返回：
  - `code = RISK001`
  - `message = 风险校验未通过`

---

## 9.2 演示价值

该场景适合向银行客户展示：

1. 外围系统未就绪时，主流程仍可推进开发
2. AI 可同时生成：
  - 主业务改造代码
  - 外围 Mock 接口与实现
3. 风控规则可快速切换与验证
4. 未来替换成真实系统实现时，主业务改动最小

---

## 10. 测试场景

## 10.1 功能测试场景

在案例B已落地的前提下，建议覆盖：

1. 小额转账，风控通过
2. 大额转账，风控拒绝
3. 风控拒绝后返回 `RISK001`
4. 风控拒绝后不更新转出账户余额
5. 风控拒绝后不更新转入账户余额
6. 风控拒绝后不写入交易流水
7. 风控拒绝后幂等状态更新为 `FAILED`
8. 相同 `requestId` 重试风控失败请求，返回幂等失败结果

---

## 10.2 建议补充的单元测试场景

为适配 AI 编码插件 POC 中“单元测试生成”能力，建议补充：

1. `amount <= 50000` 时 `riskControlClient` 返回通过
2. `amount > 50000` 时 `riskControlClient` 返回拒绝
3. 风控拒绝时抛出 `RISK001`
4. 风控拒绝时不执行余额更新
5. 风控拒绝时不执行交易流水保存
6. 风控拒绝时幂等失败状态更新正确
7. 风控通过时主流程继续执行

---

## 11. AI 输出与实现要求

AI 在实现本案例时必须：

1. 保持当前工程主架构不变
2. 仅新增 `client` 与 `client.mock` 包
3. 不引入新的外部依赖
4. 通过 Spring 注入 Mock 实现
5. 只在转账逻辑中接入风控
6. 保持业务异常仍使用 `BizException`
7. 不得引入 Feign、Dubbo、OpenFeign、RestTemplate 封装等额外远程调用框架
8. 若涉及数据库访问，仍必须走 Repository + Mapper + XML
9. 若案例B已存在幂等逻辑，必须同步考虑幂等失败状态更新
10. 输出代码应可直接编译运行
11. 明确列出本次涉及哪些文件变更
12. 生成代码时不得暴露敏感信息日志

---

## 12. 与 AI 编码插件 POC 要求的对应关系

本案例可直接用于验证 AI 编码插件以下能力：

### 12.1 编码生成功能

1. **自然语言转编码**  
   可让 AI 根据“新增风险控制外围 mock，并接入转账主流程”一次性生成：
  - client 接口
  - mock 实现
  - service 改造代码

2. **代码智能补全**  
   可在 `TransactionServiceImpl`、`MockRiskControlClient` 中演示上下文补全。

3. **指定技术栈生成**  
   明确要求生成：
  - Java 17
  - Spring Boot 3
  - 普通 Spring Bean Mock
  - MyBatis XML 工程兼容代码

4. **特色编码模板**  
   可沉淀为“外围依赖抽象 + mock 接入模板”：
  - client interface
  - mock implementation
  - service injection
  - 主流程调用点插入

5. **自定义模板生成与记忆**  
   可沉淀为行内统一模板：
  - 风控 mock 模板
  - 外围依赖抽象模板
  - 主流程接入模板

6. **多文件协同生成**  
   本案例涉及：
  - client
  - client.mock
  - service impl  
    多文件联动明显，适合演示。

---

### 12.2 编码纠错与优化功能

1. **语法与规范纠错**  
   可检查：
  - 是否误用了真实远程调用框架
  - 是否在 client 中写了主业务逻辑
  - 是否遗漏 Spring Bean 注入

2. **逻辑漏洞与安全隐患排查**  
   可检查：
  - 风控拒绝后是否仍更新余额
  - 风控拒绝后是否仍写流水
  - 是否遗漏幂等失败联动
  - 是否在 mock 中打印敏感账号

3. **代码性能优化**  
   本案例性能点不在 SQL，而在于：
  - mock 接入应轻量
  - 不引入不必要的复杂框架
  - 不破坏主流程清晰性

4. **合规性校验**  
   可检查：
  - 是否符合当前工程统一异常规范
  - 是否符合当前分层结构
  - 是否未暴露敏感数据

5. **版本兼容适配**  
   可检查：
  - 是否兼容 Spring Boot 3
  - 是否兼容当前真实工程结构
  - 是否不依赖额外中间件

---

### 12.3 代码理解与单元测试

1. **单元测试生成**  
   本案例适合自动生成 mock 驱动的单元测试。

2. **注释生成与优化**  
   可自动生成：
  - client 注释
  - mock 规则说明
  - service 风控接入点注释

---

## 13. 当前详设可进行演示的功能点

## 13.1 基于当前真实工程可直接演示的功能点

即使案例B尚未完成，本案例也可以先演示：

1. **在当前工程中新增 `client` / `client.mock` 包**
2. **AI 自动生成外围接口与 Mock 实现**
3. **AI 保持当前工程架构不变，只做轻量扩展**
4. **AI 不引入 Feign / Dubbo 等额外框架**
5. **AI 为未来真实外围系统接入预留扩展点**

---

## 13.2 若案例B已落地，可完整演示的功能点

在案例B已实现后，本案例可完整演示：

1. **转账主流程接入风险校验**
2. **小额转账通过**
3. **大额转账被 mock 风控拒绝**
4. **风控失败不改余额、不写流水**
5. **风控失败与幂等状态联动**
6. **AI 自动完成主流程与 mock 的多文件改造**

---

## 13.3 最适合现场演示的场景

建议现场优先演示以下 5 个场景：

1. **AI 一次生成 `RiskControlClient` 与 `MockRiskControlClient`**
2. **AI 在转账服务中插入风险校验逻辑**
3. **小额转账成功**
4. **大额转账返回 `RISK001`**
5. **AI 展示“外围未就绪也可联调”的工程改造价值**

---
