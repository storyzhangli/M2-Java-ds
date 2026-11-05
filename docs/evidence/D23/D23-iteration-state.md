# D23 迭代状态

## 需求编号

- D23-R01 同步聊天回归
- D23-R02 SSE 流式回归
- D23-R03 Memory 多轮上下文
- D23-R04 结构化输出
- D23-R05 关键问题修复

## 修改文件

### 生产代码

- `D23/D23_CRTFC_SSE_MASO/src/main/java/com/zl/d23/controller/ChatController.java`
- `D23/D23_CRTFC_SSE_MASO/src/main/java/com/zl/d23/controller/ChatStreamController.java`
- `D23/D23_CRTFC_SSE_MASO/src/main/java/com/zl/d23/conversation/RoleConversationService.java`
- `D23/D23_CRTFC_SSE_MASO/src/main/java/com/zl/d23/equipment/EquipmentCandidateParser.java`

### 新增测试

- `ChatModelFailureRegressionTest.java`
- `ChatMemoryOrderRegressionTest.java`
- `ChatMemoryReadFailureRegressionTest.java`
- `EquipmentCandidateParserRegressionTest.java`

### 修改测试

- `ContextWindowTest.java`：新增仅有 PENDING 时窗口为空。

## 设计决定

- 复用既有 ChatHistoryService 和 SSE 生命周期，不重构控制器。
- 轮次 `startTurn` 成功后发生的上下文读取/模型异常统一 `failTurn`。
- 角色准备的窗口读取失败在 RoleConversationService 内收口。
- SSE 普通链路在发送 start 前完成窗口读取；失败走既有 JSON 错误。
- 结构化 JSON 使用 `FAIL_ON_TRAILING_TOKENS`。
- 增加 `NEEDS_CLARIFICATION -> missingInformation 非空` 跨字段规则。

## 测试

- 修复前：12 项定向测试，5 项失败。
- 修复后：12/12 通过。
- 扩展回归：15/15 通过。
- 最终全量：194 项，0 失败，0 错误，1 跳过。
- 打包成功。

## 证据

- 测试摘要：`D23-test-summary.md`
- 缺陷记录：`D23-defects-and-fixes.md`
- Memory：`D23-memory-evidence.md`
- 结构化输出：`D23-structured-output-evidence.md`
- 完整日志：`logs/D23`
- Git Diff：`D23-git-diff.patch`
- 安全扫描：`D23-sensitive-scan.md`

## Token 统计

- 真实模型请求：0。
- 真实 Token/费用：未产生。
- Agent Token：平台不可观测，不估算。

## 遗留问题

- 未执行真实 DeepSeek API 调用。
- Redis ConversationMemory 非主链路，仅保留兼容资产。
