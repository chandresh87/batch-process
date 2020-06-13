package com.cm.batch.modal;

import lombok.Builder;
import lombok.Data;

/**
 * @author chandresh.mishra
 *
 */
@Data
@Builder
public class Person {

    private String name;
    private String lastName;
    private int age;
    private double salary;
    private int houseNumber;
    private String line1;
    private String line2;


}
