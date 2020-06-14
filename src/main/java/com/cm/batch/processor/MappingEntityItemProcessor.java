package com.cm.batch.processor;

import com.cm.batch.data.PersonRepository;
import com.cm.batch.modal.PersonBO;
import com.cm.batch.modal.PersonEntity;
import com.cm.batch.modal.mapper.PersonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


/**
 * @author chandresh.mishra
 */
@Component("mapping-entity-item-processor")
public class MappingEntityItemProcessor implements ItemProcessor<PersonBO, PersonEntity> {

    private final PersonMapper personMapper;
    PersonRepository repository;

    public MappingEntityItemProcessor(PersonMapper personMapper,PersonRepository repository) {
        this.personMapper = personMapper;
        this.repository=repository;
    }

    @Override
    public PersonEntity process(PersonBO item) throws Exception {
        PersonEntity personEntity = personMapper.personBOToEntity(item);
        repository.save(personEntity);
        return personEntity;
    }
}
