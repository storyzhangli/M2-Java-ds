# D20 现状索引与 Git 基线

- 生成时间：2026-11-02 21:32:43 +08:00
- 扫描范围：D20 根 POM、D20_UMIEM POM、模型调用配置/异常/重试/HTTP 客户端、同步/流式错误映射及相关测试。
- 未扫描无关模块源代码。
- Git HEAD：a78b44020e1427fec7d612ea63f607c44fe957d2
- 当前分支：master
- Maven 模块：D20(pom) -> D20_UMIEM(jar)
- 运行时：JDK 21.0.7；Maven 3.9.16

## 工作区状态

```text
 M D20/D20_UMIEM/pom.xml
 M D20/D20_UMIEM/src/main/java/com/zl/d20/common/GlobalExceptionHandler.java
 M D20/D20_UMIEM/src/main/java/com/zl/d20/controller/ChatStreamController.java
 M D20/D20_UMIEM/src/main/resources/application.yml
 M D20/D20_UMIEM/src/main/resources/static/index.html
 M D20/D20_UMIEM/src/main/resources/static/js/app.js
 M D20/D20_UMIEM/src/main/resources/static/templates.html
 M D20/D20_UMIEM/src/test/java/com/zl/d20/ApplicationContextLoadTest.java
 M D20/D20_UMIEM/src/test/java/com/zl/d20/D20TestBootstrap.java
 M D20/D20_UMIEM/src/test/java/com/zl/d20/stream/ChatStreamErrorEventTest.java
 M D20/D20_UMIEM/src/test/java/com/zl/d20/stream/ChatStreamIntegrationTest.java
?? D20/D20_UMIEM/src/main/java/com/zl/d20/config/ModelCallConfiguration.java
?? D20/D20_UMIEM/src/main/java/com/zl/d20/config/ModelCallProperties.java
?? D20/D20_UMIEM/src/main/java/com/zl/d20/model/
?? D20/D20_UMIEM/src/test/java/com/zl/d20/model/
?? D20/D20_UMIEM/src/test/java/com/zl/d20/stream/ChatStreamTotalDeadlineTest.java
?? docs/evidence/D20/
?? "docs/proj_docs/D20-\346\250\241\345\236\213\345\274\202\345\270\270\350\266\205\346\227\266\344\270\216\346\234\211\351\231\220\351\207\215\350\257\225\346\212\200\346\234\257\350\257\264\346\230\216\345\217\212\345\256\236\350\267\265\346\214\207\345\215\227.md"
?? "docs/proj_docs/D20-\351\241\271\347\233\256\350\277\255\344\273\243\345\205\250\351\207\217\344\277\241\346\201\257\344\270\216\346\223\215\344\275\234.md"
```

## 相关文件 SHA-256

