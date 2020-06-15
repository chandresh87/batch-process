/**
 * 
 */
package com.cm.batch.writer;

import com.cm.batch.data.PersonRepository;
import com.cm.batch.modal.PersonBO;
import com.cm.batch.modal.PersonEntity;
import com.cm.batch.modal.mapper.PersonMapper;
import com.cm.batch.remote.PersonRemoteClient;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.item.adapter.PropertyExtractingDelegatingItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

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

	@Bean("remote-service-writer")
	public ItemWriterAdapter<PersonEntity> serviceItemWriter(PersonRemoteClient personRemoteClient) {
		ItemWriterAdapter<PersonEntity> customerItemWriterAdapter = new ItemWriterAdapter<>();

		customerItemWriterAdapter.setTargetObject(personRemoteClient);
		customerItemWriterAdapter.setTargetMethod("savePerson");

		return customerItemWriterAdapter;
	}

	@Bean("composite-writer")
	public CompositeItemWriter<PersonEntity> compositeItemWriter() throws Exception {
		return new CompositeItemWriterBuilder<PersonEntity>()
				.delegates(Arrays.asList(repositoryItemWriter(null),
						serviceItemWriter(null)))
				.build();
	}
}
