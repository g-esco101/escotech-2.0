package com.escotech.controller;

import com.escotech.dto.Cart;
import com.escotech.dto.OrderView;
import com.escotech.dto.OrdersView;
import com.escotech.entity.Order;
import com.escotech.form.*;
import com.escotech.service.MapStructMapper;
import com.escotech.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class OrderController {

    private final static Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService service;

    private final MapStructMapper mapper;

    @Autowired
    public OrderController(OrderService service, MapStructMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/admin/orders")
    public String getOrders(final Model model) {
        logger.info("/admin/orders");
        List<OrdersView> ordersView = service.findAllByOrderByIdDesc().stream()
                                                .map(mapper::orderToOrdersView)
                                                .collect(Collectors.toList());
        model.addAttribute("orders", ordersView);
        return "orders";
    }

    @GetMapping("/admin/order/{id}")
    public String getOrder(final Model model, @PathVariable("id") Long id) {
        logger.info("/admin/order/{}", id);

        Optional<Order> orderOpt = service.findById(id);
        if (orderOpt.isEmpty()) {
            logger.error("getOrder - order not present - id: " + id);
            List<OrdersView> ordersView = service.findAllByOrderByIdDesc().stream()
                                                    .map(mapper::orderToOrdersView)
                                                    .collect(Collectors.toList());
            model.addAttribute("orders", ordersView);
            return "orders";
        }
        OrderView orderView = mapper.orderToOrderView(orderOpt.get());
        model.addAttribute("order", orderView);
        return "order";
    }

    @RequestMapping({ "/admin/order/delete/{id}" })
    public String delete(final Model model, @PathVariable("id") Long id) {
        logger.info("/admin/order/delete/{}", id);
        service.deleteById(id);
        List<OrdersView> ordersView = service.findAllByOrderByIdDesc().stream()
                                                .map(mapper::orderToOrdersView)
                                                .collect(Collectors.toList());
        model.addAttribute("orders", ordersView);
        return "orders";
    }

    @PostMapping("/admin/order/create")
    public String createOrder(HttpSession session, SessionStatus status) {
        logger.info("/admin/order/create");

        Cart cart = (Cart) session.getAttribute("cart");
        PurchaserForm purchaserForm = (PurchaserForm) session.getAttribute("purchaserForm");
        AddressForm addressForm = (AddressForm) session.getAttribute("addressForm");

        Order order = mapper.cartFormToOrder(cart);
        mapper.purchaserFormToOrder(purchaserForm, order);
        mapper.addressFormToOrder(addressForm, order);
        order.setDate(LocalDate.now());
        order.setTime(LocalTime.now());
        service.updateInventoryCount(order);
        service.create(order);
        status.setComplete();
        session.removeAttribute("order");
        session.removeAttribute("purchaserForm");
        session.removeAttribute("addressForm");
        session.removeAttribute("cart");
        return "redirect:/admin/order/" + order.getId();
    }
}
