package com.example.ubernet.service;

import com.example.ubernet.model.PositionInTime;
import com.example.ubernet.repository.PositionInTimeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PositionInTimeService {

    private final PositionInTimeRepository positionInTimeRepository;

    public PositionInTime save(PositionInTime positionInTime) {
        return positionInTimeRepository.save(positionInTime);
    }
}
