package com.cm.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.scope.context.ChunkContext;



public class ReadFileStepListener {

    private final Logger logger = LoggerFactory.getLogger(ReadFileStepListener.class);

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        logger.info("{}  Started", stepExecution.getStepName());
    }

    @AfterStep
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.info("{}  ended with status {}", stepExecution.getStepName(), stepExecution.getExitStatus());
        return stepExecution.getExitStatus();
    }

    @BeforeChunk
    public void beforeChunk(ChunkContext chunkContext) {
        logger.info("Reading chunk");
    }

    @AfterChunk
    public void afterChunk(ChunkContext chunkContext) {
        logger.info("chunk ended with read count {}", chunkContext.getStepContext().getStepExecution().getReadCount());

    }

}
