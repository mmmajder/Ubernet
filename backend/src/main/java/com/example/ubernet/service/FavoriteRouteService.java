package com.example.ubernet.service;

import com.example.ubernet.dto.FavoriteRouteItem;
import com.example.ubernet.dto.FavoriteRouteRequest;
import com.example.ubernet.model.Customer;
import com.example.ubernet.model.Ride;
import com.example.ubernet.repository.CustomerRepository;
import com.example.ubernet.repository.RideRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FavoriteRouteService {

    private final CustomerRepository customerRepository;
    private final RideRepository rideRepository;

    public boolean ifRouteFavorite(FavoriteRouteRequest dto) {
        Customer customer = customerRepository.findByEmail(dto.getCustomerEmail());
        Optional<Ride> ride = rideRepository.findById(dto.getRideId());
        return ride.filter(value -> getRideIds(customer.getFavoriteRoutes()).contains(value.getId())).isPresent();
    }

    public List<FavoriteRouteItem> getFavoriteRoutes(String customerEmail) {
        List<FavoriteRouteItem> routes = new ArrayList<>();
        Customer customer = customerRepository.findByEmail(customerEmail);
        for (Ride ride : customer.getFavoriteRoutes()) {
            routes.add(FavoriteRouteItem.builder()
                    .rideId(ride.getId())
                    .checkPoints(ride.getRoute().stationList())
                    .build());
        }
        return routes;
    }

    public void addToFavoriteRoutes(FavoriteRouteRequest dto) {
        Customer customer = customerRepository.findByEmail(dto.getCustomerEmail());
        Optional<Ride> ride = rideRepository.findById(dto.getRideId());
        if (ride.isPresent()) {
            if (!getRideIds(customer.getFavoriteRoutes()).contains(ride.get().getId())) {
                customer.getFavoriteRoutes().add(ride.get());
                customerRepository.save(customer);
            }
        }
    }

    private List<Long> getRideIds(List<Ride> rides) {
        List<Long> ids = new ArrayList<>();
        rides.forEach(ride -> ids.add(ride.getId()));
        return ids;
    }

    public void removeFromFavoriteRoutes(FavoriteRouteRequest dto) {
        Customer customer = customerRepository.findByEmail(dto.getCustomerEmail());
        for (Ride ride : customer.getFavoriteRoutes()) {
            if (ride.getId() == dto.getRideId()) {
                customer.getFavoriteRoutes().remove(ride);
                customerRepository.save(customer);
                return;
            }
        }
    }
}
