package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.function.BiFunction;
import org.junit.Test;

public abstract class Int2IntMapGenericTest {
	protected Int2IntMap m;

	@SuppressWarnings("deprecation")
	@Test
	public void testDefaultGetOrDefault() {
		m.put(1, 1);

		assertEquals(1, m.getOrDefault(0, 1));
		assertEquals(Integer.valueOf(1), m.getOrDefault(null, Integer.valueOf(1)));
		assertEquals(Integer.valueOf(1), m.getOrDefault(Integer.valueOf(0), Integer.valueOf(1)));
		assertEquals(1, m.getOrDefault(1, 2));
		assertEquals(Integer.valueOf(1), m.getOrDefault(Integer.valueOf(1), Integer.valueOf(2)));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testDefaultPutIfAbsent() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		assertEquals(1, m.putIfAbsent(1, 2));
		assertEquals(-1, m.putIfAbsent(2, 2));
		assertEquals(2, m.putIfAbsent(2, 3));
		assertEquals(2, m.get(2));

		assertEquals(Integer.valueOf(1), m.putIfAbsent(Integer.valueOf(1), Integer.valueOf(2)));
		assertNull(m.putIfAbsent(Integer.valueOf(3), Integer.valueOf(2)));
		assertEquals(Integer.valueOf(2), m.putIfAbsent(Integer.valueOf(3), Integer.valueOf(2)));
		assertEquals(Integer.valueOf(2), m.get(Integer.valueOf(2)));
		assertEquals(Integer.valueOf(2), m.get(Integer.valueOf(3)));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testDefaultRemove() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		assertFalse(m.remove(2, 1));
		assertFalse(m.remove(2, 2));
		assertFalse(m.remove(1, 2));
		assertTrue(m.remove(1, 1));
		assertFalse(m.remove(1, 1));
		assertFalse(m.remove(1, -1));

		assertEquals(-1, m.get(1));
		assertFalse(m.containsKey(1));

		m.put(1, 1);

		assertFalse(m.remove(Integer.valueOf(2), Integer.valueOf(1)));
		assertFalse(m.remove(Integer.valueOf(2), Integer.valueOf(2)));
		assertFalse(m.remove(Integer.valueOf(1), Integer.valueOf(2)));
		assertTrue(m.remove(Integer.valueOf(1), Integer.valueOf(1)));
		assertFalse(m.remove(Integer.valueOf(1), Integer.valueOf(1)));

		assertNull(m.get(Integer.valueOf(1)));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testDefaultReplaceTernary() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		assertFalse(m.replace(2, 1, 1));
		assertFalse(m.replace(2, 2, 1));
		assertFalse(m.replace(1, 2, 1));
		assertTrue(m.replace(1, 1, 1));
		assertTrue(m.replace(1, 1, 2));
		assertTrue(m.replace(1, 2, -1));

		assertEquals(-1, m.get(1));
		assertTrue(m.containsKey(1));

		m.put(1, 1);

		assertFalse(m.replace(Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(1)));
		assertFalse(m.replace(Integer.valueOf(2), Integer.valueOf(2), Integer.valueOf(1)));
		assertFalse(m.replace(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(1)));
		assertTrue(m.replace(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(1)));
		assertTrue(m.replace(Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(2)));

		assertEquals(2, m.get(1));
		assertTrue(m.containsKey(1));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testDefaultReplaceBinary() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		assertEquals(-1, m.replace(2, 1));
		assertEquals(-1, m.replace(2, 2));
		assertEquals(1, m.replace(1, 2));
		assertEquals(2, m.replace(1, 2));
		assertEquals(2, m.replace(1, 1));

		assertEquals(1, m.get(1));
		assertTrue(m.containsKey(1));
		assertFalse(m.containsKey(2));

		assertNull(m.replace(Integer.valueOf(2), Integer.valueOf(1)));
		assertNull(m.replace(Integer.valueOf(2), Integer.valueOf(2)));
		assertEquals(Integer.valueOf(1), m.replace(Integer.valueOf(1), Integer.valueOf(2)));
		assertEquals(Integer.valueOf(2), m.replace(Integer.valueOf(1), Integer.valueOf(2)));
		assertEquals(Integer.valueOf(2), m.replace(Integer.valueOf(1), Integer.valueOf(1)));

		assertEquals(1, m.get(1));
		assertTrue(m.containsKey(Integer.valueOf(1)));
		assertFalse(m.containsKey(Integer.valueOf(2)));
	}

