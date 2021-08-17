package com.escotech.entity;

import com.escotech.enums.Country;
import com.escotech.enums.SaleMethod;
import com.escotech.enums.ShippingMethod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity @ToString
@EqualsAndHashCode
@Table(name = "orders")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    List<OrderLine> orderLines;
    @Column(length = 3)
    private SaleMethod saleMethod;
    @NotNull(message = "Must have a shipping method.")
    @Column(length = 6)
    private ShippingMethod shippingMethod;
    @NotNull(message = "Must have tax.")
    @PositiveOrZero(message = "Tax must be positive or zero.")
    private BigDecimal tax;
    @Transient
    private BigDecimal subTotal;
    @Transient
    private BigDecimal totalCost;
    @Transient
    @PositiveOrZero
    private Integer totalQuantity;
    @Transient
    @PositiveOrZero(message = "Total Shipping must be positive or zero.")
    private BigDecimal totalShipping;

    public Order() {
        orderLines = new ArrayList<>();
        subTotal = new BigDecimal(0);
    }

    // Calculates the totalQauntity, subTotal and totalShipping
    // when the order is retrieved from the database.
    @PostLoad
    public final void calculateCostsAndQuantity() {
        this.orderLines.stream().forEach(line -> {
            BigDecimal quantityBD = new BigDecimal(line.getQuantity());
            line.setLineTotal(quantityBD.multiply(line.getUnitCost()));
        });

        this.totalQuantity = orderLines.stream()
                .mapToInt(OrderLine::getQuantity)
                .sum();

        this.subTotal = orderLines.stream()
                .map(OrderLine::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (this.getShippingMethod().equals(ShippingMethod.PICKPUP)) {
            setTotalShipping(new BigDecimal(0));
        } else {
            this.totalShipping = orderLines.stream()
                    .map(oL -> new BigDecimal(oL.getQuantity()).multiply(oL.getUnitShipping()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        this.totalCost = this.totalShipping
                .add(this.subTotal)
                .add(this.tax);
    }
}
