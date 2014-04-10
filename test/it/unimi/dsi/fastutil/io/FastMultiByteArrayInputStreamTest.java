package it.unimi.dsi.fastutil.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Random;

import org.junit.Test;

/** It is a good idea to run this test after lowering manually {@link FastMultiByteArrayInputStream#SLICE_BITS} to 10. */

public class FastMultiByteArrayInputStreamTest {
	private final static boolean DEBUG = false;
	
	@Test
	public void testSkip() {
		FastMultiByteArrayInputStream stream;
		
		stream = new FastMultiByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r', '\n', 'D' } );
		assertEquals( 2, stream.skip( 2 ) );
		assertEquals( 2, stream.position() );
		assertEquals( 1, stream.skip( 1 ) );
		assertEquals( 3, stream.position() );
		assertEquals( 3, stream.skip( 4 ) );
		assertEquals( 6, stream.position() );
		assertEquals( 0, stream.skip( 1 ) );
		assertEquals( 6, stream.position() );
		stream.close();
	}
	
	@Test
	public void testPosition() throws IOException {
		File temp = File.createTempFile( this.getClass().getSimpleName(), ".tmp" );
		temp.deleteOnExit();
		FileOutputStream fos = new FileOutputStream( temp );
		fos.write( new byte[] { 0, 1, 2, 3, 4 } );
		fos.close();
		
		FastMultiByteArrayInputStream stream = new FastMultiByteArrayInputStream( new FastBufferedInputStream( new FileInputStream( temp ) ) );
		byte[] b = new byte[ 2 ];
		stream.read( b );
		stream.position( 0 );
		assertEquals( 0, stream.read() );
		stream.close();
		
		stream = new FastMultiByteArrayInputStream( new FastBufferedInputStream( new FileInputStream( temp ) ) );
		b = new byte[ 1 ];
		stream.read( b );
		stream.position( 0 );
		assertEquals( 0, stream.read() );
		stream.close();

		stream = new FastMultiByteArrayInputStream( new FastBufferedInputStream ( new FileInputStream( temp ) ) );
		b = new byte[ 5 ];
		stream.read( b );
		assertEquals( -1, stream.read() );
		stream.position( 5 );
		assertEquals( -1, stream.read() );
		stream.position( 0 );
		assertEquals( 0, stream.read() );
		stream.position( 1 );
		assertEquals( 1, stream.read() );
		stream.position( 3 );
		assertEquals( 3, stream.read() );
		stream.position( 1 );
		assertEquals( 1, stream.read() );
		stream.position( 0 );
		assertEquals( 0, stream.read() );
		stream.close();
	}
	
	@SuppressWarnings("resource")
	@Test
	public void testRead() throws IOException {
		// Reads with length larger than buffer size
		
		// No head, no stream
		InputStream stream = new FastMultiByteArrayInputStream( new FastByteArrayInputStream( new byte[] {} ) );
		byte[] b = new byte[ 4 ];
		
		assertEquals( -1, stream.read( b, 0, 2 ) );
		
		// Some head, no stream
		stream = new FastMultiByteArrayInputStream( new FastByteArrayInputStream( new byte[] { 'A', 'B' } ) );
		b = new byte[ 4 ];
		
		assertEquals( 1, stream.read( b, 0, 1 ) );
		assertEquals( 1, stream.read( b, 0, 3 ) );
		
		// Some head, some stream
		stream = new FastMultiByteArrayInputStream( new FastByteArrayInputStream( new byte[] { 'A', 'B', 'C', 'D' } ) );
		b = new byte[ 4 ];
		
		assertEquals( 1, stream.read( b, 0, 1 ) );
		assertEquals( 3, stream.read( b, 0, 3 ) );

		// No head, some stream
		stream = new FastMultiByteArrayInputStream( new FastByteArrayInputStream( new byte[] { 'A', 'B', 'C', 'D' } ) );
		b = new byte[ 4 ];
		
		assertEquals( 3, stream.read( b, 0, 3 ) );
		
		// Reads with length smaller than or equal to buffer size
		
		// No head, no stream
		stream = new FastMultiByteArrayInputStream( new FastByteArrayInputStream( new byte[] {} ) );
		b = new byte[ 4 ];
		
		assertEquals( -1, stream.read( b, 0, 2 ) );

	}
	
	@SuppressWarnings("resource")
	@Test
	public void testRandom() throws IOException {
		File temp = File.createTempFile( this.getClass().getSimpleName(), "tmp" );
		temp.deleteOnExit();
		
		// Create temp random file
		FileOutputStream out = new FileOutputStream( temp );
		Random random = new Random();
		int length = 10000000 + random.nextInt( 1000000 );
		for( int i = 0; i < length; i++ ) out.write( random.nextInt() );
		out.close();

		FastMultiByteArrayInputStream bis = new FastMultiByteArrayInputStream( new FastBufferedInputStream( new FileInputStream( temp ) ) );
		FileInputStream test = new FileInputStream( temp );
		FileChannel fc = test.getChannel();
		int a1, a2, off, len, pos;
		byte b1[] = new byte[ 32768 ];
		byte b2[] = new byte[ 32768 ];

		while( true ) {

			switch( random.nextInt( 6 ) ) {

			case 0:
				if ( DEBUG ) System.err.println("read()");
				a1 = bis.read();
				a2 = test.read();
				assertEquals( a1, a2 );
				if ( a1 == -1 ) return;
				break;

			case 1:
				off = random.nextInt( b1.length );
				len = random.nextInt( b1.length - off + 1 );
				a1 = bis.read( b1, off, len );
				a2 = test.read( b2, off, len );
				if ( DEBUG ) System.err.println("read(b, " + off + ", " + len + ")");

				assertEquals( a1, a2 );

				for( int i = off; i < off + len; i++ ) assertEquals( "Position " + i, b1[ i ], b2[ i ] );
				break;

			case 2:
				if ( DEBUG ) System.err.println("available()");
				assertTrue( bis.available() <= test.available() );
				break;

			case 3:
				if ( DEBUG ) System.err.println("position()" );
				pos = (int)bis.position();
				assertEquals( (int)fc.position(), pos );
				break;

			case 4:
				pos = random.nextInt( length );
				bis.position( pos );
				if ( DEBUG ) System.err.println("position(" + pos + ")" );
				(test = new FileInputStream( temp )).skip( pos );
				fc = test.getChannel();
				break;

			case 5:
				pos = random.nextInt( (int)(length - bis.position() + 1) );
				a1 = (int)bis.skip( pos );
				a2 = (int)test.skip( pos );
				if ( DEBUG ) System.err.println("skip(" + pos + ")" );
				assertEquals( a1, a2 );
				break;
			}
		}
	}

	@SuppressWarnings("resource")
	@Test
	public void testPositionOnEnd() throws IOException {
		FastMultiByteArrayInputStream stream = new FastMultiByteArrayInputStream( new byte[ FastMultiByteArrayInputStream.SLICE_SIZE ] );
		stream.position( stream.length() );
		assertEquals( 0, stream.available() );
		assertEquals( -1, stream.read() );
		assertEquals( -1, stream.read() );
		assertNull( stream.current );

		stream.position( stream.length() - 1 );
		assertEquals( 1, stream.available() );
		assertEquals( 0, stream.read() );
		assertEquals( -1, stream.read() );
		assertNotNull( stream.current );

		stream.position( stream.length() - 2 );
		assertEquals( 2, stream.read( new byte[ 2 ] ) );
		assertNotNull( stream.current );

		stream.position( stream.length() - 2 );
		assertEquals( 2, stream.skip( 3 ) );
		assertNull( stream.current );
	}
}

