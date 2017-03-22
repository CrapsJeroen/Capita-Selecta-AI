package test;
import java.io.File;

import org.junit.Before;

import common.Parser;

public class AmazonTest extends ParentTest{
    
    // 334863 nodes, 925872 edges, 75149 communities
	
	@Before
	public void setUp() throws Exception {
		Parser parser = new Parser();
		graph = parser.parseFile(new File("data/amazon.txt"));
	}
}
