package com.example.demo;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class TestdbDataSourceConfig {
	@Primary
	@Bean(name = "testdb")
	public DataSource testdbDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql://localhost:5432/testdb");
		dataSource.setUsername("postgres");
		dataSource.setPassword("root");
		return dataSource;
	}

}
