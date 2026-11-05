# D23 关键缺陷与修复

## 缺陷 1：结构化 JSON 接受尾随内容

- 复现：`EquipmentCandidateParserRegressionTest.trailingContentAfterValidObjectMustBeRejected`
- 修复前：合法候选对象后追加另一个 JSON 对象仍被解析。
- 影响：模型输出的非单一 JSON 文档可能被误当成合法候选。
- 修复：使用 Jackson `FAIL_ON_TRAILING_TOKENS` 严格读取。
- 修复后：定向测试通过。

## 缺陷 2：需要澄清但缺失信息为空

- 复现：`needsClarificationMustCarryAtLeastOneMissingInformationItem`
- 修复前：`candidateStatus=NEEDS_CLARIFICATION` 与空 `missingInformation` 同时存在仍被接受。
- 影响：结构化候选内部状态自相矛盾。
- 修复：增加跨字段业务规则，要求 `missingInformation` 非空。
- 修复后：定向测试通过。

## 缺陷 3：同步历史读取失败遗留 PENDING

- 复现：`ChatMemoryReadFailureRegressionTest.plainSyncHistoryReadFailureShouldFailTheStartedTurn`
- 根因：普通同步链路先 `startTurn`，历史窗口在 try 外读取；读取失败只抛异常，未 `failTurn`。
- 修复：将上下文读取纳入轮次异常收口范围。

## 缺陷 4：角色准备历史读取失败遗留 PENDING

- 复现：`ChatMemoryReadFailureRegressionTest.rolePreparationHistoryReadFailureShouldFailTheStartedTurn`
- 根因：`RoleConversationService.prepare` 在写入 PENDING 后读取窗口，缺少失败收口。
- 修复：startTurn 后的窗口读取异常统一 `failTurn` 并原样抛出。

## 缺陷 5：SSE 普通链路历史读取失败遗留 PENDING

- 复现：`ChatStreamHistoryReadFailureRegressionTest`（类 `ChatMemoryReadFailureRegressionTest` 内方法）
- 根因：流开始前构建历史窗口失败，轮次未收口、也未发送 SSE。
- 修复：普通 SSE 分支在 `startTurn` 后捕获窗口读取异常，`failTurn` 后抛出，仍走既有 JSON 错误契约。

## 回归

- 修复后定向：12/12 通过。
- 扩展回归：15/15 通过。
- 最终全量：194 项，0 失败，0 错误，1 跳过。
