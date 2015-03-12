package it.unimi.dsi.fastutil.floats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FloatOpenHashSetTest {
	
	@Test
	public void testNaNs() {
		FloatOpenHashSet s = new FloatOpenHashSet();
		s.add( Float.NaN );
		s.add( Float.NaN );
		assertEquals( 1, s.size() );
	}

	@Test
	public void testZeros() {
		FloatOpenHashSet s = new FloatOpenHashSet();
		assertTrue( s.add( -0.0f ) );
		assertTrue( s.add( +0.0f ) );
		assertEquals( 2, s.size() );
	}
}
