package com.example.ubernet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SetPasswordDTO {

    @NotEmpty
    private String newPassword;
    @NotEmpty
    private String reEnteredNewPassword;
}
