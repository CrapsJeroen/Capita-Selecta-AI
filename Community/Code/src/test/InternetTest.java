package test;
import java.io.File;

import org.junit.Before;

import common.Parser;

public class InternetTest  extends ParentTest{
    
    // 22963 nodes, 48436 edges, ?? communities

	
	@Before
	public void setUp() throws Exception {
		Parser parser = new Parser();
		graph = parser.parseFile(new File("data/internet.txt"));
	}

}
