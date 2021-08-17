package com.escotech.dto;

import com.escotech.entity.OrderLine;
import com.escotech.enums.Country;
import com.escotech.enums.SaleMethod;
import com.escotech.enums.ShippingMethod;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter @Setter
public class OrderView {

    private Long id;
    private String paypalId;
    private String paypalStatus;
    @Length(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
    @NotBlank(message = "First name cannot be blank.")
    private String firstName;
    @Length(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
    @NotBlank(message = "Last name cannot be blank.")
    private String lastName;
    private String phone;
    @Email
    @Length(min = 2, max = 50, message = "Email must be between 2 and 50 characters.")
    private String email;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private Country country;
    @NotNull(message = "An order must have a date.")
    private LocalDate date;
    @NotNull(message = "An order must have a time.")
    private LocalTime time;
    @NotNull(message = "An order must have products.")
    List<OrderLine> orderLines;
    @Column(length = 3)
    private SaleMethod saleMethod;
    @NotNull(message = "Must have a shipping method.")
    @Column(length = 6)
    private ShippingMethod shippingMethod;
    @NotNull(message = "Must have tax.")
    @PositiveOrZero(message = "Tax must be positive or zero.")
    private BigDecimal tax;
    private BigDecimal subTotal;
    private BigDecimal totalCost;
    @PositiveOrZero
    private Integer totalQuantity;
    @PositiveOrZero(message = "Total Shipping must be positive or zero.")
    private BigDecimal totalShipping;
}
