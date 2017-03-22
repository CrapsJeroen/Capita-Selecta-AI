package test;
import java.io.File;

import org.junit.Before;

import common.Parser;

public class ArxivTest  extends ParentTest{
    //18772 nodes, 198110 edges, ?? communities

	
	@Before
	public void setUp() throws Exception {
		Parser parser = new Parser();
		graph = parser.parseFile(new File("data/arxiv.txt"));
	      }
	      
}
