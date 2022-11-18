package com.example.ubernet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@ToString
public class EncodedImage {
    private String data;

    public EncodedImage(String encodedData) {
        this.data = encodedData;
    }
}
