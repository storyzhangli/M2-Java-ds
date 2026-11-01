# D19 Dependency Decision

## Actual dependency chain

```text
spring-boot-starter-web 3.5.16
  -> spring-boot-starter-tomcat 3.5.16
  -> spring-webmvc 6.2.19

spring-ai-starter-model-deepseek 1.1.8
  -> spring-ai-deepseek 1.1.8
  -> spring-webflux 6.2.19
  -> reactor-core 3.7.19
  -> spring-ai-client-chat 1.1.8
```

`spring-boot-starter-webflux` is not declared.

## Decision

Use Spring MVC/Tomcat as the application stack, use Reactor Flux as the model stream, and bridge it through MVC `SseEmitter`.

No dependency change is required.

## Runtime proof

- `MvcStackDecisionTest` verifies `ServletWebServerApplicationContext`.
- Runtime server class contains `Tomcat`.
- No Reactive `DispatcherHandler` bean exists.
- `spring-webflux` and `reactor-core` are available transitively for Spring AI streaming.

Full Maven dependency output: `d19-dependency-tree.log`.
