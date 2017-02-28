import java.util.List;

public class Edge {
	private final List<Vertex> neighbours;
	private final int id;

	public Edge(int id, List<Vertex> neighbours) {
		this.id = id;
		this.neighbours = neighbours;
	}

	public List<Vertex> getNeighbours() {
		return neighbours;
	}

	public int getId() {
		return id;
	}
	
	public Vertex connectsTo(Vertex vertex){
		if(neighbours.get(0).equals(vertex)){
			return neighbours.get(1);
		} else if(neighbours.get(1).equals(vertex)){
			return neighbours.get(0);
		} else {
			return null;
		}
	}

	@Override
	public String toString(){
		return ""+id+"="+neighbours.toString();
	}
}
