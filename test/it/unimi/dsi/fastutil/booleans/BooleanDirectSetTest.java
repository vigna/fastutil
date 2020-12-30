package it.unimi.dsi.fastutil.booleans;

import static org.junit.Assert.*;

import org.junit.Test;

public class BooleanDirectSetTest {

	@Test
	public void testDefaultConstructEmpty() {
		BooleanDirectSet set = new BooleanDirectSet();
		assertTrue(set.isEmpty());
		assertFalse(set.contains(false));
		assertFalse(set.contains(true));
	}
	
	@Test
	public void testAdd_true() {
		BooleanDirectSet set = new BooleanDirectSet();
		assertTrue(set.add(true));
		assertEquals(BooleanDirectSet.of(true), set);
		assertFalse(set.add(true));
		assertEquals(BooleanDirectSet.of(true), set);
	}

	@Test
	public void testAdd_false() {
		BooleanDirectSet set = new BooleanDirectSet();
		assertTrue(set.add(false));
		assertEquals(BooleanDirectSet.of(false), set);
		assertFalse(set.add(false));
		assertEquals(BooleanDirectSet.of(false), set);
	}
	
	// TODO More tests.

}
