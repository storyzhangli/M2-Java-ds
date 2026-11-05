# D23 聊天、SSE、Memory 与结构化输出技术说明与操作实践指南

## 1. 本文范围

本文说明：

- 同步聊天调用与失败收口。
- SSE 流式生命周期与兼容契约。
- MySQL 主 Memory 的会话、顺序、隔离与窗口。
- 结构化模型候选的严格解析与业务校验。
- D23 新增回归测试、关键修复和排障方法。

不重复项目基础架构，不把真实模型调用写成已验证。

## 2. 同步聊天

### 2.1 调用链

```text
ChatController
 -> ChatHistoryService.startTurn
 -> ChatHistoryService.getContextWindow
 -> KnowledgeChatOrchestrator
 -> AiChatService
 -> ChatHistoryService.completeTurn / failTurn
```

### 2.2 契约

- URL：`POST /api/v1/ai/chat`
- HTTP 层正常处理为 200。
- 统一结构 `Result{code,msg,data}`。
- 参数错误 4001。
- AI 配置/鉴权 5001。
- AI 暂不可用 5002。
- 未知系统问题 5000。

### 2.3 失败收口

`startTurn` 成功后，后续任一步失败都必须构造 FAILED 终态：

```text
startTurn
  ├─ context window read
  ├─ model invocation
  └─ response/history complete
```

D23 修复了 context window read 失败未进入 catch 的路径。

测试：

- `ChatModelFailureRegressionTest`
- `ChatMemoryReadFailureRegressionTest.plainSyncHistoryReadFailureShouldFailTheStartedTurn`

## 3. SSE

### 3.1 生命周期

```text
请求校验
 -> startTurn
 -> 构建历史窗口
 -> orchestrator.stream
 -> 订阅
 -> start
 -> thinking?/delta*
 -> citations?
 -> complete
```

异常：

```text
start
 -> delta*
 -> error
 -> close
```

### 3.2 关键规则

- `SseEmitter` 作为 MVC 异步响应。
- 首个 delta 前失败可回 JSON；已提交后只能发 error 事件。
- 已发送内容后不从头重放。
- 客户端断开取消上游。
- 有部分内容写 INTERRUPTED；无内容写 FAILED。
- 成功/失败/容器超时都只收口一次。

### 3.3 D23 修复

历史窗口构建发生在订阅和 start 事件前。D23 在普通 SSE 分支中为它增加异常收口：

```text
startTurn
 -> getContextWindow fails
 -> failTurn
 -> throw
 -> GlobalExceptionHandler JSON error
```

测试：

`ChatMemoryReadFailureRegressionTest.streamHistoryReadFailureShouldFailTheStartedTurnBeforeStartFrame`

## 4. Memory 主存储

### 4.1 存储

- `chat_session`：会话、owner、scene、模板版本、时间。
- `chat_message`：sequenceNo、role、content、status、error、model。
- Redis 组件不在主链路。

### 4.2 状态机

| 阶段 | user | assistant |
|---|---|---|
| 开始 | PENDING | 不存在 |
| 成功 | COMPLETED | COMPLETED |
| 模型失败 | FAILED | 不创建 |
| 流式中断且有部分内容 | INTERRUPTED | INTERRUPTED |

### 4.3 上下文窗口

- 仅读取 COMPLETED。
- 最近 N 条。
- 反转回 sequenceNo 正序。
- 若首条孤立 assistant，补上一条 user。
- 不同 session/owner 不混合。

### 4.4 实际顺序测试

`ChatMemoryOrderRegressionTest` 不通过文本包含判断，而是捕获下一轮实际 `ChatInvocation`：

```text
[user: 第一轮用户问题, assistant: 第一轮助手回答]
```

并验证当前问题独立、新会话为空。

### 4.5 历史读取失败

修复前：startTurn 已写入 PENDING，读取窗口失败后直接抛错。

修复后：角色/同步/SSE 都在轮次开始后统一调用 `failTurn`，避免残留 PENDING。

## 5. 结构化输出

### 5.1 输入不可信

模型生成 JSON 属于不可信输入，必须：

1. 严格 JSON 解析。
2. 检查顶层对象。
3. 必填字段非空。
4. 类型校验。
5. 枚举白名单。
6. 长度/数组上限。
7. 跨字段业务规则。
8. 映射为只读候选 DTO。

### 5.2 字段

`EquipmentTicketCandidate`：

