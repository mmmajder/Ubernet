package com.example.ubernet.service;

import com.example.ubernet.dto.UserEditDTO;
import com.example.ubernet.model.ProfileUpdateRequest;
import com.example.ubernet.model.User;
import com.example.ubernet.repository.ProfileUpdateRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ProfileUpdateRequestService {
    private final ProfileUpdateRequestRepository profileUpdateRequestRepository;
    private final UserService userService;

    public ProfileUpdateRequest save(ProfileUpdateRequest profileUpdateRequest) {
        return profileUpdateRequestRepository.save(profileUpdateRequest);
    }

    public boolean manageProfileUpdateRequest(Long id, boolean isAccepted) {
        Optional<ProfileUpdateRequest> profileUpdateRequestOptional = profileUpdateRequestRepository.findById(id);
        if (profileUpdateRequestOptional.isEmpty()) {
            return false;
        }
        ProfileUpdateRequest profileUpdateRequest = profileUpdateRequestOptional.get();
        if (profileUpdateRequest.isProcessed()) {
            return false;
        }
        if (isAccepted) {
            updateDriver(profileUpdateRequest);
        }
        profileUpdateRequest.setProcessed(true);
        save(profileUpdateRequest);
        return true;
    }

    private void updateDriver(ProfileUpdateRequest profileUpdateRequest) {
        User user = profileUpdateRequest.getUser();
        UserEditDTO userEditDTO = new UserEditDTO();
        userEditDTO.setPhoneNumber(profileUpdateRequest.getPhoneNumber());
        userEditDTO.setName(profileUpdateRequest.getName());
        userEditDTO.setSurname(profileUpdateRequest.getSurname());
        userEditDTO.setCity(profileUpdateRequest.getCity());
        userService.updateUser(user, userEditDTO);
    }
}
