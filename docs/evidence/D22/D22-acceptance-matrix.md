# D22 最终验收矩阵

| 编号 | 验收要求 | 实际结果 | 证据 | 状态 |
|---|---|---|---|---|
| D22-R01 | 模型名、Base URL、Key、模式外置且优先级清晰 | `app.ai.*` 为唯一应用来源；`AI_*` 环境变量映射；启动时绑定不支持热切换 | `AiPropertiesValidationTest`、`DeepSeekConfigurationBindingTest`、`application.yml` | 通过 |
| D22-R02 | 服务接口和业务层不依赖 DeepSeek 专有 DTO | 反射检查公开方法；源码 import 只允许配置 Adapter 与 DeepSeek Service Adapter | `ProviderIsolationTest`、`D22-provider-reference-scan.txt` | 通过 |
| D22-R03 | 同步/流式接口及现有调用行为兼容 | REST/SSE、异常、超时、Trace、usage 回归通过 | `logs\D22\D22-full-regression-02.log` | 通过 |
| D22-R04 | Mock/真实模式校验和密钥保护有效 | 空 Key Mock 启动；真实 Bean 使用测试外部值装配；空 Key 调用前失败；Key 不在 toString | `MockWithoutApiKeyTest`、`DeepSeekConfigurationBindingTest`、`DeepSeekKeyProtectionTest` | 通过 |
| D22-R05 | 构建及必要测试真实执行 | 182 个测试，0 失败，0 错误，1 跳过；打包成功 | `D22-test-summary.md`、`D22-package-01.log` | 通过 |
| D22-R06 | Git 差异和两份正式文档完整 | Git Diff/状态已归档；两份文档已生成 | `D22-git-diff.patch`、正式文档 | 通过 |

## 未验证

| 项目 | 状态 | 说明 |
|---|---|---|
| 真实 DeepSeek 在线调用 | 未验证 | 本轮不产生真实费用 |
| 真实账户对自定义模型名/URL 的兼容性 | 未验证 | 仅验证配置进入 Bean |
| 第二家 Provider | 未实现 | D22 不要求 |
