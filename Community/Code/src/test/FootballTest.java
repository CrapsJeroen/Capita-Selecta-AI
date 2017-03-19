package test;
import java.io.File;

import org.junit.Before;

import common.Parser;

public class FootballTest extends ParentTest{
    // 115 nodes, 616 edges, 10 communities


	
	@Before
	public void setUp() throws Exception {
		Parser parser = new Parser();
		graph = parser.parseFile(new File("data/football.txt"));
		
	}
	
}
