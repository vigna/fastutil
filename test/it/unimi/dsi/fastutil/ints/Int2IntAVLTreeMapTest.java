package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.AbstractMap;

import org.junit.Test;

public class Int2IntAVLTreeMapTest {

	@SuppressWarnings("deprecation")
	@Test
	public void testContainsNull() {
		Int2IntAVLTreeMap m = new Int2IntAVLTreeMap( new int[] { 1, 2, 3 },  new int[] { 1, 2, 3 } );
		assertFalse( m.containsKey( null ) );
		assertTrue( m.get( null ) == null );
	}

	@SuppressWarnings("boxing")
	@Test
	public void testEquals() {
		Int2IntAVLTreeMap m = new Int2IntAVLTreeMap( new int[] { 1, 2 },  new int[] { 1, 2 } );
		assertFalse( m.equals( new Object2ObjectOpenHashMap<Integer,Integer>( new Integer[] { 1, null }, new Integer[] { 1, 1 } ) ) );
	}	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void entrySetContainsTest() {
		Int2IntAVLTreeMap m = new Int2IntAVLTreeMap();
		m.put(0, 0);
		assertFalse(m.int2IntEntrySet().contains(new AbstractMap.SimpleEntry(new Object(), null)));
		assertFalse(m.entrySet().contains(new AbstractMap.SimpleEntry(null, new Object())));
		assertFalse(m.entrySet().contains(new AbstractMap.SimpleEntry(null, null)));
		assertFalse(m.entrySet().contains(new AbstractMap.SimpleEntry(new Object(), new Object())));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void entrySetRemoveTest() {
		Int2IntAVLTreeMap m = new Int2IntAVLTreeMap();
		m.put(0, 0);
		assertFalse(m.entrySet().remove(new AbstractMap.SimpleEntry(new Object(), null)));
		assertFalse(m.entrySet().remove(new AbstractMap.SimpleEntry(null, new Object())));
		assertFalse(m.entrySet().remove(new AbstractMap.SimpleEntry(null, null)));
		assertFalse(m.entrySet().remove(new AbstractMap.SimpleEntry(new Object(), new Object())));
	}

	@Test
	public void removeFromKeySetTest() {
		Int2IntAVLTreeMap m = new Int2IntAVLTreeMap();
		m.put(0, 0);
		assertTrue(m.keySet().remove(0));
	}
}
