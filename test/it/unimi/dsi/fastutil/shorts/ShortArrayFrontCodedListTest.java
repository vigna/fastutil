package it.unimi.dsi.fastutil.shorts;

import static org.junit.Assert.assertTrue;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;

import java.io.IOException;

import org.junit.Test;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ShortArrayFrontCodedListTest {


	private static java.util.Random r = new java.util.Random( 0 );

	private static short genKey() {
		return (short)( r.nextInt() );
	}

	private static boolean contentEquals( java.util.List x, java.util.List y ) {
		if ( x.size() != y.size() ) return false;
		for ( int i = 0; i < x.size(); i++ )
			if ( !java.util.Arrays.equals( (short[])x.get( i ), (short[])y.get( i ) ) ) return false;
		return true;
	}

	private static int l[];

	private static short[][] a;

	private static void test( int n ) throws IOException, ClassNotFoundException {
		l = new int[ n ];
		a = new short[ n ][];
		for ( int i = 0; i < n; i++ )
			l[ i ] = (int)( Math.abs( r.nextGaussian() ) * 32 );
		for ( int i = 0; i < n; i++ )
			a[ i ] = new short[ l[ i ] ];
		for ( int i = 0; i < n; i++ )
			for ( int j = 0; j < l[ i ]; j++ )
				a[ i ][ j ] = genKey();
		ShortArrayFrontCodedList m = new ShortArrayFrontCodedList( it.unimi.dsi.fastutil.objects.ObjectIterators.wrap( a ), r.nextInt( 4 ) + 1 );
		it.unimi.dsi.fastutil.objects.ObjectArrayList t = new it.unimi.dsi.fastutil.objects.ObjectArrayList( a );
		// System.out.println(m);
		// for( i = 0; i < t.size(); i++ )
		// System.out.println(ARRAY_LIST.wrap((KEY_TYPE[])t.get(i)));
		/* Now we check that m actually holds that data. */
		assertTrue( "Error: m does not equal t at creation", contentEquals( m, t ) );
		/* Now we check cloning. */
		assertTrue( "Error: m does not equal m.clone()", contentEquals( m, m.clone() ) );
		/* Now we play with iterators. */
		{
			ObjectListIterator i;
			java.util.ListIterator j;
			i = m.listIterator();
			j = t.listIterator();
			for ( int k = 0; k < 2 * n; k++ ) {
				assertTrue( "Error: divergence in hasNext()", i.hasNext() == j.hasNext() );
				assertTrue( "Error: divergence in hasPrevious()", i.hasPrevious() == j.hasPrevious() );
				if ( r.nextFloat() < .8 && i.hasNext() ) {
					assertTrue( "Error: divergence in next()", java.util.Arrays.equals( (short[])i.next(), (short[])j.next() ) );
				}
				else if ( r.nextFloat() < .2 && i.hasPrevious() ) {
					assertTrue( "Error: divergence in previous()", java.util.Arrays.equals( (short[])i.previous(), (short[])j.previous() ) );
				}
				assertTrue( "Error: divergence in nextIndex()", i.nextIndex() == j.nextIndex() );
				assertTrue( "Error: divergence in previousIndex()", i.previousIndex() == j.previousIndex() );
			}
		}
		{
			int from = r.nextInt( m.size() + 1 );
			ObjectListIterator i;
			java.util.ListIterator j;
			i = m.listIterator( from );
			j = t.listIterator( from );
			for ( int k = 0; k < 2 * n; k++ ) {
				assertTrue( "Error: divergence in hasNext() (iterator with starting point " + from + ")", i.hasNext() == j.hasNext() );
				assertTrue( "Error: divergence in hasPrevious() (iterator with starting point " + from + ")", i.hasPrevious() == j.hasPrevious() );
				if ( r.nextFloat() < .8 && i.hasNext() ) {
					assertTrue( "Error: divergence in next() (iterator with starting point " + from + ")", java.util.Arrays.equals( (short[])i.next(), (short[])j.next() ) );
					// System.err.println("Done next " + I + " " + J + "  " + badPrevious);
				}
				else if ( r.nextFloat() < .2 && i.hasPrevious() ) {
					assertTrue( "Error: divergence in previous() (iterator with starting point " + from + ")", java.util.Arrays.equals( (short[])i.previous(), (short[])j.previous() ) );
				}
			}
		}
		java.io.File ff = new java.io.File( "it.unimi.dsi.fastutil.test" );
		java.io.OutputStream os = new java.io.FileOutputStream( ff );
		java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream( os );
		oos.writeObject( m );
		oos.close();
		java.io.InputStream is = new java.io.FileInputStream( ff );
		java.io.ObjectInputStream ois = new java.io.ObjectInputStream( is );
		m = (ShortArrayFrontCodedList)ois.readObject();
		ois.close();
		ff.delete();
		assertTrue( "Error: m does not equal t after save/read", contentEquals( m, t ) );
		return;
	}

	@Test
	public void test1() throws IOException, ClassNotFoundException {
		test( 1 );
	}

	@Test
	public void test10() throws Exception, ClassNotFoundException {
		test( 10 );
	}

	@Test
	public void test100() throws IOException, ClassNotFoundException {
		test( 100 );
	}

	@Test
	public void test1000() throws IOException, ClassNotFoundException {
		test( 1000 );
	}

	@Test
	public void test10000() throws IOException, ClassNotFoundException {
		test( 10000 );
	}
}
