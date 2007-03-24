package test.it.unimi.dsi.fastutil.io;

import it.unimi.dsi.fastutil.io.BinIO;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import junit.framework.TestCase;

public class BinIOTest extends TestCase {

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
			assertTrue( Arrays.equals( a, BinIO.loadBytes( file ) ) );

			byte[] b = new byte[ a.length ];
			assertEquals( a.length, BinIO.loadBytes( file, b ) );
			assertTrue( Arrays.equals( a, b ) );
			assertEquals( a.length, BinIO.loadBytes( file, b, 0, a.length ) );
			assertTrue( Arrays.equals( a, b ) );

			assertEquals( a.length, BinIO.loadBytes( new FileInputStream( file ), b ) );
			assertTrue( Arrays.equals( a, b ) );
			assertEquals( a.length, BinIO.loadBytes( new FileInputStream( file ), b, 0, a.length ) );
			assertTrue( Arrays.equals( a, b ) );

			byte[] c = new byte[ a.length + 1 ];
			assertEquals( a.length, BinIO.loadBytes( new FileInputStream( file ), c ) );
			assertEquals( 0, c[ a.length ] );
			System.arraycopy( c, 0, b, 0, b.length );
			assertTrue( Arrays.equals( a, b ) );
			assertEquals( a.length, BinIO.loadBytes( new FileInputStream( file ), c, 1, a.length ) );
			assertEquals( 0, c[ 0 ] );
			System.arraycopy( c, 1, b, 0, b.length );
			assertTrue( Arrays.equals( a, b ) );

			c[ a.length ] = 0;
			assertEquals( a.length, BinIO.loadBytes( (DataInput)new DataInputStream( new FileInputStream( file ) ), c ) );
			assertEquals( 0, c[ a.length ] );
			System.arraycopy( c, 0, b, 0, b.length );
			assertTrue( Arrays.equals( a, b ) );
			assertEquals( a.length, BinIO.loadBytes( (DataInput)new DataInputStream( new FileInputStream( file ) ), c, 1, a.length ) );
			assertEquals( 0, c[ 0 ] );
			System.arraycopy( c, 1, b, 0, b.length );
			assertTrue( Arrays.equals( a, b ) );
		}

	}
	
	public void testBytes() throws IOException {
		testBytes( SMALL );
		testBytes( LARGE );
	}
}
