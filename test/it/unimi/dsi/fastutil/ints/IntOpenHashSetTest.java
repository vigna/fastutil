package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("rawtypes")
public class IntOpenHashSetTest {

	@SuppressWarnings("boxing")
	@Test
	public void testToArrayNullAtEnd() {
		IntOpenHashSet s = new IntOpenHashSet( new int[] { 1, 2, 3 } );
		assertEquals( 3, s.toArray( new Object[ 0 ] ).length );
		assertEquals( 3, s.toArray( new Integer[ 0 ] ).length );
		assertTrue( s.toArray( new Integer[] { -1, -1, -1, -1 } )[ 3 ] == null );
	}

	@Test
	public void testContainsNull() {
		IntOpenHashSet s = new IntOpenHashSet( new int[] { 1, 2, 3 } );
		assertFalse( s.contains( null ) );
	}

	@SuppressWarnings("boxing")
	@Test
	public void testEquals() {
		IntOpenHashSet s = new IntOpenHashSet( new int[] { 1, 2, 3 } );
		assertFalse( s.equals( new ObjectOpenHashSet<Integer>( new Integer[] { 1, null } ) ) );
	}

	@Test
	public void testInfiniteLoop0() {
        IntOpenHashSet set = new IntOpenHashSet(4, 1.0f);
        set.add(1);
        set.add(2);
        set.add(3);
        set.remove(2);
        set.trim();
        set.remove(1); // Will hang inside this call
    }

	@Test
	public void testInfiniteLoop1() {
        IntOpenHashSet set = new IntOpenHashSet();
        set.add(1);
        set.add(2);
        set.add(3);
        set.trim(1);
    }

	@Test
	public void testStrangeRetainAllCase() {

		IntArrayList initialElements = IntArrayList.wrap(new int[] { 586, 940,
				1086, 1110, 1168, 1184, 1185, 1191, 1196, 1229, 1237, 1241,
				1277, 1282, 1284, 1299, 1308, 1309, 1310, 1314, 1328, 1360,
				1366, 1370, 1378, 1388, 1392, 1402, 1406, 1411, 1426, 1437,
				1455, 1476, 1489, 1513, 1533, 1538, 1540, 1541, 1543, 1547,
				1548, 1551, 1557, 1568, 1575, 1577, 1582, 1583, 1584, 1588,
				1591, 1592, 1601, 1610, 1618, 1620, 1633, 1635, 1653, 1654,
				1655, 1660, 1661, 1665, 1674, 1686, 1688, 1693, 1700, 1705,
				1717, 1720, 1732, 1739, 1740, 1745, 1746, 1752, 1754, 1756,
				1765, 1766, 1767, 1771, 1772, 1781, 1789, 1790, 1793, 1801,
				1806, 1823, 1825, 1827, 1828, 1829, 1831, 1832, 1837, 1839,
				1844, 2962, 2969, 2974, 2990, 3019, 3023, 3029, 3030, 3052,
				3072, 3074, 3075, 3093, 3109, 3110, 3115, 3116, 3125, 3137,
				3142, 3156, 3160, 3176, 3180, 3188, 3193, 3198, 3207, 3209,
				3210, 3213, 3214, 3221, 3225, 3230, 3231, 3236, 3240, 3247,
				3261, 4824, 4825, 4834, 4845, 4852, 4858, 4859, 4867, 4871,
				4883, 4886, 4887, 4905, 4907, 4911, 4920, 4923, 4924, 4925,
				4934, 4942, 4953, 4957, 4965, 4973, 4976, 4980, 4982, 4990,
				4993, 6938, 6949, 6953, 7010, 7012, 7034, 7037, 7049, 7076,
				7094, 7379, 7384, 7388, 7394, 7414, 7419, 7458, 7459, 7466,
				7467 });

		IntArrayList retainElements = IntArrayList.wrap(new int[] { 586 });

		// Initialize both implementations with the same data
		IntOpenHashSet instance = new IntOpenHashSet(initialElements);
		IntRBTreeSet referenceInstance = new IntRBTreeSet(initialElements);

		instance.retainAll(retainElements);
		referenceInstance.retainAll(retainElements);

		// print the correct result {586}
		// System.out.println("ref: " + referenceInstance);

		// prints {586, 7379}, which is clearly wrong
		// System.out.println("ohm: " + instance);

		// Fails
		assertEquals( referenceInstance, instance );
	}	

