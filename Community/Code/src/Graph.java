import java.util.List;
import java.util.stream.Collectors;

public class Graph {
	private List<Vertex> vertices;
	private List<Edge> edges;

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

	public List<Edge> getEdgesFrom(Vertex vertex) {
		return edges.stream().filter(e -> e.getNeighbours().contains(vertex)).collect(Collectors.toList());
	}

	public Vertex getVertexById(int vertexId){
		return vertices.stream().filter(v -> v.getId() == vertexId).collect(Collectors.toList()).get(0);
	}
	
	public Edge getEdgeById(int edgeId) {
		return edges.stream().filter(e -> e.getId() == edgeId).collect(Collectors.toList()).get(0);
	}
	
	public void replaceEdge(Edge edge, Edge newEdge){
		edges.remove(edge);
		edges.add(newEdge);
	}
	
	public void replaceVertex(Vertex old, Vertex newVertex){
		vertices.remove(old);
		vertices.add(newVertex);
	}
	
	@Override
	public String toString() {
		return "Graph:" + "\nVertices: " + vertices.size() + "\nEdges: " + edges.size() + "\n";
	}
}
