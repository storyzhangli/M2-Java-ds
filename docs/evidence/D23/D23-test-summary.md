# D23 测试汇总

## 执行摘要

| 阶段 | 命令 | 结果 | 日志 |
|---|---|---|---|
| 修复前基线 | `mvn -f .\pom.xml test` | 182 个测试，0 失败，0 错误，1 跳过 | `logs\D23\D23-baseline-full-test.log` |
| 缺口复现 | `mvn -f .\pom.xml "-Dtest=EquipmentCandidateParserRegressionTest,ChatMemoryReadFailureRegressionTest,ContextWindowTest" test` | 12 项中 5 项失败，复现缺陷 | `logs\D23\D23-pre-fix-regression.log` |
| 修复后定向 | 同上 | 12/12 通过 | `logs\D23\D23-post-fix-regression-02.log` |
| 扩展回归 | `mvn -f .\pom.xml "-Dtest=ChatModelFailureRegressionTest,ChatMemoryOrderRegressionTest,EquipmentCandidateParserRegressionTest,ChatMemoryReadFailureRegressionTest,ContextWindowTest" test` | 15/15 通过 | `logs\D23\D23-regression-suite-03.log` |
| 最终全量 | `mvn -f .\pom.xml test` | 194 个测试，0 失败，0 错误，1 跳过 | `logs\D23\D23-full-regression-01.log` |
| 打包 | `mvn -f .\pom.xml -DskipTests package` | 成功 | `logs\D23\D23-package-01.log` |

唯一跳过项为默认禁用的真实 DeepSeek 烟雾测试。

## 需求覆盖

| 需求 | 新增/关键回归测试 | 结果 |
|---|---|---|
| D23-R01 同步聊天 | `ChatModelFailureRegressionTest`、既有 `MockChatControllerTest`/`RoleChatControllerTest` | 通过 |
| D23-R02 SSE | 既有顺序、完成、异常、超时、取消、断连、首段后不重放测试 | 通过 |
| D23-R03 Memory | `ChatMemoryOrderRegressionTest`、`ChatMemoryReadFailureRegressionTest`、`ContextWindowTest`、既有持久化/隔离/并发/重启测试 | 通过 |
| D23-R04 结构化输出 | `EquipmentCandidateParserRegressionTest`、`EquipmentCandidateParserTest` | 通过 |
| D23-R05 关键修复 | 修复前 5 个失败到修复后全部通过 | 通过 |
| D23-AC06 真实执行 | 全量/定向/打包日志保留 | 通过 |

## 修复前失败用例

- `EquipmentCandidateParserRegressionTest.trailingContentAfterValidObjectMustBeRejected`
- `EquipmentCandidateParserRegressionTest.needsClarificationMustCarryAtLeastOneMissingInformationItem`
- `ChatMemoryReadFailureRegressionTest.plainSyncHistoryReadFailureShouldFailTheStartedTurn`
- `ChatMemoryReadFailureRegressionTest.rolePreparationHistoryReadFailureShouldFailTheStartedTurn`
- `ChatMemoryReadFailureRegressionTest.streamHistoryReadFailureShouldFailTheStartedTurnBeforeStartFrame`

完整失败摘要：`logs\D23\D23-pre-fix-regression.log`。
