import java.util.*;
import java.util.stream.Collectors;

public class Algorithm {

	public Algorithm(Graph graph){
		this.graph = graph;
		this.vertices = graph.getVertices();
		this.edges = graph.getEdges();
		this.cliques = new ArrayList<List<Vertex>>();
	}

	private Graph graph;
	private List<Vertex> vertices;
	private List<Edge> edges;

	public Graph findCliques(int amountOfSeeds){
		Random rand = new Random();
		int vertexId = vertices.size();
		int edgeId = edges.size();
		for(int i = 0; i < amountOfSeeds; i++){

			Vertex seed = vertices.get(rand.nextInt(vertices.size()));
			List<Vertex> start = new ArrayList<Vertex>();
			start.add(seed);
			search(start);

			// Largest element first
			Collections.sort(cliques, (c1,c2) -> c1.size() < c2.size() ? +1 : c1.size() > c2.size() ? -1 : 0); 
			// Retrieve the largest
			List<Vertex> largestClique = cliques.get(0);
			
			// Remove vertices from the clique
			vertices = vertices.stream().filter(v -> !largestClique.contains(v)).collect(Collectors.toList());
			List<Vertex> neighbours = new ArrayList<>();
			// Create a list of all vertices that were connected to the clique
			for(Vertex vertex : largestClique){
				neighbours.addAll(vertex.getNeighbours());
			}
			// Create new clique and add to the list
			Vertex clique = new Clique(vertexId++,neighbours,largestClique);
			vertices.add(clique);

			// Required lists
			List<Edge> adapt = new ArrayList<>();
			List<Vertex> handled = new ArrayList<>();
			List<Edge> addNew = new ArrayList<>();
			// Get affected edges
			List<Edge> affectedEdges = new ArrayList<>();
			for(Vertex vertex : largestClique){
				affectedEdges.addAll(graph.getEdgesFrom(vertex));
			}
			// Remove all edges that are completely in the clique
			affectedEdges = affectedEdges.stream().filter(e -> !largestClique.containsAll(e.getNeighbours())).collect(Collectors.toList());
			for(Edge edge: affectedEdges){
				List<Vertex> intersect = edge.getNeighbours().stream().filter(largestClique::contains).collect(Collectors.toList());
				// Check if there is overlap
				if(intersect.size() > 0){
					// Add to edge to be removed
					adapt.add(edge);
					Vertex tmp = intersect.get(0);
					if(!handled.contains(tmp)){
						// Add the vertex to the list of updated vertices
						handled.add(tmp);
						// Create a new pair between this and the clique
						List<Vertex> pair = new ArrayList<>(2);
						pair.add(tmp);
						pair.add(clique);
						// Remove elements in the clique from old neighbours
						List<Vertex> newNeighbours = tmp.getNeighbours().stream().filter(v -> !largestClique.contains(v)).collect(Collectors.toList());
						// Add clique as new neighbour
						newNeighbours.add(clique);
						// Set the new list
						tmp.setNeighbours(newNeighbours);
						// Create a new edge for the clique and the vertex that was connected to a part of the clique earlier
						addNew.add(new Edge(edgeId++,pair));
					}
				}
			}
			// Remove previous edges
			edges.removeAll(adapt);
			// Add newly created edges
			edges.addAll(addNew);
			cliques = new ArrayList<>();
		}

		return null;
	}

	private List<List<Vertex>> cliques;

	public void search(List<Vertex> vertices){
		for(Vertex vertex: vertices.get(vertices.size()-1).getNeighbours()){
			if(vertex.getNeighbours().containsAll(vertices)){
				vertices.add(vertex);
				search(vertices);
			} else if(vertices.size() >= 3) {
				cliques.add(vertices);
			}
		}
	}
}
