package com.escotech.dto;

import com.escotech.enums.Category;
import com.escotech.enums.Condition;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@EqualsAndHashCode
@ToString
@Getter @Setter
public class CartProduct {

    private Long productId;
    private Category category;
    private String brand;
    private String model;
    private String serialNumber;
    private BigDecimal unitCost;
    private BigDecimal unitShipping;
    private Condition condition;
    @PositiveOrZero(message = "Quantity must be zero or greater.")
    @NotNull(message = "Quantity must be a number.")
    private Integer quantity;
    private Integer inventoryCount;
    private BigDecimal lineTotal;

    public CartProduct() {
        this.unitCost = BigDecimal.valueOf(0);
        this.unitShipping = BigDecimal.valueOf(0);
        this.quantity = 0;
        this.inventoryCount = 0;
        this.lineTotal = BigDecimal.valueOf(0);
    }

    public void setLineTotal() {
        BigDecimal quantityBD = new BigDecimal(this.quantity);
        lineTotal = quantityBD.multiply(this.unitCost);
    }

}
