/**
 * 
 */
package com.cm.batch.writer;

import com.cm.batch.modal.Person;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chandresh.mishra
 *
 */
@Configuration
public class DummyWriter {

	@Bean("dummuItemWriter")
	public ItemWriter<Person> dummuItemWriter() {
		return (items) -> items.forEach(System.out::println);
	}

}
