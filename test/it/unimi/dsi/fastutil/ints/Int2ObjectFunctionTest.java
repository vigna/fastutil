package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.*;

import org.junit.Test;

@SuppressWarnings("rawtypes")
public class Int2ObjectFunctionTest {

	@SuppressWarnings("deprecation")
	@Test
	public void testDefaultGenericMethods() {
		final Integer zero = Integer.valueOf(0);

		@SuppressWarnings("serial")
		final Int2ObjectFunction<Object> f = new AbstractInt2ObjectFunction<Object>() {
			@Override
			public Object get(int key) {
				return key == 0 ? zero : defRetValue;
			}
		};

		final Object drv = new Object();
		f.defaultReturnValue(drv);
		assertSame(Integer.valueOf(0), f.get(0));
		assertSame(drv, f.get(1));
		assertSame(drv, f.get(Integer.valueOf(1)));
		assertSame(zero, f.get(0));
		assertSame(zero, f.get(Integer.valueOf(0)));
	}
}
