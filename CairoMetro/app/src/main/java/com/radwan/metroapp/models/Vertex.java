package com.radwan.metroapp.models;

import java.util.ArrayList;

public class Vertex {
    private String data;
    private ArrayList<Edge> edges;
    private boolean visited;

    // Getters and Setters
    public void setData(String data) {
        this.data = data;
    }
    public String getData() {
        return this.data;
    }
    
    public ArrayList<Edge> getEdges() {
        return this.edges;
    }

    // Constructors
    public Vertex() {
        this.edges = new ArrayList<Edge>();
    }
    public Vertex(String inputData) {
        this.data = inputData;
        this.edges = new ArrayList<Edge>();
    }
    
    public void addEdge(Vertex endVertex) {
        this.edges.add(new Edge(this, endVertex));
    }


    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
