package it.unimi.dsi.fastutil.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.unimi.dsi.fastutil.bytes.ByteBigArrays;
import it.unimi.dsi.fastutil.doubles.DoubleIterator;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

public class BinIOTest {

	final static byte[] SMALL = new byte[ 1024 ];
	final static byte[] LARGE = new byte[ 1024 * 1024 + 42 ];
	
	static {
		for( int i = SMALL.length; i-- != 0; ) SMALL[ i ] = (byte)i;
		for( int i = LARGE.length; i-- != 0; ) LARGE[ i ] = (byte)i;
	}
	
	public void testBytes( byte[] a ) throws IOException {
		final File file = File.createTempFile( getClass().getSimpleName(), "dump" );
		file.deleteOnExit();
		final byte[] aShifted = new byte[ a.length + 1 ];
		System.arraycopy( a, 0, aShifted, 1, a.length );
		
		for( int i = 0; i < 6; i++ ) {
			file.delete();
			switch(i) {
			case 0: BinIO.storeBytes( a, file ); break;
			case 1: BinIO.storeBytes( a, (DataOutput)new DataOutputStream( new FileOutputStream( file ) ) ); break;
			case 2: BinIO.storeBytes( a, new FileOutputStream( file ) ); break;
			case 3: BinIO.storeBytes( aShifted, 1, a.length, file ); break;
			case 4: BinIO.storeBytes( aShifted, 1, a.length, (DataOutput)new DataOutputStream( new FileOutputStream( file ) ) ); break;
			case 5: BinIO.storeBytes( aShifted, 1, a.length, new FileOutputStream( file ) ); break;
			}
			assertArrayEquals( a, BinIO.loadBytes( file ) );

			byte[] b = new byte[ a.length ];
			assertEquals( a.length, BinIO.loadBytes( file, b ) );
			assertArrayEquals( a, b );
			assertEquals( a.length, BinIO.loadBytes( file, b, 0, a.length ) );
			assertArrayEquals( a, b );

			assertEquals( a.length, BinIO.loadBytes( new FileInputStream( file ), b ) );
			assertArrayEquals( a, b );
			assertEquals( a.length, BinIO.loadBytes( new FileInputStream( file ), b, 0, a.length ) );
			assertArrayEquals( a, b );

			byte[] c = new byte[ a.length + 1 ];
			assertEquals( a.length, BinIO.loadBytes( new FileInputStream( file ), c ) );
			assertEquals( 0, c[ a.length ] );
			System.arraycopy( c, 0, b, 0, b.length );
			assertArrayEquals( a, b );
			assertEquals( a.length, BinIO.loadBytes( new FileInputStream( file ), c, 1, a.length ) );
			assertEquals( 0, c[ 0 ] );
			System.arraycopy( c, 1, b, 0, b.length );
			assertArrayEquals( a, b );

			c[ a.length ] = 0;
			assertEquals( a.length, BinIO.loadBytes( (DataInput)new DataInputStream( new FileInputStream( file ) ), c ) );
			assertEquals( 0, c[ a.length ] );
			System.arraycopy( c, 0, b, 0, b.length );
			assertArrayEquals( a, b );
			assertEquals( a.length, BinIO.loadBytes( (DataInput)new DataInputStream( new FileInputStream( file ) ), c, 1, a.length ) );
			assertEquals( 0, c[ 0 ] );
			System.arraycopy( c, 1, b, 0, b.length );
			assertArrayEquals( a, b );
		}

	}

	@Test
	public void testBytes() throws IOException {
		testBytes( SMALL );
		testBytes( LARGE );
	}

