package com.cm.batch.config;

import java.io.File;

import javax.sql.DataSource;

import org.apache.catalina.webresources.FileResource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.cm.batch.modal.Person;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {

//	@Autowired
//	@Qualifier("personFileReader")
//	private FlatFileItemReader<Person> personFileReader;

//	@Autowired
//	@Qualifier("dummuItemWriter")
//	private ItemWriter<Person> dummyWriter;

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
				.reader(personFileReader(null))
				.writer(dummuItemWriter()).build();
		
//		return this.stepBuilderFactory.get("step1")
//				.tasklet(helloWorldTasklet(null))
//				.build();
	}

	@Bean("dummuItemWriter")
	public ItemWriter<Person> dummuItemWriter() {
		return (items) -> items.forEach(System.out::println);
	}


	@Bean
	public Job job() {
		return this.jobBuilderFactory.get("person-job")
				.start(step())
			    .validator(batchJobParamValidator)
				.incrementer(jobParametersIncrementer)
				.build();
	}
//	
//	@Bean
//	public Tasklet helloWorldTasklet() {
//
//		return (contribution, chunkContext) -> {
//				String name = (String) chunkContext.getStepContext()
//					.getJobParameters()
//					.get("customerFile");
//
//				System.out.println(String.format("Hello, %s!", name));
//				return RepeatStatus.FINISHED;
//			};
//	}
	
	@StepScope
	@Bean
	public Tasklet helloWorldTasklet(
			@Value("#{jobParameters['customerFile']}") String fileName) {

		return (contribution, chunkContext) -> {

				System.out.println(
						String.format("fileName = %s", fileName));

				return RepeatStatus.FINISHED;
			};
	}
	
	@StepScope
	@Bean("personFileReader")
	public FlatFileItemReader<Person> personFileReader(@Value("#{jobParameters['customerFile']}") FileSystemResource fileName) {
		System.out.println(
				String.format("fileName = %s", fileName));

	
		return new FlatFileItemReaderBuilder<Person>().name("personFileReader").resource(fileName).fixedLength()
				.columns(getRange()).names(getNames()).targetType(Person.class).build();

	}

	private Range[] getRange() {
		return new Range[] { new Range(1, 15), new Range(16, 30), new Range(31, 33), new Range(34, 43),
				new Range(44, 46), new Range(47, 58), new Range(59, 66), };
	}

	private String[] getNames() {
		return new String[] { "name", "lastName", "age", "salary", "houseNumber", "line1", "line2", };
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
