package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.function.BiFunction;

import org.junit.Test;

import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectSet;

public class Int2ObjectMapTest {
	private final static Integer ZERO = new Integer(0);
	private final static Integer ONE = new Integer(1);
	private final static Integer TWO = new Integer(2);
	private final static Integer THREE = new Integer(3);
	private final static Integer FOUR = new Integer(4);
	private final static Integer SIX = new Integer(6);
	private final static	 Integer DEFAULT = new Integer(-1);
	
	@SuppressWarnings("deprecation")
	@Test
	public void testDefaultGetOrDefault() {
		Int2ObjectMap<Object> m = new SimpleInt2ObjectMap(new Int2ObjectArrayMap<>());
		m.put(1, ONE);

		assertEquals(ONE, m.getOrDefault(0, ONE));
		assertEquals(ONE, m.getOrDefault(ZERO, ONE));
		assertEquals(ONE, m.getOrDefault(1, TWO));
		assertEquals(ONE, m.getOrDefault(ONE, TWO));
	}

	@SuppressWarnings("deprecation")
	@Test(expected=NullPointerException.class)
	public void testDefaultGetOrDefaultNullArg() {
		Int2ObjectMap<Object> m = new SimpleInt2ObjectMap(new Int2ObjectArrayMap<>());
		assertEquals(ONE, m.getOrDefault(null, ONE));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testDefaultPutIfAbsent() {
		Int2ObjectMap<Object> m = new SimpleInt2ObjectMap(new Int2ObjectArrayMap<>());
		m.defaultReturnValue(DEFAULT);
		m.put(1, ONE);

		assertEquals(ONE, m.putIfAbsent(1, TWO));
		assertEquals(DEFAULT, m.putIfAbsent(2, TWO));
		assertEquals(TWO, m.putIfAbsent(2, THREE));
		assertEquals(TWO, m.get(2));

		assertEquals(ONE, m.putIfAbsent(ONE, TWO));
		assertEquals(DEFAULT, m.putIfAbsent(THREE, TWO));
		assertEquals(TWO, m.putIfAbsent(THREE, TWO));
		assertEquals(TWO, m.get(TWO));
		assertEquals(TWO, m.get(THREE));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testDefaultRemove() {
		Int2ObjectMap<Object> m = new SimpleInt2ObjectMap(new Int2ObjectArrayMap<>());
		m.defaultReturnValue(DEFAULT);
		m.put(1, ONE);

		assertFalse(m.remove(2, ONE));
		assertFalse(m.remove(2, TWO));
		assertFalse(m.remove(1, TWO));
		assertTrue(m.remove(1, ONE));
		assertFalse(m.remove(1, ONE));
		assertFalse(m.remove(1, DEFAULT));

		assertEquals(DEFAULT, m.get(1));
		assertFalse(m.containsKey(1));

		m.put(1, ONE);

		assertFalse(m.remove(TWO, ONE));
		assertFalse(m.remove(TWO, TWO));
		assertFalse(m.remove(ONE, TWO));
		assertTrue(m.remove(ONE, ONE));
		assertFalse(m.remove(ONE, ONE));

		assertEquals(DEFAULT, m.get(ONE));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testDefaultReplaceTernary() {
		Int2ObjectMap<Object> m = new SimpleInt2ObjectMap(new Int2ObjectArrayMap<>());
		m.defaultReturnValue(DEFAULT);
		m.put(1, ONE);

		assertFalse(m.replace(2, ONE, ONE));
		assertFalse(m.replace(2, TWO, ONE));
		assertFalse(m.replace(1, TWO, ONE));
		assertTrue(m.replace(1, ONE, ONE));
		assertTrue(m.replace(1, ONE, TWO));
		assertTrue(m.replace(1, TWO, DEFAULT));

		assertEquals(DEFAULT, m.get(1));
		assertTrue(m.containsKey(1));

		m.put(1, ONE);

		assertFalse(m.replace(TWO, ONE, ONE));
		assertFalse(m.replace(TWO, TWO, ONE));
		assertFalse(m.replace(ONE, TWO, ONE));
		assertTrue(m.replace(ONE, ONE, ONE));
		assertTrue(m.replace(ONE, ONE, TWO));

		assertEquals(TWO, m.get(1));
		assertTrue(m.containsKey(1));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testDefaultReplaceBinary() {
		Int2ObjectMap<Object> m = new SimpleInt2ObjectMap(new Int2ObjectArrayMap<>());
		m.defaultReturnValue(DEFAULT);
		m.put(1, ONE);

		assertEquals(DEFAULT, m.replace(2, ONE));
		assertEquals(DEFAULT, m.replace(2, TWO));
		assertEquals(ONE, m.replace(1, TWO));
		assertEquals(TWO, m.replace(1, TWO));
		assertEquals(TWO, m.replace(1, ONE));

		assertEquals(ONE, m.get(1));
		assertTrue(m.containsKey(1));
		assertFalse(m.containsKey(2));

		assertEquals(DEFAULT, m.replace(TWO, ONE));
		assertEquals(DEFAULT, m.replace(TWO, TWO));
		assertEquals(ONE, m.replace(ONE, TWO));
		assertEquals(TWO, m.replace(ONE, TWO));
		assertEquals(TWO, m.replace(ONE, ONE));

		assertEquals(ONE, m.get(1));
		assertTrue(m.containsKey(ONE));
		assertFalse(m.containsKey(TWO));
	}

	@SuppressWarnings({ "deprecation", "boxing" })
	@Test
	public void testDefaultComputeIfAbsent() {
		Int2ObjectMap<Object> m = new SimpleInt2ObjectMap(new Int2ObjectArrayMap<>());
		m.defaultReturnValue(DEFAULT);
		m.put(1, ONE);

		assertEquals(1, m.computeIfAbsent(1, key -> key * 2));
		assertEquals(4, m.computeIfAbsent(2, key -> key * 2));
		assertEquals(4, m.computeIfAbsent(2, key -> key * 3));
		assertEquals(4, m.get(2));

		assertEquals(ONE, m.computeIfAbsent(ONE, key -> key * 2));
		assertEquals(SIX, m.computeIfAbsent(THREE, key -> key * 2));
		assertEquals(SIX, m.computeIfAbsent(THREE, key -> key * 3));
		assertEquals(FOUR, m.get(TWO));
		assertEquals(SIX, m.get(THREE));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testDefaultComputeIfPresent() {
		Int2ObjectMap<Object> m = new SimpleInt2ObjectMap(new Int2ObjectArrayMap<>());
		m.defaultReturnValue(DEFAULT);
		m.put(1, ONE);

		assertEquals(DEFAULT, m.computeIfPresent(2, (key, value) -> key + (Integer)value));
		assertEquals(2, m.computeIfPresent(1, (key, value) -> key + (Integer)value));
		assertEquals(2, m.get(1));
		assertEquals(DEFAULT, m.computeIfPresent(1, (key, value) -> DEFAULT));
		assertTrue(m.containsKey(1));
		assertEquals(DEFAULT, m.computeIfPresent(1, (key, value) -> null));
		assertFalse(m.containsKey(1));
	}

	@Test
	public void testDefaultCompute() {
		Int2ObjectMap<Object> m = new SimpleInt2ObjectMap(new Int2ObjectArrayMap<>());
		m.defaultReturnValue(DEFAULT);

		assertEquals(ONE, m.compute(1, (key, value) -> {
			assertNull(value);
			return key;
		}));
		assertTrue(m.containsKey(1));
		assertEquals(ONE, m.compute(1, (key, value) -> {
			assertEquals(1, ((Integer)value).intValue());
			return key;
		}));
		assertEquals(DEFAULT, m.compute(1, (key, value) -> null));
		assertFalse(m.containsKey(1));
	}

	@SuppressWarnings("boxing")
	@Test
	public void testDefaultMerge() {
		Int2ObjectMap<Object> m = new SimpleInt2ObjectMap(new Int2ObjectArrayMap<>());
		m.defaultReturnValue(DEFAULT);

		BiFunction<Object, Object, Object> add = (key, value) -> (Integer)key + (Integer)value;

		assertEquals(ONE, m.merge(1, ONE, add));
		assertEquals(TWO, m.merge(1, ONE, add));
		assertEquals(FOUR, m.merge(1, TWO, add));
		assertEquals(TWO, m.merge(2, TWO, add));
		assertTrue(m.containsKey(1));

		assertEquals(DEFAULT, m.merge(1, TWO, (key, value) -> null));
		assertEquals(DEFAULT, m.merge(2, TWO, (key, value) -> null));

		assertTrue(m.isEmpty());
	}

	private static final class SimpleInt2ObjectMap implements Int2ObjectMap<Object> {
		private final Int2ObjectMap<Object> delegate;

		SimpleInt2ObjectMap(Int2ObjectMap<Object> delegate) {
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
		public boolean containsValue(Object value) {
			return delegate.containsValue(value);
		}

		@Override
		public void defaultReturnValue(Object rv) {
			delegate.defaultReturnValue(rv);
		}

		@Override
		public Object defaultReturnValue() {
			return delegate.defaultReturnValue();
		}

		@Override
		public Object get(int key) {
			return delegate.get(key);
		}

		@Override
		public ObjectSet<it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry<Object>> int2ObjectEntrySet() {
			return delegate.int2ObjectEntrySet();
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
		public Object put(int key, Object value) {
			return delegate.put(key, value);
		}

		@Override
		public void putAll(Map<? extends Integer, ? extends Object> m) {
			delegate.putAll(m);
		}

		@Override
		public Object remove(int key) {
			return delegate.remove(key);
		}

		@Override
		public int size() {
			return delegate.size();
		}

		@Override
		public ObjectCollection<Object> values() {
			return delegate.values();
		}
	}
}