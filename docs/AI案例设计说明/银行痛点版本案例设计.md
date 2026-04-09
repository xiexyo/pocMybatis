# 零售银行 AICoding 插件 POC 案例设计（银行客户痛点版）

## 1. 文档目的

本文档在当前已完成的零售银行账户服务 POC 工程基础上，对原有案例设计进行升级，目标是不改变现有工程架构和技术栈，仅通过扩充后续案例场景，使 POC 更贴近银行客户真实研发痛点，更适合对银行客户、领导和评审进行演示汇报。

本文档重点解决以下问题：

1. 当前账户服务场景是否过于简单
2. 如何在不切换业务领域的情况下增强银行客户感知价值
3. 如何将现有工程升级为“银行研发痛点驱动”的 POC 演示方案
4. 如何确保所有扩充仍然建立在当前工程架构之上

---

## 2. 设计原则

## 2.1 不推翻当前工程
本次升级以当前工程为基础，不重做架构、不更换技术栈、不放弃已实现内容。

### 当前工程保留项
- Java 17
- Spring Boot 3
- MyBatis + XML
- H2 文件数据库
- Swagger / OpenAPI
- 现有包结构
- 现有统一返回 `ApiResponse`
- 现有 `BizException` / `GlobalExceptionHandler`
- 已实现：创建客户、开立账户、查询账户详情

---

## 2.2 不追求业务变复杂，而追求更贴近银行研发痛点
本次升级不改为贷款、审批流、授信等重场景，而是在“客户-账户-交易”主线上，通过更贴近银行研发实际的问题来组织演示案例。

---

## 2.3 保持 AI Coding 演示友好
所有新增案例都要满足：
- 适合自然语言转代码
- 适合多文件协同生成
- 适合演示需求变更
- 适合演示代码治理与质量控制
- 适合在现有工程上增量实现

---

## 2.4 不把依赖注入方式作为本轮改造重点
本次升级聚焦于：
- 工程分层清晰化
- MyBatis + XML 规范化
- Repository 封装
- 后续银行场景案例扩充

说明：
- 当前工程中的依赖注入方式沿用已有实现风格
- 本文档不强制统一 `@Resource`、`@Autowired` 或构造器注入
- 如后续需要统一编码规范，可在代码治理案例中作为单独治理项进行演示

---

## 3. 当前工程基线

## 3.1 当前业务定位
当前项目业务场景为：

**零售银行个人活期账户开立与账户服务**

当前已具备的主线包括：
- 创建客户
- 开立账户
- 查询账户详情

说明：
- 当前工程已具备开户主链路
- 当前尚未实现存入、转账、交易流水查询、账户冻结等能力
- 后续案例均基于当前已实现链路增量扩展

---

## 3.2 当前架构
项目当前采用标准分层架构：

```text
Controller -> Service -> Repository -> Mapper -> MyBatis XML -> H2 Database
```

### 各层职责
- Controller：暴露 REST 接口
- Service：承载业务逻辑、参数后置校验、事务控制
- Repository：封装仓储语义，屏蔽底层 Mapper 和 SQL 访问细节
- Mapper：定义数据库访问接口
- Mapper XML：承载自定义 SQL
- Entity：普通 POJO，对应表结构字段
- Common/Config：统一返回、异常处理、公共配置

说明：
- 当前工程已经引入 Repository 层
- Service 不直接访问 Mapper
- 所有数据库访问统一通过 `Repository -> Mapper -> XML` 完成

---

## 3.3 当前数据库
已存在 5 张核心表：

- `t_customer`
- `t_product`
- `t_account`
- `t_transaction`
- `t_idempotent_record`

说明：
- 后续扩充的存入、转账、查询、幂等、冻结场景，都基于这 5 张表扩展，不需要另起数据库模型
- 其中 `t_transaction` 和 `t_idempotent_record` 当前虽然已存在表结构、实体、Mapper 与 XML，但尚未被当前已实现业务正式接入

---

