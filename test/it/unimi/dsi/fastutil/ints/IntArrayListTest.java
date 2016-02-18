package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Test;

public class IntArrayListTest {

	@Test
	public void testEmptyListIsDifferentFromEmptySet() {
		assertFalse(IntLists.EMPTY_LIST.equals(IntSets.EMPTY_SET));
		assertFalse(IntSets.EMPTY_SET.equals(IntLists.EMPTY_LIST));
	}

	@Test
	public void testNullInContains() {
		assertFalse(new IntArrayList().contains(null));
	}

	@Test
	public void testAddUsingIteratorToTheFirstPosition() {
		IntArrayList list = new IntArrayList();
		list.add(24);
		IntListIterator it = list.listIterator();
		it.add(42);
		assertTrue(it.hasNext());
		assertEquals(IntArrayList.wrap( new int[] { 42, 24 } ), list);
	}
	
	@Test
	public void testRemoveAll() {
		IntArrayList l = IntArrayList.wrap( new int[] { 0, 1, 1, 2 } );
		l.removeAll( IntSets.singleton( 1 ) );
		assertEquals( IntArrayList.wrap( new int[] { 0, 2 } ), l );

		l = IntArrayList.wrap( new int[] { 0, 1, 1, 2 } );
		l.removeAll( Collections.singleton( Integer.valueOf( 1 ) ) );
		assertEquals( IntArrayList.wrap( new int[] { 0, 2 } ), l );
	}
}
