package com.cm.batch.modal;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chandresh.mishra
 */
@Data
@NoArgsConstructor
public class PersonDTO {

    private String name;
    private String lastName;
    private int age;
    private double salary;
    private AddressDTO addressDTO;
}
