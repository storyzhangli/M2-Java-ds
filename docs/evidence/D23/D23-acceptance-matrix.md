# D23 最终验收矩阵

| 编号 | 验收要求 | 实际结果 | 证据 | 状态 |
|---|---|---|---|---|
| D23-AC01 | 同步聊天契约兼容 | 正常、参数、异常码与历史 FAILED 回归通过 | `ChatModelFailureRegressionTest`、既有聊天测试 | 通过 |
| D23-AC02 | SSE 顺序/完成/错误/取消 | 既有真实 HTTP 和事件顺序测试全部通过；新增历史读取失败前收口 | stream 测试、全量日志 | 通过 |
| D23-AC03 | Memory 会话、顺序、隔离 | 捕获真实下一轮 history 顺序；新会话空历史；MySQL 持久化/隔离/并发通过 | `ChatMemoryOrderRegressionTest` 等 | 通过 |
| D23-AC04 | 结构化输出合法/非法边界 | 严格 JSON、枚举、类型、数组、跨字段规则通过 | `EquipmentCandidateParserRegressionTest` | 通过 |
| D23-AC05 | 关键缺陷有复现与修复 | 修复前 5 个失败，修复后定向 12/12 与扩展 15/15 通过 | `D23-defects-and-fixes.md` | 通过 |
| D23-AC06 | 回归真实执行 | 最终 194 项，0 失败，0 错误，1 跳过 | `D23-test-summary.md` | 通过 |
| D23-AC07 | 无接口破坏/敏感信息/无关变更 | 接口未改；Git/敏感扫描见证据；仅 D23 范围内代码 | Git Diff、敏感扫描 | 通过 |
| D23-AC08 | 两份正式文档 | 两份指定文档已由状态记录和证据生成 | `D23-聊天-SSE-Memory-结构化输出回归测试与关键问题修复-项目迭代全量信息与操作.md`、`D23-聊天-SSE-Memory-结构化输出回归测试与关键问题修复-技术说明与操作实践指南.md` | 通过 |
