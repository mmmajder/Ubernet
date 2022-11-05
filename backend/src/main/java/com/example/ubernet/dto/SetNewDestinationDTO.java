package com.example.ubernet.dto;

import com.example.ubernet.model.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SetNewDestinationDTO {
    @NotNull
    private long carId;
    @NotNull
    private Position newDestination;

}
