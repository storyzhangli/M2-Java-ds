# D21 测试汇总

## 执行摘要

| 阶段 | 命令 | 结果 | 日志 |
|---|---|---|---|
| 基线全量 | `mvn -f .\pom.xml test` | 构建成功 | `logs\D21\D21-baseline-full-test.log` |
| observability 专项 | `mvn -f .\pom.xml "-Dtest=TokenUsageExtractorTest,ModelCallObservabilityTest,AccessLogMaskingTest,AccessLogIntegrationTest,ChatStreamIntegrationTest,AccessLogRealContainerTest,StreamClientDisconnectIntegrationTest" test` | 29/29 通过 | `logs\D21\D21-observability-tests-03.log` |
| usage 专项 | `mvn -f .\pom.xml "-Dtest=TokenUsageExtractorTest,ModelCallObservabilityTest" test` | 11/11 通过 | `logs\D21\D21-usage-tests-04.log` |
| 关闭访问日志 | `mvn -f .\pom.xml -Dtest=TraceIdWithoutAccessLogTest test` | 1/1 通过 | `logs\D21\D21-trace-disabled-log-test-01.log` |
| 最终 observability 增量 | `mvn -f .\pom.xml "-Dtest=ModelCallObservabilityTest,ChatStreamIntegrationTest,StreamClientDisconnectIntegrationTest,TraceIdWithoutAccessLogTest" test` | 通过 | `logs\D21\D21-final-observability-tests-05.log` |
| 最终全量回归 | `mvn -f .\pom.xml test` | 174 个测试，0 失败，0 错误，1 跳过 | `logs\D21\D21-full-regression-final.log` |
| 最终打包 | `mvn -f .\pom.xml -DskipTests package` | 成功 | `logs\D21\D21-package-final.log` |

唯一跳过项是默认禁用的 `DeepSeekLiveSmokeTest`，本轮未发起真实 DeepSeek 请求。

## 需求覆盖

| 测试目标 | 实际测试 | 结果 |
|---|---|---|
| 无 Header 生成 TraceId | `AccessLogIntegrationTest.twoRequestsShouldUseDistinctRequestIdsWithoutMdcLeak` | 通过 |
| 有效 TraceId 复用 | `AccessLogIntegrationTest.normalRequestShouldCorrelateAllEventsBySameRequestIdAndKeepResponseIntact` | 通过 |
| 流式 TraceId 关联 | `ChatStreamIntegrationTest.streamShouldReturnOrderedSseFramesWithUtf8ContentAndCompleteEvent` | 通过 |
| 恶意/超长 Header | `AccessLogMaskingTest.inboundRequestIdShouldRejectForgingAndOverlongValues`、`AccessLogIntegrationTest.maliciousTraceHeaderShouldBeRegeneratedAndNotInjectLogLines` | 通过 |
| 关闭访问日志后 TraceId 仍可用 | `TraceIdWithoutAccessLogTest` | 通过 |
| 同步成功调用记录 | `ModelCallObservabilityTest.syncSuccessShouldLogTraceLatencyAndProviderUsageWithoutPromptOrAnswer` | 通过 |
| 同步失败调用记录 | `ModelCallObservabilityTest.syncErrorShouldLogOneSafeTerminalRecordAndOmitMissingUsageValues` | 通过 |
| 流式成功调用记录 | `ModelCallObservabilityTest.streamUsageShouldUseLatestSnapshotWithoutAccumulatingDuplicateChunks` | 通过 |
| 流式错误调用记录 | `ModelCallObservabilityTest.streamErrorShouldLogOnlyErrorTypeAndNoSensitiveMessageOrContent` | 通过 |
| 流式取消调用记录 | `ModelCallObservabilityTest.streamCancellationShouldLogOneCancelledTerminalRecord` | 通过 |
| 真实客户端断开触发 CANCELLED | `StreamClientDisconnectIntegrationTest` | 通过 |
| Mock usage 不伪造 | `ModelCallObservabilityTest.mockUsageShouldBeExplicitlyNotSupportedWithoutTokenValues` | 通过 |
| 完整 usage | `TokenUsageExtractorTest.completeProviderUsageShouldBeAvailableWithoutDerivation` | 通过 |
| 部分 usage | `TokenUsageExtractorTest.partialUsageShouldKeepMissingFieldsNull` | 通过 |
| usage 完全缺失 | `TokenUsageExtractorTest.missingUsageMustNotBecomeZeroOrEstimatedValue` | 通过 |
| total 派生标记 | `TokenUsageExtractorTest.totalMayBeDerivedOnlyWhenBothProviderComponentsAreExplicit` | 通过 |
| 明确返回 0 | `TokenUsageExtractorTest.explicitProviderZeroShouldRemainZero` | 通过 |
| Prompt/Completion/密钥/异常消息脱敏 | `ModelCallObservabilityTest`、`AccessLogMaskingTest`、`AccessLogIntegrationTest` | 通过 |
| 旧同步/流式契约 | `MockChatControllerTest`、`ChatStreamIntegrationTest`、`StreamContractCompatibilityTest` 及全量回归 | 通过 |

## 运行证据摘要

最终全量日志中同一 TraceId 关联到：

- `sync-trace-001`：SYNC / SUCCESS。
- `disabled-log-trace-001`：关闭访问日志后 SYNC / SUCCESS。
- `stream-trace-001`：STREAM / SUCCESS。
- `disconnect-trace-001`：STREAM / CANCELLED。

摘录见 `D21-model-call-runtime-excerpt.log`。每个 TraceId 只见一条终态 `MODEL_CALL` 记录。

## 测试修复记录

- 首轮 28 项专项中，2 个失败来自测试夹具：精确脱敏使长 `message` 不再触发截断；Spring AI `EmptyUsage` 的 0 被误判为 Provider 明确值。改为长非敏感字段与 `EmptyUsage` 类型识别后通过。
- 第一次全量回归中，既有 `MockNoExternalApiTest` 要求 Mock 类零字段；移除新增静态常量后通过。

未通过“修绿”删除或放宽任何功能断言。
