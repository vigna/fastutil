package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import it.unimi.dsi.fastutil.ints.Int2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

@RunWith(Parameterized.class)
public abstract class Int2IntMapGenericTest {
	private static final Integer MINUS_ONE = Integer.valueOf(-1);
	private static final Integer ONE = Integer.valueOf(1);
	private static final Integer THREE = Integer.valueOf(3);
	private static final Integer TWO = Integer.valueOf(2);
	private static final Integer ZERO = Integer.valueOf(0);
	private static final java.util.Random r = new java.util.Random(0);

	@Parameter(1)
	public EnumSet<Capability> capabilities;
	@Parameter(0)
	public Supplier<? extends Int2IntMap> mapSupplier;
	protected Int2IntMap m;

	@SuppressWarnings("deprecation")
	protected void check(final Int2IntMap m, final Map<Integer, Integer> t, final IntSupplier keyProvider, final IntSupplier valueProvider, final int size) {
		/* First of all, we fill t with random data. */
		for (int i = 0; i < size; i++) {
			t.put(Integer.valueOf(keyProvider.getAsInt()), Integer.valueOf(valueProvider.getAsInt()));
		}
		/* Now we add to m the same data */
		m.putAll(t);

		checkEquality(m, t, genTestKeys(m.keySet(), size), "after insertion");

		/* Now we put and remove random data in m and t, checking that the result is the same. */
		for (int i = 0; i < 20 * size; i++) {
			final int putKey = keyProvider.getAsInt();
			final int val = valueProvider.getAsInt();

			final Integer mPutObj = m.put(Integer.valueOf(putKey), Integer.valueOf(val));
			final Integer tPut = t.put(Integer.valueOf(putKey), Integer.valueOf(val));
			assertTrue("Error: divergence in put() between t and m", Objects.equals(mPutObj, tPut));

			final int remKey = keyProvider.getAsInt();
			assertTrue("Error: divergence in remove() between t and m", Objects.equals(m.remove(Integer.valueOf(remKey)), t.remove(Integer.valueOf(remKey))));
		}

		checkEquality(m, t, genTestKeys(m.keySet(), size), "after removal");
	}

	@SuppressWarnings("deprecation")
	private void checkEquality(final Int2IntMap m, final Map<Integer, Integer> t, final Iterable<Integer> keys, final String description) {
		final int drv = m.defaultReturnValue();

		assertTrue("Error: !m.equals(t) " + description, m.equals(t));
		assertTrue("Error: !t.equals(m) " + description, t.equals(m));

		/* Now we check that m actually holds that data. */
		for (final Map.Entry<Integer, Integer> o2 : t.entrySet()) {
			assertTrue("Error: m and t differ on an entry (" + o2 + ") " + description + " (iterating on t)", Objects.equals(o2.getValue(), m.get(o2.getKey())));
		}
		/* Now we check that m actually holds that data, but iterating on m. */
		for (final Map.Entry<Integer, Integer> entry : m.int2IntEntrySet()) {
			assertTrue("Error: m and t differ on an entry (" + entry + ") " + description + " (iterating on m)", Objects.equals(entry.getValue(), t.get(entry.getKey())));
		}

		/* Now we check that m actually holds the same keys. */
		for (final Integer objKey : t.keySet()) {
			assertTrue("Error: m and t differ on a key (" + objKey + ") " + description + " (iterating on t)", m.containsKey(objKey));
			assertTrue("Error: m and t differ on a key (" + objKey + ", in keySet()) " + description + " (iterating on t)", m.keySet().contains(objKey));
		}
		/* Now we check that m actually holds the same keys, but iterating on m. */
		for (final Integer objKey : m.keySet()) {
			assertTrue("Error: m and t differ on a key " + description + " (iterating on m)", t.containsKey(objKey));
			assertTrue("Error: m and t differ on a key (in keySet()) " + description + " (iterating on m)", t.keySet().contains(objKey));
		}

		/* Now we check that m actually hold the same values. */
		for (final Integer objVal : t.values()) {
			assertTrue("Error: m and t differ on a value " + description + " (iterating on t)", m.containsValue(objVal));
			assertTrue("Error: m and t differ on a value (in values()) " + description + " (iterating on t)", m.values().contains(objVal));
		}
		/* Now we check that m actually hold the same values, but iterating on m. */
		for (final Integer objVal : m.values()) {
			assertTrue("Error: m and t differ on a value " + description + " (iterating on m)", t.containsValue(objVal));
			assertTrue("Error: m and t differ on a value (in values()) " + description + " (iterating on m)", t.values().contains(objVal));
		}

		/* Now we check that inquiries about random data give the same answer in m and t. */
		for (final Integer objKey : keys) {
			assertTrue("Error: divergence in keys between t and m (polymorphic method)", m.containsKey(objKey) == t.containsKey(objKey));

			final int mVal = m.get(objKey.intValue());
			final Integer mValObj = m.get(objKey);
			final Integer tVal = t.get(objKey);
			assertEquals("Error: divergence between t and m " + description + " (polymorphic method)", tVal, mValObj);
			assertTrue("Error: divergence between polymorphic and standard method " + description, mVal == (mValObj == null ? drv : mValObj.intValue()));
			assertEquals("Error: divergence between t and m " + description + " (standard method)", tVal == null ? drv : tVal.intValue(), mVal);
		}
	}

