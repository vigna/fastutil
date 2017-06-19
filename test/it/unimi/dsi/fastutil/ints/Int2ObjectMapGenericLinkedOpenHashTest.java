package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

public class Int2ObjectMapGenericLinkedOpenHashTest extends Int2ObjectMapGenericTest<Int2ObjectLinkedOpenHashMap<Integer>> {
	@Parameters
	public static Iterable<Object[]> data() {
		return Collections.singletonList(new Object[] {(Supplier<Int2ObjectMap<Integer>>) Int2ObjectLinkedOpenHashMap::new, EnumSet.allOf(Capability.class)});
	}

	@Test
	public void testIterator() {
		m.defaultReturnValue(DEFAULT);
		for (int i = 0; i < 100; i++) {
			assertSame(DEFAULT, m.put(i, Integer.valueOf(i)));
		}
		assertEquals(0, m.firstIntKey());

		IntListIterator iterator = (IntListIterator) m.keySet().iterator();
		for (int i = 0; i <= 100; i++) {
			assertEquals(Integer.toString(i), i - 1, iterator.previousIndex());
			assertEquals(Integer.toString(i), i, iterator.nextIndex());
			if (i != 100) {
				assertEquals(Integer.toString(i), i, iterator.nextInt());
			}
		}

		iterator = (IntListIterator) m.keySet().iterator(m.lastIntKey());
		for (int i = 100; i-- != 0; ) {
			assertEquals(Integer.toString(i), i, iterator.previousIndex());
			assertEquals(Integer.toString(i), i + 1, iterator.nextIndex());
			if (i != 0) {
				assertEquals(Integer.toString(i), i, iterator.previousInt());
			}
		}

		iterator = (IntListIterator) m.keySet().iterator(50);
		for (int i = 50; i < 100; i++) {
			assertEquals(Integer.toString(i), i, iterator.previousIndex());
			assertEquals(Integer.toString(i), i + 1, iterator.nextIndex());
			if (i != 99) {
				assertEquals(Integer.toString(i), i + 1, iterator.nextInt());
			}
		}

		iterator = (IntListIterator) m.keySet().iterator(50);
		for (int i = 50; i-- != -1; ) {
			assertEquals(Integer.toString(i), i + 1, iterator.previousIndex());
			assertEquals(Integer.toString(i), i + 2, iterator.nextIndex());
			if (i != -1) {
				assertEquals(Integer.toString(i), i + 1, iterator.previousInt());
			}
		}

		iterator = (IntListIterator) m.keySet().iterator(50);
		for (int i = 50; i-- != -1; ) {
			assertEquals(Integer.toString(i), i + 1, iterator.previousInt());
		}
		assertEquals(-1, iterator.previousIndex());
		assertEquals(0, iterator.nextIndex());

		iterator = (IntListIterator) m.keySet().iterator(50);
		for (int i = 50; i < 100 - 1; i++) {
			assertEquals(Integer.toString(i), i + 1, iterator.nextInt());
		}
		assertEquals(99, iterator.previousIndex());
		assertEquals(100, iterator.nextIndex());

		iterator = (IntListIterator) m.keySet().iterator(50);
		iterator.previousInt();
		iterator.remove();
		assertEquals(49, iterator.previousIndex());
		assertEquals(49, iterator.previousInt());

		iterator = (IntListIterator) m.keySet().iterator(49);
		iterator.nextInt();
		iterator.remove();
		assertEquals(50, iterator.nextIndex());
		assertEquals(52, iterator.nextInt());
	}

	@Test(expected = NoSuchElementException.class)
	public void testIteratorMissingElement() {
		m.defaultReturnValue(DEFAULT);
		for (int i = 0; i < 100; i++) {
			assertSame(DEFAULT, m.put(i, Integer.valueOf(i)));
		}
		m.keySet().iterator(1000);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NoSuchElementException.class)
	public void testNextAtEnd() {
		m.put(1, ONE);
		m.put(2, TWO);
		m.put(3, THREE);
		final ObjectBidirectionalIterator<Map.Entry<Integer, Integer>> iterator = m.entrySet().iterator(m.entrySet().last());
		assertFalse(iterator.hasNext());
		iterator.next();
	}

