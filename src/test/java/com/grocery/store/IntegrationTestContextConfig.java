package com.grocery.store;

import com.grocery.store.config.HibernateConfig;
import com.grocery.store.service.BasicDataSetup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;

@Import(HibernateConfig.class)
@Configuration
@ComponentScan(basePackageClasses = StoreApplication.class)
@EnableAutoConfiguration
public class IntegrationTestContextConfig implements SmartInitializingSingleton, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void afterSingletonsInstantiated() {
		BasicDataSetup basicDataSetup = applicationContext.getBean(BasicDataSetup.class);
		Long orderId = basicDataSetup.init();
		((GenericApplicationContext) applicationContext).getBeanFactory().registerSingleton("orderId", new OrderId(orderId));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@RequiredArgsConstructor
	@Getter
	public static class OrderId {
		private final Long identifier;
	}

}
