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

public class FacebookTest {
    //4039 nodes, 88234 edges, ?? communities

	private Graph graph;
	
	@Before
	public void setUp() throws Exception {
		Parser parser = new Parser();
		graph = parser.parseFile(new File("output/facebook1.txt"));
		System.out.println("Parsed");
	}

	@Test
	public void findCommunities() throws IOException {
		CommunityAlgorithm algo = new CommunityAlgorithm(graph);
		
		List<Set<Vertex>> result = algo.solve(5, 1000, 60);
		System.out.println("Communities found: " + (result.size()));
		result.stream().forEach(set -> System.out.println(set.toString()));
	}
}