## 3.4 当前代码结构
当前工程目录结构保持如下：

```text
com.bank.poc
├── controller
├── service
│   └── impl
├── repository
│   └── impl
├── mapper
├── entity
├── dto
│   ├── request
│   └── response
├── common
│   ├── api
│   └── exception
└── config
```

资源目录补充说明：

```text
src/main/resources
├── application.yml
├── schema.sql
├── data.sql
└── mapper
    ├── CustomerMapper.xml
    ├── ProductMapper.xml
    ├── AccountMapper.xml
    ├── TransactionMapper.xml
    └── IdempotentRecordMapper.xml
```

说明：
- 所有 SQL 统一通过 MyBatis XML 管理
- 当前数据访问封装层为 Repository
- 当前工程不使用 JPA Repository
- 后续案例应在现有分层内扩展，不建议为 POC 再引入新的重型架构层次
- 若后续需要接入外围系统，可按案例需要轻量新增 `client`、`client.mock` 包，但不属于当前工程既有基线

---

## 4. 为什么要升级案例设计

如果只演示“开户 -> 存入 -> 转账 -> 查询”，虽然有业务闭环，但容易给客户造成以下感受：

- 更像普通 CRUD 或轻量交易样例
- 工具能力展示偏功能化，不够贴近银行研发痛点
- 没有体现银行系统开发中最痛的“变更、治理、规范、联调”问题
- 很难和客户已有存量系统改造场景形成共鸣

因此，本次升级的重点不是更换业务，而是把演示案例从“业务流程型”升级为“研发痛点型”。

---

## 5. 银行客户常见研发痛点

本次 POC 升级后，要重点对应银行客户常见的以下痛点：

### 5.1 新增接口开发成本高
新增一个接口时，通常需要联动修改：
- DTO
- Controller
- Service
- Repository
- Mapper
- Mapper XML
- Entity
- SQL 脚本
- 文档
- 测试

### 5.2 需求频繁变更
银行需求经常出现：
- 规则变更
- 字段增加
- 查询条件增加
- 状态控制调整

变更后通常需要多文件联动修改，成本高且易遗漏。

### 5.3 银行业务规则容易漏
例如：
- 幂等
- 事务
- 状态校验
- 余额校验
- 风控占位
- 日志脱敏
- 统一异常返回

### 5.4 存量系统改造多
银行开发很多时候不是新建系统，而是：
- 在已有工程上加功能
- 统一规范
- 修复旧逻辑问题
- 做接口治理和代码治理

### 5.5 外围依赖不完整，联调困难
例如：
- 风控系统
- 客户中心
- 产品中心
- 核心账务
- 柜面/渠道系统

### 5.6 查询类接口性能和规范问题多
- 全量查询
- 无分页
- 无索引
- XML SQL 不规范
- 接口文档不一致
- 查询条件写法不统一

---

## 6. 升级后的案例设计思路

本次升级后，仍然以“客户-账户-交易”为业务主线，但需要区分**当前已实现主线**与**后续规划主线**。

### 当前已实现主线
- 创建客户
- 开立账户
- 查询账户详情

### 后续规划扩展主线
- 存入
- 转账
- 查询流水
- 冻结账户

### 演示主线升级为
1. 新增接口开发
2. 复杂交易接口开发
3. 需求变更联动改造
4. 存量代码治理
5. 高并发 / 幂等 / 事务类风险控制
6. 查询优化
7. 外围依赖 Mock 联调

说明：
- 当前工程并未实现上述“后续规划扩展主线”
- 这些能力将作为后续案例逐步扩展
- 所有扩展都应遵循当前工程的 Repository + Mapper + XML 分层方式

---

## 7. 升级后的 POC 案例体系

本次建议将案例分为：

### 7.1 核心案例（必须演示）
1. 案例A：新增资金类接口开发 —— 账户存入
2. 案例B：复杂交易接口开发 —— 行内转账 + 幂等控制
3. 案例C：需求变更联动改造 —— 账户冻结 / 规则变更
4. 案例D：存量代码治理 —— 规范纠错 + 逻辑漏洞排查
5. 案例E：高频查询优化 —— 交易流水查询优化

