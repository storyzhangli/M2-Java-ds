# D23 结构化输出证据

## 解析边界

`EquipmentCandidateParserRegressionTest` 覆盖：

- 合法对象后跟随额外 JSON：拒绝。
- `NEEDS_CLARIFICATION` 但 `missingInformation=[]`：拒绝。
- 字符串字段传数字：拒绝。
- 数组字段传字符串：拒绝。
- 未知 candidateStatus/sourceType：拒绝。
- 数组超过 20 个元素：拒绝。
- 数组元素非字符串：拒绝。

原有 `EquipmentCandidateParserTest` 继续覆盖：

- 合法候选映射。
- 非法 JSON。
- 未知 ticketCategory。
- 缺失 symptomSummary。
- 超长摘要。

## 候选安全

- `candidateOnly` 固定为 true。
- 结构化结果没有正式工单 Mapper 或写库命令。
- 解析失败为受控 IllegalArgumentException。
- 非法模型输出不会进入正式业务流程。
