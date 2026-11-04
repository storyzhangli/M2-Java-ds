# D22 敏感信息扫描

## 扫描结果

| 范围/模式 | 命中 |
|---|---:|
| 工作区严格模式 `sk-[A-Za-z0-9]{20,}` | 0 |
| 工作区宽模式 `sk-[A-Za-z0-9_-]{20,}` | 1 个既有文件 |
| Git HEAD 严格模式 | 0 个文件 |
| 暂存 Diff 宽模式 | 0 行 |
| 旧 `DEEPSEEK_*` 环境变量引用 | 0 行 |

宽模式唯一命中：

```text
docs/proj_docs/D13-02_SpringAI初始化与DeepSeek接入-迭代规划任务书.md
```

该文件中是既有、未修改的显式无效 Mock 回退示例，不是真实凭据。

## 配置安全

- 主配置 `AI_API_KEY` 默认为空。
- `.example` 配置不包含真实或伪造 Key。
- 测试 JVM 使用显式无效值。
- `AiProperties.toString()` 排除了 API Key，测试已验证。
- 未读取或输出宿主真实 `AI_API_KEY`。
- 未创建或提交真实密钥文件。

## Git

- 未执行 commit、push、reset。
- 最终暂存文件数：0。
- `git diff --check` 退出码：0。
- D22 文档/证据尾随空白：0。

## 说明

真实 Provider 调用会使 Provider 使用凭据；本轮未执行，因此不存在真实请求凭据落盘。
