# D18 项目迭代全量实施记录：设备场景 Prompt、多轮会话与结构化候选

> 日期：2026-10-31  
> 项目根目录：`E:\AI\project\AI-Learning-two-lp\M2-Java-ds\D18`  
> 主模块：`D18_CbProjD15D16D17`  
> 对照项目：`E:\AI\project\AI-Learning-two-lp\M2-Java-ds\D17\D17_DHSA_MTDCA`

## 1. 背景与范围

D18 在 D17 的通用多角色对话、MySQL 会话历史、Prompt 模板持久化、知识检索和 DeepSeek/Mock 双实现基础上，补齐制造业设备客服与设备技术工程师场景。

本轮实际完成：

1. 新增设备客服、设备技术工程师两个稳定场景和版本化种子模板。
2. 补充模板不可变历史快照，使版本升级不会覆盖旧正文。
3. 新增设备型号、编号、类型、故障码、现象、时间、频率和安全状态的确定性归纳。
4. 新增先澄清后回答策略；关键信息不足时不调用模型。
5. 复用现有 MySQL 会话/消息服务，实现设备多轮对话、归属隔离和同会话串行化。
6. 新增只读故障摘要/工单分类候选 DTO、受控分类枚举和严格 JSON 解析器。
7. 默认路径完全使用 Mock/规则；真实 DeepSeek 测试另行显式授权。
8. 修复旧日志路径阻断，并移除真实模型路径中的完整 Prompt/响应正文日志。

本轮不做：

- 不实现知识库文档管理、向量检索或伪造引用。
- 不新增正式工单实体、Mapper、数据库表、建单、派单或审批流程。
- 不为了会话能力另建一套 Session/Message 表。
- 不默认调用真实 DeepSeek，不自动推断任务中未明确授权的计费调用。

## 2. 项目侦察与继承关系

### 2.1 技术基线

| 项目 | 实际值 |
|---|---|
| Maven | 3.9.16 |
| JDK | 21.0.7 |
| Spring Boot | 3.5.16 |
| Spring AI BOM | 1.1.8 |
| MyBatis-Plus | 3.5.17 |
| Web | Spring MVC / Tomcat |
| 会话主存储 | MySQL |
| 模板缓存 | Redis |
| 默认 AI 模式 | `mock` |
| Java 包名 | 继续使用 `com.zl.d17`，避免无收益的包迁移 |

### 2.2 D17 对照

逐文件哈希比较与源码扫描确认：

- D18 主体源码与 D17 基本一致。
- 主要初始差异是根 POM、模块 POM、启动类名称 `D18SpringAiApplication`。
- D18 已继承 D17 的 `PromptTemplateService`、MySQL 历史状态机、`KnowledgeChatOrchestrator`、Mock/DeepSeek 条件装配与测试设施。
- 详细对照摘要：`docs/evidence/D18/d18-d17-d18-file-diff-summary.txt`。

### 2.3 现状与复用决策

| 需求 | D17 现有实现 | D18 本轮前状态 | 结论 | 本轮处理 | 证据 |
|---|---|---|---|---|---|
| Prompt 场景与版本 | 六场景、版本号、变量校验、MySQL 持久化 | 缺设备角色；旧正文会被直接覆盖 | 部分满足需补齐 | 新增两个场景/模板；新增不可变版本表与查询 API | `PromptScenes`、`PromptTemplateService`、`schema-prompt-template.sql` |
| 缺失信息澄清 | 无设备事实和澄清策略 | 未实现 | 尚未实现 | 新增事实抽取、澄清优先级、高风险拦截 | `EquipmentInformationExtractor`、`EquipmentClarificationPolicy` |
| 会话 ID 与多轮历史 | MySQL 会话/消息、状态机、窗口、归属校验 | 已具备 | 已满足并复用 | 设备服务直接复用 `ChatHistoryService` | `schema-chat-history.sql`、`ChatHistoryService` |
| 会话存储抽象 | MySQL 为权威；Redis 旧实现不进入主链路 | 已具备 | 已满足并复用 | 不新建会话表；设备链路增加同会话分段锁 | `EquipmentChatService` |
| 结构化摘要与分类候选 | 通用 JSON 提取 Prompt | 无候选 DTO/枚举/严格解析 | 尚未实现 | 新增只读 DTO、受控字典和候选解析校验 | `EquipmentTicketCandidate`、`EquipmentCandidateParser` |
| Key 安全与 Mock | 默认 Mock、调用前校验 | 真实聊天路径打印完整 Prompt/响应 | 部分满足需补齐 | 删除正文日志；测试强制 Mock/占位 Key；在线测试增加授权开关 | `DeepSeekAiChatService`、模块 POM |

