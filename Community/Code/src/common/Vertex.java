package common;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Vertex {
	private Map<Vertex, Integer> neighbours;
	private final int id;

	public Vertex(int id, Set<Vertex> neighbours) {
		this.id = id;
		this.neighbours = neighbours.stream().collect(Collectors.toMap(Function.identity(), n -> 1));
	}

	public List<Vertex> getNeighbours() {
		return new ArrayList<Vertex>(neighbours.keySet());
	}
	
	   public Set<Vertex> getNeighboursSet() {
	        return neighbours.keySet();
	    }

	public int getId() {
		return id;
	}
	
	
	public void addNeighbour(Vertex vertex){
	    addNeighbour(vertex, 1);
	}
	
	   public void addNeighbour(Vertex vertex, int amount){
	        if(neighbours.containsKey(vertex)){
	            neighbours.put(vertex, neighbours.get(vertex) + amount);
	        }else{
	               neighbours.put(vertex, amount);
	        }
	    }
	
	public boolean isConnectedTo(Vertex vertex){
		return this.neighbours.keySet().contains(vertex);
	}
	
	public void replaceNeighbours(Clique clique){
	    List<Vertex> nbInClique = clique.getNodes().stream().filter(neighbours.keySet()::contains).collect(Collectors.toList());
	    int connections = amountOfConnectionsTo(nbInClique);
	    nbInClique.stream().forEach(v -> neighbours.remove(v));
	    neighbours.put(clique, connections);
	    
	}
	
	public Set<Edge> getEdges(){
	    return neighbours.entrySet().stream().map(e -> new Edge(this, e.getKey(), e.getValue())).collect(Collectors.toSet());
	}
	
   public Set<Edge> getInternalEdges(){
        return new HashSet<Edge>();
    }
	
	public int degree(){
	    return neighbours.values().stream().reduce(0, (count, current) -> count + current);
	}
	
	public int amountOfConnectionsTo(Collection<Vertex> vertices){
	    return neighbours.entrySet().stream()
	            .filter(entry -> vertices.contains(entry.getKey()))
	            .map(entry -> entry.getValue())
	            .reduce(0, (count, current) -> count + current);
	}
	
	public int getConnectionWeight(Vertex other){
	    if(neighbours.containsKey(other)){
	        return neighbours.get(other);
	    }
	    return 0;
	}

	@Override
	public String toString(){
		return ""+id;
	}
}
