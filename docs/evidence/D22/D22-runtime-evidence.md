# D22 运行与产物证据

## 构建产物

```text
Path: E:\AI\project\AI-Learning-two-lp\M2-Java-ds\D22\D22_EMN_URLs_Keys_schemas\target\D22_EMN_URLs_Keys_schemas-1.0-SNAPSHOT.jar
Bytes: 59514779
SHA-256: 5A86EFF493DDA794CE7BA3537F2186242EB3D4CBA3AE406AE3D8806721F3D3A9
```

打包命令：

```powershell
mvn -f .\pom.xml -DskipTests package
```

结果：成功。日志：`logs\D22\D22-package-01.log`。

## 运行边界

- 默认 Mock 无真实 Key。
- 测试真实配置只构造 Bean，不发起网络请求。
- 客户端断流、异常、超时和 usage 均由既有测试覆盖。
- 未调用真实 DeepSeek API。

## 测试统计

- 最终全量：182 个测试，0 失败，0 错误，1 跳过。
- 跳过项：`DeepSeekLiveSmokeTest`。