	private IntCollection genTestKeys(final IntCollection initial, final int amount) {
		final IntCollection keys = new IntOpenHashSet(initial);
		for (int i = 0; i < amount; i++) {
			keys.add(r.nextInt());
		}
		return keys;
	}

	@SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
	@Test
	public void int2IntEntrySetContainsTest() {
		m.put(0, 0);
		assertFalse(m.int2IntEntrySet().contains(new AbstractMap.SimpleEntry(new Object(), null)));
		assertFalse(m.int2IntEntrySet().contains(new AbstractMap.SimpleEntry(null, new Object())));
		assertFalse(m.int2IntEntrySet().contains(new AbstractMap.SimpleEntry(null, null)));
		assertFalse(m.int2IntEntrySet().contains(new AbstractMap.SimpleEntry(new Object(), new Object())));
	}

	@SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
	@Test
	public void int2IntEntrySetRemoveTest() {
		m.put(0, 0);
		assertFalse(m.int2IntEntrySet().remove(new AbstractMap.SimpleEntry(new Object(), null)));
		assertFalse(m.int2IntEntrySet().remove(new AbstractMap.SimpleEntry(null, new Object())));
		assertFalse(m.int2IntEntrySet().remove(new AbstractMap.SimpleEntry(null, null)));
		assertFalse(m.int2IntEntrySet().remove(new AbstractMap.SimpleEntry(new Object(), new Object())));
	}

	@Before
	public void setUp() {
		m = mapSupplier.get();
	}

	@Test
	public void testCompute() {
		m.defaultReturnValue(-1);
		assertEquals(1000, m.compute(0, (x, y) -> Integer.valueOf(x.intValue() + (y != null ? y.intValue() : 1000))));
		assertEquals(1000, m.get(0));
		assertEquals(2000, m.compute(0, (x, y) -> Integer.valueOf(x.intValue() + y.intValue() * 2)));
		assertEquals(2000, m.get(0));
		assertEquals(-1, m.compute(0, (x, y) -> null));
		assertEquals(-1, m.get(0));

		assertEquals(1001, m.compute(1, (x, y) -> Integer.valueOf(x.intValue() + (y != null ? y.intValue() : 1000))));
		assertEquals(1001, m.get(1));
		assertEquals(2003, m.compute(1, (x, y) -> Integer.valueOf(x.intValue() + y.intValue() * 2)));
		assertEquals(2003, m.get(1));
		assertEquals(-1, m.compute(1, (x, y) -> null));
		assertEquals(-1, m.get(1));

		assertEquals(-1, m.compute(2, (x, y) -> null));
		assertEquals(-1, m.get(2));
	}

