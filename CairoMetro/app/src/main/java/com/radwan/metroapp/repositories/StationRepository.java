package com.radwan.metroapp.repositories;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.radwan.metroapp.models.Graph;
import com.radwan.metroapp.models.Line;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StationRepository {
    private List<Line> lines;
    private List<String> totalStations;
    private Graph graph;

    public StationRepository() {
        initializeStations();
        buildGraph();
    }

    private void initializeStations() {
        // Initialize lines and stations
        Line line1 = new Line("Line 1", List.of("Helwan", "Ain Helwan", "Helwan University",
            "Wadi Hof",  "Hadayek Helwan","El-Maasara", "Tora El-Asmant", "Kolet El-Maadi",
            "Tora El-Balad", "Sakanat El-Maadi", "Maadi", "Hadayek El-Maadi", "Dar El-Salam",
            "Zahraa El-Maadi", "Mar Girgis", "El-Malek El-Saleh", "Sayeda Zeinab", "Saad Zaghloul",
            "Sadat", "Nasser", "Orabi", "Al-Shohadaa", "Ghamra", "El-Demerdash", "Manshiet El-Sadr",
            "Kobri El-Qobba", "Hammamat El-Qobba", "Saray El-Qobba", "Hadayek El-Zaitoun",
            "Helmeyet El-Zaitoun", "El-Matareyya", "Ain Shams", "Ezbet El-Nakhl", "El-Marg",
            "New El-Marg"
        ));
        Line line2 = new Line("Line 2", List.of("Shubra El-Kheima", "Kolleyet El-Zeraa",
            "El-Mazallat", "El-Khalafawi", "Saint Teresa", "Rod El-Farag", "Massara", "Al-Shohadaa",
            "Attaba", "Mohamed Naguib", "Sadat", "Opera", "Dokki", "El Bohoth", "Cairo University",
            "Faisal", "Giza", "Omm El-Misryeen", "Sakiat Mekki", "El-Mounib"
        ));
        Line line3 = new Line("Line 3", List.of("Adly Mansour", "El Haykestep", "Omar Ibn El Khattab",
            "Qobaa", "Hesham Barakat", "El Nozha", "Nadi El Shams", "Alf Maskan", "Heliopolis", "Haroun",
            "Al Ahram", "Koleyet El Banat", "Stadium", "El Maarad", "Abbassia", "Abdou Pasha", "El Geish",
            "Bab El Shaaria", "Attaba", "Nasser", "Maspero", "Safaa Hegazy", "Kit Kat", "Sudan", "Imbaba",
            "El Bohy","Al Qawmia" , "Ring Road", "Rod al-Farag Axis"
        ));
        Line line4 = new Line("Line 4", List.of("Kit Kat", "Tawfiqia", "Wadi Nile",
            "Gamaet El Dewal","Bolaa Dakrour","Cairo University"
        ));

        lines = List.of(line1, line2, line3, line4);


        // Initialize stations
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            totalStations = lines.stream()
                .map(Line::getStations)
                .flatMap(List::stream)
                .collect(Collectors.toList());
            totalStations = totalStations.stream().distinct().sorted().collect(Collectors.toList());
        }

    }

    private void buildGraph() {
        // Build graph from stations
        graph = new Graph();
        for (Line line : lines) {
            List<String> stations = line.getStations();
            graph.addVertex(stations.get(0));
            for (int i = 1; i < stations.size() - 1; i++) {
                String startStation = stations.get(i - 1);
                String endStation = stations.get(i);
                graph.addVertex(endStation);
                graph.addEdge(graph.getVertex(startStation), graph.getVertex(endStation));
            }
        }
    }

    public Graph getGraph() {
        return graph;
    }

    public List<String> getTotalStations() {
        return totalStations;
    }

    public List<Line> getLines() {
        return lines;
    }
}