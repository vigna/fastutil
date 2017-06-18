package it.unimi.dsi.fastutil.ints;

import java.util.Collections;
import java.util.EnumSet;
import java.util.function.Supplier;
import org.junit.runners.Parameterized.Parameters;

public class Int2IntMapGenericRBTest extends Int2IntMapGenericTest<Int2IntRBTreeMap> {
	@Parameters
	public static Iterable<Object[]> data() {
		return Collections.singletonList(new Object[] { (Supplier<Int2IntMap>) Int2IntRBTreeMap::new, EnumSet.allOf(Capability.class) });
	}
}