package com.zl.d20;

/**
 * D20 测试类与正式启动类位于同一个根包时，测试应直接复用 {@code D20SpringAiApplication}。
 *
 * <p>本类不再声明 {@code @SpringBootApplication}/{@code @SpringBootConfiguration}，
 * 否则 Spring Test 会在同一包内发现两个 Boot 配置类并拒绝启动上下文。</p>
 */
final class D20TestBootstrap {

    private D20TestBootstrap() {
    }
}
