package com.example.ubernet.controller;

import com.example.ubernet.dto.ReportRequest;
import com.example.ubernet.dto.ReportResponse;
import com.example.ubernet.service.ReportsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/reports", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportsController {

    private final ReportsService reportsService;

    @PostMapping("/getAdminReport")
    public ResponseEntity<ReportResponse> getAdminReport(@RequestBody ReportRequest reportRequest) {
        ReportResponse reportResponse = reportsService.getAdminReport(reportRequest);
        return new ResponseEntity<>(reportResponse, HttpStatus.OK);
    }

    @PostMapping("/getCustomerReport")
    public ResponseEntity<ReportResponse> getCustomerReport(@RequestBody ReportRequest reportRequest) {
        ReportResponse reviewResponse = reportsService.getCustomerReport(reportRequest);
        return new ResponseEntity<>(reviewResponse, HttpStatus.OK);
    }

    @PostMapping("/getDriverReport")
    public ResponseEntity<ReportResponse> getDriverReport(@RequestBody ReportRequest reportRequest) {
        ReportResponse reviewResponse = reportsService.getDriverReport(reportRequest);
        return new ResponseEntity<>(reviewResponse, HttpStatus.OK);
    }
}
