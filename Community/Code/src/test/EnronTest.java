package test;
import genetic.CommunityAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import common.Graph;
import common.Parser;
import common.Vertex;

public class EnronTest {
    //36692 nodes, 367662 edges, ?? communities

	private Graph graph;
	
	@Before
	public void setUp() throws Exception {
		Parser parser = new Parser();
//		graph = parser.parseFile(new File("data/enron.txt"));
		graph = parser.parseFile(new File("output/enron1.txt"));
		System.out.println("Parsed");
	}

	@Test
	public void findCommunities() throws IOException {
		CommunityAlgorithm algo = new CommunityAlgorithm(graph);
		
		List<Set<Vertex>> result = algo.solve(5, 1000, 0);
		System.out.println("Communities found: " + (result.size()));
		result.stream().forEach(set -> System.out.println(set.toString()));
	}
}
