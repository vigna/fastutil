package it.unimi.dsi.fastutil.ints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.unimi.dsi.fastutil.Hash;

import java.io.IOException;
import java.util.ListIterator;

import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("rawtypes")
public class IntLinkedOpenHashSetTest {

	private static java.util.Random r = new java.util.Random( 0 );

	private static int genKey() {
		return r.nextInt();
	}

	@SuppressWarnings("unchecked")
	private static void test( int n, float f ) throws IOException, ClassNotFoundException {
		int c;
		IntLinkedOpenHashSet m = new IntLinkedOpenHashSet( Hash.DEFAULT_INITIAL_SIZE, f );
		java.util.Set<Integer> t = new java.util.LinkedHashSet<Integer>();
		/* First of all, we fill t with random data. */
		for ( int i = 0; i < f * n; i++ )
			t.add( ( Integer.valueOf( genKey() ) ) );
		/* Now we add to m the same data */
		m.addAll( t );
		assertTrue( "Error: !m.equals(t) after insertion", m.equals( t ) );
		assertTrue( "Error: !t.equals(m) after insertion", t.equals( m ) );
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
		assertEquals( "Error: m has only " + c + " keys instead of " + t.size() + " after insertion (iterating on m)", t.size(), c );
		/*
		 * Now we check that inquiries about random data give the same answer in m and t. For m we
		 * use the polymorphic method.
		 */
		for ( int i = 0; i < n; i++ ) {
			int T = genKey();
			assertTrue( "Error: divergence in keys between t and m (polymorphic method)", m.contains( T ) == t.contains( ( Integer.valueOf( T ) ) ) );
		}
		/*
		 * Again, we check that inquiries about random data give the same answer in m and t, but for
		 * m we use the standard method.
		 */
		for ( int i = 0; i < n; i++ ) {
			int T = genKey();
			assertTrue( "Error: divergence between t and m (standard method)", m.contains( ( Integer.valueOf( T ) ) ) == t.contains( ( Integer.valueOf( T ) ) ) );
		}
		/* Now we put and remove random data in m and t, checking that the result is the same. */
		for ( int i = 0; i < 20 * n; i++ ) {
			int T = genKey();
			assertTrue( "Error: divergence in add() between t and m", m.add( ( Integer.valueOf( T ) ) ) == t.add( ( Integer.valueOf( T ) ) ) );
			T = genKey();
			assertTrue( "Error: divergence in remove() between t and m", m.remove( ( Integer.valueOf( T ) ) ) == t.remove( ( Integer.valueOf( T ) ) ) );
		}
		assertTrue( "Error: !m.equals(t) after removal", m.equals( t ) );
		assertTrue( "Error: !t.equals(m) after removal", t.equals( m ) );
		/* Now we check that m actually holds that data. */
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
		int a[] = m.toIntArray();
		assertEquals( "Error: toArray() output (or array-based constructor) is not OK", new IntLinkedOpenHashSet( a ), m );
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
		m = (IntLinkedOpenHashSet)ois.readObject();
		ois.close();
		ff.delete();
		assertEquals( "Error: hashCode() changed after save/read", h, m.hashCode() );

		assertEquals( "Error: clone()", m, m.clone() );
		/* Now we check that m actually holds that data, but iterating on m. */
		for ( java.util.Iterator i = m.iterator(); i.hasNext(); ) {
			Object e = i.next();
			assertTrue( "Error: m and t differ on a key (" + e + ") after save/read", t.contains( e ) );
		}
		/* Now we put and remove random data in m and t, checking that the result is the same. */
		for ( int i = 0; i < 20 * n; i++ ) {
			int T = genKey();
			assertTrue( "Error: divergence in add() between t and m after save/read", m.add( ( Integer.valueOf( T ) ) ) == t.add( ( Integer.valueOf( T ) ) ) );
			T = genKey();
			assertTrue( "Error: divergence in remove() between t and m after save/read", m.remove( ( Integer.valueOf( T ) ) ) == t.remove( ( Integer.valueOf( T ) ) ) );
		}
		assertTrue( "Error: !m.equals(t) after post-save/read removal", m.equals( t ) );
		assertTrue( "Error: !t.equals(m) after post-save/read removal", t.equals( m ) );
		/* Now we play with iterators, but only in the linked case. */
		{
			java.util.ListIterator<Integer> i, j;
			Integer J;
			i = (ListIterator<Integer>)m.iterator();
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
			Object J;
			j = new java.util.LinkedList( t ).listIterator();
			int e = r.nextInt( t.size() );
			Object from;
			do
				from = j.next();
			while ( e-- != 0 );
			i = (java.util.ListIterator)m.iterator( ( ( ( (Integer)( from ) ).intValue() ) ) );
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
		assertTrue( "Error: ! m.equals( t ) after iteration", m.equals( t ) );
		assertTrue( "Error: ! t.equals( m ) after iteration", t.equals( m ) );
		/* Now we take out of m everything, and check that it is empty. */
		for ( java.util.Iterator i = m.iterator(); i.hasNext(); ) {
			i.next();
			i.remove();
		}
		assertTrue( "Error: m is not empty (as it should be)", m.isEmpty() );
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
	public void testAdd() {
		IntLinkedOpenHashSet m = new IntLinkedOpenHashSet( Hash.DEFAULT_INITIAL_SIZE );
		assertTrue( m.add( 0 ) );
		assertTrue( m.contains( 0 ) );
		assertFalse( m.contains( 1 ) );
		assertTrue( m.add( Integer.valueOf( 1 ) ) );
		assertTrue( m.contains( Integer.valueOf( 1 ) ) );
		assertFalse( m.contains( Integer.valueOf( 2 ) ) );
	}

	@Test
	public void testRemove() {
		IntLinkedOpenHashSet m = new IntLinkedOpenHashSet( Hash.DEFAULT_INITIAL_SIZE );
		for( int i = 0; i < 100; i++ ) assertTrue( m.add( i ) );
		for( int i = 0; i < 100; i++ ) assertFalse( m.remove( 100 + i ) );
		assertEquals( 0, m.firstInt() );
		assertEquals( 99, m.lastInt() );
		for( int i = 50; i < 150; i++ ) assertTrue( m.remove( i % 100 ) );
	}
}
