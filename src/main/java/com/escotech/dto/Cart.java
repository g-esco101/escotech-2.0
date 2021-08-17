package com.escotech.dto;

import lombok.*;

import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode
@ToString
@Getter @Setter
public class Cart {

    private final BigDecimal taxRate = new BigDecimal("0.095");
    private int totalQuantity;
    private BigDecimal subTotal;
    @NotNull(message = "Must have tax.")
    @PositiveOrZero(message = "Tax must be positive or zero.")
    private BigDecimal tax;
    @PositiveOrZero(message = "Total Shipping must be positive or zero.")
    private BigDecimal totalShipping;
    private BigDecimal totalCost;
    private List<CartProduct> orderLines;

    public Cart() {
        totalQuantity = 0;
        subTotal = BigDecimal.valueOf(0);
        tax = BigDecimal.valueOf(0);
        totalShipping = BigDecimal.valueOf(0);
        totalCost = BigDecimal.valueOf(0);
        orderLines = new ArrayList<>();
    }

    public int indexOf(CartProduct cartProduct) {
        return orderLines.stream()
                            .filter(cp -> cp.getProductId().equals(cartProduct.getProductId()))
                            .findFirst()
                            .map(product -> orderLines.indexOf(product))
                            .orElse(-1);
    }
    public void setTotalQuantity() {
        this.totalQuantity = orderLines.stream()
                .mapToInt(CartProduct::getQuantity)
                .sum();
    }

    // OrderLine setLineTotal must be called for each orderLine in the order
    // and setTotalQuantity must be called before this method can be called.
    public void setSubTotal() {
        this.subTotal = orderLines.stream()
                .map(CartProduct::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Tax must be calculated like this; otherwise, paypal throws an exception.
    public void setTax() {
        this.tax = orderLines.stream()
                .map(oL -> oL.getUnitCost().multiply(taxRate).setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal(oL.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void setTotalShipping() {
        this.totalShipping = orderLines.stream()
                .map(oL -> new BigDecimal(oL.getQuantity()).multiply(oL.getUnitShipping()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // setTotalShipping, setSubTotal and setTax must be called before calling this method.
    public void setTotalCost() {
        this.totalCost = this.totalShipping
                .add(this.subTotal)
                .add(this.tax);
    }

    // Calculates the totalQauntity, subTotal and totalShipping
    // when the order is retrieved from the database.
    public final void calculateCostsAndQuantity() {
        setTotalQuantity();
        setTotalShipping();
        setSubTotal();
        setTax();
        setTotalCost();
    }
}
