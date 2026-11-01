# D19 MVC 与 WebFlux 流式聊天技术说明及实践指南

> 适用版本：Spring Boot 3.5.16、Spring AI 1.1.8、Spring Framework 6.2.19、JDK 21  
> 实际模块：`D19_SCI`

## 1. 先判断应用实际类型

不能看到代码里有 `Flux` 就认定应用已经迁移到 WebFlux。

D19 实际依赖：

```text
spring-boot-starter-web
  spring-boot-starter-tomcat
  spring-webmvc

spring-ai-starter-model-deepseek
  spring-ai-client-chat
  spring-webflux
  reactor-core
```

注意：

- `spring-webflux` 是 Spring AI 传递依赖；
- POM 没有 `spring-boot-starter-webflux`；
- 实际 ApplicationContext 是 `ServletWebServerApplicationContext`；
- 实际 WebServer 是 Tomcat；
- 没有 Reactive `DispatcherHandler` Bean。

因此 D19 是：

```text
Servlet/Tomcat + Spring MVC + Reactor Flux + SseEmitter
```

不是：

```text
Reactive Netty + WebFlux
```

## 2. Spring AI 1.1.8 同步与流式 API

### 2.1 同步

```java
String answer = chatClient.prompt()
        .user("问题")
        .call()
        .content();
```

API 链：

```text
prompt() -> ChatClientRequestSpec
call() -> CallResponseSpec
content() -> String
```

`call()` 是阻塞式单值响应。D19 的同步接口继续使用现有 `ChatModel.call()`，因此原接口行为不变。

### 2.2 流式纯文本

```java
Flux<String> chunks = chatClient.prompt()
        .user("问题")
        .stream()
        .content();
```

`stream().content()` 不会立即请求模型；首次订阅时才执行。

### 2.3 流式元数据

```java
Flux<ChatResponse> responses = chatClient.prompt()
        .messages(messages)
        .options(options)
        .stream()
        .chatResponse();
```

如需 ChatClient 包装后的更完整元数据：

```java
Flux<ChatClientResponse> responses = chatClient.prompt()
        .stream()
        .chatClientResponse();
```

实际 1.1.8 jar 证据：

`docs/evidence/D19/spring-ai-1.1.8-chatclient-api.txt`

## 3. 为什么不迁移 WebFlux

当前同步链路包含：

- Servlet Filter；
- MVC AOP 访问日志；
- `HttpServletRequest` / `HttpServletResponse`；
- JDBC/MyBatis-Plus 阻塞数据库访问；
- MVC 统一异常处理；
- 既有同步 Controller 和测试。

迁移 WebFlux 会引入：

- Filter/WebFilter 模型变化；
- 阻塞 DAO 需要线程调度隔离；
- 异步异常处理变化；
- 同步接口回归风险；
- 新的服务器与部署验证成本。

这些投入不改变同步契约，也不提升本需求的业务价值。

采用 MVC 后，Reactor 仍可用于模型层：

```text
ChatClient/Flux
   -> KnowledgeChatOrchestrator
   -> SseEmitter
   -> Servlet async output
```

## 4. MVC 异步响应原理

`SseEmitter` 是 MVC 的异步 `ResponseBodyEmitter` 子类：

1. Controller 返回 `SseEmitter`；
2. Spring MVC 开启 Servlet async request；
3. Controller 订阅 Reactor 流；
4. 每个可达 chunk 调用 `emitter.send(...)`；
5. 正常或异常时关闭 emitter 对应的异步请求；
6. 最终由容器处理 Servlet 输出。

它提供的效果：

- HTTP 连接提前建立；
- 多个数据帧分段到达；
- 可在客户端断开后取消上游；
- 不需要 WebFlux Server。

它不提供：

- 端到端 Reactive 背压；
- 非阻塞 Servlet 输出；
- 自动纠正代理缓冲；
- 自动持久化半截消息。

## 5. D19 SSE 协议

请求：

```http
POST /api/v1/ai/chat/stream
Content-Type: application/json
```

每次事件由 `event:` 和 JSON `data:` 组成。

### 5.1 start

```text
event:start
data:{"requestId":"...","mode":"mock","model":"offline-mock","sessionId":"..."}
```

用途：

- 让客户端在首段文本前取得服务端生成的 sessionId；
- 回传模式、模型和模板版本；
- 首块边界指示流已经建立。

### 5.2 delta

```text
event:delta
data:{"seq":0,"content":"[MOCK:D01] "}
```

`seq` 从 0 连续增长。客户端应按到达顺序拼接。

不应当：

- 假设每个 delta 是一个 token；
- 假设每个 delta 是一个汉字；
- 假设 chunk 边界稳定。

### 5.3 complete

```text
event:complete
data:{"seq":41,"mode":"mock","model":"offline-mock","mocked":true,"totalChars":252}
```

`complete` 是唯一正常结束信号。

### 5.4 error

```text
event:error
data:{"code":5000,"message":"系统繁忙，请稍后重试"}
```

异常流不能同时发送 `complete`。

## 6. 前端消费

