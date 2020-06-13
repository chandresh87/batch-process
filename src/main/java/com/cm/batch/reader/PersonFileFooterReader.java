package com.cm.batch.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author chandresh.mishra
 */
@Component("read-footer-task")
@StepScope
public class PersonFileFooterReader implements Tasklet {

    private final ItemStreamReader<FieldSet> itemStreamReader;
    private final Logger logger = LoggerFactory.getLogger(PersonFileFooterReader.class);

    public PersonFileFooterReader(@Qualifier("flatFileFooterReader") ItemStreamReader<FieldSet> itemStreamReader) {
        this.itemStreamReader = itemStreamReader;
    }


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        itemStreamReader.open(chunkContext.getStepContext().getStepExecution().getExecutionContext());
        FieldSet fieldSet = itemStreamReader.read();
        int recordCount = Integer.parseInt(fieldSet.readString(0).trim());
        logger.info("record Count is {}", recordCount);
        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("recordCount", recordCount);
        return RepeatStatus.FINISHED;
    }
}
