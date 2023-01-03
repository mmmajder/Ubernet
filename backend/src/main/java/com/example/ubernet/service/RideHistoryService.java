package com.example.ubernet.service;

import com.example.ubernet.dto.RideHistoryRequestParam;
import com.example.ubernet.dto.RideHistorySimpleResponse;
import com.example.ubernet.model.Ride;
import com.example.ubernet.repository.RideRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RideHistoryService {

    RideRepository rideRepository;

    public List<RideHistorySimpleResponse> getRides(RideHistoryRequestParam filter) {
        List<Ride> rides = rideRepository.findByDriverEmail(filter.getDriverEmail(), PageRequest.of(filter.getPageNumber(), filter.getPageSize(), getSort(filter))).getContent();
        return convertToSimpleRides(rides);
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

    private List<RideHistorySimpleResponse> convertToSimpleRides(List<Ride> rides) {
        List<RideHistorySimpleResponse> simpleRides = new ArrayList<>();
        for (Ride r : rides) {
            simpleRides.add(RideHistorySimpleResponse.builder()
                    .id(r.getId())
                    .route("Zeleznicka stanica - Nis")
//                    .route(r.getRoute().stationList())
                    .start(timeToString(r.getActualStart()))
                    .end(timeToString(r.getActualEnd()))
                    .price(r.getPayment().getTotalPrice())
                    .build());
        }
        return simpleRides;
    }

    private String timeToString(LocalDateTime time) {
        DateTimeFormatter customFormat = DateTimeFormatter.ofPattern("yyyy.dd.MM HH:mm");;
        return time.format(customFormat);
    }
}