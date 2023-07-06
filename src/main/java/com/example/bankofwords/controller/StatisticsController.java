package com.example.bankofwords.controller;

import com.example.bankofwords.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/stats")
public class StatisticsController {
    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserStatistics(@RequestHeader("Authorization") String authHeader) {
        return statisticsService.getUserStatistics(authHeader);
    }

    @GetMapping("/global")
    public ResponseEntity<?> getGlobalStatistics(@RequestHeader("Authorization") String authHeader) {
        return statisticsService.getGlobalStatistics(authHeader);
    }

    @GetMapping("/user/activity")
    public ResponseEntity<?> getUserActivity(@RequestHeader("Authorization") String authHeader) {
        return statisticsService.getUserActivity(authHeader);
    }
}