	private static java.util.Random r = new java.util.Random( 0 );

	private static int genKey() {
		return r.nextInt();
	}

	@Test
	public void testSmallExpectedValuesWeirdLoadFactors() {
		for( int expected = 0; expected < 5; expected ++ )
			for( float loadFactor: new float[] { Float.MIN_VALUE, .25f, .5f, .75f, 1 - Float.MIN_VALUE } ) {
				IntOpenHashSet s = new IntOpenHashSet( 0, loadFactor );
				assertTrue( s.add( 2 ) );
				assertTrue( s.add( 3 ) );
				assertFalse( s.add( 2 ) );
				assertFalse( s.add( 3 ) );
			}
	}
	
	@Test
	public void testRemove() {
		IntOpenHashSet s = new IntOpenHashSet( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = 0; i < 100; i++ ) assertTrue( s.add( i ) );
		for( int i = 0; i < 100; i++ ) assertFalse( s.remove( 100 + i ) );
		for( int i = 50; i < 150; i++ ) assertTrue( Integer.toString( i % 100 ), s.remove( i % 100 ) );
	}

	@Test
	public void testRemove0() {
		IntOpenHashSet s = new IntOpenHashSet( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = -1; i <= 1; i++ ) assertTrue( s.add( i ) );
		assertTrue( s.remove( 0 ) );
		IntIterator iterator = s.iterator();
		IntOpenHashSet z = new IntOpenHashSet();
		z.add( iterator.nextInt() );
		z.add( iterator.nextInt() );
		assertFalse( iterator.hasNext() );
		assertEquals( new IntOpenHashSet( new int[] { -1, 1 } ), z );
		
		s = new IntOpenHashSet( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = -1; i <= 1; i++ ) assertTrue( s.add( i ) );
		iterator = s.iterator();
		while( iterator.hasNext() ) if ( iterator.nextInt() == 0 ) iterator.remove();
		
		assertFalse( s.contains( 0 ) );
		
		iterator = s.iterator();
		int[] content = new int[ 2 ];
		content[ 0 ] = iterator.nextInt();
		content[ 1 ] = iterator.nextInt();
		assertFalse( iterator.hasNext() );
		Arrays.sort( content );
		assertArrayEquals( new int[] { -1, 1 }, content );
	}
	
	@Test
	public void testWrapAround() {
		IntOpenHashSet s = new IntOpenHashSet( 4, .5f );
		assertEquals( 8, s.n );
		// The following code inverts HashCommon.phiMix() and places strategically keys in slots 6, 7 and 0
		s.add( HashCommon.invMix( 6 ) );
		s.add( HashCommon.invMix( 7 ) );
		s.add( HashCommon.invMix( 6 + 8 ) );
		assertNotEquals( 0, s.key[ 0 ] );
		assertNotEquals( 0, s.key[ 6 ] );
		assertNotEquals( 0, s.key[ 7 ] );
		IntOpenHashSet keys = s.clone();
		IntIterator iterator = s.iterator();
		IntOpenHashSet t = new IntOpenHashSet();
		t.add( iterator.nextInt() );
		t.add( iterator.nextInt() );
		// Originally, this remove would move the entry in slot 0 in slot 6 and we would return the entry in 0 twice
		iterator.remove(); 
		t.add( iterator.nextInt() );
		assertEquals( keys, t );
	}	

