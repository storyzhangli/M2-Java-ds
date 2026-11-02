# D20 最终验收矩阵

| 验收项 | 通过条件 | 状态 | 实际证据 |
|---|---|---|---|
| A01 现状索引 | 有扫描摘要、Git HEAD、POM/依赖摘要、相关文件索引 | 通过 | `D20-baseline-index.md`，索引脚本为 `docs/temp_docs/D20/D20-index-scan.ps1` |
| A02 版本核验 | 有 Spring AI 1.1.8 / Boot 3.5.16 依据和实际依赖证据 | 通过 | `D20-version-and-source-evidence.md`、`D20-dependency-tree-filtered.log`、元数据与 Javap 文件 |
| A03 异常映射 | 同步/流式错误归类一致且兼容现有对外契约 | 通过 | `ModelFailureClassifier`、`GlobalExceptionHandler`、流式错误测试、全量回归 |
| A04 超时 | 配置外部化、范围明确、实际客户端生效并有测试 | 通过 | `ModelCallPropertiesValidationTest`、`ModelHttpClientTimeoutTest` |
| A05 重试边界 | 仅批准的可恢复错误重试，尝试次数/总 Deadline 有上限 | 通过 | `ModelHttpRetryIntegrationTest`、`ModelRetryPolicyTest` |
| A06 流式安全 | 已输出片段后不重放；客户端取消不重试 | 通过 | `ChatStreamErrorEventTest`、`StreamClientDisconnectIntegrationTest`、`ChatStreamTotalDeadlineTest` |
| A07 测试 | 第 7 节适用测试实际执行，报告与日志路径可追溯 | 通过 | `D20-test-summary.md`；专项 28/28；全量 161 项 |
| A08 兼容性 | 旧同步接口契约及既有测试无非预期回归 | 通过 | `D20-full-regression-01.log`，161 项 0 失败 |
| A09 安全与差异 | 无密钥/敏感数据泄露，Git Diff 无无关修改 | 通过 | `D20-sensitive-scan.md` 0 命中；`git diff --check` 退出码 0；变更限定在 D20 与指定文档/证据目录 |
| A10 文档 | 两份正式文档均由结构化记录和真实证据生成 | 通过 | `docs/proj_docs/D20-项目迭代全量信息与操作.md`、`docs/proj_docs/D20-模型异常超时与有限重试技术说明及实践指南.md` |

## 未验证项

| 项目 | 状态 | 原因 |
|---|---|---|
| 真实 DeepSeek API 在线烟雾测试 | 未验证 | 本轮优先模拟，未授权产生真实 Token 费用 |
| 真实供应商限流正文与 `Retry-After` 的实际格式 | 未验证 | 仅用本地 Mock HTTP Server 验证标准语义 |
| 生产代理/网关是否额外重试 | 未验证 | 外部部署状态不在本轮工作区范围 |

未验证项不纳入上述验收项的“通过”依据。
