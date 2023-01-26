package com.example.ubernet.service;

import com.example.ubernet.dto.RideDetails;
import com.example.ubernet.dto.RideHistoryRequestParam;
import com.example.ubernet.dto.RideHistorySimpleResponse;
import com.example.ubernet.dto.SimpleUser;
import com.example.ubernet.model.Customer;
import com.example.ubernet.model.Ride;
import com.example.ubernet.repository.RideRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
public class RideHistoryService {

    RideRepository rideRepository;

    public Page<RideHistorySimpleResponse> getRides(RideHistoryRequestParam filter) {
        PageRequest pageRequest = PageRequest.of(filter.getPageNumber(), filter.getPageSize(), getSort(filter));
        if (filter.getCustomerEmail().equals("") && filter.getDriverEmail().equals(""))
            return convertToSimpleRides(rideRepository.findAll(pageRequest));
        if (filter.getCustomerEmail().equals(""))
            return convertToSimpleRides(rideRepository.findByDriverEmail(filter.getDriverEmail(), pageRequest));
        if (filter.getDriverEmail().equals(""))
            return convertToSimpleRides(rideRepository.findByCustomersEmail(filter.getCustomerEmail(), pageRequest));
        Page<Ride> page = rideRepository.findByDriverEmailAndCustomersEmail(filter.getDriverEmail(), filter.getCustomerEmail(), pageRequest);
        return convertToSimpleRides(page);
    }

    private Sort getSort(RideHistoryRequestParam filter) {
        String sortKind = getSortKind(filter.getSortKind());
        if (filter.getSortOrder().equals("asc"))
            return Sort.by(sortKind).ascending();
        return Sort.by(sortKind).descending();
    }

    private String getSortKind(String sortKind) {
        return switch (sortKind) {
            case "price" -> "payment.totalPrice";
            case "start" -> "actualStart";
            case "end" -> "actualEnd";
            default -> sortKind;
        };
    }

    private Page<RideHistorySimpleResponse> convertToSimpleRides(Page<Ride> page) {
        List<RideHistorySimpleResponse> simpleRides = new ArrayList<>();
        for (Ride r : page.getContent()) {
            simpleRides.add(RideHistorySimpleResponse.builder()
                    .id(r.getId())
                    .route(r.getRoute().stationListConcatenated())
                    .start(timeToString(r.getActualStart()))
                    .end(timeToString(r.getActualEnd()))
                    .price(r.getPayment().getTotalPrice())
                    .build());
        }
        return new PageImpl<>(simpleRides, page.getPageable(), page.getTotalElements());
    }

    private String timeToString(LocalDateTime time) {
        DateTimeFormatter customFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return time.format(customFormat);
    }

    public RideDetails getRideById(Long id) {
        Ride ride = rideRepository.findById(id).get();
        return RideDetails.builder()
                .id(ride.getId())
                .driver(SimpleUser.builder()
                        .email(ride.getDriver().getEmail())
                        .name(ride.getDriver().getName())
                        .surname(ride.getDriver().getSurname())
                        .build())
                .customers(createSimpleUsers(ride.getCustomers()))
                .actualEnd(timeToString(ride.getActualEnd()))
                .actualStart(timeToString(ride.getActualStart()))
                .scheduledStart(timeToString(ride.getScheduledStart()))
                .reservationTime(timeToString(ride.getRequestTime()))
                .checkPoints(ride.getRoute().getCheckPoints())
                .totalPrice(ride.getPayment().getTotalPrice())
                .carReviews(ride.getCarReviews())
                .driverReviews(ride.getDriverReviews())
                .build();
    }

    private List<SimpleUser> createSimpleUsers(List<Customer> customers) {
        return new ArrayList<>() {{
            for (Customer customer : customers)
                add(SimpleUser.builder()
                        .email(customer.getEmail())
                        .name(customer.getName())
                        .surname(customer.getSurname())
                        .build());
        }};
    }
}
