package com.example.ubernet.controller;

import com.example.ubernet.dto.PaymentOrder;
import com.example.ubernet.service.PayPalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/payment", produces = MediaType.APPLICATION_JSON_VALUE)
public class PayPalController {
    private final PayPalService service;
    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";
    public static final String BASE_URL = "http://localhost:8000/";

    @PostMapping(value = "/pay")
    public String payment(@RequestBody PaymentOrder paymentOrder) {
        try {
            Payment payment = service.createPayment(paymentOrder.getPrice(), paymentOrder.getCurrency(),
                    paymentOrder.getMethod(), paymentOrder.getIntent(), paymentOrder.getDescription(),
                    BASE_URL + CANCEL_URL, BASE_URL + SUCCESS_URL);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }

        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping(value = CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping(value = SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("payerID") String payerID) {
        try {
            Payment payment = service.executePayment(paymentId, payerID);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return "success";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }

        return "redirect:/";
    }




}
