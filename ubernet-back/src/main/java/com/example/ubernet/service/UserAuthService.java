package com.example.ubernet.service;

import com.example.ubernet.model.UserAuth;
import com.example.ubernet.repository.UserAuthRepository;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService {
    private final UserAuthRepository userAuthRepository;

    public UserAuthService(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }
    public UserAuth save(UserAuth userAuth){
        return userAuthRepository.save(userAuth);
    }
}
