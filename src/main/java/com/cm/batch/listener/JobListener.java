package com.cm.batch.listener;

import com.cm.batch.download.FileDownloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JobListener extends JobExecutionListenerSupport {

    private final Logger logger = LoggerFactory.getLogger(JobListener.class);

    private final FileDownloader fileDownloader;

    public JobListener(FileDownloader fileDownloader) {
        this.fileDownloader = fileDownloader;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info("{} Job Started", jobExecution.getJobInstance().getJobName());
        try {
            String filePath = fileDownloader.downloadFile();
            jobExecution.getExecutionContext().put("customerFile", filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        logger.info("{} Job Finished with status {}", jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());
    }
}
