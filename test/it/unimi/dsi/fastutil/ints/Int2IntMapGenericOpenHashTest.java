package it.unimi.dsi.fastutil.ints;

import org.junit.Before;

public class Int2IntMapGenericOpenHashTest extends Int2IntMapGenericTest {
	@Before
	public void setUp() {
		m = new Int2IntLinkedOpenHashMap();
	}
}