package com.example.ubernet.service;

import com.example.ubernet.model.Position;
import com.example.ubernet.repository.PositionRepository;
import org.springframework.stereotype.Service;

@Service
public class PositionService {

    private final PositionRepository positionRepository;


    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public Position save(Position position) {
        return positionRepository.save(position);
    }
}
