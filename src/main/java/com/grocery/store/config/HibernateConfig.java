package com.grocery.store.config;

import com.grocery.store.domain.PO;
import com.grocery.store.util.ManagedSessionFactory;
import jakarta.persistence.EntityManagerFactory;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.context.internal.ManagedSessionContext;
import org.hibernate.context.internal.ThreadLocalSessionContext;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class HibernateConfig {

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSourceProperties dataSourceProperties, JpaProperties jpaProperties) {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource(dataSourceProperties));
		entityManagerFactoryBean.setPackagesToScan(PO.class.getPackageName());
		entityManagerFactoryBean.setJpaVendorAdapter(getHibernateJpaVendorAdapter(jpaProperties));
		Properties properties = new Properties();
		properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, ManagedSessionContext.class.getName());
		entityManagerFactoryBean.setJpaProperties(properties);
		return entityManagerFactoryBean;
	}

	@Bean
	public DataSource dataSource(DataSourceProperties dataSourceProperties) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
		dataSource.setUrl(dataSourceProperties.getUrl());
		dataSource.setUsername(dataSourceProperties.getUsername());
		dataSource.setPassword(dataSourceProperties.getPassword());
		return dataSource;
	}

	@Bean
	@DependsOn("entityManagerFactory")
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	@Bean
	public ManagedSessionFactory managedSessionFactory(SessionFactory sessionFactory){
		return new ManagedSessionFactory(sessionFactory);
	}

	private HibernateJpaVendorAdapter getHibernateJpaVendorAdapter(JpaProperties jpaProperties) {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setShowSql(jpaProperties.isShowSql());
		vendorAdapter.setGenerateDdl(jpaProperties.isGenerateDdl());
		vendorAdapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
		return vendorAdapter;
	}
}