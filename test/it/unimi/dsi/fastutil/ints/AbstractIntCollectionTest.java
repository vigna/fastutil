package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

public class AbstractIntCollectionTest {
	@Test
	public void testAllSpecific() {
		IntCollection m = new AbstractIntCollection() {
			final IntOpenHashSet s = new IntOpenHashSet(new int[] { 0, 4, 5, 6, 9 });

			@Override
			public IntIterator iterator() {
				return s.iterator();
			}

			@Override
			public boolean add(int k) {
				return s.add(k);
			}

			@Override
			public boolean rem(int k) {
				return s.remove(k);
			}

			@Override
			public int size() {
				return s.size();
			}
		};

		m.addAll(new IntOpenHashSet(new int[] { 1, 3 }));
		assertEquals(new IntOpenHashSet(new int[] { 0, 1, 3, 4, 5, 6, 9 }), new IntOpenHashSet(m));
		assertTrue(m.containsAll(new IntOpenHashSet(new int[] { 1, 3, 6 })));
		assertFalse(m.containsAll(new IntOpenHashSet(new int[] { 1, 2, 6 })));
		assertTrue(m.removeAll(new IntOpenHashSet(new int[] { 1, 2, 3 })));
		assertEquals(new IntOpenHashSet(new int[] { 0, 4, 5, 6, 9 }), new IntOpenHashSet(m));
		assertTrue(m.retainAll(new IntOpenHashSet(new int[] { -1, 0, 5, 9, 11 })));
		assertEquals(new IntOpenHashSet(new int[] { 0, 5, 9 }), new IntOpenHashSet(m));
	}

	@Test
	public void testAllGeneric() {
		IntCollection m = new AbstractIntCollection() {
			final IntOpenHashSet s = new IntOpenHashSet(new int[] { 0, 4, 5, 6, 9 });

			@Override
			public IntIterator iterator() {
				return s.iterator();
			}

			@Override
			public boolean add(int k) {
				return s.add(k);
			}

			@Override
			public boolean rem(int k) {
				return s.remove(k);
			}

			@Override
			public int size() {
				return s.size();
			}
		};

		m.addAll((Collection<? extends Integer>)new IntOpenHashSet(new int[] { 1, 3 }));
		assertEquals(new IntOpenHashSet(new int[] { 0, 1, 3, 4, 5, 6, 9 }), new IntOpenHashSet(m));
		assertTrue(m.containsAll((Collection<?>)new IntOpenHashSet(new int[] { 1, 3, 6 })));
		assertFalse(m.containsAll((Collection<?>)new IntOpenHashSet(new int[] { 1, 2, 6 })));
		assertTrue(m.removeAll((Collection<?>)new IntOpenHashSet(new int[] { 1, 2, 3 })));
		assertEquals(new IntOpenHashSet(new int[] { 0, 4, 5, 6, 9 }), new IntOpenHashSet(m));
		assertTrue(m.retainAll((Collection<?>)new IntOpenHashSet(new int[] { -1, 0, 5, 9, 11 })));
		assertEquals(new IntOpenHashSet(new int[] { 0, 5, 9 }), new IntOpenHashSet(m));
	}
}
