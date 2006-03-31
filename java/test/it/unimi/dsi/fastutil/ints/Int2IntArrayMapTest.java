package test.it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.ints.AbstractInt2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

import java.util.Map.Entry;

import junit.framework.TestCase;

public class Int2IntArrayMapTest extends TestCase {
	
	public void testMap() {
		for( int i = 0; i <= 2; i++ ) {
			Int2IntArrayMap m = i == 0 ? new Int2IntArrayMap() : new Int2IntArrayMap( new int[ i ], new int[ i ] );
			assertEquals( 0, m.put( 1, 1 ) );
			assertEquals( 1, m.size() );
			assertTrue( m.containsKey( 1 ) );
			assertTrue( m.containsValue( 1 ) );
			assertEquals( 0, m.put(  2, 2  ) );
			assertTrue( m.containsKey( 2 ) );
			assertTrue( m.containsValue( 2 ) );
			assertEquals( 2, m.size() );
			assertEquals( 1, m.put( 1, 3 ) );
			assertTrue( m.containsValue( 3 ) );
			assertEquals( 0, m.remove( 3 ) );
			assertEquals( 0, m.put(  3, 3  ) );
			assertTrue( m.containsKey( 3 ) );
			assertTrue( m.containsValue( 3 ) );
			assertEquals( 3, m.size() );
			assertEquals( 3, m.get( 1 ) );
			assertEquals( 2, m.get( 2 ) );
			assertEquals( 3, m.get( 3 ) );
			assertEquals( new IntOpenHashSet( new int[] { 1, 2, 3 } ), new IntOpenHashSet( m.keySet().iterator() ) );
			assertEquals( new IntOpenHashSet( new int[] { 3, 2, 3 } ), new IntOpenHashSet( m.values().iterator() ) );

			for( Entry<Integer, Integer> e: m.entrySet() ) assertEquals( e.getValue(), m.get( e.getKey() ) );

			assertTrue( m.entrySet().contains( new AbstractInt2IntMap.BasicEntry( 1, 3 ) ) );
			assertTrue( m.entrySet().contains( new AbstractInt2IntMap.BasicEntry( 2, 2 ) ) );
			assertTrue( m.entrySet().contains( new AbstractInt2IntMap.BasicEntry( 3, 3 ) ) );
			assertFalse( m.entrySet().contains( new AbstractInt2IntMap.BasicEntry( 1, 2 ) ) );
			assertFalse( m.entrySet().contains( new AbstractInt2IntMap.BasicEntry( 2, 1 ) ) );

			assertEquals( 3, m.remove( 3 ) );
			assertEquals( 2, m.size() );
			assertEquals( 3, m.remove( 1 ) );
			assertEquals( 1, m.size() );
			assertFalse( m.containsKey( 1 ) );
			assertEquals( 2, m.remove( 2 ) );
			assertEquals( 0, m.size() );
			assertFalse( m.containsKey( 1 ) );
		}
	}
}