## 3. 基线验证与既有问题

### 3.1 原始基线

命令：

```powershell
cd E:\AI\project\AI-Learning-two-lp\M2-Java-ds\D18
mvn -f .\pom.xml test
```

结果：

```text
Tests run: 121, Failures: 0, Errors: 88, Skipped: 1
BUILD FAILURE
```

根因不是在 88 个测试中分别存在业务缺陷，而是：

1. `logback-spring.xml` 仍硬编码旧目录 `E:/AI/project/AI-Learning-one/M2-Java/logs`。
2. 当前沙箱无法写该路径，Logback 配置失败。
3. 后续完整 Spring 上下文初始化被失败阈值拦截，产生大量连带错误。

修复：

- 默认日志目录改为 `E:/AI/project/AI-Learning-two-lp/M2-Java-ds/logs`。
- 仍允许 `APP_LOG_HOME` 或 `-DAPP_LOG_HOME` 覆盖。
- 应用名和健康检查中的旧 D17 标识一并更新为 D18。

### 3.2 测试关键修复

| 问题 | 处理 | 验证 |
|---|---|---|
| 旧日志路径阻断 | 改为当前项目 logs 目录 | 上下文测试通过 |
| 测试继承宿主真实 Key | POM 强制 `app.ai.mode=mock`、无效占位 Key、受控测试环境 | 133 个测试不依赖真实 Key |
| 在线测试只看“有 Key”就运行 | 新增 `DEEPSEEK_LIVE_TEST_AUTHORIZED=true` 第三重条件 | 本轮明确跳过 |
| Surefire 报告记录受污染 `java.library.path` | 测试 JVM 使用受控 JDK 原生库路径 | 最终报告敏感模式 0 |
| 模板更新只保留新行 | 更新前后都归档快照 | v1/v2 均可查询 |

## 4. Prompt 模板与版本化设计

### 4.1 场景标识

新增：

- `equipment_customer_service`
- `equipment_technical_engineer`

两者加入既有 `PromptScenes.CHAT_ROLE_SCENES`，未增加含义重复的场景枚举。

### 4.2 模板元数据

复用现有字段：

| 要求 | 项目字段 |
|---|---|
| 模板稳定 ID | `templateId` |
| 版本 | `version` |
| 场景 | `scene` |
| 名称/说明 | `name` / `description` |
| 变量定义 | `variables` |
| 正文 | `content` |
| 更新时间 | `updatedAt` |
| 状态 | `status` |

设备客服模板声明：

- `known_information`：系统归纳的已知信息。
- `missing_information`：当前缺口。
- `business_context`：调用方提供且已受控的业务背景。

设备技术工程师模板使用同一组变量。变量均非必填，默认值明确说明“当前信息以会话为准”，避免缺少业务变量时生成残缺 Prompt。

### 4.3 历史版本可追溯

新增表：

```text
prompt_template_version
```

关键约束：

```text
UNIQUE KEY uk_template_version (template_id, version)
```

规则：

