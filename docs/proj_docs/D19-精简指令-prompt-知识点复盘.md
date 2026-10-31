# 精简指令-prompt

~~~
# Spring AI 知识笔记：阶段性知识点复盘｜生成任务书

## 一、任务目标

请生成一份完整的《Spring AI 知识点复盘》Markdown 笔记，将已学内容整合成一套可理解、可应用、可自测的知识体系。

重点不是重复旧笔记，而是：

1. 梳理知识地图及各组件的关系。
2. 解释核心原理和易混淆概念。
3. 贯通模型调用、Prompt、记忆、输出解析与工具调用。
4. 用 Java 代码、故障案例和综合练习验证理解。
5. 通过自测诊断薄弱点，明确后续补学方向。

## 二、技术基线

- JDK 21。
- Spring Boot 3.5.x，项目基线 3.5.16。
- Spring AI 1.1.x，优先核验 1.1.8。
- Maven 3.9.x，项目基线 3.9.16。
- 模型服务：DeepSeek API。
- 主要接口：`ChatClient`。

生成前核实官方文档、版本兼容性、API 和配置。不得混用 Spring AI 不同大版本的实现；不得声称已运行代码或测试，除非确实执行过。

## 三、必须覆盖的知识范围

### 1. Spring AI 架构与核心组件

讲清 Spring AI 的定位、与 Spring Boot 和模型服务的关系、Starter/BOM/自动配置，以及一次模型调用的完整链路。

复盘以下组件的职责、输入输出和相互关系：

`ChatClient`、`ChatModel`、`Prompt`、`Message`、`ChatOptions`、`ChatResponse`、`Generation`、`Advisor`、`ChatMemory`、`ChatMemoryRepository`、`MessageWindowChatMemory`、`StructuredOutputConverter`、`BeanOutputConverter`、`@Tool`、`ToolCallback`。

必须提供架构图或 Mermaid 流程图，不能只列定义。

### 2. 模型 API 与生成参数

覆盖：

- 同步调用：`prompt()`、`user()`、`call()`、`content()`。
- 完整响应与纯文本的区别。
- 流式调用：`stream()`、`Flux<String>`、`Flux<ChatResponse>`。
- 流式响应、HTTP SSE、前端消费之间的关系。
- 流式结束、异常、取消和部分输出。
- Temperature、`top_p`、输入/输出 Token、最大输出 Token、上下文窗口。
- 超时、限流、响应结束原因、Token 用量和调用成本。

必须重点辨析：Token 不等于字符；最大输出 Token 不等于上下文窗口；流式 API 不等于 SSE；流式收到部分文本不代表请求完整成功。

### 3. Prompt 工程基础与进阶

**基础部分：** 角色设定、任务目标、上下文、分步指令、约束、输出格式、验收条件、Prompt Template 与动态变量。

**进阶部分：**

- Zero-shot、One-shot、Few-shot 的区别与示例设计。
- CoT 与普通分步指令、应用程序多阶段调用的区别。
- CoT 的适用边界、成本和错误风险。
- 幻觉的类型与原因。
- 通过证据约束、检索、工具、程序校验和人工复核降低幻觉风险。

必须说明：示例越多不一定越好；推理文本越长不代表结论越正确；Prompt 无法保证事实绝对正确。

### 4. 对话记忆与上下文管理

覆盖：

- 短期记忆、长期记忆、完整聊天历史、用户画像。
- `ChatMemory`、`ChatMemoryRepository`、`MessageWindowChatMemory` 和目标版本支持的记忆 Advisor。
- `conversationId`、会话隔离、历史消息注入和窗口淘汰。
- 按消息数量、Token、时间、相关性进行记忆管理。
- 长期记忆的提取、筛选、持久化、检索、更新、过期和删除。
- 用户隔离、敏感信息、并发更新和错误记忆污染。

必须辨析：数据已持久化不代表当前请求会使用它；记忆窗口不等于模型上下文窗口；短期与长期记忆不一定是框架内置的两种固定实现。

### 5. 输出解析、JSON 与工具调用

覆盖：

- JSON 类型、语法、字段约束及 Java 对象映射。
- Prompt-only JSON、提供方 JSON 模式、Spring AI 结构化输出和程序校验的区别。
- `StructuredOutputConverter`、`BeanOutputConverter`、`ChatClient.entity()` 的目标版本用法。
- 必需字段、枚举、类型、空响应、截断、解析异常和业务校验。
- Tool Calling 的工具描述、参数、调用请求、Java 方法执行、结果回传与异常处理。
- 工具参数校验、授权、幂等性与副作用控制。

