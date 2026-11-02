# D20 测试汇总

## 执行摘要

| 阶段 | 命令 | 结果 | 日志 |
|---|---|---|---|
| 超时测试修复验证 | `mvn -f .\pom.xml -Dtest=ModelHttpClientTimeoutTest test` | 2/2 通过 | `logs\D20\D20-timeout-test-fix-01.log` |
| D20 专项集 | `mvn -f .\pom.xml "-Dtest=ApplicationContextLoadTest,ModelCallPropertiesValidationTest,ModelFailureClassifierTest,ModelRetryPolicyTest,ModelHttpRetryIntegrationTest,ModelHttpClientTimeoutTest,ChatStreamErrorEventTest,ChatStreamTimeoutTest,ChatStreamTotalDeadlineTest,StreamClientDisconnectIntegrationTest" test` | 28/28 通过 | `logs\D20\D20-targeted-retry-tests-04.log` |
| 全量回归 | `mvn -f .\pom.xml test` | 161 个测试，0 失败，0 错误，1 跳过 | `logs\D20\D20-full-regression-01.log` |
| 打包 | `mvn -f .\pom.xml -DskipTests package` | 成功，生成可执行 JAR | `logs\D20\D20-package-01.log` |

唯一的跳过项是 `DeepSeekLiveSmokeTest`。该测试默认禁用，未执行真实 DeepSeek API 烟雾调用，不产生 Token 费用。

## 任务书测试矩阵映射

| 编号 | 覆盖测试/证据 | 结果 |
|---|---|---|
| T01 正常同步响应 | `MockChatControllerTest`、`RoleChatControllerTest`、全量回归 | 通过 |
| T02 正常流式响应 | `ChatStreamIntegrationTest`、`RoleChatStreamIntegrationTest`、`StreamContractCompatibilityTest` | 通过 |
| T03 鉴权失败 | `ModelFailureClassifierTest.permanentQuotaAndClientErrorsShouldNotRetry`、`DeepSeekKeyProtectionTest` | 通过 |
| T04 参数/模型配置错误 | `ModelFailureClassifierTest`、`RoleChatControllerTest`、`MockChatControllerTest` | 通过 |
| T05 可恢复 429 | `ModelHttpRetryIntegrationTest.recoverableRateLimitShouldHonorRetryAfter` | 通过 |
| T06 永久额度 | `ModelHttpRetryIntegrationTest.permanentQuotaShouldNotRetry` | 通过 |
| T07 可恢复 503 | `ModelHttpRetryIntegrationTest.recoverable503ShouldRetryOnceAndThenSucceed` | 通过 |
| T08 建连失败 | `ModelHttpRetryIntegrationTest.connectFailureShouldReceiveOnlyOneBoundedRetry` | 通过 |
| T09 请求后读取超时 | `ModelHttpRetryIntegrationTest.readTimeoutAfterRequestWasSentShouldNotRetry` | 通过 |
| T10 总 Deadline | `ModelRetryPolicyTest`、`ChatStreamTotalDeadlineTest` | 通过 |
| T11 防多层重试 | `ApplicationContextLoadTest.deepSeekChatModelShouldUseTheSingleD20RetryTemplate`；实际请求次数断言 | 通过 |
| T12 流式首内容前失败 | `ChatStreamErrorEventTest.errorAfterStreamStartedShouldEmitErrorEventWithoutComplete`；调用一次 | 通过 |
| T13 流式首内容后失败 | `ChatStreamErrorEventTest.failureAfterFirstDeltaShouldNotReplayFromBeginning` | 通过 |
| T14 客户端取消/断开 | `StreamClientDisconnectIntegrationTest`、`ChatStreamTimeoutTest` | 通过 |
| T15 并发请求 | `ModelRetryPolicyTest.concurrentCallsShouldNotShareRetryCounters`（16 并发任务） | 通过 |
| T16 配置边界 | `ModelCallPropertiesValidationTest` | 通过 |
| T17 异常映射回归 | 全量回归中的同步、流式、业务异常与既有 Controller 测试 | 通过 |
| T18 敏感信息 | `DeepSeekKeyProtectionTest`、`AccessLogMaskingTest`、`D20-sensitive-scan.md` | 通过 |

## HTTP 超时与重试的确定性验证

- `ModelHttpClientTimeoutTest` 使用本地慢响应 HTTP Server 验证 RestClient 与 WebClient 的读取/响应超时实际生效。
- `ModelHttpRetryIntegrationTest` 使用本地 `HttpServer`，断言真实请求次数；不访问 DeepSeek。
- 429 测试设置 `Retry-After: 1`，测得等待不少于约 900ms，再执行第二次请求。
- 503 只有第二次成功场景实际请求 2 次。
- 永久额度、请求后读取超时均实际请求 1 次。
- 连接拒绝最多 2 次总尝试。

## 测试修复记录

专项集第一次运行出现 2 个同根因错误：`ModelHttpClientTimeoutTest` 把
`max-backoff` 设为 100ms，却沿用初始退避 500ms，触发“初始退避不得超过最大退避”的
启动校验。测试补充 `initial-backoff=100ms` 后通过。生产默认配置和校验规则未放宽。

## Token 成本元数据

- 真实模型请求数：0
- 输入/缓存/输出 Token：未产生真实模型费用
- Agent 平台 Token 用量：平台不可观测，不估算为精确账单

## 报告路径

- Surefire 报告：`D20\D20_UMIEM\target\surefire-reports`
- 打包产物：`D20\D20_UMIEM\target\D20_UMIEM-1.0-SNAPSHOT.jar`
