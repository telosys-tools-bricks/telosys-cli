package org.telosys.tools.cli;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import jline.console.ConsoleReader;

public class CommandTest {

	private Command command;

	private Command createCommand() throws IOException {
		ConsoleReader consoleReader = new ConsoleReader();
		CommandProvider commandProvider = new CommandProvider(consoleReader);
		Environment environment = new Environment(commandProvider);
		return new FakeCommand(consoleReader, environment);
	}

	@Before
	public void initialize() throws IOException {
		command = createCommand();
	}

	@Test
	public void testCurrentHome() {
		assertNull(command.getCurrentHome());
		command.setCurrentHome("/a/b");
		assertEquals("/a/b", command.getCurrentHome());
	}

	@Test
	public void testGetArgumentsAsList() {
		String[] args0 = { "cmd" };
		List<String> list = command.getArgumentsAsList(args0);
		assertTrue(list.isEmpty());

		String[] args3 = { "cmd", "a", "b", "-y" };
		List<String> list3 = command.getArgumentsAsList(args3);
		assertEquals(3, list3.size() );
	}

	@Test
	public void testCheckArguments() {
//		String[] args0 = { "cmd" };
//		String[] args1 = { "cmd", "a" };
//		String[] args2 = { "cmd", "a", "b" };
//		String[] args3 = { "cmd", "a", "b", "c" };
		List<String> args0 = new LinkedList<>();
		List<String> args1 = Arrays.asList("a"); 
		List<String> args2 = Arrays.asList( "a", "b" );
		List<String> args3 = Arrays.asList( "a", "b", "c" );
		
		assertTrue(command.checkArguments(args0, 0 ));
		assertTrue(command.checkArguments(args0, 0, 1));
		assertFalse(command.checkArguments(args0, 1));

		assertFalse(command.checkArguments(args1, 0));
		assertTrue(command.checkArguments(args1, 1));
		assertTrue(command.checkArguments(args1, 0, 1, 2, 3));

		assertTrue(command.checkArguments(args2, 2));
		assertTrue(command.checkArguments(args2, 1, 2 ));
		assertTrue(command.checkArguments(args2, 1, 2, 3, 4 ));
		assertTrue(command.checkArguments(args2, 0, 1, 2 ));
		assertTrue(command.checkArguments(args2, 0, 1, 2, 3 ));
		assertTrue(command.checkArguments(args2, 0, 1, 2, 3, 4 ));
		
		assertTrue(command.checkArguments(args3, 3));
		assertTrue(command.checkArguments(args3, 1, 2, 3));
		
		assertFalse(command.checkArguments(args3, 0));
		assertFalse(command.checkArguments(args3, 1));
		assertFalse(command.checkArguments(args3, 2));
	}
	
	@Test
	public void testCheckOptions() {
		
//		String[] args1 = { "cmd", "a", "-r" };		
		List<String> args;
		args = Arrays.asList("a", "-r"); 
		assertTrue(command.checkOptions(args, Arrays.asList("-r")       ));
		assertTrue(command.checkOptions(args, Arrays.asList("-r", "-y") ));
//		assertTrue(command.checkOptions(args1, "-r" ));

//		String[] args2 = { "cmd", "a", "-y" };
		args = Arrays.asList("a", "-y"); 
		assertTrue(command.checkOptions(args, Arrays.asList("-r", "-y") )); // "-r", "-y" ));
		assertTrue(command.checkOptions(args, Arrays.asList("-y")       )); //"-y" ));

//		String[] args3 = { "cmd", "a", "-y", "-z" };
		args = Arrays.asList("a", "-y", "-z" ); 
		assertTrue (command.checkOptions(args, Arrays.asList("-y", "-z") )); // "-y", "-z" ));
		assertFalse(command.checkOptions(args, Arrays.asList("-r")       )); // Invalid option '-y'
		assertFalse(command.checkOptions(args, Arrays.asList("-y")       )); // Invalid option '-z'
	}

	@Test
	public void testIsActiveOptions() {
		assertTrue(  command.isOptionActive("-y", Arrays.asList("-y", "-z")) );
		assertFalse( command.isOptionActive("-x", Arrays.asList("-y", "-z")) );
		assertFalse( command.isOptionActive("-x", new LinkedList<String>()) );
	}

	@Test
	public void testGetOptions() {
		Set<String> options;
		options = command.getOptions(Arrays.asList("-y", "-z"));
		assertEquals( 2, options.size());
		assertTrue( options.contains("-y"));
		assertTrue( options.contains("-z"));
		
		options = command.getOptions(Arrays.asList("aaa", "-y", "bb", "-z", "-x"));
		assertEquals( 3, options.size());
		assertTrue( options.contains("-x"));
		assertTrue( options.contains("-y"));
		assertTrue( options.contains("-z"));
	}

	@Test
	public void testRemoveOptions() {
		List<String> withoutOptions;
		withoutOptions = command.removeOptions(Arrays.asList("-y", "-z"));
		assertEquals(0, withoutOptions.size());
		
		withoutOptions = command.removeOptions(Arrays.asList("aaa", "-y", "bb", "-z", "-x"));
		assertEquals(2, withoutOptions.size());
		assertTrue( withoutOptions.contains("aaa"));
		assertTrue( withoutOptions.contains("bb"));
	}

}
