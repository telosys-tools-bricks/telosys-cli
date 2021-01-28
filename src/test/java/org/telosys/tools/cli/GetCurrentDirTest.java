package org.telosys.tools.cli;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;

public class GetCurrentDirTest {

//	@Before
//	public void initialize() throws IOException {
//		command = createCommand();
//	}

	@Test
	public void testCurrentDir() {
		String dir = Utils.getCurrentDir();
		System.out.println("Current dir : " + dir);
		assertNotNull(dir);
		assertFalse(dir.endsWith("."));
		assertFalse(dir.endsWith("/"));
		assertFalse(dir.endsWith("\\"));
	}
	
}
