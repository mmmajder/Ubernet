package com.example.ubernet.service;

import com.example.ubernet.model.Admin;
import com.example.ubernet.model.User;
import com.example.ubernet.repository.AdminRepository;
import com.example.ubernet.utils.EntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public Admin save(User user) {
        return adminRepository.save(EntityMapper.mapToAdmin(user));
    }
}
