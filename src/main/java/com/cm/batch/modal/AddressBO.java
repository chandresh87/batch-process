/**
 * 
 */
package com.cm.batch.modal;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author chandresh.mishra
 *
 */
@Data
@NoArgsConstructor
public class AddressBO {

	@NotNull(message = "House Number is required")
	private int houseNumber;

	@NotNull(message="Line 1 is required")
	@Pattern(regexp="[0-9a-zA-Z\\. ]+")
	private String line1;

	@NotNull(message="City is required")
	@Pattern(regexp="[a-zA-Z\\. ]+")
	private String line2;

}
