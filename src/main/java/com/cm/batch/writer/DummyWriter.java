/**
 * 
 */
package com.cm.batch.writer;

import com.cm.batch.modal.PersonBO;
import com.cm.batch.modal.PersonDTO;
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
	public ItemWriter<PersonBO> dummuItemWriter() {
		return (items) -> items.forEach(System.out::println);
	}

}