### 7.2 增强案例（加分项）
6. 案例F：外围依赖 Mock 接入 —— 风控 / 产品中心 / 客户中心
7. 案例G：银行标准模板沉淀与复用

---

## 8. 各案例与当前工程的关系

| 案例 | 是否基于当前工程直接扩充 | 是否需要新增包层次 | 是否需要新增表 | 说明 |
|---|---|---|---|---|
| 案例A 存入 | 是 | 否 | 否 | 基于现有 `t_account`、`t_transaction`、`t_idempotent_record` 扩展 |
| 案例B 转账+幂等 | 是 | 否 | 否 | 基于现有交易表和幂等表扩展业务接入能力 |
| 案例C 冻结/规则变更 | 是 | 否 | 否 | 主要修改账户相关表结构、DTO、Service、Repository、Mapper、XML |
| 案例D 代码治理 | 是 | 否 | 否 | 直接基于现有代码或预置问题代码演示 |
| 案例E 查询优化 | 是 | 否 | 否 | 增强 `TransactionRepository`、`TransactionMapper`、XML、Service 查询能力 |
| 案例F 外围依赖 Mock | 是 | 可选轻量新增 `client` 包 | 否 | 不影响主架构，可作为轻量扩展 |
| 案例G 模板复用 | 是 | 否 | 否 | 基于已有实现抽象模板 |

说明：
- 所有案例都以当前工程为基础
- 核心案例不要求重构架构
- 不要求新增中间件或分布式组件
- 表中提到的 `TransactionRepository` 等内容，属于后续案例扩展时新增的类，不代表当前工程已经实现

---

## 9. 升级后的详细案例设计

# 9.1 案例A：新增资金类接口开发 —— 账户存入

## 9.1.1 对应银行痛点
- 新增资金类接口需要同时改多层代码
- 容易遗漏金额校验、状态校验、流水落表
- 缺少统一编码风格时容易代码散乱

## 9.1.2 对应当前工程扩充点
在当前工程基础上新增或增强：

### DTO
- `DepositRequest`
- `DepositResponse`

### Service
- `TransactionService`
- `TransactionServiceImpl#deposit`

说明：
- `TransactionService` 当前工程中尚未实现
- 将作为存入案例扩展新增

### Repository
- 新增 `TransactionRepository`
- 新增 `IdempotentRecordRepository`，或在初版中暂由现有数据层封装幂等记录访问
- 增强 `AccountRepository`，增加账户余额更新和状态校验所需方法

说明：
- 上述 Repository 均为后续案例扩展内容
- 当前工程尚未实现资金类仓储能力

### Controller
- `TransactionController#deposit`

### Mapper 增强
- `AccountMapper`
- `TransactionMapper`
- `IdempotentRecordMapper`

### XML 增强
- `AccountMapper.xml`
- `TransactionMapper.xml`
- `IdempotentRecordMapper.xml`

### Entity / SQL 增强
- 复用现有 `TransactionEntity`
- 复用现有 `IdempotentRecordEntity`
- 如需增加余额快照等字段，再同步调整 `schema.sql`

说明：
- 当前交易实体和幂等实体已存在
- 但当前业务尚未正式接入这些表和对象

## 9.1.3 演示重点
- 自然语言转编码
- 多层代码自动生成
- 资金类规则补齐
- Repository、Mapper 与 XML 协同生成
- 生成后的接口可直接联调运行

## 9.1.4 现场演示步骤
1. 展示当前系统只有开户和查账户
2. 输入“新增存入接口”的自然语言需求
3. 查看插件生成的 DTO / Controller / Service / Repository / Mapper / XML 代码
4. 运行项目
5. 调用存入接口
6. 查询账户余额变化
7. 查询交易流水表数据
8. 如本次案例同时纳入幂等要求，可使用同一 `requestId` 再次调用，展示幂等控制

