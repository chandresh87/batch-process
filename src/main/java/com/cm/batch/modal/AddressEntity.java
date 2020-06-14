/**
 *
 */
package com.cm.batch.modal;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author chandresh.mishra
 *
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "address")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    private int houseNumber;
    private String line1;
    private String line2;
    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private PersonEntity personEntity;

}
