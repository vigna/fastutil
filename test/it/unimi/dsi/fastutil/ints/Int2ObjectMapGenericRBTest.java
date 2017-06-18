package it.unimi.dsi.fastutil.ints;

import java.util.Collections;
import java.util.EnumSet;
import java.util.function.Supplier;
import org.junit.runners.Parameterized.Parameters;

public class Int2ObjectMapGenericRBTest extends Int2ObjectMapGenericTest<Int2ObjectRBTreeMap<Integer>> {
	@Parameters
	public static Iterable<Object[]> data() {
		return Collections.singletonList(new Object[] {(Supplier<Int2ObjectRBTreeMap<Integer>>) Int2ObjectRBTreeMap::new, EnumSet.allOf(Capability.class)});
	}
}