说明：
- 第 8 步是可选增强项
- 如果本轮存入接口不引入幂等控制，可在案例B中统一演示

## 9.1.5 演示价值
该案例证明插件不仅会生成接口骨架，还能理解银行资金接口的最小规则集合，并适配当前工程的 Repository + Mapper + XML 分层模式。

---

# 9.2 案例B：复杂交易接口开发 —— 行内转账 + 幂等控制

## 9.2.1 对应银行痛点
- 转账类接口开发复杂度高
- 双账户更新容易出现事务问题
- 幂等控制是银行高频痛点
- 重试导致重复扣款是高风险问题

## 9.2.2 对应当前工程扩充点
在现有工程基础上新增或增强：

### DTO
- `InternalTransferRequest`
- `InternalTransferResponse`

### Service
- `TransactionService#internalTransfer`
- `TransactionServiceImpl#internalTransfer`

### 可选新增 Service
- `IdempotentService`
- `IdempotentServiceImpl`

说明：
- `IdempotentService` 不是当前工程既有类
- 是否单独抽出，取决于演示时是否希望突出幂等能力封装
- 若保持简单，也可由 `TransactionServiceImpl` 直接编排幂等逻辑

### Repository
- 新增 `TransactionRepository`
- 新增 `IdempotentRecordRepository`
- 增强 `AccountRepository`，增加锁定查询、余额更新、状态校验能力

### Controller
- `TransactionController#internalTransfer`

### Mapper 增强
- `AccountMapper` 增加锁定查询和余额更新能力
- `TransactionMapper` 增强 `requestId` 查询能力
- `IdempotentRecordMapper` 增强幂等记录查询和写入能力

### XML 增强
- `AccountMapper.xml` 增加锁定与余额更新 SQL
- `TransactionMapper.xml` 增强流水写入与按 `requestId` 查询 SQL
- `IdempotentRecordMapper.xml` 增强幂等记录处理 SQL

### Entity / SQL 增强
- 复用现有 `TransactionEntity`
- 如演示需要更完整的流水快照，可扩展增加借贷后余额字段
- `schema.sql` 可按需要增加相关字段和索引

说明：
- 上述字段增强不是当前工程既有内容
- 仅在转账案例需要时按需扩展

## 9.2.3 演示重点
- 多文件协同生成
- Repository 封装
- 幂等控制逻辑
- 事务控制
- 双账户余额联动更新
- 首次成功结果重放
- MyBatis XML 下的复杂 SQL 生成

## 9.2.4 现场演示步骤
1. 创建两个客户和两个账户
2. 如前序已完成案例A，可先给 A 账户存入
3. 输入转账需求，让插件生成多文件代码
4. 查看 Service、Repository、Controller、Mapper、XML、SQL 变更
5. 发起 A -> B 转账
6. 查询 A、B 两个账户余额
7. 查询流水
8. 重复提交同一个 `requestId`
9. 展示不重复扣款

说明：
- 若未先实现案例A，也可通过初始化数据方式准备转账前余额

## 9.2.5 演示价值
这是整个 POC 最关键的案例，最能体现银行客户对“高风险交易接口开发”的真实关注点，也能体现 AI 对 Repository + Mapper + XML 多层协同生成的适配能力。

---

# 9.3 案例C：需求变更联动改造 —— 账户冻结 / 规则变更

## 9.3.1 对应银行痛点
- 银行业务需求变更多、节奏快
- 一个字段或规则变化，通常会牵动多层代码
- 最容易出现“SQL 改了、DTO 没改”“接口改了、查询没改”的问题

## 9.3.2 推荐变更方式
建议二选一，或都做：

### 方案1：账户冻结接口
新增：
- 冻结接口
- 冻结原因字段
- 冻结账户禁止存入和转账

### 方案2：规则变更
例如：
- 原规则：同一客户只允许一个一类户
- 变更为：允许最多三个二类户

