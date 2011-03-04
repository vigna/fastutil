package it.unimi.dsi.fastutil.objects;

import static org.junit.Assert.*;

import org.junit.Test;

public class AbstractObject2IntFunctionTest {
	@Test
	public void testRemove() {
		final Object2IntArrayMap<Object> a = new Object2IntArrayMap<Object>();
		final Object key = new Object();
		a.put( key, 1 );
		assertEquals( Integer.valueOf( 1 ), a.remove( key ) );
	}
}

