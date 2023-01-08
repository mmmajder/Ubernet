package com.example.ubernet.service;

import com.example.ubernet.model.Position;
import com.example.ubernet.repository.PositionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class PositionService {
    private final PositionRepository positionRepository;

    public Position save(Position position) {
        return positionRepository.save(position);
    }

    public void savePositions(List<Position> positions) {
        for (Position position :
                positions) {
            save(position);
        }
    }
}
