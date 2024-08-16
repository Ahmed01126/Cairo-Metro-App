
package com.radwan.metroapp.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Graph {
    private List<Vertex> vertices;
    private List<List<String>> allPaths;

    public Graph() {
        this.vertices = new ArrayList<Vertex>();
        this.allPaths = new ArrayList<List<String>>();
    }

    public Vertex addVertex(String data) {
        Vertex newVertex = new Vertex(data);
        this.vertices.add(newVertex);
        return newVertex;
    }

    public Vertex getVertex(String data) {
        for (Vertex v : this.vertices) {
            if (v.getData().equalsIgnoreCase(data)) {
                return v;
            }
        }
        return null;
    }


    public void addEdge(Vertex vertex1, Vertex vertex2) {
        vertex1.addEdge(vertex2);
        vertex2.addEdge(vertex1);
    }
    
    private void depthFirstTraversal(Vertex start, Vertex end, String path) {
        start.setVisited(true);
        path += start.getData() + ", ";
        if (start.getData().equalsIgnoreCase(end.getData())) {
            allPaths.add(Arrays.asList(path.split(", ")));
        } else {
            for (Edge e : start.getEdges()) {
                Vertex neighbor = e.getEnd();
                if (!neighbor.isVisited()) {
                    depthFirstTraversal(neighbor, end, path);
                }
            }
        }
        start.setVisited(false);
    }

    public List<List<String>> getAllPaths(String start, String end) {
        allPaths.clear();
        Vertex startVertex = getVertex(start);
        Vertex endVertex = getVertex(end);
        depthFirstTraversal(startVertex, endVertex, "");
        return allPaths;
    }

}

