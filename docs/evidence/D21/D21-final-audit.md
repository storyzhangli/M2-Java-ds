# D21 最终审计

## 仓库

- 分支：`master`
- HEAD：`10a3920bef10d4ef5856f1b58c87cf1fca4ea20f`
- 暂存文件：0
- 本轮未 commit、push、reset 或改写历史。
- 变更范围：`D21`、`docs/proj_docs/D21-*`、`docs/evidence/D21`、`docs/temp_docs/D21`、`logs/D21`。

## 测试与构建

- 最终全量：174 个测试，0 失败，0 错误，1 跳过。
- 跳过项：`DeepSeekLiveSmokeTest`，默认禁用，未发起真实 API 调用。
- 最终打包：成功。
- JAR 字节数：59,509,811。
- JAR SHA-256：`E707ECC844FA66157ECF99A4EC4C30AA92062CC6D48EDEE0FAE7F7AECCBE6199`。

## 静态检查

- tracked `git diff --check`：退出码 0。
- D21 文档/证据尾随空白：0。
- 工作区严格 Key 模式：0 个文件。
- Git HEAD 严格 Key 模式：0 个文件。
- 暂存 Diff 宽 Key 模式：0 行。
- 宽模式唯一命中为未修改的 D13 文档中显式无效 Mock 回退示例。

## 文档

- `docs/proj_docs/D21-项目迭代全量信息与操作.md`
- `docs/proj_docs/D21-聊天调用可观测性技术说明与实践指南.md`

## 未验证

- 真实 DeepSeek API 在线调用。
- 真实 Provider 本轮是否返回 usage。
- W3C traceparent 跨服务传播。
