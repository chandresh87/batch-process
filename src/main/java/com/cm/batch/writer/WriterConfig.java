/**
 * 
 */
package com.cm.batch.writer;

import com.cm.batch.data.PersonRepository;
import com.cm.batch.modal.PersonBO;
import com.cm.batch.modal.PersonDTO;
import com.cm.batch.modal.PersonEntity;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chandresh.mishra
 *
 */
@Configuration
public class WriterConfig {

	@Bean("dummyItemWriter")
	public ItemWriter<PersonBO> dummyItemWriter() {
		return (items) -> items.forEach(System.out::println);
	}

	@Bean("database-writer")
	public RepositoryItemWriter<PersonEntity> repositoryItemWriter(PersonRepository repository) {
		return new RepositoryItemWriterBuilder<PersonEntity>()
				.repository(repository)
				.methodName("save")
				.build();
	}
}
