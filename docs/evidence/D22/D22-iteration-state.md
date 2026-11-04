# D22 迭代状态

## 需求编号

- D22-R01 模型名、URL、Key、模式外置
- D22-R02 服务公开契约不依赖 DeepSeek 专有 DTO
- D22-R03 同步/流式调用兼容
- D22-R04 配置校验、Mock/真实模式及安全测试

## 修改文件

### 新增

- `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/config/DeepSeekChatModelConfiguration.java`
- `D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/config/AiPropertiesValidationTest.java`
- `D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/config/DeepSeekConfigurationBindingTest.java`
- `D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/ProviderIsolationTest.java`
- `D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/MockWithoutApiKeyTest.java`

### 修改

- `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/config/AiProperties.java`
- `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/config/AiConstants.java`
- `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/controller/ChatStreamController.java`
- `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/knowledge/KnowledgeChatOrchestrator.java`
- `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/service/AiChatService.java`
- `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/service/DeepSeekAiChatService.java`
- `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/service/MockAiChatService.java`
- `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/D22SpringAiApplication.java`
- `D22/D22_EMN_URLs_Keys_schemas/src/main/resources/application.yml`
- `D22/D22_EMN_URLs_Keys_schemas/src/main/resources/application-local.yml.example`
- `D22/D22_EMN_URLs_Keys_schemas/pom.xml`（仅测试环境变量）
- 相关既有测试

## 设计决定

- `app.ai.*` 是模型模式、模型名、URL、Key、温度与 reasoner 模型的唯一应用配置源。
- 禁用 DeepSeek 供应商自动配置。
- 仅在 deepseek 模式、且容器无其他 ChatModel 时构造 `DeepSeekChatModel`。
- Mock 模式不创建供应商 ChatModel，从结构上阻断外部请求。
- Controller 从 `AiProperties` 读取模型展示值，不再读取 vendor 属性。
- `AiChatService` 继续作为内部 Port，只使用项目内部类型。
- DeepSeek Options/DTO 仅在配置 Adapter 和 DeepSeek Service Adapter。
- 复用 D20/D21 的超时、重试、异常、Trace、脱敏和 usage。
- Key 不进入 toString，真实模式通过安全环境变量注入。

## 测试

- 配置/架构专项：26/26 通过。
- Mock 空 Key：1/1 通过。
- 最终全量：182 个测试，0 失败，0 错误，1 跳过。
- 打包成功。

完整命令与日志：`D22-test-summary.md`。

## 证据

- 索引：`D22-baseline-index.md`
- 依赖树：`D22-dependency-tree.log`
- Provider 引用扫描：`D22-provider-reference-scan.txt`
- 旧环境变量扫描：`D22-legacy-env-scan.txt`
- Git Diff：`D22-git-diff.patch`
- 安全扫描：`D22-sensitive-scan.md`

## Token 统计

- 真实模型请求数：0。
- 真实输入/缓存/输出 Token：未产生。
- Agent 平台 Token：不可观测，不估算。

## 遗留问题

- 未执行真实 DeepSeek API 烟雾测试。
- 当前只隔离 DeepSeek，未实现第二家 Provider。
