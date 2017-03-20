package common;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Graph {

    private List<Vertex> vertices;

    // Maps a vertex Id to the Vertex
    private HashMap<Integer, Vertex> verticesMap;

    // Maps vertex Id to its index in the vertices list
    private HashMap<Integer, Integer> indexMap;

    private Set<Edge> edges;
    
    private HashMap<Vertex, Set<Edge>> edgeMap;

    public Graph(List<Vertex> vertices) {
        super();
        this.vertices = vertices;
        verticesMap = new HashMap<Integer, Vertex>();
        indexMap = new HashMap<Integer, Integer>();
        edges = new HashSet<Edge>();
        edgeMap = new HashMap<Vertex, Set<Edge>>();
        vertices.stream().forEach(v -> verticesMap.put(v.getId(), v));
        IntStream.range(0, vertices.size()).forEach(
                i -> indexMap.put(vertices.get(i).getId(), i));
        vertices.stream().forEach(v -> edges.addAll(v.getEdges()));
        vertices.stream().forEach(v -> edgeMap.put(v, v.getEdges()));

    }

    public List<Vertex> getVertices() {
        return new ArrayList<Vertex>(vertices);
    }

    public Map<Integer, Vertex> getVerticesMap() {
        return new HashMap<Integer, Vertex>(verticesMap);
    }

    public Map<Integer, Integer> getIndexMap() {
        return new HashMap<Integer, Integer>(indexMap);
    }

    public Set<Edge> getEdges() {
        return new HashSet<Edge>(edges);
    }

    public Set<Edge> getEdgesFrom(Vertex vertex) {
        return edgeMap.get(vertex);
    }

    public Vertex getVertexById(int vertexId) {
        return verticesMap.get(vertexId);
    }

    public Integer getVertexIndex(Vertex vertex) {
        return getVertexIndexById(vertex.getId());
    }

    public Integer getVertexIndexById(int vertexId) {
        return indexMap.get(vertexId);
    }

    public void replaceVertex(Vertex old, Vertex newVertex) {
        vertices.remove(old);
        vertices.add(newVertex);
    }

    public void writeOut(String path) throws IOException {
        StringBuffer result = new StringBuffer();
        // Adding the amount of vertices
        result.append(this.vertices.size() + "\n");
        // Adding the edges
        for (Edge edge : this.edges)
            result.append(edge.toString() + "\n");

        Files.write(Paths.get(path), result.toString().getBytes());
    }
    
    public Set<Integer> getNeighborsIndexByIndex(int index){
        return getVertices().get(index).getNeighboursSet().stream()
                .map(v -> getVertexIndexById(v.getId()))
                .collect(Collectors.toSet());
    }
    
    public int amountOfInternalConnections(Set<Vertex> vertices){
        Set<Edge> intEdges = new HashSet<Edge>();
        
        for(Vertex v : vertices){
            for(Edge e : getEdgesFrom(v)){
                if(!vertices.contains(e.connectsTo(v))) continue;
                intEdges.add(e);
            }
            
            for(Edge e : v.getInternalEdges()){
                intEdges.add(e);
            }
        }
        
//        Set<Edge> intEdges = vertices.stream().flatMap(v -> getEdgesFrom(v).stream()
//                                    .filter(e -> vertices.contains(e.connectsTo(v)))
//                                )
//                                .collect(Collectors.toSet());
        
//        intEdges.addAll(vertices.stream().flatMap(v -> v.getInternalEdges().stream()).collect(Collectors.toSet()));
        
        return intEdges.stream().map(e -> e.getWeight()).reduce(0, (count, current) -> count + current);
    }

    @Override
    public String toString() {
        return "Graph:" + "\nVertices: " + vertices.size() + "\nEdges: "
                + edges.size() + "\n";
    }
}
