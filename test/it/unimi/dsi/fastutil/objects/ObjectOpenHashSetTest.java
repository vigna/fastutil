package it.unimi.dsi.fastutil.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import it.unimi.dsi.fastutil.Hash;

import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("rawtypes")
public class ObjectOpenHashSetTest {

	private static java.util.Random r = new java.util.Random( 0 );

	private static Object genKey() {
		return Integer.toBinaryString( r.nextInt() );
	}

	@SuppressWarnings("boxing")
	private static void checkTable( ObjectOpenHashSet<Integer> s ) {
		final boolean[] used = s.used;
		final Object[] key = s.key;
		assert ( s.n & -s.n ) == s.n : "Table length is not a power of two: " + s.n;
		assert s.n == s.key.length;
		assert s.n == used.length;
		int n = s.n;
		while ( n-- != 0 )
			if ( used[ n ] && !s.contains( key[ n ] ) ) throw new AssertionError( "Hash table has key " + key[ n ]
					+ " marked as occupied, but the key does not belong to the table" );

		java.util.HashSet<String> t = new java.util.HashSet<String>();
		for ( int i = s.size(); i-- != 0; )
			if ( used[ i ] && !t.add( (String)key[ i ] ) ) throw new AssertionError( "Key " + key[ i ] + " appears twice" );

	}

	private static void printProbes( ObjectOpenHashSet m ) {
		long totProbes = 0;
		double totSquareProbes = 0;
		int maxProbes = 0;
		final double f = (double)m.size / m.n;
		for ( int i = 0, c = 0; i < m.n; i++ ) {
			if ( m.used[ i ] ) c++;
			else {
				if ( c != 0 ) {
					final long p = ( c + 1 ) * ( c + 2 ) / 2;
					totProbes += p;
					totSquareProbes += (double)p * p;
				}
				maxProbes = Math.max( c, maxProbes );
				c = 0;
				totProbes++;
				totSquareProbes++;
			}
		}

		final double expected = (double)totProbes / m.n;
		System.err.println( "Expected probes: " + (
				3 * Math.sqrt( 3 ) * ( f / ( ( 1 - f ) * ( 1 - f ) ) ) + 4 / ( 9 * f ) - 1
				) + "; actual: " + expected + "; stddev: " + Math.sqrt( totSquareProbes / m.n - expected * expected ) + "; max probes: " + maxProbes );
	}


