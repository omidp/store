package com.grocery.store;

import com.grocery.store.config.HibernateConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(HibernateConfig.class)
@Configuration
@ComponentScan(basePackageClasses = StoreApplication.class)
@EnableAutoConfiguration
public class IntegrationTestContextConfig {
}
