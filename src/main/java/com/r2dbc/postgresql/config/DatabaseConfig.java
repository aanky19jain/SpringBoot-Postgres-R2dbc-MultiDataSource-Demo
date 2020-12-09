package com.r2dbc.postgresql.config;

import static io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD;
import static io.r2dbc.spi.ConnectionFactoryOptions.USER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.core.DatabaseClient;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "macys.commons.datasource")
public class DatabaseConfig {

	@Autowired
	private ConfigurableApplicationContext context;

	@Bean
	@ConfigurationProperties("macys.commons.datasource.main")
	public ConnectionFactory mainConnectionFactory() {
		ConnectionFactoryOptions baseOptions = ConnectionFactoryOptions
				.parse("r2dbc:postgresql://localhost:5432/b008967");
		ConnectionFactoryOptions connectionBuilder = ConnectionFactoryOptions.builder().from(baseOptions)
				.option(USER, "postgres").option(PASSWORD, "postgres").build();
		return ConnectionFactories.get(connectionBuilder);
	}

	@Bean
	@ConfigurationProperties("macys.commons.datasource.test")
	public ConnectionFactory testConnectionFactory() {
		ConnectionFactoryOptions baseOptions = ConnectionFactoryOptions
				.parse("r2dbc:postgresql://localhost:5432/employees");
		ConnectionFactoryOptions connectionBuilder = ConnectionFactoryOptions.builder().from(baseOptions)
				.option(USER, "postgres").option(PASSWORD, "postgres").build();
		return ConnectionFactories.get(connectionBuilder);
	}

	@Primary
	@Bean
	public DatabaseClient mainDatabaseClient() {
		return DatabaseClient.create(mainConnectionFactory());
	}

	@Bean
	public DatabaseClient testDatabaseClient() {
		return DatabaseClient.create(testConnectionFactory());
	}

	public DatabaseClient databaseClient() {
		boolean testUser = false;
		if (testUser) {
			log.info("DatabaseClient created for test data source");
			return context.getBean("testDatabaseClient", DatabaseClient.class);
		} else {
			log.info("DatabaseClient created for main data source");
			return context.getBean("mainDatabaseClient", DatabaseClient.class);
		}
	}

}
