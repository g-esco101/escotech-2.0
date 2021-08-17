package com.escotech;

import com.escotech.dto.Cart;
import com.escotech.dto.CartProduct;
import com.escotech.dto.ProductView;
import com.escotech.entity.Image;
import com.escotech.entity.Product;
import com.escotech.enums.Category;
import com.escotech.enums.Condition;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

public class TestUtils {

    public static  CartProduct cartProduct1(int inventory, int quantity) {
        BigDecimal unitCost = new BigDecimal("1250.00");
        CartProduct cartProduct = new CartProduct();
        cartProduct.setProductId(1L);
        cartProduct.setCategory(Category.PHONE);
        cartProduct.setBrand("EscoTech");
        cartProduct.setModel("Budget");
        cartProduct.setSerialNumber("ETLB101");
        cartProduct.setUnitCost(new BigDecimal("1250.00"));
        cartProduct.setUnitShipping(new BigDecimal("150.00"));
        cartProduct.setCondition(Condition.USED);
        cartProduct.setInventoryCount(inventory);
        cartProduct.setLineTotal(unitCost.multiply(BigDecimal.valueOf(quantity)));
        cartProduct.setQuantity(quantity);
        return cartProduct;
    }

    public static  Product product1(int inventory) {
        List<Image> images = new LinkedList<>();
        Product product = new Product();
        product.setId(1L);
        product.setCategory(Category.PHONE);
        product.setBrand("EscoTech");
        product.setModel("Budget");
        product.setSerialNumber("ETLB101");
        product.setUnitCost(new BigDecimal("1250.00"));
        product.setUnitShipping(new BigDecimal("150.00"));
        product.setCondition(Condition.USED);
        product.setInventoryCount(inventory);
        product.setDescription("The Budget is the most affordable time machine on the market for a single traveler. It is also the slowest, but it will get you when and where you need to be in spacetime.");
        product.setImages(images);
        return product;
    }

    public static ProductView productView1(int inventory) {
        ProductView product = new ProductView();
        product.setId(1L);
        product.setCategory(Category.PHONE);
        product.setBrand("EscoTech");
        product.setModel("Budget");
        product.setSerialNumber("ETLB101");
        product.setUnitCost(new BigDecimal("1250.00"));
        product.setUnitShipping(new BigDecimal("150.00"));
        product.setCondition(Condition.USED);
        product.setInventoryCount(inventory);
        product.setDescription("The Budget is the most affordable time machine on the market for a single traveler. It is also the slowest, but it will get you when and where you need to be in spacetime.");
        product.setImageCount(0);
        return product;
    }

    public static  CartProduct cartProduct2(int inventory, int quantity) {
        BigDecimal unitCost = new BigDecimal("600.00");
        CartProduct cartProduct = new CartProduct();
        cartProduct.setProductId(2L);
        cartProduct.setCategory(Category.BRITISH_BOOTH);
        cartProduct.setBrand("EscoTech");
        cartProduct.setModel("Premier");
        cartProduct.setSerialNumber("ETP9999");
        cartProduct.setUnitCost(unitCost);
        cartProduct.setUnitShipping(new BigDecimal("100.00"));
        cartProduct.setCondition(Condition.USED);
        cartProduct.setInventoryCount(inventory);
        cartProduct.setLineTotal(unitCost.multiply(BigDecimal.valueOf(quantity)));
        cartProduct.setQuantity(quantity);
        return cartProduct;
    }

    public static  Product product2(int inventory) {
        List<Image> images = new LinkedList<>();
        Product product = new Product();
        product.setId(2L);
        product.setCategory(Category.PHONE);
        product.setBrand("EscoTech");
        product.setModel("Premier");
        product.setSerialNumber("ETP9999");
        product.setUnitCost(new BigDecimal("600.00"));
        product.setUnitShipping(new BigDecimal("100.00"));
        product.setCondition(Condition.USED);
        product.setInventoryCount(inventory);
        product.setDescription("The Premier is the most expensive time machine on the market for a single traveler. It will get you when and where you need to be in the fastest time possible. It uses EscoTech's latest technology as of July 2021, which defies the laws of physics as most humans know it.");
        product.setImages(images);
        return product;
    }

