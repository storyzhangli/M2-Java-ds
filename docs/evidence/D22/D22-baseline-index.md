# D22 现状索引与 Git 基线

- 生成时间：2026-11-04 22:08:45 +08:00
- 扫描范围：D22 POM、应用 AI 配置、模型 Bean 创建/适配、聊天 Controller/Service、相关配置与架构测试。
- 未扫描无关模块源码，未将完整日志送入分析。
- Git HEAD：c5b73e7875f3de12b798a79a8b0df2d838e4389f
- 当前分支：master
- 模块：D22(pom) -> D22_EMN_URLs_Keys_schemas(jar)
- 运行时：JDK 21.0.7；Maven 3.9.16；Spring Boot 3.5.16；Spring AI 1.1.8

## 工作区状态

```text
 M D22/D22_EMN_URLs_Keys_schemas/pom.xml
 M D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/D22SpringAiApplication.java
 M D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/config/AiConstants.java
 M D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/config/AiProperties.java
 M D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/controller/ChatStreamController.java
 M D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/knowledge/KnowledgeChatOrchestrator.java
 M D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/service/AiChatService.java
 M D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/service/DeepSeekAiChatService.java
 M D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/service/MockAiChatService.java
 M D22/D22_EMN_URLs_Keys_schemas/src/main/resources/application-local.yml.example
 M D22/D22_EMN_URLs_Keys_schemas/src/main/resources/application.yml
 M D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/ApplicationContextLoadTest.java
 M D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/DeepSeekKeyProtectionTest.java
 M D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/DeepSeekLiveSmokeTest.java
 M D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/d16/KnowledgeChatOrchestratorTest.java
 M D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/observability/ModelCallObservabilityTest.java
 M D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/stream/SpringAiChatClientBehaviorTest.java
 M D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/stream/StreamPreStartFailureTest.java
?? D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/config/DeepSeekChatModelConfiguration.java
?? D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/MockWithoutApiKeyTest.java
?? D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/ProviderIsolationTest.java
?? D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/config/
?? docs/evidence/D22/
?? "docs/proj_docs/D22-\346\250\241\345\236\213\351\205\215\347\275\256\345\244\226\347\275\256\344\270\216\344\276\233\345\272\224\345\225\206\346\227\240\345\205\263\346\234\215\345\212\241\346\216\245\345\217\243\345\256\236\350\267\265\346\214\207\345\215\227.md"
?? "docs/proj_docs/D22-\351\241\271\347\233\256\350\277\255\344\273\243\345\205\250\351\207\217\344\277\241\346\201\257\344\270\216\346\223\215\344\275\234.md"
```

## 相关文件 SHA-256