	@Test
	public void testComputeIfAbsentNullablePrimitive() {
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
	public void testComputeIfAbsentNullablePrimitiveNullFunction() {
		m.put(1, 1);
		m.computeIfAbsentNullable(1, null);
	}

	@Test(expected = NullPointerException.class)
	public void testComputeIfAbsentNullablePrimitiveNullFunctionMissingKey() {
		m.computeIfAbsentNullable(1, null);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testComputeIfAbsentObject() {
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
	public void testComputeIfAbsentObjectNullFunction() {
		m.put(1, 1);
		m.computeIfAbsent(ONE, null);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testComputeIfAbsentObjectNullFunctionMissingKey() {
		m.computeIfAbsent(ONE, null);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testComputeIfAbsentObjectNullKey() {
		m.computeIfAbsent(null, key -> key);
	}

	@Test
	public void testComputeIfAbsentPartialPrimitive() {
		m.defaultReturnValue(-1);

		final Int2IntFunction f = new Int2IntArrayMap();
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
	public void testComputeIfAbsentPartialPrimitiveNullFunction() {
		m.put(1, 1);
		m.computeIfAbsentPartial(1, null);
	}

	@Test(expected = NullPointerException.class)
	public void testComputeIfAbsentPartialPrimitiveNullFunctionMissingKey() {
		m.computeIfAbsentPartial(1, null);
	}

	@Test
	public void testComputeIfAbsentPrimitive() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		assertEquals(1, m.computeIfAbsent(1, key -> key - 1));

		assertEquals(1, m.computeIfAbsent(2, key -> key - 1));
		assertEquals(1, m.computeIfAbsent(2, key -> key - 2));
		assertEquals(1, m.get(2));

		assertEquals(2, m.size());
	}

	@Test(expected = NullPointerException.class)
	public void testComputeIfAbsentPrimitiveNullFunction() {
		m.put(1, 1);
		m.computeIfAbsent(1, null);
	}

	@Test(expected = NullPointerException.class)
	public void testComputeIfAbsentPrimitiveNullFunctionMissingKey() {
		m.computeIfAbsent(1, null);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testComputeIfPresentObject() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		final BiFunction<Integer, Integer, Integer> add = (key, value) -> Integer.valueOf(key.intValue() + value.intValue());

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
	public void testComputeIfPresentObjectNullFunction() {
		m.put(1, 1);
		m.computeIfPresent(ONE, null);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testComputeIfPresentObjectNullFunctionMissingKey() {
		m.computeIfPresent(ONE, null);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testComputeIfPresentObjectNullKey() {
		m.computeIfPresent(null, (key, value) -> key);
	}

	@Test
	public void testComputeIfPresentPrimitive() {
		m.defaultReturnValue(-1);
		m.put(1, 1);

		final BiFunction<Integer, Integer, Integer> add = (key, value) -> Integer.valueOf(key.intValue() + value.intValue());

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
	public void testComputeIfPresentPrimitiveNullFunction() {
		m.put(1, 1);
		m.computeIfPresent(1, null);
	}

	@Test(expected = NullPointerException.class)
	public void testComputeIfPresentPrimitiveNullFunctionMissingKey() {
		m.computeIfPresent(1, null);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testComputeObject() {
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
	public void testComputeObjectNullFunction() {
		m.put(1, 1);
		m.compute(ONE, null);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testComputeObjectNullFunctionMissingKey() {
		m.compute(ONE, null);
	}

	@Test
	public void testComputePrimitive() {
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
	public void testComputePrimitiveNullFunction() {
		m.put(1, 1);
		m.compute(1, null);
	}

	@Test(expected = NullPointerException.class)
	public void testComputePrimitiveNullFunctionMissingKey() {
		m.compute(1, null);
	}

	@Test
	public void testEntrySetIteratorFastForEach() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final Set<Int2IntMap.Entry> s = new HashSet<>();
		Int2IntMaps.fastForEach(m, x -> s.add(new AbstractInt2IntMap.BasicEntry(x.getIntKey(), x.getIntValue())));
		assertEquals(m.int2IntEntrySet(), s);
	}

	@Test
	public void testEntrySetIteratorForEach() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final Set<Entry> s = new HashSet<>();
		m.int2IntEntrySet().forEach(s::add);
		assertEquals(m.int2IntEntrySet(), s);
	}

	@Test
	public void testFastIterator() {
		assumeTrue(m.int2IntEntrySet() instanceof Int2IntMap.FastEntrySet);
		assumeTrue(capabilities.contains(Capability.ITERATOR_MODIFY));
		m.defaultReturnValue(-1);
		for (int i = 0; i < 100; i++) {
			assertEquals(-1, m.put(i, i));
		}
		final ObjectIterator<Entry> fastIterator = Int2IntMaps.fastIterator(m);
		final Entry entry = fastIterator.next();
		final int key = entry.getIntKey();
		entry.setValue(-1000);
		assertEquals(m.get(key), -1000);
		fastIterator.remove();
		assertEquals(m.get(key), -1);
	}

	@Test
	public void testGetOrDefault() {
		m.defaultReturnValue(-1);
		assertEquals(0, m.getOrDefault(0, 0));
		m.put(0, 1);
		assertEquals(1, m.getOrDefault(0, 0));
		assertEquals(0, m.getOrDefault(1, 0));
		m.put(1, 1);
		assertEquals(1, m.getOrDefault(1, 2));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetOrDefaultObject() {
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
	public void testGetOrDefaultObjectInvalidKey() {
		m.getOrDefault(Long.valueOf(1), ONE);
	}

	@Test
	public void testGetOrDefaultPrimitive() {
		m.put(1, 1);

		assertEquals(1, m.getOrDefault(0, 1));
		assertEquals(1, m.getOrDefault(1, 2));
	}

	@Test
	public void testInt2IntEntrySet() {
		m.defaultReturnValue(-1);
		for (int i = 0; i < 100; i++) {
			assertEquals(-1, m.put(i, i));
		}
		for (int i = 0; i < 100; i++) {
			assertTrue(m.int2IntEntrySet().contains(new AbstractInt2IntMap.BasicEntry(0, 0)));
		}
		for (int i = 0; i < 100; i++) {
			assertFalse(m.int2IntEntrySet().contains(new AbstractInt2IntMap.BasicEntry(i, -1)));
		}
		for (int i = 0; i < 100; i++) {
			assertTrue(m.int2IntEntrySet().contains(new AbstractInt2IntMap.BasicEntry(i, i)));
		}
		for (int i = 0; i < 100; i++) {
			assertFalse(m.int2IntEntrySet().remove(new AbstractInt2IntMap.BasicEntry(i, -1)));
		}
		for (int i = 0; i < 100; i++) {
			assertTrue(m.int2IntEntrySet().remove(new AbstractInt2IntMap.BasicEntry(i, i)));
		}
		assertTrue(m.int2IntEntrySet().isEmpty());
	}

	@Test
	public void testKeySetIteratorForEach() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final IntOpenHashSet s = new IntOpenHashSet();
		m.keySet().forEach((java.util.function.IntConsumer) s::add);
		assertEquals(s, m.keySet());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testKeySetIteratorForEachObject() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final IntOpenHashSet s = new IntOpenHashSet();
		m.keySet().forEach((Consumer<Integer>) s::add);
		assertEquals(s, m.keySet());
	}

	@Test
	public void testMerge() {
		m.defaultReturnValue(-1);
		assertEquals(0, m.merge(0, 0, (x, y) -> Integer.valueOf(1000)));
		assertEquals(0, m.get(0));
		assertEquals(1000, m.merge(0, 0, (x, y) -> Integer.valueOf(1000)));
		assertEquals(1000, m.get(0));
		assertEquals(2000, m.merge(0, 500, (x, y) -> Integer.valueOf(x.intValue() + y.intValue() * 2)));
		assertEquals(2000, m.get(0));
		assertEquals(-1, m.merge(0, 0, (x, y) -> null));
		assertEquals(-1, m.get(0));

		assertEquals(0, m.merge(1, 0, (x, y) -> Integer.valueOf(1000)));
		assertEquals(0, m.get(1));
		assertEquals(1000, m.merge(1, 0, (x, y) -> Integer.valueOf(1000)));
		assertEquals(1000, m.get(1));
		assertEquals(2000, m.merge(1, 500, (x, y) -> Integer.valueOf(x.intValue() + y.intValue() * 2)));
		assertEquals(2000, m.get(1));
		assertEquals(-1, m.merge(1, 0, (x, y) -> null));
		assertEquals(-1, m.get(1));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testMergeObject() {
		m.defaultReturnValue(-1);

		assertEquals(ZERO, m.merge(ONE, ZERO, (oldVal, newVal) -> {
			assertNull(oldVal);
			assertEquals(ZERO, newVal);
			return ZERO;
		}));
		assertEquals(ZERO, m.get(ONE));
		m.clear();

		final BiFunction<Integer, Integer, Integer> add = (oldVal, newVal) -> Integer.valueOf(oldVal.intValue() + newVal.intValue());

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
	public void testMergeObjectNullFunction() {
		m.put(1, 1);
		m.merge(ONE, ONE, null);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testMergeObjectNullFunctionMissingKey() {
		m.merge(ONE, ONE, null);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testMergeObjectNullKey() {
		m.merge(null, ONE, (key, vale) -> ONE);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testMergeObjectNullValue() {
		m.put(1, 1);
		m.merge(ONE, null, (key, vale) -> ONE);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testMergeObjectNullValueMissingKey() {
		m.merge(ONE, null, (key, vale) -> ONE);
	}

	@Test
	public void testMergePrimitive() {
		m.defaultReturnValue(-1);

		assertEquals(0, m.merge(1, 0, (oldVal, newVal) -> {
			assertNull(oldVal);
			assertEquals(0, newVal.intValue());
			return Integer.valueOf(0);
		}));
		assertEquals(0, m.get(1));
		m.clear();

		final BiFunction<Integer, Integer, Integer> add = (oldVal, newVal) -> Integer.valueOf(oldVal.intValue() + newVal.intValue());

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
	public void testMergePrimitiveNullFunction() {
		m.put(1, 1);
		m.merge(1, 1, null);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testMergePrimitiveNullFunctionMissingKey() {
		m.merge(1, 1, null);
	}

	@Test
	public void testPutAndGetPrimitive() {
		m.defaultReturnValue(-1);
		assertEquals(-1, m.get(1));
		m.put(1, 1);
		assertEquals(1, m.get(1));
		m.defaultReturnValue(1);
		assertTrue(m.containsKey(1));
		assertEquals(1, m.get(1));
	}

	@Test
	public void testPutIfAbsent() {
		m.defaultReturnValue(-1);
		assertEquals(-1, m.putIfAbsent(0, 0));
		assertTrue(m.containsKey(0));
		assertEquals(0, m.get(0));
		assertEquals(0, m.putIfAbsent(0, 1));
		assertEquals(0, m.get(0));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testPutIfAbsentObject() {
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
	public void testPutIfAbsentObjectNullValueMissingKey() {
		m.putIfAbsent(ONE, null);
	}

	@Test
	public void testPutIfAbsentPrimitive() {
		m.defaultReturnValue(-1);

		m.put(1, 1);
		assertEquals(1, m.putIfAbsent(1, 2));

		assertEquals(-1, m.putIfAbsent(2, 2));
		assertEquals(2, m.get(2));

		assertEquals(2, m.putIfAbsent(2, 3));
		assertEquals(2, m.get(2));
	}

	@Test
	public void testRemove() {
		m.defaultReturnValue(-1);
		for (int i = 0; i < 100; i++) {
			assertEquals(-1, m.put(i, i));
		}
		for (int i = 0; i < 100; i++) {
			assertEquals(-1, m.remove(100 + i));
		}
		for (int i = 50; i < 150; i++) {
			assertEquals(Integer.toString(i % 100), i % 100, m.remove(i % 100));
		}
	}

	@Test
	public void testRemove0() {
		m.defaultReturnValue(-1);
		for (int i = -1; i <= 1; i++) {
			assertEquals(-1, m.put(i, i));
		}
		assertEquals(0, m.remove(0));
		final IntIterator iterator = m.keySet().iterator();
		final IntOpenHashSet z = new IntOpenHashSet();
		z.add(iterator.nextInt());
		z.add(iterator.nextInt());
		assertFalse(iterator.hasNext());
		assertEquals(new IntOpenHashSet(new int[] {-1, 1}), z);
	}

	@Test
	public void testRemove0KeySet() {
		assumeTrue(capabilities.contains(Capability.KEY_SET_MODIFY));
		m.defaultReturnValue(-1);
		for (int i = -1; i <= 1; i++) {
			assertEquals(-1, m.put(i, i));
		}
		IntIterator iterator = m.keySet().iterator();
		while (iterator.hasNext()) {
			if (iterator.nextInt() == 0) {
				iterator.remove();
			}
		}

		assertFalse(m.containsKey(0));
		assertEquals(-1, m.get(0));

		iterator = m.keySet().iterator();
		final int[] content = new int[2];
		content[0] = iterator.nextInt();
		content[1] = iterator.nextInt();
		assertFalse(iterator.hasNext());
		Arrays.sort(content);
		assertArrayEquals(new int[] {-1, 1}, content);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRemoveObject() {
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
	public void testRemoveObjectInvalidKey() {
		m.put(1, 1);
		m.remove(Long.valueOf(1), ONE);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = ClassCastException.class)
	public void testRemoveObjectInvalidValue() {
		m.put(1, 1);
		m.remove(ONE, Long.valueOf(1));
	}

	@SuppressWarnings("deprecation")
	@Test(expected = ClassCastException.class)
	public void testRemoveObjectInvalidValueMissingKey() {
		m.remove(ONE, Long.valueOf(1));
	}

	@Test
	public void testRemovePrimitive() {
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

	@Test
	public void testRemoveWithValue() {
		m.defaultReturnValue(-1);
		assertFalse(m.remove(0, 0));
		m.put(0, 1);
		assertFalse(m.remove(0, 0));
		assertTrue(m.containsKey(0));
		m.put(0, 0);
		assertTrue(m.remove(0, 0));
		assertFalse(m.containsKey(0));

		assertFalse(m.remove(1, 0));
		m.put(1, 1);
		assertFalse(m.remove(1, 0));
		assertTrue(m.containsKey(1));
		m.put(1, 0);
		assertTrue(m.remove(1, 0));
		assertFalse(m.containsKey(1));
	}

	@Test
	public void testReplace() {
		m.defaultReturnValue(-1);
		assertEquals(-1, m.replace(0, 0));
		m.put(0, 1);
		assertEquals(1, m.replace(0, 0));
		assertEquals(0, m.get(0));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testReplaceBinaryObject() {
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
	public void testReplaceBinaryObjectNullKey() {
		m.replace(null, ONE);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testReplaceBinaryObjectNullValue() {
		m.put(1, 1);
		m.replace(ONE, null);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testReplaceBinaryObjectNullValueMissingKey() {
		assertNull(m.replace(ONE, null));
	}

	@Test
	public void testReplaceBinaryPrimitive() {
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
	public void testReplaceTernaryObject() {
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

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testReplaceTernaryObjectNullKey() {
		m.replace(null, ONE, ONE);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testReplaceTernaryObjectNullNewValue() {
		m.put(1, 1);
		m.replace(ONE, ONE, null);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testReplaceTernaryObjectNullNewValueMissingKey() {
		m.replace(ONE, ONE, null);
	}

	@SuppressWarnings("deprecation")
	@Test(expected = NullPointerException.class)
	public void testReplaceTernaryObjectNullOldValueMissingKey() {
		m.replace(ONE, null, ONE);
	}

	@Test
	public void testReplaceTernaryPrimitive() {
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

	@Test
	public void testReplaceWithValue() {
		m.defaultReturnValue(-1);
		assertFalse(m.replace(0, 0, 1));
		m.put(0, 1);
		assertFalse(m.replace(0, 0, 1));
		assertEquals(1, m.get(0));
		assertTrue(m.replace(0, 1, 0));
		assertEquals(0, m.get(0));
	}

	@Test
	public void testSizeAndIsEmpty() {
		assertEquals(0, m.size());
		assertTrue(m.isEmpty());
		for (int i = 0; i < 100; i++) {
			assertEquals(i, m.size());
			assertEquals(m.defaultReturnValue(), m.put(i, i));
			assertFalse(m.isEmpty());
		}
		for (int i = 0; i < 100; i++) {
			assertEquals(100, m.size());
			assertEquals(i, m.put(i, i));
		}
		for (int i = 99; i >= 0; i--) {
			assertEquals(i, m.remove(i));
			assertEquals(i, m.size());
		}
		assertEquals(0, m.size());
		assertTrue(m.isEmpty());
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		m.clear();
		assertEquals(0, m.size());
		assertTrue(m.isEmpty());
	}

	@Test
	public void testValueSetIteratorForEach() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final IntOpenHashSet s = new IntOpenHashSet();
		m.values().forEach((java.util.function.IntConsumer) s::add);
		assertEquals(s, new IntOpenHashSet(m.values()));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testValueSetIteratorForEachObject() {
		for (int i = 0; i < 100; i++) {
			m.put(i, i);
		}
		final IntOpenHashSet s = new IntOpenHashSet();
		m.values().forEach((Consumer<Integer>) s::add);
		assertEquals(s, new IntOpenHashSet(m.values()));
	}

	@Test
	public void testWithReference10() {
		check(m, new HashMap<>(), () -> r.nextInt(10), r::nextInt, 10);
	}

	@Test
	public void testWithReference100() {
		check(m, new HashMap<>(), () -> r.nextInt(100), r::nextInt, 100);
	}

	@Test
	public void testWithReference1000() {
		check(m, new HashMap<>(), () -> r.nextInt(1000), r::nextInt, 1000);
	}

	@Test
	public void testWithReference10000() {
		check(m, new HashMap<>(), () -> r.nextInt(10000), r::nextInt, 10000);
	}

	public enum Capability {
		NULL_VALUE, KEY_SET_MODIFY, ITERATOR_MODIFY
	}
}