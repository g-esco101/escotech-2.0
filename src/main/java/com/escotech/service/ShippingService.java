package com.escotech.service;

import com.escotech.dto.Cart;
import com.escotech.entity.Product;
import com.escotech.dto.CartProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ShippingService {

    private final ProductService productService;

    @Autowired
    public ShippingService(ProductService productService) {
        this.productService = productService;
    }

    // Shipping rates are doubled for Canadians...
    // Since paypal requires the shipping cost of each product, each "product" in the order must
    // be updated with the new shipping cost. You cannot just double the of the products in the
    // order, because this can more than double the cost if users toggle between the countries
    // more than once. This is analagous reasoning for implementing usaShipping().
    public void calculateShipping(Cart cart, int shippingFactor) {
        for (CartProduct line: cart.getOrderLines()) {
            Optional<Product> productOpt = productService.findById(line.getProductId());
            if (productOpt.isEmpty()) {
                cart.getOrderLines().remove(line);
                continue;
            }
            Product product = productOpt.get();
            if (product.getInventoryCount() < 1) {
                cart.getOrderLines().remove(line);
            } else {
                if (product.getInventoryCount() < line.getQuantity()) {
                    line.setQuantity(product.getInventoryCount());
                }
                line.setUnitShipping(product.getUnitShipping().multiply(new BigDecimal(shippingFactor)));
                line.setLineTotal();
            }
        }
        cart.calculateCostsAndQuantity();
    }
}
