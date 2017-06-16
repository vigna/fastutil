package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertEquals;

import it.unimi.dsi.fastutil.Hash;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.function.Supplier;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

public class Int2IntMapGenericOpenHashTest extends Int2IntMapGenericTest {
	@Parameter(2)
	public float loadFactor;

	@SuppressWarnings("AutoBoxing")
	@Parameters(name = "{index}: lf {2}")
	public static Iterable<Object[]> data() {
		final EnumSet<Capability> capabilities = EnumSet.allOf(Capability.class);
		final int defSize = Int2IntOpenHashMap.DEFAULT_INITIAL_SIZE;
		final Collection<Object[]> data = new ArrayList<>();
		for (final float loadFactor : Arrays.asList(Hash.DEFAULT_LOAD_FACTOR, Hash.FAST_LOAD_FACTOR, Hash.VERY_FAST_LOAD_FACTOR)) {
			data.add(new Object[] {supplier(defSize, loadFactor), capabilities, loadFactor});
		}
		return data;
	}

	private static Supplier<Int2IntMap> supplier(final int defSize, final float loadFactor) {
		return () -> new Int2IntOpenHashMap(defSize, loadFactor);
	}

	@Test
	public void testAddTo() {
		final Int2IntOpenHashMap map = (Int2IntOpenHashMap) m;
		assertEquals(0, map.addTo(0, 2));
		assertEquals(2, map.get(0));
		assertEquals(2, map.addTo(0, 3));
		assertEquals(5, map.get(0));
		map.defaultReturnValue(-1);
		assertEquals(-1, map.addTo(1, 1));
		assertEquals(0, map.get(1));
		assertEquals(0, map.addTo(1, 1));
		assertEquals(1, map.get(1));
		assertEquals(1, map.addTo(1, -2));
		assertEquals(-1, map.get(1));
	}
}