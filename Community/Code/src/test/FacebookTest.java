package test;
import java.io.File;

import org.junit.Before;
import org.junit.Test;

import common.Graph;
import common.Parser;

public class FacebookTest  extends ParentTest{
    //4039 nodes, 88234 edges, ?? communities

	
	@Before
	public void setUp() throws Exception {
		Parser parser = new Parser();
		graph = parser.parseFile(new File("data/facebook.txt"));
//		feedback = true;
//		skip = true;
//		maxTime = 1;
	}

}