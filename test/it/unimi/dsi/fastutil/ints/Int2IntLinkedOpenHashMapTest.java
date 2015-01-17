package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.ints.Int2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Ignore;
import org.junit.Test;

public class Int2IntLinkedOpenHashMapTest {

	private static java.util.Random r = new java.util.Random( 0 );

	private static int genKey() {
		return r.nextInt();
	}

	private static int genValue() {
		return r.nextInt();
	}

	private static boolean valEquals( Object o1, Object o2 ) {
		return o1 == null ? o2 == null : o1.equals( o2 );
	}

	@SuppressWarnings("unchecked")
	protected static void test( int n, float f ) throws IOException, ClassNotFoundException {
		Int2IntLinkedOpenHashMap m = new Int2IntLinkedOpenHashMap( Hash.DEFAULT_INITIAL_SIZE, f );
		Map<Integer,Integer> t = new java.util.LinkedHashMap<Integer,Integer>();
		/* First of all, we fill t with random data. */
		for ( int i = 0; i < n; i++ )
			t.put( ( Integer.valueOf( genKey() ) ), ( Integer.valueOf( genValue() ) ) );
		/* Now we add to m the same data */
		m.putAll( t );
		assertTrue( "Error: !m.equals(t) after insertion", m.equals( t ) );
		assertTrue( "Error: !t.equals(m) after insertion", t.equals( m ) );
		/* Now we check that m actually holds that data. */
		for ( java.util.Iterator<?> i = t.entrySet().iterator(); i.hasNext(); ) {
			java.util.Map.Entry<?, ?> e = (java.util.Map.Entry<?, ?>)i.next();
			assertTrue( "Error: m and t differ on an entry (" + e + ") after insertion (iterating on t)", valEquals( e.getValue(), m.get( e.getKey() ) ) );
		}
		/* Now we check that m actually holds that data, but iterating on m. */
		for ( java.util.Iterator<?> i = m.entrySet().iterator(); i.hasNext(); ) {
			java.util.Map.Entry<?, ?> e = (java.util.Map.Entry<?, ?>)i.next();
			assertTrue( "Error: m and t differ on an entry (" + e + ") after insertion (iterating on m)", valEquals( e.getValue(), t.get( e.getKey() ) ) );
		}
		/* Now we check that m actually holds the same keys. */
		for ( java.util.Iterator<Integer> i = t.keySet().iterator(); i.hasNext(); ) {
			Object o = i.next();
			assertTrue( "Error: m and t differ on a key (" + o + ") after insertion (iterating on t)", m.containsKey( o ) );
			assertTrue( "Error: m and t differ on a key (" + o + ", in keySet()) after insertion (iterating on t)", m.keySet().contains( o ) );
		}
		/* Now we check that m actually holds the same keys, but iterating on m. */
		for ( java.util.Iterator<?> i = m.keySet().iterator(); i.hasNext(); ) {
			Object o = i.next();
			assertTrue( "Error: m and t differ on a key after insertion (iterating on m)", t.containsKey( o ) );
			assertTrue( "Error: m and t differ on a key (in keySet()) after insertion (iterating on m)", t.keySet().contains( o ) );
		}
		/* Now we check that m actually hold the same values. */
		for ( java.util.Iterator<Integer> i = t.values().iterator(); i.hasNext(); ) {
			Object o = i.next();
			assertTrue( "Error: m and t differ on a value after insertion (iterating on t)", m.containsValue( o ) );
			assertTrue( "Error: m and t differ on a value (in values()) after insertion (iterating on t)", m.values().contains( o ) );
		}
		/* Now we check that m actually hold the same values, but iterating on m. */
		for ( java.util.Iterator<?> i = m.values().iterator(); i.hasNext(); ) {
			Object o = i.next();
			assertTrue( "Error: m and t differ on a value after insertion (iterating on m)", t.containsValue( o ) );
			assertTrue( "Error: m and t differ on a value (in values()) after insertion (iterating on m)", t.values().contains( o ) );
		}
		/*
		 * Now we check that inquiries about random data give the same answer in m and t. For m we
		 * use the polymorphic method.
		 */
		for ( int i = 0; i < n; i++ ) {
			int T = genKey();
			assertTrue( "Error: divergence in keys between t and m (polymorphic method)", m.containsKey( ( Integer.valueOf( T ) ) ) == t.containsKey( ( Integer.valueOf( T ) ) ) );
			assertFalse( "Error: divergence between t and m (polymorphic method)", ( m.get( T ) != ( 0 ) ) != ( ( t.get( ( Integer.valueOf( T ) ) ) == null ? ( 0 ) : ( ( ( t.get( ( Integer.valueOf( T ) ) ) ).intValue() ) ) ) != ( 0 ) ) || t.get( ( Integer.valueOf( T ) ) ) != null && !m.get( ( Integer.valueOf( T ) ) ).equals( t.get( ( Integer.valueOf( T ) ) ) ) );
		}
		/*
		 * Again, we check that inquiries about random data give the same answer in m and t, but for
		 * m we use the standard method.
		 */
		for ( int i = 0; i < n; i++ ) {
			int T = genKey();
			assertTrue( "Error: divergence between t and m (standard method)", valEquals( m.get( ( Integer.valueOf( T ) ) ), t.get( ( Integer.valueOf( T ) ) ) ) );
		}
		/* Now we put and remove random data in m and t, checking that the result is the same. */
		for ( int i = 0; i < 20 * n; i++ ) {
			int T = genKey();
			int U = genValue();
			assertTrue( "Error: divergence in put() between t and m", valEquals( m.put( ( Integer.valueOf( T ) ), ( Integer.valueOf( U ) ) ), t.put( ( Integer.valueOf( T ) ), ( Integer.valueOf( U ) ) ) ) );
			T = genKey();
			assertTrue( "Error: divergence in remove() between t and m", valEquals( m.remove( ( Integer.valueOf( T ) ) ), t.remove( ( Integer.valueOf( T ) ) ) ) );
		}
		assertTrue( "Error: !m.equals(t) after removal", m.equals( t ) );
		assertTrue( "Error: !t.equals(m) after removal", t.equals( m ) );
		/* Now we check that m actually holds the same data. */
		for ( java.util.Iterator<?> i = t.entrySet().iterator(); i.hasNext(); ) {
			java.util.Map.Entry<?, ?> e = (java.util.Map.Entry<?, ?>)i.next();
			assertTrue( "Error: m and t differ on an entry (" + e + ") after removal (iterating on t)", valEquals( e.getValue(), m.get( e.getKey() ) ) );
		}
		/* Now we check that m actually holds that data, but iterating on m. */
		for ( java.util.Iterator<?> i = m.entrySet().iterator(); i.hasNext(); ) {
			java.util.Map.Entry<?, ?> e = (java.util.Map.Entry<?, ?>)i.next();
			assertTrue( "Error: m and t differ on an entry (" + e + ") after removal (iterating on m)", valEquals( e.getValue(), t.get( e.getKey() ) ) );
		}
		/* Now we check that m actually holds the same keys. */
		for ( java.util.Iterator<Integer> i = t.keySet().iterator(); i.hasNext(); ) {
			Object o = i.next();
			assertTrue( "Error: m and t differ on a key (" + o + ") after removal (iterating on t)", m.containsKey( o ) );
			assertTrue( "Error: m and t differ on a key (" + o + ", in keySet()) after removal (iterating on t)", m.keySet().contains( o ) );
		}
		/* Now we check that m actually holds the same keys, but iterating on m. */
		for ( java.util.Iterator<?> i = m.keySet().iterator(); i.hasNext(); ) {
			Object o = i.next();
			assertTrue( "Error: m and t differ on a key after removal (iterating on m)", t.containsKey( o ) );
			assertTrue( "Error: m and t differ on a key (in keySet()) after removal (iterating on m)", t.keySet().contains( o ) );
		}
		/* Now we check that m actually hold the same values. */
		for ( java.util.Iterator<Integer> i = t.values().iterator(); i.hasNext(); ) {
			Object o = i.next();
			assertTrue( "Error: m and t differ on a value after removal (iterating on t)", m.containsValue( o ) );
			assertTrue( "Error: m and t differ on a value (in values()) after removal (iterating on t)", m.values().contains( o ) );
		}
		/* Now we check that m actually hold the same values, but iterating on m. */
		for ( java.util.Iterator<?> i = m.values().iterator(); i.hasNext(); ) {
			Object o = i.next();
			assertTrue( "Error: m and t differ on a value after removal (iterating on m)", t.containsValue( o ) );
			assertTrue( "Error: m and t differ on a value (in values()) after removal (iterating on m)", t.values().contains( o ) );
		}
		int h = m.hashCode();
		/* Now we save and read m. */
		java.io.File ff = new java.io.File( "it.unimi.dsi.fastutil.test" );
		java.io.OutputStream os = new java.io.FileOutputStream( ff );
		java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream( os );
		oos.writeObject( m );
		oos.close();
		java.io.InputStream is = new java.io.FileInputStream( ff );
		java.io.ObjectInputStream ois = new java.io.ObjectInputStream( is );
		m = (Int2IntLinkedOpenHashMap)ois.readObject();
		ois.close();
		ff.delete();
		assertEquals( "Error: hashCode() changed after save/read", h, m.hashCode() );

		assertEquals( "Error: clone()", m, m.clone() );
		/* Now we check that m actually holds that data. */
		for ( java.util.Iterator<Integer> i = t.keySet().iterator(); i.hasNext(); ) {
			Object o = i.next();
			assertTrue( "Error: m and t differ on an entry after save/read", valEquals( m.get( o ), t.get( o ) ) );
		}
		/* Now we put and remove random data in m and t, checking that the result is the same. */
		for ( int i = 0; i < 20 * n; i++ ) {
			int T = genKey();
			int U = genValue();
			assertTrue( "Error: divergence in put() between t and m after save/read", valEquals( m.put( ( Integer.valueOf( T ) ), ( Integer.valueOf( U ) ) ), t.put( ( Integer.valueOf( T ) ), ( Integer.valueOf( U ) ) ) ) );
			T = genKey();
			assertTrue( "Error: divergence in remove() between t and m after save/read", valEquals( m.remove( ( Integer.valueOf( T ) ) ), t.remove( ( Integer.valueOf( T ) ) ) ) );
		}
		assertTrue( "Error: !m.equals(t) after post-save/read removal", m.equals( t ) );
		assertTrue( "Error: !t.equals(m) after post-save/read removal", t.equals( m ) );
		/* Now we play with iterators. */
		{
			java.util.ListIterator<?> i, j;
			Object J = null;
			Map.Entry<Integer,Integer> E = null, F = null;
			i = (java.util.ListIterator<?>)m.entrySet().iterator();
			j = new java.util.LinkedList<Object>( t.entrySet() ).listIterator();
			for ( int k = 0; k < 2 * n; k++ ) {
				assertTrue( "Error: divergence in hasNext()" ,  i.hasNext() == j.hasNext() );
				assertTrue( "Error: divergence in hasPrevious()" ,  i.hasPrevious() == j.hasPrevious() );
				if ( r.nextFloat() < .8 && i.hasNext() ) {
					assertTrue( "Error: divergence in next()" ,  ( E = (java.util.Map.Entry<Integer, Integer>)i.next() ).getKey().equals( J = ( F = (Map.Entry<Integer, Integer>)j.next() ).getKey() ) );
					if ( r.nextFloat() < 0.3 ) {
						i.remove();
						j.remove();
						t.remove( J );
					}
					else if ( r.nextFloat() < 0.3 ) {
						Integer U = Integer.valueOf( genValue() );
						E.setValue( U );
						t.put( F.getKey(), U );
					}
				}
				else if ( r.nextFloat() < .2 && i.hasPrevious() ) {
					assertTrue( "Error: divergence in previous()" ,  ( E = (java.util.Map.Entry<Integer, Integer>)i.previous() ).getKey().equals( J = ( F = (Map.Entry<Integer, Integer>)j.previous() ).getKey() ) );
					if ( r.nextFloat() < 0.3 ) {
						i.remove();
						j.remove();
						t.remove( J );
					}
					else if ( r.nextFloat() < 0.3 ) {
						Integer U = Integer.valueOf( genValue() );
						E.setValue( U );
						t.put( F.getKey(), U );
					}
				}
				assertTrue( "Error: divergence in nextIndex()" ,  i.nextIndex() == j.nextIndex() );
				assertTrue( "Error: divergence in previousIndex()" ,  i.previousIndex() == j.previousIndex() );
			}
		}
		if ( t.size() > 0 ) {
			java.util.ListIterator<Integer> i, j;
			Object J = null;
			j = new java.util.LinkedList<Integer>( t.keySet() ).listIterator();
			int e = r.nextInt( t.size() );
			Object from;
			do
				from = j.next();
			while ( e-- != 0 );
			i = (java.util.ListIterator<Integer>)m.keySet().iterator( ( ( ( (Integer)( from ) ).intValue() ) ) );
			for ( int k = 0; k < 2 * n; k++ ) {
				assertTrue( "Error: divergence in hasNext() (iterator with starting point " + from + ")" ,  i.hasNext() == j.hasNext() );
				assertTrue( "Error: divergence in hasPrevious() (iterator with starting point " + from + ")" ,  i.hasPrevious() == j.hasPrevious() );
				if ( r.nextFloat() < .8 && i.hasNext() ) {
					assertTrue( "Error: divergence in next() (iterator with starting point " + from + ")" ,  i.next().equals( J = j.next() ) );
					if ( r.nextFloat() < 0.5 ) {
						i.remove();
						j.remove();
						t.remove( J );
					}
				}
				else if ( r.nextFloat() < .2 && i.hasPrevious() ) {
					assertTrue( "Error: divergence in previous() (iterator with starting point " + from + ")" ,  i.previous().equals( J = j.previous() ) );
					if ( r.nextFloat() < 0.5 ) {
						i.remove();
						j.remove();
						t.remove( J );
					}
				}
				assertTrue( "Error: divergence in nextIndex() (iterator with starting point " + from + ")" ,  i.nextIndex() == j.nextIndex() );
				assertTrue( "Error: divergence in previousIndex() (iterator with starting point " + from + ")" ,  i.previousIndex() == j.previousIndex() );
			}
		}
		/* Now we check that m actually holds that data. */
		assertTrue( "Error: ! m.equals( t ) after iteration" ,  m.equals( t ) );
		assertTrue( "Error: ! t.equals( m ) after iteration" ,  t.equals( m ) );
		/* Now we take out of m everything, and check that it is empty. */
		for ( java.util.Iterator<Integer> i = t.keySet().iterator(); i.hasNext(); )
			m.remove( i.next() );
		assertTrue( "Error: m is not empty (as it should be)", m.isEmpty() );
		/*
		 * Now we check that the iteration order of m is properly affected, using random movements
		 */
		{
			m.clear();
			final java.util.Deque<Integer> d = new java.util.ArrayDeque<Integer>();
			for ( int k = 0; k < 2 * n; k++ ) {
				int T = genKey();
				int U = genValue();
				boolean dr = d.remove( ( Integer.valueOf( T ) ) );
				int rU = m.put( T, U );
				assertTrue( "Error: deque reported previous key differently than map." ,  dr == ( m.defaultReturnValue() != rU ) );
				if ( 1 == ( r.nextInt( 2 ) % 2 ) ) {
					d.addFirst( ( Integer.valueOf( T ) ) );
					m.getAndMoveToFirst( T );
				}
				else {
					d.addLast( ( Integer.valueOf( T ) ) );
					m.getAndMoveToLast( T );
				}
			}
			// Iteration order should be identical
			assertTrue( "Error: Iteration order of map different than iteration order of deque." ,  new java.util.ArrayList<Object>( m.keySet() ).equals( new java.util.ArrayList<Integer>( d ) ) );
		}
		m.clear();
		t.clear();
		m.trim();
		assertTrue( "Error: !m.equals(t) after rehash()", m.equals( t ) );
		assertTrue( "Error: !t.equals(m) after rehash()", t.equals( m ) );
		m.trim();
		assertTrue( "Error: !m.equals(t) after trim()", m.equals( t ) );
		assertTrue( "Error: !t.equals(m) after trim()", t.equals( m ) );

		return;
	}

