package com.cm.batch.data;

import com.cm.batch.modal.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author chandresh.mishra
 */
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {
}