- 当前生效模板仍在 `prompt_template`。
- `prompt_template_version` 只插入、不更新。
- 创建模板后归档 v1。
- 更新时先归档旧版本，再写当前模板并归档新版本。
- 应用启动时为存量模板幂等补齐至少一个当前版本快照。
- 逻辑删除不删除历史快照。
- 查询接口：`GET /api/v1/ai/prompts/{templateId}/versions`。

数据库证据显示两个设备模板均为 v1、启用、3 个变量，且各有一条 v1 快照：

`docs/evidence/D18/d18-database-evidence.txt`

## 5. 设备对话实现

### 5.1 处理顺序

```text
校验设备场景
→ 同会话分段锁
→ 校验当前会话归属和场景
→ 读取最近 COMPLETED 用户消息
→ 与当前消息合并且提取设备事实
→ 生成缺失项和候选
→ 渲染设备 Prompt
→ 关键信息不足：本地澄清并结束本轮
→ 信息足够：调用既有模型编排器
→ 完成/失败消息落库
→ 返回回答、事实、缺口、方向和只读候选
```

### 5.2 会话复用

直接复用：

- `ChatHistoryService.startTurn`
- `ChatHistoryService.getContextWindow`
- `ChatHistoryService.completeTurn`
- `ChatHistoryService.failTurn`
- `ChatHistoryService.requireOwnedSession`

没有新增 Session、Conversation、ChatMessage 表，也没有第二套历史服务。

同会话使用 64 个固定分段锁串行化设备编排，避免两轮同时读取旧事实后交错。不同 session 通常落在不同锁；相同锁桶只降低并行度，不破坏正确性。该锁是单实例内的补充约束，跨实例仍依靠 MySQL 会话行锁和消息序号唯一约束。

### 5.3 归属隔离

- 会话 ID 不存在时由 `ChatHistoryService` 生成 UUID 去横线。
- 续聊时先校验 `owner_id`。
- 非归属用户与不存在会话统一返回“会话不存在或无权访问”。
- 同一会话场景不可切换；客服和工程师之间需要新建会话。

## 6. 澄清与不确定性

### 6.1 事实提取

`EquipmentInformationExtractor` 只解析用户消息，不解析助手推测内容。

可识别：

- 设备型号；
- 设备编号/序列号/产线位置；
- 设备类型；
- 故障码/报警码；
- 包含故障关键词的原始子句；
- 发生时间；
- 发生频率；
- 当前运行/停机/隔离状态；
- 高风险关键词。

缺失值保持空值，不生成“看起来合理”的型号、代码或传感器值。

### 6.2 澄清优先级

1. 无设备身份：询问设备型号或可识别身份。
2. 无故障现象：询问可观察到的报警、异响、振动、温度、停机等现象。
3. 高风险且安全状态未知：先询问是否停机、断电或隔离。
4. 故障码未提供：保留在缺失清单，但不阻断所有回答；提示用户有报警码时原样提供。

### 6.3 不确定性表达

正常回答明确返回：

- `knownFacts`：仅由用户原话支持的事实；
- `missingInformation`：仍缺信息；
- `possibleDirections`：前缀带有“尚未验证”的观察方向；
- `uncertaintyNote`：未经现场验证、未提供故障码时不推测代码或根因。

高风险信息不足时返回规则澄清，不调用模型，不给出带电、带压、高温接触或旋转部件操作步骤。

### 6.4 引用边界

- 设备接口默认 `retrieveEnabled=false`，响应 `citations=[]`。
- 如调用方显式请求检索，仍由既有 `KnowledgeChatOrchestrator` 使用真实语料命中结果生成引用。
- 无真实命中时不生成来源、页码、URL 或“知识库已确认”。
- 设备接口的访问日志已从通用正文日志排除，避免记录完整故障描述和候选摘要。

## 7. 结构化候选

### 7.1 DTO

`EquipmentTicketCandidate` 包含：

