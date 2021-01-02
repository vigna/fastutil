package it.unimi.dsi.fastutil.booleans;

import static org.junit.Assert.*;
import static it.unimi.dsi.fastutil.booleans.BooleanDirectSet.of;
import static it.unimi.dsi.fastutil.booleans.BooleanDirectSet.ofAll;

import org.junit.Test;

@SuppressWarnings("static-method")
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
		assertEquals(of(true), set);
		assertFalse(set.add(true));
		assertEquals(of(true), set);
	}

	@Test
	public void testAdd_false() {
		BooleanDirectSet set = new BooleanDirectSet();
		assertTrue(set.add(false));
		assertEquals(of(false), set);
		assertFalse(set.add(false));
		assertEquals(of(false), set);
	}
	
	@Test
	public void testAddAll() {
		BooleanDirectSet set = new BooleanDirectSet();
		// Using list instead of BooleanDirectSet as the special logic for another BooleanDirectSet is in a different test.
		BooleanList toAdd = BooleanList.of(false, true, true);
		assertTrue(set.addAll(toAdd));
		assertEquals(ofAll(), set);
		assertEquals(2, set.size());
	}

	@Test
	public void testAddAll_someExisting() {
		BooleanDirectSet set = of(false);
		BooleanList toAdd = BooleanList.of(false, true, true);
		assertTrue(set.addAll(toAdd));
		assertEquals(ofAll(), set);
		assertEquals(2, set.size());
	}

	@Test
	public void testAddAll_allExisting() {
		BooleanDirectSet set = of(false, true);
		BooleanList toAdd = BooleanList.of(false, true, true);
		assertFalse(set.addAll(toAdd));
		assertEquals(ofAll(), set);
		assertEquals(2, set.size());
	}

	@Test
	public void testAddAll_elementExisting() {
		BooleanDirectSet set = of(true);
		BooleanList toAdd = BooleanList.of(true, true);
		assertFalse(set.addAll(toAdd));
		assertEquals(of(true), set);
		assertEquals(1, set.size());
	}

	@Test
	public void testAddAll_booleanDirectSet() {
		BooleanDirectSet set = new BooleanDirectSet();
		// Using list instead of BooleanDirectSet as the special logic for another BooleanDirectSet is in a different test.
		BooleanDirectSet toAdd = of(false, true);
		assertTrue(set.addAll(toAdd));
		assertEquals(ofAll(), set);
		assertEquals(2, set.size());
	}

	@Test
	public void testAddAll_booleanDirectSet_someExisting() {
		BooleanDirectSet set = of(false);
		BooleanDirectSet toAdd = of(false, true);
		assertTrue(set.addAll(toAdd));
		assertEquals(ofAll(), set);
		assertEquals(2, set.size());
	}

	@Test
	public void testAddAll_booleanDirectSet_allExisting() {
		BooleanDirectSet set = of(false, true);
		BooleanDirectSet toAdd = of(false, true);
		assertFalse(set.addAll(toAdd));
		assertEquals(ofAll(), set);
		assertEquals(2, set.size());
	}

	@Test
	public void testAddAll_booleanDirectSet_elementExisting() {
		BooleanDirectSet set = of(true);
		BooleanList toAdd = BooleanList.of(true);
		assertFalse(set.addAll(toAdd));
		assertEquals(of(true), set);
		assertEquals(1, set.size());
	}

	@Test
	public void testRemove() {
		BooleanDirectSet set = ofAll();
		assertTrue(set.remove(true));
		assertEquals(of(false), set);
		assertEquals(1, set);
		assertFalse(set.remove(true));
		assertEquals(of(false), set);
		assertEquals(1, set);
	}

	@Test
	public void testRemove_notPresent() {
		BooleanDirectSet set = of(false);
		assertFalse(set.remove(true));
		assertEquals(of(false), set);
		assertEquals(1, set);
	}
}
