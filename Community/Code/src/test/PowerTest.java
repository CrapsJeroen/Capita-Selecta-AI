package test;
import java.io.File;

import org.junit.Before;

import common.Parser;

public class PowerTest extends ParentTest{
    
    // 4941 nodes, 6594 edges, ?? communities

	
	@Before
	public void setUp() throws Exception {
		Parser parser = new Parser();
		graph = parser.parseFile(new File("data/power.txt"));
		
	}
}
