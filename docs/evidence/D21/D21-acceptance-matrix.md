# D21 最终验收矩阵

| 编号 | 验收要求 | 实际结果 | 证据 | 状态 |
|---|---|---|---|---|
| D21-R01 | TraceId 在同步/流式请求中正确关联 | 有效 Header 复用；缺失自动生成；恶意值重新生成；REST 与 SSE 都关联同一 ID；访问日志关闭仍保留 | `D21-test-summary.md`、`D21-model-call-runtime-excerpt.log` | 通过 |
| D21-R02 | 调用记录脱敏且不泄露敏感内容 | 模型白名单日志无 Prompt/Completion/Key/异常 message；通用日志精确脱敏 message/content；Authorization/Cookie 掩码 | `D21-sensitive-scan.md`、observability/access-log 测试 | 通过 |
| D21-R03 | 延迟口径正确，成功/失败/取消均记录 | 模型耗时为单调时钟；HTTP 延迟复用访问日志 elapsedMs；同步成功/错误、流式成功/错误/断流取消均只记一条终态 | `ModelCallObservabilityTest`、`StreamClientDisconnectIntegrationTest` | 通过 |
| D21-R04 | Usage 只记录实际可获得数值，缺失不伪造 | 完整/部分/缺失/显式 0/派生 total 均测试；EmptyUsage 不记 0；流式累计 usage 不重复求和 | `TokenUsageExtractorTest`、`ModelCallObservabilityTest` | 通过 |
| D21-R05 | 原有接口兼容，必要回归通过 | 最终 174 个测试，0 失败，0 错误，1 跳过；REST/SSE 契约测试通过 | `logs\D21\D21-full-regression-final.log` | 通过 |
| D21-R06 | Git Diff、敏感信息扫描和两份文档完成 | Git Diff/check/状态证据已归档；严格 Key 模式 0 命中；两份正式文档已输出 | `D21-git-diff.patch`、`D21-sensitive-scan.md`、正式文档 | 通过 |

## 未验证但不是失败

| 项目 | 状态 | 说明 |
|---|---|---|
| 真实 DeepSeek API 调用 | 未验证 | 本轮优先 Mock，未产生 API 费用 |
| 真实 Provider 一定返回 usage | 未验证 | 必须由实际响应决定，代码在缺失时不填值 |
| W3C 分布式 traceparent | 不适用/未启用 | 项目无 Micrometer Tracing 依赖；当前实现明确为本地关联 TraceId |
