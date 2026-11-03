# D21 版本与源码证据

## 基线

- 采集日期：2026-11-03（Asia/Shanghai）
- 当前 Git HEAD：`10a3920bef10d4ef5856f1b58c87cf1fca4ea20f`
- 模块：`D21(pom)` -> `D21_TOKEN_ID_LOGS(jar)`
- JDK：21.0.7
- Maven：3.9.16
- Spring Boot：3.5.16
- Spring AI：1.1.8
- Spring Retry：2.0.13
- 服务端：Spring MVC/Tomcat

## Tracing 依赖核验

`D21-tracing-dependency-check.log` 未匹配到：

- `spring-boot-starter-actuator`
- `micrometer-tracing`
- `micrometer-tracing-bridge-*`

因此当前没有完整 Micrometer Tracing，也没有 W3C traceparent 传播。D21 使用项目原有 `X-Request-Id` 作为本地 request/correlation TraceId，并增加同值 `X-Trace-Id` 响应别名；不把它宣称为分布式 Trace。

## Spring AI Usage 事实

`D21-spring-ai-usage-javap.txt` 显示：

```text
Usage.getPromptTokens(): Integer
Usage.getCompletionTokens(): Integer
Usage.getTotalTokens(): Integer
ChatResponseMetadata.getUsage(): Usage
```

三个 token 字段都可能缺失。`ChatResponseMetadata` 默认使用 `EmptyUsage`，其表面值可能为 0；该 0 不是 Provider 明确返回，D21 显式识别为空缺失。

`DefaultUsage` 会在构造时计算 total。D21 优先读取 Provider native usage 的 total accessor；只有 prompt 与 completion 明确存在、total 无法确认时才自行相加并写入 `totalTokensDerived=true`。

## DeepSeek 1.1.8 流式 usage

`D21-deepseek-usage-flow-javap.txt` 显示：

- `DeepSeekChatModel.internalStream` 调用 `getDefaultUsage(DeepSeekApi.Usage)`。
- 该 `DefaultUsage` 保存 native `DeepSeekApi.Usage`。
- 流式路径使用 `UsageCalculator.getCumulativeUsage(...)` 将完成累计 usage附着到 `ChatResponse` metadata。

因此 D21 在流式 `ChatResponse` 流中保留最新 usage 快照，而不是对每个 chunk 的累计值再次求和。

## 实际调用链

```text
请求线程
  TraceIdFilter
    -> X-Request-Id / X-Trace-Id / MDC requestId
    -> Controller
    -> KnowledgeChatOrchestrator
    -> AiChatService
    -> ModelCallRecorder

同步：
ChatModel.call
  -> ChatResponse.metadata.usage
  -> MODEL_CALL terminal record

流式：
ChatClient.stream().chatResponse()
  -> 捕获请求线程 TraceId
  -> 每个 ChatResponse 更新最新 usage 快照 / 首个输出延迟
  -> SUCCESS / ERROR / CANCELLED 唯一终态
```

## 直接关联源码

- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/TraceIdFilter.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/LogMaskingUtils.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/CachedBodyAccessLogFilter.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/ControllerAccessLogAspect.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/service/DeepSeekAiChatService.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/service/MockAiChatService.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/observability/ModelCallRecorder.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/observability/ModelTokenUsage.java`
- `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/observability/TokenUsageExtractor.java`

## 未验证

- 未发起真实 DeepSeek API 调用。
- 未验证真实账户本轮响应必含 usage，也未验证生产代理对 `traceparent` 的处理。
- 当前 TraceId 是本地关联 ID，不是跨服务 W3C Trace。