	@Test(expected = NoSuchElementException.class)
	public void testNextAtEndFast() {
		m.put(1, ONE);
		m.put(2, TWO);
		m.put(3, THREE);
		final ObjectBidirectionalIterator<Entry<Integer>> iterator = m.int2ObjectEntrySet().iterator(m.int2ObjectEntrySet().last());
		assertFalse(iterator.hasNext());
		iterator.next();
	}

	@Test(expected = NoSuchElementException.class)
	public void testPreviousAtStart() {
		m.put(1, ONE);
		m.put(2, TWO);
		m.put(3, THREE);
		@SuppressWarnings("deprecation") final ObjectBidirectionalIterator<Map.Entry<Integer, Integer>> iterator = m.entrySet().iterator();
		assertFalse(iterator.hasPrevious());
		iterator.previous();
	}

	@Test(expected = NoSuchElementException.class)
	public void testPreviousAtStartFast() {
		m.put(1, ONE);
		m.put(2, TWO);
		m.put(3, THREE);
		final ObjectBidirectionalIterator<Entry<Integer>> iterator = m.int2ObjectEntrySet().iterator();
		assertFalse(iterator.hasPrevious());
		iterator.previous();
	}

	@Test
	public void testPutAndMove() {
		m.defaultReturnValue(DEFAULT);
		for (int i = 0; i < 100; i++) {
			assertSame(DEFAULT, m.putAndMoveToFirst(i, Integer.valueOf(i)));
		}
		m.clear();
		for (int i = 0; i < 100; i++) {
			assertSame(DEFAULT, m.putAndMoveToLast(i, Integer.valueOf(i)));
		}
		assertSame(DEFAULT, m.putAndMoveToFirst(-1, MINUS_ONE));
		assertEquals(-1, m.firstIntKey());
		assertSame(DEFAULT, m.putAndMoveToFirst(-2, Integer.valueOf(-2)));
		assertEquals(-2, m.firstIntKey());
		assertEquals(MINUS_ONE, m.putAndMoveToFirst(-1, MINUS_ONE));
		assertEquals(-1, m.firstIntKey());
		assertEquals(MINUS_ONE, m.putAndMoveToFirst(-1, MINUS_ONE));
		assertEquals(-1, m.firstIntKey());
		assertEquals(MINUS_ONE, m.putAndMoveToLast(-1, MINUS_ONE));
		assertEquals(-1, m.lastIntKey());
		assertSame(DEFAULT, m.putAndMoveToLast(100, Integer.valueOf(100)));
		assertEquals(100, m.lastIntKey());
		assertSame(DEFAULT, m.putAndMoveToLast(101, Integer.valueOf(101)));
		assertEquals(101, m.lastIntKey());
		assertEquals(Integer.valueOf(100), m.putAndMoveToLast(100, Integer.valueOf(100)));
		assertEquals(100, m.lastIntKey());
		assertEquals(Integer.valueOf(100), m.putAndMoveToLast(100, Integer.valueOf(100)));
		assertEquals(100, m.lastIntKey());
		assertEquals(Integer.valueOf(100), m.putAndMoveToFirst(100, Integer.valueOf(100)));
		assertEquals(100, m.firstIntKey());
	}

	@Test(expected = NoSuchElementException.class)
	public void testRemoveFirstEmpty() {
		m.removeFirst();
	}

	@Test
	public void testRemoveFirstLast() {
		m.defaultReturnValue(DEFAULT);
		for (int i = 0; i < 100; i++) {
			assertEquals(DEFAULT, m.put(i, Integer.valueOf(1 + i)));
		}
		assertEquals(ONE, m.removeFirst());
		assertEquals(TWO, m.removeFirst());
		assertEquals(Integer.valueOf(100), m.removeLast());
	}

	@Test(expected = NoSuchElementException.class)
	public void testRemoveLastEmpty() {
		m.removeLast();
	}
}