/**
 * 
 */
package com.cm.batch.writer;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cm.batch.modal.Person;

/**
 * @author chandresh.mishra
 *
 */
//@Configuration
//@StepScope
public class DummyWriter {

	@Bean("dummuItemWriter")
	public ItemWriter<Person> dummuItemWriter() {
		return (items) -> items.forEach(System.out::println);
	}

}
