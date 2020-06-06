/**
 * 
 */
package com.cm.batch.config;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author chand
 *
 */
@Configuration
@EnableTransactionManagement
public class AppConfig {

	@Bean(name = "batch-transaction-manager")
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(getDataSource());
	}

	   @Bean("batch-datasource")
	    public DataSource getDataSource() {
	        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
	        dataSourceBuilder.driverClassName("org.postgresql.Driver");
	        dataSourceBuilder.url("jdbc:postgresql://localhost:26257/defaultdb?sslmode=disable&timezone=UTC");
	        dataSourceBuilder.username("root");
	        dataSourceBuilder.password("");
	        return dataSourceBuilder.build();
	    }
}
