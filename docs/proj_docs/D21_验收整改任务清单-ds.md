# D21 验收整改任务清单

- 日期：2026-11-03
- 仓库：<https://github.com/storyzhangli/M2-Java-ds>
- 验收提交：
  - 知识笔记：`f37a4ea116b58a930bd4443e0474f08a1789ba3b`
  - 项目基础骨架：`f7b31e5b513df1307687eecbd447a7e3d37b773b`
  - 项目更改文件：`dc66e5b6b2125ea58f9ba812b9fb7759314181fb`
  - 项目新增文件：`7bb9da70ebcff905992479e66b0e304ce2d54cfa`
  - 口述材料：`b874cdc78e9273a5cb77fe31f878cbfd2af3e560`
- 当前验收结论：主体功能已实现；存在日志数据暴露风险、两项失败分类遗留、文档口径不一致和证据归档不完整，整改完成前不视为最终关闭。

## 一、P1：通用访问日志没有做到字段白名单，动态变量可能泄露

**涉及文件**

- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/AccessLogProperties.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/LogMaskingUtils.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/CachedBodyAccessLogFilter.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/ControllerAccessLogAspect.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/dto/ChatRequest.java`

**问题与依据**

`includeRequestBody`、`includeResponseBody`、`includeMethodArgs` 默认均为 `true`。过滤器会记录请求/响应正文，Controller AOP 会把整个请求 DTO 序列化后再按字段名称脱敏。`ChatRequest` 中存在 `Map<String,Object> variables`，其键名由模板变量定义决定；当前脱敏主要依赖已知字段名/关键字，未命中的业务字段和值仍可能进入访问日志。`prepareObject()` 是“完整对象序列化后黑名单遮盖”，不是字段白名单。

D21 对 `MODEL_CALL` 单独使用字段白名单是正确方向，但不能据此推断 HTTP 访问日志和 Controller AOP 也同样安全。

**整改步骤**

1. 将请求正文、响应正文、Controller 方法参数日志默认关闭；生产安全默认值不得依赖每个 DTO 字段都被列入敏感关键字。
2. 将访问日志改为固定字段白名单，只保留请求方法、路由模板、HTTP 状态、耗时、requestId、内容长度、场景枚举等低风险元数据。
3. 如确实需要诊断变量，只记录变量数量、变量名白名单或类型摘要；不记录 `variables` 的原始值，不序列化整个 ChatRequest。
4. 对请求正文日志保留显式 opt-in，并增加独立开关、权限/环境限制和审计说明；默认关闭必须在配置文件与 Java 默认值两处一致。
5. 日志失败分支不要输出 `loggingFailure.getMessage()`，改记异常类型或固定事件代码，避免异常消息本身包含敏感值。

**必须补充的测试**

- 发送 ChatRequest，`variables` 中放入唯一 canary 值，例如 `DO_NOT_LOG_D21_VARIABLE_9f31`，键名使用 `questionText`、`serialNumber`、`deviceDescription` 等非敏感字样，并嵌套一层对象。
- 分别捕获 `HTTP_REQUEST`、`HTTP_RESPONSE`、`CONTROLLER_ACCESS`，断言 canary、变量原值和完整正文均不出现。
- 验证默认配置不输出请求体、响应体和方法参数；显式开启诊断模式时也只能输出经过白名单筛选的摘要。
- 覆盖正常、异常、参数校验失败和日志序列化失败路径，确保业务响应不受日志组件影响。

**最终验收标准**

- 默认配置下未知字段、动态 Map、嵌套业务数据不会进入访问日志。
- 只允许固定低风险字段进入通用访问日志；模型调用事件继续使用显式白名单。
- 上述 canary 测试全部通过，并提交测试日志或 CI 产物。

## 二、P1：DNS 与 TLS 异常仍误分类为认证失败（D20 R01 未关闭）

**涉及文件**

- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/model/ModelFailureClassifier.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/model/ModelFailureCategory.java`
- `D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/model/ModelFailureClassifierTest.java`

**问题与依据**

`fromNetworkCause()` 中 `UnknownHostException` 与 `SSLHandshakeException` 当前都返回 `AUTHENTICATION_FAILED`。DNS 解析失败和 TLS 握手失败不是 API Key 鉴权失败；被错误分类后会影响业务错误码、告警分组和排障方向。

**整改步骤**