| 字段 | 说明 |
|---|---|
| `symptomSummary` | 已识别用户现象的直接汇总 |
| `ticketCategory` | 受控候选分类 |
| `deviceModel` | 用户明确提供的型号，缺失为 null |
| `faultCode` | 用户明确提供的代码，缺失为 null |
| `deviceIdentifier` | 用户明确提供的设备编号 |
| `observedSymptoms` | 原始现象子句列表 |
| `missingInformation` | 待补充信息 |
| `uncertaintyNote` | 不确定性说明 |
| `candidateStatus` | `CANDIDATE` / `NEEDS_CLARIFICATION` |
| `sourceType` | `USER_DESCRIPTION` / `CONVERSATION_SUMMARY` |
| `candidateOnly` | 固定 true |

候选分类：

```text
MECHANICAL
THERMAL_COOLING
ELECTRICAL
CONTROL_SENSOR
SAFETY_RISK
UNCLASSIFIED
```

这些是 AI 候选字典，不是正式工单分类表。

### 7.2 严格解析

`EquipmentCandidateParser` 校验：

- 顶层必须是 JSON 对象；
- 必填摘要和不确定性说明；
- 枚举值必须在白名单；
- 数组字段类型；
- 摘要最长 500；
- 数组最多 20 项；
- 单项最长 200；
- 空字符串过滤；
- 无法解析时统一抛出受控 `IllegalArgumentException`。

默认设备对话使用本地确定性投影，不额外调用一次模型。严格解析器供未来真实模型结构化适配或测试复用。

### 7.3 禁止副作用

设备模块中不存在：

- 正式工单 Mapper/Repository；
- 工单业务表；
- 建单服务调用；
- 派单调用；
- 候选结果自动落库。

候选只出现在 HTTP 响应对象中。

## 8. API 与运行方式

### 8.1 设备对话

```http
POST /api/v1/equipment/chat
X-Dev-User: demo-owner
Content-Type: application/json
```

```json
{
  "message": "设备运行中一直异常振动",
  "scene": "equipment_customer_service"
}
```

第一次响应会返回 32 位会话 ID。继续对话：

```json
{
  "message": "型号是 CNC-760，仍然有异常振动",
  "scene": "equipment_customer_service",
  "sessionId": "第一次响应中的会话ID"
}
```

### 8.2 模板接口

- `GET /api/v1/ai/roles`
- `GET /api/v1/ai/prompts`
- `GET /api/v1/ai/prompts/{templateId}`
- `GET /api/v1/ai/prompts/{templateId}/versions`
- `POST /api/v1/ai/prompts`
- `PUT /api/v1/ai/prompts/{templateId}`
- `PATCH /api/v1/ai/prompts/{templateId}/status`
- `DELETE /api/v1/ai/prompts/{templateId}`（逻辑删除）

### 8.3 数据库迁移

新增/更新脚本：

`src/main/resources/db/schema-prompt-template.sql`

回滚：

`src/main/resources/db/rollback-d18-prompt-template-version.sql`

回滚只删除本轮新增的 `prompt_template_version`，不删除原模板、会话或消息数据。本轮已对本机 `ai_db` 执行幂等 `CREATE TABLE IF NOT EXISTS`，并验证两个设备模板及快照数据。

## 9. 文件变更清单

### 9.1 新增主要代码

- `equipment/EquipmentChatController.java`
- `equipment/EquipmentChatService.java`
- `equipment/EquipmentInformationExtractor.java`
- `equipment/EquipmentClarificationPolicy.java`
- `equipment/EquipmentCandidateExtractor.java`
- `equipment/EquipmentCandidateParser.java`
- `equipment/EquipmentFacts.java`
- `equipment/dto/EquipmentChatResponse.java`
- `equipment/model/EquipmentTicketCandidate.java`
- `equipment/model/TicketCategory.java`
- `equipment/model/CandidateStatus.java`
- `equipment/model/CandidateSourceType.java`
- `prompt/PromptTemplateVersionRepository.java`
- `prompt/MybatisPromptTemplateVersionRepository.java`
- `prompt/PromptTemplateVersionService.java`
- `prompt/PromptTemplateVersionInitializer.java`
- `prompt/dto/PromptTemplateVersionView.java`
- `prompt/po/PromptTemplateVersionPO.java`
- `prompt/mapper/PromptTemplateVersionMapper.java`