## 9.3.3 对应当前工程扩充点
如果选择冻结接口，建议在当前工程基础上新增或增强：

### DTO
- `FreezeAccountRequest`
- `FreezeAccountResponse`

### Service
- `AccountService#freezeAccount`
- `AccountServiceImpl#freezeAccount`

说明：
- `freezeAccount` 当前工程尚未实现
- 这是案例C中的新增能力

### Repository
- 增强 `AccountRepository`，增加冻结更新和冻结状态查询能力

说明：
- 当前工程中没有冻结相关仓储方法
- 应作为案例实现时新增，而不是视为当前已存在能力

### Controller
- `AccountController#freezeAccount`

### Entity / SQL 增强
- `AccountEntity` 新增 `freezeReason`
- `schema.sql` 增加 `freeze_reason`
- `AccountDetailResponse` 返回冻结原因

### Mapper / XML 增强
- `AccountMapper` 增加冻结更新和查询能力
- `AccountMapper.xml` 增加冻结状态更新 SQL

### 相关联动
- 若前序已完成案例A“存入接口”，则 `TransactionServiceImpl#deposit` 需要补充冻结状态校验
- 若前序已完成案例B“转账接口”，则 `TransactionServiceImpl#internalTransfer` 需要补充冻结状态校验

说明：
- 当前工程并不存在 `TransactionServiceImpl#deposit` 和 `internalTransfer`
- 只有在案例A / B 已先实现的前提下，才需要做上述联动修改

## 9.3.4 演示重点
- 需求变更时的多文件联动修改
- `schema / entity / dto / service / repository / controller / mapper / xml` 同步改造
- AI 对已有代码上下文的理解与补全

## 9.3.5 现场演示步骤
1. 展示当前账户接口
2. 提出新增冻结需求
3. 让插件在当前工程上增量修改
4. 查看哪些文件被联动更新
5. 调用冻结接口
6. 若前序已存在存入或转账接口，再次调用相关接口，验证被冻结账户不可操作

## 9.3.6 演示价值
这个案例更贴近银行客户真实研发，因为很多开发工作不是新建，而是变更与改造。

---

# 9.4 案例D：存量代码治理 —— 规范纠错 + 逻辑漏洞排查

## 9.4.1 对应银行痛点
- 银行存量代码多
- 老代码风格不统一
- 容易遗留事务、幂等、安全风险
- 单纯会生成新代码不足以打动客户

## 9.4.2 演示方式
不新增业务，而是准备一段“故意有问题”的已有代码进行扫描检查。

## 9.4.3 建议预置问题

### 规范类问题
- 直接在 Service 中拼接 SQL
- Service 直接访问 Mapper，绕过 Repository
- Mapper 方法与 XML 不一致
- 未使用 `ApiResponse`
- 异常未使用 `BizException`
- 命名风格不符合现有工程
- 仍保留 JPA 风格实现，未统一到当前 MyBatis + Repository 基线

### 逻辑类问题
- 未校验余额
- 未校验相同账户转账
- 未加事务
- 落流水与余额更新不一致
- 一类户限制校验存在遗漏

### 安全类问题
- 打印完整账号和证件号
- 重复 `requestId` 未拦截
- 冻结账户仍允许操作

说明：
- “重复 requestId 未拦截”“冻结账户仍允许操作”更适合放在后续新增资金接口的治理演示中
- 不代表当前已实现接口已经存在这些逻辑

### 查询实现问题
- 全量查询后内存过滤
- 无分页
- XML 动态 SQL 写法不规范
- 未显式控制排序

## 9.4.4 对应当前工程扩充点
此案例不要求新增包结构，仅要求：
- 准备问题代码
- 由 AI 插件扫描和修复
- 修复后回归现有工程风格

## 9.4.5 演示重点
- 语法与规范检查
- 逻辑漏洞识别
- 安全风险提示
- XML SQL 规范检查
- Repository / Mapper 分层问题识别
- 自动修复建议

