package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class IntSetsTest {
	@Test
	public void testToArrayShouldNullElementAfterLastEntry() {
		IntSet set = IntSets.EMPTY_SET;
		Object[] values = new Object[] { "test" };
		set.toArray(values);
		assertNull(values[0]);
	}
}
