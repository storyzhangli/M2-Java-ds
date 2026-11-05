# D23 项目迭代全量信息与操作

## 1. 文档信息

| 项目 | 内容 |
|---|---|
| 迭代 | D23：聊天、SSE、Memory 与结构化输出回归测试及关键问题修复 |
| 项目根目录 | `E:\AI\project\AI-Learning-two-lp\M2-Java-ds\D23` |
| 基础骨架 | `D23\D23_CRTFC_SSE_MASO` |
| 文档日期 | 2026-11-05 |
| Git HEAD | `0d098e67f68dfbabfda0863221d02227277f1d59` |
| 最终结果 | 194 个测试，0 失败，0 错误，1 跳过 |
| 真实 API | 未调用 |

## 2. 需求与结果

| 编号 | 范围 | 结果 |
|---|---|---|
| D23-R01 | 同步聊天正常、参数、异常、契约 | 通过 |
| D23-R02 | SSE 顺序、完成、异常、取消、超时 | 通过 |
| D23-R03 | Memory 会话、顺序、隔离、失败、恢复 | 通过 |
| D23-R04 | 结构化输出合法映射与非法输入 | 通过 |
| D23-R05 | 测试确认问题的关键修复 | 通过 |

## 3. 实际基线

- JDK 21
- Maven 3.9.16
- Spring Boot 3.5.16
- Spring AI 1.1.8
- 模块：`D23(pom) -> D23_CRTFC_SSE_MASO(jar)`
- 基线全量：182 项通过，1 项真实 API smoke 跳过

未升级依赖，未新增框架、表或外部服务。

## 4. 测试缺口

现有测试已覆盖大部分链路，本轮新增验证确认：

1. 结构化候选 JSON 接受合法对象后的尾随内容。
2. `NEEDS_CLARIFICATION` 可与空 `missingInformation` 同时出现。
3. 同步普通链路在历史窗口读取失败后未收口 PENDING。
4. 角色准备阶段历史读取失败后未收口 PENDING。
5. SSE 普通链路历史读取失败后未收口 PENDING。

修复前定向测试 12 项中 5 项失败，完整日志：

```text
logs\D23\D23-pre-fix-regression.log
```

## 5. 关键修复

### 5.1 严格结构化 JSON

`EquipmentCandidateParser` 使用：

```java
objectMapper.reader()
    .with(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
    .readTree(json)
```

合法对象后的第二段内容不再被忽略。

### 5.2 跨字段候选规则

当：

```text
candidateStatus = NEEDS_CLARIFICATION
```

`missingInformation` 必须至少包含一项，否则拒绝。

### 5.3 Memory 失败收口

轮次 `startTurn` 成功后，只要历史窗口读取失败：

- 普通同步链路捕获并 `failTurn`。
- SSE 普通链路在发送 start 前捕获并 `failTurn`，仍返回既有错误契约。
- `RoleConversationService.prepare` 在角色链路中捕获并 `failTurn`。

不会留下 PENDING 用户消息。

## 6. 修改文件

### 生产代码

- `controller/ChatController.java`
- `controller/ChatStreamController.java`
- `conversation/RoleConversationService.java`
- `equipment/EquipmentCandidateParser.java`

### 新增测试

- `ChatModelFailureRegressionTest.java`
- `ChatMemoryOrderRegressionTest.java`
- `ChatMemoryReadFailureRegressionTest.java`
- `EquipmentCandidateParserRegressionTest.java`

### 修改测试

- `ContextWindowTest.java`

没有修改 POM。

## 7. 同步聊天

保持：

- `POST /api/v1/ai/chat`
- HTTP 200 + `code/msg/data`
- 正常响应字段
- 既有 4001/5001/5002/5000 错误码

新增回归：

- 401 类不可重试错误返回 5001，用户消息 FAILED，无助手消息。
- 503 类可重试错误耗尽后返回 5002，失败轮次不进入下轮上下文。

## 8. SSE

保持：

- `POST /api/v1/ai/chat/stream`
- 事件 `start/thinking/delta/citations/complete/error`
- 顺序与 UTF-8
- 首段后不重放
- 客户端断开取消上游
- 超时与总 Deadline

