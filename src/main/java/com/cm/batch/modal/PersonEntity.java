package com.cm.batch.modal;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author chandresh.mishra
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "person")
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastName;
    private int age;
    private double salary;
    @OneToOne(mappedBy = "personEntity",cascade = CascadeType.ALL)
    private AddressEntity addressEntity;
}