浏览器原生 `EventSource` 只适合 GET。D19 使用 POST JSON，因此前端采用：

```javascript
fetch("/api/v1/ai/chat/stream", {
  method: "POST",
  headers: {"Content-Type": "application/json"},
  body: JSON.stringify(request),
  signal: abortController.signal
});
```

然后使用：

```text
response.body.getReader()
TextDecoder("utf-8")
自定义 SSE 分帧解析器
```

分帧器支持：

- 网络任意切块；
- UTF-8 中文跨块；
- LF、CR、CRLF；
- 事件间空行；
- 结尾未带空行的 flush。

用户点击“停止”时：

```javascript
abortController.abort();
```

## 7. Reactor 生命周期

D19 的处理顺序：

```text
校验请求
-> 建立/验证会话
-> 写 PENDING 用户消息
-> 组装 Prompt 与历史
-> 创建 Flux
-> SseEmitter 返回
-> Web 层订阅 Flux
-> delta 发送
-> complete 或 error
-> 持久化终态
```

### 7.1 没有主动 subscribe()

Controller 的 `.subscribe(...)` 是 MVC 桥接必要边界：返回值不是 Flux，而是需要手工转发的 `SseEmitter`。订阅生命周期与 emitter 绑定，并由超时/完成/错误/断开回调取消。

这也是 MVC 方案与 WebFlux 返回 Flux 由框架订阅的差异。

### 7.2 不在流中 block

流式生产链路没有：

```text
block()
collectList()
```

客户端可观察到多个独立 delta。

### 7.3 ChatClient 懒执行

测试 `SpringAiChatClientBehaviorTest` 在订阅前断言模型 stream 订阅计数为 0，订阅后为 1。

## 8. 超时和取消

### 8.1 应用层超时

```java
outcome.chunks()
    .timeout(Duration.ofMillis(streamTimeoutMs))
```

默认 60 秒，可配置：

```powershell
$env:AI_STREAM_TIMEOUT_MS = "60000"
```

超时后：

- 发送 SSE `error`；
- 不发送 `complete`；
- 用户消息标记 `FAILED`；
- 上游订阅被 dispose。

### 8.2 容器兜底

SseEmitter 的容器超时比应用超时多 5 秒。应用 timeout 未生效时，容器回调负责兜底。

### 8.3 客户端主动断开

处理不依赖容器一定触发 `onError`。SSE 写入失败时：

```text
cancel upstream
if partial content exists -> INTERRUPTED
else -> FAILED
complete emitter safely
```

集成测试使用 JDK `HttpClient`：

1. 读取 start；
2. 读取首个 delta；
3. 在 complete 前关闭输入流；
4. 验证数据库不是 PENDING/COMPLETED。

证据：

`D19-targeted-stream-tests-04-final.log`、`D19-prestart-and-lifecycle-tests.log` 与
`D19-full-regression-06-final.log` 中的断流、超时和启动前失败状态。

## 9. 会话历史

同步和流式共用：

- `RoleConversationService.prepare`
- `ChatHistoryService.startTurn`
- `getContextWindow`
- `completeTurn`
- `failTurn`
- `interruptTurn`

不复制 Prompt、权限或历史逻辑。

### 9.1 持久化时点

| 生命周期 | 数据库状态 |
|---|---|
| 请求开始 | user PENDING |
| 正常完成 | user/assistant COMPLETED |
| 上游错误 | user FAILED |
| 订阅前配置/鉴权错误 | user FAILED |
| 超时 | user FAILED |
| 客户端断开，无正文 | user FAILED |
| 客户端断开，有正文 | user/assistant INTERRUPTED |

流式过程不逐 chunk 写 MySQL，避免将网络 chunk 当成数据库事件流。

## 10. 错误传播

### 10.1 流开始前

参数错误、非法角色、会话无权访问发生在 `SseEmitter` 返回前，走全局 JSON。

### 10.2 流开始后

HTTP 响应头已提交为 SSE，不能再变成 HTTP 4xx/5xx JSON。错误只能通过协议内 `error` 事件表达。

### 10.3 客户端已断开

响应响应流不可写，不再尝试序列化 JSON。`GlobalExceptionHandler` 识别响应已提交的 IOException 并安全收尾。

## 11. 背压与资源边界

- Reactor 对模型流提供 request/demand 机制；
- `SseEmitter` 最终写入 Servlet 输出流，因此不是完整 Reactive 背压链；
- 单次请求有消息长度上限；
- 单次流有超时；
- 客户端 abort 会 dispose 上游；
- 会话窗口限制进入 Prompt 的消息数量；
- 当前未做全局并发数配额，生产环境应按模型 QPS 和 Tomcat 线程配置限流。

## 12. 代理和伪流式

“接口返回了 Flux”不代表浏览器一定实时收到。

常见缓冲点：

- Nginx `proxy_buffering on`；
- CDN；
- API Gateway；
- Servlet Filter 包装响应；
- 压缩中间件；
- 前端把所有 chunk 重新聚合成整句后再渲染。

D19 已做：

```http
Cache-Control: no-cache
X-Accel-Buffering: no
```