## 9.4.6 现场演示步骤
1. 打开故意存在问题的转账或存入代码
2. 让插件扫描
3. 展示问题列表
4. 展示修复建议
5. 对比修复前后代码
6. 再次运行验证

## 9.4.7 演示价值
该案例能直接回应银行客户的典型问题：

> 新代码大家都能写，老系统和存量代码能不能辅助治理？

---

# 9.5 案例E：高频查询优化 —— 交易流水查询优化

## 9.5.1 对应银行痛点
- 查询接口数量多
- 初版代码往往只求能跑，不考虑性能
- 内存过滤、无分页、无索引是常见问题
- 银行客户更关心“上线可用性”而不是只看功能

## 9.5.2 对应当前工程扩充点
在现有工程基础上新增或增强：

### DTO
- `TransactionQueryRequest`
- `TransactionItemResponse`
- `TransactionQueryResponse`

### Service
- `TransactionService#queryTransactions`
- `TransactionServiceImpl#queryTransactions`

### Repository
- 新增或增强 `TransactionRepository`
- 在 Repository 层封装分页、排序、组合条件查询语义

说明：
- 当前工程尚未存在 `TransactionRepository`
- 该仓储层由案例E扩展新增
- 当前 POC 阶段可继续使用现有 Repository 分层，不强制再拆独立 `QueryRepository`

### Controller
- `TransactionController#queryTransactions`

### Mapper 增强
- `TransactionMapper` 支持分页和组合条件查询

### XML 增强
- `TransactionMapper.xml` 使用动态 SQL 实现过滤、排序、分页

### SQL 增强
- `schema.sql` 补充索引

## 9.5.3 演示重点
- 从“低质量实现”优化成“分页、排序、条件过滤”
- MyBatis XML 动态 SQL 优化
- Repository 对查询语义的统一封装
- 索引建议
- 返回结构保持不变

## 9.5.4 现场演示步骤
1. 展示一版普通实现
2. 让插件做性能优化
3. 对比前后代码
4. 调用查询接口
5. 展示分页效果和条件过滤
6. 展示 `schema.sql` 中索引建议

## 9.5.5 演示价值
该案例说明插件不仅会“写功能”，还能“优化功能”，更接近企业级研发诉求。

---

# 9.6 案例F：外围依赖 Mock 接入 —— 风控 / 产品中心 / 客户中心（增强项）

## 9.6.1 对应银行痛点
- 银行研发很少是纯单体闭门开发
- 经常依赖外围系统
- 联调环境不全、外围系统未就绪是常态

## 9.6.2 推荐最小扩充方式
在不破坏当前架构的前提下，可轻量新增：

```text
com.bank.poc.client
com.bank.poc.client.mock
```

例如：
- `RiskControlClient`
- `ProductCenterClient`
- `CustomerCenterClient`

POC 阶段采用 Mock 实现即可。

说明：
- `client` 与 `client.mock` 不属于当前工程既有包结构
- 仅在需要演示外围依赖场景时按需新增

## 9.6.3 推荐接入点
例如在转账中增加一条规则：

- 单笔转账金额超过 50,000 时，调用 `RiskControlClient` 做校验

## 9.6.4 演示重点
- AI 自动生成 client 接口和 mock 实现
- 主业务无须等待真实外围系统
- 可快速搭建联调链路
- 若涉及数据库校验，仍通过 `Repository + Mapper + XML` 实现

## 9.6.5 演示价值
非常贴近银行客户环境，因为客户很容易共鸣“外围依赖不全、联调慢”的问题。

---

# 9.7 案例G：银行标准模板沉淀与复用（增强项）

## 9.7.1 对应银行痛点
- 大量接口具有高度重复结构
- 每次从零写，效率低且不统一
- 团队规范难沉淀

## 9.7.2 建议模板

