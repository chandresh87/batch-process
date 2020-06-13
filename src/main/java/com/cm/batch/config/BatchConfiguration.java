package com.cm.batch.config;

import com.cm.batch.listener.JobListener;
import com.cm.batch.listener.PreProcessFileListener;
import com.cm.batch.listener.ReadFileStepListener;
import com.cm.batch.modal.Person;
import com.cm.batch.reader.PreProcessFile;
import org.apache.commons.io.FilenameUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.listener.JobListenerFactoryBean;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {

    @Autowired
    @Qualifier("personFileBodyReader")
    private ItemStreamReader<Person> personFileBodyReader;

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


    @Autowired
    @Qualifier("read-footer-task")
    private Tasklet readFooterTask;


    @Autowired
    @Qualifier("split-body-footer-task")
    private MethodInvokingTaskletAdapter systemCommandTasklet;

    @Autowired
    private JobListener jobListener;


    @Bean("split-body-footer-task")
    @StepScope
    public MethodInvokingTaskletAdapter preProcessFileAdapter(@Value("#{jobExecutionContext['customerFile']}") String file, PreProcessFile preProcessFile)
    {
        String workingDir = FilenameUtils.getFullPath(file);
        String fileName = FilenameUtils.getName(file);

        MethodInvokingTaskletAdapter methodInvokingTaskletAdapter=new MethodInvokingTaskletAdapter();
        methodInvokingTaskletAdapter.setTargetObject(preProcessFile);
        methodInvokingTaskletAdapter.setTargetMethod("processFile");
        methodInvokingTaskletAdapter.setArguments(new String[]{fileName,workingDir});

        return methodInvokingTaskletAdapter;
    }


    @Bean("retrieve-body-footer-step")
    public Step extractFooterAndBodyStep() {
        return this.stepBuilderFactory.get("splitBodyFooter")
                .tasklet(systemCommandTasklet)
                .listener(new PreProcessFileListener())
                .listener(executionContextPromotionListener())
                .build();
    }

    @Bean("read-footer-step")
    public Step readFooterStep() {
        return this.stepBuilderFactory.get("readFooter")
                .tasklet(readFooterTask)
                .listener(executionContextPromotionListener())
                .build();
    }

    @Bean
	public Flow preProcessingFlow() {
        return new FlowBuilder<Flow>("preProcessingFlow")
                .start(extractFooterAndBodyStep())
                .next(readFooterStep())
                .build();
    }
    @Bean
	public Step initializeBatch() {
		return this.stepBuilderFactory.get("initializeBatch")
				.flow(preProcessingFlow())
				.build();
	}

    @Bean("readBodyStep")
    public Step readBodystep() {
        return this.stepBuilderFactory.get("readChunkStep")
                .<Person, Person>chunk(10)
                .reader(personFileBodyReader)
                .writer(dummyWriter)
                .listener(new ReadFileStepListener())
                .build();
    }

    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("person-job")
                .start(initializeBatch())
                .next(readBodystep())
                .validator(batchJobParamValidator)
                .incrementer(jobParametersIncrementer)
                .listener(jobListener)
                .build();
    }

    @Bean
    public ExecutionContextPromotionListener executionContextPromotionListener()
    {
        ExecutionContextPromotionListener executionContextPromotionListener = new ExecutionContextPromotionListener();
        executionContextPromotionListener.setKeys(new String[] {"dataFile","footerFile","recordCount"});
        return executionContextPromotionListener;

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
