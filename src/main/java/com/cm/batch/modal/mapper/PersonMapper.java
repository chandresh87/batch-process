package com.cm.batch.modal.mapper;

import com.cm.batch.modal.PersonBO;
import com.cm.batch.modal.PersonDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author chandresh.mishra
 */
@Mapper
public interface PersonMapper {

    @Mapping(target = "addressBO.houseNumber", source = "houseNumber")
    @Mapping(target = "addressBO.line1", source = "line1")
    @Mapping(target = "addressBO.line2", source = "line2")
    PersonBO personBOMapping(PersonDTO person);

}
