import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

public class Graph {
	private List<Vertex> vertices;
	private HashMap<Integer, Vertex> verticesMap;
	private Set<Edge> edges;

	public Graph(List<Vertex> vertices) {
		super();
		this.vertices = vertices;
		verticesMap = new HashMap<Integer, Vertex>();
		edges = new HashSet<Edge>();
		vertices.stream().forEach(v -> verticesMap.put(v.getId(), v));
		vertices.stream().forEach(v -> edges.addAll(v.getEdges()));
	}

	public List<Vertex> getVertices() {
		return vertices;
	}
	
   public Map<Integer, Vertex> getVerticesMap() {
        return verticesMap;
    }


	public Set<Edge> getEdges() {
		return edges;
	}

	public List<Edge> getEdgesFrom(Vertex vertex) {
		return edges.stream().filter(e -> e.getNeighbours().contains(vertex)).collect(Collectors.toList());
	}

	public Vertex getVertexById(int vertexId){
		return vertices.stream().filter(v -> v.getId() == vertexId).collect(Collectors.toList()).get(0);
	}
	
	public void replaceVertex(Vertex old, Vertex newVertex){
		vertices.remove(old);
		vertices.add(newVertex);
	}
	
	public void writeOut(String path) throws IOException{
		StringBuffer result = new StringBuffer();
		//Adding the amount of vertices
		result.append(this.vertices.size()+"\n");
		//Adding the edges
		for(Edge edge : this.edges)
			result.append(edge.toString()+"\n");
		
		Files.write(Paths.get(path), result.toString().getBytes());
	}
	
	@Override
	public String toString() {
		return "Graph:" + "\nVertices: " + vertices.size() + "\nEdges: " + edges.size() + "\n";
	}
}
