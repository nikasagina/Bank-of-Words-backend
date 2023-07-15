package com.example.bankofwords.controller;

import com.example.bankofwords.annotation.Secure;
import com.example.bankofwords.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/stats")
@Secure
public class StatisticsController {
    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserStatistics() {
        return ResponseEntity.ok(statisticsService.getUserStatistics());
    }

    @GetMapping("/global")
    public ResponseEntity<?> getGlobalStatistics() {
        return ResponseEntity.ok(statisticsService.getGlobalStatistics());
    }

    @GetMapping("/user/activity")
    public ResponseEntity<?> getUserActivity() {
        return ResponseEntity.ok(statisticsService.getUserActivity());
    }
}