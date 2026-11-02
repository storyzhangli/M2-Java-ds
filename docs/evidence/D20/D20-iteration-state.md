# D20 迭代状态

## 需求编号

D20-R01：统一模型调用异常映射、超时配置与有限重试。

## 完成范围

- 建立同步/流式共用的内部失败分类和既有错误码映射。
- 外化建连、读取、同步总 Deadline 和流式总时限。
- 在 Spring AI 已注入的唯一同步 `RetryTemplate` 上实施有限重试。
- 仅重试 429、502/503/504、可确认请求未发送的建连失败。
- 流式调用不自动重试；已输出内容后绝不从头重放。
- 保持既有同步 URL、HTTP 方法、请求字段和响应结构。

## 修改文件

### 构建与配置

- `D20/D20_UMIEM/pom.xml`
- `D20/D20_UMIEM/src/main/resources/application.yml`

### 新增模型调用治理

- `D20/D20_UMIEM/src/main/java/com/zl/d20/config/ModelCallProperties.java`
- `D20/D20_UMIEM/src/main/java/com/zl/d20/config/ModelCallConfiguration.java`
- `D20/D20_UMIEM/src/main/java/com/zl/d20/model/ClassifiedResponseErrorHandler.java`
- `D20/D20_UMIEM/src/main/java/com/zl/d20/model/ModelAttemptRetryPolicy.java`
- `D20/D20_UMIEM/src/main/java/com/zl/d20/model/ModelFailure.java`
- `D20/D20_UMIEM/src/main/java/com/zl/d20/model/ModelFailureCategory.java`
- `D20/D20_UMIEM/src/main/java/com/zl/d20/model/ModelFailureClassifier.java`
- `D20/D20_UMIEM/src/main/java/com/zl/d20/model/ModelRetryBackOffPolicy.java`
- `D20/D20_UMIEM/src/main/java/com/zl/d20/model/ModelRetryListener.java`
- `D20/D20_UMIEM/src/main/java/com/zl/d20/model/NonRetryableModelException.java`
- `D20/D20_UMIEM/src/main/java/com/zl/d20/model/RetryableModelException.java`

### 对外映射

- `D20/D20_UMIEM/src/main/java/com/zl/d20/common/GlobalExceptionHandler.java`
- `D20/D20_UMIEM/src/main/java/com/zl/d20/controller/ChatStreamController.java`

### 页面标识

- `D20/D20_UMIEM/src/main/resources/static/index.html`
- `D20/D20_UMIEM/src/main/resources/static/js/app.js`
- `D20/D20_UMIEM/src/main/resources/static/templates.html`

### 测试

- `D20/D20_UMIEM/src/test/java/com/zl/d20/model/`
- `D20/D20_UMIEM/src/test/java/com/zl/d20/stream/ChatStreamTotalDeadlineTest.java`
- `D20/D20_UMIEM/src/test/java/com/zl/d20/stream/ChatStreamErrorEventTest.java`
- `D20/D20_UMIEM/src/test/java/com/zl/d20/stream/StreamClientDisconnectIntegrationTest.java`
- `D20/D20_UMIEM/src/test/java/com/zl/d20/ApplicationContextLoadTest.java`
- `D20/D20_UMIEM/src/test/java/com/zl/d20/D20TestBootstrap.java`
- `D20/D20_UMIEM/src/test/java/com/zl/d20/stream/ChatStreamIntegrationTest.java`

## 设计决定

- 保留 Spring AI Starter 已传递的 Spring Retry，不新增另一套重试框架。
- 只注册一个 D20 `RetryTemplate` Bean；DeepSeek 自动配置注入该实例。
- `spring.ai.retry.max-attempts=1` 是 Bean 意外缺失时的安全 fallback，不是主路径。
- 不增加 Service 或 HTTP 客户端层重试。
- 同步最多 2 次总尝试；流式不自动重试。
- 请求已发送后的读取超时按结果不确定处理，不重试。
- `Retry-After` 超过退避上限时不重试；总 Deadline 约束全部同步尝试与退避。

## 测试

- D20 专项：28/28 通过。
- 全量回归：161 个测试，0 失败，0 错误，1 跳过。
- 打包：成功。
- 实际命令、退出码与日志见 `D20-test-summary.md`。

## 证据

- 现状索引与文件哈希：`D20-baseline-index.md`
- 版本与调用链：`D20-version-and-source-evidence.md`
- 配置与依赖：`D20-dependency-tree-filtered.log`
- Spring AI 元数据：`D20-retry-configuration-metadata.json`
- 测试摘要：`D20-test-summary.md`
- Git 差异：`D20-git-diff.patch`、`D20-git-diff-stat.txt`
- 安全检查：`D20-sensitive-scan.md`
- 完整日志：`logs\D20`

## 遗留问题

- 次级原始 Prompt 附件未定位到；未影响主任务书最低范围，但不能声称覆盖缺失附件的额外要求。
- 当前超时值是基于保守预算的初始配置，尚无 DeepSeek 生产 SLA/时延分布支撑，不作为性能最优值。
- 未执行真实 DeepSeek API 烟雾测试，因此真实账户限流语义、Retry-After 实际格式及网络中间代理行为均为未验证；不产生真实费用也是本阶段的安全选择。
