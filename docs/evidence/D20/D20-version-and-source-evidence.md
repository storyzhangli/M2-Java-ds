# D20 版本、依赖与调用链证据

## 基线

- 采集日期：2026-11-02（Asia/Shanghai）
- Git HEAD：`a78b440 D20 项目迭代任务书 DeepSeekDo`
- 根模块：`D20`，`pom` 聚合；子模块：`D20_UMIEM`，`jar`
- JDK：21.0.7
- Maven：3.9.16
- Spring Boot：3.5.16（根 POM 父工程）
- Spring AI BOM / DeepSeek Starter：1.1.8
- Spring Retry：2.0.13（Spring AI 传递依赖）
- Reactor Netty HTTP：1.2.18（D20 显式补充的 WebClient 连接器）
- 服务端：Spring MVC + Tomcat；模型流式客户端：WebClient + Reactor Netty

实际依赖证据见 `D20-dependency-tree-filtered.log`。

## Spring AI 1.1.8 默认重试配置

本地 1.1.8 JAR 的配置元数据记录：

| 属性 | 默认值/行为 |
|---|---|
| `spring.ai.retry.max-attempts` | `10` |
| `spring.ai.retry.backoff.initial-interval` | `2000ms` |
| `spring.ai.retry.backoff.multiplier` | `5` |
| `spring.ai.retry.backoff.max-interval` | 类默认值 `180000ms` |
| `spring.ai.retry.on-client-errors` | `false` |
| `spring.ai.retry.on-http-codes` | 空集合，未默认指定 |
| `spring.ai.retry.exclude-on-http-codes` | 空集合，未默认指定 |

依据：

- `D20-retry-configuration-metadata.json`
- `D20-retry-properties-defaults-javap.txt`

## DeepSeek 自动配置接线

`D20-deepseek-autoconfiguration-javap.txt` 显示，1.1.8 的
`DeepSeekChatAutoConfiguration#deepSeekChatModel(...)` 接收 `RetryTemplate` 与
`ResponseErrorHandler`，并通过 Builder 把它们注入 `DeepSeekChatModel` 和
`DeepSeekApi`。因此 D20 自定义的两个 Bean 会进入实际调用链。

`D20-deepseek-chatmodel-javap.txt` 进一步显示：

- 同步 `internalCall` 从字段 `retryTemplate` 调用 `RetryTemplate.execute(...)`。
- 流式 `internalStream` 直接构建 Reactor 流，没有调用该 `RetryTemplate`。

## 实际调用链

```text
同步：
ChatController
 -> KnowledgeChatOrchestrator
 -> DeepSeekAiChatService.chat()
 -> DeepSeekChatModel.call()
 -> DeepSeekChatModel.internalCall()
 -> D20 RetryTemplate
 -> DeepSeekApi.chatCompletionEntity()
 -> RestClient

流式：
ChatStreamController
 -> KnowledgeChatOrchestrator
 -> DeepSeekAiChatService.streamChat()
 -> ChatClient.stream()
 -> DeepSeekChatModel.stream()
 -> DeepSeekChatModel.internalStream()
 -> DeepSeekApi.chatCompletionStream()
 -> WebClient
 -> SseEmitter
```

## 直接关联文件

- `D20/D20_UMIEM/pom.xml`
- `D20/D20_UMIEM/src/main/resources/application.yml`
- `D20/D20_UMIEM/src/main/java/com/zl/d20/config/ModelCallProperties.java`
- `D20/D20_UMIEM/src/main/java/com/zl/d20/config/ModelCallConfiguration.java`
- `D20/D20_UMIEM/src/main/java/com/zl/d20/model/`
- `D20/D20_UMIEM/src/main/java/com/zl/d20/common/GlobalExceptionHandler.java`
- `D20/D20_UMIEM/src/main/java/com/zl/d20/controller/ChatStreamController.java`

次级附件《D20-统一模型调用异常映射、超时配置与有限重试-prompt 指令》未在本地资料中定位到，D20 以主任务书第 1 节为最低功能边界，未臆造缺失附件内容。
