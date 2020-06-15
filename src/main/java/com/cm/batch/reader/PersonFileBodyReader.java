package com.cm.batch.reader;

import com.cm.batch.modal.PersonDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author chandresh.mishra
 */
@Component("personFileBodyReader")
@StepScope
public class PersonFileBodyReader implements ItemStreamReader<PersonDataModel> {

    private final ItemStreamReader<FieldSet> itemStreamReader;
    private final Logger logger = LoggerFactory.getLogger(PersonFileBodyReader.class);
    private int recordCount = 0;
    private int expectedRecordCount;
    private StepExecution stepExecution;

    public PersonFileBodyReader(@Qualifier("flatFileBodyReader") ItemStreamReader itemStreamReader) {
        this.itemStreamReader = itemStreamReader;
    }

    @Override
    public PersonDataModel read() throws Exception {
        return process(itemStreamReader.read());
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.itemStreamReader.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        this.itemStreamReader.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        this.itemStreamReader.close();
    }

    @AfterStep
    public ExitStatus afterStep(StepExecution execution) {
        if (recordCount == expectedRecordCount && execution.getReadCount()>0) {
            return execution.getExitStatus();
        } else {
            return ExitStatus.STOPPED;
        }
    }

    @BeforeStep
    public void beforeStep(StepExecution execution) {
        this.stepExecution = execution;
        this.expectedRecordCount = execution.getJobExecution().getExecutionContext().getInt("recordCount");
        logger.info("Expected record count is {}", expectedRecordCount);
    }

    private PersonDataModel process(FieldSet fieldSet) {
        PersonDataModel result = null;
        if (fieldSet != null) {

                result = PersonDataModel.builder()
                        .name(fieldSet.readString(0))
                        .lastName(fieldSet.readString(1))
                        .age(fieldSet.readInt(2))
                        .salary(fieldSet.readDouble(3))
                        .houseNumber(fieldSet.readInt(4))
                        .line1(fieldSet.readString(5))
                        .line2(fieldSet.readString(6)).build();

                recordCount++;
            } else {

                if (expectedRecordCount != this.recordCount) {
                    this.stepExecution.setTerminateOnly();
                }
            }
        return result;
    }
}
