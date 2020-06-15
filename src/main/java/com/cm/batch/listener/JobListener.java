package com.cm.batch.listener;

import com.cm.batch.download.FileDownloader;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Component
public class JobListener extends JobExecutionListenerSupport {

    private final Logger logger = LoggerFactory.getLogger(JobListener.class);

    private final FileDownloader fileDownloader;
    private WireMockServer wireMockServer;

    public JobListener(FileDownloader fileDownloader) {
        this.fileDownloader = fileDownloader;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info("{} Job Started", jobExecution.getJobInstance().getJobName());
        try {
            wireMockServer = new WireMockServer(9000); //No-args constructor will start on port 8080, no HTTPS
            WireMock.configureFor(9000);
            wireMockServer.start();
            String filePath = fileDownloader.downloadFile();
            jobExecution.getExecutionContext().put("customerFile", filePath);
            stubFor(post("/save/person").willReturn(aResponse().
                    withStatus(200)
                    .withStatusMessage("person saved!")
                    .withHeader("Content-Type", "application/json")
                    .withBody("{\"Status\":\"saved succesfully\"}")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        logger.info("{} Job Finished with status {}", jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());
        wireMockServer.stop();
    }
}
