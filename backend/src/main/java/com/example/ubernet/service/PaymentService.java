package com.example.ubernet.service;

import com.example.ubernet.model.CustomerPayment;
import com.example.ubernet.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class PaymentService {

    private final CustomerRepository customerRepository;
    private final SimpMessagingService simpMessagingService;
    private final NotificationService notificationService;
    public void returnMoneyToCustomers(List<CustomerPayment> customerPayments) {
        for (CustomerPayment payment : customerPayments) {
            if (payment.isPayed()) {
                double price = payment.getPricePerCustomer();
                payment.getCustomer().setNumberOfTokens(payment.getCustomer().getNumberOfTokens() + price);
                customerRepository.save(payment.getCustomer());
                notificationService.createNotificationForCustomersMoneyPayback(payment.getCustomer());
                simpMessagingService.sendPaybackNotification(payment.getCustomer().getEmail(), payment.getCustomer().getNumberOfTokens());
                System.out.println("Return money");
                System.out.println(payment.getCustomer().getNumberOfTokens());
            }
        }
    }
}
