package com.radwan.metroapp.services;

import java.util.List;

public class RouteAnalyzer {
    private final RouteFormatter routeFormatter;
    private final TimeCalculator timeCalculator;
    private final PriceCalculator priceCalculator;

    public RouteAnalyzer(RouteFormatter routeFormatter,
                        TimeCalculator timeCalculator,
                        PriceCalculator priceCalculator) {
        this.routeFormatter = routeFormatter;
        this.timeCalculator = timeCalculator;
        this.priceCalculator = priceCalculator;
    }

    public RouteSummary analyzePaths(List<List<String>> paths, String startStation, String endStation) {
        String shortestPath = analyzePathLengths(paths);
        String[] shortestPathStations = shortestPath.split(", ");
        routeFormatter.getInitialDirection(shortestPathStations);

        return new RouteSummary.Builder()
            .setStartStation(startStation)
            .setEndStation(endStation)
            .setTotalPaths(paths.size())
            .setShortestPath(shortestPath)
            .setNumberOfStations(shortestPathStations.length)
            .setRouteString(routeFormatter.getRouteString(shortestPathStations))
            .setInitialDirection(routeFormatter.getInitialDirection())
            .setExpectedTime(timeCalculator.calculateTime(shortestPathStations.length))
            .setTicketPrice(priceCalculator.calculatePrice(shortestPathStations.length))
            .build();
    }

    private String analyzePathLengths(List<List<String>> paths) {
        String shortestPath = "";
        int shortestPathLength = Integer.MAX_VALUE;

        for (int i = 0; i < paths.size(); i++) {
            List<String> path = paths.get(i);
            int pathLength = path.size();
            if (pathLength < shortestPathLength) {
                shortestPath = String.join(", ", path);
                shortestPathLength = pathLength;
            }
        }

        return shortestPath;
    }

    private String generateSpeechText(RouteSummary summary) {
        StringBuilder speech = new StringBuilder();
        speech.append("Your route from ").append(summary.startStation)
              .append(" to ").append(summary.endStation)
              .append(" has ").append(summary.numberOfStations)
              .append(" stations. The expected time is ").append(summary.expectedTime)
              .append(". The ticket price is ").append(summary.ticketPrice)
              .append(" Egyptian pounds.");
        return speech.toString();
    }
}