	@SuppressWarnings({ "deprecation", "boxing" })
	@Test
	public void testDefaultComputeIfAbsent() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		assertEquals(1, m.computeIfAbsent(1, key -> key * 2));
		assertEquals(4, m.computeIfAbsent(2, key -> key * 2));
		assertEquals(4, m.computeIfAbsent(2, key -> key * 3));
		assertEquals(4, m.get(2));

		assertEquals(1, m.computeIfAbsentNullable(1, key -> null));
		assertEquals(4, m.computeIfAbsentNullable(2, key -> null));
		assertEquals(4, m.computeIfAbsentNullable(2, key -> null));
		assertEquals(2, m.size());

		m.clear();
		assertEquals(-1, m.computeIfAbsentNullable(1, key -> null));

		Int2IntFunction f = new Int2IntArrayMap();
		f.defaultReturnValue(1);
		f.put(1, 1);

		assertEquals(1, m.computeIfAbsentPartial(1, f));
		assertEquals(-1, m.computeIfAbsentPartial(2, f));
		f.put(2, 2);
		assertEquals(2, m.computeIfAbsentPartial(2, f));

		assertEquals(Integer.valueOf(1), m.computeIfAbsent(Integer.valueOf(1), key -> key * 2));
		assertEquals(Integer.valueOf(6), m.computeIfAbsent(Integer.valueOf(3), key -> key * 2));
		assertEquals(Integer.valueOf(6), m.computeIfAbsent(Integer.valueOf(3), key -> key * 3));
		assertEquals(Integer.valueOf(1), m.get(Integer.valueOf(1)));
		assertEquals(Integer.valueOf(2), m.get(Integer.valueOf(2)));
		assertEquals(Integer.valueOf(6), m.get(Integer.valueOf(3)));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testDefaultComputeIfPresent() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		assertEquals(-1, m.computeIfPresent(2, (key, value) -> key + value));
		assertEquals(2, m.computeIfPresent(1, (key, value) -> key + value));
		assertEquals(2, m.get(1));
		assertEquals(-1, m.computeIfPresent(1, (key, value) -> -1));
		assertTrue(m.containsKey(1));
		assertEquals(-1, m.computeIfPresent(1, (key, value) -> null));
		assertFalse(m.containsKey(1));
	}

	@Test
	public void testDefaultCompute() {
		m.defaultReturnValue(-1);

		assertEquals(1, m.compute(1, (key, value) -> {
			assertNull(value);
			return key;
		}));
		assertTrue(m.containsKey(1));
		assertEquals(1, m.compute(1, (key, value) -> {
			assertEquals(1, value.intValue());
			return key;
		}));
		assertEquals(-1, m.compute(1, (key, value) -> null));
		assertFalse(m.containsKey(1));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testDefaultMerge() {
		m.defaultReturnValue(-1);

		BiFunction<Integer, Integer, Integer> add = (key, value) -> key + value;

		assertEquals(1, m.merge(1, 1, add));
		assertEquals(2, m.merge(1, 1, add));
		assertEquals(4, m.merge(1, 2, add));
		assertEquals(2, m.merge(2, 2, add));
		assertTrue(m.containsKey(1));

		assertEquals(-1, m.merge(1, 2, (key, value) -> null));
		assertEquals(-1, m.merge(2, 2, (key, value) -> null));

		assertTrue(m.isEmpty());
	}
}