	@Test
	public void test1() throws IOException, ClassNotFoundException {
		test( 1, Hash.DEFAULT_LOAD_FACTOR );
		test( 1, Hash.FAST_LOAD_FACTOR );
		test( 1, Hash.VERY_FAST_LOAD_FACTOR );
	}

	@Test
	public void test10() throws IOException, ClassNotFoundException {
		test( 10, Hash.DEFAULT_LOAD_FACTOR );
		test( 10, Hash.FAST_LOAD_FACTOR );
		test( 10, Hash.VERY_FAST_LOAD_FACTOR );
	}

	@Test
	public void test100() throws IOException, ClassNotFoundException {
		test( 100, Hash.DEFAULT_LOAD_FACTOR );
		test( 100, Hash.FAST_LOAD_FACTOR );
		test( 100, Hash.VERY_FAST_LOAD_FACTOR );
	}

	@Ignore("Too long")
	@Test
	public void test1000() throws IOException, ClassNotFoundException {
		test( 1000, Hash.DEFAULT_LOAD_FACTOR );
		test( 1000, Hash.FAST_LOAD_FACTOR );
		test( 1000, Hash.VERY_FAST_LOAD_FACTOR );
	}

	@Test
	public void testAddTo() {
		Int2IntLinkedOpenHashMap m = new Int2IntLinkedOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
		assertEquals( 0, m.addTo( 0, 2 ) );
		assertEquals( 2, m.get( 0 ) );
		assertEquals( 2, m.addTo( 0, 3 ) );
		assertEquals( 5, m.get( 0 ) );
		ObjectIterator<Int2IntMap.Entry> fastIterator = m.int2IntEntrySet().fastIterator();
		Int2IntMap.Entry next = fastIterator.next();
		assertEquals( 0, next.getIntKey() );
		assertEquals( 5, next.getIntValue() );
		assertFalse( fastIterator.hasNext() );
		
		m.defaultReturnValue( -1 );
		assertEquals( -1, m.addTo( 1, 1 ) );
		assertEquals( 0, m.get( 1 ) );
		assertEquals( 0, m.addTo( 1, 1 ) );
		assertEquals( 1, m.get( 1 ) );
		assertEquals( 1, m.addTo( 1, -2 ) );
		assertEquals( -1, m.get( 1 ) );
		fastIterator = m.int2IntEntrySet().fastIterator();
		next = fastIterator.next();
		assertEquals( 0, next.getIntKey() );
		assertEquals( 5, next.getIntValue() );
		next = fastIterator.next();
		assertEquals( 1, next.getIntKey() );
		assertEquals( -1, next.getIntValue() );
		assertFalse( fastIterator.hasNext() );
		
		for( int i = 0; i < 100; i++ ) m.addTo( i, 1 );
		assertEquals( 0, m.firstIntKey() );
		assertEquals( 99, m.lastIntKey() );
	}