	public void testBigBytes( byte[][] a ) throws IOException {
		final File file = File.createTempFile( getClass().getSimpleName(), "dump" );
		file.deleteOnExit();
		final long length = ByteBigArrays.length( a );
		final byte[][] aShifted = ByteBigArrays.newBigArray( length + 1 );
		ByteBigArrays.copy( a, 0, aShifted, 1, length );
		
		for( int i = 0; i < 6; i++ ) {
			file.delete();
			switch(i) {
			case 0: BinIO.storeBytes( a, file ); break;
			case 1: BinIO.storeBytes( a, (DataOutput)new DataOutputStream( new FileOutputStream( file ) ) ); break;
			case 2: BinIO.storeBytes( a, new FileOutputStream( file ) ); break;
			case 3: BinIO.storeBytes( aShifted, 1, length, file ); break;
			case 4: BinIO.storeBytes( aShifted, 1, length, (DataOutput)new DataOutputStream( new FileOutputStream( file ) ) ); break;
			case 5: BinIO.storeBytes( aShifted, 1, length, new FileOutputStream( file ) ); break;
			}
			assertArrayEquals( a, BinIO.loadBytesBig( file ) );

			byte[][] b = ByteBigArrays.newBigArray( length );
			assertEquals( length, BinIO.loadBytes( file, b ) );
			assertArrayEquals( a, b );
			assertEquals( length, BinIO.loadBytes( file, b, 0, length ) );
			assertArrayEquals( a, b );

			assertEquals( length, BinIO.loadBytes( new FileInputStream( file ), b ) );
			assertArrayEquals( a, b );
			assertEquals( length, BinIO.loadBytes( new FileInputStream( file ), b, 0, length ) );
			assertArrayEquals( a, b );

			byte[][] c = ByteBigArrays.newBigArray( length + 1 );
			assertEquals( length, BinIO.loadBytes( new FileInputStream( file ), c ) );
			assertEquals( 0, ByteBigArrays.get( c, length ) );
			ByteBigArrays.copy( c, 0, b, 0, b.length );
			assertArrayEquals( a, b );
			assertEquals( length, BinIO.loadBytes( new FileInputStream( file ), c, 1, length ) );
			assertEquals( 0, ByteBigArrays.get( c, 0 ) );
			ByteBigArrays.copy( c, 1, b, 0, b.length );
			assertArrayEquals( a, b );

			ByteBigArrays.set( c, length, (byte)0 );
			assertEquals( length, BinIO.loadBytes( (DataInput)new DataInputStream( new FileInputStream( file ) ), c ) );
			assertEquals( 0, ByteBigArrays.get( c, length ) );
			ByteBigArrays.copy( c, 0, b, 0, b.length );
			assertArrayEquals( a, b );
			
			assertEquals( length, BinIO.loadBytes( (DataInput)new DataInputStream( new FileInputStream( file ) ), c, 1, length ) );
			assertEquals( 0, ByteBigArrays.get( c, 0 ) );
			ByteBigArrays.copy( c, 1, b, 0, b.length );
			assertArrayEquals( a, b );
		}

	}

	@Test
	public void testBigBytes() throws IOException {
		testBigBytes( ByteBigArrays.wrap( SMALL ) );
		testBigBytes( ByteBigArrays.wrap( LARGE ) );
	}
	
	public void testFileDataWrappers() throws IOException {
		final File file = File.createTempFile( getClass().getSimpleName(), "dump" );
		file.deleteOnExit();
		final DataOutputStream dos = new DataOutputStream( new FileOutputStream( file ) );
		for( int i = 0; i < 100; i++ ) dos.writeDouble( i );
		dos.close();
		
		DoubleIterator di = BinIO.asDoubleIterator( file );
		for( int i = 0; i < 100; i++ ) assertEquals( i, di.nextDouble(), 0. );
		assertFalse( di.hasNext() );

		di = BinIO.asDoubleIterator( file );
		for( int i = 0; i < 100; i++ ) {
			assertTrue( di.hasNext() );
			assertEquals( i, di.nextDouble(), 0. );
		}
		
		di = BinIO.asDoubleIterator( file );
		int s = 1;
		for( int i = 0; i < 100; i++ ) {
			assertEquals( Math.min( s, 100 - i ), di.skip( s ) );
			i += s;
			if ( i >= 100 ) break;
			assertEquals( i, di.nextDouble(), 0. );
			s *= 2;
		}

		di = BinIO.asDoubleIterator( file );
		s = 1;
		for( int i = 0; i < 100; i++ ) {
			if ( s > 100 - i ) break;
			assertTrue( di.hasNext() );
			assertEquals( Math.min( s, 100 - i ), di.skip( s ) );
			i += s;
			if ( i >= 100 ) {
				assertFalse( di.hasNext() );
				break;
			}
			assertTrue( di.hasNext() );
			assertTrue( di.hasNext() ); // To increase coverage
			assertEquals( i, di.nextDouble(), 0. );
			s *= 2;
		}

	}
}
