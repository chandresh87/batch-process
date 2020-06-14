package com.cm.batch.data;

import com.cm.batch.modal.AddressEntity;
import com.cm.batch.modal.PersonEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author chandresh.mishra
 */
@ExtendWith(SpringExtension.class)
//@EnableJpaRepositories(basePackageClasses = PersonRepository.class)
@DataJpaTest
public class RepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void personRepoTest()
    {
        PersonEntity personEntity =new PersonEntity();
        personEntity.setName("chandresh");
        personEntity.setLastName("Mishra");
        personEntity.setAge(30);
        personEntity.setSalary(500000);

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setHouseNumber(10);
        addressEntity.setLine1("53 A");
        addressEntity.setLine2("Glasgow");
        personEntity.setAddressEntity(addressEntity);
        personRepository.save(personEntity);

        System.out.println("Saved Entity"+personRepository.findAll());

    }
}
