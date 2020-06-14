package com.cm.batch.modal;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author chandresh.mishra
 */
@Data
@NoArgsConstructor
public class PersonBO {

    @NotNull(message = "First name is required")
    @Pattern(regexp = "[a-zA-Z]+", message = "First name must be alphabetical")
    private String name;
    @NotNull(message = "Last name is required")
    @Pattern(regexp = "[a-zA-Z]+", message = "Last name must be alphabetical")
    private String lastName;
    @NotNull(message = "Age is required")
    private int age;
    @NotNull(message = "Salary is required")
    private double salary;
    @NotNull(message = "AddressBO is required")
    private AddressBO addressBO;
}
