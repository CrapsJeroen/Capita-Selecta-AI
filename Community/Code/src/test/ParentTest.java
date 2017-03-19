package test;
import genetic.CommunityAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import cliques.CliqueAlgorithm;

import common.Graph;
import common.Parser;
import common.Vertex;

public abstract class ParentTest {

	protected Graph graph;
	protected final int RUNS = 5;
	protected boolean feedback = false;
	protected double maxTime = 300;
	protected boolean skip = false;
	
	@Before
	public void setUp() throws Exception {
		Parser parser = new Parser();
		graph = parser.parseFile(new File("data/football.txt"));
		
	}
	
    protected List<Set<Vertex>> find(Graph graph, Data data) {
        CommunityAlgorithm algo = new CommunityAlgorithm(graph);
        List<Set<Vertex>> result = algo.solve(5, 1000, maxTime, data, feedback);
        
        return result;
    }
    
    protected Graph getCliqueGraph(){
        CliqueAlgorithm cliqueAlgo = new CliqueAlgorithm(graph);
        Graph clique = cliqueAlgo.findCliques(100000, 1000);
        return clique;
    }

	@Test
	public void findCommunities() throws IOException {	
	    if(skip) return;
	    Data data = new Data();
	    System.out.println("REGULAR:");
		IntStream.range(0, RUNS)
		        .forEach(i -> {
		            System.out.print("RUN " + (i) + "...");
		            find(graph, data);
		            System.out.println(" DONE");
		            });
		System.out.println("REGULAR:");
		data.printData();
	}
	
   @Test
    public void findCommunitiesWithCliques() throws IOException {
       if(skip) return;
        Graph clique = getCliqueGraph();
        Data data = new Data();
        System.out.println("CLIQUES:");
        IntStream.range(0, RUNS)
                .forEach(i -> {
                    System.out.print("RUN " + (i) + "...");
                    find(clique, data);
                    System.out.println(" DONE");
                    });
        System.out.println("CLIQUES:");
        data.printData();
    }
}
