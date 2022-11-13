package com.example.ubernet.dto;

import com.example.ubernet.model.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuturePositionsDTO {
    private List<Position> positions;
    private long carId;
}
