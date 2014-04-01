package it.unimi.dsi.fastutil.floats;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FloatOpenHashSetTest {
	
	@Test
	public void testNaNs() {
		FloatOpenHashSet s = new FloatOpenHashSet();
		s.add( Float.NaN );
		s.add( Float.NaN );
		assertEquals( 1, s.size() );
	}

}