	@Test
	public void testWrapAround2() {
		IntOpenHashSet s = new IntOpenHashSet( 4, .75f );
		assertEquals( 8, s.n );
		// The following code inverts HashCommon.phiMix() and places strategically keys in slots 4, 5, 6, 7 and 0
		s.add( HashCommon.invMix( 4 ) );
		s.add( HashCommon.invMix( 5 ) );
		s.add( HashCommon.invMix( 4 + 8 ) );
		s.add( HashCommon.invMix( 5 + 8 ) );
		s.add( HashCommon.invMix( 4 + 16 ) );
		assertNotEquals( 0, s.key[ 0 ] );
		assertNotEquals( 0, s.key[ 4 ] );
		assertNotEquals( 0, s.key[ 5 ] );
		assertNotEquals( 0, s.key[ 6 ] );
		assertNotEquals( 0, s.key[ 7 ] );
		//System.err.println(Arrays.toString( s.key ));
		IntOpenHashSet keys = s.clone();
		IntIterator iterator = s.iterator();
		IntOpenHashSet t = new IntOpenHashSet();
		assertTrue( t.add( iterator.nextInt() ) );
		iterator.remove();
		//System.err.println(Arrays.toString( s.key ));
		assertTrue( t.add( iterator.nextInt() ) );
		//System.err.println(Arrays.toString( s.key ));
		// Originally, this remove would move the entry in slot 0 in slot 6 and we would return the entry in 0 twice
		assertTrue( t.add( iterator.nextInt() ) );
		//System.err.println(Arrays.toString( s.key ));
		assertTrue( t.add( iterator.nextInt() ) );
		iterator.remove();
		//System.err.println(Arrays.toString( s.key ));
		assertTrue( t.add( iterator.nextInt() ) );
		assertEquals( 3, s.size() );
		assertEquals( keys, t );
	}	
	
	@Test
	public void testWrapAround3() {
		IntOpenHashSet s = new IntOpenHashSet( 4, .75f );
		assertEquals( 8, s.n );
		// The following code inverts HashCommon.phiMix() and places strategically keys in slots 5, 6, 7, 0 and 1
		s.add( HashCommon.invMix( 5 ) );
		s.add( HashCommon.invMix( 5 + 8 ) );
		s.add( HashCommon.invMix( 5 + 16 ) );
		s.add( HashCommon.invMix( 5 + 32 ) );
		s.add( HashCommon.invMix( 5 + 64 ) );
		assertNotEquals( 0, s.key[ 5 ] );
		assertNotEquals( 0, s.key[ 6 ] );
		assertNotEquals( 0, s.key[ 7 ] );
		assertNotEquals( 0, s.key[ 0 ] );
		assertNotEquals( 0, s.key[ 1 ] );
		//System.err.println(Arrays.toString( s.key ));
		IntOpenHashSet keys = s.clone();
		IntIterator iterator = s.iterator();
		IntOpenHashSet t = new IntOpenHashSet();
		assertTrue( t.add( iterator.nextInt() ) );
		iterator.remove();
		//System.err.println(Arrays.toString( s.key ));
		assertTrue( t.add( iterator.nextInt() ) );
		iterator.remove();
		//System.err.println(Arrays.toString( s.key ));
		// Originally, this remove would move the entry in slot 0 in slot 6 and we would return the entry in 0 twice
		assertTrue( t.add( iterator.nextInt() ) );
		iterator.remove();
		//System.err.println(Arrays.toString( s.key ));
		assertTrue( t.add( iterator.nextInt() ) );
		iterator.remove();
		//System.err.println(Arrays.toString( s.key ));
		assertTrue( t.add( iterator.nextInt() ) );
		iterator.remove();
		assertEquals( 0, s.size() );
		assertEquals( keys, t );
	}	
	
