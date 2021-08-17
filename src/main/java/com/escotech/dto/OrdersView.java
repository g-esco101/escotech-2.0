package com.escotech.dto;

import com.escotech.enums.SaleMethod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
@EqualsAndHashCode
@ToString
public class OrdersView {
    private Long id;
    @Length(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
    @NotBlank(message = "First name cannot be blank.")
    private String firstName;
    @Length(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
    @NotBlank(message = "Last name cannot be blank.")
    private String lastName;
    @Email
    @Length(min = 2, max = 50, message = "Email must be between 2 and 50 characters.")
    private String email;
    private String paypalId;
    private String paypalStatus;
    @javax.validation.constraints.NotNull(message = "An order must have a date.")
    private LocalDate date;
    @Column(length = 3)
    private SaleMethod saleMethod;
    private BigDecimal totalCost;
}
