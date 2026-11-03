# D21 现状索引与 Git 基线

- 生成时间：2026-11-03 19:07:33 +08:00
- 扫描范围：D21 POM、聊天 Controller/Service、Trace/MDC/访问日志组件、Spring AI ChatResponse/usage 相关测试。
- 未扫描无关模块源码，未读取完整日志。
- Git HEAD：10a3920bef10d4ef5856f1b58c87cf1fca4ea20f
- 当前分支：master
- 模块：D21(pom) -> D21_TOKEN_ID_LOGS(jar)
- 运行时：JDK 21.0.7；Maven 3.9.16；Spring Boot 3.5.16；Spring AI 1.1.8

## 工作区状态

```text
 M D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/AccessLogAutoConfiguration.java
 M D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/AccessLogProperties.java
 M D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/CachedBodyAccessLogFilter.java
 M D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/ControllerAccessLogAspect.java
 M D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/LogMaskingUtils.java
 M D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/TraceIdFilter.java
 M D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/service/DeepSeekAiChatService.java
 M D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/service/MockAiChatService.java
 M D21/D21_TOKEN_ID_LOGS/src/main/resources/application.yml
 M D21/D21_TOKEN_ID_LOGS/src/main/resources/logback-spring.xml
 M D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/logging/AccessLogIntegrationTest.java
 M D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/logging/AccessLogMaskingTest.java
 M D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/stream/ChatStreamIntegrationTest.java
 M D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/stream/StreamClientDisconnectIntegrationTest.java
?? D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/observability/
?? D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/logging/TraceIdWithoutAccessLogTest.java
?? D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/observability/
?? docs/evidence/D21/
?? "docs/proj_docs/D21-\350\201\212\345\244\251\350\260\203\347\224\250\345\217\257\350\247\202\346\265\213\346\200\247\346\212\200\346\234\257\350\257\264\346\230\216\344\270\216\345\256\236\350\267\265\346\214\207\345\215\227.md"
?? "docs/proj_docs/D21-\351\241\271\347\233\256\350\277\255\344\273\243\345\205\250\351\207\217\344\277\241\346\201\257\344\270\216\346\223\215\344\275\234.md"
```

## 相关文件 SHA-256

| 文件 | SHA-256 |
|---|---|
| `D21/pom.xml` | `BC59464D2411D8AD059E5F0F70B7D275E44E473C9EB252958E83B21057DFF42A` |
| `D21/D21_TOKEN_ID_LOGS/pom.xml` | `F8168DEBBDE26D29048FF34F5F258F07E1613B06F24D54680C6C613F3C5DCCEE` |
| `D21/D21_TOKEN_ID_LOGS/src/main/resources/application.yml` | `7E849D84FB27E8758E1D2CEF4FB36628A6FD6CCFDC11C30EE4AFB496176B6BCB` |
| `D21/D21_TOKEN_ID_LOGS/src/main/resources/logback-spring.xml` | `7A7E74EF42099400615F1ED3B76B4B0CBBF74A6D79A105A1F5F74590EAFA6259` |
| `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/TraceIdFilter.java` | `AC51738A9888DF873B98A79A5CF89F5EEFF0362473B8A9B6F1C0BEEE9294FEBF` |
| `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/AccessLogProperties.java` | `4DDD00E6E599926C6A6B974BA206F6342BB904CE8A197EB8A82430303B648C91` |
| `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/AccessLogAutoConfiguration.java` | `2C7C310A1B957E73B98EAD4A3D4ED83D223572A18C53ED0E6332F4330A94799D` |
| `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/CachedBodyAccessLogFilter.java` | `F4D3ACD795C179421E0B1A438C31BEB2863B7E37779B107AF2E85571F87DAC2B` |
| `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/ControllerAccessLogAspect.java` | `025D2B610807C5C05365E209566F3D85698654062CBE538951B36659DF003D1F` |
| `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/LogMaskingUtils.java` | `8160674D2D90BC8456C57622A9B2BAE51B422EEDA24E7734B919222331B24C03` |
| `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/service/AiChatResult.java` | `B26764EA6D5DB0F47772E7A1FE15D671E595A482BD4633E98103559A0CE841E1` |
| `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/service/AiChatService.java` | `60BB873CFF54D8210965848424C31DCA0930A31D67D5500376B24C933523B63A` |
| `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/service/DeepSeekAiChatService.java` | `1BE182A06711C027A96D31BB78729F4DD880907914F2DE547BC630046359C7E6` |
| `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/service/MockAiChatService.java` | `6C7025941D9CF796943858BEFC7F75497C92719146430A1E62D4C52DF9673420` |
| `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/service/StreamChunk.java` | `C1277D21A7B963894B1630663090542BD80B9831796B94DCEDC8FF0BB4304F85` |
| `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/controller/ChatController.java` | `3D59D0149FCD22809587FEEE29C9B3E1F0F87A27EAD4E1C52841123951C595F1` |
| `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/controller/ChatStreamController.java` | `0041DBAB51F541396561A2A4DE014351B1DC10DE608CB4A93DBCB0FFAA1E6878` |
| `D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/logging/AccessLogIntegrationTest.java` | `10FCB618EE2FEC4EE1B5D3E5A8BC903E37C0BA5660536615B77B629DEE3B9F2B` |
| `D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/logging/AccessLogRealContainerTest.java` | `12B35F7DBF9BC63C762A12B25A08950967E4E0BE05F1832D2B2BC798E5FFA736` |
| `D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/stream/SpringAiChatClientBehaviorTest.java` | `A793D4DDA5872FA123159226F37D83CD5F03AF9CE2F5707D1BB0CFE8B69595A5` |
| `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/observability/ModelCallRecorder.java` | `C341AD801A95188EFC8F4AC7680D37F823AA844E10C1C121F78EA25EABC48D20` |
| `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/observability/ModelTokenUsage.java` | `6F87EABBE24147A1FDCF90A0C99F9F701E7A631E320EC18FD3A9F9AD656D9C0E` |
| `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/observability/TokenUsageExtractor.java` | `E054E8753F1A3D61BCE132E056C05E19F50727869046C8E26B703F0F514AA782` |
| `D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/observability/ModelCallObservabilityTest.java` | `9215CA950AED827738C4906160DE2F3C20F7CCD6EA324EBCE134B000CF9381F5` |
| `D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/observability/TokenUsageExtractorTest.java` | `08BACE01A89965A59BE35E11ABBEFE9D668A23028F37039A83963AE3684E6072` |
| `D21/D21_TOKEN_ID_LOGS/src/test/java/com/zl/d21/logging/TraceIdWithoutAccessLogTest.java` | `4082E02759F37D8A6B6D186BFBC831B6B8673AB28C46C19B67D095BEFC704D60` |
| `D21/D21_TOKEN_ID_LOGS/src/main/java/com/zl/d21/logging/AccessLogAutoConfiguration.java` | `2C7C310A1B957E73B98EAD4A3D4ED83D223572A18C53ED0E6332F4330A94799D` |

