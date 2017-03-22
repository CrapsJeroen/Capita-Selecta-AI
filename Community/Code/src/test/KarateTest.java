package test;
import java.io.File;

import org.junit.Before;
import org.junit.Test;

import common.Parser;

public class KarateTest extends ParentTest{
    // 34 nodes, 78 edges, 4 communities

	
	@Before
	public void setUp() throws Exception {
		Parser parser = new Parser();
		graph = parser.parseFile(new File("data/karate.txt"));
    }

}
