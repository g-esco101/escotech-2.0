package com.escotech.controller;

import com.escotech.dto.Cart;
import com.escotech.dto.CartProduct;
import com.escotech.enums.ShippingMethod;
import com.escotech.form.*;
import com.escotech.entity.Product;
import com.escotech.service.MapStructMapper;
import com.escotech.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SessionAttributes("cart")
@Controller
public class ShoppingController {

    private final static Logger logger = LoggerFactory.getLogger(ShoppingController.class);

    private final ProductService service;

    private final MapStructMapper mapper;

    @Autowired
    public ShoppingController(ProductService service, MapStructMapper mapper) {
        logger.info("ShoppingController");
        this.service = service;
        this.mapper = mapper;
    }

    // GET: Show order.
    @GetMapping("/cart")
    public String getCart(final Model model, @ModelAttribute("cart") Cart cart,
                          @SessionAttribute(required = false) AddressForm addressForm,
                          @SessionAttribute(required = false) PurchaserForm purchaserForm) {

        logger.info("/cart");
        if (purchaserForm != null && !purchaserForm.inCompete()) {
            model.addAttribute("needAddress", !purchaserForm.getShippingMethod().equals(ShippingMethod.PICKPUP));
            model.addAttribute("purchaserComplete", true);
        } else {
            model.addAttribute("purchaserComplete", false);
        }

        if (addressForm != null && !addressForm.inCompete()) {
            model.addAttribute("addressComplete", true);
        } else {
            model.addAttribute("addressComplete", false);
        }
        model.addAttribute("cart", cart);
        return "cart";
    }

    @ResponseBody
    @PostMapping("/addItem/{id}")
    public void addItem(@PathVariable("id") Long id,
                        @ModelAttribute(value = "cart", binding = false) Cart cart) {

        logger.info("/addItem/{}", id);
        Product product = service.findByIdAndInventoryCountGreaterThan(id, 0);
        if(product == null) {
            logger.error("addItem - findByIdAndInventoryCountGreaterThan - null - id: {}", id);
            return;
        }
        CartProduct cartProduct = mapper.productToCartProduct(product);
        int index = cart.indexOf(cartProduct);
        if (index >= 0) {
            CartProduct cp = cart.getOrderLines().get(index);
            int quantity = cp.getQuantity() + 1;
            if (quantity <= cp.getInventoryCount()) {
                cp.setQuantity(quantity);
                cp.setLineTotal();
            }
        } else {
            cartProduct.setQuantity(1);
            cartProduct.setLineTotal();
            cart.getOrderLines().add(cartProduct);
        }
        cart.calculateCostsAndQuantity();
    }

    @RequestMapping("/removeItem/{id}")
    public String removeItem(@PathVariable("id") Long id,
                             @ModelAttribute(value = "cart", binding = false) Cart cart) {

        logger.info("/removeItem/{}", id);
        cart.getOrderLines().removeIf(p -> p.getProductId().equals(id));
        cart.calculateCostsAndQuantity();
        return "redirect:/cart";
    }

    // POST: Updates the quantities of the products in the cart.
    @ResponseBody
    @PostMapping("/updateCart")
    public void updateQuantity(@ModelAttribute("cart") Cart cart,
                                 @RequestBody CartProduct[] cartProducts,
                                 BindingResult result) {

        logger.info("/updateCart");
        if (result.hasErrors()) {
            return;
        }
        for (CartProduct prod: cart.getOrderLines()) {
            Arrays.stream(cartProducts)
                    .filter(cartProd -> prod.getProductId().equals(cartProd.getProductId()))
                    .forEach(cartProd -> {
                        prod.setQuantity(cartProd.getQuantity());
                        prod.setLineTotal();
                    });
        }
        cart.calculateCostsAndQuantity();
    }

    // GET: Show information to confirm.
    @GetMapping("/orderReview")
    public String review(final Model model,
                         @ModelAttribute(value = "cart", binding = false) Cart cart,
                         @SessionAttribute PurchaserForm purchaserForm,
                         @SessionAttribute AddressForm addressForm) {

        logger.info("/orderReview");
        if (cart.getOrderLines() == null || cart.getOrderLines().isEmpty()) {
            model.addAttribute("cart", cart);
            return "cart";
        }
        List<CartProduct> cartProducts = cart.getOrderLines().stream()
                                                        .filter(cp -> {
                                                            Optional<Product> prodOpt = service.findById(cp.getProductId());
                                                            Product prod = prodOpt.get();
                                                            return cp.getQuantity() > 0 && prod.getInventoryCount() > 0;
                                                        })
                                                        .map(cp -> {
                                                            BigDecimal quantityBD = new BigDecimal(cp.getQuantity());
                                                            cp.setLineTotal(quantityBD.multiply(cp.getUnitCost()));
                                                            return cp;
                                                        })
                                                        .collect(Collectors.toList());
        cart.setOrderLines(cartProducts);
        cart.setTotalQuantity();
        if (purchaserForm.getShippingMethod().equals(ShippingMethod.PICKPUP)) {
            cart.setTotalShipping(new BigDecimal(0));
        } else {
            cart.setTotalShipping();
        }
        cart.setSubTotal();
        cart.setTax();
        cart.setTotalCost();

        model.addAttribute("order", cart);
        model.addAttribute("purchaserForm", purchaserForm);
        model.addAttribute("addressForm", addressForm);
        return "orderReview";
    }

    @ModelAttribute("cart")
    public Cart cart() {
        logger.info("ModelAttribute - cart");
        return new Cart();
    }
}