| 文件 | SHA-256 |
|---|---|
| `D22/D22_EMN_URLs_Keys_schemas/pom.xml` | `BBA4B3279D38A18D7A9D5D8D019BCD6084C01CC7D3391CA21AB7D570BDCF389C` |
| `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/config/AiConstants.java` | `90C9F7AD59EBF2A89D88350B131F8C77B45A5EAB008A72A69650B53E5DBF0423` |
| `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/config/AiProperties.java` | `BC5D7D935BF3A5DFD5CB3C85649E861C854BD702A5F59E61EACC9DFE850DE545` |
| `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/config/ConversationRedisProperties.java` | `CBE71C11E77A9B28EA9598968A8700666B879916DC71DC50FFD1D32379195728` |
| `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/config/DeepSeekChatModelConfiguration.java` | `F5FCAD44AB902E8198CAD2D3042AAC96B646AB57A54CC3F08077DD4FC47B0AAA` |
| `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/config/ModelCallConfiguration.java` | `9CFD4CCDD9F355B5756542591451B263CE36455694A8BE83B3792F6AC9814EAE` |
| `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/config/ModelCallProperties.java` | `AE0DA7BD19F02EF615E3238A58A56416388422AAB7E9E878A645CFA6B42DAF11` |
| `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/config/PromptCacheProperties.java` | `082987206AAAB99D658F2526B5547D1E74BC6571864F0215F34DD5AA165110A1` |
| `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/controller/ChatController.java` | `5DD0C2D16EBE0822408D1FCDCC1D41708E1F522A97F2451CE428FC2F365111CF` |
| `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/controller/ChatStreamController.java` | `4594E5BDD94AE3AA0B9E5A4F996048AA313B38B3E2C41147EED1232F74F5BCB7` |
| `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/knowledge/KnowledgeChatOrchestrator.java` | `49BE35AE9758C62576A2EAA2527D2C43AB01B8750D7F9FD8E1276FCD078DBA3A` |
| `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/service/AiChatResult.java` | `B1D485F610D9549BA0EA6B8BE0D23B9D67F56B21763068B45D6FA9D4783175A4` |
| `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/service/AiChatService.java` | `6932D68C2EA14FB8EE0DD0A742CBF8EB681E4D796004B0BD4AA90E9295B66333` |
| `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/service/DeepSeekAiChatService.java` | `954FF0999D404F51994BE99774BEEB7146179E35668A78833E176FEF0D7587D1` |
| `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/service/MockAiChatService.java` | `F71D3457021D20CC8910878DB8A29FA77B647F3011B4C617D930836415F96B30` |
| `D22/D22_EMN_URLs_Keys_schemas/src/main/java/com/zl/d22/service/StreamChunk.java` | `D2F86C58AA5A4A52E16FAE04819E1067E4C7A05C71B4095778B4864C30CE6C98` |
| `D22/D22_EMN_URLs_Keys_schemas/src/main/resources/application-local.yml.example` | `0B4270DA88CA4EE2DDE506CD66B4DEFD5B2FE34E353BF39BAC698174A916DE64` |
| `D22/D22_EMN_URLs_Keys_schemas/src/main/resources/application.yml` | `494FB1368E3381DA81646EAA128D8F603B42B1DB190FA6BF410C35013612DC73` |
| `D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/ApplicationContextLoadTest.java` | `9B5B025C2238C9FE0EC51462A0DE92AE3A384296F9B545205D4518A27F30BB48` |
| `D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/config/AiPropertiesValidationTest.java` | `C6E25BE4C51E22AB48D69C8AA67E2F3F93CBE27AFFF5429AB712A724D93C3434` |
| `D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/config/DeepSeekConfigurationBindingTest.java` | `DFD93971A39C9AA89FD94F0B74EB50A1AE7AF0960097766A056FF2CE5CE1C1FD` |
| `D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/DeepSeekKeyProtectionTest.java` | `C1246F35877CD00EBFB27C37D02346F908B47D8BB2B5C347A9CC615FD6C0194E` |
| `D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/DeepSeekLiveSmokeTest.java` | `FB9FD63DDDE175D6C982992D6F4B38E7F93F94C40E21AB9C76A0884D9F533ADA` |
| `D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/observability/ModelCallObservabilityTest.java` | `4CBA48F9C6C6C95F325AD929C34DABCBAAA682617C15565FDAF4FBC3EE009D69` |
| `D22/D22_EMN_URLs_Keys_schemas/src/test/java/com/zl/d22/stream/SpringAiChatClientBehaviorTest.java` | `51A62A4346DF9BE1578F16FDB7D39DE3331F21FA01AE198EDB8A77D648278700` |
| `D22/pom.xml` | `DEADEE2814EDDA710BCD2E629B74FBAC3E17DB6B3FF11F0D404EB056169F9955` |

## Surefire 测试类摘要