必须辨析：合法 JSON 不代表业务数据正确；模型提出工具调用不代表工具已经执行；模型输出不能取代程序权限校验。

### 6. 工程化与故障诊断

结合上述主题，至少分析：

- 依赖或自动配置失败。
- 模型认证、参数错误、超时、限流。
- 流式输出异常。
- Prompt 效果不稳定或出现幻觉。
- 记忆丢失、串线或过期。
- JSON 解析失败、字段不合法。
- 工具调用失败或越权风险。
- Token 成本异常和敏感信息泄露。

统一采用“现象—可能原因—核查证据—解决方向”的结构，不得只提供“重启应用”“换模型”等泛化建议。

## 四、复盘必须怎样组织

### 1. 知识地图

展示总体架构与知识依赖关系，说明模型调用、Prompt、记忆、输出解析、工具调用及程序校验如何协作。

### 2. 核心原理

按主题说明定义、工作机制、关键 API、使用场景、局限和常见错误。重点解释原理，避免堆砌术语。

### 3. 易混淆概念总表

至少比较：

- `ChatClient` 与 `ChatModel`。
- 同步调用与流式调用。
- Token 与字符。
- 上下文窗口与记忆窗口。
- 聊天历史与对话记忆。
- Few-shot 与模型微调。
- CoT 与程序化分步执行。
- 模型解释与可核验的证据。
- JSON 解析与业务校验。
- JSON 输出与 Tool Calling。

### 4. 综合案例

使用虚构的制造业设备故障工单，将本阶段知识串起来：

输入包括设备编号、故障现象和缺失的测量数据。展示如何组织 Prompt、选择相关记忆、生成结构化结果、调用只读查询工具（如适用）并校验最终输出。

必须区分已知事实与推测，不得编造设备数据或将示例描述成真实诊断结果。

### 5. 主动回忆与自测

按难度提供：

- 基础概念题。
- 原理解释题。
- API 代码阅读题。
- 易混淆概念辨析题。
- 故障诊断题。
- 综合设计题。

问题应覆盖全部主题，不能只考概念记忆。题目和参考答案分开，避免在作答前直接暴露答案。

提供简明评分标准和薄弱点分类，指导学习者根据结果选择补学内容。

## 五、代码、资料与可靠性约束

1. 所有 API、类名、依赖和配置必须匹配指定版本。
2. 代码示例必须解释关键行为、输入输出与适用条件。
3. 明确区分框架内置能力、模型提供方能力和应用自定义逻辑。
4. 不得把未经核实的代码描述为可运行，也不得编造测试或性能数据。
5. 关注 API Key、用户隔离、输入校验、异常处理和日志脱敏。
6. 不必展开完整 RAG、MCP、Agent 或 Workflow 教程，只需说明与当前知识的衔接关系。
7. 外部资料采用“英文原始标题 | 中文翻译标题：[完整 URL](完整 URL)”格式，引用紧贴相关结论。

优先核对以下官方资料：

