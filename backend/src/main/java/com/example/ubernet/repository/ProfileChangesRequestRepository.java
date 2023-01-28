package com.example.ubernet.repository;

import com.example.ubernet.model.ProfileChangesRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileChangesRequestRepository extends JpaRepository<ProfileChangesRequest, Long> {
    ProfileChangesRequest findByDriverEmail(String driverEmail);
    List<ProfileChangesRequest> findByProcessedFalse();
}