package com.radwan.metroapp.models;

import java.util.List;

public class Line {
    private String name;
    private List<String> stations;

    public Line(String name, List<String> stations) {
        this.name = name;
        this.stations = stations;
    }

    // Getters and setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<String> getStations() {
        return stations;
    }
    public void setStations(List<String> stations) {
        this.stations = stations;
    }
}