    public static  ProductView productView2(int inventory) {
        ProductView product = new ProductView();
        product.setId(2L);
        product.setCategory(Category.PHONE);
        product.setBrand("EscoTech");
        product.setModel("Premier");
        product.setSerialNumber("ETP9999");
        product.setUnitCost(new BigDecimal("600.00"));
        product.setUnitShipping(new BigDecimal("100.00"));
        product.setCondition(Condition.USED);
        product.setInventoryCount(inventory);
        product.setDescription("The Premier is the most expensive time machine on the market for a single traveler. It will get you when and where you need to be in the fastest time possible. It uses EscoTech's latest technology as of July 2021, which defies the laws of physics as most humans know it.");
        product.setImageCount(0);
        return product;
    }

    public static  CartProduct cartProduct3(int inventory, int quantity) {
        BigDecimal unitCost = new BigDecimal("1500.00");
        BigDecimal unitShipping = new BigDecimal("200.00");
        CartProduct cartProduct = new CartProduct();
        cartProduct.setProductId(3L);
        cartProduct.setCategory(Category.BOOTH);
        cartProduct.setBrand("EscoTech");
        cartProduct.setModel("Family");
        cartProduct.setSerialNumber("ETF500");
        cartProduct.setUnitCost(unitCost);
        cartProduct.setUnitShipping(unitShipping);
        cartProduct.setCondition(Condition.USED);
        cartProduct.setInventoryCount(inventory);
        cartProduct.setLineTotal(unitCost.multiply(BigDecimal.valueOf(quantity)));
        cartProduct.setQuantity(quantity);
        return cartProduct;
    }

    public static  Product product3(int inventory) {
        List<Image> images = new LinkedList<>();
        Product product = new Product();
        product.setId(3L);
        product.setCategory(Category.PHONE);
        product.setBrand("EscoTech");
        product.setModel("Family");
        product.setSerialNumber("ETF500");
        product.setUnitCost(new BigDecimal("1500.00"));
        product.setUnitShipping(new BigDecimal("200.00"));
        product.setCondition(Condition.USED);
        product.setInventoryCount(inventory);
        product.setDescription("The Family is the most affordable time machine on the market for a group of travelers. It is also the slowest for group travel, but it will get you when and where you need to be in spacetime.");
        product.setImages(images);
        return product;
    }

    public static  ProductView productView3(int inventory) {
        ProductView product = new ProductView();
        product.setId(3L);
        product.setCategory(Category.PHONE);
        product.setBrand("EscoTech");
        product.setModel("Family");
        product.setSerialNumber("ETF500");
        product.setUnitCost(new BigDecimal("1500.00"));
        product.setUnitShipping(new BigDecimal("200.00"));
        product.setCondition(Condition.USED);
        product.setInventoryCount(inventory);
        product.setDescription("The Family is the most affordable time machine on the market for a group of travelers. It is also the slowest for group travel, but it will get you when and where you need to be in spacetime.");
        product.setImageCount(0);
        return product;
    }

    public static Cart cart(List<CartProduct> orderLines) {
        BigDecimal taxRate = new BigDecimal("0.095");
        Integer totalQuantity = orderLines.stream()
                .mapToInt(CartProduct::getQuantity)
                .sum();

        BigDecimal subtotal = orderLines.stream()
                .map(CartProduct::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

//        BigDecimal tax = subtotal.multiply(taxRate);
        BigDecimal tax = orderLines.stream()
                .map(oL -> oL.getUnitCost().multiply(taxRate).setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal(oL.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalShipping = orderLines.stream()
                .map(oL -> new BigDecimal(oL.getQuantity()).multiply(oL.getUnitShipping()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Cart cart = new Cart();
        cart.setTotalQuantity(totalQuantity);
        cart.setSubTotal(subtotal);
        cart.setTax(tax);
        cart.setTotalShipping(totalShipping);
        cart.setTotalCost(subtotal.add(tax.add(totalShipping)));
        cart.setOrderLines(orderLines);
        return cart;
    }
}
