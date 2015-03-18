package it.unimi.dsi.fastutil.longs;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;

import java.util.Arrays;

import org.junit.Test;

public class LongOpenHashBigSetTest {

	@Test
	public void testRemove0() {
		LongOpenHashBigSet s = new LongOpenHashBigSet( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = -1; i <= 1; i++ ) assertTrue( s.add( i ) );
		assertTrue( s.remove( 0 ) );
		LongIterator iterator = s.iterator();
		LongOpenHashSet z = new LongOpenHashSet();
		z.add( iterator.nextLong() );
		z.add( iterator.nextLong() );
		assertFalse( iterator.hasNext() );
		assertEquals( new LongOpenHashSet( new long[] { -1, 1 } ), z );
		
		s = new LongOpenHashBigSet( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = -1; i <= 1; i++ ) assertTrue( s.add( i ) );
		iterator = s.iterator();
		while( iterator.hasNext() ) if ( iterator.nextLong() == 0 ) iterator.remove();
		
		assertFalse( s.contains( 0 ) );
		
		iterator = s.iterator();
		long[] content = new long[ 2 ];
		content[ 0 ] = iterator.nextLong();
		content[ 1 ] = iterator.nextLong();
		assertFalse( iterator.hasNext() );
		Arrays.sort( content );
		assertArrayEquals( new long[] { -1, 1 }, content );
	}
	
	@Test
	public void testWrapAround() {
		LongOpenHashBigSet s = new LongOpenHashBigSet( 4, .5f );
		assertEquals( 8, s.n );
		// The following code inverts HashCommon.phiMix() and places strategically keys in slots 6, 7 and 0
		s.add( HashCommon.invMix( 6L ) );
		s.add( HashCommon.invMix( 7L ) );
		s.add( HashCommon.invMix( 6L + 8 ) );
		assertNotEquals( 0, s.key[ 0 ][ 0 ] );
		assertNotEquals( 0, s.key[ 0 ][ 6 ] );
		assertNotEquals( 0, s.key[ 0 ][ 7 ] );
		LongOpenHashBigSet keys = s.clone();
		LongIterator iterator = s.iterator();
		LongOpenHashBigSet t = new LongOpenHashBigSet();
		t.add( iterator.nextLong() );
		t.add( iterator.nextLong() );
		// Originally, this remove would move the entry in slot 0 in slot 6 and we would return the entry in 0 twice
		iterator.remove(); 
		t.add( iterator.nextLong() );
		assertEquals( keys, t );
	}	

	@Test
	public void testWrapAround2() {
		LongOpenHashBigSet s = new LongOpenHashBigSet( 4, .75f );
		assertEquals( 8, s.n );
		// The following code inverts HashCommon.phiMix() and places strategically keys in slots 4, 5, 6, 7 and 0
		s.add( HashCommon.invMix( 4L ) );
		s.add( HashCommon.invMix( 5L ) );
		s.add( HashCommon.invMix( 4L + 8 ) );
		s.add( HashCommon.invMix( 5L + 8 ) );
		s.add( HashCommon.invMix( 4L + 16 ) );
		assertNotEquals( 0, s.key[ 0 ][ 0 ] );
		assertNotEquals( 0, s.key[ 0 ][ 4 ] );
		assertNotEquals( 0, s.key[ 0 ][ 5 ] );
		assertNotEquals( 0, s.key[ 0 ][ 6 ] );
		assertNotEquals( 0, s.key[ 0 ][ 7 ] );
		//System.err.println(Arrays.toString( s.key[ 0 ] ));
		LongOpenHashBigSet keys = s.clone();
		LongIterator iterator = s.iterator();
		LongOpenHashBigSet t = new LongOpenHashBigSet();
		assertTrue( t.add( iterator.nextLong() ) );
		iterator.remove();
		//System.err.println(Arrays.toString( s.key[ 0 ] ));
		assertTrue( t.add( iterator.nextLong() ) );
		//System.err.println(Arrays.toString( s.key[ 0 ] ));
		// Originally, this remove would move the entry in slot 0 in slot 6 and we would return the entry in 0 twice
		assertTrue( t.add( iterator.nextLong() ) );
		//System.err.println(Arrays.toString( s.key[ 0 ] ));
		assertTrue( t.add( iterator.nextLong() ) );
		iterator.remove();
		//System.err.println(Arrays.toString( s.key[ 0 ] ));
		assertTrue( t.add( iterator.nextLong() ) );
		assertEquals( 3, s.size64() );
		assertEquals( keys, t );
	}	
	
	@Test
	public void testWrapAround3() {
		LongOpenHashBigSet s = new LongOpenHashBigSet( 4, .75f );
		assertEquals( 8, s.n );
		// The following code inverts HashCommon.phiMix() and places strategically keys in slots 5, 6, 7, 0 and 1
		s.add( HashCommon.invMix( 5L ) );
		s.add( HashCommon.invMix( 5L + 8 ) );
		s.add( HashCommon.invMix( 5L + 16 ) );
		s.add( HashCommon.invMix( 5L + 32 ) );
		s.add( HashCommon.invMix( 5L + 64 ) );
		assertNotEquals( 0, s.key[ 0 ][ 5 ] );
		assertNotEquals( 0, s.key[ 0 ][ 6 ] );
		assertNotEquals( 0, s.key[ 0 ][ 7 ] );
		assertNotEquals( 0, s.key[ 0 ][ 0 ] );
		assertNotEquals( 0, s.key[ 0 ][ 1 ] );
		//System.err.println(Arrays.toString( s.key[ 0 ] ));
		LongOpenHashBigSet keys = s.clone();
		LongIterator iterator = s.iterator();
		LongOpenHashBigSet t = new LongOpenHashBigSet();
		assertTrue( t.add( iterator.nextLong() ) );
		iterator.remove();
		//System.err.println(Arrays.toString( s.key[ 0 ] ));
		assertTrue( t.add( iterator.nextLong() ) );
		iterator.remove();
		//System.err.println(Arrays.toString( s.key[ 0 ] ));
		// Originally, this remove would move the entry in slot 0 in slot 6 and we would return the entry in 0 twice
		assertTrue( t.add( iterator.nextLong() ) );
		iterator.remove();
		//System.err.println(Arrays.toString( s.key[ 0 ] ));
		assertTrue( t.add( iterator.nextLong() ) );
		iterator.remove();
		//System.err.println(Arrays.toString( s.key[ 0 ] ));
		assertTrue( t.add( iterator.nextLong() ) );
		iterator.remove();
		assertEquals( 0, s.size64() );
		assertEquals( keys, t );
	}	
}
