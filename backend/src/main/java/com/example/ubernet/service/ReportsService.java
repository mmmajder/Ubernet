package com.example.ubernet.service;

import com.example.ubernet.dto.ReportRequest;
import com.example.ubernet.dto.ReportResponse;
import com.example.ubernet.model.Ride;
import com.example.ubernet.repository.RideRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.ubernet.utils.TimeUtils.getEndOfTheDay;
import static com.example.ubernet.utils.TimeUtils.getStartOfTheDay;

@Service
@AllArgsConstructor
public class ReportsService {

    private final RideRepository rideRepository;

    public ReportResponse getDriverReport(ReportRequest reportRequest) {
        LocalDateTime start = getStartOfTheDay(reportRequest.getStartDate());
        LocalDateTime end = getEndOfTheDay(reportRequest.getEndDate());
        List<Ride> rides = rideRepository.findRideByDriverEmailAndDateRange(reportRequest.getEmail(), start, end);
        return buildReportResponse(rides, getDatesInRange(start, end));
    }

    public ReportResponse getCustomerReport(ReportRequest reportRequest) {
        LocalDateTime start = getStartOfTheDay(reportRequest.getStartDate());
        LocalDateTime end = getEndOfTheDay(reportRequest.getEndDate());
        List<Ride> rides = rideRepository.findRideByCustomersEmailAndDateRange(reportRequest.getEmail(), start, end);
        return buildReportResponse(rides, getDatesInRange(start, end));
    }

    public ReportResponse getAdminReport(ReportRequest reportRequest) {
        LocalDateTime start = getStartOfTheDay(reportRequest.getStartDate());
        LocalDateTime end = getEndOfTheDay(reportRequest.getEndDate());
        List<Ride> rides = rideRepository.findRideByDateRange(start, end);
        return buildReportResponse(rides, getDatesInRange(start, end));
    }

    private ReportResponse buildReportResponse(List<Ride> rides, List<LocalDate> dateRange) {
        return ReportResponse.builder()
                .numberOfRides(calculateNumberOfRides(rides, dateRange))
                .numberOfKm(calculateNumberOfKm(rides, dateRange))
                .money(calculateRaisedOrSpentMoney(rides, dateRange))
                .averageMoneyPerDay(calculateAverageMoney(rides))
                .totalSum(calculateTotalSum(rides))
                .build();
    }

    private Double calculateTotalSum(List<Ride> rides) {
        Double sum = 0.0;
        for (Ride ride : rides)
            sum += ride.getPayment().getTotalPrice();
        return sum;
    }

    private Double calculateAverageMoney(List<Ride> rides) {
        if (rides.size() == 0)
            return 0.0;
        return calculateTotalSum(rides) / rides.size();
    }

    private List<LocalDate> getDatesInRange(LocalDateTime start, LocalDateTime end) {
        List<LocalDate> ret = new ArrayList<>();
        LocalDate tmp = start.toLocalDate();
        while (tmp.isBefore(end.toLocalDate()) || tmp.equals(end.toLocalDate())) {
            ret.add(tmp);
            tmp = tmp.plusDays(1);
        }
        return ret;
    }

    private List<Double> calculateRaisedOrSpentMoney(List<Ride> rides, List<LocalDate> dateRange) {
        List<Double> money = new ArrayList<>();
        for (LocalDate date : dateRange) {
            double dailySum = 0.0;
            for (Ride ride : getRidesForDate(rides, date)) {
                dailySum += ride.getPayment().getTotalPrice();
            }
            money.add(dailySum);
        }
        return money;
    }

    private List<Ride> getRidesForDate(List<Ride> rides, LocalDate date) {
        List<Ride> ridesOnDate = new ArrayList<>();
        for (Ride ride : rides) {
            if (ride.getActualStart().toLocalDate().equals(date))
                ridesOnDate.add(ride);
        }
        return ridesOnDate;
    }

    private List<Double> calculateNumberOfKm(List<Ride> rides, List<LocalDate> dateRange) {
        List<Double> km = new ArrayList<>();
        for (LocalDate date : dateRange) {
            double dailyKm = 0.0;
            for (Ride ride : getRidesForDate(rides, date)) {
                dailyKm += ride.getRoute().getKm();
            }
            km.add(dailyKm);
        }
        return km;
    }

    private List<Integer> calculateNumberOfRides(List<Ride> rides, List<LocalDate> dateRange) {
        List<Integer> money = new ArrayList<>();
        for (LocalDate date : dateRange) {
            money.add(getRidesForDate(rides, date).size());
        }
        return money;
    }
}
