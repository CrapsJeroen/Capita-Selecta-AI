import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Parser {
	private Graph parse(List<String> lines) {
		Iterator<String> it = lines.iterator();

		// Metadata:
		String firstLine = it.next();
		String[] splitted = firstLine.split(" ");

		int amountOfVertices = Integer.parseInt(splitted[0]);

		// Create Vertices
		List<Vertex> vertices = new ArrayList<Vertex>(amountOfVertices);
		for (int i = 0; i < amountOfVertices; i++) {
			vertices.add(new Vertex(i, new ArrayList<Vertex>()));
		}

		// Parse Edges
		List<Edge> edges = new ArrayList<Edge>();
		int i = 0;
		while(it.hasNext()){
			String[] edgeLine = it.next().split(" ");
			int home = Integer.parseInt(edgeLine[0]);
			int away = Integer.parseInt(edgeLine[1]);
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
			// Create Edges
			edges.add(new Edge(i++, neighbours));
		}

		return new Graph(vertices, edges);
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