| 文件 | SHA-256 |
|---|---|
| `D20/D20_UMIEM/pom.xml` | `01CE79BFC34CA62893BF79D2C276741C2F5B38630A9B06EEA53858F01820456D` |
| `D20/D20_UMIEM/src/main/java/com/zl/d20/common/GlobalExceptionHandler.java` | `B889E47432045905B395BEE04D0CC6A3633E7917FE9D215A6F2C5B166EABD692` |
| `D20/D20_UMIEM/src/main/java/com/zl/d20/config/ModelCallConfiguration.java` | `7800DC03AE107472AD84077F9EDB60CB8CCF0EE99F51D69A9C9845D8BB668B53` |
| `D20/D20_UMIEM/src/main/java/com/zl/d20/config/ModelCallProperties.java` | `43FA7178450B5CCDC537F39F62D3E2D27A660F41D76ABCD29F96B26A5CB2119E` |
| `D20/D20_UMIEM/src/main/java/com/zl/d20/controller/ChatStreamController.java` | `F0F526BA0D30958A8FEC4BC937E59B73F22BA090D72904E1B85BC0CB561F305F` |
| `D20/D20_UMIEM/src/main/java/com/zl/d20/model/ClassifiedResponseErrorHandler.java` | `B3D7F7CF2A74C30DF45CDCD5B7010941679A9B947BAB7C1AADCECDD91874BBA9` |
| `D20/D20_UMIEM/src/main/java/com/zl/d20/model/ModelAttemptRetryPolicy.java` | `BFBDC4FC949206131211F8FF52C10F14D41C11801F488A5A806CA86D85157EEA` |
| `D20/D20_UMIEM/src/main/java/com/zl/d20/model/ModelFailure.java` | `8CF80FAF1E480F02062CFC03B5ABDF81BBC05FF23746F634FC347E0B3A2131B3` |
| `D20/D20_UMIEM/src/main/java/com/zl/d20/model/ModelFailureCategory.java` | `DDFF0C98AF01AA3365B71B0D420863A167E7D7C35A46600473E942FC149D841C` |
| `D20/D20_UMIEM/src/main/java/com/zl/d20/model/ModelFailureClassifier.java` | `403D299B1FE465F7DF8F56BED508067922D429014FB2DD483954555D6591FC05` |
| `D20/D20_UMIEM/src/main/java/com/zl/d20/model/ModelRetryBackOffPolicy.java` | `6073B69BA56E5BE34104977569CA9607910D60D5A6D2A554DBB83153B57C19AA` |
| `D20/D20_UMIEM/src/main/java/com/zl/d20/model/ModelRetryListener.java` | `61C457AF393B3B1C519DB4EE6042E574E04B68EE268B562227591C1936AFFED6` |
| `D20/D20_UMIEM/src/main/java/com/zl/d20/model/NonRetryableModelException.java` | `CDF5B6BA0041B2AFFC64F6C57568CB734C316B46A61BE9805DC51356474754BE` |
| `D20/D20_UMIEM/src/main/java/com/zl/d20/model/RetryableModelException.java` | `4ACE648E1BA2DB1367E407AAD0DFA5B396820DFFFAA9C2F7C1F5558B54B5406F` |
| `D20/D20_UMIEM/src/main/resources/application.yml` | `4148179C60100F6E6C7A3444E3818394FFB388441563D28EA9871D1CE7ED4263` |
| `D20/D20_UMIEM/src/test/java/com/zl/d20/ApplicationContextLoadTest.java` | `798A0B137E8BBECD2E1AC8C2FCBA53B87A03187549185D44DFEF1603381BB522` |
| `D20/D20_UMIEM/src/test/java/com/zl/d20/model/ModelCallPropertiesValidationTest.java` | `9CCA35C0DA74311D624E5DB21876B9B7A1AD3F6535728C20CA854F968B907B23` |
| `D20/D20_UMIEM/src/test/java/com/zl/d20/model/ModelFailureClassifierTest.java` | `0B607689B954CFD9BED59B5455C064CE741AC5A3917380B3239492B7D99F825B` |
| `D20/D20_UMIEM/src/test/java/com/zl/d20/model/ModelHttpClientTimeoutTest.java` | `D904450CA3A7651EEFB0330CD056F134A40AFA286E8CAC41436AA6FE1FE05215` |
| `D20/D20_UMIEM/src/test/java/com/zl/d20/model/ModelHttpRetryIntegrationTest.java` | `82511DA39C5E2AC508A362E9DB17B60ADE85788C73B6F956D675BB44C8479BBB` |
| `D20/D20_UMIEM/src/test/java/com/zl/d20/model/ModelRetryPolicyTest.java` | `25070A7BF11BA2D42729DD63ACA68BF698F05E921A571B4550417269E610B291` |
| `D20/D20_UMIEM/src/test/java/com/zl/d20/stream/ChatStreamErrorEventTest.java` | `032254CC545EB0D92B9FBF4112A60FFE011511DE052727BF08CBD93BCBDDF93B` |
| `D20/D20_UMIEM/src/test/java/com/zl/d20/stream/ChatStreamTotalDeadlineTest.java` | `00F8C03CC54A7F1841536141E9BA4FFDC41B75F62B13AD69564A61E9B734CE28` |
| `D20/D20_UMIEM/src/test/java/com/zl/d20/stream/StreamClientDisconnectIntegrationTest.java` | `59230103738D68BA30728D2E586710A52C3E5220FB8D0E22FE7BAB694AA0E6BE` |
| `D20/pom.xml` | `C6836621A833BCCB705C14306A3141B217951809AB67868A4BB02BB4BF494640` |