### 9.2 修改

- `PromptScenes`：增加两个设备场景。
- `PromptTemplateService`：创建/更新时写版本快照。
- `PromptTemplateController`：增加版本历史接口。
- `PromptTemplateRepository`：增加版本回填查询口径。
- `MybatisPromptTemplateRepository`：支持包含逻辑删除的存量模板回填。
- `seed-templates.json`：增加设备客服和工程师模板。
- `application.yml`：D18 应用名、设备日志排除路径。
- `logback-spring.xml`：当前 logs 默认路径。
- `DeepSeekAiChatService`：删除完整 Prompt/响应正文日志。
- `ChatController`：健康检查应用名更新。
- 模块 POM：测试 Mock/占位 Key/授权隔离。

### 9.3 测试新增/修改

- `EquipmentInformationAndClarificationTest`
- `EquipmentCandidateParserTest`
- `EquipmentChatControllerTest`
- `PromptTemplateServiceTest`
- `PromptTemplateControllerWebTest`
- `ApplicationContextLoadTest`
- `DeepSeekKeyProtectionTest`
- `DeepSeekLiveSmokeTest`
- `AbstractPersistenceCleanupTest`

本轮未删除项目源码、用户日志、临时文件或证据。

## 10. 测试结果

### 10.1 定向测试

```powershell
mvn -f .\pom.xml -pl D18_CbProjD15D16D17 `
  -Dtest='EquipmentInformationAndClarificationTest,EquipmentCandidateParserTest,ApplicationContextLoadTest,PromptTemplateServiceTest,PromptTemplateControllerWebTest,EquipmentChatControllerTest' test
```

结果：29/29 通过。

### 10.2 最终全量回归

```powershell
cd E:\AI\project\AI-Learning-two-lp\M2-Java-ds\D18
mvn -f .\pom.xml test
```

结果：

```text
Tests run: 133, Failures: 0, Errors: 0, Skipped: 1
BUILD SUCCESS
```

跳过项是 `DeepSeekLiveSmokeTest`，需要独立的 `DEEPSEEK_LIVE_TEST_AUTHORIZED=true`。本轮未发起真实 DeepSeek API 调用。

证据：

- `docs/evidence/D18/d18-test-summary.txt`
- `docs/evidence/D18/D18-full-regression-03-final.log`
- `docs/evidence/D18/D18-targeted-tests-02.log`
- `docs/evidence/D18/D18-baseline-test.log`
- `docs/evidence/D18/D18-baseline-context-after-log-path-fix.log`

## 11. API Key 安全复核与事件

### 11.1 最终状态

| 检查项 | 结果 |
|---|---|
| 当前工作区文件是否命中真实 Key 模式 | 否（0） |
| 当前暂存区 diff | 否（0） |
| 默认测试是否使用 Mock/占位 Key | 是 |
| 是否发起真实 DeepSeek 调用 | 否 |
| 最终日志/报告是否含 Key 模式 | 否（0） |
| Git HEAD 是否仍含历史敏感模式 | 是（1 个旧文档路径） |

扫描报告：

`docs/evidence/D18/d18-sensitive-pattern-scan.txt`

### 11.2 本轮安全事件

第一次修复日志路径后运行旧测试时，旧断言把宿主环境中的真实 Key 写入标准输出和 Surefire XML。发现后立即采取：

- 停止真实模式验证。
- 对生成的日志、Surefire 报告和命中文档按模式遮蔽。
- 测试 JVM 强制无效占位 Key 和 Mock 模式。
- 在线冒烟测试增加独立授权开关。
- 测试 JVM 限制外部 PATH 派生属性进入报告。
- 重新执行全量测试并确认当前工作区、日志和最终报告敏感模式为 0。

### 11.3 尚未解除的风险

敏感模式仍存在于 Git HEAD 的旧文档：

`docs/proj_docs/D13-idea设置项目的环境变量.md`

当前工作树已遮蔽该内容，但原值仍在 Git 历史中；`origin/master` 包含 HEAD。该风险不能通过本轮代码提交消除。

必须由用户完成：

1. 立即在 DeepSeek 控制台轮换/吊销受影响 Key。
2. 评估远程仓库访问范围是否可信。
3. 在明确授权后使用 `git filter-repo` 等工具清理历史，并强制更新远程；不要在未备份和未协调团队前重写共享历史。
4. 轮换后重新扫描提交、历史、CI 日志和日志服务器。

本轮未提交、未推送、未改写 Git 历史。

## 12. Git 状态

实际状态：

```text
AM D18/D18_CbProjD15D16D17/pom.xml
A  D18/pom.xml
 M docs/proj_docs/D13-idea设置项目的环境变量.md
