import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.*;
import org.junit.Test;

public class GraphTest {

	private Graph graph;
	
	@Before
	public void setUp() throws Exception {
		Parser parser = new Parser();
		graph = parser.parseFile(new File("graph01.txt"));
	}
	
	@Test
	public void addedVertices() {
		assertTrue(6 == graph.getVertices().size());
		for(int i = 0; i < graph.getVertices().size(); i++){
			assertNotEquals(null, graph.getVertexById(i));
		}
	}
	
	@Test
	public void addedEdges() {
		assertTrue(8 == graph.getEdges().size());
		for(int i = 0; i < graph.getEdges().size(); i++){
			assertNotEquals(null, graph.getEdgeById(i));
		}
	}
	
	@Test
	public void addNeighbours() {
		for(int i = 0; i < 4; i++)
			assertTrue(3 == graph.getEdgesFrom(graph.getVertexById(i)).size());
			
		for(int i = 4; i < 6; i++)
			assertTrue(2 == graph.getEdgesFrom(graph.getVertexById(i)).size());
		
		assertEquals("[0, 1, 2]",graph.getVertexById(3).getNeighbours().toString());
		assertEquals("[2, 4]",graph.getVertexById(5).getNeighbours().toString());
		assertEquals("[0, 3, 5]",graph.getVertexById(2).getNeighbours().toString());
	}

	@Test
	public void findClique() throws IOException {
		Algorithm algo = new Algorithm(graph);
		Graph newGraph = algo.findCliques(3);
		newGraph.writeOut("./graph01Reduced.txt");
		assertTrue(4 == newGraph.getVertices().size());
		assertTrue(4 == newGraph.getEdges().size());
	}
}