## Surefire 测试类摘要

```text
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 16.16 s -- in com.zl.d20.ApplicationContextLoadTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.012 s -- in com.zl.d20.ChatLengthValidationTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.842 s -- in com.zl.d20.conversation.RedisConversationMemoryTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.520 s -- in com.zl.d20.conversation.RoleConversationServiceTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.985 s -- in com.zl.d20.d16.CitationIntegrationTest
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.011 s -- in com.zl.d20.d16.CitationRegistryTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 4.051 s -- in com.zl.d20.d16.D16ThinkingStreamIntegrationTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.010 s -- in com.zl.d20.d16.KnowledgeChatOrchestratorTest
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.014 s -- in com.zl.d20.d16.KnowledgeRetrievalServiceTest
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.204 s -- in com.zl.d20.d16.ThinkingModeIntegrationTest
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.011 s -- in com.zl.d20.d16.ThinkingSettingsResolverTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.765 s -- in com.zl.d20.DeepSeekKeyProtectionTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 1, Time elapsed: 0.001 s -- in com.zl.d20.DeepSeekLiveSmokeTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.031 s -- in com.zl.d20.equipment.EquipmentCandidateParserTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.800 s -- in com.zl.d20.equipment.EquipmentChatControllerTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.017 s -- in com.zl.d20.equipment.EquipmentInformationAndClarificationTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.916 s -- in com.zl.d20.history.ContextWindowTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.308 s -- in com.zl.d20.history.HistoryConcurrencyTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.135 s -- in com.zl.d20.history.HistoryFailureTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.223 s -- in com.zl.d20.history.HistoryPersistenceTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.126 s -- in com.zl.d20.history.SessionIsolationTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.076 s -- in com.zl.d20.history.SpringLockDiagnosticTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.161 s -- in com.zl.d20.logging.AccessLogIntegrationTest
Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.014 s -- in com.zl.d20.logging.AccessLogMaskingTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.075 s -- in com.zl.d20.logging.AccessLogRealContainerTest
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.143 s -- in com.zl.d20.MockChatControllerTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.051 s -- in com.zl.d20.MockNoExternalApiTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.020 s -- in com.zl.d20.model.ModelCallPropertiesValidationTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.010 s -- in com.zl.d20.model.ModelFailureClassifierTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.596 s -- in com.zl.d20.model.ModelHttpClientTimeoutTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.510 s -- in com.zl.d20.model.ModelHttpRetryIntegrationTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.053 s -- in com.zl.d20.model.ModelRetryPolicyTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.172 s -- in com.zl.d20.prompt.PersistenceCacheIntegrationTest
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.342 s -- in com.zl.d20.prompt.PromptTemplateServiceTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.443 s -- in com.zl.d20.service.MockAiChatServiceStreamTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.242 s -- in com.zl.d20.stream.ChatStreamErrorEventTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.532 s -- in com.zl.d20.stream.ChatStreamIntegrationTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.117 s -- in com.zl.d20.stream.ChatStreamTimeoutTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.152 s -- in com.zl.d20.stream.ChatStreamTotalDeadlineTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.006 s -- in com.zl.d20.stream.MvcStackDecisionTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.867 s -- in com.zl.d20.stream.RoleChatStreamIntegrationTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.070 s -- in com.zl.d20.stream.SpringAiChatClientBehaviorTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.209 s -- in com.zl.d20.stream.StreamClientDisconnectIntegrationTest
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.939 s -- in com.zl.d20.stream.StreamContractCompatibilityTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.959 s -- in com.zl.d20.stream.StreamPreStartFailureTest
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.344 s -- in com.zl.d20.web.HistoryControllerWebTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.226 s -- in com.zl.d20.web.PromptTemplateControllerWebTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.171 s -- in com.zl.d20.web.RoleChatControllerTest
```
