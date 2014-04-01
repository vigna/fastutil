package it.unimi.dsi.fastutil.doubles;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DoubleOpenHashSetTest {
	
	@Test
	public void testNaNs() {
		DoubleOpenHashSet s = new DoubleOpenHashSet();
		s.add( Double.NaN );
		s.add( Double.NaN );
		assertEquals( 1, s.size() );
	}

}