1. 为 DNS 解析和 TLS 握手定义独立网络失败分类，或在现有 `NETWORK_IO` 下保留明确子类型；不要复用 `AUTHENTICATION_FAILED`。
2. 定义这些分类是否可重试，必须根据故障语义决定，不可仅因属于网络异常就全部重试。
3. 对外业务码映射到网络/上游不可用类别；日志只保留异常类型与分类，不记录异常原始 message。

**必须补充的测试**

- `UnknownHostException` -> DNS/网络分类，不得为 `AUTHENTICATION_FAILED`。
- `SSLHandshakeException` -> TLS/网络分类，不得为 `AUTHENTICATION_FAILED`。
- `Http 401` 与占位/缺失 API Key 仍为认证失败，避免修复时破坏真实鉴权分类。
- 覆盖分类后的重试许可和 `ResultCode` 映射。

**最终验收标准**

DNS、TLS、401 三类故障在分类、业务码、重试行为和测试断言中明确区分。

## 三、P1：DEADLINE_EXCEEDED 未映射业务码（D20 R02 未关闭）

**涉及文件**

- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/model/ModelFailureClassifier.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/model/ModelFailureCategory.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/common/GlobalExceptionHandler.java`
- `D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/model/ModelFailureClassifierTest.java`

**问题与依据**

`ModelFailureCategory` 已包含 `DEADLINE_EXCEEDED`，`classify(TimeoutException)` 也会产生该分类；但 `resultCode()` 的 switch 未包含它，最终走 `default -> SYSTEM_ERROR`。因此总期限超时与普通系统错误无法按预期区分。

**整改步骤**

1. 明确 `DEADLINE_EXCEEDED` 对外映射为 `AI_UNAVAILABLE`（或项目明确约定的等价服务不可用业务码）。
2. 同步与流式超时分别核对异常类型，保证都被识别为超时，而不是只修同步 `TimeoutException`。
3. 保持 API 对外提示不暴露上游原始异常内容。

**必须补充的测试**

- `TimeoutException` / 项目实际 deadline 异常 -> `DEADLINE_EXCEEDED` -> `AI_UNAVAILABLE`。
- 同步调用总 deadline 超时的业务码测试。
- 流式总时限超时的 SSE error 与业务分类测试。
- 401 仍返回配置/鉴权业务码；普通未知异常仍使用系统兜底码。

**最终验收标准**

同步与流式总期限超时在内部分类、外部业务码和测试结果一致。

## 四、P2：Retry TimeoutPolicy 是重试预算，不等于硬取消 deadline（D20 R03）

**涉及文件**

- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/config/ModelCallConfiguration.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/config/ModelCallProperties.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/resources/application.yml`
- `D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/model/ModelRetryPolicyTest.java`
- `D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/model/ModelHttpClientTimeoutTest.java`

**问题与依据**

`TimeoutRetryPolicy(totalDeadline)` 放在 `RetryTemplate` 的复合重试策略中，主要限制是否继续下一次重试；它本身不保证取消已经执行中的 HTTP 请求。当前另外配置的连接超时、响应超时是独立约束，不能把三者合称为可中断的单次调用硬 deadline。

**整改步骤**

1. 若本轮只需要限制重试预算，将配置和文档中的 `totalDeadline` 语义改为 retry budget / retry window，并明确在途请求由 connect/read/response timeout 管理。
2. 若业务要求硬 deadline，需设计调用级取消机制，确保到期能取消底层 HTTP 请求和订阅；不可只靠 `TimeoutRetryPolicy` 命名实现。
3. 分开记录“重试预算耗尽”和“在途请求超时/取消”。

**必须补充的测试**

- 慢响应在途期间超过重试预算，验证实际行为并记录是否仍等待当前请求结束。
- 连接超时、读取超时、重试间退避分别测试。
- 若增加硬 deadline，测试到期后底层请求/流订阅确实取消，而不只是调用方收到超时异常。

**最终验收标准**

配置名称、文档描述和实际取消行为一致；没有硬取消能力时不得宣称硬 deadline。

## 五、P2：logback 文件行不是纯 JSON Lines

**涉及文件**

