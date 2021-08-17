package com.escotech.dto;

import com.escotech.enums.Category;
import com.escotech.enums.Condition;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@NoArgsConstructor
public class ProductView {
    private Long id;
    private Category category;
    private String brand;
    private String model;
    private String serialNumber;
    private String description;
    private Integer inventoryCount;
    private BigDecimal unitCost;
    private BigDecimal unitShipping;
    private Condition condition;
    private int imageCount;
}