### 模板1：银行写接口模板
包含：
- `requestId`
- 统一 `ApiResponse`
- `BizException`
- 参数校验
- 状态校验
- 幂等占位
- 审计日志占位
- Repository + Mapper + XML 更新模板

### 模板2：银行查询接口模板
包含：
- 分页
- 排序
- 查询条件校验
- 分页响应结构
- Repository + Mapper + XML 动态 SQL 模板

## 9.7.3 演示重点
- 把存入 / 冻结接口沉淀成模板
- 后续复用模板快速生成新接口
- 插件记忆团队编码习惯和工程规范
- 统一 Repository、Mapper 与 XML 风格

## 9.7.4 演示价值
该案例适合向领导说明：

AI 插件不仅提升单次开发效率，还能沉淀团队资产。

---

## 10. 升级后的演示主线建议

建议整体演示顺序如下：

### 第一阶段：已有工程基线（2~3分钟）
展示：
- 当前工程架构
- 已实现接口
- Swagger
- H2 Console
- Repository / Mapper / XML 结构

目的：
证明不是从空白工程开始，而是在现有工程基础上做增量研发。

---

### 第二阶段：案例A - 存入接口（5~7分钟）
展示：
- 自然语言需求输入
- 自动生成代码
- 接口运行
- 余额变化
- 流水落表

---

### 第三阶段：案例B - 转账 + 幂等（8~10分钟）
展示：
- 多文件协同生成
- Repository / Mapper / XML 联动
- 幂等逻辑
- 转账成功
- 重复请求不重复扣款

---

### 第四阶段：案例C - 需求变更（5~6分钟）
展示：
- 新增冻结接口或规则变更
- 多文件联动修改
- 改完直接运行验证

---

### 第五阶段：案例D - 代码治理（4~6分钟）
展示：
- 问题代码扫描
- 规范纠错
- 逻辑漏洞与安全隐患排查

---

### 第六阶段：案例E - 查询优化（4~5分钟）
展示：
- 普通版查询
- 优化后查询
- 分页 / 索引 / 过滤改进

---

## 11. 升级后的汇报口径

建议统一对外口径如下：

> 本次 POC 以零售银行账户服务为业务载体，保留了当前已完成的客户创建、账户开立和账户查询能力，并在不改变现有工程架构的前提下，将后续演示案例升级为更贴近银行客户研发痛点的场景，包括新增资金类接口开发、复杂交易接口幂等与事务控制、需求变更下的多文件联动改造、存量代码治理和高频查询接口优化，从而验证 AICoding 插件在银行研发全流程中的辅助价值。当前工程采用 MyBatis + XML 作为持久化基线，并通过 Repository 对底层数据访问进行封装，更贴近银行现有存量系统中的常见实现方式。

---

## 12. 对领导和客户的价值表达

### 12.1 对领导
可以强调：
- 工程基线已搭好
- 后续不再是纯环境准备，而是进入 AI 赋能实战阶段
- 已经从“功能演示”升级为“研发痛点演示”
- 当前工程分层更清晰，便于后续案例持续扩展

### 12.2 对银行客户
可以强调：
- 不只是生成代码
- 更关注新增开发、变更开发、存量治理、联调和优化
- 更贴近银行研发日常问题
- 可以覆盖 Repository、Mapper、XML、SQL、Service、接口联调的完整开发链路

---

## 13. 后续实施优先级

建议按以下顺序推进：

### 第一优先级
案例A：账户存入  
原因：最容易形成第一条新增开发闭环。

### 第二优先级
案例B：转账 + 幂等  
原因：最能体现银行高风险交易接口研发痛点。

### 第三优先级
案例C：冻结接口 / 规则变更  
原因：最适合演示需求变更联动改造。

### 第四优先级
案例D：代码治理  
原因：增强银行客户对工具质量能力的认可。

### 第五优先级
案例E：查询优化  
原因：体现从功能到性能的延伸价值。

### 第六优先级
案例F / G：外围 Mock 与模板沉淀  
原因：作为加分项，用于进一步提高方案完整度。
```
