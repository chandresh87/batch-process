package com.cm.batch.processor;

import com.cm.batch.modal.PersonBO;
import com.cm.batch.modal.PersonDataModel;
import com.cm.batch.modal.mapper.PersonMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


/**
 * @author chandresh.mishra
 */
@Component("mapping-bo-item-processor")
public class MappingBOItemProcessor implements ItemProcessor<PersonDataModel, PersonBO> {

    private final PersonMapper personMapper;

    public MappingBOItemProcessor(PersonMapper personMapper) {
        this.personMapper = personMapper;
    }

    @Override
    public PersonBO process(PersonDataModel item) throws Exception {
        return personMapper.personBOMapping(item);
    }
}
