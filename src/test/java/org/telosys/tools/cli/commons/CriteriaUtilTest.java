package org.telosys.tools.cli.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CriteriaUtilTest {

	@Test
	public void testSelect0() {
		List<String> strings = new ArrayList<>( Arrays.asList("Hello", "world") ) ;
		List<String> result = CriteriaUtil.select(strings, null);
		assertEquals(2, result.size() );
		assertTrue( result.contains("Hello"));
		assertTrue( result.contains("world"));
	}

	@Test
	public void testSelect1() {
		List<String> strings = new ArrayList<>( Arrays.asList("Hello", "world") ) ;
		List<String> criteria = new LinkedList<>( Arrays.asList("Hel", "foo") ) ;

		List<String> result = CriteriaUtil.select(strings, criteria);
		assertEquals(1, result.size() );
		assertTrue( result.contains("Hello"));
	}

	@Test
	public void testSelectAndSort() {
		List<String> strings = new ArrayList<>( Arrays.asList("Baba", "Zoderi", "Acoca", "Fotypi") ) ;

		List<String> result = CriteriaUtil.selectAndSort(strings, null);
		assertEquals(4, result.size() );
		assertEquals("Acoca", result.get(0) );
		assertEquals("Zoderi", result.get(3) );

		List<String> criteria = new LinkedList<>( Arrays.asList("Ba", "pi") ) ;
		result = CriteriaUtil.selectAndSort(strings, criteria);
		assertEquals(2, result.size() );
		assertEquals("Baba", result.get(0) );
		assertEquals("Fotypi", result.get(1) );
	}
}
