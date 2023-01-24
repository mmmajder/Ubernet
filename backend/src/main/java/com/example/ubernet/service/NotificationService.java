package com.example.ubernet.service;

import com.example.ubernet.model.Customer;
import com.example.ubernet.model.Notification;
import com.example.ubernet.model.Ride;
import com.example.ubernet.model.enums.NotificationType;
import com.example.ubernet.repository.CustomerRepository;
import com.example.ubernet.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final CustomerRepository customerRepository;
    private final SimpMessagingService simpMessagingService;

    public Notification save(Notification notification) {
        return this.notificationRepository.save(notification);
    }

    public List<Notification> getNotifications(String userEmail) {
        return notificationRepository.findAllByEmail(userEmail);
    }

    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    public boolean areNotificationsSeenForUser(String email) {
        for (Notification notification : getNotifications(email)) {
            if (!notification.isOpened()) {
                return false;
            }
        }
        return true;
    }

    public void openNotificationForCustomer(String email) {
        for (Notification notification : getNotifications(email)) {
            if (!notification.isOpened()) {
                notification.setOpened(true);
                save(notification);
            }
        }
    }

    private Notification notificationFactory(String email, long rideId) {
        Notification notification = new Notification();
        notification.setRideId(rideId);
        notification.setTimeCreated(LocalDateTime.now());
        notification.setOpened(false);
        notification.setReceiverEmail(email);
        return notification;
    }

    public void createNotificationForCustomerInitRide(Ride ride) {
        for (Customer customer : ride.getCustomers()) {
            Notification notification = notificationFactory(customer.getEmail(), ride.getId());
            notification.setType(NotificationType.ACCEPTED_RIDE);
            notification.setText("Driver accepted ride! Your Uber is coming soon");
            save(notification);
            simpMessagingService.notifyCustomersInitRide(notification);
        }
    }

    public void createNotificationForCustomersEveryonePayed(Ride ride) {
        for (Customer customer : ride.getCustomers()) {
            Notification notification = notificationFactory(customer.getEmail(), ride.getId());
            notification.setType(NotificationType.EVERYONE_PAYED);
            notification.setText("All customers for ride accepted split fair!");
            save(notification);
            simpMessagingService.notifyCustomersEveryonePayed(notification);
        }
    }

    public void createNotificationForCustomersCarTechnicalProblem(Ride ride) {
        for (Customer customer : ride.getCustomers()) {
            Notification notification = notificationFactory(customer.getEmail(), ride.getId());
            notification.setType(NotificationType.CAR_TECHNICAL_PROBLEM);
            notification.setText("Unfortunately our car/driver had some problem. We will send you other Uber!");
            save(notification);
            simpMessagingService.notifyCustomersTechnicalProblem(notification);
        }
    }

    public void createNotificationForCustomersDidNotAppear(Ride ride) {
        for (Customer customer : ride.getCustomers()) {
            Notification notification = notificationFactory(customer.getEmail(), ride.getId());
            notification.setType(NotificationType.DID_NOT_APPEAR);
            notification.setText("Your ride is canceled because you did not appear on start destination!");
            save(notification);
            simpMessagingService.notifyCustomersDidNotAppear(notification);
        }
    }

    public void createNotificationForCustomersReservationReminder(Ride ride, double minutes) {
        for (Customer customer : ride.getCustomers()) {
            Notification notification = notificationFactory(customer.getEmail(), ride.getId());
            notification.setType(NotificationType.REMINDER);
            notification.setText("Your reservation is in " + minutes + " minutes!");
            save(notification);
            simpMessagingService.notifyCustomersReservationReminder(notification);
        }
    }
}
