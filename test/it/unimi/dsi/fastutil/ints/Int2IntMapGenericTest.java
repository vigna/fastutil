package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.function.BiFunction;
import org.junit.Test;

public abstract class Int2IntMapGenericTest {
	static final Integer MINUS_ONE = Integer.valueOf(-1);
	static final Integer ZERO = Integer.valueOf(0);
	static final Integer ONE = Integer.valueOf(1);
	static final Integer TWO = Integer.valueOf(2);
	static final Integer THREE = Integer.valueOf(3);

	protected Int2IntMap m;

	@Test
	public void testPrimitiveGetOrDefault() {
		m.put(1, 1);

		assertEquals(1, m.getOrDefault(0, 1));
		assertEquals(1, m.getOrDefault(1, 2));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGenericGetOrDefault() {
		m.put(1, 1);

		assertEquals(THREE, m.getOrDefault(null, THREE));
		assertEquals(THREE, m.getOrDefault(ZERO, THREE));
		assertNull(m.getOrDefault(ZERO, null));
		assertNull(m.getOrDefault(null, null));

		assertEquals(ONE, m.getOrDefault(ONE, THREE));
		assertEquals(ONE, m.getOrDefault(ONE, null));
	}

	@SuppressWarnings("deprecation")
	@Test(expected = ClassCastException.class)
	public void testGenericGetOrDefaultInvalidKey() {
		m.getOrDefault(Long.valueOf(1), ONE);
	}

	@Test
	public void testPrimitivePutIfAbsent() {
		m.defaultReturnValue(-1);

		m.put(1, 1);
		assertEquals(1, m.putIfAbsent(1, 2));

		assertEquals(-1, m.putIfAbsent(2, 2));
		assertEquals(2, m.get(2));

		assertEquals(2, m.putIfAbsent(2, 3));
		assertEquals(2, m.get(2));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGenericPutIfAbsent() {
		m.defaultReturnValue(-1);

		m.put(1, 1);
		assertEquals(ONE, m.putIfAbsent(ONE, TWO));
		assertEquals(ONE, m.putIfAbsent(ONE, null));

		assertNull(m.putIfAbsent(TWO, TWO));
		assertEquals(TWO, m.get(TWO));

		assertEquals(TWO, m.putIfAbsent(TWO, THREE));
		assertEquals(TWO, m.get(TWO));
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testGenericPutIfAbsentNullValueMissingKey() {
		m.putIfAbsent(ONE, null);
	}

	@Test
	public void testPrimitiveRemove() {
		m.defaultReturnValue(-1);
		m.put(1, 1);
		assertTrue(m.containsKey(1));

		assertFalse(m.remove(2, 1));
		assertFalse(m.remove(2, 2));
		assertFalse(m.remove(1, 2));

		assertTrue(m.containsKey(1));

		assertTrue(m.remove(1, 1));
		assertFalse(m.containsKey(1));

		assertFalse(m.remove(1, 1));
		assertFalse(m.remove(1, -1));

		assertEquals(-1, m.get(1));
		assertFalse(m.containsKey(1));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGenericRemove() {
		m.defaultReturnValue(-1);
		m.put(1, 1);
		assertTrue(m.containsKey(ONE));

		assertFalse(m.remove(TWO, ONE));
		assertFalse(m.remove(TWO, TWO));
		assertFalse(m.remove(ONE, TWO));

		assertTrue(m.containsKey(ONE));

		assertTrue(m.remove(ONE, ONE));
		assertFalse(m.containsKey(ONE));

		assertFalse(m.remove(ONE, ONE));
		assertFalse(m.remove(ONE, MINUS_ONE));

		assertNull(m.get(ONE));
		assertFalse(m.containsKey(ONE));
	}

	@SuppressWarnings("deprecation")
	@Test(expected = ClassCastException.class)
	public void testGenericRemoveInvalidKey() {
		m.put(1, 1);
		m.remove(Long.valueOf(1), ONE);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = ClassCastException.class)
	public void testGenericRemoveInvalidValue() {
		m.put(1, 1);
		m.remove(ONE, Long.valueOf(1));
	}

	@SuppressWarnings("deprecation")
	@Test(expected = ClassCastException.class)
	public void testGenericRemoveInvalidValueMissingKey() {
		m.remove(ONE, Long.valueOf(1));
	}

	@Test
	public void testPrimitiveReplaceTernary() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		assertFalse(m.replace(2, 1, 1));
		assertFalse(m.replace(2, 2, 1));
		assertFalse(m.replace(1, 2, 1));
		assertEquals(1, m.get(1));

		assertTrue(m.replace(1, 1, 1));
		assertTrue(m.replace(1, 1, 2));
		assertFalse(m.replace(1, 1, 2));

		assertTrue(m.replace(1, 2, -1));
		assertEquals(-1, m.get(1));
		assertTrue(m.containsKey(1));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGenericReplaceTernary() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		assertFalse(m.replace(TWO, ONE, ONE));
		assertFalse(m.replace(TWO, TWO, ONE));
		assertFalse(m.replace(ONE, TWO, ONE));
		assertEquals(ONE, m.get(ONE));

		assertTrue(m.replace(ONE, ONE, ONE));
		assertTrue(m.replace(ONE, ONE, TWO));
		assertFalse(m.replace(ONE, ONE, TWO));

		assertTrue(m.replace(ONE, TWO, MINUS_ONE));
		assertEquals(MINUS_ONE, m.get(ONE));
		assertTrue(m.containsKey(ONE));
	}
	/*

        Object curValue = get(key);
        if (!Objects.equals(curValue, oldValue) ||
            (curValue == null && !containsKey(key))) {
            return false;
        }
        put(key, newValue);
        return true;
	 */

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testGenericReplaceTernaryNullKey() {
		m.replace(null, ONE, ONE);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testGenericReplaceTernaryNullOldValueMissingKey() {
		m.replace(ONE, null, ONE);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testGenericReplaceTernaryNullNewValue() {
		m.put(1, 1);
		m.replace(ONE, ONE, null);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testGenericReplaceTernaryNullNewValueMissingKey() {
		m.replace(ONE, ONE, null);
	}

	@Test
	public void testPrimitiveReplaceBinary() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		assertEquals(-1, m.replace(2, 1));
		assertEquals(-1, m.replace(2, 2));
		assertFalse(m.containsKey(2));

		assertEquals(1, m.replace(1, 2));
		assertEquals(2, m.replace(1, 2));
		assertEquals(2, m.replace(1, 1));
		assertEquals(1, m.get(1));
		assertTrue(m.containsKey(1));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGenericReplaceBinary() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		assertNull(m.replace(TWO, ONE));
		assertNull(m.replace(TWO, TWO));
		assertFalse(m.containsKey(TWO));

		assertEquals(ONE, m.replace(ONE, TWO));
		assertEquals(TWO, m.replace(ONE, TWO));
		assertEquals(TWO, m.replace(ONE, ONE));
		assertEquals(ONE, m.get(ONE));
		assertTrue(m.containsKey(ONE));
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testGenericReplaceBinaryNullKey() {
		m.replace(null, ONE);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testGenericReplaceBinaryNullValue() {
		m.put(1, 1);
		m.replace(ONE, null);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGenericReplaceBinaryNullValueMissingKey() {
		assertNull(m.replace(ONE, null));
	}

	@Test
	public void testPrimitiveComputeIfAbsent() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		assertEquals(1, m.computeIfAbsent(1, key -> key - 1));

		assertEquals(1, m.computeIfAbsent(2, key -> key - 1));
		assertEquals(1, m.computeIfAbsent(2, key -> key - 2));
		assertEquals(1, m.get(2));

		assertEquals(2, m.size());
	}

	@Test(expected = NullPointerException.class)
	public void testPrimitiveComputeIfAbsentNullFunction() {
		m.put(1, 1);
		m.computeIfAbsent(1, null);
	}

	@Test(expected = NullPointerException.class)
	public void testPrimitiveComputeIfAbsentNullFunctionMissingKey() {
		m.computeIfAbsent(1, null);
	}

	@Test
	public void testPrimitiveComputeIfAbsentNullable() {
		m.defaultReturnValue(-1);

		assertEquals(-1, m.computeIfAbsentNullable(1, key -> null));
		assertEquals(-1, m.computeIfAbsentNullable(2, key -> null));

		m.put(1, 1);
		assertEquals(1, m.computeIfAbsentNullable(1, key -> null));
		assertEquals(-1, m.computeIfAbsentNullable(2, key -> null));

		assertEquals(1, m.computeIfAbsentNullable(1, Integer::valueOf));
		assertEquals(2, m.computeIfAbsentNullable(2, Integer::valueOf));
		assertEquals(2, m.computeIfAbsentNullable(2, key -> null));
		assertEquals(2, m.get(2));
	}

	@Test(expected = NullPointerException.class)
	public void testPrimitiveComputeIfAbsentNullableNullFunction() {
		m.put(1, 1);
		m.computeIfAbsentNullable(1, null);
	}

	@Test(expected = NullPointerException.class)
	public void testPrimitiveComputeIfAbsentNullableNullFunctionMissingKey() {
		m.computeIfAbsentNullable(1, null);
	}

	@Test
	public void testPrimitiveComputeIfAbsentPartial() {
		m.defaultReturnValue(-1);

		Int2IntFunction f = new Int2IntArrayMap();
		f.defaultReturnValue(1);
		f.put(1, 1);

		assertEquals(1, m.computeIfAbsentPartial(1, f));
		assertEquals(1, m.get(1));

		assertEquals(-1, m.computeIfAbsentPartial(2, f));
		assertFalse(m.containsKey(2));

		f.put(2, 2);
		assertEquals(2, m.computeIfAbsentPartial(2, f));
		assertTrue(m.containsKey(2));
	}

	@Test(expected = NullPointerException.class)
	public void testPrimitiveComputeIfAbsentPartialNullFunction() {
		m.put(1, 1);
		m.computeIfAbsentPartial(1, null);
	}

	@Test(expected = NullPointerException.class)
	public void testPrimitiveComputeIfAbsentPartialNullFunctionMissingKey() {
		m.computeIfAbsentPartial(1, null);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGenericComputeIfAbsent() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		assertEquals(ONE, m.computeIfAbsent(ONE, key -> Integer.valueOf(key.intValue() - 1)));

		assertEquals(ONE, m.computeIfAbsent(TWO, key -> Integer.valueOf(key.intValue() - 1)));
		assertEquals(ONE, m.computeIfAbsent(TWO, key -> Integer.valueOf(key.intValue() - 2)));
		assertEquals(ONE, m.get(TWO));

		assertEquals(2, m.size());
		m.clear();

		assertEquals(null, m.computeIfAbsent(ONE, key -> null));
		assertEquals(null, m.computeIfAbsent(TWO, key -> null));
		assertTrue(m.isEmpty());

		m.put(ONE, ONE);
		assertEquals(ONE, m.computeIfAbsent(ONE, key -> null));
		assertEquals(null, m.computeIfAbsent(TWO, key -> null));

		assertEquals(ONE, m.computeIfAbsent(ONE, key -> key));
		assertEquals(TWO, m.computeIfAbsent(TWO, key -> key));
		assertEquals(TWO, m.computeIfAbsent(TWO, key -> null));
		assertEquals(TWO, m.get(TWO));
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testGenericComputeIfAbsentNullKey() {
		m.computeIfAbsent(null, key -> key);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testGenericComputeIfAbsentNullFunction() {
		m.put(1, 1);
		m.computeIfAbsent(ONE, null);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testGenericComputeIfAbsentNullFunctionMissingKey() {
		m.computeIfAbsent(ONE, null);
	}

	@Test
	public void testPrimitiveComputeIfPresent() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		BiFunction<Integer, Integer, Integer> add = (key, value) -> Integer.valueOf(key.intValue() + value.intValue());

		assertEquals(-1, m.computeIfPresent(2, add));
		assertEquals(-1, m.computeIfPresent(2, (key, value) -> null));
		assertFalse(m.containsKey(2));

		assertEquals(2, m.computeIfPresent(1, add));
		assertEquals(2, m.get(1));
		assertEquals(3, m.computeIfPresent(1, add));
		assertEquals(3, m.get(1));

		assertEquals(-1, m.computeIfPresent(1, (key, value) -> Integer.valueOf(-1)));
		assertTrue(m.containsKey(1));

		assertEquals(-1, m.computeIfPresent(1, (key, value) -> null));
		assertFalse(m.containsKey(1));
	}

	@Test(expected = NullPointerException.class)
	public void testPrimitiveComputeIfPresentNullFunction() {
		m.put(1, 1);
		m.computeIfPresent(1, null);
	}

	@Test(expected = NullPointerException.class)
	public void testPrimitiveComputeIfPresentNullFunctionMissingKey() {
		m.computeIfPresent(1, null);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGenericComputeIfPresent() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		BiFunction<Integer, Integer, Integer> add = (key, value) -> Integer.valueOf(key.intValue() + value.intValue());

		assertEquals(null, m.computeIfPresent(TWO, add));
		assertEquals(null, m.computeIfPresent(TWO, (key, value) -> null));
		assertFalse(m.containsKey(TWO));

		assertEquals(TWO, m.computeIfPresent(ONE, add));
		assertEquals(TWO, m.get(ONE));
		assertEquals(THREE, m.computeIfPresent(ONE, add));
		assertEquals(THREE, m.get(ONE));

		assertEquals(MINUS_ONE, m.computeIfPresent(ONE, (key, value) -> MINUS_ONE));
		assertTrue(m.containsKey(ONE));

		assertEquals(null, m.computeIfPresent(ONE, (key, value) -> null));
		assertFalse(m.containsKey(ONE));
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testGenericComputeIfPresentNullKey() {
		m.computeIfPresent(null, (key, value) -> key);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testGenericComputeIfPresentNullFunction() {
		m.put(1, 1);
		m.computeIfPresent(ONE, null);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testGenericComputeIfPresentNullFunctionMissingKey() {
		m.computeIfPresent(ONE, null);
	}

	@Test
	public void testPrimitiveCompute() {
		m.defaultReturnValue(-1);

		assertEquals(1, m.compute(1, (key, value) -> {
			assertEquals(1, key.intValue());
			assertNull(value);
			return Integer.valueOf(1);
		}));
		assertEquals(1, m.get(1));

		assertEquals(2, m.compute(1, (key, value) -> {
			assertEquals(1, key.intValue());
			assertEquals(1, value.intValue());
			return Integer.valueOf(2);
		}));
		assertEquals(2, m.get(1));

		assertEquals(-1, m.compute(1, (key, value) -> {
			assertEquals(1, key.intValue());
			assertEquals(2, value.intValue());
			return null;
		}));
		assertFalse(m.containsKey(1));
	}

	@Test(expected = NullPointerException.class)
	public void testPrimitiveComputeNullFunction() {
		m.put(1, 1);
		m.compute(1, null);
	}

	@Test(expected = NullPointerException.class)
	public void testPrimitiveComputeNullFunctionMissingKey() {
		m.compute(1, null);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGenericCompute() {
		m.defaultReturnValue(-1);

		assertEquals(ONE, m.compute(ONE, (key, value) -> {
			assertEquals(ONE, key);
			assertNull(value);
			return ONE;
		}));
		assertEquals(ONE, m.get(ONE));

		assertEquals(TWO, m.compute(ONE, (key, value) -> {
			assertEquals(ONE, key);
			assertEquals(ONE, value);
			return TWO;
		}));
		assertEquals(TWO, m.get(ONE));

		assertEquals(null, m.compute(ONE, (key, value) -> {
			assertEquals(ONE, key);
			assertEquals(TWO, value);
			return null;
		}));
		assertFalse(m.containsKey(ONE));
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testGenericComputeNullFunction() {
		m.put(1, 1);
		m.compute(ONE, null);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testGenericComputeNullFunctionMissingKey() {
		m.compute(ONE, null);
	}

	@Test
	public void testPrimitiveMerge() {
		m.defaultReturnValue(-1);

		assertEquals(0, m.merge(1, 0, (oldVal, newVal) -> {
			assertNull(oldVal);
			assertEquals(0, newVal.intValue());
			return Integer.valueOf(0);
		}));
		assertEquals(0, m.get(1));
		m.clear();

		BiFunction<Integer, Integer, Integer> add = (oldVal, newVal) -> Integer.valueOf(oldVal.intValue() + newVal.intValue());

		assertEquals(0, m.merge(1, 0, add));
		assertEquals(1, m.merge(1, 1, add));
		assertEquals(3, m.merge(1, 2, add));
		assertEquals(0, m.merge(2, 0, add));
		assertTrue(m.containsKey(1));

		assertEquals(-1, m.merge(1, 2, (key, value) -> null));
		assertEquals(-1, m.merge(2, 2, (key, value) -> null));

		assertTrue(m.isEmpty());
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testPrimitiveMergeNullFunction() {
		m.put(1, 1);
		m.merge(1, 1, null);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testPrimitiveMergeNullFunctionMissingKey() {
		m.merge(1, 1, null);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGenericMerge() {
		m.defaultReturnValue(-1);

		assertEquals(ZERO, m.merge(ONE, ZERO, (oldVal, newVal) -> {
			assertNull(oldVal);
			assertEquals(ZERO, newVal);
			return ZERO;
		}));
		assertEquals(ZERO, m.get(ONE));
		m.clear();

		BiFunction<Integer, Integer, Integer> add = (oldVal, newVal) -> Integer.valueOf(oldVal.intValue() + newVal.intValue());

		assertEquals(ZERO, m.merge(ONE, ZERO, add));
		assertEquals(ONE, m.merge(ONE, ONE, add));
		assertEquals(THREE, m.merge(ONE, TWO, add));
		assertEquals(ZERO, m.merge(TWO, ZERO, add));
		assertTrue(m.containsKey(ONE));

		assertEquals(null, m.merge(ONE, TWO, (key, value) -> null));
		assertEquals(null, m.merge(TWO, TWO, (key, value) -> null));

		assertTrue(m.isEmpty());
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testGenericMergeNullKey() {
		m.merge(null, ONE, (key, vale) -> ONE);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testGenericMergeNullValue() {
		m.put(1, 1);
		m.merge(ONE, null, (key, vale) -> ONE);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testGenericMergeNullValueMissingKey() {
		m.merge(ONE, null, (key, vale) -> ONE);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testGenericMergeNullFunction() {
		m.put(1, 1);
		m.merge(ONE, ONE, null);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testGenericMergeNullFunctionMissingKey() {
		m.merge(ONE, ONE, null);
	}

}