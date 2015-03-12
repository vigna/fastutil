package it.unimi.dsi.fastutil.doubles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DoubleOpenHashSetTest {
	
	@Test
	public void testNaNs() {
		DoubleOpenHashSet s = new DoubleOpenHashSet();
		s.add( Double.NaN );
		s.add( Double.NaN );
		assertEquals( 1, s.size() );
	}

	@Test
	public void testZeros() {
		DoubleOpenHashSet s = new DoubleOpenHashSet();
		assertTrue( s.add( -0.0d ) );
		assertTrue( s.add( +0.0d ) );
		assertEquals( 2, s.size() );
	}

}
