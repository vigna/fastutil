package it.unimi.dsi.fastutil.objects;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Ignore;
import org.junit.Test;

@SuppressWarnings("rawtypes")
public class ObjectBigArrayBigListTest {

	private static java.util.Random r = new java.util.Random( 0 );

	private static int genKey() {
		return r.nextInt();
	}

	private static Object[] k, nk;

	private static Object kt[];

	private static Object nkt[];

	@SuppressWarnings({ "unchecked", "boxing" })
	protected static void testLists( ObjectBigList m, ObjectBigList t, int n, int level ) {
		Exception mThrowsOutOfBounds, tThrowsOutOfBounds;
		Object rt = null;
		Object rm = ( null );
		if ( level > 4 ) return;
		/* Now we check that both sets agree on random keys. For m we use the polymorphic method. */
		for ( int i = 0; i < n; i++ ) {
			int p = r.nextInt() % ( n * 2 );
			Object T = genKey();
			mThrowsOutOfBounds = tThrowsOutOfBounds = null;
			try {
				m.set( p, T );
			}
			catch ( IndexOutOfBoundsException e ) {
				mThrowsOutOfBounds = e;
			}
			try {
				t.set( p, ( T ) );
			}
			catch ( IndexOutOfBoundsException e ) {
				tThrowsOutOfBounds = e;
			}
			assertTrue( "Error (" + level + "): set() divergence at start in IndexOutOfBoundsException for index " + p + "  (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")",
					( mThrowsOutOfBounds == null ) == ( tThrowsOutOfBounds == null ) );
			if ( mThrowsOutOfBounds == null ) assertTrue( "Error (" + level + "): m and t differ after set() on position " + p + " (" + m.get( p ) + ", " + t.get( p ) + ")",
					t.get( p ).equals( ( m.get( p ) ) ) );
			p = r.nextInt() % ( n * 2 );
			mThrowsOutOfBounds = tThrowsOutOfBounds = null;
			try {
				m.get( p );
			}
			catch ( IndexOutOfBoundsException e ) {
				mThrowsOutOfBounds = e;
			}
			try {
				t.get( p );
			}
			catch ( IndexOutOfBoundsException e ) {
				tThrowsOutOfBounds = e;
			}
			assertTrue( "Error (" + level + "): get() divergence at start in IndexOutOfBoundsException for index " + p + "  (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")",
					( mThrowsOutOfBounds == null ) == ( tThrowsOutOfBounds == null ) );
			if ( mThrowsOutOfBounds == null ) assertTrue( "Error (" + level + "): m and t differ aftre get() on position " + p + " (" + m.get( p ) + ", " + t.get( p ) + ")",
					t.get( p ).equals( ( m.get( p ) ) ) );
		}
		/* Now we check that both sets agree on random keys. For m we use the standard method. */
		for ( int i = 0; i < n; i++ ) {
			int p = r.nextInt() % ( n * 2 );
			mThrowsOutOfBounds = tThrowsOutOfBounds = null;
			try {
				m.get( p );
			}
			catch ( IndexOutOfBoundsException e ) {
				mThrowsOutOfBounds = e;
			}
			try {
				t.get( p );
			}
			catch ( IndexOutOfBoundsException e ) {
				tThrowsOutOfBounds = e;
			}
			assertTrue( "Error (" + level + "): get() divergence at start in IndexOutOfBoundsException for index " + p + "  (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")",
					( mThrowsOutOfBounds == null ) == ( tThrowsOutOfBounds == null ) );
			if ( mThrowsOutOfBounds == null ) assertTrue( "Error (" + level + "): m and t differ at start on position " + p + " (" + m.get( p ) + ", " + t.get( p ) + ")", t.get( p )
					.equals( m.get( p ) ) );
		}
		/* Now we check that m and t are equal. */
		if ( !m.equals( t ) || !t.equals( m ) ) System.err.println( "m: " + m + " t: " + t );
		assertTrue( "Error (" + level + "): ! m.equals( t ) at start", m.equals( t ) );
		assertTrue( "Error (" + level + "): ! t.equals( m ) at start", t.equals( m ) );
		/* Now we check that m actually holds that data. */
		for ( Iterator i = t.iterator(); i.hasNext(); ) {
			assertTrue( "Error (" + level + "): m and t differ on an entry after insertion (iterating on t)", m.contains( i.next() ) );
		}
		/* Now we check that m actually holds that data, but iterating on m. */
		for ( Iterator i = m.listIterator(); i.hasNext(); ) {
			assertTrue( "Error (" + level + "): m and t differ on an entry after insertion (iterating on m)", t.contains( i.next() ) );
		}
		/*
		 * Now we check that inquiries about random data give the same answer in m and t. For m we
		 * use the polymorphic method.
		 */
		for ( int i = 0; i < n; i++ ) {
			Object T = genKey();
			assertTrue( "Error (" + level + "): divergence in content between t and m (polymorphic method)", m.contains( T ) == t.contains( ( T ) ) );
		}
		/*
		 * Again, we check that inquiries about random data give the same answer in m and t, but for
		 * m we use the standard method.
		 */
		for ( int i = 0; i < n; i++ ) {
			Object T = genKey();
			assertTrue( "Error (" + level + "): divergence in content between t and m (polymorphic method)", m.contains( ( T ) ) == t.contains( ( T ) ) );
		}
		/* Now we add and remove random data in m and t, checking that the result is the same. */
		for ( int i = 0; i < 2 * n; i++ ) {
			Object T = genKey();
			try {
				m.add( T );
			}
			catch ( IndexOutOfBoundsException e ) {
				mThrowsOutOfBounds = e;
			}
			try {
				t.add( ( T ) );
			}
			catch ( IndexOutOfBoundsException e ) {
				tThrowsOutOfBounds = e;
			}
			T = genKey();
			int p = r.nextInt() % ( 2 * n + 1 );
			mThrowsOutOfBounds = tThrowsOutOfBounds = null;
			try {
				m.add( p, T );
			}
			catch ( IndexOutOfBoundsException e ) {
				mThrowsOutOfBounds = e;
			}
			try {
				t.add( p, ( T ) );
			}
			catch ( IndexOutOfBoundsException e ) {
				tThrowsOutOfBounds = e;
			}
			assertTrue( "Error (" + level + "): add() divergence in IndexOutOfBoundsException for index " + p + " for " + T + " (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")",
					( mThrowsOutOfBounds == null ) == ( tThrowsOutOfBounds == null ) );
			p = r.nextInt() % ( 2 * n + 1 );
			mThrowsOutOfBounds = tThrowsOutOfBounds = null;
			try {
				rm = m.remove( p );
			}
			catch ( IndexOutOfBoundsException e ) {
				mThrowsOutOfBounds = e;
			}
			try {
				rt = t.remove( p );
			}
			catch ( IndexOutOfBoundsException e ) {
				tThrowsOutOfBounds = e;
			}
			assertTrue( "Error (" + level + "): remove() divergence in IndexOutOfBoundsException for index " + p + " (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")",
					( mThrowsOutOfBounds == null ) == ( tThrowsOutOfBounds == null ) );
			if ( mThrowsOutOfBounds == null ) assertTrue( "Error (" + level + "): divergence in remove() between t and m (" + rt + ", " + rm + ")", rt.equals( ( rm ) ) );
		}
		assertTrue( "Error (" + level + "): ! m.equals( t ) after add/remove", m.equals( t ) );
		assertTrue( "Error (" + level + "): ! t.equals( m ) after add/remove", t.equals( m ) );
		/*
		 * Now we add random data in m and t using addAll on a collection, checking that the result
		 * is the same.
		 */
		for ( int i = 0; i < n; i++ ) {
			int p = r.nextInt() % ( 2 * n + 1 );
			java.util.Collection m1 = new java.util.ArrayList();
			int s = r.nextInt( n / 2 + 1 );
			for ( int j = 0; j < s; j++ )
				m1.add( ( genKey() ) );
			mThrowsOutOfBounds = tThrowsOutOfBounds = null;
			try {
				m.addAll( p, m1 );
			}
			catch ( IndexOutOfBoundsException e ) {
				mThrowsOutOfBounds = e;
			}
			try {
				t.addAll( p, m1 );
			}
			catch ( IndexOutOfBoundsException e ) {
				tThrowsOutOfBounds = e;
			}
			assertTrue( "Error (" + level + "): addAll() divergence in IndexOutOfBoundsException for index " + p + " for " + m1 + " (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")",
					( mThrowsOutOfBounds == null ) == ( tThrowsOutOfBounds == null ) );
			assertTrue( "Error (" + level + m + t + "): ! m.equals( t ) after addAll", m.equals( t ) );
			assertTrue( "Error (" + level + m + t + "): ! t.equals( m ) after addAll", t.equals( m ) );
		}
		if ( m.size64() > n ) {
			m.size( n );
			while ( t.size() != n )
				t.remove( t.size() - 1 );
		}
		/*
		 * Now we add random data in m and t using addAll on a type-specific collection, checking
		 * that the result is the same.
		 */
		for ( int i = 0; i < n; i++ ) {
			int p = r.nextInt() % ( 2 * n + 1 );
			ObjectCollection m1 = new ObjectBigArrayBigList();
			java.util.Collection t1 = new java.util.ArrayList();
			int s = r.nextInt( n / 2 + 1 );
			for ( int j = 0; j < s; j++ ) {
				Object x = genKey();
				m1.add( x );
				t1.add( ( x ) );
			}
			mThrowsOutOfBounds = tThrowsOutOfBounds = null;
			try {
				m.addAll( p, m1 );
			}
			catch ( IndexOutOfBoundsException e ) {
				mThrowsOutOfBounds = e;
			}
			try {
				t.addAll( p, t1 );
			}
			catch ( IndexOutOfBoundsException e ) {
				tThrowsOutOfBounds = e;
			}
			assertTrue( "Error (" + level + "): polymorphic addAll() divergence in IndexOutOfBoundsException for index " + p + " for " + m1 + " (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds
					+ ")", ( mThrowsOutOfBounds == null ) == ( tThrowsOutOfBounds == null ) );
			assertTrue( "Error (" + level + m + t + "): ! m.equals( t ) after polymorphic addAll", m.equals( t ) );
			assertTrue( "Error (" + level + m + t + "): ! t.equals( m ) after polymorphic addAll", t.equals( m ) );
		}
		if ( m.size64() > n ) {
			m.size( n );
			while ( t.size() != n )
				t.remove( t.size() - 1 );
		}
		/*
		 * Now we add random data in m and t using addAll on a list, checking that the result is the
		 * same.
		 */
		for ( int i = 0; i < n; i++ ) {
			int p = r.nextInt() % ( 2 * n + 1 );
			ObjectBigList m1 = new ObjectBigArrayBigList();
			java.util.Collection t1 = new java.util.ArrayList();
			int s = r.nextInt( n / 2 + 1 );
			for ( int j = 0; j < s; j++ ) {
				Object x = genKey();
				m1.add( x );
				t1.add( ( x ) );
			}
			mThrowsOutOfBounds = tThrowsOutOfBounds = null;
			try {
				m.addAll( p, m1 );
			}
			catch ( IndexOutOfBoundsException e ) {
				mThrowsOutOfBounds = e;
			}
			try {
				t.addAll( p, t1 );
			}
			catch ( IndexOutOfBoundsException e ) {
				tThrowsOutOfBounds = e;
			}
			assertTrue( "Error (" + level + "): list addAll() divergence in IndexOutOfBoundsException for index " + p + " for " + m1 + " (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")",
					( mThrowsOutOfBounds == null ) == ( tThrowsOutOfBounds == null ) );
			assertTrue( "Error (" + level + "): ! m.equals( t ) after list addAll", m.equals( t ) );
			assertTrue( "Error (" + level + "): ! t.equals( m ) after list addAll", t.equals( m ) );
		}
		/* Now we check that both sets agree on random keys. For m we use the standard method. */
		for ( int i = 0; i < n; i++ ) {
			int p = r.nextInt() % ( n * 2 );
			mThrowsOutOfBounds = tThrowsOutOfBounds = null;
			try {
				m.get( p );
			}
			catch ( IndexOutOfBoundsException e ) {
				mThrowsOutOfBounds = e;
			}
			try {
				t.get( p );
			}
			catch ( IndexOutOfBoundsException e ) {
				tThrowsOutOfBounds = e;
			}
			assertTrue( "Error (" + level + "): get() divergence in IndexOutOfBoundsException for index " + p + "  (" + mThrowsOutOfBounds + ", " + tThrowsOutOfBounds + ")",
					( mThrowsOutOfBounds == null ) == ( tThrowsOutOfBounds == null ) );
			if ( mThrowsOutOfBounds == null ) assertTrue( "Error (" + level + "): m and t differ on position " + p + " (" + m.get( p ) + ", " + t.get( p ) + ")", t.get( p ).equals( m.get( p ) ) );
		}
		/* Now we inquiry about the content with indexOf()/lastIndexOf(). */
		for ( int i = 0; i < 10 * n; i++ ) {
			Object T = genKey();
			assertTrue( "Error (" + level + "): indexOf() divergence for " + T + "  (" + m.indexOf( ( T ) ) + ", " + t.indexOf( ( T ) ) + ")", m.indexOf( ( T ) ) == t.indexOf( ( T ) ) );
			assertTrue( "Error (" + level + "): lastIndexOf() divergence for " + T + "  (" + m.lastIndexOf( ( T ) ) + ", " + t.lastIndexOf( ( T ) ) + ")",
					m.lastIndexOf( ( T ) ) == t.lastIndexOf( ( T ) ) );
			assertTrue( "Error (" + level + "): polymorphic indexOf() divergence for " + T + "  (" + m.indexOf( T ) + ", " + t.indexOf( ( T ) ) + ")", m.indexOf( T ) == t.indexOf( ( T ) ) );
			assertTrue( "Error (" + level + "): polymorphic lastIndexOf() divergence for " + T + "  (" + m.lastIndexOf( T ) + ", " + t.lastIndexOf( ( T ) ) + ")",
					m.lastIndexOf( T ) == t.lastIndexOf( ( T ) ) );
		}
		/* Now we check cloning. */
		if ( level == 0 ) {
			assertTrue( "Error (" + level + "): m does not equal m.clone()", m.equals( ( (ObjectBigArrayBigList)m ).clone() ) );
			assertTrue( "Error (" + level + "): m.clone() does not equal m", ( (ObjectBigArrayBigList)m ).clone().equals( m ) );
		}
		/* Now we play with constructors. */
		assertTrue( "Error (" + level + "): m does not equal new ( type-specific Collection m )", m.equals( new ObjectBigArrayBigList( (ObjectCollection)m ) ) );
		assertTrue( "Error (" + level + "): new ( type-specific nCollection m ) does not equal m", ( new ObjectBigArrayBigList( (ObjectCollection)m ) ).equals( m ) );
		assertTrue( "Error (" + level + "): m does not equal new ( type-specific List m )", m.equals( new ObjectBigArrayBigList( m ) ) );
		assertTrue( "Error (" + level + "): new ( type-specific List m ) does not equal m", ( new ObjectBigArrayBigList( m ) ).equals( m ) );
		assertTrue( "Error (" + level + "): m does not equal new ( m.listIterator() )", m.equals( new ObjectBigArrayBigList( m.listIterator() ) ) );
		assertTrue( "Error (" + level + "): new ( m.listIterator() ) does not equal m", ( new ObjectBigArrayBigList( m.listIterator() ) ).equals( m ) );
		assertTrue( "Error (" + level + "): m does not equal new ( m.type_specific_iterator() )", m.equals( new ObjectBigArrayBigList( m.iterator() ) ) );
		assertTrue( "Error (" + level + "): new ( m.type_specific_iterator() ) does not equal m", ( new ObjectBigArrayBigList( m.iterator() ) ).equals( m ) );
		int h = m.hashCode();
		/* Now we save and read m. */
		ObjectBigList m2 = null;
		try {
			java.io.File ff = new java.io.File( "it.unimi.dsi.fastutil.test" );
			java.io.OutputStream os = new java.io.FileOutputStream( ff );
			java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream( os );
			oos.writeObject( m );
			oos.close();
			java.io.InputStream is = new java.io.FileInputStream( ff );
			java.io.ObjectInputStream ois = new java.io.ObjectInputStream( is );
			m2 = (ObjectBigList)ois.readObject();
			ois.close();
			ff.delete();
		}
		catch ( Exception e ) {
			e.printStackTrace();
			System.exit( 1 );
		}
		assertTrue( "Error (" + level + "): hashCode() changed after save/read", m2.hashCode() == h );
		/* Now we check that m2 actually holds that data. */
		assertTrue( "Error (" + level + "): ! m2.equals( t ) after save/read", m2.equals( t ) );
		assertTrue( "Error (" + level + "): ! t.equals( m2 ) after save/read", t.equals( m2 ) );
		/* Now we take out of m everything, and check that it is empty. */
		for ( Iterator i = t.iterator(); i.hasNext(); )
			m2.remove( i.next() );
		assertTrue( "Error (" + level + "): m2 is not empty (as it should be)", m2.isEmpty() );
		/* Now we play with iterators. */
		{
			ObjectBigListIterator i;
			ObjectBigListIterator j;
			i = m.listIterator();
			j = t.listIterator();
			for ( int k = 0; k < 2 * n; k++ ) {
				assertTrue( "Error (" + level + "): divergence in hasNext()", i.hasNext() == j.hasNext() );
				assertTrue( "Error (" + level + "): divergence in hasPrevious()", i.hasPrevious() == j.hasPrevious() );
				if ( r.nextFloat() < .8 && i.hasNext() ) {
					assertTrue( "Error (" + level + "): divergence in next()", i.next().equals( j.next() ) );
					if ( r.nextFloat() < 0.2 ) {
						i.remove();
						j.remove();
					}
					else if ( r.nextFloat() < 0.2 ) {
						Object T = genKey();
						i.set( T );
						j.set( ( T ) );
					}
					else if ( r.nextFloat() < 0.2 ) {
						Object T = genKey();
						i.add( T );
						j.add( ( T ) );
					}
				}
				else if ( r.nextFloat() < .2 && i.hasPrevious() ) {
					assertTrue( "Error (" + level + "): divergence in previous()", i.previous().equals( j.previous() ) );
					if ( r.nextFloat() < 0.2 ) {
						i.remove();
						j.remove();
					}
					else if ( r.nextFloat() < 0.2 ) {
						Object T = genKey();
						i.set( T );
						j.set( ( T ) );
					}
					else if ( r.nextFloat() < 0.2 ) {
						Object T = genKey();
						i.add( T );
						j.add( ( T ) );
					}
				}
				assertTrue( "Error (" + level + "): divergence in nextIndex()", i.nextIndex() == j.nextIndex() );
				assertTrue( "Error (" + level + "): divergence in previousIndex()", i.previousIndex() == j.previousIndex() );
			}
		}
		{
			Object I, J;
			int from = r.nextInt( m.size() + 1 );
			ObjectBigListIterator i;
			ObjectBigListIterator j;
			i = m.listIterator( from );
			j = t.listIterator( from );
			for ( int k = 0; k < 2 * n; k++ ) {
				assertTrue( "Error (" + level + "): divergence in hasNext() (iterator with starting point " + from + ")", i.hasNext() == j.hasNext() );
				assertTrue( "Error (" + level + "): divergence in hasPrevious() (iterator with starting point " + from + ")", i.hasPrevious() == j.hasPrevious() );
				if ( r.nextFloat() < .8 && i.hasNext() ) {
					I = i.next();
					J = j.next();
					assertTrue( "Error (" + level + "): divergence in next() (" + I + ", " + J + ", iterator with starting point " + from + ")", I.equals( J ) );
					// System.err.println("Done next " + I + " " + J + "  " + badPrevious);
					if ( r.nextFloat() < 0.2 ) {
						// System.err.println("Removing in next");
						i.remove();
						j.remove();
					}
					else if ( r.nextFloat() < 0.2 ) {
						Object T = genKey();
						i.set( T );
						j.set( ( T ) );
					}
					else if ( r.nextFloat() < 0.2 ) {
						Object T = genKey();
						i.add( T );
						j.add( ( T ) );
					}
				}
				else if ( r.nextFloat() < .2 && i.hasPrevious() ) {
					I = i.previous();
					J = j.previous();
					assertTrue( "Error (" + level + "): divergence in previous() (" + I + ", " + J + ", iterator with starting point " + from + ")", I.equals( J ) );
					if ( r.nextFloat() < 0.2 ) {
						// System.err.println("Removing in prev");
						i.remove();
						j.remove();
					}
					else if ( r.nextFloat() < 0.2 ) {
						Object T = genKey();
						i.set( T );
						j.set( ( T ) );
					}
					else if ( r.nextFloat() < 0.2 ) {
						Object T = genKey();
						i.add( T );
						j.add( ( T ) );
					}
				}
			}
		}
		/* Now we check that m actually holds that data. */
		assertTrue( "Error (" + level + "): ! m.equals( t ) after iteration", m.equals( t ) );
		assertTrue( "Error (" + level + "): ! t.equals( m ) after iteration", t.equals( m ) );
		/* Now we select a pair of keys and create a subset. */
		if ( !m.isEmpty() ) {
			int start = r.nextInt( m.size() );
			int end = start + r.nextInt( m.size() - start );
			// System.err.println("Checking subList from " + start + " to " + end + " (level=" +
			// (level+1) + ")..." );
			testLists( m.subList( start, end ), t.subList( start, end ), n, level + 1 );
			assertTrue( "Error (" + level + m + t + "): ! m.equals( t ) after subList", m.equals( t ) );
			assertTrue( "Error (" + level + "): ! t.equals( m ) after subList", t.equals( m ) );
		}
		m.clear();
		t.clear();
		assertTrue( "Error (" + level + "): m is not empty after clear()", m.isEmpty() );
	}

	@SuppressWarnings({ "boxing", "unchecked" })
	protected static void test( int n ) {
		ObjectBigArrayBigList m = new ObjectBigArrayBigList();
		ObjectBigList t = ObjectBigLists.asBigList( new ObjectArrayList() );
		k = new Object[ n ];
		nk = new Object[ n ];
		kt = new Object[ n ];
		nkt = new Object[ n ];
		for ( int i = 0; i < n; i++ ) {
			k[ i ] = kt[ i ] = genKey();
			nk[ i ] = nkt[ i ] = genKey();
		}
		/* We add pairs to t. */
		for ( int i = 0; i < n; i++ ) t.add( k[ i ] );
		/* We add to m the same data */
		m.addAll( t );
		testLists( m, t, n, 0 );

		// This tests all reflection-based methods.
		m = ObjectBigArrayBigList.wrap( ObjectBigArrays.EMPTY_BIG_ARRAY );
		t = ObjectBigLists.asBigList( new ObjectArrayList() );
		/* We add pairs to t. */
		for ( int i = 0; i < n; i++ ) t.add( k[ i ] );
		/* We add to m the same data */
		m.addAll( t );
		testLists( m, t, n, 0 );
		return;
	}

	@Test
	public void test1() {
		test( 1 );
	}

	@Test
	public void test10() {
		test( 10 );
	}

	@Test
	public void test100() {
		test( 100 );
	}

	@Ignore("Too long")
	@Test
	public void test1000() {
		test( 1000 );
	}
}
