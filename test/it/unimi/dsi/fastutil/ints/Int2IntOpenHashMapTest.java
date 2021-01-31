/*
 * Copyright (C) 2017-2021 Sebastiano Vigna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.MainRunner;
import it.unimi.dsi.fastutil.ints.Int2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public class Int2IntOpenHashMapTest {
	@SuppressWarnings("deprecation")
	@Test
	public void testContainsNull() {
		final Int2IntOpenHashMap m = new Int2IntOpenHashMap(new int[] {1, 2, 3}, new int[] {1, 2, 3});
		assertFalse(m.containsKey(null));
		assertTrue(m.get(null) == null);
	}

	@SuppressWarnings({ "boxing", "unlikely-arg-type" })
	@Test
	public void testEquals() {
		final Int2IntOpenHashMap m = new Int2IntOpenHashMap(new int[] {1, 2}, new int[] {1, 2});
		assertFalse(m.equals(new Object2ObjectOpenHashMap<>(new Integer[] {1, null}, new Integer[] {1, 1})));
	}

	@Test
	public void testStrangeRetainAllCase() {
		final IntArrayList initialElements = IntArrayList.wrap(new int[] {586, 940,
				1086, 1110, 1168, 1184, 1185, 1191, 1196, 1229, 1237, 1241,
				1277, 1282, 1284, 1299, 1308, 1309, 1310, 1314, 1328, 1360,
				1366, 1370, 1378, 1388, 1392, 1402, 1406, 1411, 1426, 1437,
				1455, 1476, 1489, 1513, 1533, 1538, 1540, 1541, 1543, 1547,
				1548, 1551, 1557, 1568, 1575, 1577, 1582, 1583, 1584, 1588,
				1591, 1592, 1601, 1610, 1618, 1620, 1633, 1635, 1653, 1654,
				1655, 1660, 1661, 1665, 1674, 1686, 1688, 1693, 1700, 1705,
				1717, 1720, 1732, 1739, 1740, 1745, 1746, 1752, 1754, 1756,
				1765, 1766, 1767, 1771, 1772, 1781, 1789, 1790, 1793, 1801,
				1806, 1823, 1825, 1827, 1828, 1829, 1831, 1832, 1837, 1839,
				1844, 2962, 2969, 2974, 2990, 3019, 3023, 3029, 3030, 3052,
				3072, 3074, 3075, 3093, 3109, 3110, 3115, 3116, 3125, 3137,
				3142, 3156, 3160, 3176, 3180, 3188, 3193, 3198, 3207, 3209,
				3210, 3213, 3214, 3221, 3225, 3230, 3231, 3236, 3240, 3247,
				3261, 4824, 4825, 4834, 4845, 4852, 4858, 4859, 4867, 4871,
				4883, 4886, 4887, 4905, 4907, 4911, 4920, 4923, 4924, 4925,
				4934, 4942, 4953, 4957, 4965, 4973, 4976, 4980, 4982, 4990,
				4993, 6938, 6949, 6953, 7010, 7012, 7034, 7037, 7049, 7076,
				7094, 7379, 7384, 7388, 7394, 7414, 7419, 7458, 7459, 7466,
				7467});

		final IntArrayList retainElements = IntArrayList.wrap(new int[] {586});

		// Initialize both implementations with the same data
		final Int2IntOpenHashMap instance = new Int2IntOpenHashMap(initialElements.elements(), new int[initialElements.size()]);
		final IntRBTreeSet referenceInstance = new IntRBTreeSet(initialElements);

		instance.keySet().retainAll(retainElements);
		referenceInstance.retainAll(retainElements);

		// Fails
		assertEquals(referenceInstance, instance.keySet());
	}

	@Test
	public void testWrapAround() {
		final Int2IntOpenHashMap m = new Int2IntOpenHashMap(4, .5f);
		assertEquals(8, m.n);
		// The following code inverts HashCommon.phiMix() and places strategically keys in slots 6, 7 and 0
		m.put(HashCommon.invMix(6), 0);
		m.put(HashCommon.invMix(7), 0);
		m.put(HashCommon.invMix(6 + 8), 0);
		assertNotEquals(0, m.key[0]);
		assertNotEquals(0, m.key[6]);
		assertNotEquals(0, m.key[7]);
		final IntOpenHashSet keys = new IntOpenHashSet(m.keySet());
		final IntIterator iterator = m.keySet().iterator();
		final IntOpenHashSet t = new IntOpenHashSet();
		t.add(iterator.nextInt());
		t.add(iterator.nextInt());
		// Originally, this remove would move the entry in slot 0 in slot 6 and we would return the entry in 0 twice
		iterator.remove();
		t.add(iterator.nextInt());
		assertEquals(keys, t);
	}

	@Test
	public void testWrapAround2() {
		final Int2IntOpenHashMap m = new Int2IntOpenHashMap(4, .75f);
		assertEquals(8, m.n);
		// The following code inverts HashCommon.phiMix() and places strategically keys in slots 4, 5, 6, 7 and 0
		m.put(HashCommon.invMix(4), 0);
		m.put(HashCommon.invMix(5), 0);
		m.put(HashCommon.invMix(4 + 8), 0);
		m.put(HashCommon.invMix(5 + 8), 0);
		m.put(HashCommon.invMix(4 + 16), 0);
		assertNotEquals(0, m.key[0]);
		assertNotEquals(0, m.key[4]);
		assertNotEquals(0, m.key[5]);
		assertNotEquals(0, m.key[6]);
		assertNotEquals(0, m.key[7]);

		final IntOpenHashSet keys = new IntOpenHashSet(m.keySet());
		final IntIterator iterator = m.keySet().iterator();
		final IntOpenHashSet t = new IntOpenHashSet();
		assertTrue(t.add(iterator.nextInt()));
		iterator.remove();
		assertTrue(t.add(iterator.nextInt()));
		// Originally, this remove would move the entry in slot 0 in slot 6 and we would return the entry in 0 twice
		assertTrue(t.add(iterator.nextInt()));
		assertTrue(t.add(iterator.nextInt()));
		iterator.remove();
		assertTrue(t.add(iterator.nextInt()));
		assertEquals(3, m.size());
		assertEquals(keys, t);
	}

	@Test
	public void testWrapAround3() {
		final Int2IntOpenHashMap m = new Int2IntOpenHashMap(4, .75f);
		assertEquals(8, m.n);
		// The following code inverts HashCommon.phiMix() and places strategically keys in slots 5, 6, 7, 0 and 1
		m.put(HashCommon.invMix(5), 0);
		m.put(HashCommon.invMix(5 + 8), 0);
		m.put(HashCommon.invMix(5 + 16), 0);
		m.put(HashCommon.invMix(5 + 32), 0);
		m.put(HashCommon.invMix(5 + 64), 0);
		assertNotEquals(0, m.key[5]);
		assertNotEquals(0, m.key[6]);
		assertNotEquals(0, m.key[7]);
		assertNotEquals(0, m.key[0]);
		assertNotEquals(0, m.key[1]);

		final IntOpenHashSet keys = new IntOpenHashSet(m.keySet());
		final IntIterator iterator = m.keySet().iterator();
		final IntOpenHashSet t = new IntOpenHashSet();
		assertTrue(t.add(iterator.nextInt()));
		iterator.remove();
		assertTrue(t.add(iterator.nextInt()));
		iterator.remove();
		// Originally, this remove would move the entry in slot 0 in slot 6 and we would return the entry in 0 twice
		assertTrue(t.add(iterator.nextInt()));
		iterator.remove();
		assertTrue(t.add(iterator.nextInt()));
		iterator.remove();
		assertTrue(t.add(iterator.nextInt()));
		iterator.remove();
		assertEquals(0, m.size());
		assertEquals(keys, t);
	}

	@Test
	public void testTrim() {
		Int2IntOpenHashMap s = new Int2IntOpenHashMap(100, .75f);
		s.trim(0);
		assertEquals(1, s.n);

		s = new Int2IntOpenHashMap(100, .75f);
		s.trim(10);
		assertEquals(16, s.n);
		s.trim(20);
		assertEquals(16, s.n);

		s = new Int2IntOpenHashMap(6, .75f);
		assertEquals(8, s.n);
		for(int i = 0; i < 6; i++) s.put(i, i);
		assertEquals(8, s.n);
		s.trim(2);
		assertEquals(8, s.n);
	}

	@Test
	public void testLegacyMainMethodTests() throws Exception {
		MainRunner.callMainIfExists(Int2IntOpenHashMap.class, "test", /*num=*/"500", /*loadFactor=*/"0.75", /*seed=*/"383454");
	}

	@Test
	public void testForEachRemaining() {
		// This set of extremely contorted parameters is necessary to trigger the usage of wrapped
		final Int2IntOpenHashMap m = new Int2IntOpenHashMap(0, .99f);
		m.put(1, 1);
		m.int2IntEntrySet().fastIterator().forEachRemaining(x -> {
		});
		m.put(0, 0);
		m.int2IntEntrySet().fastIterator().forEachRemaining(x -> {
		});

		for (int i = 2; i < 1000; i++) m.put(i, i);

		final ObjectIterator<Entry> it = m.int2IntEntrySet().fastIterator();
		for (int i = 1; i < 990; i++) {
			it.next();
			it.remove();
		}

		it.forEachRemaining(x -> {
		});
	}

	@Test
	public void testForEach() {
		final Int2IntOpenHashMap s = new Int2IntOpenHashMap();
		for (int i = 0; i < 100; i++) s.put(i, i);
		final int[] c = new int[1];
		s.forEach((x, y) -> c[0] += x.intValue());
		assertEquals((100 * 99) / 2, c[0]);
	}

}
