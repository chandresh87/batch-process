package com.cm.batch.reader;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;

import com.cm.batch.modal.Person;

/**
 * @author chandresh.mishra
 *
 */
//@Configuration
public class FileReader {

//	@StepScope
//	@Bean("personFileReader")
	public FlatFileItemReader<Person> personFileReader(@Value("#(jobParameters['customerFile']") FileSystemResource fileName) {

		return new FlatFileItemReaderBuilder<Person>().name("personItemReader").resource(fileName).fixedLength()
				.columns(getRange()).names(getNames()).targetType(Person.class).build();

	}

	private Range[] getRange() {
		return new Range[] { new Range(1, 15), new Range(16, 30), new Range(31, 33), new Range(34, 43),
				new Range(44, 46), new Range(47, 58), new Range(59, 66), };
	}

	private String[] getNames() {
		return new String[] { "name", "lastName", "age", "salary", "houseNumber", "line1", "line2", };
	}
}