	@Test
	public void testPut() {
		Int2IntLinkedOpenHashMap m = new Int2IntLinkedOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
		assertEquals( 0, m.put( 0, 2 ) );
		assertEquals( 2, m.put( 0, 3 ) );
		assertEquals( 3, m.get( 0 ) );
		assertEquals( null, m.put( Integer.valueOf( 1 ), Integer.valueOf( 2 ) ) );
		assertEquals( Integer.valueOf( 2 ), m.put( Integer.valueOf( 1 ), Integer.valueOf( 3 ) ) );
		assertEquals( Integer.valueOf( 3 ), m.get( Integer.valueOf( 0 ) ) );
	}

	@Test
	public void testRemove() {
		Int2IntLinkedOpenHashMap m = new Int2IntLinkedOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
		m.defaultReturnValue( -1 );
		for( int i = 0; i < 100; i++ ) assertEquals( -1, m.put( i, i ) );
		for( int i = 0; i < 100; i++ ) assertEquals( -1, m.remove( i + 100 ) );
		for( int i = 50; i < 150; i++ ) assertEquals( i % 100, m.remove( i % 100 ) );
	}

	@Test
	public void testRemove0() {
		Int2IntLinkedOpenHashMap s = new Int2IntLinkedOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = -1; i <= 1; i++ ) assertEquals( 0, s.put( i, i ) );
		s.remove( 0 );
		IntIterator iterator = s.keySet().iterator();
		assertEquals( -1, iterator.nextInt() );
		assertEquals( 1, iterator.nextInt() );
		assertFalse( iterator.hasNext() );
		
