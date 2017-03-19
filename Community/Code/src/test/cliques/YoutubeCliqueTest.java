package test.cliques;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;

import org.junit.BeforeClass;
import org.junit.Test;

import cliques.CliqueAlgorithm;
import common.Graph;
import common.Parser;

public class YoutubeCliqueTest {

	private static Graph graph;
	
	@BeforeClass
	public static void setUp() throws Exception {
		Parser parser = new Parser();
		graph = parser.parseFile(new File("data/youtube.txt"));
	}

	@Test
	public void findClique() throws IOException {
	    NumberFormat              formatter        = new DecimalFormat("#0.0000");
		CliqueAlgorithm algo = new CliqueAlgorithm(graph);
		int initialVertices = graph.getVertices().size();
		int initialEdges = graph.getEdges().size();
        Instant start = Instant.now();
        Graph tmp = algo.findCliques(100000, 1000);
        Instant end = Instant.now();
        System.out.println(Duration.between(start, end));
		int newVertices = tmp.getVertices().size();
		int newEdges = tmp.getEdges().size();
		int diffVertices = initialVertices - newVertices;
		int diffEdges  = initialEdges - newEdges;
		tmp.writeOut("output/youtube1.txt");
		System.out.println("Reduced vertices: " + (diffVertices) + " of " + (initialVertices) 
		        + " (" + formatter.format(100.0 * diffVertices / initialVertices)+ "%)");
		System.out.println("Reduced edges: " + (diffEdges) + " of " + (initialEdges)
	              + " (" + formatter.format(100.0 * diffEdges / initialEdges)+ "%)");
	}
}
