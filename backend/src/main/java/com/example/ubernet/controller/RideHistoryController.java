package com.example.ubernet.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/ridesHistory", produces = MediaType.APPLICATION_JSON_VALUE)
public class RideHistoryController {
}