## Surefire 测试类摘要

```text
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 15.94 s -- in com.zl.d21.ApplicationContextLoadTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.945 s -- in com.zl.d21.ChatLengthValidationTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.646 s -- in com.zl.d21.conversation.RedisConversationMemoryTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.491 s -- in com.zl.d21.conversation.RoleConversationServiceTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.920 s -- in com.zl.d21.d16.CitationIntegrationTest
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.014 s -- in com.zl.d21.d16.CitationRegistryTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 4.112 s -- in com.zl.d21.d16.D16ThinkingStreamIntegrationTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.012 s -- in com.zl.d21.d16.KnowledgeChatOrchestratorTest
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.014 s -- in com.zl.d21.d16.KnowledgeRetrievalServiceTest
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.196 s -- in com.zl.d21.d16.ThinkingModeIntegrationTest
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.009 s -- in com.zl.d21.d16.ThinkingSettingsResolverTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.825 s -- in com.zl.d21.DeepSeekKeyProtectionTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 1, Time elapsed: 0 s -- in com.zl.d21.DeepSeekLiveSmokeTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.030 s -- in com.zl.d21.equipment.EquipmentCandidateParserTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.625 s -- in com.zl.d21.equipment.EquipmentChatControllerTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.017 s -- in com.zl.d21.equipment.EquipmentInformationAndClarificationTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.784 s -- in com.zl.d21.history.ContextWindowTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.260 s -- in com.zl.d21.history.HistoryConcurrencyTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.122 s -- in com.zl.d21.history.HistoryFailureTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.206 s -- in com.zl.d21.history.HistoryPersistenceTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.124 s -- in com.zl.d21.history.SessionIsolationTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.071 s -- in com.zl.d21.history.SpringLockDiagnosticTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.200 s -- in com.zl.d21.logging.AccessLogIntegrationTest
Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.019 s -- in com.zl.d21.logging.AccessLogMaskingTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.080 s -- in com.zl.d21.logging.AccessLogRealContainerTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.208 s -- in com.zl.d21.logging.TraceIdWithoutAccessLogTest
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.151 s -- in com.zl.d21.MockChatControllerTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.054 s -- in com.zl.d21.MockNoExternalApiTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.018 s -- in com.zl.d21.model.ModelCallPropertiesValidationTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.008 s -- in com.zl.d21.model.ModelFailureClassifierTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.323 s -- in com.zl.d21.model.ModelHttpClientTimeoutTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.545 s -- in com.zl.d21.model.ModelHttpRetryIntegrationTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.084 s -- in com.zl.d21.model.ModelRetryPolicyTest
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.524 s -- in com.zl.d21.observability.ModelCallObservabilityTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.084 s -- in com.zl.d21.observability.TokenUsageExtractorTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.148 s -- in com.zl.d21.prompt.PersistenceCacheIntegrationTest
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.339 s -- in com.zl.d21.prompt.PromptTemplateServiceTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.389 s -- in com.zl.d21.service.MockAiChatServiceStreamTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.118 s -- in com.zl.d21.stream.ChatStreamErrorEventTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.519 s -- in com.zl.d21.stream.ChatStreamIntegrationTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.079 s -- in com.zl.d21.stream.ChatStreamTimeoutTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.994 s -- in com.zl.d21.stream.ChatStreamTotalDeadlineTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.006 s -- in com.zl.d21.stream.MvcStackDecisionTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.935 s -- in com.zl.d21.stream.RoleChatStreamIntegrationTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.014 s -- in com.zl.d21.stream.SpringAiChatClientBehaviorTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.206 s -- in com.zl.d21.stream.StreamClientDisconnectIntegrationTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.957 s -- in com.zl.d21.stream.StreamContractCompatibilityTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.846 s -- in com.zl.d21.stream.StreamPreStartFailureTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.372 s -- in com.zl.d21.web.HistoryControllerWebTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.221 s -- in com.zl.d21.web.PromptTemplateControllerWebTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.171 s -- in com.zl.d21.web.RoleChatControllerTest
```
