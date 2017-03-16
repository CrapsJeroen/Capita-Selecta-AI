package genetic;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import common.Graph;
import common.Parser;
import common.Vertex;

public class InternetTest {

	private Graph graph;
	
	@Before
	public void setUp() throws Exception {
		Parser parser = new Parser();
		graph = parser.parseFile(new File("internet.txt"));
		System.out.println("Parsed");
	}

	@Test
	public void findCommunities() throws IOException {
		CommunityAlgorithm algo = new CommunityAlgorithm(graph);
		
		List<Set<Vertex>> result = algo.solve(5, 1000, Double.MAX_VALUE);
		System.out.println("Communities found: " + (result.size()));
		result.stream().forEach(set -> System.out.println(set.toString()));
	}
}
