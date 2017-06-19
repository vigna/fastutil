package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Hash;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.function.Supplier;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

public class Int2ObjectMapGenericOpenHashTest extends Int2ObjectMapGenericTest<Int2ObjectOpenHashMap<Integer>> {
	@Parameter(2)
	public float loadFactor;

	@SuppressWarnings({"AutoBoxing", "boxing"})
	@Parameters(name = "{index}: lf {2}")
	public static Iterable<Object[]> data() {
		final EnumSet<Capability> capabilities = EnumSet.allOf(Capability.class);
		final int defSize = Int2ObjectOpenHashMap.DEFAULT_INITIAL_SIZE;
		final Collection<Object[]> data = new ArrayList<>();
		for (final float loadFactor : new float[] {Hash.DEFAULT_LOAD_FACTOR, Hash.FAST_LOAD_FACTOR, Hash.VERY_FAST_LOAD_FACTOR}) {
			data.add(new Object[] {(Supplier<Int2ObjectMap<Integer>>) () -> new Int2ObjectOpenHashMap<>(defSize, loadFactor), capabilities, loadFactor});
		}
		return data;
	}
}