- `D21/D21_TOKEN_ID_LOGS/src/main/resources/logback-spring.xml`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/observability/ModelCallRecorder.java`
- `docs/proj_docs/D21-聊天调用可观测性技术说明与实践指南.md`

**问题与依据**

`ModelCallRecorder` 将 JSON 写入日志 message，但当前文件 appender 的 `FILE_PATTERN` 还在 JSON 前添加时间、级别、线程、traceId 和 logger 文本前缀。因此实际文件每行不是可直接按 JSON 解析的 NDJSON/JSON Lines；现有 ListAppender 测试读取的是 `getFormattedMessage()`，没有验证最终文件行格式。

**整改步骤**

1. 使用 Spring Boot 3.5 支持的结构化 JSON Encoder，或为 `MODEL_CALL` 配置独立 JSON appender。
2. 若暂不调整 Encoder，则将文档准确描述为“普通日志行中的 JSON message”，不要称为纯 JSON Lines。
3. 日志 JSON 的字段由单一序列化层负责，避免 message 内 JSON 与外层 Encoder 重复包装。

**必须补充的测试**

- 启动真实 Logback 配置写入临时文件，逐行读取文件原始字节。
- 对每一条 `MODEL_CALL` 行直接执行 JSON 解析；验证 event、traceId、status、usage 字段。
- 验证中文、引号、换行输入不会破坏 JSON 行结构。

**最终验收标准**

若文档称 JSON Lines，则实际文件每行必须是独立合法 JSON；否则更正文档术语。

## 六、P2：知识笔记与 D21 实际事件字段/枚举不一致

**涉及文件**

- `docs/proj_docs/D21-知识笔记-Token用量调用耗时请求ID结构化日志与敏感日志风险-GPT.md`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/observability/ModelCallRecorder.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/observability/ModelTokenUsage.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/observability/TokenUsageExtractor.java`

**问题与依据**

笔记示例字段使用 `inputTokens` / `outputTokens`、`durationMs`，实际事件使用 `promptTokens` / `completionTokens`、`elapsedMs`；笔记给出的 `usageSource` 枚举包含 `PROVIDER_RESPONSE_METADATA`、`ESTIMATED`、`MOCK`、`UNKNOWN`，实际代码使用 `PROVIDER_METADATA`、`DERIVED`、`NONE`，同时通过 `usageStatus` 区分 `AVAILABLE`、`PARTIAL`、`NOT_RETURNED`、`NOT_SUPPORTED`。笔记需清楚区分通用设计示例与 D21 实际落地字段，避免学习者照搬后与日志查询不匹配。

**整改步骤**

1. 在笔记增加“通用字段与 D21 实际字段映射表”。
2. 明确 `promptTokens` 对应通用 `inputTokens`，`completionTokens` 对应通用 `outputTokens`，`elapsedMs` 对应通用 `durationMs`。
3. 解释 `usageStatus` 与 `usageSource` 是两个正交字段；缺失值在 D21 中为 `NOT_RETURNED + NONE`，Mock 为 `NOT_SUPPORTED + NONE`；只有显式供应商字段才用 `PROVIDER_METADATA`，派生 total 使用 `DERIVED` 并置 `totalTokensDerived=true`。
4. 为日志安全章节增加 OWASP Logging Cheat Sheet，覆盖敏感数据排除、日志注入与安全测试依据。
5. 将示例中的 Mock 数值始终保留 `sampleType=synthetic` 标记，不得作为真实供应商调用记录展示。

**必须补充的测试/核对**

- 从一条 D21 实际 `MODEL_CALL` 样本逐字段对照笔记映射表。
- 核对完整、部分、缺失、Mock、派生 total 五种情形的字段和值。
- 检查全文示例中的枚举名与字段名，不保留过期值。

**最终验收标准**

笔记的抽象示例、D21 真实字段、Mock 样本和 Provider 返回值边界清晰且互相一致。

## 七、P2：D21 测试结果缺少仓库内可复核的原始日志

**涉及文件**

- `docs/evidence/D21/D21-test-summary.md`
- `docs/evidence/D21/D21-runtime-evidence.md`
- `docs/evidence/D21/D21-acceptance-matrix.md`
- `logs/D21/`（当前验收提交中未看到汇总所引用的 Maven 原始日志）

**问题与依据**

证据摘要写明最终全量 174 tests、0 failures、0 errors、1 skipped，并引用 `logs/D21/D21-full-regression-final.log` 等日志；但所提交的 D21 文件清单中没有这些原始日志。GitHub 当前提交的 combined status 为空，也没有关联的 PR workflow runs。因此目前只能确认仓库提交了测试摘要，无法从本次提交独立复核实际 Maven 输出。

**整改步骤**

