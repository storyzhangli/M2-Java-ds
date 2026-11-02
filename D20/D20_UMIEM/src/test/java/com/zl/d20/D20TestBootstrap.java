package com.zl.d20;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * D20 测试引导配置。
 *
 * <p>测试包为 {@code com.zl.d20}，Spring Boot 测试默认从测试类所在包向上查找</p>
 *
 * <p>本类仅存在于 test classpath，作为测试上下文的唯一引导入口，显式扫描主代码包
 * {@code com.zl.d20} 并启用配置属性扫描，行为与正式启动类保持一致；不引入任何测试专用 Bean。</p>
 */
@SpringBootApplication(scanBasePackages = "com.zl.d20")
@ConfigurationPropertiesScan(basePackages = "com.zl.d20")
class D20TestBootstrap {
}
