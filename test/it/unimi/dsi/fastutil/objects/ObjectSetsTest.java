package it.unimi.dsi.fastutil.objects;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ObjectSetsTest {
	@Test
	public void testToArrayShouldNullElementAfterLastEntry() {
		ObjectSet<?> set = ObjectSets.EMPTY_SET;
		Object[] values = new Object[] { "test" };
		set.toArray(values);
		assertNull(values[0]);
	}
}