新增历史读取失败场景：

- 在 SSE start 前失败。
- 不返回 event-stream 成功帧。
- 已开始轮次 FAILED。

## 9. Memory

主链路：

```text
ChatController / ChatStreamController
 -> ChatHistoryService
 -> MySQL chat_session/chat_message
```

关键规则：

- `sessionId` 关联同一会话。
- `ownerId` 隔离不同开发身份。
- sequenceNo 保证顺序。
- 只有 COMPLETED 消息进入下一轮。
- 失败/中断/PENDING 不污染上下文。
- 空历史返回空列表。
- 数据库持久化可在新读取中恢复。

新增 `ChatMemoryOrderRegressionTest` 捕获下一轮实际 `ChatInvocation.history()`：

```text
user: 第一轮用户问题
assistant: 第一轮助手回答
```

当前轮问题仍在 `userMessage`；新会话 history 为空。

## 10. 结构化输出

`EquipmentTicketCandidate` 只作为候选：

- `candidateOnly=true`
- 无正式工单写库
- 非法 JSON/缺失/类型/枚举/超长/数组越界均拒绝

新增覆盖：

- 尾随 token。
- `NEEDS_CLARIFICATION` 空缺失信息。
- 非字符串数组元素。
- 21 个数组元素。
- 数字代替字符串字段。
- 未知状态/来源枚举。

## 11. 测试操作

### 11.1 定向

```powershell
cd E:\AI\project\AI-Learning-two-lp\M2-Java-ds\D23
mvn -f .\pom.xml "-Dtest=ChatModelFailureRegressionTest,ChatMemoryOrderRegressionTest,EquipmentCandidateParserRegressionTest,ChatMemoryReadFailureRegressionTest,ContextWindowTest" test
```

结果：15/15 通过。

### 11.2 全量

```powershell
mvn -f .\pom.xml test
```

结果：

```text
Tests run: 194, Failures: 0, Errors: 0, Skipped: 1
```

### 11.3 打包

```powershell
mvn -f .\pom.xml -DskipTests package
```

结果：成功。

## 12. 产物

```text
D23\D23_CRTFC_SSE_MASO\target\D23_CRTFC_SSE_MASO-1.0-SNAPSHOT.jar
```

- 字节数：59,515,292
- SHA-256：`46218350CB9AD2C1029CFC87FFE811F7667FB9538C395E79E4B462A642233A11`

## 13. Git 与证据

- 分支：`master`
- HEAD：`0d098e67f68dfbabfda0863221d02227277f1d59`
- 未 commit、push、reset。
- 仅修改 D23 相关源码/测试及指定文档、日志、证据、临时目录。

证据目录：

```text
E:\AI\project\AI-Learning-two-lp\M2-Java-ds\docs\evidence\D23
```

## 14. 最终验收

| 编号 | 状态 | 依据 |
|---|---|---|
| D23-AC01 | 通过 | 同步契约、参数、异常码与失败状态回归 |
| D23-AC02 | 通过 | SSE 顺序、完成、错误、超时、取消、断开 |
| D23-AC03 | 通过 | 实际下一轮历史顺序、隔离、持久化、空历史 |
| D23-AC04 | 通过 | 结构化输出边界和业务规则 |
| D23-AC05 | 通过 | 5 个修复前失败已复现，修复后全部通过 |
| D23-AC06 | 通过 | 194 项全量测试和日志 |
| D23-AC07 | 通过 | Git Diff、敏感信息扫描 |
| D23-AC08 | 通过 | 本文件与技术实践指南 |

## 15. 遗留问题

- 未执行真实 DeepSeek API 烟雾测试。
- Redis ConversationMemory 保留但不属于主聊天 Memory 链路。
- 测试使用开发身份 Header，不是生产认证机制。

## 16. 证据索引

- `D23-baseline-index.md`
- `D23-test-summary.md`
- `D23-defects-and-fixes.md`
- `D23-memory-evidence.md`
- `D23-structured-output-evidence.md`
- `D23-runtime-evidence.md`
- `D23-iteration-state.md`
- `D23-acceptance-matrix.md`
- `D23-sensitive-scan.md`
- `D23-git-diff.patch`
