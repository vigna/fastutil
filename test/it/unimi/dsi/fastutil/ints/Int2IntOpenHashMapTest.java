package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.ints.Int2IntMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("rawtypes")
public class Int2IntOpenHashMapTest {

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
		Int2IntOpenHashMap instance = new Int2IntOpenHashMap(initialElements.elements(), new int[ initialElements.size() ]);
		IntRBTreeSet referenceInstance = new IntRBTreeSet(initialElements);

		instance.keySet().retainAll(retainElements);
		referenceInstance.retainAll(retainElements);

		// print the correct result {586}
		System.out.println("ref: " + referenceInstance);

		// prints {586, 7379}, which is clearly wrong
		System.out.println("ohm: " + instance);

		// Fails
		assertEquals( referenceInstance, instance.keySet() );
	}	

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

	@SuppressWarnings({ "unchecked", "boxing" })
	protected static void test( int n, float f ) throws IOException, ClassNotFoundException {
		Int2IntOpenHashMap m = new Int2IntOpenHashMap( Hash.DEFAULT_INITIAL_SIZE, f );
		Map t = new java.util.HashMap();
		/* First of all, we fill t with random data. */
		for ( int i = 0; i < n; i++ )
			t.put( ( Integer.valueOf( genKey() ) ), ( Integer.valueOf( genValue() ) ) );
		/* Now we add to m the same data */
		m.putAll( t );
		assertTrue( "Error: !m.equals(t) after insertion", m.equals( t ) );
		assertTrue( "Error: !t.equals(m) after insertion", t.equals( m ) );
		/*
		 * Now we check that m actually holds that data.
		 */
		for ( java.util.Iterator i = t.entrySet().iterator(); i.hasNext(); ) {
			java.util.Map.Entry e = (java.util.Map.Entry)i.next();
			assertTrue( "Error: m and t differ on an entry (" + e + ") after insertion (iterating on t)", valEquals( e.getValue(), m.get( e.getKey() ) ) );
		}
		/* Now we check that m actually holds that data, but iterating on m. */
		for ( java.util.Iterator i = m.entrySet().iterator(); i.hasNext(); ) {
			java.util.Map.Entry e = (java.util.Map.Entry)i.next();
			assertTrue( "Error: m and t differ on an entry (" + e + ") after insertion (iterating on m)", valEquals( e.getValue(), t.get( e.getKey() ) ) );
		}
		/* Now we check that m actually holds the same keys. */
		for ( java.util.Iterator i = t.keySet().iterator(); i.hasNext(); ) {
			Object o = i.next();
			assertTrue( "Error: m and t differ on a key (" + o + ") after insertion (iterating on t)", m.containsKey( o ) );
			assertTrue( "Error: m and t differ on a key (" + o + ", in keySet()) after insertion (iterating on t)", m.keySet().contains( o ) );
		}
		/* Now we check that m actually holds the same keys, but iterating on m. */
		for ( java.util.Iterator i = m.keySet().iterator(); i.hasNext(); ) {
			Object o = i.next();
			assertTrue( "Error: m and t differ on a key after insertion (iterating on m)", t.containsKey( o ) );
			assertTrue( "Error: m and t differ on a key (in keySet()) after insertion (iterating on m)", t.keySet().contains( o ) );
		}
		/* Now we check that m actually hold the same values. */
		for ( java.util.Iterator i = t.values().iterator(); i.hasNext(); ) {
			Object o = i.next();
			assertTrue( "Error: m and t differ on a value after insertion (iterating on t)", m.containsValue( o ) );
			assertTrue( "Error: m and t differ on a value (in values()) after insertion (iterating on t)", m.values().contains( o ) );
		}
		/* Now we check that m actually hold the same values, but iterating on m. */
		for ( java.util.Iterator i = m.values().iterator(); i.hasNext(); ) {
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
			assertTrue( "Error: divergence between t and m (polymorphic method)",
					!( m.get( T ) != ( 0 ) ) != ( ( t.get( ( Integer.valueOf( T ) ) ) == null ? ( 0 ) : ( ( ( (Integer)( t.get( ( Integer.valueOf( T ) ) ) ) ).intValue() ) ) ) != ( 0 ) ) ||
							t.get( ( Integer.valueOf( T ) ) ) != null &&
							!m.get( ( Integer.valueOf( T ) ) ).equals( t.get( ( Integer.valueOf( T ) ) ) ) );
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
			assertTrue( "Error: divergence in put() between t and m",
					valEquals( m.put( ( Integer.valueOf( T ) ), ( Integer.valueOf( U ) ) ), t.put( ( Integer.valueOf( T ) ), ( Integer.valueOf( U ) ) ) ) );
			T = genKey();
			assertTrue( "Error: divergence in remove() between t and m", valEquals( m.remove( ( Integer.valueOf( T ) ) ), t.remove( ( Integer.valueOf( T ) ) ) ) );
		}
		assertTrue( "Error: !m.equals(t) after removal", m.equals( t ) );
		assertTrue( "Error: !t.equals(m) after removal", t.equals( m ) );
		/*
		 * Now we check that m actually holds the same data.
		 */
		for ( java.util.Iterator i = t.entrySet().iterator(); i.hasNext(); ) {
			java.util.Map.Entry e = (java.util.Map.Entry)i.next();
			assertTrue( "Error: m and t differ on an entry (" + e + ") after removal (iterating on t)", valEquals( e.getValue(), m.get( e.getKey() ) ) );
		}
		/* Now we check that m actually holds that data, but iterating on m. */
		for ( java.util.Iterator i = m.entrySet().iterator(); i.hasNext(); ) {
			java.util.Map.Entry e = (java.util.Map.Entry)i.next();
			assertTrue( "Error: m and t differ on an entry (" + e + ") after removal (iterating on m)", valEquals( e.getValue(), t.get( e.getKey() ) ) );
		}
		/* Now we check that m actually holds the same keys. */
		for ( java.util.Iterator i = t.keySet().iterator(); i.hasNext(); ) {
			Object o = i.next();
			assertTrue( "Error: m and t differ on a key (" + o + ") after removal (iterating on t)", m.containsKey( o ) );
			assertTrue( "Error: m and t differ on a key (" + o + ", in keySet()) after removal (iterating on t)", m.keySet().contains( o ) );
		}
		/* Now we check that m actually holds the same keys, but iterating on m. */
		for ( java.util.Iterator i = m.keySet().iterator(); i.hasNext(); ) {
			Object o = i.next();
			assertTrue( "Error: m and t differ on a key after removal (iterating on m)", t.containsKey( o ) );
			assertTrue( "Error: m and t differ on a key (in keySet()) after removal (iterating on m)", t.keySet().contains( o ) );
		}
		/* Now we check that m actually hold the same values. */
		for ( java.util.Iterator i = t.values().iterator(); i.hasNext(); ) {
			Object o = i.next();
			assertTrue( "Error: m and t differ on a value after removal (iterating on t)", m.containsValue( o ) );
			assertTrue( "Error: m and t differ on a value (in values()) after removal (iterating on t)", m.values().contains( o ) );
		}
		/* Now we check that m actually hold the same values, but iterating on m. */
		for ( java.util.Iterator i = m.values().iterator(); i.hasNext(); ) {
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
		m = (Int2IntOpenHashMap)ois.readObject();
		ois.close();
		ff.delete();
		assertEquals( "Error: hashCode() changed after save/read", m.hashCode(), h );
		/* Now we check that m actually holds that data. */
		for ( java.util.Iterator i = t.keySet().iterator(); i.hasNext(); ) {
			Object o = i.next();
			assertTrue( "Error: m and t differ on an entry after save/read", valEquals( m.get( o ), t.get( o ) ) );
		}
		/* Now we put and remove random data in m and t, checking that the result is the same. */
		for ( int i = 0; i < 20 * n; i++ ) {
			int T = genKey();
			int U = genValue();
			assertTrue( "Error: divergence in put() between t and m after save/read",
					valEquals( m.put( ( Integer.valueOf( T ) ), ( Integer.valueOf( U ) ) ), t.put( ( Integer.valueOf( T ) ), ( Integer.valueOf( U ) ) ) ) );
			T = genKey();
			Integer result;
			assertTrue( "Error: divergence in remove() between t and m after save/read", valEquals( m.remove( T ), ( result = (Integer)t.remove( ( Integer.valueOf( T ) ) ) ) != null ? result.intValue() : 0 ) );
		}
		assertTrue( "Error: !m.equals(t) after post-save/read removal", m.equals( t ) );
		assertTrue( "Error: !t.equals(m) after post-save/read removal", t.equals( m ) );
		/*
		 * Now we take out of m everything , and check that it is empty.
		 */
		for ( java.util.Iterator i = t.keySet().iterator(); i.hasNext(); )
			m.remove( i.next() );
		assertTrue( "Error: m is not empty (as it should be)", m.isEmpty() );
		m = new Int2IntOpenHashMap( n, f );
		t.clear();
		for( int i = n; i-- != 0; ) m.put( i, 1 );
		t.putAll( m );
		for( int i = n; i-- != 0; ) assertEquals( "Error: m and t differ on a key during torture-test insertion.", m.put( i, 2 ), t.put( Integer.valueOf( i ), 2 ) );	
		
		assertTrue( "Error: !m.equals(t) after torture-test removal", m.equals( t ) );
		assertTrue( "Error: !t.equals(m) after torture-test removal", t.equals( m ) );
		assertTrue( "Error: !m.equals(m.clone()) after torture-test removal", m.equals( m.clone() ) );
		assertTrue( "Error: !m.clone().equals(m) after torture-test removal", m.clone().equals( m ) );
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
		Int2IntOpenHashMap m = new Int2IntOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
		assertEquals( 0, m.addTo( 0, 2 ) );
		assertEquals( 2, m.get( 0 ) );
		assertEquals( 2, m.addTo( 0, 3 ) );
		assertEquals( 5, m.get( 0 ) );
		m.defaultReturnValue( -1 );
		assertEquals( -1, m.addTo( 1, 1 ) );
		assertEquals( 0, m.get( 1 ) );
		assertEquals( 0, m.addTo( 1, 1 ) );
		assertEquals( 1, m.get( 1 ) );
		assertEquals( 1, m.addTo( 1, -2 ) );
		assertEquals( -1, m.get( 1 ) );
		
	}

	@Test
	public void testRemove() {
		Int2IntOpenHashMap s = new Int2IntOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
		s.defaultReturnValue( -1 );
		for( int i = 0; i < 100; i++ ) assertEquals( -1, s.put( i, i ) );
		for( int i = 0; i < 100; i++ ) assertEquals( -1, s.remove( 100 + i ) );
		for( int i = 50; i < 150; i++ ) assertEquals( Integer.toString( i % 100 ), i % 100, s.remove( i % 100 ) );
	}

	@Test
	public void testRemove0() {
		Int2IntOpenHashMap s = new Int2IntOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
		s.defaultReturnValue( -1 );
		for( int i = -1; i <= 1; i++ ) assertEquals( -1, s.put( i, i ) );
		assertEquals( 0, s.remove( 0 ) );
		IntIterator iterator = s.keySet().iterator();
		assertEquals( 1, iterator.nextInt() );
		assertEquals( -1, iterator.nextInt() );
		assertFalse( iterator.hasNext() );
		
		s = new Int2IntOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
		s.defaultReturnValue( -1 );
		for( int i = -1; i <= 1; i++ ) assertEquals( -1, s.put( i, i ) );
		iterator = s.keySet().iterator();
		while( iterator.hasNext() ) if ( iterator.nextInt() == 0 ) iterator.remove();
		
		assertFalse( s.containsKey( 0 ) );
		assertEquals( -1, s.get( 0 ) );
		
		iterator = s.keySet().iterator();
		int[] content = new int[ 2 ];
		content[ 0 ] = iterator.nextInt();
		content[ 1 ] = iterator.nextInt();
		assertFalse( iterator.hasNext() );
		Arrays.sort( content );
		assertArrayEquals( new int[] { -1, 1 }, content );
	}


	@Test
	public void testEntrySet() {
		Int2IntOpenHashMap s = new Int2IntOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
		s.defaultReturnValue( -1 );
		for( int i = 0; i < 100; i++ ) assertEquals( -1, s.put( i, i ) );
		for( int i = 0; i < 100; i++ ) assertTrue( s.entrySet().contains( new AbstractInt2IntMap.BasicEntry( 0, 0 ) ) );
		for( int i = 0; i < 100; i++ ) assertFalse( s.entrySet().contains( new AbstractInt2IntMap.BasicEntry( i, -1 ) ) );
		for( int i = 0; i < 100; i++ ) assertTrue( s.entrySet().contains( new AbstractInt2IntMap.BasicEntry( i, i ) ) );
		for( int i = 0; i < 100; i++ ) assertFalse( s.entrySet().remove( new AbstractInt2IntMap.BasicEntry( i, -1 ) ) );
		for( int i = 0; i < 100; i++ ) assertTrue( s.entrySet().remove( new AbstractInt2IntMap.BasicEntry( i, i ) ) );
		assertTrue( s.entrySet().isEmpty() );
	}
	
	@Test
	public void testFastIterator() {
		Int2IntOpenHashMap s = new Int2IntOpenHashMap( Hash.DEFAULT_INITIAL_SIZE );
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
