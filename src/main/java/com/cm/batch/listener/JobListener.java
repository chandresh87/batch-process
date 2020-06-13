package com.cm.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;


public class JobListener {

    private final Logger logger = LoggerFactory.getLogger(JobListener.class);

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        logger.info("{} Job Started", jobExecution.getJobInstance().getJobName());
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        logger.info("{} Job Finished with status {}", jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());
    }
}
