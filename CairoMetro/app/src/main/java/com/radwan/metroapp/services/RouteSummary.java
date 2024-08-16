package com.radwan.metroapp.services;

public class RouteSummary {
    public final String startStation;
    public final String endStation;
    public final int totalPaths;
    public final String shortestPath;
    public final int numberOfStations;
    public final String initialDirection;
    public final String routeString;
    public final String expectedTime;
    public final int ticketPrice;

    private RouteSummary(Builder builder) {
        this.startStation = builder.startStation;
        this.endStation = builder.endStation;
        this.totalPaths = builder.totalPaths;
        this.shortestPath = builder.shortestPath;
        this.numberOfStations = builder.numberOfStations;
        this.initialDirection = builder.initialDirection;
        this.routeString = builder.routeString;
        this.expectedTime = builder.expectedTime;
        this.ticketPrice = builder.ticketPrice;
    }

    public static class Builder {
        private String startStation;
        private String endStation;
        private int totalPaths;
        private String shortestPath;
        private int numberOfStations;
        private String initialDirection;
        private String routeString;
        private String expectedTime;
        private int ticketPrice;

        public Builder setStartStation(String startStation) {
            this.startStation = startStation;
            return this;
        }

        public Builder setEndStation(String endStation) {
            this.endStation = endStation;
            return this;
        }

        public Builder setTotalPaths(int totalPaths) {
            this.totalPaths = totalPaths;
            return this;
        }

        public Builder setShortestPath(String shortestPath) {
            this.shortestPath = shortestPath;
            return this;
        }
        
        public Builder setNumberOfStations(int numberOfStations) {
            this.numberOfStations = numberOfStations;
            return this;
        }

        public Builder setInitialDirection(String initialDirection) {
            this.initialDirection = initialDirection;
            return this;
        }

        public Builder setRouteString(String routeString) {
            this.routeString = routeString;
            return this;
        }

        public Builder setExpectedTime(String expectedTime) {
            this.expectedTime = expectedTime;
            return this;
        }

        public Builder setTicketPrice(int ticketPrice) {
            this.ticketPrice = ticketPrice;
            return this;
        }

        public RouteSummary build() {
            return new RouteSummary(this);
        }
    }

    @Override
    public String toString() {
        return "RouteSummary:" +
                "\n\nStart Station = " + startStation +
                "\n\nEnd Station = " + endStation +
                "\n\nTotal Paths = " + totalPaths +
                "\n\nNumber Of Stations = " + numberOfStations +
                "\n\nShortest Route = " + routeString +
                "\n\nDirection = " + initialDirection +
                expectedTime +
                "\n\nTicket Price = " + ticketPrice;
    }
}
