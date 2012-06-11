package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.unimi.dsi.fastutil.io.BinIO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map.Entry;

import org.junit.Test;

public class Int2IntArrayMapTest  {


	@Test
	public void testEquals() {
		Int2IntArrayMap a1 = new Int2IntArrayMap();
		a1.put(0,  1);
		a1.put(1000, -1);
		a1.put(2000, 3);

		Int2IntArrayMap a2 = new Int2IntArrayMap();
		a2.put(0,  1);
		a2.put(1000, -1);
		a2.put(2000, 3);

		assertEquals(a1, a2);

	}

	@Test
	public void testMap() {
		for( int i = 0; i <= 1; i++ ) {
			Int2IntArrayMap m = i == 0 ? new Int2IntArrayMap() : new Int2IntArrayMap( new int[ i ], new int[ i ] );
			assertEquals( 0, m.put( 1, 1 ) );
			assertEquals( 1 + i, m.size() );
			assertTrue( m.containsKey( 1 ) );
			assertTrue( m.containsValue( 1 ) );
			assertEquals( 0, m.put(  2, 2  ) );
			assertTrue( m.containsKey( 2 ) );
			assertTrue( m.containsValue( 2 ) );
			assertEquals( 2 + i, m.size() );
			assertEquals( 1, m.put( 1, 3 ) );
			assertTrue( m.containsValue( 3 ) );
			assertEquals( 0, m.remove( 3 ) );
			assertEquals( 0, m.put(  3, 3  ) );
			assertTrue( m.containsKey( 3 ) );
			assertTrue( m.containsValue( 3 ) );
			assertEquals( 3 + i, m.size() );
			assertEquals( 3, m.get( 1 ) );
			assertEquals( 2, m.get( 2 ) );
			assertEquals( 3, m.get( 3 ) );
			assertEquals( new IntOpenHashSet( i == 0 ? new int[] { 1, 2, 3 } : new int[] { 0, 1, 2, 3 } ), new IntOpenHashSet( m.keySet().iterator() ) );
			assertEquals( new IntOpenHashSet( i == 0 ? new int[] { 3, 2, 3 } : new int[] { 0, 3, 2, 3 } ), new IntOpenHashSet( m.values().iterator() ) );

			for( Entry<Integer, Integer> e: m.entrySet() ) assertEquals( e.getValue(), m.get( e.getKey() ) );

			assertTrue( i != 0 == m.entrySet().contains( new AbstractInt2IntMap.BasicEntry( 0, 0 ) ) );
			assertTrue( m.entrySet().contains( new AbstractInt2IntMap.BasicEntry( 1, 3 ) ) );
			assertTrue( m.entrySet().contains( new AbstractInt2IntMap.BasicEntry( 2, 2 ) ) );
			assertTrue( m.entrySet().contains( new AbstractInt2IntMap.BasicEntry( 3, 3 ) ) );
			assertFalse( m.entrySet().contains( new AbstractInt2IntMap.BasicEntry( 1, 2 ) ) );
			assertFalse( m.entrySet().contains( new AbstractInt2IntMap.BasicEntry( 2, 1 ) ) );

			assertEquals( 3, m.remove( 3 ) );
			assertEquals( 2 + i, m.size() );
			assertEquals( 3, m.remove( 1 ) );
			assertEquals( 1 + i, m.size() );
			assertFalse( m.containsKey( 1 ) );
			assertEquals( 2, m.remove( 2 ) );
			assertEquals( 0 + i, m.size() );
			assertFalse( m.containsKey( 1 ) );
		}
	}
	
	@Test
	public void testClone() {
		Int2IntArrayMap m = new Int2IntArrayMap();
		assertEquals( m, m.clone() );
		m.put( 0, 1 );
		assertEquals( m, m.clone() );
		m.put( 0, 2 );
		assertEquals( m, m.clone() );
		m.put( 1, 2 );
		assertEquals( m, m.clone() );
		m.remove( 1 );
		assertEquals( m, m.clone() );
	}

	@Test
	public void testSerialisation() throws IOException, ClassNotFoundException {
		Int2IntArrayMap m = new Int2IntArrayMap();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream( baos );
		oos.writeObject( m );
		oos.close();
		assertEquals( m, BinIO.loadObject( new ByteArrayInputStream( baos.toByteArray() ) ) );
		
		m.put( 0, 1 );
		m.put( 1, 2 );

		baos.reset();
		oos = new ObjectOutputStream( baos );
		oos.writeObject( m );
		oos.close();
		assertEquals( m, BinIO.loadObject( new ByteArrayInputStream( baos.toByteArray() ) ) );
	}
}
