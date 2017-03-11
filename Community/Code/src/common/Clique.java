package common;


import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Clique extends Vertex {

    private final List<Vertex> nodes;

    public Clique(int id, List<Vertex> nodes) {
        super(id, new HashSet<Vertex>());
        nodes.stream().flatMap(v -> v.getNeighbours().stream())
                .filter(v -> !nodes.contains(v)).forEach(v -> addNeighbour(v));
        this.nodes = nodes;
    }

    public List<Vertex> getNodes() {
        return this.nodes;
    }

    @Override
    public int degree() {
        int external = super.degree();
        
        // Each node is connected to every other node
        int internal = nodes.size() * (nodes.size() - 1);
        
        return internal + external;
    }

    @Override
    public int amountOfConnectionsTo(Set<Vertex> vertices) {
        int external = super.amountOfConnectionsTo(vertices);
        int internal = 0;
        
        // The internal connections only count if the clique is in the set we need to count connections to
        if(vertices.contains(this)){
            
            // Each node is connected to every other node, don't count edges twice.
            internal = nodes.size() * (nodes.size() - 1) / 2;
        }
        return internal + external;
    }
    
    public Set<Edge> getInternalEdges(){
        Set<Edge> result = super.getInternalEdges();
        for(int i = 0; i < nodes.size(); i++){
            for(int j = i + 1; j < nodes.size(); j++){
                Vertex node1 = nodes.get(i);
                Vertex node2 = nodes.get(j);
                result.add(new Edge(node1, node2, node1.getConnectionWeight(node2)));
            }
        }
        return result;
    }

}
