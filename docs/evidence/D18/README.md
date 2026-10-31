# D18 Evidence Index

> 生成日期：2026-10-31

## 结论

- 最终全量测试：133 tests，0 failures，0 errors，1 skipped，BUILD SUCCESS。
- 跳过的唯一用例是 `DeepSeekLiveSmokeTest`，本轮未授权、未执行真实 DeepSeek 调用。
- 当前工作区、暂存区和最终日志/报告的 Key 模式扫描均为 0。
- Git HEAD 的旧文档仍命中敏感模式，必须轮换并协调清理历史。

## 文件

| 文件 | 用途 |
|---|---|
| `d18-test-summary.txt` | 最终测试聚合结果 |
| `D18-full-regression-03-final.log` | 最终全量 Maven 原始输出 |
| `D18-package-final.log` | 最终可执行 JAR 打包输出 |
| `D18-targeted-tests-02.log` | 设备与模板定向测试原始输出 |
| `D18-baseline-test.log` | 修复前基线失败输出 |
| `D18-baseline-context-after-log-path-fix.log` | 日志路径修复后上下文验证 |
| `d18-database-evidence.txt` | 设备模板、变量数、更新时间和版本快照 |
| `d18-sensitive-pattern-scan.txt` | 工作区、暂存区和 HEAD 的模式扫描 |
| `baseline-git-status.txt` | 本轮开始前 Git 状态 |
| `baseline-git-log.txt` | 本轮开始前最近提交 |
| `d18-final-git-status.txt` | 最终 Git 工作区状态 |
| `d18-final-unstaged-diff-stat.txt` | 未暂存跟踪文件 diff 统计 |
| `d18-final-staged-diff-stat.txt` | 已有暂存文件 diff 统计 |
| `d18-d17-d18-file-diff-summary.txt` | D17/D18 文件哈希差异摘要 |
| `d18-git-remote-contains-head.txt` | HEAD 所在远程分支 |
| `d18-sensitive-file-git-log.txt` | 旧敏感文档的 Git 提交记录 |

## 未完成

- 真实 DeepSeek 在线联调：需要新的明确授权。
- Git 历史敏感值清理：需要用户先轮换 Key，并授权协调远程历史重写。

## 保留规则

所有临时脚本、日志和证据均保留，未自动删除。
