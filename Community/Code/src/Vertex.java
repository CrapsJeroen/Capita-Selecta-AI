import java.util.Collections;
import java.util.List;

public class Vertex {
	private List<Vertex> neighbours;
	private final int id;

	public Vertex(int id, List<Vertex> neighbours) {
		this.id = id;
		this.neighbours = neighbours;
	}

	public List<Vertex> getNeighbours() {
		return neighbours;
	}

	public int getId() {
		return id;
	}
	
	public void setNeighbours(List<Vertex> list){
		this.neighbours = list;
	}
	
	public void addNeighbour(Vertex vertex){
		this.neighbours.add(vertex);
		Collections.sort(this.neighbours, (e1, e2) -> e1.getId() > e2.getId() ? +1 : e1.getId() < e2.getId() ? -1 : 0);
	}
	
	public boolean isConnectedTo(Vertex vertex){
		return this.neighbours.contains(vertex);
	}

	@Override
	public String toString(){
		return ""+id;
	}
}
