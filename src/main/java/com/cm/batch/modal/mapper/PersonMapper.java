package com.cm.batch.modal.mapper;

import com.cm.batch.modal.PersonBO;
import com.cm.batch.modal.PersonDTO;
import com.cm.batch.modal.PersonEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author chandresh.mishra
 */
@Mapper
public interface PersonMapper {

    @Mapping(target = "addressBO.houseNumber", source = "houseNumber")
    @Mapping(target = "addressBO.line1", source = "line1")
    @Mapping(target = "addressBO.line2", source = "line2")
    PersonBO personBOMapping(PersonDTO person);

    @Mapping(target = "addressEntity", source = "addressBO")
    PersonEntity personBOToEntity(PersonBO personBO);

    @AfterMapping
    default PersonEntity populateEntity(@MappingTarget PersonEntity personEntity) {
        personEntity.getAddressEntity().setPersonEntity(personEntity);
        return personEntity;
    }
}
