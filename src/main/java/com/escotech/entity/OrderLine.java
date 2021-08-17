package com.escotech.entity;

import com.escotech.enums.Category;
import com.escotech.enums.Condition;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@EqualsAndHashCode
@ToString
@Table(name = "order_lines")
public class OrderLine implements Serializable {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter @Setter
    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "Product id must be numeric.")
    @PositiveOrZero(message ="Product id must be zero or greater.")
    private Long productId;
    @Getter @Setter
    @Column(length = 5)
    private Category category;
    @Getter @Setter
    @Length(min = 1, max = 50, message = "Brand must be between 1 and 50 characters.")
    private String brand;
    @Getter @Setter
    @Length(min = 1, max = 50, message = "Model must be between 1 and 50 characters.")
    private String model;
    @Getter @Setter
    @Length(min = 1, max = 50, message = "Serial Number must be between 1 and 50 characters.")
    private String serialNumber;
    @Getter @Setter
    @PositiveOrZero(message = "Unit cost must be zero or greater.")
    @NotNull(message = "Unit shipping cannot be empty.")
    private BigDecimal unitCost;
    @Getter @Setter
    @PositiveOrZero(message = "Unit shipping must be zero or greater.")
    @NotNull(message = "Unit shipping cannot be empty.")
    private BigDecimal unitShipping;
    @Getter @Setter
    @Column(length = 5)
    private Condition condition;
    @Getter
    @Setter
    @PositiveOrZero(message = "Quantity must be zero or greater.")
    @NotNull(message = "Quantity cannot be empty.")
    private Integer quantity;

    @Getter
    @Transient @Setter
    private BigDecimal lineTotal;

    public OrderLine() {
        unitCost = new BigDecimal(0);
        unitShipping = new BigDecimal(0);
        lineTotal = new BigDecimal(0);
        quantity = 0;
    }
}
