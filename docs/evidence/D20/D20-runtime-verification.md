# D20 运行与产物核验

## 构建产物

```text
Path: E:\AI\project\AI-Learning-two-lp\M2-Java-ds\D20\D20_UMIEM\target\D20_UMIEM-1.0-SNAPSHOT.jar
Bytes: 59491026
SHA-256: 7A86504F62945B4BD81F7686744252BF7485390C24290CAF3B38BE2C402AF695
```

打包命令：

```powershell
mvn -f .\pom.xml -DskipTests package
```

退出状态：成功。完整日志 `logs\D20\D20-package-01.log`。

## 安全运行边界

- 默认模式：`AI_MODE=mock`。
- 测试 profile：固定 `AI_MODE=mock` 与无效占位凭据。
- 本轮未启动真实 DeepSeek 模式，未发起真实网络模型请求。
- 未读取、回显或落盘宿主真实 DeepSeek API Key。

## 测试审计

- D20 专项：28 个测试全部通过。
- 全量回归：161 个测试，0 失败，0 错误，1 跳过。
- 跳过项：`DeepSeekLiveSmokeTest`，默认禁用。
- 测试报告：`D20\D20_UMIEM\target\surefire-reports`。
- 完整日志：`E:\AI\project\AI-Learning-two-lp\M2-Java-ds\logs\D20`。

## 平台 Token 元数据

- 真实模型请求数：0。
- 真实输入、缓存、输出 Token：0 / 未产生费用。
- Agent 平台内部 Token 用量：平台不可观测，未估算。
