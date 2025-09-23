package com.examly.springapp.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    // ✅ GET KPIs
    @GetMapping("/kpis")
    public Map<String, Object> getKpis() {
        Map<String, Object> kpis = new HashMap<>();
        kpis.put("totalCustomers", 120);
        kpis.put("totalSales", 540000);
        kpis.put("activeUsers", 15);
        kpis.put("closedDeals", 48);
        return kpis;
    }
}
