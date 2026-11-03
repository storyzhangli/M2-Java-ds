# D21 迭代状态

## 需求编号

- D21-R01 TraceId 贯穿聊天请求
- D21-R02 脱敏模型调用记录
- D21-R03 请求/模型延迟记录
- D21-R04 实际 Token usage 提取及缺失标记

## 修改文件

### 新增

- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/observability/ModelTokenUsage.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/observability/TokenUsageExtractor.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/observability/ModelCallRecorder.java`
- `D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/observability/TokenUsageExtractorTest.java`
- `D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/observability/ModelCallObservabilityTest.java`
- `D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/logging/TraceIdWithoutAccessLogTest.java`

### 修改

- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/TraceIdFilter.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/AccessLogProperties.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/AccessLogAutoConfiguration.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/LogMaskingUtils.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/CachedBodyAccessLogFilter.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/ControllerAccessLogAspect.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/service/DeepSeekAiChatService.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/service/MockAiChatService.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/resources/application.yml`
- `D21/D21_TOKEN_ID_LOGS/src/main/resources/logback-spring.xml`
- `D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/logging/AccessLogIntegrationTest.java`
- `D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/logging/AccessLogMaskingTest.java`
- `D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/stream/ChatStreamIntegrationTest.java`
- `D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/stream/StreamClientDisconnectIntegrationTest.java`

未修改 POM，未升级/降级依赖，未新增数据库表、Actuator、Micrometer Tracing Bridge 或监控平台。

## 设计决定

- 复用现有 `X-Request-Id`/MDC 作为本地关联 TraceId。
- 严格校验外部 ID：1–64 位、字母或数字开头、仅字母数字下划线中划线；非法值重新生成。
- 回写同值 `X-Request-Id` 与 `X-Trace-Id`，保持 SSE `requestId` 契约不变。
- TraceId 在请求线程捕获；Reactor 异步生命周期使用捕获字符串，不依赖异步线程 MDC。
- 访问日志关闭时，TraceId 与 `MODEL_CALL` 记录仍保留。
- 使用单一 `MODEL_CALL` JSON Logger，白名单字段、原子终态守卫。
- Usage 只读 `ChatResponse.metadata.usage`；识别 `EmptyUsage`，缺失字段不写值。
- 流式 usage 保留最新累计快照，不累加重复 chunk。
- Mock 明确标记 `NOT_SUPPORTED`。
- Prompt/Completion/推理内容在通用日志中按精确字段名脱敏。

## 测试

- 最终全量：174 个测试，0 失败，0 错误，1 跳过。
- 专项：29/29 通过。
- Usage 专项：11/11 通过。
- Mock/真实容器断流取消：通过。
- 完整命令与日志见 `D21-test-summary.md`。

## 证据

- 基线索引：`D21-baseline-index.md`
- 依赖与源码事实：`D21-version-and-source-evidence.md`
- 依赖树：`D21-dependency-tree.log`
- Tracing 依赖核验：`D21-tracing-dependency-check.log`
- Usage API：`D21-spring-ai-usage-javap.txt`
- DeepSeek usage 路径：`D21-deepseek-usage-flow-javap.txt`
- 运行摘录：`D21-model-call-runtime-excerpt.log`
- 测试摘要：`D21-test-summary.md`
- Git Diff：`D21-git-diff.patch`
- 敏感扫描：`D21-sensitive-scan.md`

## Token 统计

- 真实模型请求数：0。
- 真实输入/缓存/输出 Token：未产生。
- Agent 平台 Token：平台不可观测，不估算。
- 测试中 usage 值为测试夹具，仅用于验证字段映射，不代表真实模型消费。

## 遗留问题

- 未启用 W3C 分布式 tracing；当前 TraceId 仅保证单服务请求与模型调用关联。
- 未执行真实 DeepSeek 在线烟雾测试，真实 Provider usage 是否存在仍取决于调用响应和账户/协议。
- Async Reactor 普通业务日志的 MDC 为空，但结构化 `MODEL_CALL.traceId` 已显式捕获；若未来引入 Micrometer Tracing，应迁移到其 Context Propagation。
