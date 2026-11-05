# D23 现状索引与 Git 基线

- 生成时间：2026-11-05 18:44:41 +08:00
- 扫描范围：D23 POM、同步聊天、SSE 控制器、MySQL/Redis Memory、结构化候选解析及直接关联测试。
- 未扫描无关模块源码，未将完整日志送入分析。
- Git HEAD：0d098e67f68dfbabfda0863221d02227277f1d59
- 当前分支：master
- 模块：D23(pom) -> D23_CRTFC_SSE_MASO(jar)

## 工作区状态

```text
 M D23/D23_CRTFC_SSE_MASO/src/main/java/com/zl/d23/controller/ChatController.java
 M D23/D23_CRTFC_SSE_MASO/src/main/java/com/zl/d23/controller/ChatStreamController.java
 M D23/D23_CRTFC_SSE_MASO/src/main/java/com/zl/d23/conversation/RoleConversationService.java
 M D23/D23_CRTFC_SSE_MASO/src/main/java/com/zl/d23/equipment/EquipmentCandidateParser.java
 M D23/D23_CRTFC_SSE_MASO/src/test/java/com/zl/d23/history/ContextWindowTest.java
?? D23/D23_CRTFC_SSE_MASO/src/test/java/com/zl/d23/ChatMemoryOrderRegressionTest.java
?? D23/D23_CRTFC_SSE_MASO/src/test/java/com/zl/d23/ChatModelFailureRegressionTest.java
?? D23/D23_CRTFC_SSE_MASO/src/test/java/com/zl/d23/equipment/EquipmentCandidateParserRegressionTest.java
?? D23/D23_CRTFC_SSE_MASO/src/test/java/com/zl/d23/history/ChatMemoryReadFailureRegressionTest.java
?? docs/evidence/D23/
?? "docs/proj_docs/D23-\350\201\212\345\244\251-SSE-Memory-\347\273\223\346\236\204\345\214\226\350\276\223\345\207\272\345\233\236\345\275\222\346\265\213\350\257\225\344\270\216\345\205\263\351\224\256\351\227\256\351\242\230\344\277\256\345\244\215-\346\212\200\346\234\257\350\257\264\346\230\216\344\270\216\346\223\215\344\275\234\345\256\236\350\267\265\346\214\207\345\215\227.md"
?? "docs/proj_docs/D23-\350\201\212\345\244\251-SSE-Memory-\347\273\223\346\236\204\345\214\226\350\276\223\345\207\272\345\233\236\345\275\222\346\265\213\350\257\225\344\270\216\345\205\263\351\224\256\351\227\256\351\242\230\344\277\256\345\244\215-\351\241\271\347\233\256\350\277\255\344\273\243\345\205\250\351\207\217\344\277\241\346\201\257\344\270\216\346\223\215\344\275\234.md"
```

## 相关文件 SHA-256

