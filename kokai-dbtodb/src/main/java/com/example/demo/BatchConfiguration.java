package com.example.demo;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfiguration {

    // tag::readerwriterprocessor[]
    @Bean
    public JdbcCursorItemReader<Person> reader(@Qualifier("testdb") DataSource testdb) {
        return new JdbcCursorItemReaderBuilder<Person>()
                .name("personItemReader")
                .dataSource(testdb)
                .sql("SELECT person_id, first_name, last_name FROM people") // 根据你的表结构和查询方式修改
                .rowMapper(new PersonRowMapper()) // 根据你的数据结构修改
                .build();
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Person> writer(@Qualifier("postgres") DataSource postgres) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .sql("INSERT INTO people (person_id, first_name, last_name) VALUES (:personId, :firstName, :lastName)")
                .dataSource(postgres)
                .beanMapped()
                .build();
    }

    // end::readerwriterprocessor[]

    private final DataSource testdb;
    private final DataSource postgres;

    public BatchConfiguration(@Qualifier("testdb") DataSource testdb, @Qualifier("postgres") DataSource postgres) {
        this.testdb = testdb;
        this.postgres = postgres;
    }
    
    
    @Bean
    public PlatformTransactionManager transactionManagerTestDB() {
        return new DataSourceTransactionManager(testdb);
    }
    
    @Primary
    @Bean
    public PlatformTransactionManager transactionManagerPostgres() {
        return new DataSourceTransactionManager(postgres);
    }

    // tag::jobstep[]
    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1, JobCompletionNotificationListener listener) {
        return new JobBuilder("importUserJob", jobRepository)
                .listener(listener)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManagerPostgres,
                      JdbcCursorItemReader<Person> reader, PersonItemProcessor processor, 
                      JdbcBatchItemWriter<Person> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Person, Person>chunk(3)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .transactionManager(transactionManagerPostgres) // 使用 postgres 的事务管理器
                .allowStartIfComplete(true)
                .build();
    }
    // end::jobstep[]
}
