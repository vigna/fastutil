package it.unimi.dsi.fastutil.objects;

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

public class Object2ObjectArrayMapTest  {


	@SuppressWarnings("boxing")
	@Test
	public void testContainsNull() {
		Object2ObjectArrayMap<Integer,Integer> m = new Object2ObjectArrayMap<Integer,Integer>( new Integer[] { 1, 2, 3 },  new Integer[] { 1, 2, 3 } );
		assertFalse( m.containsKey( null ) );
		assertTrue( m.get( null ) == null );
	}

	@SuppressWarnings("boxing")
	@Test
	public void testEquals() {
		Object2ObjectArrayMap<Integer,Integer> a1 = new Object2ObjectArrayMap<Integer,Integer>();
		a1.put(0,  1);
		a1.put(1000, -1);
		a1.put(2000, 3);

		Object2ObjectArrayMap<Integer,Integer> a2 = new Object2ObjectArrayMap<Integer,Integer>();
		a2.put(0,  1);
		a2.put(1000, -1);
		a2.put(2000, 3);

		assertEquals(a1, a2);

		Object2ObjectArrayMap<Integer,Integer> m = new Object2ObjectArrayMap<Integer,Integer>( new Integer[] { 1, 2 },  new Integer[] { 1, 2 } );
		assertFalse( m.equals( new Object2ObjectOpenHashMap<Integer,Integer>( new Integer[] { 1, null }, new Integer[] { 1, 1 } ) ) );
	}

	@SuppressWarnings({ "boxing" })
	@Test
	public void testMap() {
		for( int i = 0; i <= 1; i++ ) {
			Object2ObjectArrayMap<Integer,Integer> m = i == 0 ? new Object2ObjectArrayMap<Integer,Integer>() : new Object2ObjectArrayMap<Integer,Integer>( new Integer[] { 0 }, new Integer[] { 0 } );
			assertEquals( null, m.put( 1, 1 ) );
			assertEquals( 1 + i, m.size() );
			assertTrue( m.containsKey( 1 ) );
			assertTrue( m.containsValue( 1 ) );
			assertEquals( null, m.put(  2, 2  ) );
			assertTrue( m.containsKey( 2 ) );
			assertTrue( m.containsValue( 2 ) );
			assertEquals( 2 + i, m.size() );
			assertEquals(  Integer.valueOf( 1 ), m.put( 1, 3 ) );
			assertTrue( m.containsValue( 3 ) );
			assertEquals( null, m.remove( 3 ) );
			assertEquals( null, m.put(  3, 3  ) );
			assertTrue( m.containsKey( 3 ) );
			assertTrue( m.containsValue( 3 ) );
			assertEquals( 3 + i, m.size() );
			assertEquals( Integer.valueOf( 3 ), m.get( 1 ) );
			assertEquals( Integer.valueOf( 2 ), m.get( 2 ) );
			assertEquals( Integer.valueOf( 3 ), m.get( 3 ) );
			assertEquals( new ObjectOpenHashSet<Integer>( i == 0 ? new Integer[] { 1, 2, 3 } : new Integer[] { 0, 1, 2, 3 } ), new ObjectOpenHashSet<Integer>( m.keySet().iterator() ) );
			assertEquals( new ObjectOpenHashSet<Integer>( i == 0 ? new Integer[] { 3, 2, 3 } : new Integer[] { 0, 3, 2, 3 } ), new ObjectOpenHashSet<Integer>( m.values().iterator() ) );

			for( Entry<Integer, Integer> e: m.entrySet() ) assertEquals( e.getValue(), m.get( e.getKey() ) );

			assertTrue( i != 0 == m.entrySet().contains( new AbstractObject2ObjectMap.BasicEntry<Integer,Integer>( 0, 0 ) ) );
			assertTrue( m.entrySet().contains( new AbstractObject2ObjectMap.BasicEntry<Integer,Integer>( 1, 3 ) ) );
			assertTrue( m.entrySet().contains( new AbstractObject2ObjectMap.BasicEntry<Integer,Integer>( 2, 2 ) ) );
			assertTrue( m.entrySet().contains( new AbstractObject2ObjectMap.BasicEntry<Integer,Integer>( 3, 3 ) ) );
			assertFalse( m.entrySet().contains( new AbstractObject2ObjectMap.BasicEntry<Integer,Integer>( 1, 2 ) ) );
			assertFalse( m.entrySet().contains( new AbstractObject2ObjectMap.BasicEntry<Integer,Integer>( 2, 1 ) ) );

			assertEquals( Integer.valueOf( 3 ), m.remove( 3 ) );
			assertEquals( 2 + i, m.size() );
			assertEquals( Integer.valueOf( 3 ), m.remove( 1 ) );
			assertEquals( 1 + i, m.size() );
			assertFalse( m.containsKey( 1 ) );
			assertEquals( Integer.valueOf( 2 ), m.remove( 2 ) );
			assertEquals( 0 + i, m.size() );
			assertFalse( m.containsKey( 1 ) );
		}
	}
	
	@SuppressWarnings("boxing")
	@Test
	public void testClone() {
		Object2ObjectArrayMap<Integer,Integer> m = new Object2ObjectArrayMap<Integer,Integer>();
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

	@SuppressWarnings("boxing")
	@Test
	public void testSerialisation() throws IOException, ClassNotFoundException {
		Object2ObjectArrayMap<Integer,Integer> m = new Object2ObjectArrayMap<Integer,Integer>();
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
	
	@SuppressWarnings("boxing")
	@Test
	public void testIteratorRemove() {
		Object2ObjectArrayMap<Integer,Integer> m = new Object2ObjectArrayMap<Integer,Integer>( new Integer[] { 1, 2, 3 },  new Integer[] { 1, 2, 3 } );
		ObjectIterator<Entry<Integer, Integer>> keySet = m.entrySet().iterator();
		keySet.next();
		keySet.next();
		keySet.remove();
		assertTrue( keySet.hasNext() );
		Entry<Integer, Integer> next = keySet.next();
		assertEquals( Integer.valueOf( 3 ), next.getKey() );
		assertEquals( Integer.valueOf( 3 ), next.getValue() );
	}
}
