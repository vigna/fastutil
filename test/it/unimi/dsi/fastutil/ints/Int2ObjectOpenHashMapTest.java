package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Assert;

import org.junit.Test;

@SuppressWarnings("rawtypes")
public class Int2ObjectOpenHashMapTest {
	private static Int2ObjectOpenHashMap<Integer> original = new Int2ObjectOpenHashMap<Integer>();
	static {
		original.put( 0, Integer.valueOf( 30303000 ) ); // slot 0
		original.put( -152, Integer.valueOf( 31313100 ) ); // slot 31
		original.put( -1520082082, Integer.valueOf( 42 ) ); // slot 31 -> 1
		original.put( 20, Integer.valueOf( 30303001 ) ); // slot 1 -> 2
	}

	public void print( Int2ObjectOpenHashMap<Integer> cache ) {
		System.err.println( "\nYou should get the following entries in no particular order:\n" + cache + "\n" );
	}

	/**
	 * a hash collision (hashcode & mask) at the very end of the underlying array inducing a wrap
	 * around the array and causing a gap of unused entries from index 3 to 30 the cursor of the
	 * element which should be returned next will already be set to index 2
	 */

	@Test
	public void twiceDoubleCollision() {
		Int2ObjectOpenHashMap<Integer> cache = new Int2ObjectOpenHashMap<Integer>();
		cache.put( 0, Integer.valueOf( 30303000 ) ); // slot 0
		cache.put( -152, Integer.valueOf( 31313100 ) ); // slot 31
		cache.put( -1520082082, Integer.valueOf( 42 ) ); // slot 31 -> 1
		cache.put( 20, Integer.valueOf( 30303001 ) ); // slot 1 -> 2
		print( cache );
		handleCacheIterator( cache.int2ObjectEntrySet().fastIterator() );
	}



	/**
	 * a hash collision (hashcode & mask) at the very end of the underlying array inducing a wrap
	 * around the array and causing a gap of unused entries from index 2 to 30 the cursor of the
	 * element which should be returned next will already be set to index 1
	 * 
	 * Three entries are added that should be put at the same position (index 31), ending at 31,0,1
	 */
	@Test
	public void tripleCollision() {
		Int2ObjectOpenHashMap<Integer> cache = new Int2ObjectOpenHashMap<Integer>();
		cache.put( -152, Integer.valueOf( 1 ) ); // slot 31
		cache.put( -1520082082, Integer.valueOf( 42 ) ); // slot 31 -> 0
		cache.put( 256740984, Integer.valueOf( 666 ) ); // slot 31 ->1
		print( cache );
		handleCacheIterator( cache.int2ObjectEntrySet().fastIterator() );
	}

	/**
	 * a hash collision (hashcode & mask) in the middle of the underlying array Works fine in
	 * fastutil 6.3 already, just added for the sake of completeness
	 */
	@Test
	public void normalCollision() {
		Int2ObjectOpenHashMap<Integer> cache = new Int2ObjectOpenHashMap<Integer>();
		cache.put( 19, Integer.valueOf( 1111 ) ); // slot 20
		cache.put( 55, Integer.valueOf( 2222 ) ); // slot 20 ->21
		print( cache );
		handleCacheIterator( cache.int2ObjectEntrySet().fastIterator() );
	}

	public void handleCacheIterator( ObjectIterator<it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry<Integer>> iterator ) {

		final List<Integer> finishedEntries = new ArrayList<Integer>();
		final List<Integer> finishedEntriesAfterRemove = new ArrayList<Integer>();
		final List<Integer> finishedEntryKey = new ArrayList<Integer>();
		final List<Integer> finishedEntryKeyAfter = new ArrayList<Integer>();
		int i = 0;
		while ( iterator.hasNext() ) {
			it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry<Integer> next = iterator.next();
			System.err.println( "element returned at position " + ( i++ ) + ": " + next );
			final Integer value = next.getValue();
			Assert.assertNotNull( value );
			finishedEntryKey.add( next.getKey() );
			iterator.remove();
			finishedEntryKeyAfter.add( next.getKey() );
			finishedEntries.add( value );
			finishedEntriesAfterRemove.add( next.getValue() );

		}
		System.err.println( "\nkeys if retrieved before remove():\n" + finishedEntryKey );
		System.err.println( "and the values :\n" + finishedEntries );
		System.err.println( "\nkeys if retrieved after remove():\n" + finishedEntryKeyAfter );
		System.err.println( "and the values :\n" + finishedEntriesAfterRemove );
	}
	
	@Test(expected=ArrayIndexOutOfBoundsException.class)
	public void testRemovedEntry() {
		final Int2ObjectOpenHashMap<Integer> map = new Int2ObjectOpenHashMap<Integer>();
		map.put( 1, Integer.valueOf( 0 ) );
		map.put( 2, Integer.valueOf( 1 ) );
		final ObjectIterator<Entry<Integer, Integer>> iterator = map.entrySet().iterator();
		final Entry e = iterator.next();
		iterator.remove();
		e.getKey();
	}
}
