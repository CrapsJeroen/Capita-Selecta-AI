package test;
import genetic.CommunityAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import cliques.CliqueAlgorithm;

import common.Graph;
import common.Parser;
import common.Vertex;

public class FootballTest {
    // 115 nodes, 616 edges, 10 communities

	private Graph graph;
	
	@Before
	public void setUp() throws Exception {
		Parser parser = new Parser();
		graph = parser.parseFile(new File("data/football.txt"));
//		graph = parser.parseFile(new File("output/football1.txt"));
		System.out.println("Parsed");
	}

	@Test
	public void findCommunities() throws IOException {
	    CliqueAlgorithm cliqueAlgo = new CliqueAlgorithm(graph);
	    Graph clique = cliqueAlgo.findCliques(100000, 1000);
		CommunityAlgorithm algo = new CommunityAlgorithm(clique);
		
		List<Set<Vertex>> result = algo.solve(5, 1000, 0);
		System.out.println("Communities found: " + (result.size()));
		result.stream().forEach(set -> System.out.println(set.toString()));
	}
}
