package com.cm.batch.listener;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

/**
 * @author chandresh.mishra
 */
public class PreProcessFileListener {

    private final Logger logger = LoggerFactory.getLogger(PreProcessFileListener.class);


    @AfterStep
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.info("{}  ended with status {}", stepExecution.getStepName(), stepExecution.getExitStatus());

        String fileName = stepExecution.getJobParameters().getString("customerFile");
        String workingDir = FilenameUtils.getFullPath(fileName);
        logger.info("working dir set Input File Name {}", workingDir);
        stepExecution.getJobExecution().getExecutionContext().put("dataFile", workingDir + "input_body.txt");
        stepExecution.getJobExecution().getExecutionContext().put("footerFile", workingDir + "input_footer.txt");

        return stepExecution.getExitStatus();
    }
}
