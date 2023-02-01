package com.example.ubernet.service;

import com.example.ubernet.model.*;
import com.example.ubernet.model.enums.NotificationType;
import com.example.ubernet.repository.NotificationRepository;
import com.example.ubernet.repository.RideRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@AllArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final RideRepository rideRepository;
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

    public List<Notification> openNotificationForCustomer(String email) {
        List<Notification> notifications = getNotifications(email);
        for (Notification notification : notifications) {
            if (!notification.isOpened()) {
                notification.setOpened(true);
                save(notification);
            }
        }
        return notifications;
    }

    private Notification notificationFactory(String email, Long rideId) {
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
            notification.setDriverEmail(ride.getDriver().getEmail());
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

    public void createNotificationForCustomersReservationReminder(Ride ride, long minutes) {
        for (Customer customer : ride.getCustomers()) {
            Notification notification = notificationFactory(customer.getEmail(), ride.getId());
            notification.setType(NotificationType.REMINDER);
            notification.setText("Your reservation is in " + minutes + " minutes!");
            save(notification);
            simpMessagingService.notifyCustomersReservationReminder(notification);
        }
    }

    public void createNotificationForCustomersCarReachedDestination(Car car) {
        Ride nextRide = rideRepository.findRidesFromDriverEmail(car.getDriver().getEmail()).get(0);
        for (Customer customer : nextRide.getCustomers()) {
            Notification notification = notificationFactory(customer.getEmail(), nextRide.getId());
            notification.setType(NotificationType.CAR_REACHED_DESTINATION);
            notification.setText("Car reached start point!");
            save(notification);
            simpMessagingService.notifyCustomersCarReachedStartPoint(notification);
        }
    }

    public void createNotificationForCustomerTimeUntilRide(Car car) {
        Navigation navigation = car.getNavigation();
        if (car.getNavigation().getApproachFirstRide() == null) return;
        long approachFirstRideTime = calculateTimeUntilRideStarts(navigation.getApproachFirstRide());
        Ride ride = rideRepository.findRidesFromDriverEmail(car.getDriver().getEmail()).get(0);
        for (Customer customer : ride.getCustomers()) {
            Notification notification = notificationFactory(customer.getEmail(), ride.getId());
            notification.setType(NotificationType.CAR_POSITION);
            notification.setText(String.valueOf(approachFirstRideTime));
            save(notification);
            simpMessagingService.notifyCustomersTimeUntilRide(notification);
        }
    }

    private long calculateTimeUntilRideStarts(CurrentRide currentRide) {
        int numberOfPositions = currentRide.getPositions().size();
        LocalDateTime endTime = currentRide.getStartTime().plusSeconds((long) currentRide.getPositions().get(numberOfPositions - 1).getSecondsPast());
        if (endTime.isAfter(LocalDateTime.now())) {
            return LocalDateTime.now().until(endTime, ChronoUnit.SECONDS);
        }
        return 0;
    }

    public void notifyCustomerForDriverInconsistency(Customer customer, Ride ride) {
        Notification notification = notificationFactory(customer.getEmail(), ride.getId());
        notification.setType(NotificationType.DRIVER_INCONSISTENCY);
        notification.setText("Driver is heading in wrong direction. Click to report!");
        save(notification);
        simpMessagingService.notifyCustomersDriverInconsistency(notification);
    }

    public void createNotificationForCustomersRideDenied(Ride ride) {
        for (Customer customer : ride.getCustomers()) {
            Notification notification = notificationFactory(customer.getEmail(), ride.getId());
            notification.setType(NotificationType.RIDE_DENIED);
            notification.setText("There are no available cars. Your request has been canceled. You will soon have your tokens back. You can request new ride.");
            save(notification);
            simpMessagingService.notifyCustomersTimeUntilRide(notification);
        }
    }

    public void createNotificationForCustomersMoneyPayback(Customer customer) {
        Notification notification = notificationFactory(customer.getEmail(), null);
        notification.setType(NotificationType.RIDE_DENIED);
        notification.setText("Your ride has been canceled. We sent you your tokens back.");
        save(notification);
    }
}
