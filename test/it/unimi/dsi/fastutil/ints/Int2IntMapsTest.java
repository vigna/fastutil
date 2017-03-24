package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.Collections;

import org.junit.Test;

public class Int2IntMapsTest {

	@SuppressWarnings("boxing")
	@Test
	public void testSingletonMapEqualsShouldCheckTheTypeOfParamters() {
		Int2IntMap map = Int2IntMaps.singleton(1, 2);
		assertFalse(map.equals(Collections.singletonMap(null, 2)));
		assertFalse(map.equals(Collections.singletonMap(1, null)));
		assertFalse(map.equals(Collections.singletonMap("foo", 2)));
		assertFalse(map.equals(Collections.singletonMap(1, "foo")));
	}

	@Test
	public void testToArrayShouldNullElementAfterLastEntry() {
		Int2IntMap map = Int2IntMaps.EMPTY_MAP;
		Object[] values = new Object[] { "test" };
		map.int2IntEntrySet().toArray(values);
		assertNull(values[0]);
	}

	@Test
	public void testReadingDefaultReturnValueFromUnmodifiableMap() {
		Int2IntMap map = Int2IntMaps.unmodifiable(Int2IntMaps.EMPTY_MAP);
		assert(map.defaultReturnValue() == 0);
	}
}
