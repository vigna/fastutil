package test.it.unimi.dsi.fastutil.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;

import it.unimi.dsi.fastutil.io.FastBufferedInputStream;
import it.unimi.dsi.fastutil.io.FastBufferedInputStream.LineTerminator;
import junit.framework.TestCase;

public class FastBufferedInputStreamTest extends TestCase {

	public void testReadline( int bufferSize ) throws IOException {
		FastBufferedInputStream stream;
		byte[] b;
		
		stream = new FastBufferedInputStream( new ByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r' } ), bufferSize );
		
		b = new byte[ 4 ];
		stream.readLine( b, 0, b.length, EnumSet.of( LineTerminator.CR ) );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'C', 0 } ) );
		assertEquals( 4, stream.position() );
		assertEquals( -1, stream.readLine( b, 0, b.length, EnumSet.of( LineTerminator.CR ) ) );

		stream = new FastBufferedInputStream( new ByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r' } ), bufferSize );
		assertEquals( 4, stream.readLine( b, 0, b.length, EnumSet.of( LineTerminator.LF ) ) );
		assertEquals( 4, stream.position() );

		stream = new FastBufferedInputStream( new ByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r' } ), bufferSize );
		assertEquals( 4, stream.readLine( b, 0, b.length, EnumSet.of( LineTerminator.LF ) ) );
		assertEquals( 4, stream.position() );

		stream = new FastBufferedInputStream( new ByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r' } ), bufferSize );
		assertEquals( 4, stream.readLine( b, 0, b.length, EnumSet.of( LineTerminator.CR_LF ) ) );
		assertEquals( 4, stream.position() );

		stream = new FastBufferedInputStream( new ByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r' } ), bufferSize );
		assertEquals( 4, stream.readLine( b, 0, b.length, EnumSet.of( LineTerminator.CR_LF ) ) );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'C', '\r' } ) );
		assertEquals( 4, stream.position() );
		
		b = new byte[ 4 ];
		stream = new FastBufferedInputStream( new ByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r' } ), bufferSize );
		stream.readLine( b, 0, 2, EnumSet.of( LineTerminator.CR ) );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 0, 0 } ) );
		assertEquals( 2, stream.position() );
		
		// Reads with only LF as terminator
		stream = new FastBufferedInputStream( new ByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r', '\n', 'D' } ), bufferSize );
		assertEquals( 4, stream.readLine( b, 0, 4, EnumSet.of( LineTerminator.LF ) ) );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'C', '\r' } ) );
		assertEquals( 4, stream.position() );
		assertEquals( 0, stream.readLine( b, 0, 4, EnumSet.of( LineTerminator.LF ) ) );
		assertEquals( 5, stream.position() );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'C', '\r' } ) );
		assertEquals( 1, stream.readLine( b, 2, 2, EnumSet.of( LineTerminator.LF ) ) );
		assertEquals( 6, stream.position() );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'D', '\r' } ) );

		// Reads with both LF and CR/LF as terminators
		b = new byte[ 4 ];
		stream = new FastBufferedInputStream( new ByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r', '\n', 'D' } ), bufferSize );
		assertEquals( 3, stream.readLine( b, 0, 4, EnumSet.of( LineTerminator.CR, LineTerminator.CR_LF ) ) );
		assertEquals( 5, stream.position() );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'C', 0 } ) );
		assertEquals( 1, stream.readLine( b, 2, 2, EnumSet.of( LineTerminator.CR, LineTerminator.CR_LF ) ) );
		assertEquals( 6, stream.position() );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'D', 0 } ) );

		// Reads with only CR as terminator
		b = new byte[ 4 ];
		stream = new FastBufferedInputStream( new ByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r', '\n', 'D' } ), bufferSize );
		assertEquals( 3, stream.readLine( b, 0, 4, EnumSet.of( LineTerminator.CR ) ) );
		assertEquals( 4, stream.position() );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'C', 0 } ) );
		assertEquals( 2, stream.readLine( b, 2, 2, EnumSet.of( LineTerminator.CR ) ) );
		assertEquals( 6, stream.position() );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', '\n', 'D' } ) );

		// Reads with only CR/LF as terminator
		stream = new FastBufferedInputStream( new ByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r', '\n', 'D' } ), bufferSize );
		b = new byte[ 4 ];
		assertEquals( 3, stream.readLine( b, 0, 4, EnumSet.of( LineTerminator.CR_LF ) ) );
		assertEquals( 5, stream.position() );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'C', 0 } ) );
		assertEquals( 1, stream.readLine( b, 0, 4, EnumSet.of( LineTerminator.CR_LF ) ) );
		assertEquals( 6, stream.position() );
		assertTrue( Arrays.equals( b, new byte[] { 'D', 'B', 'C', 0 } ) );
		assertEquals( -1, stream.readLine( b, 0, 4, EnumSet.of( LineTerminator.CR_LF ) ) );
	}


	public void testReadLine() throws IOException {
		testReadline( 1 );
		testReadline( 2 );
		testReadline( 3 );
		testReadline( 4 );
		testReadline( 5 );
		testReadline( 6 );
		testReadline( 7 );
		testReadline( 100 );
	}

	
	public void testSkip( int bufferSize ) throws IOException {
		FastBufferedInputStream stream;
		
		stream = new FastBufferedInputStream( new ByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r', '\n', 'D' } ), bufferSize );
		assertEquals( 2, stream.skip( 2 ) );
		assertEquals( 2, stream.position() );
		assertEquals( 1, stream.skip( 1 ) );
		assertEquals( 3, stream.position() );
		assertEquals( 3, stream.skip( 4 ) );
		assertEquals( 6, stream.position() );
		assertEquals( 0, stream.skip( 1 ) );
		assertEquals( 6, stream.position() );
	}

	public void testSkip() throws IOException {
		testSkip( 1 );
		testSkip( 2 );
		testSkip( 3 );
		testSkip( 4 );
		testSkip( 5 );
		testSkip( 6 );
		testSkip( 7 );
		testSkip( 100 );
	}
	
	public void testPosition() throws IOException {
		File temp = File.createTempFile( this.getClass().getName(), ".tmp" );
		temp.deleteOnExit();
		FileOutputStream fos = new FileOutputStream( temp );
		fos.write( new byte[] { 0, 1, 2, 3, 4 } );
		fos.close();
		
		FastBufferedInputStream stream = new FastBufferedInputStream( new FileInputStream( temp ), 2 );
		byte[] b = new byte[ 2 ];
		stream.read( b );
		stream.reset();
		stream.position( 0 );
		assertEquals( 0, stream.read() );
		stream.close();
		
		stream = new FastBufferedInputStream( new FileInputStream( temp ) );
		b = new byte[ 1 ];
		stream.read( b );
		stream.reset();
		stream.position( 0 );
		assertEquals( 0, stream.read() );
		stream.close();
	}
}

