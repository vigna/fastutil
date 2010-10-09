package it.unimi.dsi.fastutil.io;

import it.unimi.dsi.fastutil.io.BinIO;
import it.unimi.dsi.fastutil.io.FastBufferedOutputStream;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import junit.framework.TestCase;

public class FastBufferedOutputStreamTest extends TestCase {

	public void testWriteEqualToBufferSize() throws IOException {
		final FastBufferedOutputStream fbos = new FastBufferedOutputStream( new ByteArrayOutputStream(), 4 );
		fbos.write( 0 );
		fbos.write( new byte[ 4 ] );
		fbos.write( 0 );
	}

	public void testRandom( int bufSize ) throws FileNotFoundException, IOException {

		File file = File.createTempFile( getClass().getSimpleName(), "test" );
		file.deleteOnExit();
		FastBufferedOutputStream fbos = new FastBufferedOutputStream( new FileOutputStream( file + "1" ), bufSize );
		BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream( file + "2" ) );
		Random r = new Random();

		int j = r.nextInt( 10000 );
		while( j-- != 0 ) {
			switch( r.nextInt( 3 ) ) {

			case 0:
				int x = (byte)r.nextInt();
				fbos.write( x );
				bos.write(x );
				break;

			case 1:
				byte[] b  = new byte[ r.nextInt( 32768 ) + 16 ];
				for( int i = 0; i < b.length; i++ ) b[ i ] = (byte)r.nextInt();
				int offset = r.nextInt( b.length / 4 );
				int length = r.nextInt( b.length - offset );
				fbos.write( b, offset, length );
				bos.write( b, offset, length );
				break;

			case 2:
				fbos.flush();
				break;
			}
		}

		fbos.close();
		bos.close();
		assertTrue( Arrays.equals( BinIO.loadBytes( file + "1" ), BinIO.loadBytes( file + "2" ) ) );
	}
	
	public void testRandom() throws FileNotFoundException, IOException {
		testRandom( 1 );
		testRandom( 2 );
		testRandom( 3 );
		testRandom( 1024 );
	}
}

