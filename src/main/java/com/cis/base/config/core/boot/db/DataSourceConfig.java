/**
 * Copyright 2023 CIS (Cam info Services)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *     http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cis.base.config.core.boot.db;

import com.cis.base.config.core.boot.JdbcDriverConfig;
import jakarta.persistence.EntityManagerFactory;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author YSivlay
 * Configuration for a DataSource.
 * @see DataSourceProperties about how to configure this DS
 */
@Configuration
@ComponentScan(basePackages = "com.cis.base.**")
@EnableTransactionManagement
public class DataSourceConfig {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Autowired
    JdbcDriverConfig config;

    @Bean
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties(config.getDriverClassName(), config.getProtocol(), config.getSubProtocol(), config.getPort());
    }

    @Bean
    public DataSource tenantDataSourceJndi() {
        PoolConfiguration p = getProperties();
        org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource(p);
        logger.info("Created new datasource; url=" + p.getUrl());
        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(tenantDataSourceJndi());
        em.setPackagesToScan("com.cis.base.**.domain");
        em.setJpaVendorAdapter(jpaVendorAdapter());
        return em;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);
        return vendorAdapter;
    }

    @Bean
    @Autowired
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public static BeanPostProcessor beanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof LocalContainerEntityManagerFactoryBean emfBean) {
                    Properties properties = new Properties();
                    properties.setProperty("hibernate.jdbc.batch_size", "100");
                    properties.setProperty("hibernate.order_inserts", "true");
                    properties.setProperty("hibernate.order_updates", "true");
                    emfBean.setJpaProperties(properties);
                }
                return bean;
            }
        };
    }

    protected DataSourceProperties getProperties() {
        return dataSourceProperties();
    }
}
