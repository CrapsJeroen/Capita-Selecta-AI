package common;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import cliques.Clique;

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
	    if(neighbours.containsKey(vertex)){
	        neighbours.put(vertex, neighbours.get(vertex) + 1);
	    }else{
	           neighbours.put(vertex, 1);
	    }
	}
	
	public boolean isConnectedTo(Vertex vertex){
		return this.neighbours.keySet().contains(vertex);
	}
	
	public void replaceNeighbours(Clique clique){
	    List<Vertex> nbInClique = clique.getNodes().stream().filter(neighbours.keySet()::contains).collect(Collectors.toList());
	    int connections = nbInClique.size();
	    nbInClique.stream().forEach(v -> neighbours.remove(v));
	    neighbours.put(clique, connections);
	    
	}
	
	public Set<Edge> getEdges(){
	    return neighbours.entrySet().stream().map(e -> new Edge(this, e.getKey(), e.getValue())).collect(Collectors.toSet());
	}

	@Override
	public String toString(){
		return ""+id;
	}
}
