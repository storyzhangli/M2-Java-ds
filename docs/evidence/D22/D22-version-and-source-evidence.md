# D22 版本与源码证据

## 实际基线

- 日期：2026-11-04（Asia/Shanghai）
- Git HEAD：`c5b73e7875f3de12b798a79a8b0df2d838e4389f`
- 根模块：`D22`（POM）
- 子模块：`D22_EMN_URLs_Keys_schemas`（JAR）
- JDK：21.0.7
- Maven：3.9.16
- Spring Boot：3.5.16
- Spring AI：1.1.8

依赖树见 `D22-dependency-tree.log`。

## Spring AI 1.1.8 事实

本地 `DeepSeekChatAutoConfiguration` 字节码显示：

- 自动配置从 `spring.ai.deepseek.*` 读取 Key/Base URL/Options。
- 创建 API 前执行 `Assert.hasText(apiKey, "DeepSeek API key must be set")`。
- 空 Key 会使供应商自动配置失败。
- 自动配置最终通过 `DeepSeekChatModel.builder()` 注入 DeepSeekApi、Options、RetryTemplate、ToolCallingManager 和 ObservationRegistry。

因此单纯将真正空 Key 映射到 vendor 自动配置无法满足“Mock 无 Key 启动”。

## 项目映射

D22 显式禁用 `DeepSeekChatAutoConfiguration`，并在 `DeepSeekChatModelConfiguration` 中：

- 仅在 `app.ai.mode=deepseek` 装配。
- 仅在容器没有其他 `ChatModel` 时装配。
- 使用 `AiProperties` 的 model/baseUrl/apiKey/temperature。
- 注入既有 `RetryTemplate` 与 `ResponseErrorHandler`。
- 复用 Boot RestClient/WebClient Builder、ToolCallingManager 与 ObservationRegistry。
- 构造 DeepSeekApi 和 DeepSeekChatModel。

这保证 `app.ai.*` 是唯一应用来源，而不是只写 YAML 未接 Bean。

## 配置映射

| 环境变量 | 应用属性 | 用途 |
|---|---|---|
| `AI_MODE` | `app.ai.mode` | mock / deepseek |
| `AI_MODEL` | `app.ai.model` | 默认模型名 |
| `AI_BASE_URL` | `app.ai.base-url` | 模型 Base URL |
| `AI_API_KEY` | `app.ai.api-key` | Provider Key |
| `AI_REASONER_MODEL` | `app.ai.thinking.reasoner-model` | 思考模型 |
| `AI_TEMPERATURE` | `app.ai.temperature` | 采样温度 |

扫描结果：

- `D22-legacy-env-scan.txt`：旧 `DEEPSEEK_*` 配置变量 0 行。
- `D22-provider-reference-scan.txt`：Provider import/类型使用仅出现在配置 Adapter 与 DeepSeek Service Adapter。

## 供应商隔离

`ProviderIsolationTest` 验证：

- `AiChatService` 公开签名不出现 `org.springframework.ai.deepseek` 类型。
- Controller 和应用 DTO 公开方法不出现 Provider 类型。
- 主源码中 `org.springframework.ai.deepseek` import 仅允许：
  - `config/DeepSeekChatModelConfiguration.java`
  - `service/DeepSeekAiChatService.java`
- Controller 与 `AiChatService` 不引用 `spring.ai.deepseek` 配置。

## 配置优先级

使用 Spring Boot 标准优先级：

```text
命令行参数
 > 操作系统环境变量
 > 外部 Profile 配置
 > application.yml
 > Java 默认值
```

配置在启动时绑定，不支持热切换。

## 安全事实

- `application.yml` 中 `AI_API_KEY` 默认为空。
- `.example` 不含真实或伪造 Key。
- `AiProperties.apiKey` 使用 `@ToString.Exclude`。
- 测试 JVM 使用显式无效值，且不会读取宿主真实 Key。
