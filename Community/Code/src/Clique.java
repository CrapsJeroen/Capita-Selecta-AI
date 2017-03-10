import java.util.*;
import java.util.stream.*;

public class Clique extends Vertex {

	private final List<Vertex> nodes;
	
	public Clique(int id, List<Vertex> nodes) {
		super(id, new HashSet<Vertex>());
		nodes.stream().flatMap(v -> v.getNeighbours().stream()).filter(v -> !nodes.contains(v)).forEach(v -> addNeighbour(v));
		this.nodes = nodes;
	}
	
	public List<Vertex> getNodes(){
		return this.nodes;
	}

}
