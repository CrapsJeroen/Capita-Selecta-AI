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

public class AmazonTest {

	private Graph graph;
	
	@Before
	public void setUp() throws Exception {
		Parser parser = new Parser();
		graph = parser.parseFile(new File("amazonReduced.txt"));
		System.out.println("Parsed");
	}

	@Test
	public void findCommunities() throws IOException {
		CommunityAlgorithm algo = new CommunityAlgorithm(graph);
		
		List<Set<Vertex>> result = algo.solve(2, 1000, 600);
		System.out.println("Communities found: " + (result.size()));
		result.stream().forEach(set -> System.out.println(set.toString()));
	}
}
