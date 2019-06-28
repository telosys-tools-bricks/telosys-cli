package org.telosys.tools.cli;

import java.io.IOException;
import java.util.Arrays;

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

//	public boolean hasOption(String[] args, String option) {
//		for ( String a : args ) {
//			if ( option.equals(a) ) {
//				return true ;
//			}
//		}
//		return false ;
//	}
	
	@Test
	public void testCurrentHome() {
		assertNull(command.getCurrentHome());
		command.setCurrentHome("/a/b");
		assertEquals("/a/b", command.getCurrentHome());
	}

	@Test
	public void testCheckArguments() {
		String[] args0 = { "cmd" };
		String[] args1 = { "cmd", "a" };
		String[] args2 = { "cmd", "a", "b" };
		String[] args3 = { "cmd", "a", "b", "c" };
		
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
		
		String[] args1 = { "cmd", "a", "-r" };		
		assertTrue(command.checkOptions(args1 ));
		assertTrue(command.checkOptions(args1, "-r", "-y" ));
		assertTrue(command.checkOptions(args1, "-r" ));

		String[] args2 = { "cmd", "a", "-y" };
		assertTrue(command.checkOptions(args2, "-r", "-y" ));
		assertTrue(command.checkOptions(args2, "-y" ));

		String[] args3 = { "cmd", "a", "-y", "-z" };
		assertTrue(command.checkOptions(args3, "-y", "-z" ));
		assertFalse(command.checkOptions(args3, "-r" )); // Invalid option '-y'
		assertFalse(command.checkOptions(args3, "-y" )); // Invalid option '-z'
	}

	@Test
	public void testRegisterAndRemoveYesOption() {
		String[] newArgs ;
		
		String[] args1 = { "cmd", "a", "-r" };		
		newArgs = command.registerAndRemoveYesOption(args1);
		assertTrue(newArgs == args1); // Same array
		
		String[] args2 = { "cmd", "a", "-r", "-y" };
		System.out.println("initial args  : " + Arrays.toString(args2) );
		newArgs = command.registerAndRemoveYesOption(args2);
		assertTrue(newArgs != args2); // New array
		assertEquals(3, newArgs.length);
		System.out.println("args returned : " + Arrays.toString(newArgs) );
		
		String[] args3 = { "cmd", "-y" };
		System.out.println("initial args  : " + Arrays.toString(args3) );
		newArgs = command.registerAndRemoveYesOption(args3);
		assertTrue(newArgs != args3); // New array
		assertEquals(1, newArgs.length);
		System.out.println("args returned : " + Arrays.toString(newArgs) );
		
	}
	
}
