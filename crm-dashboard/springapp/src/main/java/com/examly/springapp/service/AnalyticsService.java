package com.examly.springapp.service;

import com.examly.springapp.model.AnalyticsData;
import com.examly.springapp.repository.AnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnalyticsService {

    @Autowired
    private AnalyticsRepository analyticsRepository;

    public List<AnalyticsData> getAllAnalytics() {
        return analyticsRepository.findAll();
    }

    public AnalyticsData getLatestAnalytics() {
        List<AnalyticsData> data = analyticsRepository.findAll();
        if (data.isEmpty()) {
            // Initialize with sample data if empty
            AnalyticsData sampleData = AnalyticsData.builder()
                .totalCustomers(150)
                .totalSales(250000.0)
                .activeUsers(85)
                .closedDeals(42)
                .reportDate("2024-10-15")
                .build();
            return analyticsRepository.save(sampleData);
        }
        return data.get(data.size() - 1);
    }

    public AnalyticsData saveAnalytics(AnalyticsData data) {
        return analyticsRepository.save(data);
    }

    public void deleteAnalytics(Long id) {
        analyticsRepository.deleteById(id);
    }
}
