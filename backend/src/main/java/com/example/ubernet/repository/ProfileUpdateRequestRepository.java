package com.example.ubernet.repository;

import com.example.ubernet.model.ProfileUpdateRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileUpdateRequestRepository extends JpaRepository<ProfileUpdateRequest, Long> {
    ProfileUpdateRequest save(ProfileUpdateRequest profileUpdateRequest);
}
