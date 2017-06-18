package it.unimi.dsi.fastutil.ints;

import java.util.Collections;
import java.util.EnumSet;
import java.util.function.Supplier;
import org.junit.runners.Parameterized.Parameters;

public class Int2ObjectMapGenericAVLTest extends Int2ObjectMapGenericTest<Int2ObjectAVLTreeMap<Integer>> {
	@Parameters
	public static Iterable<Object[]> data() {
		return Collections.singletonList(new Object[] {(Supplier<Int2ObjectAVLTreeMap<Integer>>) Int2ObjectAVLTreeMap::new, EnumSet.allOf(Capability.class)});
	}
}