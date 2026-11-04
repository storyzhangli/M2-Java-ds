# D22 测试汇总

## 执行摘要

| 阶段 | 命令 | 结果 | 日志 |
|---|---|---|---|
| 基线全量 | `mvn -f .\pom.xml test` | 构建成功 | `logs\D22\D22-baseline-full-test.log` |
| 配置/架构专项 | `mvn -f .\pom.xml "-Dtest=ApplicationContextLoadTest,AiPropertiesValidationTest,DeepSeekConfigurationBindingTest,ProviderIsolationTest,DeepSeekKeyProtectionTest,ModelCallObservabilityTest,SpringAiChatClientBehaviorTest,MockNoExternalApiTest,MockWithoutApiKeyTest" test` | 26/26 通过 | `logs\D22\D22-config-architecture-tests-final.log` |
| Mock 空 Key | `mvn -f .\pom.xml -Dtest=MockWithoutApiKeyTest test` | 1/1 通过 | `logs\D22\D22-mock-no-key-test-01.log` |
| 失败修复验证 | `mvn -f .\pom.xml "-Dtest=StreamPreStartFailureTest,DeepSeekConfigurationBindingTest,AiPropertiesValidationTest" test` | 通过 | `logs\D22\D22-provider-condition-tests-02.log` |
| 最终全量回归 | `mvn -f .\pom.xml test` | 182 个测试，0 失败，0 错误，1 跳过 | `logs\D22\D22-full-regression-02.log` |
| 打包 | `mvn -f .\pom.xml -DskipTests package` | 成功 | `logs\D22\D22-package-01.log` |

唯一跳过项为默认禁用的 `DeepSeekLiveSmokeTest`，本轮未发起真实 DeepSeek 请求。

## 需求覆盖

| 目标 | 实际测试 | 结果 |
|---|---|---|
| 默认 Mock 空 Key 启动 | `MockWithoutApiKeyTest` | 通过 |
| Mock 不装配供应商 ChatModel | `ApplicationContextLoadTest`、`MockWithoutApiKeyTest` | 通过 |
| 外部模型名进入真实 Bean | `DeepSeekConfigurationBindingTest` | 通过 |
| 外部 Base URL/Key/温度进入配置绑定 | `DeepSeekConfigurationBindingTest` | 通过 |
| 未知模式/非法 URL/空模型校验 | `AiPropertiesValidationTest` | 通过 |
| 真实模式空 Key 调用前失败 | `DeepSeekKeyProtectionTest` | 通过 |
| API Key 不进入 toString | `DeepSeekKeyProtectionTest.apiKeyMustNotAppearInPropertiesToString` | 通过 |
| 服务公开契约无 DeepSeek 类型 | `ProviderIsolationTest` 反射扫描 | 通过 |
| Provider 专有 import 只在 Adapter/Config | `ProviderIsolationTest` 源码扫描 | 通过 |
| Controller/Service Port 无 vendor config | `ProviderIsolationTest` 源码扫描 | 通过 |
| 同步/流式/异常/超时/Trace/usage 回归 | D16–D21 既有测试 + 最终全量 | 通过 |
| 旧订阅前错误契约 | `StreamPreStartFailureTest`：空 Key + Mock ChatModel，仍返回 5001 | 通过 |

## 失败与修复

首次全量回归出现 1 个失败：旧的 `StreamPreStartFailureTest` 使用非空无效测试 Key，导致服务通过 Key 非空判断并进入模型流。

修复方式：

- 自定义 Provider Bean 增加 `@ConditionalOnMissingBean(ChatModel.class)`。
- 该测试注入 Mock `ChatModel`，使供应商 Bean 不构造。
- 测试配置使用空 Key，Service 在任何模型交互前继续返回原有 5001。
- 未删除断言、未跳过测试、未放宽 Provider 隔离要求。

## 未验证

- 未调用真实 DeepSeek API。
- 未验证真实账户的模型名兼容、Base URL 网关行为和费用。
