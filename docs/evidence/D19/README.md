# D19 Evidence Index

> 最终生成：2026-11-01

## 结论

- D19 最终验收：通过。
- 全量测试：141 tests，0 failures，0 errors，1 skipped。
- 跳过项：真实 DeepSeek 在线测试，需要独立显式授权。
- 可执行 JAR：`D19_SCI/target/D19_SCI-1.0-SNAPSHOT.jar`。
- 当前工作区、暂存区和 Git HEAD 的真实 Key 模式扫描均为 0。
- 本轮未 commit、未 push、未发起真实 DeepSeek 调用。

## 证据文件

| 文件 | 用途 |
|---|---|
| `D19-现状与设计决策.md` | 初始事实、MVC/WebFlux 决策、ChatClient 1.1.8 结论 |
| `D19-迭代状态.md` | 修改文件、功能切片、测试与遗留问题 |
| `D19-验收追踪.md` | D19-T01～T09 验收追踪 |
| `d19-dependency-decision.md` | 依赖链和容器选择结论 |
| `d19-test-summary.txt` | 最终测试聚合结果 |
| `d19-sensitive-pattern-scan.txt` | 工作区、暂存区和 HEAD 扫描 |
| `D19-full-regression-03-final.log` | 中间完整回归 |
| `D19-full-regression-06-final.log` | 最终完整回归 |
| `D19-targeted-stream-tests-04-final.log` | 流式、断流、超时和 Mvc 栈定向测试 |
| `D19-prestart-and-lifecycle-tests.log` | 订阅前失败、超时、异常、断流专项测试 |
| `D19-rate-limit-mapping-test.log` | 可重试/限流异常映射 SSE error 5002 的测试 |
| `D19-baseline-test.log` | 初始基线：无测试用例 |
| `D19-package-final-03.log` | 最终可执行 JAR 打包 |
| `d19-java-index.json` | 94 个主源码文件、包名、类型、imports、SHA-256 |
| `d19-module-summary.json` | 根/模块 POM、模块关系和直接依赖 |
| `d19-config-property-index.txt` | 相关配置项，敏感值已遮蔽 |
| `d19-dependency-tree.log` | 完整 Maven 依赖树 |
| `spring-ai-1.1.8-chatclient-api.txt` | 实际 1.1.8 jar 的 ChatClient `javap` 输出 |
| `d19-final-git-status.txt` | 最终 Git 状态 |
| `d19-final-diff-stat.txt` | 最终差异统计 |
| `d19-git-scan.txt` | 扫描时的 Git 状态和最近提交 |
| `d19-token-usage.md` | 真实调用 0 次；无可用的供应商 Token 用量 |

## 未执行项

- 真实 DeepSeek API 调用。
- 真实上游 429/限流报文。
- 多实例和反向代理长时间连接压测。

## 保留规则

临时脚本、扫描索引、日志和证据均保留，未自动删除。
