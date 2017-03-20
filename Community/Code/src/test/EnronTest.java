package test;
import java.io.File;

import org.junit.Before;
import org.junit.Test;

import common.Parser;

public class EnronTest extends ParentTest{
    //36692 nodes, 367662 edges, ?? communities

	
	@Before
	public void setUp() throws Exception {
		Parser parser = new Parser();
		graph = parser.parseFile(new File("data/enron.txt"));
//        skipRegular = true;
//        skipClique = true;
//        feedback = true;
        maxTime = 60*10;
    }
}
