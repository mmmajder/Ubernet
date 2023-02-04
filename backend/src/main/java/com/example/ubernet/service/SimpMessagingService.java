package com.example.ubernet.service;

import com.example.ubernet.dto.CarResponse;
import com.example.ubernet.dto.RideDriverNotificationDTO;
import com.example.ubernet.model.Customer;
import com.example.ubernet.model.DriverNotification;
import com.example.ubernet.model.Notification;
import com.example.ubernet.model.Ride;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class SimpMessagingService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendStartRideNotificationToDriver(String driverEmail, Ride ride, DriverNotification driverNotification) {
        RideDriverNotificationDTO rideDriverNotificationDTO = new RideDriverNotificationDTO();
        rideDriverNotificationDTO.setDriverNotification(driverNotification);
        rideDriverNotificationDTO.setRide(ride);
        this.simpMessagingTemplate.convertAndSend("/notify-driver/start-ride-" + driverEmail, rideDriverNotificationDTO);
    }

    public void sendEndRideNotification(String driverEmail, DriverNotification driverNotification) {
        this.simpMessagingTemplate.convertAndSend("/notify-driver/end-ride-" + driverEmail, driverNotification);
    }

    public void sendNextRideNotification(String driverEmail, DriverNotification driverNotification) {
        this.simpMessagingTemplate.convertAndSend("/notify-driver/new-ride-" + driverEmail, driverNotification);
    }

    public void declineRide(List<DriverNotification> driverNotifications, String driverEmail) {
        this.simpMessagingTemplate.convertAndSend("/notify-driver/decline-ride-" + driverEmail, driverNotifications);

    }

    public void updateRouteForSelectedCar(String driverEmail, Ride ride) {
        this.simpMessagingTemplate.convertAndSend("/map-updates/update-route-for-selected-car-" + driverEmail, ride);
    }

    public void updateVehiclePosition(List<CarResponse> carsResponse) {
        this.simpMessagingTemplate.convertAndSend("/map-updates/update-vehicle-position", carsResponse);
    }

    public void notifyCustomersSplitFair(String customerEmail, Notification notification) {
        this.simpMessagingTemplate.convertAndSend("/notify/split-fare-" + customerEmail, notification);

    }
    public void sendPaybackNotification(String email, double numberOfTokens) {
        this.simpMessagingTemplate.convertAndSend("/customer/payback-" + email, numberOfTokens);
    }

    public void notifyCustomersInitRide(Notification notification) {
        this.simpMessagingTemplate.convertAndSend("/customer/init-ride-" + notification.getReceiverEmail(), notification);
    }

    public void notifyCustomersEveryonePayed(Notification notification) {
        this.simpMessagingTemplate.convertAndSend("/customer/everyone-payed-" + notification.getReceiverEmail(), notification);
    }

    public void notifyCustomersTechnicalProblem(Notification notification) {
        this.simpMessagingTemplate.convertAndSend("/customer/technical-problem-" + notification.getReceiverEmail(), notification);

    }

    public void notifyCustomersReservationReminder(Notification notification) {
        this.simpMessagingTemplate.convertAndSend("/customer/reservation-reminder-" + notification.getReceiverEmail(), notification);
    }

    public void notifyCustomersCarReachedStartPoint(Notification notification) {
        this.simpMessagingTemplate.convertAndSend("/customer/car-start-point-" + notification.getReceiverEmail(), notification);
    }

    public void notifyCustomersDidNotAppear(Notification notification) {
        this.simpMessagingTemplate.convertAndSend("/customer/did-not-appear-" + notification.getReceiverEmail(), notification);

    }

    public void notifyCustomersTimeUntilRide(Notification notification) {
        this.simpMessagingTemplate.convertAndSend("/customer/time-until-ride-" + notification.getReceiverEmail(), notification);
    }

    public void notifyCustomersDriverInconsistency(Notification notification) {
        this.simpMessagingTemplate.convertAndSend("/customer/driver-inconsistency-" + notification.getReceiverEmail(), notification);
    }

    public void endShiftDriverNotification(DriverNotification driverNotification, String email) {
        this.simpMessagingTemplate.convertAndSend("/driver/end-shift-" + email, driverNotification);
    }

    public void sendNumberOfWorkingSecondsToDriver(long numberOfActiveSeconds, String email) {
        this.simpMessagingTemplate.convertAndSend("/driver/active-seconds-" + email, numberOfActiveSeconds);
    }
}