| 文件 | SHA-256 |
|---|---|
| `D23/D23_CRTFC_SSE_MASO/pom.xml` | `4BE1C9AF7B06A020B620433D8F1335BBA6F139A85B3C4DDFE0B3D916D60DBDF3` |
| `D23/D23_CRTFC_SSE_MASO/src/main/java/com/zl/d23/controller/ChatController.java` | `1C3C9555992D4CEC3F0343D765CD21332ADCB51D3E31EB099A9E7FB8F4FB5663` |
| `D23/D23_CRTFC_SSE_MASO/src/main/java/com/zl/d23/controller/ChatStreamController.java` | `76FCA521246CDC198FEE95F84774D26467FB942AF68B866DC715E6E76BF7454A` |
| `D23/D23_CRTFC_SSE_MASO/src/main/java/com/zl/d23/conversation/ConversationMemory.java` | `198BF907D19EC67EFCB85037E744E46416793B73B532E70DD602664C2CC5ED09` |
| `D23/D23_CRTFC_SSE_MASO/src/main/java/com/zl/d23/conversation/RoleConversationService.java` | `9D7F3943865435B666C1785ED5BA37EEC315D155DD873F3E6E92E457A1FE1173` |
| `D23/D23_CRTFC_SSE_MASO/src/main/java/com/zl/d23/equipment/EquipmentCandidateExtractor.java` | `C2CAE204976C08EA0DEF4A811D2DE524B54F37EB68BE1A8B285E5F0AD80122FF` |
| `D23/D23_CRTFC_SSE_MASO/src/main/java/com/zl/d23/equipment/EquipmentCandidateParser.java` | `FE739D4AFA206F8D6087C3F08F320706E9C260CB1F93E666A367DEBD0A8BEF34` |
| `D23/D23_CRTFC_SSE_MASO/src/main/java/com/zl/d23/equipment/model/EquipmentTicketCandidate.java` | `154CCC10F6F5EEFA34AAA5EA1A1D759A824D8A409B74A2847A4A2861044C18B8` |
| `D23/D23_CRTFC_SSE_MASO/src/main/java/com/zl/d23/equipment/model/TicketCategory.java` | `13533A0366373BD903D3A340E95CBD709C253699912A3CBAB2573425A22AA4DA` |
| `D23/D23_CRTFC_SSE_MASO/src/main/java/com/zl/d23/history/ChatHistoryService.java` | `0BB48452CBFDD9C1EB994E9F529090C510D7350724E9CCE2A2138524FFF652B3` |
| `D23/D23_CRTFC_SSE_MASO/src/test/java/com/zl/d23/ChatMemoryOrderRegressionTest.java` | `A21CFC7258FED741E56D42B216A5A2F7164CA4EB2B134DAE8AB936B8C79024F6` |
| `D23/D23_CRTFC_SSE_MASO/src/test/java/com/zl/d23/ChatModelFailureRegressionTest.java` | `126F973C8A6B40AA99971A0F107819661D57747F433FA41C9C5A21B906E2326F` |
| `D23/D23_CRTFC_SSE_MASO/src/test/java/com/zl/d23/equipment/EquipmentCandidateParserRegressionTest.java` | `B1F698F8681BE72B3BB4AD51EF6BED9FB8AA64344B61E7C3405E8FF67629AB71` |
| `D23/D23_CRTFC_SSE_MASO/src/test/java/com/zl/d23/equipment/EquipmentCandidateParserTest.java` | `C1D3F64925D20A8B415C84604B64B3D5B764C494E1EB07E8200B13DA196A20F3` |
| `D23/D23_CRTFC_SSE_MASO/src/test/java/com/zl/d23/history/ChatMemoryReadFailureRegressionTest.java` | `FD3DAAF8579A665D02335720481E3C2BA591CE928E15B997DA3D51DDD196F98F` |
| `D23/D23_CRTFC_SSE_MASO/src/test/java/com/zl/d23/history/ContextWindowTest.java` | `6E43CBBB3142BBBB897F987FF9E6C88281F32B472528C44DBB23E3AA5E173AB6` |
| `D23/D23_CRTFC_SSE_MASO/src/test/java/com/zl/d23/history/HistoryPersistenceTest.java` | `E14BEC607EFC405123BCF3783B0717B30F5E04BED76EBD80CB51B0AA94E6DD64` |
| `D23/D23_CRTFC_SSE_MASO/src/test/java/com/zl/d23/history/SessionIsolationTest.java` | `E255429589F8FABDF351590DA69A78E9A77A95487AD98D90C3873F4FEC1A521F` |
| `D23/D23_CRTFC_SSE_MASO/src/test/java/com/zl/d23/MockChatControllerTest.java` | `892B801573C569FCE563EF5E202770C69DD6590CF7B5CB2C8D7E37B41989A7AB` |
| `D23/D23_CRTFC_SSE_MASO/src/test/java/com/zl/d23/stream/ChatStreamErrorEventTest.java` | `A80DBC9D0A8513E1AFD0D2CE8C97EA6F54AE1B46C771C297E4C143209F473E89` |
| `D23/D23_CRTFC_SSE_MASO/src/test/java/com/zl/d23/stream/ChatStreamIntegrationTest.java` | `AC480142568D6F796EB8FE1F50B2892AF78AC25A8CE4D463FC1B9A8DAEBBF8AC` |
| `D23/D23_CRTFC_SSE_MASO/src/test/java/com/zl/d23/stream/StreamClientDisconnectIntegrationTest.java` | `697EE756B93115130C89E9B574500D89DE2FFC2F67C59982C47BA901261C7C66` |
| `D23/pom.xml` | `519AA0A807D778BAB3AABE713F041DD27560451B9BEE87F78D5FC6F2EBA79C94` |

## Surefire 测试类摘要

