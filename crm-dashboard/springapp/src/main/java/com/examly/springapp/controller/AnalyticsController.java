package com.examly.springapp.controller;

import com.examly.springapp.model.AnalyticsData;
import com.examly.springapp.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    // Get all analytics records
    @GetMapping("/all")
    public ResponseEntity<List<AnalyticsData>> getAllAnalytics() {
        return ResponseEntity.ok(analyticsService.getAllAnalytics());
    }

    // Get latest analytics KPIs (for dashboard)
    @GetMapping("/kpis")
    public ResponseEntity<?> getKpis() {
        AnalyticsData latest = analyticsService.getLatestAnalytics();
        if (latest == null) {
            return ResponseEntity.ok("No analytics data found");
        }
        return ResponseEntity.ok(latest);
    }

    // Add new analytics data (used by ANALYST role)
    @PostMapping("/save")
    public ResponseEntity<AnalyticsData> saveKpi(@RequestBody AnalyticsData data) {
        return ResponseEntity.ok(analyticsService.saveAnalytics(data));
    }

    // Delete an analytics record by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAnalytics(@PathVariable Long id) {
        analyticsService.deleteAnalytics(id);
        return ResponseEntity.ok("Analytics record deleted successfully");
    }
}
