/**
 *
 */
package com.cm.batch.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * @author chand
 *
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.cm.batch.data","com.cm.batch.modal"},
        transactionManagerRef = "batch-transaction-manager",
        entityManagerFactoryRef = "batchEntitMagerFactory")
@EntityScan(basePackages = "com.cm.batch.modal")
public class AppConfig {

    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_NAME_HIBERNATE_MAX_FETCH_DEPTH = "hibernate.max_fetch_depth";
    private static final String PROPERTY_NAME_HIBERNATE_JDBC_FETCH_SIZE = "hibernate.jdbc.fetch_size";
    private static final String PROPERTY_NAME_HIBERNATE_JDBC_BATCH_SIZE = "hibernate.jdbc.batch_size";
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String[] ENTITYMANAGER_PACKAGES_TO_SCAN = {"a.b.c.entities", "a.b.c.converters"};


    @Bean(name = "batch-transaction-manager")
    @Primary
    public PlatformTransactionManager transactionManager(@Qualifier("batchEntitMagerFactory") LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(localContainerEntityManagerFactoryBean.getObject());

        return jpaTransactionManager;
    }

    @Bean("batch-datasource")
    @Primary
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url("jdbc:postgresql://localhost:26257/defaultdb?sslmode=disable&timezone=UTC");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("");
        return dataSourceBuilder.build();
    }

    @Bean("batchEntitMagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(@Qualifier("batch-datasource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(dataSource);
        localContainerEntityManagerFactoryBean.setPackagesToScan("com.cm.batch.data","com.cm.batch.modal");
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "create");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.jdbc.time_zone", "UTC");
        localContainerEntityManagerFactoryBean.setJpaPropertyMap(properties);
        return localContainerEntityManagerFactoryBean;
    }

    @Bean("batchEntityManger")
    @Primary
    public EntityManager entityManager(@Qualifier("batchEntitMagerFactory") LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        return localContainerEntityManagerFactoryBean.getNativeEntityManagerFactory().createEntityManager();
    }

//    @PostConstruct
//    public void init() {
//        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
//    }
}