```text
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 15.22 s -- in com.zl.d22.ApplicationContextLoadTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.846 s -- in com.zl.d22.ChatLengthValidationTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.073 s -- in com.zl.d22.config.AiPropertiesValidationTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.533 s -- in com.zl.d22.config.DeepSeekConfigurationBindingTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.535 s -- in com.zl.d22.conversation.RedisConversationMemoryTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.446 s -- in com.zl.d22.conversation.RoleConversationServiceTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.451 s -- in com.zl.d22.d16.CitationIntegrationTest
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.008 s -- in com.zl.d22.d16.CitationRegistryTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.910 s -- in com.zl.d22.d16.D16ThinkingStreamIntegrationTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.008 s -- in com.zl.d22.d16.KnowledgeChatOrchestratorTest
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.014 s -- in com.zl.d22.d16.KnowledgeRetrievalServiceTest
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.201 s -- in com.zl.d22.d16.ThinkingModeIntegrationTest
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.016 s -- in com.zl.d22.d16.ThinkingSettingsResolverTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.480 s -- in com.zl.d22.DeepSeekKeyProtectionTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 1, Time elapsed: 0.001 s -- in com.zl.d22.DeepSeekLiveSmokeTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.030 s -- in com.zl.d22.equipment.EquipmentCandidateParserTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.849 s -- in com.zl.d22.equipment.EquipmentChatControllerTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.021 s -- in com.zl.d22.equipment.EquipmentInformationAndClarificationTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.687 s -- in com.zl.d22.history.ContextWindowTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.321 s -- in com.zl.d22.history.HistoryConcurrencyTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.113 s -- in com.zl.d22.history.HistoryFailureTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.221 s -- in com.zl.d22.history.HistoryPersistenceTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.132 s -- in com.zl.d22.history.SessionIsolationTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.072 s -- in com.zl.d22.history.SpringLockDiagnosticTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.211 s -- in com.zl.d22.logging.AccessLogIntegrationTest
Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.015 s -- in com.zl.d22.logging.AccessLogMaskingTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.074 s -- in com.zl.d22.logging.AccessLogRealContainerTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.190 s -- in com.zl.d22.logging.TraceIdWithoutAccessLogTest
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.146 s -- in com.zl.d22.MockChatControllerTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.766 s -- in com.zl.d22.MockNoExternalApiTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.173 s -- in com.zl.d22.MockWithoutApiKeyTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.014 s -- in com.zl.d22.model.ModelCallPropertiesValidationTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.009 s -- in com.zl.d22.model.ModelFailureClassifierTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.329 s -- in com.zl.d22.model.ModelHttpClientTimeoutTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.535 s -- in com.zl.d22.model.ModelHttpRetryIntegrationTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.055 s -- in com.zl.d22.model.ModelRetryPolicyTest
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.560 s -- in com.zl.d22.observability.ModelCallObservabilityTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.078 s -- in com.zl.d22.observability.TokenUsageExtractorTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.149 s -- in com.zl.d22.prompt.PersistenceCacheIntegrationTest
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.346 s -- in com.zl.d22.prompt.PromptTemplateServiceTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.047 s -- in com.zl.d22.ProviderIsolationTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.340 s -- in com.zl.d22.service.MockAiChatServiceStreamTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.053 s -- in com.zl.d22.stream.ChatStreamErrorEventTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.555 s -- in com.zl.d22.stream.ChatStreamIntegrationTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.034 s -- in com.zl.d22.stream.ChatStreamTimeoutTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.862 s -- in com.zl.d22.stream.ChatStreamTotalDeadlineTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.006 s -- in com.zl.d22.stream.MvcStackDecisionTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.107 s -- in com.zl.d22.stream.RoleChatStreamIntegrationTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.018 s -- in com.zl.d22.stream.SpringAiChatClientBehaviorTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.203 s -- in com.zl.d22.stream.StreamClientDisconnectIntegrationTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.981 s -- in com.zl.d22.stream.StreamContractCompatibilityTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.764 s -- in com.zl.d22.stream.StreamPreStartFailureTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.308 s -- in com.zl.d22.web.HistoryControllerWebTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.210 s -- in com.zl.d22.web.PromptTemplateControllerWebTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.166 s -- in com.zl.d22.web.RoleChatControllerTest
```