1. 提交脱敏后的最终全量 Maven 日志、Surefire XML（至少 `TEST-*.xml` 的汇总）或 GitHub Actions artifact。
2. 日志至少包含 Maven 命令、JDK/Maven/Spring 版本、tests/failures/errors/skipped、BUILD SUCCESS/FAILURE。
3. 将证据路径改成仓库中真实存在的路径；没有保留的日志不要在摘要中引用为已归档文件。
4. 对 DeepSeek Live Smoke Test 的跳过原因继续明确标记“真实 Provider 未验证”。

**必须补充的测试/核对**

- 从原始日志或 Surefire XML 重新计算测试数和失败数，核对 174/0/0/1。
- 检查被跳过测试确为 Live Smoke Test，且不会误把未执行的在线验证计入通过数。
- 复核 Maven package 日志与 JAR SHA-256 是否匹配。

**最终验收标准**

任意审查者可从仓库证据或 Actions artifact 复核最终测试数量、失败数、跳过项与打包成功状态。

## 八、P2：口述材料有技术表述需要修订，且缺少实际录音

**涉及文件**

- `docs/proj_docs/D21-费曼口述-AI 应用的最小可观测性-DB.md`

**问题与依据**

本次提交的是 38 题问答稿，不是音频。稿件中仍有几处容易形成错误认知的表达：

1. 把可观测性描述为“不用改代码、不用重新发布”即可判断内部状态，忽略了事先必须有合适的埋点和遥测数据。
2. 把模型调用通常占请求耗时 70% 以上作为普遍事实，缺乏项目数据依据；并且并发 Span 的耗时不能简单用总耗时减去模型 Span 耗时。
3. 把 Spring AI 1.1.x 描述为启用 Micrometer/OTel 后即可自动得到所有 Provider 的指标和 Span，忽略具体 Provider 自动观测支持范围及项目是否存在 Tracer 实现。
4. 将 TraceId 查全链路 Span 的表述套用到 D21；本轮实际只实现本地 request/correlation ID，没有 W3C `traceparent` 和 Span 树。
5. 把首个非空流式输出块称作首 Token；除非底层协议明确提供 Token 边界，否则应称为 TTFT 代理指标或 first-chunk latency。

**整改步骤**

1. 修订以上问题，明确“可观测性依赖预先埋点”“指标比例必须来自真实基线”“TraceId 与 requestId 语义边界”“Provider 自动观测能力因实现而异”。
2. 将 first token 与 first non-empty chunk 区分；当前 D21 实现字段为 `firstChunkLatencyMs`。
3. 录制至少 3 分钟的真实口述音频（mp3/m4a/wav 均可），内容应脱离照读稿，能解释：日志/指标/追踪区别、requestId 不等于 W3C TraceId、usage 缺失不造值、Prompt/变量脱敏风险、D21 自身的实现边界。
4. 提交可访问的录音文件或可核验附件；Markdown 讲稿继续作为辅助材料，不替代音频。

**必须补充的验收证据**

- 修订后的口述稿 commit。
- 至少 3 分钟真实录音文件。
- 一段简短口述提纲或转写，用于核对是否准确解释关键概念；不得用转写文本冒充录音。

**最终验收标准**

口述稿中无上述关键技术误导；有可播放、时长达到要求的真实录音，且能清楚解释项目实际做到与未做到的边界。

## 九、复验优先顺序

1. 先关闭 P1：通用日志白名单风险、DNS/TLS 错分类、deadline 业务码遗漏。
2. 再关闭 P2：retry budget 语义、JSON Lines 格式、笔记字段统一、原始测试证据归档、口述稿修订与录音。
3. 每项整改必须给出：代码/文档 commit、对应测试名与结果、原始日志或可核验产物、Git diff。
4. 未补真实 DeepSeek 调用时，仍明确标记 Provider 在线行为未验证；未接入 Tracing 时，仍明确标记 W3C 分布式追踪未实现。

## 十、复验时不得接受的替代证据

- 只提交任务书、测试计划或 AI 自述，不等于测试通过。
- 只提交测试摘要而没有可复核原始日志/CI artifact，不等于独立验证。
- 只提交口述 Markdown，不等于真实口述录音。
- 只生成 `X-Trace-Id` 响应头，不等于建立 W3C 分布式 Trace/Span。
- 只在 JSON DTO 中遮盖已知敏感字段名，不等于未知变量值已安全。
