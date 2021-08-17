package com.escotech.service;

import com.escotech.entity.Order;
import com.escotech.entity.OrderLine;
import com.escotech.enums.ShippingMethod;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class PayPalService {

    @Value("${paypal.client.id}")
    private String clientId;
    @Value("${paypal.client.secret}")
    private String secretId;
    private final BigDecimal taxRate = new BigDecimal("0.095");


    public PayPalHttpClient client() {
//      PayPalEnvironment environment = new PayPalEnvironment.Live(clientId,secretId);
        PayPalEnvironment environment = new PayPalEnvironment.Sandbox(clientId,secretId);

        // PayPal HTTP client instance with environment that has access credentials context. Use to invoke PayPal APIs.
        PayPalHttpClient client = new PayPalHttpClient(environment);
        client.setConnectTimeout((int) TimeUnit.SECONDS.toMillis(900));
        return client;
    }

    // Creates empty request body for capture request.
    public OrderRequest buildRequestBody() {
        return new OrderRequest();
    }

    // Creates request body for create request.
    public OrderRequest buildRequestBody(Order order) {
        ApplicationContext applicationContext = new ApplicationContext().brandName("EscoTech").landingPage("BILLING");
        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
                                                        .amountWithBreakdown(getAmountWithBreakdown(order))
                                                        .items(getPurchaseUnitItems(order));
        if (order.getShippingMethod().equals(ShippingMethod.PICKPUP)) {
            applicationContext.shippingPreference("NO_SHIPPING");
        } else {
            applicationContext.shippingPreference("SET_PROVIDED_ADDRESS");
            purchaseUnitRequest.shippingDetail(getShippingDetail(order));
        }
        // Add this line when you go live; right now it is generating duplicate invoice ids
        // for paypal whenever we re-creating the database.
//        purchaseUnitRequest.invoiceId(String.valueOf(order.getId()));
        List<PurchaseUnitRequest> purchaseUnitRequests = new ArrayList<>();
        purchaseUnitRequests.add(purchaseUnitRequest);
        return new OrderRequest()
                .checkoutPaymentIntent("CAPTURE")
                .applicationContext(applicationContext)
                .purchaseUnits(purchaseUnitRequests);
    }

    private AmountWithBreakdown getAmountWithBreakdown(Order order) {
        AmountBreakdown amountBreakdown = new AmountBreakdown()
                .itemTotal(new Money().currencyCode("USD").value(String.valueOf(order.getSubTotal())))
                .shipping(new Money().currencyCode("USD").value(String.valueOf(order.getTotalShipping())))
                .taxTotal(new Money().currencyCode("USD").value(String.valueOf(order.getTax())));
        return new AmountWithBreakdown()
                .currencyCode("USD")
                .amountBreakdown(amountBreakdown)
                .value(String.valueOf(order.getTotalCost()));
    }

    private Name getName(Order order) {
        StringBuilder name = new StringBuilder(order.getFirstName());
        name.append(" ");
        name.append(order.getLastName());
        return new Name()
                .fullName(name.toString());
    }

    private ShippingDetail getShippingDetail(Order order) {
        AddressPortable addressPortable = new AddressPortable().addressLine1(order.getAddress1())
                .addressLine2(order.getAddress2()).adminArea2(order.getCity()).adminArea1(order.getState())
                .postalCode(order.getZip()).countryCode(String.valueOf(order.getCountry()));
        return new ShippingDetail()
                .name(this.getName(order))
                .addressPortable(addressPortable);
    }

    private List<Item> getPurchaseUnitItems(Order order) {
        List<Item> items = new ArrayList<>();
        StringBuilder name = new StringBuilder();
        for (OrderLine line: order.getOrderLines()) {
            name.append(line.getBrand());
            name.append(" ");
            name.append(line.getModel());
            BigDecimal tax = taxRate.multiply(line.getUnitCost()).setScale(2, RoundingMode.HALF_UP);
            Item item = new Item()
                    .name(name.toString())
                    .sku(line.getSerialNumber())
                    .quantity(String.valueOf(line.getQuantity()))
                    .unitAmount(new Money().currencyCode("USD").value(String.valueOf(line.getUnitCost())))
                    .tax(new Money().currencyCode("USD").value(String.valueOf(tax)));
            items.add(item);
            name.delete(0, name.length());
        }
        return items;
    }
}
