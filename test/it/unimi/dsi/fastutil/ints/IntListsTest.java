package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.RandomAccess;

import org.junit.Test;

public class IntListsTest {
	@Test
	public void testRandomAccess() {
		final IntList fakeList = new AbstractIntList() {
			@Override
			public int getInt(int index) {
				return 0;
			}
			@Override
			public int size() {
				return 1;
			}
		};

		assertFalse(IntLists.unmodifiable(fakeList) instanceof RandomAccess);
		assertFalse(IntLists.synchronize(fakeList) instanceof RandomAccess);
		assertFalse(IntLists.synchronize(fakeList, new Object()) instanceof RandomAccess);

		assertTrue(IntLists.unmodifiable(new IntArrayList()) instanceof RandomAccess);
		assertTrue(IntLists.synchronize(new IntArrayList()) instanceof RandomAccess);
		assertTrue(IntLists.synchronize(new IntArrayList(), new Object()) instanceof RandomAccess);
	}
}
