package com.radwan.metroapp.services;

import com.radwan.metroapp.repositories.StationRepository;

import java.util.List;

public class RouteService {
    private StationRepository stationRepository;
    private RouteSummary summary;
    public RouteService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public String getRouteSummary(String startStation, String endStation) {
        
        List<List<String>> paths = stationRepository.getGraph().getAllPaths(startStation, endStation);
        
        // Process paths and create summary
        RouteAnalyzer analyzer = new RouteAnalyzer(
        new RouteFormatterImpl(stationRepository),
        new TimeCalculatorImpl(),
        new PriceCalculatorImpl()
        );

        summary = analyzer.analyzePaths(paths, startStation, endStation);
        return summary.toString();
    }

}