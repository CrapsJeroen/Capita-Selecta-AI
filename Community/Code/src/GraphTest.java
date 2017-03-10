import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;
import java.io.File;
import java.io.IOException;

import jdk.nashorn.internal.ir.SetSplitState;

import org.junit.*;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

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
	}
	
	@Test
	public void addNeighbours() {
		for(int i = 0; i < 4; i++)
			assertTrue(3 == graph.getEdgesFrom(graph.getVertexById(i)).size());
			
		for(int i = 4; i < 6; i++)
			assertTrue(2 == graph.getEdgesFrom(graph.getVertexById(i)).size());
		
		assertTrue(graph.getVertexById(3).getNeighbours().contains(graph.getVertexById(0)));
		assertTrue(graph.getVertexById(3).getNeighbours().contains(graph.getVertexById(1)));
		assertTrue(graph.getVertexById(3).getNeighbours().contains(graph.getVertexById(2)));
		assertEquals(3, graph.getVertexById(3).getNeighbours().size());

		assertTrue(graph.getVertexById(5).getNeighbours().contains(graph.getVertexById(2)));
        assertTrue(graph.getVertexById(5).getNeighbours().contains(graph.getVertexById(4)));
        assertEquals(2, graph.getVertexById(5).getNeighbours().size());
        
        assertTrue(graph.getVertexById(2).getNeighbours().contains(graph.getVertexById(0)));
        assertTrue(graph.getVertexById(2).getNeighbours().contains(graph.getVertexById(3)));
        assertTrue(graph.getVertexById(2).getNeighbours().contains(graph.getVertexById(5)));
	}

	@Test
	public void findClique() throws IOException {
		Algorithm algo = new Algorithm(graph);
		Graph newGraph = algo.findCliques(1, 5);
		newGraph.writeOut("./graph01Reduced.txt");
		assertTrue(4 == newGraph.getVertices().size());
		assertTrue(4 == newGraph.getEdges().size());
	}
}
