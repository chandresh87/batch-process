package com.cm.batch.reader;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.core.step.tasklet.SystemCommandTasklet;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 * @author chandresh.mishra
 */
@Configuration
public class FileReader {

    private final Logger logger = LoggerFactory.getLogger(FileReader.class);

    @StepScope
    @Bean("flatFileBodyReader")
    public FlatFileItemReader<FieldSet> personFileReader(@Value("#{jobExecutionContext['dataFile']}") FileSystemResource fileName) {

        logger.info("Reading file with name {}", fileName);

        return new FlatFileItemReaderBuilder<FieldSet>()
                .name("personItemReader")
                .resource(fileName)
                .fixedLength()
                .columns(getRange())
                .names(getNames())
                .fieldSetMapper(new PassThroughFieldSetMapper())
                .linesToSkip(1)
                //.saveState(false)
                .build();

    }
    @StepScope
    @Bean("flatFileFooterReader")
    public FlatFileItemReader<FieldSet> personFileFooterReader(@Value("#{jobExecutionContext['footerFile']}") FileSystemResource fileName) {

        logger.info("footerFile fileName {}", fileName);

        return new FlatFileItemReaderBuilder<FieldSet>()
                .name("personFooterItemReader")
                .resource(fileName)
                .fixedLength()
                .columns(new Range[]{new Range(1, 10)})
                .names(new String[]{"count"})
                .fieldSetMapper(new PassThroughFieldSetMapper())
                //.saveState(false)
                .build();

    }

    private Range[] getRange() {
        return new Range[]{new Range(1, 15), new Range(16, 30), new Range(31, 33), new Range(34, 43),
                new Range(44, 46), new Range(47, 58), new Range(59, 66),};
    }

    private String[] getNames() {
        return new String[]{"name", "lastName", "age", "salary", "houseNumber", "line1", "line2",};
    }


}
