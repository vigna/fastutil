package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IntCollectionsTest {

	@Test
	public void testIsNotEmpty() {
		IntCollection test = IntCollections.asCollection(new IntIterable() {
			@Override
			public IntIterator iterator() {
				return IntSets.singleton(0).iterator();
			}
		});
		
		assertFalse(test.isEmpty());
	}

	@Test
	public void testEmpty() {
		IntCollection test = IntCollections.asCollection(new IntIterable() {
			@Override
			public IntIterator iterator() {
				return IntSets.EMPTY_SET.iterator();
			}
		});
		
		assertTrue(test.isEmpty());
	}
}
