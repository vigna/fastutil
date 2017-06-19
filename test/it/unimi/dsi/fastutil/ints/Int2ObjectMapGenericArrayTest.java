package it.unimi.dsi.fastutil.ints;

import java.util.Collections;
import java.util.EnumSet;
import java.util.function.Supplier;
import org.junit.runners.Parameterized.Parameters;

public class Int2ObjectMapGenericArrayTest extends Int2ObjectMapGenericTest<Int2ObjectArrayMap<Integer>> {
	@Parameters
	public static Iterable<Object[]> data() {
		final EnumSet<Capability> capabilities = EnumSet.allOf(Capability.class);
		capabilities.remove(Capability.ITERATOR_MODIFY);
		return Collections.singletonList(new Object[] { (Supplier<Int2ObjectArrayMap<Integer>>) Int2ObjectArrayMap::new, capabilities });
	}
}