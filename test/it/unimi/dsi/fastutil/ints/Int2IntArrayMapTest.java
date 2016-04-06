package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.unimi.dsi.fastutil.io.BinIO;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.AbstractMap;
import java.util.Map.Entry;

import org.junit.Test;

public class Int2IntArrayMapTest  {

	@Test
	public void testRemove() {
		Int2IntMap map = new Int2IntArrayMap();
		assertFalse( map.entrySet().remove( new Object() ) );
		map.put( 1, 2 );
		map.put( 2, 3 );
		assertFalse( map.entrySet().remove( new AbstractInt2IntMap.BasicEntry( 1, 1 ) ) );
		assertFalse( map.entrySet().remove( new AbstractInt2IntMap.BasicEntry( 3, 2 ) ) );
		assertTrue( map.entrySet().remove( new AbstractInt2IntMap.BasicEntry( 1, 2 ) ) );
		assertFalse( map.entrySet().contains( new AbstractInt2IntMap.BasicEntry( 1, 2 ) ) );
		assertEquals( map.size(), map.entrySet().size() );
		assertFalse( map.containsKey( 1 ) );
	}

	@Test(expected = IllegalStateException.class)
	public void testArrayMapEmptyEntrySetThrowsExceptionOnIteratorRemove() {
		ObjectIterator<Entry<Integer, Integer>> iterator = new Int2IntArrayMap( 4 ).entrySet().iterator();
		assertFalse( iterator.hasNext() );
		iterator.remove();
	}

	@Test(expected = IllegalStateException.class)
	public void testArrayMapEmptyEntrySetThrowsExceptionTwoIteratorRemoves() {
		Int2IntArrayMap m = new Int2IntArrayMap();
		m.put( 0, 0 );
		m.put( 1, 1 );
		ObjectIterator<Entry<Integer, Integer>> iterator = m.entrySet().iterator();
		iterator.next();
		iterator.remove();
		iterator.remove();
	}

	@Test(expected = IllegalStateException.class)
	public void testArrayMapEmptyEntrySetThrowsExceptionOnFastIteratorRemove() {
		ObjectIterator<it.unimi.dsi.fastutil.ints.Int2IntMap.Entry> iterator = new Int2IntArrayMap().int2IntEntrySet().fastIterator();
		assertFalse( iterator.hasNext() );
		iterator.remove();
	}

	@Test(expected = IllegalStateException.class)
	public void testArrayMapEmptyEntrySetThrowsExceptionTwoFastIteratorRemoves() {
		Int2IntArrayMap m = new Int2IntArrayMap();
		m.put( 0, 0 );
		m.put( 1, 1 );
		ObjectIterator<it.unimi.dsi.fastutil.ints.Int2IntMap.Entry> iterator = m.int2IntEntrySet().fastIterator();
		iterator.next();
		iterator.remove();
		iterator.remove();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testContainsNull() {
		Int2IntArrayMap m = new Int2IntArrayMap( new int[] { 1, 2, 3 },  new int[] { 1, 2, 3 } );
		assertFalse( m.containsKey( null ) );
		assertTrue( m.get( null ) == null );
	}

	@SuppressWarnings("boxing")
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

		Int2IntArrayMap m = new Int2IntArrayMap( new int[] { 1, 2 },  new int[] { 1, 2 } );
		assertFalse( m.equals( new Object2ObjectOpenHashMap<Integer,Integer>( new Integer[] { 1, null }, new Integer[] { 1, 1 } ) ) );
	}

	@SuppressWarnings("deprecation")
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

	@Test
	public void testIteratorRemove() {
		Int2IntArrayMap m = new Int2IntArrayMap( new int[] { 1, 2, 3 },  new int[] { 1, 2, 3 } );
		ObjectIterator<Entry<Integer, Integer>> keySet = m.entrySet().iterator();
		keySet.next();
		keySet.next();
		keySet.remove();
		assertTrue( keySet.hasNext() );
		Entry<Integer, Integer> next = keySet.next();
		assertEquals( Integer.valueOf( 3 ), next.getKey() );
		assertEquals( Integer.valueOf( 3 ), next.getValue() );
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void entrySetContainsTest() {
		Int2IntArrayMap m = new Int2IntArrayMap();
		m.put(0, 0);
		assertFalse(m.int2IntEntrySet().contains(new AbstractMap.SimpleEntry(new Object(), null)));
		assertFalse(m.entrySet().contains(new AbstractMap.SimpleEntry(null, new Object())));
		assertFalse(m.entrySet().contains(new AbstractMap.SimpleEntry(null, null)));
		assertFalse(m.entrySet().contains(new AbstractMap.SimpleEntry(new Object(), new Object())));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void entrySetRemoveTest() {
		Int2IntArrayMap m = new Int2IntArrayMap();
		m.put(0, 0);
		assertFalse(m.entrySet().remove(new AbstractMap.SimpleEntry(new Object(), null)));
		assertFalse(m.entrySet().remove(new AbstractMap.SimpleEntry(null, new Object())));
		assertFalse(m.entrySet().remove(new AbstractMap.SimpleEntry(null, null)));
		assertFalse(m.entrySet().remove(new AbstractMap.SimpleEntry(new Object(), new Object())));
	}
}
