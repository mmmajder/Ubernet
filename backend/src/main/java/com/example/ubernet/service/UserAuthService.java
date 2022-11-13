package com.example.ubernet.service;

import com.example.ubernet.model.UserAuth;
import com.example.ubernet.repository.UserAuthRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserAuthService {
    private final UserAuthRepository userAuthRepository;

    public UserAuth save(UserAuth userAuth){
        return userAuthRepository.save(userAuth);
    }
}
