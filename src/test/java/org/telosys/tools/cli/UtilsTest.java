package org.telosys.tools.cli;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

	@Test
	public void testGetWords() {
		String[] words ;

		words = Utils.getWords("");
		assertEquals(0, words.length );
		
		words = Utils.getWords(null);
		assertEquals(0, words.length );
		
		words = Utils.getWords("a b c");
		assertEquals(3, words.length );
		assertEquals("a", words[0] );
		assertEquals("b", words[1] );
		assertEquals("c", words[2] );
		
		words = Utils.getWords(" aa b ccc ");
		assertEquals(3, words.length );
		assertEquals("aa",  words[0] );
		assertEquals("b",   words[1] );
		assertEquals("ccc", words[2] );
		
		words = Utils.getWords(" aa\tb\t ccc ");
		assertEquals(3, words.length );
		assertEquals("aa",  words[0] );
		assertEquals("b",   words[1] );
		assertEquals("ccc", words[2] );
		
		words = Utils.getWords(" aa\t b\t\r ccc\ndd ");
		assertEquals(4, words.length );
		assertEquals("aa",  words[0] );
		assertEquals("b",   words[1] );
		assertEquals("ccc", words[2] );
		assertEquals("dd",  words[3] );
		
	}
}