	@SuppressWarnings("boxing")
	private static void checkTable( IntOpenHashSet s ) {
		final int[] key = s.key;
		assert ( s.n & -s.n ) == s.n : "Table length is not a power of two: " + s.n;
		assert s.n == s.key.length - 1;
		int n = s.n;
		while ( n-- != 0 )
			if ( key[ n ] != 0 && !s.contains( key[ n ] ) ) throw new AssertionError( "Hash table has key " + key[ n ]
					+ " marked as occupied, but the key does not belong to the table" );
		
		if ( s.containsNull && ! s.contains( 0 ) ) throw new AssertionError( "Hash table should contain zero by internal state, but it doesn't when queried" );
		if ( ! s.containsNull && s.contains( 0 ) ) throw new AssertionError( "Hash table should not contain zero by internal state, but it does when queried" );

		java.util.HashSet<Integer> t = new java.util.HashSet<Integer>();
		for ( int i = s.size(); i-- != 0; )
			if ( key[ i ] != 0 && !t.add( key[ i ] ) ) throw new AssertionError( "Key " + key[ i ] + " appears twice" );

	}

	private static void printProbes( IntOpenHashSet m ) {
		long totProbes = 0;
		double totSquareProbes = 0;
		int maxProbes = 0;
		final int[] key = m.key;
		final double f = (double)m.size / m.n;
		for ( int i = 0, c = 0; i < m.n; i++ ) {
			if ( key[ i ] != 0 ) c++;
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

	@SuppressWarnings({ "unchecked", "boxing" })
	private static void test( int n, float f ) throws IOException, ClassNotFoundException {
		int c;
		IntOpenHashSet m = new IntOpenHashSet( Hash.DEFAULT_INITIAL_SIZE, f );
		java.util.Set t = new java.util.HashSet();

		/* First of all, we fill t with random data. */

		for ( int i = 0; i < Math.ceil( f * n ); i++ )
			t.add( ( Integer.valueOf( genKey() ) ) );

		/* Now we add to m the same data */

		m.addAll( t );
		checkTable( m );
		
		assertTrue( "Error: !m.equals(t) after insertion", m.equals( t ) );
		assertTrue( "Error: !t.equals(m) after insertion", t.equals( m ) );
		printProbes( m );

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
			int T = genKey();
			assertEquals( "Error: divergence in keys between t and m (polymorphic method)", m.contains( T ), t.contains( ( Integer.valueOf( T ) ) ) );
		}

		/*
		 * Again, we check that inquiries about random data give the same answer in m and t, but for
		 * m we use the standard method.
		 */

		for ( int i = 0; i < n; i++ ) {
			int T = genKey();
			assertFalse( "Error: divergence between t and m (standard method)", m.contains( ( Integer.valueOf( T ) ) ) != t.contains( ( Integer.valueOf( T ) ) ) );
		}


		/* Now we put and remove random data in m and t, checking that the result is the same. */

		for ( int i = 0; i < 20 * n; i++ ) {
			int T = genKey();
			assertFalse( "Error: divergence in add() between t and m", m.add( ( Integer.valueOf( T ) ) ) != t.add( ( Integer.valueOf( T ) ) ) );
			T = genKey();
			assertFalse( "Error: divergence in remove() between t and m", m.remove( ( Integer.valueOf( T ) ) ) != t.remove( ( Integer.valueOf( T ) ) ) );
		}

		checkTable( m );
		assertTrue( "Error: !m.equals(t) after removal", m.equals( t ) );
		assertTrue( "Error: !t.equals(m) after removal", t.equals( m ) );
		/* Now we check that m actually holds that data. */

		for ( java.util.Iterator i = t.iterator(); i.hasNext(); ) {
			Object e = i.next();
			assertFalse( "Error: m and t differ on a key (" + e + ") after removal (iterating on t)", !m.contains( e ) );
		}

		/* Now we check that m actually holds that data, but iterating on m. */

		for ( java.util.Iterator i = m.iterator(); i.hasNext(); ) {
			Object e = i.next();
			assertFalse( "Error: m and t differ on a key (" + e + ") after removal (iterating on m)", !t.contains( e ) );
		}

		/* Now we make m into an array, make it again a set and check it is OK. */
		int a[] = m.toIntArray();

		assertTrue( "Error: toArray() output (or array-based constructor) is not OK", new IntOpenHashSet( a ).equals( m ) );

		/* Now we check cloning. */

		assertTrue( "Error: m does not equal m.clone()", m.equals( m.clone() ) );
		assertTrue( "Error: m.clone() does not equal m", m.clone().equals( m ) );

		int h = m.hashCode();

		/* Now we save and read m. */

		java.io.File ff = new java.io.File( "it.unimi.dsi.fastutil.test" );
		java.io.OutputStream os = new java.io.FileOutputStream( ff );
		java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream( os );

		oos.writeObject( m );
		oos.close();

		java.io.InputStream is = new java.io.FileInputStream( ff );
		java.io.ObjectInputStream ois = new java.io.ObjectInputStream( is );

		m = (IntOpenHashSet)ois.readObject();
		ois.close();
		ff.delete();

		assertEquals( "Error: hashCode() changed after save/read", h, m.hashCode() );

		printProbes( m );
		checkTable( m );

		/* Now we check that m actually holds that data, but iterating on m. */

		for ( java.util.Iterator i = m.iterator(); i.hasNext(); ) {
			Object e = i.next();
			assertFalse( "Error: m and t differ on a key (" + e + ") after save/read", !t.contains( e ) );
		}


		/* Now we put and remove random data in m and t, checking that the result is the same. */

		for ( int i = 0; i < 20 * n; i++ ) {
			int T = genKey();
			assertFalse( "Error: divergence in add() between t and m after save/read", m.add( ( Integer.valueOf( T ) ) ) != t.add( ( Integer.valueOf( T ) ) ) );
			T = genKey();
			assertFalse( "Error: divergence in remove() between t and m after save/read", m.remove( ( Integer.valueOf( T ) ) ) != t.remove( ( Integer.valueOf( T ) ) ) );
		}

		assertTrue( "Error: !m.equals(t) after post-save/read removal", m.equals( t ) );
		assertTrue( "Error: !t.equals(m) after post-save/read removal", t.equals( m ) );

		/* Now we take out of m everything, and check that it is empty. */

		for ( java.util.Iterator i = m.iterator(); i.hasNext(); ) {
			i.next();
			i.remove();
		}

		assertFalse( "Error: m is not empty (as it should be)", !m.isEmpty() );


		m = new IntOpenHashSet( n, f );
		t.clear();

		/* Now we torture-test the hash table. This part is implemented only for integers and longs. */

		for( int i = n; i-- != 0; ) m.add( i );
		t.addAll( m );
		printProbes( m );
		checkTable( m );

		for( int i = n; i-- != 0; )
			assertEquals( "Error: m and t differ on a key during torture-test insertion.", m.add( i ), t.add( ( Integer.valueOf( i ) ) ) );

		assertTrue( "Error: !m.equals(t) after torture-test insertion", m.equals( t ) );
		assertTrue( "Error: !t.equals(m) after torture-test insertion", t.equals( m ) );

		for( int i = n; i-- != 0; )
			assertEquals( "Error: m and t differ on a key during torture-test insertion.", m.remove( i ), t.remove( ( Integer.valueOf( i ) ) ) );

		assertTrue( "Error: !m.equals(t) after torture-test removal", m.equals( t ) );
		assertTrue( "Error: !t.equals(m) after torture-test removal", t.equals( m ) );
		assertTrue( "Error: !m.equals(m.clone()) after torture-test removal", m.equals( m.clone() ) );
		assertTrue( "Error: !m.clone().equals(m) after torture-test removal", m.clone().equals( m ) );

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
}
