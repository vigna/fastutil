package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

import it.unimi.dsi.fastutil.ints.Int2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

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

	@Test
	public void testFastIteratorHelpers() {
		Int2IntMap m = new Int2IntOpenHashMap();
		m.put(0, 0);
		m.put(1, 1);
		ObjectIterator<Entry> fastIterator = Int2IntMaps.fastIterator(m);
		Entry e = fastIterator.next();
		assertSame(e, fastIterator.next());

		ObjectIterable<Entry> fastIterable = Int2IntMaps.fastIterable(m);
		fastIterator = fastIterable.iterator();
		e = fastIterator.next();
		assertSame(e, fastIterator.next());

	}
}
