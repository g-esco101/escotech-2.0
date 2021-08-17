package com.escotech.entity;


import com.escotech.enums.Category;
import com.escotech.enums.Condition;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@Table(name = "products")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 5)
    private Category category;
    @Length(min = 1, max = 50, message = "Brand must be between 1 and 50 characters.")
    private String brand;
    @Length(min = 1, max = 50, message = "Model must be between 1 and 50 characters.")
    private String model;
    @Length(min = 1, max = 50, message = "Serial Number must be between 1 and 50 characters.")
    private String serialNumber;
    @Length(min = 1, max = 1500, message = "Description must be between 1 and 1500 characters.")
    private String description;

    @NotNull(message = "Inventory Count must have a number.")
    @PositiveOrZero(message = "Inventory Count must be zero or greater.")
    private Integer inventoryCount;

    @NotNull(message = "Unit Cost must have a number.")
    @PositiveOrZero(message = "Unit Cost must be zero or greater.")
    private BigDecimal unitCost;

    @NotNull(message = "Unit Shipping must have a number.")
    @PositiveOrZero(message = "Unit Shipping must be zero or greater.")
    private BigDecimal unitShipping;

    @Column(length = 5)
    private Condition condition;

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Image> images;

    public Product() {
        images = new ArrayList<>();
    }
}