- Chat Client API | 聊天客户端 API：[https://docs.spring.io/spring-ai/reference/1.1/api/chatclient.html](https://docs.spring.io/spring-ai/reference/1.1/api/chatclient.html)
- Chat Memory | 对话记忆：[https://docs.spring.io/spring-ai/reference/1.1/api/chat-memory.html](https://docs.spring.io/spring-ai/reference/1.1/api/chat-memory.html)
- Structured Output Converter | 结构化输出转换器：[https://docs.spring.io/spring-ai/reference/1.1/api/structured-output-converter.html](https://docs.spring.io/spring-ai/reference/1.1/api/structured-output-converter.html)
- Tool Calling | 工具调用：[https://docs.spring.io/spring-ai/reference/1.1/api/tools.html](https://docs.spring.io/spring-ai/reference/1.1/api/tools.html)
- Spring AI Releases | Spring AI 版本发布记录：[https://github.com/spring-projects/spring-ai/releases](https://github.com/spring-projects/spring-ai/releases)

## 六、最终交付结构

请直接生成完整的 Typora 兼容 Markdown 文档，依次包括：

1. 文档元信息与复盘目标。
2. 知识地图与整体架构。
3. 核心知识模块复盘。
4. 易混淆概念对比表。
5. 综合案例与关键代码。
6. 常见故障与诊断。
7. 分层自测题。
8. 参考答案与评分标准。
9. 薄弱点诊断和补学建议。
10. 官方参考资料。

最终验收标准：**知识覆盖完整、概念边界准确、API 版本一致、案例能串联知识、自测能够发现真实薄弱点、引用可追溯。**

现在直接生成完整的《Spring AI 知识笔记：阶段性知识点复盘》，不要只输出提纲，也不要扩展成新的完整技术教程。
~~~



# Spring AI 知识笔记：阶段性知识点复盘｜生成任务书

## 一、任务目标

请生成一份完整的《Spring AI 知识点复盘》Markdown 笔记，将已学内容整合成一套可理解、可应用、可自测的知识体系。

重点不是重复旧笔记，而是：

1. 梳理知识地图及各组件的关系。
2. 解释核心原理和易混淆概念。
3. 贯通模型调用、Prompt、记忆、输出解析与工具调用。
4. 用 Java 代码、故障案例和综合练习验证理解。
5. 通过自测诊断薄弱点，明确后续补学方向。

## 二、技术基线

- JDK 21。
- Spring Boot 3.5.x，项目基线 3.5.16。
- Spring AI 1.1.x，优先核验 1.1.8。
- Maven 3.9.x，项目基线 3.9.16。
- 模型服务：DeepSeek API。
- 主要接口：`ChatClient`。

生成前核实官方文档、版本兼容性、API 和配置。不得混用 Spring AI 不同大版本的实现；不得声称已运行代码或测试，除非确实执行过。

## 三、必须覆盖的知识范围

### 1. Spring AI 架构与核心组件

讲清 Spring AI 的定位、与 Spring Boot 和模型服务的关系、Starter/BOM/自动配置，以及一次模型调用的完整链路。

复盘以下组件的职责、输入输出和相互关系：

`ChatClient`、`ChatModel`、`Prompt`、`Message`、`ChatOptions`、`ChatResponse`、`Generation`、`Advisor`、`ChatMemory`、`ChatMemoryRepository`、`MessageWindowChatMemory`、`StructuredOutputConverter`、`BeanOutputConverter`、`@Tool`、`ToolCallback`。

必须提供架构图或 Mermaid 流程图，不能只列定义。

### 2. 模型 API 与生成参数

覆盖：

- 同步调用：`prompt()`、`user()`、`call()`、`content()`。
- 完整响应与纯文本的区别。
- 流式调用：`stream()`、`Flux<String>`、`Flux<ChatResponse>`。
- 流式响应、HTTP SSE、前端消费之间的关系。
- 流式结束、异常、取消和部分输出。
- Temperature、`top_p`、输入/输出 Token、最大输出 Token、上下文窗口。
- 超时、限流、响应结束原因、Token 用量和调用成本。

必须重点辨析：Token 不等于字符；最大输出 Token 不等于上下文窗口；流式 API 不等于 SSE；流式收到部分文本不代表请求完整成功。

### 3. Prompt 工程基础与进阶

**基础部分：** 角色设定、任务目标、上下文、分步指令、约束、输出格式、验收条件、Prompt Template 与动态变量。

**进阶部分：**

- Zero-shot、One-shot、Few-shot 的区别与示例设计。
- CoT 与普通分步指令、应用程序多阶段调用的区别。
- CoT 的适用边界、成本和错误风险。
- 幻觉的类型与原因。
- 通过证据约束、检索、工具、程序校验和人工复核降低幻觉风险。

必须说明：示例越多不一定越好；推理文本越长不代表结论越正确；Prompt 无法保证事实绝对正确。

### 4. 对话记忆与上下文管理

覆盖：

- 短期记忆、长期记忆、完整聊天历史、用户画像。
- `ChatMemory`、`ChatMemoryRepository`、`MessageWindowChatMemory` 和目标版本支持的记忆 Advisor。
- `conversationId`、会话隔离、历史消息注入和窗口淘汰。
- 按消息数量、Token、时间、相关性进行记忆管理。
- 长期记忆的提取、筛选、持久化、检索、更新、过期和删除。
- 用户隔离、敏感信息、并发更新和错误记忆污染。

必须辨析：数据已持久化不代表当前请求会使用它；记忆窗口不等于模型上下文窗口；短期与长期记忆不一定是框架内置的两种固定实现。

### 5. 输出解析、JSON 与工具调用

覆盖：

- JSON 类型、语法、字段约束及 Java 对象映射。
- Prompt-only JSON、提供方 JSON 模式、Spring AI 结构化输出和程序校验的区别。
- `StructuredOutputConverter`、`BeanOutputConverter`、`ChatClient.entity()` 的目标版本用法。
- 必需字段、枚举、类型、空响应、截断、解析异常和业务校验。
- Tool Calling 的工具描述、参数、调用请求、Java 方法执行、结果回传与异常处理。
- 工具参数校验、授权、幂等性与副作用控制。

必须辨析：合法 JSON 不代表业务数据正确；模型提出工具调用不代表工具已经执行；模型输出不能取代程序权限校验。

### 6. 工程化与故障诊断

结合上述主题，至少分析：

- 依赖或自动配置失败。
- 模型认证、参数错误、超时、限流。
- 流式输出异常。
- Prompt 效果不稳定或出现幻觉。
- 记忆丢失、串线或过期。
- JSON 解析失败、字段不合法。
- 工具调用失败或越权风险。
- Token 成本异常和敏感信息泄露。

统一采用“现象—可能原因—核查证据—解决方向”的结构，不得只提供“重启应用”“换模型”等泛化建议。

## 四、复盘必须怎样组织

### 1. 知识地图

展示总体架构与知识依赖关系，说明模型调用、Prompt、记忆、输出解析、工具调用及程序校验如何协作。

### 2. 核心原理

按主题说明定义、工作机制、关键 API、使用场景、局限和常见错误。重点解释原理，避免堆砌术语。

### 3. 易混淆概念总表

至少比较：

- `ChatClient` 与 `ChatModel`。
- 同步调用与流式调用。
- Token 与字符。
- 上下文窗口与记忆窗口。
- 聊天历史与对话记忆。
- Few-shot 与模型微调。
- CoT 与程序化分步执行。
- 模型解释与可核验的证据。
- JSON 解析与业务校验。
- JSON 输出与 Tool Calling。

### 4. 综合案例

使用虚构的制造业设备故障工单，将本阶段知识串起来：

输入包括设备编号、故障现象和缺失的测量数据。展示如何组织 Prompt、选择相关记忆、生成结构化结果、调用只读查询工具（如适用）并校验最终输出。

必须区分已知事实与推测，不得编造设备数据或将示例描述成真实诊断结果。

### 5. 主动回忆与自测

按难度提供：

- 基础概念题。
- 原理解释题。
- API 代码阅读题。
- 易混淆概念辨析题。
- 故障诊断题。
- 综合设计题。

问题应覆盖全部主题，不能只考概念记忆。题目和参考答案分开，避免在作答前直接暴露答案。

提供简明评分标准和薄弱点分类，指导学习者根据结果选择补学内容。

## 五、代码、资料与可靠性约束

1. 所有 API、类名、依赖和配置必须匹配指定版本。
2. 代码示例必须解释关键行为、输入输出与适用条件。
3. 明确区分框架内置能力、模型提供方能力和应用自定义逻辑。
4. 不得把未经核实的代码描述为可运行，也不得编造测试或性能数据。
5. 关注 API Key、用户隔离、输入校验、异常处理和日志脱敏。
6. 不必展开完整 RAG、MCP、Agent 或 Workflow 教程，只需说明与当前知识的衔接关系。
7. 外部资料采用“英文原始标题 | 中文翻译标题：[完整 URL](完整 URL)”格式，引用紧贴相关结论。

优先核对以下官方资料：

- Chat Client API | 聊天客户端 API：[https://docs.spring.io/spring-ai/reference/1.1/api/chatclient.html](https://docs.spring.io/spring-ai/reference/1.1/api/chatclient.html)
- Chat Memory | 对话记忆：[https://docs.spring.io/spring-ai/reference/1.1/api/chat-memory.html](https://docs.spring.io/spring-ai/reference/1.1/api/chat-memory.html)
- Structured Output Converter | 结构化输出转换器：[https://docs.spring.io/spring-ai/reference/1.1/api/structured-output-converter.html](https://docs.spring.io/spring-ai/reference/1.1/api/structured-output-converter.html)
- Tool Calling | 工具调用：[https://docs.spring.io/spring-ai/reference/1.1/api/tools.html](https://docs.spring.io/spring-ai/reference/1.1/api/tools.html)
- Spring AI Releases | Spring AI 版本发布记录：[https://github.com/spring-projects/spring-ai/releases](https://github.com/spring-projects/spring-ai/releases)

## 六、最终交付结构

请直接生成完整的 Typora 兼容 Markdown 文档，依次包括：

1. 文档元信息与复盘目标。
2. 知识地图与整体架构。
3. 核心知识模块复盘。
4. 易混淆概念对比表。
5. 综合案例与关键代码。
6. 常见故障与诊断。
7. 分层自测题。
8. 参考答案与评分标准。
9. 薄弱点诊断和补学建议。
10. 官方参考资料。

最终验收标准：**知识覆盖完整、概念边界准确、API 版本一致、案例能串联知识、自测能够发现真实薄弱点、引用可追溯。**

现在直接生成完整的《Spring AI 知识笔记：阶段性知识点复盘》，不要只输出提纲，也不要扩展成新的完整技术教程。