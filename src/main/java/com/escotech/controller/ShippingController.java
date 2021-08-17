package com.escotech.controller;

import com.escotech.enums.Country;
import com.escotech.enums.Province;
import com.escotech.enums.ShippingMethod;
import com.escotech.enums.State;
import com.escotech.form.AddressForm;
import com.escotech.dto.Cart;
import com.escotech.form.PurchaserForm;
import com.escotech.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@SessionAttributes({"purchaserForm", "addressForm"})
@Controller
public class ShippingController {

    private final ShippingService service;

    @Autowired
    public ShippingController(ShippingService service) {
        this.service = service;
    }

    // GET: Displays shipping form.
    @GetMapping("/address")
    public String addressForm(final Model model,
                               @ModelAttribute("addressForm") AddressForm addressForm,
                               BindingResult result, @SessionAttribute("cart") Cart cart) {

        if (cart == null || cart.getOrderLines() == null || cart.getOrderLines().isEmpty()) {
            return "redirect:/cart";
        }
        setShipMethodMap(model);
        if (addressForm.getCountry() == Country.CN) {
            model.addAttribute("stateEnum", Province.values());
        }
        else {
            model.addAttribute("stateEnum", State.values());
        }
        model.addAttribute("addressForm", addressForm);
        return "address";
    }

    // POST: Save shipping information.
    @PostMapping("/address")
    public String address(final Model model, @SessionAttribute Cart cart,
                          @Valid @ModelAttribute("addressForm") AddressForm addressForm, BindingResult resultForm,
                          @ModelAttribute(name = "purchaserForm", binding = false) PurchaserForm purchaserForm,
                          final RedirectAttributes redirectAttributes) {

        if(resultForm.hasErrors()) {
            // Must create the map and the enum again if there is an error,
            // because they are not passed back to the method, like purchaserForm.
            setShipMethodMap(model);
            if (addressForm.getCountry() == Country.CN) {
                model.addAttribute("stateEnum", Province.values());
            } else {
                model.addAttribute("stateEnum", State.values());
            }
            return "address";
        }
        purchaserForm.setShippingMethod(ShippingMethod.TWOTOSEVEN);
        if (addressForm.getCountry().equals(Country.CN)) {
            service.calculateShipping(cart,2);
        } else {
            service.calculateShipping(cart, 1);
        }
        redirectAttributes.addFlashAttribute("order", cart);
        return "redirect:/orderReview";
    }


    // GET: Displays shipping form.
    @GetMapping("/purchaser")
    public String purchaserForm(final Model model,
                               @ModelAttribute("purchaserForm") PurchaserForm purchaserForm,
                               BindingResult result, @SessionAttribute("cart") Cart cart) {

        if (cart == null || cart.getOrderLines() == null || cart.getOrderLines().isEmpty()) {
            return "redirect:/cart";
        }
        setShipMethodMap(model);
        model.addAttribute("purchaserForm", purchaserForm);
        return "purchaser";
    }
    // POST: Save shipping information.
    @PostMapping("/purchaser")
    public String purchaser(final Model model,
                           @Valid @ModelAttribute("purchaserForm") PurchaserForm purchaserForm,
                           BindingResult resultForm, @ModelAttribute(name = "addressForm", binding = false) AddressForm addressForm) {

        if(resultForm.hasErrors()) {
            // Must create the map and the enum again if there is an error,
            // because they are not passed back to the method, like purchaserForm.
            setShipMethodMap(model);
            return "purchaser";
        }
        if (purchaserForm.getShippingMethod().equals(ShippingMethod.PICKPUP) || !addressForm.inCompete()) {
            return "redirect:/orderReview";
        }
        return "redirect:/address";
    }

    // Updates the dropdown list with the state enum or province enum based on the country
    // that is selected in the country dropdown list.
    @GetMapping("/state/enum/{country}")
    public String getStateEnum(Model model, @PathVariable String country) {

        if (country.equals(String.valueOf(Country.CN))) {
            model.addAttribute("stateEnum", Province.values());
        } else {
            model.addAttribute("stateEnum", State.values());
        }
        return "address :: #stateUpdate";
    }

    private void setShipMethodMap(final Model model) {
        Map<String, String> shipMethodMap = new HashMap<>();
        shipMethodMap.put(ShippingMethod.PICKPUP.getDisplayName(), "$0.00");
        shipMethodMap.put(ShippingMethod.TWOTOSEVEN.getDisplayName(), "ground shipping rate");
        model.addAttribute("shipMethodMap", shipMethodMap);
    }

    @ModelAttribute("purchaserForm")
    public PurchaserForm purchaserForm() {
        return new PurchaserForm();
    }
    @ModelAttribute("addressForm")
    public AddressForm addressForm() {
        return new AddressForm();
    }
}
