# D21 敏感信息扫描

## 扫描策略

- 不读取、打印、复制或落盘真实环境变量值。
- 只记录命中文件路径和数量，不输出凭据内容。
- 同时检查工作区、Git HEAD 已跟踪文件、暂存 Diff 和最终 D21 运行日志。

## 结果

| 范围/模式 | 命中 |
|---|---:|
| 工作区严格模式 `sk-[A-Za-z0-9]{20,}` | 0 |
| 工作区宽模式 `sk-[A-Za-z0-9_-]{20,}` | 1 个既有文件 |
| Git HEAD 严格模式 | 0 个文件 |
| 暂存 Diff 宽模式 | 0 行 |
| D21 `MODEL_CALL` 日志中的测试 Prompt/Completion/伪 Key/异常消息 | 0 |

宽模式唯一命中的是既有且本轮未修改的文件：

```text
docs/proj_docs/D13-02_SpringAI初始化与DeepSeek接入-迭代规划任务书.md
```

脱敏上下文确认其为 `${DEEPSEEK_API_KEY:...}` 的显式无效 Mock 回退示例，不是真实凭据。

## 日志脱敏验证

最终全量日志中以下文本在 `MODEL_CALL` 中均为 0 次：

- `full prompt must not enter logs`
- `full model answer must not enter logs`
- `non-placeholder-observability-test-key`
- `Bearer must-not-log`
- `must-be-masked-cookie`
- `访问日志关联测试`

通用访问日志中的 `message`、`content`、`reasoning_content` 被替换为 `*****`；Authorization 与 Cookie 头整体掩码。

## Git 状态

- 未执行 commit、push、reset。
- 未删除用户文件。
- 最终暂存文件数：0；本轮未主动 `git add`。
- `git diff --check` 退出码：0。
- D21 新增文档/证据尾随空白行数：0。
