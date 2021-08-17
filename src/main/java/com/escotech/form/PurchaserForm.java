package com.escotech.form;

import com.escotech.enums.SaleMethod;
import com.escotech.enums.ShippingMethod;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter @Setter
@EqualsAndHashCode
@ToString
public class PurchaserForm {
    @NotNull(message = "Please select a shipping method.")
    private ShippingMethod shippingMethod;
    private SaleMethod saleMethod;
    @Length(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
    @NotBlank(message = "First name cannot be blank.")
    private String firstName;
    @Length(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
    @NotBlank(message = "Last name cannot be blank.")
    private String lastName;
    @Length(min = 0, max = 15, message = "Phone must be between 0 and 15 characters.")
    private String phone;
    @Email
    @Length(min = 2, max = 50, message = "Email must be between 2 and 50 characters.")
    private String email;

    public boolean inCompete() {
        if (firstName == null || lastName == null || email == null || shippingMethod == null) {
            return true;
        }
        if (firstName.isBlank() || lastName.isBlank() || email.isBlank()) {
            return true;
        }
        return false;
    }
}
