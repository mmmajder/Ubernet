package com.example.ubernet.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class EncodedImage {
    private String data;

    public EncodedImage(String encodedData) {
        this.data = encodedData;
    }
}
