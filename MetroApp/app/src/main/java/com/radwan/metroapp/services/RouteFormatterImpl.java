package com.radwan.metroapp.services;

import com.radwan.metroapp.repositories.StationRepository;

import java.util.List;

public class RouteFormatterImpl implements RouteFormatter {
    private final StationRepository stationRepository;
    private String initialDirection;

    public RouteFormatterImpl(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public void getInitialDirection(String[] stations) {
        List<String> line1Stations = stationRepository.getLines().get(0).getStations();
        List<String> line2Stations = stationRepository.getLines().get(1).getStations();
        List<String> line3Stations = stationRepository.getLines().get(2).getStations();
        if (line1Stations.contains(stations[0]) && line1Stations.contains(stations[1])) {
            initialDirection = line1Stations.indexOf(stations[1]) > line1Stations.indexOf(stations[0]) ? "New El-Marg" : "Helwan";
        } else if (line2Stations.contains(stations[0]) && line2Stations.contains(stations[1])) {
            initialDirection = line2Stations.indexOf(stations[1]) > line2Stations.indexOf(stations[0]) ? "El-Mounib" : "Shubra El-Kheima";
        } else if (line3Stations.contains(stations[0]) && line3Stations.contains(stations[1])) {
            initialDirection = line3Stations.indexOf(stations[1]) > line3Stations.indexOf(stations[0]) ? "Rod al-Farag Axis" : "Adly Mansour";
        }
        return;
    }

    @Override
    public String getRouteString(String[] stations) {
        StringBuilder routeString = new StringBuilder();
        for (int i = 0; i < stations.length; i++) {
            routeString.append(stations[i]).append(" ");
            // Check if the station is a switch station (Al-Shohadaa) and change the direction
            if (stations[i].equals("Al-Shohadaa")) {
                if (stations[i + 1].equals("Ghamra")) {
                    if (!initialDirection.equals("New El-Marg")){
                        routeString.append("(~Switch in Al-Shohadaa~ to New El-Marg Direction)");
                    }
                    initialDirection = "New El-Marg";
                } else if (stations[i + 1].equals("Massara")) {
                    if (!initialDirection.equals("Shubra El-Kheima")){
                        routeString.append("(~Switch in Al-Shohadaa~ to Shubra El-Kheima Direction)");
                    }
                    initialDirection = "Shubra El-Kheima";
                } else if (stations[i + 1].equals("Attaba")) {
                    if (!initialDirection.equals("El-Mounib")){
                        routeString.append("(~Switch in Al-Shohadaa~ to El-Mounib Direction)");
                    }
                    initialDirection = "El-Mounib";
                } else if (stations[i + 1].equals("Orabi")) {
                    if (!initialDirection.equals("Helwan")){
                        routeString.append("(~Switch in Al-Shohadaa~ to Helwan Direction)");
                    }
                    initialDirection = "Helwan";
                }
            }
            // Check if the station is a switch station (Sadat) and change the direction
            else if (stations[i].equals("Sadat")) {
                if (stations[i + 1].equals("Opera")) {
                    if (!initialDirection.equals("El-Mounib")){
                        routeString.append("(~Switch in Sadat~ to El-Mounib Direction)");
                    }
                    initialDirection = "El-Mounib";
                } else if (stations[i + 1].equals("Mohamed Naguib")) {
                    if (!initialDirection.equals("Shubra El-Kheima")){
                        routeString.append("(~Switch in Sadat~ to Shubra El-Kheima Direction)");
                    }
                    initialDirection = "Shubra El-Kheima";
                } else if (stations[i + 1].equals("Saad Zaghloul")) {
                    if (!initialDirection.equals("Helwan")){
                        routeString.append("(~Switch in Sadat~ to Helwan Direction)");
                    }
                    initialDirection = "Helwan";
                } else if (stations[i + 1].equals("Nasser")) {
                    if (!initialDirection.equals("New El-Marg")){
                        routeString.append("(~Switch in Sadat~ to New El-Marg Direction)");
                    }
                    initialDirection = "New El-Marg";
                }
            }
            // Check if the station is a switch station (Attaba) and change the direction
            else if (stations[i].equals("Attaba")) {
                if (stations[i + 1].equals("Al-Shohadaa")) {
                    if (!initialDirection.equals("Shubra El-Kheima")){
                        routeString.append("(~Switch in Attaba~ to Shubra El-Kheima Direction)");
                    }
                    initialDirection = "Shubra El-Kheima";
                } else if (stations[i + 1].equals("Mohamed Naguib")) {
                    if (!initialDirection.equals("El-Mounib")){
                        routeString.append("(~Switch in Attaba~ to El-Mounib Direction)");
                    }
                    initialDirection = "El-Mounib";
                } else if (stations[i + 1].equals("Bab El Shaaria")) {
                    if (!initialDirection.equals("Adly Mansour")){
                        routeString.append("(~Switch in Attaba~ to Adly Mansour Direction)");
                    }
                    initialDirection = "Adly Mansour";
                } else if (stations[i + 1].equals("Nasser")) {
                    if (!initialDirection.equals("Rod al-Farag Axis")){
                        routeString.append("(~Switch in Attaba~ to Rod al-Farag Axis Direction)");
                    }
                    initialDirection = "Rod al-Farag Axis";
                }
            }
            // Check if the station is a switch station (Nasser) and change the direction
            else if (stations[i].equals("Nasser")) {
                if (stations[i + 1].equals("Maspero")) {
                    if (!initialDirection.equals("Rod al-Farag Axis")){
                        routeString.append("(~Switch in Nasser~ to Rod al-Farag Axis Direction)");
                    }
                    initialDirection = "Rod al-Farag Axis";
                } else if (stations[i + 1].equals("Attaba")) {
                    if (!initialDirection.equals("Adly Mansour")){
                        routeString.append("(~Switch in Nasser~ to Adly Mansour Direction)");
                    }
                    initialDirection = "Adly Mansour";
                } else if (stations[i + 1].equals("Sadat")) {
                    if (!initialDirection.equals("Helwan")){
                        routeString.append("(~Switch in Nasser~ to Helwan Direction)");
                    }
                    initialDirection = "Helwan";
                } else if (stations[i + 1].equals("Orabi")) {
                    if (!initialDirection.equals("New El-Marg")){
                        routeString.append("(~Switch in Nasser~ to New El-Marg Direction)");
                    }
                    initialDirection = "New El-Marg";
                }
            }
            if (i + 1 < stations.length)
                routeString.append(" -> ");
        }
        return routeString.toString();
    
    }
    @Override
    public String getInitialDirection() {
        return initialDirection;
    }
}