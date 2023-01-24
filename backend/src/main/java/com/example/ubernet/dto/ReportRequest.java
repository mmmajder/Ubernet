package com.example.ubernet.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class ReportRequest {
    String email;
    String startDate;
    String endDate;
}
