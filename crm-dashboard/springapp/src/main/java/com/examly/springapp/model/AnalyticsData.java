package com.examly.springapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "analytics_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalCustomers;
    private double totalSales;
    private int activeUsers;
    private int closedDeals;

    @Column(name = "report_date")
    private String reportDate;
}
