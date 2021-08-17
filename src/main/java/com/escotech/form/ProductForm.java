package com.escotech.form;

import com.escotech.enums.Category;
import com.escotech.enums.Condition;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode
@ToString
@Getter @Setter
@NoArgsConstructor
public class ProductForm {
    private Long id;
    private Category category;
    @Length(min = 1, max = 50, message = "Brand must be between 1 and 50 characters.")
    private String brand;
    @Length(min = 1, max = 50, message = "Model must be between 1 and 50 characters.")
    private String model;
    @Length(min = 1, max = 50, message = "Serial Number must be between 1 and 50 characters.")
    private String serialNumber;
    @Length(min = 1, max = 1500, message = "Description must be between 1 and 1500 characters.")
    private String description;
    @PositiveOrZero(message = "Inventory Count must be zero or greater.")
    @NotNull(message = "Inventory Count must have a number.")
    private Integer inventoryCount;
    @PositiveOrZero(message = "Unit Cost must be zero or greater.")
    @NotNull(message = "Unit Cost must have a number.")
    private BigDecimal unitCost;
    @PositiveOrZero(message = "Unit Shipping must be zero or greater.")
    @NotNull(message = "Unit Shipping must have a number.")
    private BigDecimal unitShipping;
    private Condition condition;
    private List<MultipartFile> fileData;

    private long[] imageIds;
}
