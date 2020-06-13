package com.cm.batch.launcher;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

/**
 * @author chandresh.mishra
 */
@RestController
public class RestJobLauncher {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private Job job;

    @PostMapping(path = "/run")
    public Long runJob(String FilePath) throws Exception {

        JobParameters jobParameters =
                new JobParametersBuilder(getJobParameters(FilePath),
                        this.jobExplorer)
                        .getNextJobParameters(job)
                        .toJobParameters();

        return this.jobLauncher.run(job, jobParameters).getJobId();

    }

    private JobParameters getJobParameters(String path) {
        Properties properties = new Properties();
        properties.put("customerFile", path);

        return new JobParametersBuilder(properties)
                .toJobParameters();
    }
}
