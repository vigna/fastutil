package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.unimi.dsi.fastutil.Hash;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("rawtypes")
public class IntLinkedOpenHashSetTest {

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
		IntLinkedOpenHashSet instance = new IntLinkedOpenHashSet(initialElements);
		IntRBTreeSet referenceInstance = new IntRBTreeSet(initialElements);

		instance.retainAll(retainElements);
		referenceInstance.retainAll(retainElements);

		// print the correct result {586}
		System.out.println("ref: " + referenceInstance);

		// prints {586, 7379}, which is clearly wrong
		System.out.println("ohm: " + instance);

		// Fails
		assertEquals( referenceInstance, instance );
	}

	private static java.util.Random r = new java.util.Random( 0 );

	private static int genKey() {
		return r.nextInt();
	}

	@SuppressWarnings("unchecked")
	private static void test( int n, float f ) throws IOException, ClassNotFoundException {
		int c;
		IntLinkedOpenHashSet s = new IntLinkedOpenHashSet( Hash.DEFAULT_INITIAL_SIZE, f );
		java.util.Set<Integer> t = new java.util.LinkedHashSet<Integer>();
		/* First of all, we fill t with random data. */
		for ( int i = 0; i < f * n; i++ )
			t.add( ( Integer.valueOf( genKey() ) ) );
		/* Now we add to m the same data */
		s.addAll( t );
		assertTrue( "Error: !m.equals(t) after insertion", s.equals( t ) );
		assertTrue( "Error: !t.equals(m) after insertion", t.equals( s ) );
		/* Now we check that m actually holds that data. */
		for ( java.util.Iterator i = t.iterator(); i.hasNext(); ) {
			Object e = i.next();
			assertTrue( "Error: m and t differ on a key (" + e + ") after insertion (iterating on t)", s.contains( e ) );
		}
		/* Now we check that m actually holds that data, but iterating on m. */
		c = 0;
		for ( java.util.Iterator i = s.iterator(); i.hasNext(); ) {
			Object e = i.next();
			c++;
			assertTrue( "Error: m and t differ on a key (" + e + ") after insertion (iterating on m)", t.contains( e ) );
		}
		assertEquals( "Error: m has only " + c + " keys instead of " + t.size() + " after insertion (iterating on m)", t.size(), c );
		/*
		 * Now we check that inquiries about random data give the same answer in m and t. For m we
		 * use the polymorphic method.
		 */
		for ( int i = 0; i < n; i++ ) {
			int T = genKey();
			assertTrue( "Error: divergence in keys between t and m (polymorphic method)", s.contains( T ) == t.contains( ( Integer.valueOf( T ) ) ) );
		}
		/*
		 * Again, we check that inquiries about random data give the same answer in m and t, but for
		 * m we use the standard method.
		 */
		for ( int i = 0; i < n; i++ ) {
			int T = genKey();
			assertTrue( "Error: divergence between t and m (standard method)", s.contains( ( Integer.valueOf( T ) ) ) == t.contains( ( Integer.valueOf( T ) ) ) );
		}
		/* Now we put and remove random data in m and t, checking that the result is the same. */
		for ( int i = 0; i < 20 * n; i++ ) {
			int T = genKey();
			assertTrue( "Error: divergence in add() between t and m", s.add( ( Integer.valueOf( T ) ) ) == t.add( ( Integer.valueOf( T ) ) ) );
			T = genKey();
			assertTrue( "Error: divergence in remove() between t and m", s.remove( ( Integer.valueOf( T ) ) ) == t.remove( ( Integer.valueOf( T ) ) ) );
		}
		assertTrue( "Error: !m.equals(t) after removal", s.equals( t ) );
		assertTrue( "Error: !t.equals(m) after removal", t.equals( s ) );
		/* Now we check that m actually holds that data. */
		for ( java.util.Iterator i = t.iterator(); i.hasNext(); ) {
			Object e = i.next();
			assertTrue( "Error: m and t differ on a key (" + e + ") after removal (iterating on t)", s.contains( e ) );
		}
		/* Now we check that m actually holds that data, but iterating on m. */
		for ( java.util.Iterator i = s.iterator(); i.hasNext(); ) {
			Object e = i.next();
			assertTrue( "Error: m and t differ on a key (" + e + ") after removal (iterating on m)", t.contains( e ) );
		}
		/* Now we make m into an array, make it again a set and check it is OK. */
		int a[] = s.toIntArray();
		assertEquals( "Error: toArray() output (or array-based constructor) is not OK", new IntLinkedOpenHashSet( a ), s );
		/* Now we check cloning. */
		assertTrue( "Error: m does not equal m.clone()", s.equals( s.clone() ) );
		assertTrue( "Error: m.clone() does not equal m", s.clone().equals( s ) );
		int h = s.hashCode();
		/* Now we save and read m. */
		java.io.File ff = new java.io.File( "it.unimi.dsi.fastutil.test" );
		java.io.OutputStream os = new java.io.FileOutputStream( ff );
		java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream( os );
		oos.writeObject( s );
		oos.close();
		java.io.InputStream is = new java.io.FileInputStream( ff );
		java.io.ObjectInputStream ois = new java.io.ObjectInputStream( is );
		s = (IntLinkedOpenHashSet)ois.readObject();
		ois.close();
		ff.delete();
		assertEquals( "Error: hashCode() changed after save/read", h, s.hashCode() );

		assertEquals( "Error: clone()", s, s.clone() );
		/* Now we check that m actually holds that data, but iterating on m. */
		for ( java.util.Iterator i = s.iterator(); i.hasNext(); ) {
			Object e = i.next();
			assertTrue( "Error: m and t differ on a key (" + e + ") after save/read", t.contains( e ) );
		}
		/* Now we put and remove random data in m and t, checking that the result is the same. */
		for ( int i = 0; i < 20 * n; i++ ) {
			int T = genKey();
			assertTrue( "Error: divergence in add() between t and m after save/read", s.add( ( Integer.valueOf( T ) ) ) == t.add( ( Integer.valueOf( T ) ) ) );
			T = genKey();
			assertTrue( "Error: divergence in remove() between t and m after save/read", s.remove( ( Integer.valueOf( T ) ) ) == t.remove( ( Integer.valueOf( T ) ) ) );
		}
		assertTrue( "Error: !m.equals(t) after post-save/read removal", s.equals( t ) );
		assertTrue( "Error: !t.equals(m) after post-save/read removal", t.equals( s ) );
		/* Now we play with iterators, but only in the linked case. */
		{
			java.util.ListIterator<Integer> i, j;
			Integer J = null;
			i = s.iterator();
			j = new java.util.LinkedList<Integer>( t ).listIterator();
			for ( int k = 0; k < 2 * n; k++ ) {
				assertTrue( "Error: divergence in hasNext()", i.hasNext() == j.hasNext() );
				assertTrue( "Error: divergence in hasPrevious()", i.hasPrevious() == j.hasPrevious() );
				if ( r.nextFloat() < .8 && i.hasNext() ) {
					assertTrue( "Error: divergence in next()", i.next().equals( J = j.next() ) );
					if ( r.nextFloat() < 0.5 ) {
						i.remove();
						j.remove();
						t.remove( J );
					}
				}
				else if ( r.nextFloat() < .2 && i.hasPrevious() ) {
					assertTrue( "Error: divergence in previous()", i.previous().equals( J = j.previous() ) );
					if ( r.nextFloat() < 0.5 ) {
						i.remove();
						j.remove();
						t.remove( J );
					}
				}
				assertTrue( "Error: divergence in nextIndex()", i.nextIndex() == j.nextIndex() );
				assertTrue( "Error: divergence in previousIndex()", i.previousIndex() == j.previousIndex() );
			}
		}
		if ( t.size() > 0 ) {
			java.util.ListIterator i, j;
			Object J = null;
			j = new java.util.LinkedList( t ).listIterator();
			int e = r.nextInt( t.size() );
			Object from;
			do
				from = j.next();
			while ( e-- != 0 );
			i = s.iterator( ( ( ( (Integer)( from ) ).intValue() ) ) );
			for ( int k = 0; k < 2 * n; k++ ) {
				assertTrue( "Error: divergence in hasNext() (iterator with starting point " + from + ")", i.hasNext() == j.hasNext() );
				assertTrue( "Error: divergence in hasPrevious() (iterator with starting point " + from + ")", i.hasPrevious() == j.hasPrevious() );
				if ( r.nextFloat() < .8 && i.hasNext() ) {
					assertTrue( "Error: divergence in next() (iterator with starting point " + from + ")", i.next().equals( J = j.next() ) );
					if ( r.nextFloat() < 0.5 ) {
						i.remove();
						j.remove();
						t.remove( J );
					}
				}
				else if ( r.nextFloat() < .2 && i.hasPrevious() ) {
					assertTrue( "Error: divergence in previous() (iterator with starting point " + from + ")", i.previous().equals( J = j.previous() ) );
					if ( r.nextFloat() < 0.5 ) {
						i.remove();
						j.remove();
						t.remove( J );
					}
				}
				assertTrue( "Error: divergence in nextIndex() (iterator with starting point " + from + ")", i.nextIndex() == j.nextIndex() );
				assertTrue( "Error: divergence in previousIndex() (iterator with starting point " + from + ")", i.previousIndex() == j.previousIndex() );
			}
		}
		/* Now we check that m actually holds that data. */
		assertTrue( "Error: ! m.equals( t ) after iteration", s.equals( t ) );
		assertTrue( "Error: ! t.equals( m ) after iteration", t.equals( s ) );
		/* Now we take out of m everything, and check that it is empty. */
		for ( java.util.Iterator i = s.iterator(); i.hasNext(); ) {
			i.next();
			i.remove();
		}
		assertTrue( "Error: m is not empty (as it should be)", s.isEmpty() );
		s.clear();
		t.clear();
		s.trim();
		assertTrue( "Error: !m.equals(t) after rehash()", s.equals( t ) );
		assertTrue( "Error: !t.equals(m) after rehash()", t.equals( s ) );
		s.trim();
		assertTrue( "Error: !m.equals(t) after trim()", s.equals( t ) );
		assertTrue( "Error: !t.equals(m) after trim()", t.equals( s ) );
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
	public void testAdd() {
		IntLinkedOpenHashSet s = new IntLinkedOpenHashSet( Hash.DEFAULT_INITIAL_SIZE );
		assertTrue( s.add( 0 ) );
		assertTrue( s.contains( 0 ) );
		assertFalse( s.contains( 1 ) );
		assertTrue( s.add( Integer.valueOf( 1 ) ) );
		assertTrue( s.contains( Integer.valueOf( 1 ) ) );
		assertFalse( s.contains( Integer.valueOf( 2 ) ) );
	}

	@Test
	public void testRemove() {
		IntLinkedOpenHashSet s = new IntLinkedOpenHashSet( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = 0; i < 100; i++ ) assertTrue( s.add( i ) );
		for( int i = 0; i < 100; i++ ) assertFalse( s.remove( 100 + i ) );
		assertEquals( 0, s.firstInt() );
		assertEquals( 99, s.lastInt() );
		for( int i = 50; i < 150; i++ ) assertTrue( Integer.toString( i % 100 ), s.remove( i % 100 ) );
	}

	@Test
	public void testRemove0() {
		IntLinkedOpenHashSet s = new IntLinkedOpenHashSet( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = -1; i <= 1; i++ ) assertTrue( s.add( i ) );
		assertTrue( s.remove( 0 ) );
		IntListIterator iterator = s.iterator();
		assertEquals( -1, iterator.nextInt() );
		assertEquals( 1, iterator.nextInt() );
		assertFalse( iterator.hasNext() );
		
		s = new IntLinkedOpenHashSet( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = -1; i <= 1; i++ ) assertTrue( s.add( i ) );
		iterator = s.iterator();
		assertEquals( -1, iterator.nextInt() );
		assertEquals( 0, iterator.nextInt() );
		iterator.remove();
		assertEquals( 1, iterator.nextInt() );
		assertFalse( iterator.hasNext() );

		assertFalse( s.contains( 0 ) );
		
		iterator = s.iterator();
		assertEquals( -1, iterator.nextInt() );
		assertEquals( 1, iterator.nextInt() );
		assertFalse( iterator.hasNext() );
	}

	@Test
	public void testFirtLast0() {
		IntLinkedOpenHashSet s;
		
		s = new IntLinkedOpenHashSet( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = 0; i < 100; i++ ) assertTrue( s.add( i ) );
		for( int i = 0; i < 100; i++ ) assertEquals( i, s.removeFirstInt() );
		assertTrue( s.isEmpty() );
		
		s = new IntLinkedOpenHashSet( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = 0; i < 100; i++ ) assertTrue( s.add( i ) );
		for( int i = 100; i-- != 0; ) assertEquals( i, s.removeLastInt() );
		assertTrue( s.isEmpty() );

		s = new IntLinkedOpenHashSet( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = 100; i-- != 0; ) assertTrue( s.add( i ) );
		for( int i = 0; i < 100; i++ ) assertEquals( i, s.removeLastInt() );
		assertTrue( s.isEmpty() );

		s = new IntLinkedOpenHashSet( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = 100; i-- != 0; ) assertTrue( s.add( i ) );
		for( int i = 100; i-- != 0; ) assertEquals( i, s.removeFirstInt() );
		assertTrue( s.isEmpty() );
	}


	@Test
	public void testIterator() {
		IntLinkedOpenHashSet s = new IntLinkedOpenHashSet( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = 0; i < 100; i++ ) assertTrue( s.add( i ) );
		assertEquals( 0, s.firstInt() );
		
		IntListIterator iterator = s.iterator();
		for( int i = 0; i <= 100; i++ ) {
			assertEquals( Integer.toString( i ), i - 1, iterator.previousIndex() );
			assertEquals( Integer.toString( i ), i, iterator.nextIndex() );
			if ( i != 100 ) assertEquals( Integer.toString( i ), i, iterator.nextInt() );
		}

		iterator = s.iterator( s.lastInt() );
		for( int i = 100; i-- != 0; ) {
			assertEquals( Integer.toString( i ), i, iterator.previousIndex() );
			assertEquals( Integer.toString( i ), i + 1, iterator.nextIndex() );
			if ( i != 0 ) assertEquals( Integer.toString( i ), i, iterator.previousInt() );
		}

		iterator = s.iterator( 50 );
		for( int i = 50; i < 100; i++ ) {
			assertEquals( Integer.toString( i ), i, iterator.previousIndex() );
			assertEquals( Integer.toString( i ), i + 1, iterator.nextIndex() );
			if ( i != 99 ) assertEquals( Integer.toString( i ), i + 1, iterator.nextInt() );
		}

		iterator = s.iterator( 50 );
		for( int i = 50; i-- != -1; ) {
			assertEquals( Integer.toString( i ), i + 1, iterator.previousIndex() );
			assertEquals( Integer.toString( i ), i + 2, iterator.nextIndex() );
			if ( i != -1 ) assertEquals( Integer.toString( i ), i + 1, iterator.previousInt() );
		}

		iterator = s.iterator( 50 );
		for( int i = 50; i-- != -1; ) assertEquals( Integer.toString( i ), i + 1, iterator.previousInt() );
		assertEquals( -1, iterator.previousIndex() );
		assertEquals( 0, iterator.nextIndex() );
		
		iterator = s.iterator( 50 );
		for( int i = 50; i < 100 - 1; i++ ) assertEquals( Integer.toString( i ), i + 1, iterator.nextInt() );
		assertEquals( 99, iterator.previousIndex() );
		assertEquals( 100, iterator.nextIndex() );

		iterator = s.iterator( 50 );
		iterator.previousInt();
		iterator.remove();
		assertEquals( 49, iterator.previousIndex() );
		assertEquals( 49, iterator.previousInt() );
		
		iterator = s.iterator( 49 );
		iterator.nextInt();
		iterator.remove();
		assertEquals( 50, iterator.nextIndex() );
		assertEquals( 52, iterator.nextInt() );
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testIteratorMissingElement() {
		IntLinkedOpenHashSet s = new IntLinkedOpenHashSet( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = 0; i < 100; i++ ) assertTrue( s.add( i ) );
		s.iterator( 1000 );
	}

	@Test
	public void testPutAndMove() {
		IntLinkedOpenHashSet s = new IntLinkedOpenHashSet( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = 0; i < 100; i++ ) assertTrue( s.addAndMoveToFirst( i ) );
		s.clear();
		for( int i = 0; i < 100; i++ ) assertTrue( s.addAndMoveToLast( i ) );
		assertTrue( s.addAndMoveToFirst( -1 ) );
		assertEquals( -1, s.firstInt() );
		assertTrue( s.addAndMoveToFirst( -2 ) );
		assertEquals( -2, s.firstInt() );
		assertFalse( s.addAndMoveToFirst( -1 ) );
		assertEquals( -1, s.firstInt() );
		assertFalse( s.addAndMoveToFirst( -1 ) );
		assertEquals( -1, s.firstInt() );
		assertFalse( s.addAndMoveToLast( -1 ) );
		assertEquals( -1, s.lastInt() );
		assertTrue( s.addAndMoveToLast( 100 ) );
		assertEquals( 100, s.lastInt() );
		assertTrue( s.addAndMoveToLast( 101 ) );
		assertEquals( 101, s.lastInt() );
		assertFalse( s.addAndMoveToLast( 100 ) );
		assertEquals( 100, s.lastInt() );
		assertFalse( s.addAndMoveToLast( 100 ) );
		assertEquals( 100, s.lastInt() );
		assertFalse( s.addAndMoveToFirst( 100 ) );
		assertEquals( 100, s.firstInt() );
	}

	@Test
	public void testRemoveFirstLast() {
		IntLinkedOpenHashSet s = new IntLinkedOpenHashSet( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = 0; i < 100; i++ ) assertTrue( s.add( i ) );
		assertEquals( 0, s.removeFirstInt() );
		assertEquals( 1, s.removeFirstInt() );
		assertEquals( 99, s.removeLastInt() );
	}	

	@Test(expected=NoSuchElementException.class)
	public void testRemoveFirstEmpty() {
		new IntLinkedOpenHashSet( Hash.DEFAULT_INITIAL_SIZE ).firstInt();
	}

	@Test(expected=NoSuchElementException.class)
	public void testRemoveLastEmpty() {
		new IntLinkedOpenHashSet( Hash.DEFAULT_INITIAL_SIZE ).lastInt();
	}
}
