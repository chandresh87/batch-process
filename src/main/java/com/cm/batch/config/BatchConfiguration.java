package com.cm.batch.config;

import com.cm.batch.modal.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {

	@Autowired
	@Qualifier("personFileReader")
	private FlatFileItemReader<Person> personFileReader;

	@Autowired
	@Qualifier("dummuItemWriter")
	private ItemWriter<Person> dummyWriter;

	@Autowired
	@Qualifier("batch-datasource")
	private DataSource batchDataSource;

	@Autowired
	@Qualifier("batch-transaction-manager")
	private PlatformTransactionManager transactionManager;

	@Autowired
	@Qualifier("batch-param-validator")
	private JobParametersValidator batchJobParamValidator;
	
	@Autowired
	@Qualifier("batch-param-incrementer")
	private JobParametersIncrementer jobParametersIncrementer;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
 
	@Bean
	public Step step() {
		return this.stepBuilderFactory.get("chunkStep")
				.<Person, Person>chunk(10)
				.reader(personFileReader)
				.writer(dummyWriter).build();
	}

	@Bean
	public Job job() {
		return this.jobBuilderFactory.get("person-job")
				.start(step())
			    .validator(batchJobParamValidator)
				.incrementer(jobParametersIncrementer)
				.build();
	}

	@Override
	protected JobRepository createJobRepository() throws Exception {

		JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
		//factoryBean.setTablePrefix("defaultdb.cm_");
		factoryBean.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ");
		factoryBean.setTransactionManager(transactionManager);
		factoryBean.setDataSource(this.batchDataSource);
		factoryBean.afterPropertiesSet();
		return factoryBean.getObject();

	}

	@Override
	public PlatformTransactionManager getTransactionManager() {
		return this.transactionManager;
	}

	@Override
	public JobExplorer createJobExplorer() throws Exception {

		JobExplorerFactoryBean explorerFactoryBean = new JobExplorerFactoryBean();
		explorerFactoryBean.setDataSource(this.batchDataSource);
		//explorerFactoryBean.setTablePrefix("defaultdb.cm_");
		explorerFactoryBean.afterPropertiesSet();
		return explorerFactoryBean.getObject();
	}
	
	 @Override
     protected JobLauncher createJobLauncher() throws Exception {
         SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
         jobLauncher.setJobRepository(createJobRepository());
         jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
         jobLauncher.afterPropertiesSet();
         return jobLauncher;
     }

}
