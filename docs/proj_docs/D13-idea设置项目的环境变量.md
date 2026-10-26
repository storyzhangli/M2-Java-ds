# IDEA 2024 配置 DeepSeek 环境变量 & 接入真实模型完整指南
> 对应配置：Spring AI 1.1.x + `spring-ai-starter-model-deepseek`
> 配置依据：DeepSeek Chat | DeepSeek 聊天模型：[https://docs.spring.io/spring-ai/reference/1.1/api/chat/deepseek-chat.html](https://docs.spring.io/spring-ai/reference/1.1/api/chat/deepseek-chat.html)

---

## 一、IDEA 2024 配置环境变量（两种方式，推荐第一种）
### 方式一：运行/调试配置（单启动项生效，推荐）
只对当前 Spring Boot 启动项生效，不影响其他项目，最常用。

1. **打开运行配置**
   点击 IDEA 右上角启动按钮旁边的下拉框 → 选择「Edit Configurations...」。

2. **选中 Spring Boot 启动项**
   左侧找到你的项目启动类（`D01SpringAiInitApplication` 或对应启动类）。

3. **添加环境变量**
   找到「Environment variables:」输入框，点击右侧的文件夹图标；
   点击「+」号，依次添加以下键值对：

   | 变量名              | 示例值                                                 | 说明                                                |
   | :------------------ | :----------------------------------------------------- | :-------------------------------------------------- |
   | `DEEPSEEK_API_KEY`  | `sk-xxxxxxxxxxxxxxxxxxxxxxxx`                          | 你的 DeepSeek 真实 API 密钥                         |
   | `DEEPSEEK_BASE_URL` | `[https://api.deepseek.com](https://api.deepseek.com)` | 官方地址，有代理/网关可改                           |
   | `DEEPSEEK_MODEL`    | `deepseek-chat`                                        | 模型名，可选 `deepseek-flash` / `deepseek-reasoner` |
   | `AI_MODE`           | `deepseek`                                             | 切换为真实调用模式，默认是 `mock`                   |

   > 多个环境变量之间用分号 `;` 分隔，也可以在弹窗里逐条添加。

4. **保存配置**
   点击「Apply」→「OK」，配置完成。

---

### 方式二：全局项目环境变量（所有启动项生效）
适合项目内多个启动项都要用同一套变量。

1. 打开「File」→「Project Structure...」（快捷键 `Ctrl + Alt + Shift + S`）
2. 左侧选择「Project」→「Modules」
3. 选中你的主模块 → 右侧「Dependencies」选项卡
4. 点击「+」→「Property」，添加环境变量键值对
5. 保存后重启 IDEA 生效

---

## 二、对应 YML 配置的变量说明
你的 `application.yml` 中使用了 `${变量名:默认值}` 的占位语法，规则如下：
- 启动时优先读取**系统环境变量**，环境变量存在就覆盖默认值；
- 环境变量不存在，就使用冒号后面的默认值。

对应关系：
```yaml
spring:
  ai:
    deepseek:
      api-key: ${DEEPSEEK_API_KEY:d01-mock-key-not-for-authentication}  # 环境变量优先
      base-url: ${DEEPSEEK_BASE_URL:[https://api.deepseek.com](https://api.deepseek.com)}
      chat:
        options:
          model: ${DEEPSEEK_MODEL:deepseek-flash}

app:
  ai:
    mode: ${AI_MODE:mock}  # 默认 mock 离线模式，设为 deepseek 才真实调用
```

> 核心提醒：默认 `AI_MODE=mock` 是离线占位模式，**必须设置 `AI_MODE=deepseek` 才会发起真实 API 调用**，否则不会走网络。

---

## 三、验证环境变量是否加载成功
### 方法1：启动日志验证
在启动类或配置类中添加启动打印，确认变量注入：
```java
@SpringBootApplication
public class D01SpringAiInitApplication {
    public static void main(String[] args) {
        // 启动时打印，验证环境变量是否覆盖
        System.out.println("API_KEY 前8位：" + System.getenv("DEEPSEEK_API_KEY").substring(0, 8));
        System.out.println("AI_MODE：" + System.getenv("AI_MODE"));
        SpringApplication.run(D01SpringAiInitApplication.class, args);
    }
}
```
启动后控制台输出对应值，说明环境变量已正确读取。

### 方法2：调用测试接口验证
写一个最简测试接口，调用 `ChatModel`：
```java
@RestController
@RequestMapping("/test")
public class TestController {

    private final ChatModel chatModel;

    public TestController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/chat")
    public String chat(String message) {
        return chatModel.call(message);
    }
}
```
访问 `[http://localhost:8080/test/chat?message=](http://localhost:8080/test/chat?message=)你好`，返回正常回复即接入成功。

---

## 四、快速切换 Mock / 真实模式
| 场景     | 环境变量设置       | 效果                              |
| :------- | :----------------- | :-------------------------------- |
| 开发调试 | `AI_MODE=mock`     | 不调用外网，返回固定值，零成本    |
| 真实测试 | `AI_MODE=deepseek` | 真实调用 DeepSeek API，消耗 Token |

> 无需修改代码，只改 IDEA 运行配置里的环境变量，重启项目即可切换。

---

## 五、常见问题排查
1. **提示 401 鉴权失败**
   - 检查 `DEEPSEEK_API_KEY` 是否正确、有没有多余空格；
   - 确认 `AI_MODE=deepseek`，mock 模式不会使用真实密钥。

2. **调用超时 / 连接失败**
   - 检查网络能否访问 `api.deepseek.com`；
   - 有代理的话，在 IDEA 启动配置里添加 JVM 参数：`-Dhttp.proxyHost=127.0.0.1 -Dhttp.proxy.port=7890`。

3. **环境变量不生效**
   - 确认是在「Spring Boot 启动项」里加的，不是加到系统全局环境里；
   - 执行 `mvn clean` 后重新启动，避免配置缓存。

---

## 参考文献
[1] Spring. DeepSeek Chat | DeepSeek 聊天模型：[https://docs.spring.io/spring-ai/reference/1.1/api/chat/deepseek-chat.html](https://docs.spring.io/spring-ai/reference/1.1/api/chat/deepseek-chat.html)