package common;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Parser {
	private Graph parse(List<String> lines) {
		Iterator<String> it = lines.iterator();

		// Metadata:
		String firstLine = it.next();
		String[] splitted = firstLine.split(" ");

		int amountOfVertices = Integer.parseInt(splitted[0]);

		// Create Vertices
		List<Vertex> vertices = new ArrayList<Vertex>(amountOfVertices+1);
		for (int i = 0; i < amountOfVertices; i++) {
			vertices.add(new Vertex(i, new HashSet<Vertex>()));
		}

		while(it.hasNext()){
			String[] edgeLine = it.next().split("	");
			int home = Integer.parseInt(edgeLine[0]);
			int away = Integer.parseInt(edgeLine[1]);
			
			// 
			int max = (home > away) ? home : away;
			for (int j = vertices.size(); j <= max; j++) {
				vertices.add(new Vertex(j, new HashSet<Vertex>()));
			}
			
			// Retrieve vertices
			Vertex from = vertices.get(home);
			Vertex to = vertices.get(away);
			// Create list
			List<Vertex> neighbours = new ArrayList<>(2);
			neighbours.add(from);
			neighbours.add(to);
			// Add to both lists
			from.addNeighbour(to);
			to.addNeighbour(from);
		}

		vertices = vertices.stream().filter(v -> v.getNeighbours().size() != 0).collect(Collectors.toList());
		
		return new Graph(vertices);
	}

	public Graph parseFile(File file) throws IOException {
		return parse(Files.readAllLines(file.toPath()));
	}

	public static Graph parseFile(String path) throws IOException {
		return (new Parser()).parseFile(new File(path));
	}

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			throw new IllegalArgumentException("No file given");
		}
		
		System.out.println(parseFile(args[0]));	
	}
}
