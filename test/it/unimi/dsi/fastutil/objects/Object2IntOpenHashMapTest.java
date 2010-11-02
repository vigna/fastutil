package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.Hash;

import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;

@SuppressWarnings("rawtypes")
public class Object2IntOpenHashMapTest {

	private static java.util.Random r = new java.util.Random( 0 );

	private static Object genKey() {
		return Integer.toBinaryString( r.nextInt() );
	}

	private static int genValue() {
		return r.nextInt();
	}

	private static java.text.NumberFormat format = new java.text.DecimalFormat( "#,###.00" );

	private static java.text.FieldPosition fp = new java.text.FieldPosition( 0 );

	private static boolean valEquals( Object o1, Object o2 ) {
		return o1 == null ? o2 == null : o1.equals( o2 );
	}

	@SuppressWarnings("unchecked")
	protected static void test( int n, float f ) {
		Object2IntOpenHashMap m = new Object2IntOpenHashMap( Hash.DEFAULT_INITIAL_SIZE, f );
		Map t = new java.util.HashMap();
		/* First of all, we fill t with random data. */
		for ( int i = 0; i < n; i++ )
			t.put( ( genKey() ), ( Integer.valueOf( genValue() ) ) );
		/* Now we add to m the same data */
		m.putAll( t );
		if ( !m.equals( t ) ) System.out.println( "Error: !m.equals(t) after insertion" );
		if ( !t.equals( m ) ) System.out.println( "Error: !t.equals(m) after insertion" );
		/* Now we check that m actually holds that data. */
		for ( java.util.Iterator i = t.entrySet().iterator(); i.hasNext(); ) {
			java.util.Map.Entry e = (java.util.Map.Entry)i.next();
			if ( !valEquals( e.getValue(), m.get( e.getKey() ) ) ) System.out.println( "Error: m and t differ on an entry (" + e + ") after insertion (iterating on t)" );
		}
		/* Now we check that m actually holds that data, but iterating on m. */
		for ( java.util.Iterator i = m.entrySet().iterator(); i.hasNext(); ) {
			java.util.Map.Entry e = (java.util.Map.Entry)i.next();
			if ( !valEquals( e.getValue(), t.get( e.getKey() ) ) ) System.out.println( "Error: m and t differ on an entry (" + e + ") after insertion (iterating on m)" );
		}
		/* Now we check that m actually holds the same keys. */
		for ( java.util.Iterator i = t.keySet().iterator(); i.hasNext(); ) {
			Object o = i.next();
			if ( !m.containsKey( o ) ) {
				System.out.println( "Error: m and t differ on a key (" + o + ") after insertion (iterating on t)" );
				System.exit( 1 );
			}
			if ( !m.keySet().contains( o ) ) {
				System.out.println( "Error: m and t differ on a key (" + o + ", in keySet()) after insertion (iterating on t)" );
				System.exit( 1 );
			}
		}
		/* Now we check that m actually holds the same keys, but iterating on m. */
		for ( java.util.Iterator i = m.keySet().iterator(); i.hasNext(); ) {
			Object o = i.next();
			if ( !t.containsKey( o ) ) {
				System.out.println( "Error: m and t differ on a key after insertion (iterating on m)" );
				System.exit( 1 );
			}
			if ( !t.keySet().contains( o ) ) {
				System.out.println( "Error: m and t differ on a key (in keySet()) after insertion (iterating on m)" );
				System.exit( 1 );
			}
		}
		/* Now we check that m actually hold the same values. */
		for ( java.util.Iterator i = t.values().iterator(); i.hasNext(); ) {
			Object o = i.next();
			if ( !m.containsValue( o ) ) {
				System.out.println( "Error: m and t differ on a value after insertion (iterating on t)" );
				System.exit( 1 );
			}
			if ( !m.values().contains( o ) ) {
				System.out.println( "Error: m and t differ on a value (in values()) after insertion (iterating on t)" );
				System.exit( 1 );
			}
		}
		/* Now we check that m actually hold the same values, but iterating on m. */
		for ( java.util.Iterator i = m.values().iterator(); i.hasNext(); ) {
			Object o = i.next();
			if ( !t.containsValue( o ) ) {
				System.out.println( "Error: m and t differ on a value after insertion (iterating on m)" );
				System.exit( 1 );
			}
			if ( !t.values().contains( o ) ) {
				System.out.println( "Error: m and t differ on a value (in values()) after insertion (iterating on m)" );
				System.exit( 1 );
			}
		}
		/*
		 * Now we check that inquiries about random data give the same answer in m and t. For m we
		 * use the polymorphic method.
		 */
		for ( int i = 0; i < n; i++ ) {
			Object T = genKey();
			if ( m.containsKey( ( T ) ) != t.containsKey( ( T ) ) ) {
				System.out.println( "Error: divergence in keys between t and m (polymorphic method)" );
				System.exit( 1 );
			}
			if ( ( m.getInt( T ) != ( 0 ) ) != ( ( t.get( ( T ) ) == null ? ( 0 ) : ( ( ( (Integer)( t.get( ( T ) ) ) ).intValue() ) ) ) != ( 0 ) ) ||
					t.get( ( T ) ) != null &&
					!( Integer.valueOf( m.getInt( T ) ) ).equals( t.get( ( T ) ) ) ) {
				System.out.println( "Error: divergence between t and m (polymorphic method)" );
				System.exit( 1 );
			}
		}
		/*
		 * Again, we check that inquiries about random data give the same answer in m and t, but for
		 * m we use the standard method.
		 */
		for ( int i = 0; i < n; i++ ) {
			Object T = genKey();
			if ( !valEquals( m.get( ( T ) ), t.get( ( T ) ) ) ) {
				System.out.println( "Error: divergence between t and m (standard method)" );
				System.exit( 1 );
			}
		}
		/* Now we put and remove random data in m and t, checking that the result is the same. */
		for ( int i = 0; i < 20 * n; i++ ) {
			Object T = genKey();
			int U = genValue();
			if ( !valEquals( m.put( ( T ), ( Integer.valueOf( U ) ) ), t.put( ( T ), ( Integer.valueOf( U ) ) ) ) ) {
				System.out.println( "Error: divergence in put() between t and m" );
				System.exit( 1 );
			}
			T = genKey();
			if ( !valEquals( m.remove( ( T ) ), t.remove( ( T ) ) ) ) {
				System.out.println( "Error: divergence in remove() between t and m" );
				System.exit( 1 );
			}
		}
		if ( !m.equals( t ) ) System.out.println( "Error: !m.equals(t) after removal" );
		if ( !t.equals( m ) ) System.out.println( "Error: !t.equals(m) after removal" );
		/* Now we check that m actually holds the same data. */
		for ( java.util.Iterator i = t.entrySet().iterator(); i.hasNext(); ) {
			java.util.Map.Entry e = (java.util.Map.Entry)i.next();
			if ( !valEquals( e.getValue(), m.get( e.getKey() ) ) ) {
				System.out.println( "Error: m and t differ on an entry (" + e + ") after removal (iterating on t)" );
				System.exit( 1 );
			}
		}
		/* Now we check that m actually holds that data, but iterating on m. */
		for ( java.util.Iterator i = m.entrySet().iterator(); i.hasNext(); ) {
			java.util.Map.Entry e = (java.util.Map.Entry)i.next();
			if ( !valEquals( e.getValue(), t.get( e.getKey() ) ) ) {
				System.out.println( "Error: m and t differ on an entry (" + e + ") after removal (iterating on m)" );
				System.exit( 1 );
			}
		}
		/* Now we check that m actually holds the same keys. */
		for ( java.util.Iterator i = t.keySet().iterator(); i.hasNext(); ) {
			Object o = i.next();
			if ( !m.containsKey( o ) ) {
				System.out.println( "Error: m and t differ on a key (" + o + ") after removal (iterating on t)" );
				System.exit( 1 );
			}
			if ( !m.keySet().contains( o ) ) {
				System.out.println( "Error: m and t differ on a key (" + o + ", in keySet()) after removal (iterating on t)" );
				System.exit( 1 );
			}
		}
		/* Now we check that m actually holds the same keys, but iterating on m. */
		for ( java.util.Iterator i = m.keySet().iterator(); i.hasNext(); ) {
			Object o = i.next();
			if ( !t.containsKey( o ) ) {
				System.out.println( "Error: m and t differ on a key after removal (iterating on m)" );
				System.exit( 1 );
			}
			if ( !t.keySet().contains( o ) ) {
				System.out.println( "Error: m and t differ on a key (in keySet()) after removal (iterating on m)" );
				System.exit( 1 );
			}
		}
		/* Now we check that m actually hold the same values. */
		for ( java.util.Iterator i = t.values().iterator(); i.hasNext(); ) {
			Object o = i.next();
			if ( !m.containsValue( o ) ) {
				System.out.println( "Error: m and t differ on a value after removal (iterating on t)" );
				System.exit( 1 );
			}
			if ( !m.values().contains( o ) ) {
				System.out.println( "Error: m and t differ on a value (in values()) after removal (iterating on t)" );
				System.exit( 1 );
			}
		}
		/* Now we check that m actually hold the same values, but iterating on m. */
		for ( java.util.Iterator i = m.values().iterator(); i.hasNext(); ) {
			Object o = i.next();
			if ( !t.containsValue( o ) ) {
				System.out.println( "Error: m and t differ on a value after removal (iterating on m)" );
				System.exit( 1 );
			}
			if ( !t.values().contains( o ) ) {
				System.out.println( "Error: m and t differ on a value (in values()) after removal (iterating on m)" );
				System.exit( 1 );
			}
		}
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
			m = (Object2IntOpenHashMap)ois.readObject();
			ois.close();
			ff.delete();
		}
		catch ( Exception e ) {
			e.printStackTrace();
			System.exit( 1 );
		}
		if ( m.hashCode() != h ) System.out.println( "Error: hashCode() changed after save/read" );
		/* Now we check that m actually holds that data. */
		for ( java.util.Iterator i = t.keySet().iterator(); i.hasNext(); ) {
			Object o = i.next();
			if ( !valEquals( m.get( o ), t.get( o ) ) ) {
				System.out.println( "Error: m and t differ on an entry after save/read" );
				System.exit( 1 );
			}
		}
		/* Now we put and remove random data in m and t, checking that the result is the same. */
		for ( int i = 0; i < 20 * n; i++ ) {
			Object T = genKey();
			int U = genValue();
			if ( !valEquals( m.put( ( T ), ( Integer.valueOf( U ) ) ), t.put( ( T ), ( Integer.valueOf( U ) ) ) ) ) {
				System.out.println( "Error: divergence in put() between t and m after save/read" );
				System.exit( 1 );
			}
			T = genKey();
			if ( !valEquals( m.remove( ( T ) ), t.remove( ( T ) ) ) ) {
				System.out.println( "Error: divergence in remove() between t and m after save/read" );
				System.exit( 1 );
			}
		}
		if ( !m.equals( t ) ) System.out.println( "Error: !m.equals(t) after post-save/read removal" );
		if ( !t.equals( m ) ) System.out.println( "Error: !t.equals(m) after post-save/read removal" );
		/* Now we take out of m everything, and check that it is empty. */
		for ( java.util.Iterator i = t.keySet().iterator(); i.hasNext(); )
			m.remove( i.next() );
		if ( !m.isEmpty() ) {
			System.out.println( "Error: m is not empty (as it should be)" );
			System.exit( 1 );
		}
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
		test( 10, Hash.DEFAULT_LOAD_FACTOR );
		test( 10, Hash.FAST_LOAD_FACTOR );
		test( 10, Hash.VERY_FAST_LOAD_FACTOR );
	}

	@Test
	public void test1000() {
		test( 10, Hash.DEFAULT_LOAD_FACTOR );
		test( 10, Hash.FAST_LOAD_FACTOR );
		test( 10, Hash.VERY_FAST_LOAD_FACTOR );
	}
}

