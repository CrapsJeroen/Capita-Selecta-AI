import java.util.*;
import java.util.stream.*;

public class Clique extends Vertex {

	private final List<Vertex> nodes;
	private final Map<Vertex,Integer> weights;
	
	public Clique(int id, List<Vertex> neighbours, List<Vertex> nodes) {
		super(id, neighbours);
		super.setNeighbours(neighbours.stream().filter(n -> !nodes.contains(n)).distinct().collect(Collectors.toList()));
		this.nodes = nodes;
		Map<Vertex,Integer> result = neighbours.stream().filter(n -> !nodes.contains(n)).distinct()
				.collect(Collectors.toMap(no -> no, no -> (neighbours.stream().filter(n -> n.equals(no)).collect(Collectors.toList()).size())));
		this.weights = result;
	}
	
	public List<Vertex> getNodes(){
		return this.nodes;
	}
	
	public Map<Vertex,Integer> getWeights(){
		return this.weights;
	}
	
	public int weightConnection(Vertex vertex){
		return weights.get(vertex);
	}

}
