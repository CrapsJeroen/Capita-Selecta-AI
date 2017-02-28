import java.util.List;
import java.util.stream.Collectors;

public class Graph {
	private final List<Vertex> vertices;
	private final List<Edge> edges;

	public Graph(List<Vertex> vertices, List<Edge> edges) {
		super();
		this.vertices = vertices;
		this.edges = edges;
	}

	public List<Vertex> getVertices() {
		return vertices;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public List<Edge> getEdges(Vertex vertex) {
		return edges.stream().filter(e -> e.getNeighbours().contains(vertex)).collect(Collectors.toList());
	}

	public Vertex getVertexById(int vertexId){
		return vertices.get(vertexId);
	}
	
	public Edge getEdgeById(int edgeId) {
		return edges.get(edgeId);
	}
	
	@Override
	public String toString() {
		return "Graph:" + "\nVertices: " + vertices.size() + "\nEdges: " + edges.size() + "\n";
	}
}