```text
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 15.28 s -- in com.zl.d23.ApplicationContextLoadTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.737 s -- in com.zl.d23.ChatLengthValidationTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.714 s -- in com.zl.d23.ChatMemoryOrderRegressionTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.198 s -- in com.zl.d23.ChatModelFailureRegressionTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.070 s -- in com.zl.d23.config.AiPropertiesValidationTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.643 s -- in com.zl.d23.config.DeepSeekConfigurationBindingTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.254 s -- in com.zl.d23.conversation.RedisConversationMemoryTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.352 s -- in com.zl.d23.conversation.RoleConversationServiceTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.745 s -- in com.zl.d23.d16.CitationIntegrationTest
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.009 s -- in com.zl.d23.d16.CitationRegistryTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.385 s -- in com.zl.d23.d16.D16ThinkingStreamIntegrationTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.008 s -- in com.zl.d23.d16.KnowledgeChatOrchestratorTest
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.013 s -- in com.zl.d23.d16.KnowledgeRetrievalServiceTest
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.178 s -- in com.zl.d23.d16.ThinkingModeIntegrationTest
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.008 s -- in com.zl.d23.d16.ThinkingSettingsResolverTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.097 s -- in com.zl.d23.DeepSeekKeyProtectionTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 1, Time elapsed: 0 s -- in com.zl.d23.DeepSeekLiveSmokeTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.011 s -- in com.zl.d23.equipment.EquipmentCandidateParserRegressionTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.002 s -- in com.zl.d23.equipment.EquipmentCandidateParserTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.118 s -- in com.zl.d23.equipment.EquipmentChatControllerTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.013 s -- in com.zl.d23.equipment.EquipmentInformationAndClarificationTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.803 s -- in com.zl.d23.history.ChatMemoryReadFailureRegressionTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.367 s -- in com.zl.d23.history.ContextWindowTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.370 s -- in com.zl.d23.history.HistoryConcurrencyTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.148 s -- in com.zl.d23.history.HistoryFailureTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.243 s -- in com.zl.d23.history.HistoryPersistenceTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.146 s -- in com.zl.d23.history.SessionIsolationTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.076 s -- in com.zl.d23.history.SpringLockDiagnosticTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.178 s -- in com.zl.d23.logging.AccessLogIntegrationTest
Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.015 s -- in com.zl.d23.logging.AccessLogMaskingTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.067 s -- in com.zl.d23.logging.AccessLogRealContainerTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.160 s -- in com.zl.d23.logging.TraceIdWithoutAccessLogTest
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.126 s -- in com.zl.d23.MockChatControllerTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.045 s -- in com.zl.d23.MockNoExternalApiTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.741 s -- in com.zl.d23.MockWithoutApiKeyTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.017 s -- in com.zl.d23.model.ModelCallPropertiesValidationTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.010 s -- in com.zl.d23.model.ModelFailureClassifierTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.060 s -- in com.zl.d23.model.ModelHttpClientTimeoutTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.454 s -- in com.zl.d23.model.ModelHttpRetryIntegrationTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.072 s -- in com.zl.d23.model.ModelRetryPolicyTest
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.514 s -- in com.zl.d23.observability.ModelCallObservabilityTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.066 s -- in com.zl.d23.observability.TokenUsageExtractorTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.145 s -- in com.zl.d23.prompt.PersistenceCacheIntegrationTest
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.324 s -- in com.zl.d23.prompt.PromptTemplateServiceTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.043 s -- in com.zl.d23.ProviderIsolationTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.454 s -- in com.zl.d23.service.MockAiChatServiceStreamTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.824 s -- in com.zl.d23.stream.ChatStreamErrorEventTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.398 s -- in com.zl.d23.stream.ChatStreamIntegrationTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.850 s -- in com.zl.d23.stream.ChatStreamTimeoutTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.890 s -- in com.zl.d23.stream.ChatStreamTotalDeadlineTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.007 s -- in com.zl.d23.stream.MvcStackDecisionTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.752 s -- in com.zl.d23.stream.RoleChatStreamIntegrationTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.010 s -- in com.zl.d23.stream.SpringAiChatClientBehaviorTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.195 s -- in com.zl.d23.stream.StreamClientDisconnectIntegrationTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.843 s -- in com.zl.d23.stream.StreamContractCompatibilityTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.718 s -- in com.zl.d23.stream.StreamPreStartFailureTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.292 s -- in com.zl.d23.web.HistoryControllerWebTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.196 s -- in com.zl.d23.web.PromptTemplateControllerWebTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.168 s -- in com.zl.d23.web.RoleChatControllerTest
```