	@SuppressWarnings("unchecked")
	private static void test( int n, float f ) {
		int c;
		ObjectOpenHashSet m = new ObjectOpenHashSet( Hash.DEFAULT_INITIAL_SIZE, f );
		java.util.Set t = new java.util.HashSet();
		/* First of all, we fill t with random data. */
		for ( int i = 0; i < f * n; i++ )
			t.add( ( genKey() ) );
		/* Now we add to m the same data */
		m.addAll( t );
		assertTrue( "Error: !m.equals(t) after insertion", m.equals( t ) );
		assertTrue( "Error: !t.equals(m) after insertion", t.equals( m ) );
		printProbes( m );
		checkTable( m );
		/* Now we check that m actually holds that data. */
		for ( java.util.Iterator i = t.iterator(); i.hasNext(); ) {
			Object e = i.next();
			assertTrue( "Error: m and t differ on a key (" + e + ") after insertion (iterating on t)", m.contains( e ) );
		}
		/* Now we check that m actually holds that data, but iterating on m. */
		c = 0;
		for ( java.util.Iterator i = m.iterator(); i.hasNext(); ) {
			Object e = i.next();
			c++;
			assertTrue( "Error: m and t differ on a key (" + e + ") after insertion (iterating on m)", t.contains( e ) );
		}
		assertEquals( "Error: m has only " + c + " keys instead of " + t.size() + " after insertion (iterating on m)", c, t.size() );
		/*
		 * Now we check that inquiries about random data give the same answer in m and t. For m we
		 * use the polymorphic method.
		 */
		for ( int i = 0; i < n; i++ ) {
			Object T = genKey();
			assertTrue( "Error: divergence in keys between t and m (polymorphic method)", m.contains( T ) == t.contains( ( T ) ) );
		}
		/*
		 * Again, we check that inquiries about random data give the same answer in m and t, but for
		 * m we use the standard method.
		 */
		for ( int i = 0; i < n; i++ ) {
			Object T = genKey();
			assertTrue( "Error: divergence between t and m (standard method)", m.contains( ( T ) ) == t.contains( ( T ) ) );
		}
		/* Now we put and remove random data in m and t, checking that the result is the same. */
		for ( int i = 0; i < 20 * n; i++ ) {
			Object T = genKey();
			assertTrue( "Error: divergence in add() between t and m", m.add( ( T ) ) == t.add( ( T ) ) );
			T = genKey();
			assertTrue( "Error: divergence in remove() between t and m", m.remove( ( T ) ) == t.remove( ( T ) ) );
		}
		assertTrue( "Error: !m.equals(t) after removal", m.equals( t ) );
		assertTrue( "Error: !t.equals(m) after removal", t.equals( m ) );
		
		checkTable( m );
		printProbes( m );

		/*
		 * Now we check that m actually holds that data.
		 */
		for ( java.util.Iterator i = t.iterator(); i.hasNext(); ) {
			Object e = i.next();
			assertTrue( "Error: m and t differ on a key (" + e + ") after removal (iterating on t)", m.contains( e ) );
		}
		/* Now we check that m actually holds that data, but iterating on m. */
		for ( java.util.Iterator i = m.iterator(); i.hasNext(); ) {
			Object e = i.next();
			assertTrue( "Error: m and t differ on a key (" + e + ") after removal (iterating on m)", t.contains( e ) );
		}
		/* Now we make m into an array, make it again a set and check it is OK. */
		Object a[] = m.toArray();
		assertTrue( "Error: toArray() output (or array-based constructor) is not OK", new ObjectOpenHashSet( a ).equals( m ) );
		/* Now we check cloning. */
		assertTrue( "Error: m does not equal m.clone()", m.equals( m.clone() ) );
		assertTrue( "Error: m.clone() does not equal m", m.clone().equals( m ) );
		int h = m.hashCode();
		/* Now we save and read m. */
		try {
			java.io.File ff = new java.io.File( "it.unimi.dsi.fastutil.test" );
			java.io.OutputStream os = new java.io.FileOutputStream( ff );
			java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream( os );
			oos.writeObject( m );
			oos.close();
			java.io.InputStream is = new java.io.FileInputStream( ff );
			java.io.ObjectInputStream ois = new java.io.ObjectInputStream( is );
			m = (ObjectOpenHashSet)ois.readObject();
			ois.close();
			ff.delete();
		}
		catch ( Exception e ) {
			e.printStackTrace();
			System.exit( 1 );
		}
		assertEquals( "Error: hashCode() changed after save/read", h, m.hashCode() );
		checkTable( m );
		printProbes( m );
		/* Now we check that m actually holds that data, but iterating on m. */
		for ( java.util.Iterator i = m.iterator(); i.hasNext(); ) {
			Object e = i.next();
			assertTrue( "Error: m and t differ on a key (" + e + ") after save/read", t.contains( e ) );
		}
		/* Now we put and remove random data in m and t, checking that the result is the same. */
		for ( int i = 0; i < 20 * n; i++ ) {
			Object T = genKey();
			assertTrue( "Error: divergence in add() between t and m after save/read", m.add( ( T ) ) == t.add( ( T ) ) );
			T = genKey();
			assertTrue( "Error: divergence in remove() between t and m after save/read", m.remove( ( T ) ) == t.remove( ( T ) ) );
		}
		assertTrue( "Error: !m.equals(t) after post-save/read removal", m.equals( t ) );
		assertTrue( "Error: !t.equals(m) after post-save/read removal", t.equals( m ) ); 		
		/*
		 * Now we take out of m everything , and check that it is empty.
		 */
		for ( java.util.Iterator i = m.iterator(); i.hasNext(); ) {
			i.next();
			i.remove();
		}
		assertTrue( "Error: m is not empty (as it should be)", m.isEmpty() );
		return;
	}

	@Test
	public void test1() {
		test( 1, Hash.DEFAULT_LOAD_FACTOR );
		test( 1, Hash.FAST_LOAD_FACTOR );
		test( 1, Hash.VERY_FAST_LOAD_FACTOR );
	}

	@Test
	public void test10() {
		test( 10, Hash.DEFAULT_LOAD_FACTOR );
		test( 10, Hash.FAST_LOAD_FACTOR );
		test( 10, Hash.VERY_FAST_LOAD_FACTOR );
	}

	@Test
	public void test100() {
		test( 100, Hash.DEFAULT_LOAD_FACTOR );
		test( 100, Hash.FAST_LOAD_FACTOR );
		test( 100, Hash.VERY_FAST_LOAD_FACTOR );
	}

	@Ignore("Too long")
	@Test
	public void test1000() {
		test( 1000, Hash.DEFAULT_LOAD_FACTOR );
		test( 1000, Hash.FAST_LOAD_FACTOR );
		test( 1000, Hash.VERY_FAST_LOAD_FACTOR );
	}
}
