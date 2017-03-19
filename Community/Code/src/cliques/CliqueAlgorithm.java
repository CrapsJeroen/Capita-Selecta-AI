package cliques;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import common.Clique;
import common.Graph;
import common.Vertex;

public class CliqueAlgorithm {
    
    private List<Vertex> vertices;
    private List<List<Vertex>> cliques;
    private Graph graph;

	public CliqueAlgorithm(Graph graph){
		this.vertices = graph.getVertices();
		this.cliques = new ArrayList<List<Vertex>>();
		this.graph = graph;
	}


	public Graph findCliques(int amountOfCliques, int maxAttemptsWithoutClique){
		Random rand = new Random();
		int vertexId = vertices.size();
		int i = 0;
		int failedAttempts = 0;
		while(i < amountOfCliques && failedAttempts < maxAttemptsWithoutClique){
			cliques = new ArrayList<>();
			Vertex seed = vertices.get(rand.nextInt(vertices.size()));
			if(seed.getClass().equals(Clique.class)){
			    failedAttempts++;
				continue;
			}
			
			
			search(seed);
			if(cliques.size() == 0){
	             failedAttempts++;
				 continue;
			}
//	         System.out.println("Progress: " + i);
			// Largest element first
			Collections.sort(cliques, (c1,c2) -> c1.size() < c2.size() ? +1 : c1.size() > c2.size() ? -1 : 0);
			
			// Retrieve the largest
			List<Vertex> largestClique = cliques.get(0);
			
			Set<Vertex> neighbours = new HashSet<>();
			// Create a list of all vertices that were connected to the clique
			for(Vertex vertex : largestClique){
				neighbours.addAll(vertex.getNeighbours());
			}
			
	        // Remove vertices from the clique from neighbours
			neighbours.removeAll(largestClique);
//			System.out.println("Found neighbours: " + neighbours.size());
		
			// Create new clique and add to the list
			Clique clique = new Clique(vertexId++,largestClique);
			vertices.add(clique);
			
			// Remove vertices from the clique
			vertices.removeAll(largestClique);
			
			// Replace references to nodes in clique to the clique itself
			neighbours.stream().forEach(n -> n.replaceNeighbours(clique));
			
			failedAttempts = 0;
			i++;
		}
		
		return new Graph(this.vertices);
	}

    public void search(Vertex vertex){
        vertex.getNeighbours().stream().forEach(v -> searchSingle(v, vertex));
//        for(Vertex vertex: vertices.get(vertices.size()-1).getNeighbours()){
//            searchSingle(vertex);
//        }
    }
    
    
	public void searchSingle(Vertex startVertex, Vertex center){
	    List<Vertex> clique = new ArrayList<Vertex>(Arrays.asList(center));
//        Set<Vertex> done = startVertex.getNeighbours().stream().collect(Collectors.toSet());
	    Set<Vertex> done = new HashSet<Vertex>();
	    done.add(startVertex);
	    done.add(center);
	    Queue<Vertex> toSearch = new LinkedList<Vertex>();
	    toSearch.add(startVertex); // done.stream().filter(v -> v != center).collect(Collectors.toSet())
		while(!toSearch.isEmpty()){
		    Vertex vertex = toSearch.poll();
			if(vertex.getNeighbours().containsAll(clique)){
			    if(vertex.getClass() == Clique.class) continue;
			    clique.add(vertex);
				toSearch.addAll(vertex.getNeighbours().stream().filter(v -> !done.contains(v)).collect(Collectors.toSet()));
                done.addAll(vertex.getNeighbours());
			}
		}
		
		if(clique.size() >= 3)
		    cliques.add(clique);
	}
	
	   public void search(List<Vertex> vertices){
	        for(Vertex vertex: vertices.get(vertices.size()-1).getNeighbours()){
	            if(vertex.getNeighbours().containsAll(vertices)){
	                if(vertex.getClass() == Clique.class) continue;
	                vertices.add(vertex);
	                search(vertices);
	            } else if(vertices.size() >= 3 && !cliques.contains(vertices)) {
	                cliques.add(vertices);
	            }
	        }
	   }
}
