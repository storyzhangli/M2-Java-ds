# D21 运行与产物证据

## 构建产物

```text
Path: E:\AI\project\AI-Learning-two-lp\M2-Java-ds\D21\D21_TOKEN_ID_LOGS\target\D21_TOKEN_ID_LOGS-1.0-SNAPSHOT.jar
Bytes: 59509811
SHA-256: E707ECC844FA66157ECF99A4EC4C30AA92062CC6D48EDEE0FAE7F7AECCBE6199
```

打包命令：

```powershell
mvn -f .\pom.xml -DskipTests package
```

完整日志：`logs\D21\D21-package-final.log`。

## Trace / MODEL_CALL 运行摘录

`D21-model-call-runtime-excerpt.log` 记录：

- 同步请求 `sync-trace-001`：SUCCESS。
- 关闭正文访问日志后的 `disabled-log-trace-001`：SUCCESS。
- 流式请求 `stream-trace-001`：SUCCESS，含首 Chunk 延迟。
- 客户端断流 `disconnect-trace-001`：CANCELLED。

每个关联 ID 只出现一条终态 `MODEL_CALL`。

## 安全运行边界

- 默认模式 `AI_MODE=mock`。
- 测试 JVM 固定使用无效占位 Key。
- 未读取或回显宿主真实 `DEEPSEEK_API_KEY`。
- 未发起真实模型网络请求。

## 完整日志

- 最终全量：`logs\D21\D21-full-regression-final.log`
- 专项：`logs\D21\D21-observability-tests-03.log`
- Usage 专项：`logs\D21\D21-usage-tests-04.log`
- 打包：`logs\D21\D21-package-02.log`
