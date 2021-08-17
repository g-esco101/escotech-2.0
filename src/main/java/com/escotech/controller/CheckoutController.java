package com.escotech.controller;

import com.escotech.enums.SaleMethod;
import com.escotech.enums.ShippingMethod;
import com.escotech.form.AddressForm;
import com.escotech.dto.Cart;
import com.escotech.form.PurchaserForm;
import com.escotech.service.EmailService;
import com.escotech.service.MapStructMapper;
import com.escotech.service.OrderService;
import com.escotech.service.PayPalService;

import com.paypal.http.HttpResponse;
import com.paypal.orders.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

@SessionAttributes({"order"})
@RestController
public class CheckoutController {

    private final static Logger logger = LoggerFactory.getLogger(CheckoutController.class);

    private final PayPalService service;

    private final OrderService orderService;

    private final MapStructMapper mapper;

    private final EmailService emailService;

    @Autowired
    public CheckoutController(PayPalService service, OrderService orderService, MapStructMapper mapper, EmailService emailService) {
        this.service = service;
        this.orderService = orderService;
        this.mapper = mapper;
        this.emailService = emailService;
    }

    // Creates PayPal Order when user logins to paypal.
    @Transactional
    @PostMapping(value = "create/order")
    public String createOrder(final Model model, HttpSession session,
                                                    @ModelAttribute("order") com.escotech.entity.Order order) throws IOException {

        logger.info("create/order");
        Cart cart = (Cart) session.getAttribute("cart");
        PurchaserForm purchaserForm = (PurchaserForm) session.getAttribute("purchaserForm");
        AddressForm addressForm = (AddressForm) session.getAttribute("addressForm");

        order = mapper.cartFormToOrder(cart);
        mapper.purchaserFormToOrder(purchaserForm, order);
        mapper.addressFormToOrder(addressForm, order);

        OrdersCreateRequest request = new OrdersCreateRequest();
        request.requestBody(service.buildRequestBody(order));
        HttpResponse<Order> response = service.client().execute(request);
        logger.info("create/order - response.statusCode(): {}. response.result().status(): {}.", response.statusCode(), response.result().status());
        if (response.statusCode() == 201 && response.result().status().equals("CREATED")) {
            order.setDate(LocalDate.now());
            order.setTime(LocalTime.now());
            order.setPaypalId(response.result().id());
            order.setPaypalStatus(response.result().status());
            order.setSaleMethod(SaleMethod.WEBAPP);
        }
        // On the first request, when a model attribute with the name, order, is added to
        // the model, it is automatically promoted to and saved in the HTTP Servlet session.
        model.addAttribute("order", order);

        session.removeAttribute("purchaserForm");
        session.removeAttribute("addressForm");
        session.removeAttribute("cart");
        return order.getPaypalId();
    }

    // Captures order from PayPal - stores the order in the database and updates the inventory of the Products on the order.
    @Transactional(noRollbackFor = MessagingException.class)
    @PostMapping("/paypal/capture")
    public String captureOrder(HttpSession session, SessionStatus status) throws IOException {
        logger.info("/paypal/capture");
        com.escotech.entity.Order order = (com.escotech.entity.Order) session.getAttribute("order");
        StringBuilder confirmation = new StringBuilder("Order #: ");
        if (order.getPaypalId() == null) {
            logger.error("/paypal/capture - paypal id: null");
            confirmation.append("Unable to process order - please contact store to check status");
            return confirmation.toString();
        }
        OrdersCaptureRequest request = new OrdersCaptureRequest(order.getPaypalId());
        OrderRequest orderRequest= service.buildRequestBody();
        if (order.getShippingMethod().equals(ShippingMethod.PICKPUP)) {
            Name name = new Name().givenName(order.getFirstName())
                                    .surname(order.getLastName())
                                    .fullName(order.getFirstName() + order.getLastName());

            Payer payer = new Payer().name(name)
                                    .email(order.getEmail());

            orderRequest.payer(payer);
        }
        request.requestBody(orderRequest);
        request.requestBody(service.buildRequestBody());
        HttpResponse<Order> response = service.client().execute(request);
        logger.info("/paypal/capture - response.statusCode: {}. response.result.status: {}.",
                response.statusCode(), response.result().status());
        if (response.statusCode() == 201) {
            order.setPaypalStatus(response.result().status());
            if (order.getPaypalStatus().equals("COMPLETED")) {
                orderService.updateInventoryCount(order);
                orderService.create(order);
                confirmation.append(order.getId());
                try {
                    emailService.sendEditableMail(order, Locale.ENGLISH);
                    confirmation.append(String.format("\n\nReceipt sent to %s", order.getEmail()));
                } catch (MessagingException m) {
                    logger.error("/paypal/capture - order #: {}.\nMessagingException: {}",
                            order.getId(), m.getMessage());
                    confirmation.append("\n\nFailed to send receipt - please contact store to check status");
                }
            }
        }
        return confirmation.toString();
    }

    @ModelAttribute("order")
    public com.escotech.entity.Order order() {
        return new com.escotech.entity.Order();
    }
}

