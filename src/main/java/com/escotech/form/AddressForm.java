package com.escotech.form;

import com.escotech.enums.Country;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
@EqualsAndHashCode
@ToString
public class AddressForm {

    @NotBlank(message = "Street Address cannot be blank.")
    @Length(min = 5, max = 50, message = "Street Address must be between 5 and 50 characters.")
    private String address1;
    @Length(min = 0, max = 50, message = "Apt/Suite must be between 0 and 50 characters.")
    private String address2;
    @NotBlank(message = "City cannot be blank.")
    @Length(min = 2, max = 50, message = "City must be between 2 and 50 characters.")
    private String city;
    @NotBlank(message = "State/Provice cannot be blank.")
    private String state;
    @NotBlank(message = "Zip/Postal Code cannot be blank.")
    @Length(min = 0, max = 15, message = "Zip/Postal Code must be between 0 and 15 characters.")
    private String zip;
    @NotNull(message = "Country cannot be blank.")
    private Country country;

    public boolean inCompete() {
        if (address1 == null || city == null || state == null || zip == null|| country == null) {
            return true;
        }
        if (address1.isBlank() || city.isBlank() || state.isBlank() || zip.isBlank()) {
            return true;
        }
        return false;
    }
}
