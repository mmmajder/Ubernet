package com.example.ubernet.utils;

import com.example.ubernet.exception.BadRequestException;

import java.time.LocalDateTime;

public class TimeUtils {

    public static LocalDateTime getDateTimeForReservationMaxFiveHoursAdvance(String time) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime selectedTime = getSelectedTime(time, now);
        if (selectedTime.isAfter(now.plusHours(5))) {
            throw new BadRequestException("Reservation can be maximum 5 hours in advance");
        }
        return selectedTime;
    }

    private static LocalDateTime getSelectedTime(String time, LocalDateTime now) {
        boolean isMorning = time.split("\\s+")[1].equals("AM");
        int hours = Integer.parseInt(time.split(":")[0]);
        if (!isMorning) {
            if (hours != 12)
                hours += 12;
        } else if (hours == 12) {
            hours = 0;
        }
        int minutes = Integer.parseInt(time.split("\\s+")[0].split(":")[1]);

        LocalDateTime selectedTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), hours, minutes, 0);
        if (selectedTime.isBefore(now.minusMinutes(3))) {
            selectedTime = selectedTime.plusDays(1);
        }
        return selectedTime;
    }
}