?? D18/D18_CbProjD15D16D17/src/
?? docs/evidence/D18/
?? docs/temp_docs/D18/
```

说明：

- 两个 D18 POM 在本轮开始前已由用户暂存，本轮保留并增量修改。
- D18 源码原本未跟踪，本轮未擅自提交。
- D13 文档仅做敏感值遮蔽。
- 本轮没有 commit、push、tag 或远程写操作。

## 13. 日志、临时材料和证据索引

日志目录：

`E:\AI\project\AI-Learning-two-lp\M2-Java-ds\logs`

关键日志：

- `D18-baseline-test.log`
- `D18-baseline-context-after-log-path-fix.log`
- `D18-compile-01.log`
- `D18-targeted-tests-01.log`
- `D18-targeted-tests-02.log`
- `D18-full-regression-01.log`
- `D18-full-regression-02-safe.log`
- `D18-full-regression-03-final.log`

临时材料：

- `docs/temp_docs/D18/D18-现状盘点与复用决策.md`
- `docs/temp_docs/D18/ApplyD18Schema.java`
- `docs/temp_docs/D18/ApplyD18Schema.class`
- `docs/temp_docs/D18/QueryD18Evidence.java`
- `docs/temp_docs/D18/QueryD18Evidence.class`

证据：

- `docs/evidence/D18/d18-test-summary.txt`
- `docs/evidence/D18/d18-database-evidence.txt`
- `docs/evidence/D18/d18-sensitive-pattern-scan.txt`
- `docs/evidence/D18/d18-final-git-status.txt`
- `docs/evidence/D18/d18-final-unstaged-diff-stat.txt`
- `docs/evidence/D18/d18-final-staged-diff-stat.txt`
- `docs/evidence/D18/d18-git-remote-contains-head.txt`
- `docs/evidence/D18/d18-sensitive-file-git-log.txt`

没有自动删除任何临时脚本、日志或证据。

## 14. 限制与后续建议

1. 设备事实抽取是确定性规则，不会覆盖所有工业设备命名变体。真实生产应维护受控设备字典，并保留用户原文作为候选溯源。
2. 当前候选分类是内部演示字典，正式上线前应与正式工单分类字典做映射和人工确认。
3. 分段锁是单实例补充，多实例部署时如要求严格对话时序，需要引入协调机制或对同会话请求做入口限流。
4. 会话窗口按消息条数而非 Token 预算；长故障描述或多工具输出增多后应增加 Token 预算治理。
5. 真实 DeepSeek 联调未执行，真实模型对结构化解析器输出的实际遵循程度仍需另行授权验证。
6. D13 历史文档中的旧 Key 必须轮换并清理历史，这是当前最高优先级遗留安全事项。
