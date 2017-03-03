import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.*;

public class LargeTest {

	private static Graph graph;
	
	@BeforeClass
	public static void setUp() throws Exception {
		Parser parser = new Parser();
		graph = parser.parseFile(new File("amazon.txt"));
	}
	
	@Test
	public void addedVertices() {
		assertTrue(334863 == graph.getVertices().size());
	}
	
	@Test
	public void addedEdges() {
		assertTrue(925872 == graph.getEdges().size());
	}

	@Test
	public void findClique() throws IOException {
		Algorithm algo = new Algorithm(graph);
		Graph tmp = algo.findCliques(50000);
		tmp.writeOut("./amazonReduced.txt");
		System.out.println("Reduced vertices: " + (graph.getVertices().size()-tmp.getVertices().size()));
		System.out.println("Reduced edges: " + (graph.getEdges().size()-tmp.getEdges().size()));
	}
}