并将 `/api/v1/ai/chat/stream` 排除在通用请求/响应缓存日志过滤器之外。

部署时仍需：

```nginx
proxy_buffering off;
proxy_cache off;
proxy_read_timeout 120s;
```

具体值应按代理产品与业务超时配置。

## 13. 关键代码位置

| 职责 | 文件 |
|---|---|
| 同步 Controller | `controller/ChatController.java` |
| SSE Controller | `controller/ChatStreamController.java` |
| SSE payload | `dto/SsePayloads.java` |
| DeepSeek 同步/流式适配 | `service/DeepSeekAiChatService.java` |
| 模型抽象 | `service/AiChatService.java` |
| 类型化 chunk | `service/StreamChunk.java` |
| 知识/引用编排 | `knowledge/KnowledgeChatOrchestrator.java` |
| 会话状态 | `history/ChatHistoryService.java` |
| 日志过滤 | `logging/CachedBodyAccessLogFilter.java` |
| 前端 SSE 读取 | `static/js/app.js` |
| SSE 分帧 | `static/js/sse-parser.js` |

## 14. 可复现实验

### 14.1 编译

```powershell
cd E:\AI\project\AI-Learning-two-lp\M2-Java-ds\D19
mvn -f .\pom.xml -DskipTests compile
```

### 14.2 定向测试

```powershell
mvn -f .\pom.xml -pl D19_SCI `
  -Dtest='ChatStreamIntegrationTest,ChatStreamTimeoutTest,ChatStreamErrorEventTest,StreamClientDisconnectIntegrationTest,MvcStackDecisionTest,SpringAiChatClientBehaviorTest,StreamContractCompatibilityTest' test
```

### 14.3 全量回归

```powershell
mvn -f .\pom.xml test
```

已验证：

```text
141 tests, 0 failures, 0 errors, 1 skipped
```

### 14.4 打包

```powershell
mvn -f .\pom.xml -DskipTests package
```

## 15. IDEA 运行

Maven 导入根 `D19/pom.xml`。

启动类：

```text
com.zl.d19.D19SpringAiApplication
```

Mock 模式不需要 API Key：

- Active profiles：无特殊要求。
- Environment：`AI_MODE=mock`。

真实模式：

- 用户自行通过进程环境注入 `DEEPSEEK_API_KEY`；
- 设置 `AI_MODE=deepseek`；
- 不在 Run Configuration XML 中写真实 Key；
- 仅在明确授权时调用。

## 16. 诊断清单

### 响应不是逐段到达

1. 确认 `Content-Type` 是 SSE。
2. 直接请求应用端口。
3. 检查 Nginx/Gateway 缓冲。
4. 检查 Filter 是否包装并缓冲输出。
5. 检查前端是否等待 reader 结束后才渲染。

### 流一直挂起

1. 查看 `app.ai.stream-timeout-ms`。
2. 确认上游是否会发首块。
3. 检查代理 read timeout。
4. 检查模型网络和鉴权。

### 客户端停止后数据库仍 PENDING

1. 确认请求已进入流式 Controller。
2. 查看 `ClientDisconnect`/`SseTimeout` 日志。
3. 验证上游 `doOnCancel` 是否触发。
4. 检查 emitter 写失败是否被异常处理器再次接管。

### 测试误触真实模型

检查 Maven 测试属性：

```text
app.ai.mode=mock
spring.ai.deepseek.api-key=d01-mock-key-not-for-authentication
AI_MODE=mock
DEEPSEEK_API_KEY=d01-mock-key-not-for-authentication
```

在线测试还要求：

```text
DEEPSEEK_LIVE_TEST_AUTHORIZED=true
```

## 17. 官方资料

- Chat Client API | 聊天客户端 API：[https://docs.spring.io/spring-ai/reference/1.1/api/chatclient.html](https://docs.spring.io/spring-ai/reference/1.1/api/chatclient.html)
- Reactive Web Applications | 响应式 Web 应用：[https://docs.spring.io/spring-boot/3.5/reference/web/reactive.html](https://docs.spring.io/spring-boot/3.5/reference/web/reactive.html)
- Asynchronous Requests | 异步请求：[https://docs.spring.io/spring-framework/reference/6.2/web/webmvc/mvc-ann-async.html](https://docs.spring.io/spring-framework/reference/6.2/web/webmvc/mvc-ann-async.html)
- Spring WebFlux | Spring WebFlux 框架说明：[https://docs.spring.io/spring-framework/reference/6.2/web/webflux.html](https://docs.spring.io/spring-framework/reference/6.2/web/webflux.html)
- DeepSeek Chat | DeepSeek 对话模型：[https://docs.spring.io/spring-ai/reference/api/chat/deepseek-chat.html](https://docs.spring.io/spring-ai/reference/api/chat/deepseek-chat.html)
- DeepSeek API Docs | DeepSeek API 官方文档：[https://api-docs.deepseek.com/](https://api-docs.deepseek.com/)

版本提示：以上结论以本地实际解析的 Spring AI 1.1.8、Spring Boot 3.5.16、Spring Framework 6.2.19 以及对应测试为准。