		s = new Int2IntLinkedOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = -1; i <= 1; i++ ) assertEquals( 0, s.put( i, i ) );
		iterator = s.keySet().iterator();
		assertEquals( -1, iterator.nextInt() );
		assertEquals( 0, iterator.nextInt() );
		iterator.remove();
		assertEquals( 1, iterator.nextInt() );
		assertFalse( iterator.hasNext() );

		assertFalse( s.containsKey( 0 ) );
		
		iterator = s.keySet().iterator();
		assertEquals( -1, iterator.nextInt() );
		assertEquals( 1, iterator.nextInt() );
		assertFalse( iterator.hasNext() );
	}

	@Test
	public void testFirtLast0() {
		Int2IntLinkedOpenHashMap s;
		
		s = new Int2IntLinkedOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = 1; i < 100; i++ ) 
			assertEquals( 0, s.put( i, i ) );
		for( int i = 1; i < 100; i++ ) assertEquals( i, s.removeFirstInt() );
		assertTrue( s.isEmpty() );
		
		s = new Int2IntLinkedOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = 0; i < 100; i++ ) assertEquals( 0, s.put( i, i ) );
		for( int i = 100; i-- != 0; ) assertEquals( i, s.removeLastInt() );
		assertTrue( s.isEmpty() );

		s = new Int2IntLinkedOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = 100; i-- != 0; ) assertEquals( 0, s.put( i, i ) );
		for( int i = 0; i < 100; i++ ) assertEquals( i, s.removeLastInt() );
		assertTrue( s.isEmpty() );

		s = new Int2IntLinkedOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = 100; i-- != 0; ) assertEquals( 0, s.put( i, i ) );
		for( int i = 100; i-- != 0; ) assertEquals( i, s.removeFirstInt() );
		assertTrue( s.isEmpty() );
	}


	@Test
	public void testContainsValue() {
		Int2IntLinkedOpenHashMap m = new Int2IntLinkedOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
		assertEquals( 0, m.put( 0, 2 ) );
		assertEquals( 0, m.put( 1, 3 ) );
		assertTrue( m.containsValue( 2 ) );
		assertTrue( m.containsValue( 3 ) );
		assertFalse( m.containsValue( 4 ) );
		assertTrue( m.containsKey( 0 ) );
		assertTrue( m.containsKey( 1 ) );
		assertFalse( m.containsKey( 2 ) );
	}
	
	@Test
	public void testIterator() {
		Int2IntLinkedOpenHashMap m = new Int2IntLinkedOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
		m.defaultReturnValue( -1 );
		for( int i = 0; i < 100; i++ ) assertEquals( -1, m.put( i, i ) );
		assertEquals( 0, m.firstIntKey() );
		
		IntListIterator iterator = (IntListIterator)m.keySet().iterator();
		for( int i = 0; i <= 100; i++ ) {
			assertEquals( Integer.toString( i ), i - 1, iterator.previousIndex() );
			assertEquals( Integer.toString( i ), i, iterator.nextIndex() );
			if ( i != 100 ) assertEquals( Integer.toString( i ), i, iterator.nextInt() );
		}

		iterator = (IntListIterator)m.keySet().iterator( m.lastIntKey() );
		for( int i = 100; i-- != 0; ) {
			assertEquals( Integer.toString( i ), i, iterator.previousIndex() );
			assertEquals( Integer.toString( i ), i + 1, iterator.nextIndex() );
			if ( i != 0 ) assertEquals( Integer.toString( i ), i, iterator.previousInt() );
		}

		iterator = (IntListIterator)m.keySet().iterator( 50 );
		for( int i = 50; i < 100; i++ ) {
			assertEquals( Integer.toString( i ), i, iterator.previousIndex() );
			assertEquals( Integer.toString( i ), i + 1, iterator.nextIndex() );
			if ( i != 99 ) assertEquals( Integer.toString( i ), i + 1, iterator.nextInt() );
		}

		iterator = (IntListIterator)m.keySet().iterator( 50 );
		for( int i = 50; i-- != -1; ) {
			assertEquals( Integer.toString( i ), i + 1, iterator.previousIndex() );
			assertEquals( Integer.toString( i ), i + 2, iterator.nextIndex() );
			if ( i != -1 ) assertEquals( Integer.toString( i ), i + 1, iterator.previousInt() );
		}

		iterator = (IntListIterator)m.keySet().iterator( 50 );
		for( int i = 50; i-- != -1; ) assertEquals( Integer.toString( i ), i + 1, iterator.previousInt() );
		assertEquals( -1, iterator.previousIndex() );
		assertEquals( 0, iterator.nextIndex() );
		
		iterator = (IntListIterator)m.keySet().iterator( 50 );
		for( int i = 50; i < 100 - 1; i++ ) assertEquals( Integer.toString( i ), i + 1, iterator.nextInt() );
		assertEquals( 99, iterator.previousIndex() );
		assertEquals( 100, iterator.nextIndex() );

		iterator = (IntListIterator)m.keySet().iterator( 50 );
		iterator.previousInt();
		iterator.remove();
		assertEquals( 49, iterator.previousIndex() );
		assertEquals( 49, iterator.previousInt() );
		
		iterator = (IntListIterator)m.keySet().iterator( 49 );
		iterator.nextInt();
		iterator.remove();
		assertEquals( 50, iterator.nextIndex() );
		assertEquals( 52, iterator.nextInt() );
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testIteratorMissingElement() {
		Int2IntLinkedOpenHashMap m = new Int2IntLinkedOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
		m.defaultReturnValue( -1 );
		for( int i = 0; i < 100; i++ ) assertEquals( -1, m.put( i, i ) );
		m.keySet().iterator( 1000 );
	}


	@Test
	public void testPutAndMove() {
		Int2IntLinkedOpenHashMap m = new Int2IntLinkedOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
		m.defaultReturnValue( Integer.MIN_VALUE );
		for( int i = 0; i < 100; i++ ) assertEquals( Integer.MIN_VALUE, m.putAndMoveToFirst( i, i ) );
		m.clear();
		for( int i = 0; i < 100; i++ ) assertEquals( Integer.MIN_VALUE, m.putAndMoveToLast( i, i ) );
		assertEquals( Integer.MIN_VALUE, m.putAndMoveToFirst( -1, -1 ) );
		assertEquals( -1, m.firstIntKey() );
		assertEquals( Integer.MIN_VALUE, m.putAndMoveToFirst( -2, -2 ) );
		assertEquals( -2, m.firstIntKey() );
		assertEquals( -1, m.putAndMoveToFirst( -1, -1 ) );
		assertEquals( -1, m.firstIntKey() );
		assertEquals( -1, m.putAndMoveToFirst( -1, -1 ) );
		assertEquals( -1, m.firstIntKey() );
		assertEquals( -1, m.putAndMoveToLast( -1, -1 ) );
		assertEquals( -1, m.lastIntKey() );
		assertEquals( Integer.MIN_VALUE, m.putAndMoveToLast( 100, 100 ) );
		assertEquals( 100, m.lastIntKey() );
		assertEquals( Integer.MIN_VALUE, m.putAndMoveToLast( 101, 101 ) );
		assertEquals( 101, m.lastIntKey() );
		assertEquals( 100, m.putAndMoveToLast( 100, 100 ) );
		assertEquals( 100, m.lastIntKey() );
		assertEquals( 100, m.putAndMoveToLast( 100, 100 ) );
		assertEquals( 100, m.lastIntKey() );
		assertEquals( 100, m.putAndMoveToFirst( 100, 100 ) );
		assertEquals( 100, m.firstIntKey() );
	}

	@Test
	public void testRemoveFirstLast() {
		Int2IntLinkedOpenHashMap m = new Int2IntLinkedOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
		m.defaultReturnValue( -1 );
		for( int i = 0; i < 100; i++ ) assertEquals( -1, m.put( i, 1 + i ) );
		assertEquals( 1, m.removeFirstInt() );
		assertEquals( 2, m.removeFirstInt() );
		assertEquals( 100, m.removeLastInt() );
	}	

	@Test(expected=NoSuchElementException.class)
	public void testRemoveFirstEmpty() {
		new Int2IntLinkedOpenHashMap( Hash.DEFAULT_INITIAL_SIZE ).removeFirstInt();
	}

	@Test(expected=NoSuchElementException.class)
	public void testRemoveLastEmpty() {
		new Int2IntLinkedOpenHashMap( Hash.DEFAULT_INITIAL_SIZE ).removeLastInt();
	}

	@Test
	public void testFastIterator() {
		Int2IntLinkedOpenHashMap s = new Int2IntLinkedOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
		s.defaultReturnValue( -1 );
		for( int i = 0; i < 100; i++ ) assertEquals( -1, s.put( i, i ) );
		ObjectIterator<Entry> fastIterator = s.int2IntEntrySet().fastIterator();
		Entry entry = fastIterator.next();
		int key = entry.getIntKey();
		entry.setValue( -1000 );
		assertEquals( s.get( key ), -1000 );
		fastIterator.remove();
		assertEquals( s.get( key ), -1 );
	}
}
