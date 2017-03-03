import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
	
	public void writeOut(String path) throws IOException{
		String result = "";
		//Adding the amount of vertices
		result += this.vertices.size()+"\n";
		//Adding the edges
		for(Edge edge : this.edges)
			result += edge.toString()+"\n";
		
		Files.write(Paths.get(path), result.getBytes());
	}
	
	@Override
	public String toString() {
		return "Graph:" + "\nVertices: " + vertices.size() + "\nEdges: " + edges.size() + "\n";
	}
}
