package com.example.ubernet.dto;

import com.example.ubernet.model.DriverNotification;
import com.example.ubernet.model.Ride;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RideDriverNotificationDTO {
    private Ride ride;
    private DriverNotification driverNotification;
}