- symptomSummary
- ticketCategory
- deviceModel/faultCode/deviceIdentifier
- observedSymptoms
- missingInformation
- uncertaintyNote
- candidateStatus
- sourceType
- candidateOnly=true

### 5.3 D23 修复

- `FAIL_ON_TRAILING_TOKENS`。
- `NEEDS_CLARIFICATION` 必须有缺失信息。

### 5.4 非法输入示例

```json
{
  "candidateStatus": "NEEDS_CLARIFICATION",
  "missingInformation": []
}
```

拒绝原因：需要澄清却没有说明缺失什么。

```json
{
  "symptomSummary": "...",
  "ticketCategory": "MECHANICAL"
}
{"unexpected": true}
```

拒绝原因：不是单一 JSON 文档。

## 6. 回归测试设计

### 6.1 同步

- 正常 Mock 响应。
- 空/缺失/非法 JSON。
- 最大长度。
- 401/503 映射。
- FAILED 落库与无 assistant。

### 6.2 SSE

- 事件顺序。
- start 带 session/template。
- complete 统计正确。
- error 不发送 complete。
- 首段后错误不重放。
- 客户端断开取消。
- 空闲超时与总 Deadline。
- start 前 owner/session 错误。

### 6.3 Memory

- PENDING→COMPLETED。
- 多轮序号。
- 窗口裁剪。
- 失败/中断不进窗口。
- 空历史。
- session/owner 隔离。
- 并发序号。
- 跨重启读回。
- 下一轮实际顺序。
- 历史读取失败。

### 6.4 结构化

- 合法映射。
- 非法 JSON/顶层/类型/枚举/字段。
- 超长/元素数。
- 尾随 token。
- 跨字段规则。

## 7. 执行命令

```powershell
cd E:\AI\project\AI-Learning-two-lp\M2-Java-ds\D23
```

修复前复现：

```powershell
mvn -f .\pom.xml "-Dtest=EquipmentCandidateParserRegressionTest,ChatMemoryReadFailureRegressionTest,ContextWindowTest" test
```

修复后扩展回归：

```powershell
mvn -f .\pom.xml "-Dtest=ChatModelFailureRegressionTest,ChatMemoryOrderRegressionTest,EquipmentCandidateParserRegressionTest,ChatMemoryReadFailureRegressionTest,ContextWindowTest" test
```

全量：

```powershell
mvn -f .\pom.xml test
mvn -f .\pom.xml -DskipTests package
```

## 8. 运行与调试

### 8.1 Mock 启动

```powershell
java -jar .\D23_CRTFC_SSE_MASO\target\D23_CRTFC_SSE_MASO-1.0-SNAPSHOT.jar
```

默认 Mock，不访问真实模型。

### 8.2 观察 SSE

使用支持 `text/event-stream` 的客户端请求：

```text
POST /api/v1/ai/chat/stream
Content-Type: application/json
Accept: text/event-stream

{"message":"测试","sessionId":"可选"}
```

检查：

- start 先出现且一次。
- delta seq 连续。
- complete 或 error 只能一个。

### 8.3 排查历史混合

1. 确认 sessionId 相同。
2. 确认 X-Dev-User 相同。
3. 查询 chat_session.owner_id。
4. 查询 chat_message.session_id/sequence_no/status。
5. 确认上下文中只有 COMPLETED。

### 8.4 排查 PENDING 残留

查看：

```sql
SELECT session_id, sequence_no, status, error_code
FROM chat_message
WHERE status = 'PENDING'
ORDER BY created_at DESC;
```

若发生在 `startTurn` 后、模型调用前，检查 Controller/RoleConversationService 是否执行 `failTurn`。

## 9. 已验证与未验证

已验证：

- 194 项全量测试通过（1 项真实 smoke 跳过）。
- 修复前 5 项失败可复现。
- 修复后 15 项扩展定向通过。
- 同步/SSE/Memory/结构化边界。
- MySQL 持久化与隔离。
- 包构建。

未验证：

- 真实 DeepSeek API 调用。
- 多实例部署下跨节点事务/分布式锁扩展。
- Redis ConversationMemory 主链路恢复（当前不是主链路）。

## 10. 证据

```text
E:\AI\project\AI-Learning-two-lp\M2-Java-ds\docs\evidence\D23
```

优先阅读：

1. `D23-test-summary.md`
2. `D23-defects-and-fixes.md`
3. `D23-memory-evidence.md`
4. `D23-structured-output-evidence.md`
5. `D23-acceptance-matrix.md`
