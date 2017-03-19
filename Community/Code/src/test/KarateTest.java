package test;
import genetic.CommunityAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import common.Graph;
import common.Parser;
import common.Vertex;

public class KarateTest {
    // 34 nodes, 78 edges, 4 communities

	private Graph graph;
	
	@Before
	public void setUp() throws Exception {
		Parser parser = new Parser();
//		graph = parser.parseFile(new File("data/karate.txt"));
		graph = parser.parseFile(new File("output/karate1.txt"));
	}

	@Test
	public void findCommunities() throws IOException {
		CommunityAlgorithm algo = new CommunityAlgorithm(graph);
		
//        List<Set<Integer>> optimal = new ArrayList<Set<Integer>>();
//        optimal.add(new HashSet<Integer>(Arrays.asList(5,6,7,11,17)));
//        optimal.add(new HashSet<Integer>(Arrays.asList(1,2,3,4,8,12,13,14,18,20,22)));
//        optimal.add(new HashSet<Integer>(Arrays.asList(9,10,15,16,19,21,23,27,30,31,33,0)));
//        optimal.add(new HashSet<Integer>(Arrays.asList(24,25,26,28,29,32)));
//        System.out.println("Optimal: " + (algo.fitness(optimal)));
		
		List<Set<Vertex>> result = algo.solve(5, 1000, 0);
		System.out.println("Communities found: " + (result.size()));
		result.stream().forEach(set -> System.out.println(set.toString()));
	}
}
