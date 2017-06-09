package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.IntUnaryOperator;
import org.junit.Test;

public class Int2IntMapTest {
	@SuppressWarnings("deprecation")
	@Test
	public void testDefaultGetOrDefault() {
		Int2IntMap m = new SimpleInt2IntMap(new Int2IntArrayMap());
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
		Int2IntMap m = new SimpleInt2IntMap(new Int2IntArrayMap());
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
		Int2IntMap m = new SimpleInt2IntMap(new Int2IntArrayMap());
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
		Int2IntMap m = new SimpleInt2IntMap(new Int2IntArrayMap());
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
		Int2IntMap m = new SimpleInt2IntMap(new Int2IntArrayMap());
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

	@SuppressWarnings("deprecation")
	@Test
	public void testDefaultComputeIfAbsent() {
		Int2IntMap m = new SimpleInt2IntMap(new Int2IntArrayMap());
		m.defaultReturnValue(-1);
		m.put(1, 1);

		assertEquals(1, m.computeIfAbsent(1, (IntUnaryOperator) key -> key * 2));
		assertEquals(4, m.computeIfAbsent(2, (IntUnaryOperator) key -> key * 2));
		assertEquals(4, m.computeIfAbsent(2, (IntUnaryOperator) key -> key * 3));
		assertEquals(4, m.get(2));

		assertEquals(Integer.valueOf(1), m.computeIfAbsent(Integer.valueOf(1), key -> key * 2));
		assertEquals(Integer.valueOf(6), m.computeIfAbsent(Integer.valueOf(3), key -> key * 2));
		assertEquals(Integer.valueOf(6), m.computeIfAbsent(Integer.valueOf(3), key -> key * 3));
		assertEquals(Integer.valueOf(4), m.get(Integer.valueOf(2)));
		assertEquals(Integer.valueOf(6), m.get(Integer.valueOf(3)));
	}

	@Test
	public void testDefaultComputeIfPresent() {
		Int2IntMap m = new SimpleInt2IntMap(new Int2IntArrayMap());
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
		Int2IntMap m = new SimpleInt2IntMap(new Int2IntArrayMap());
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

	@Test
	public void testDefaultMerge() {
		Int2IntMap m = new SimpleInt2IntMap(new Int2IntArrayMap());
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

	private static final class SimpleInt2IntMap implements Int2IntMap {
		private final Int2IntMap delegate;

		SimpleInt2IntMap(Int2IntMap delegate) {
			this.delegate = delegate;
		}

		@Override
		public void clear() {
			delegate.clear();
		}

		@Override
		public boolean containsKey(int key) {
			return delegate.containsKey(key);
		}

		@Override
		public boolean containsValue(int value) {
			return delegate.containsValue(value);
		}

		@Override
		public void defaultReturnValue(int rv) {
			delegate.defaultReturnValue(rv);
		}

		@Override
		public int defaultReturnValue() {
			return delegate.defaultReturnValue();
		}

		@Override
		public int get(int key) {
			return delegate.get(key);
		}

		@Override
		public ObjectSet<Entry> int2IntEntrySet() {
			return delegate.int2IntEntrySet();
		}

		@Override
		public boolean isEmpty() {
			return delegate.isEmpty();
		}

		@Override
		public IntSet keySet() {
			return delegate.keySet();
		}

		@Override
		public int put(int key, int value) {
			return delegate.put(key, value);
		}

		@Override
		public void putAll(Map<? extends Integer, ? extends Integer> m) {
			delegate.putAll(m);
		}

		@Override
		public int remove(int key) {
			return delegate.remove(key);
		}

		@Override
		public int size() {
			return delegate.size();
		}

		@Override
		public IntCollection values() {
			return delegate.values();
		}
	}
}