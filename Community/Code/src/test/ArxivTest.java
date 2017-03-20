package test;
import java.io.File;

import org.junit.Before;
import org.junit.Test;

import common.Parser;

public class ArxivTest  extends ParentTest{
    //18772 nodes, 198110 edges, ?? communities

	
	@Before
	public void setUp() throws Exception {
		Parser parser = new Parser();
		graph = parser.parseFile(new File("data/arxiv.txt"));
	      feedback = true;
	      skip = true;
//	      maxTime = 1;
	      }
	      
	      @Test
	      public void customtest(){
	          find(getCliqueGraph(), null);
	      }
}
