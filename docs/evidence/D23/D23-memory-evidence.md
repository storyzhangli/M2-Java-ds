# D23 Memory 证据

## 权威存储

- 主链路 MySQL：`chat_session` + `chat_message`。
- Redis `ConversationMemory` 保留但不在主聊天链路。
- 会话标识：服务端生成/客户端传入 `sessionId`。
- 所有者：开发身份 `X-Dev-User`，不同 owner 隔离。

## 状态与窗口

- 轮次开始：user=PENDING。
- 成功：user/assistant=COMPLETED。
- 失败：user=FAILED，无 assistant。
- 中断：user/assistant=INTERRUPTED（有部分内容时）。
- 上下文只取最近 N 条 COMPLETED，按 sequenceNo 正序。

## 实际下一轮顺序证据

`ChatMemoryOrderRegressionTest` 使用记录型 `AiChatService` 捕获真实传给模型的 `ChatInvocation`：

- 第二轮 history = user「第一轮用户问题」、assistant「第一轮助手回答」。
- 当前轮问题独立在 `userMessage`。
- 新会话 history 为空。

这验证的是实际模型入参，而不仅是方法调用次数。

## 历史读取失败证据

`ChatMemoryReadFailureRegressionTest` 验证：

- 普通同步窗口读取失败：调用 failTurn。
- 角色准备窗口读取失败：调用 failTurn。
- SSE 窗口读取失败：在 start 前中止并调用 failTurn。

## 持久化与隔离

保留并通过：

- `HistoryPersistenceTest`
- `ContextWindowTest`
- `SessionIsolationTest`
- `HistoryFailureTest`
- `HistoryConcurrencyTest`
- `SpringLockDiagnosticTest`
