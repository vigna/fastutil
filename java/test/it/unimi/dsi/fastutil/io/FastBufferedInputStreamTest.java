package test.it.unimi.dsi.mg4j.io;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;

import it.unimi.dsi.fastutil.io.FastBufferedInputStream;
import it.unimi.dsi.fastutil.io.FastBufferedInputStream.LineTerminator;
import it.unimi.dsi.mg4j.io.FastByteArrayInputStream;
import junit.framework.TestCase;

/** This should really be in fastutil, but it's so damned
 * difficult to manage&hellip; */

public class FastBufferedInputStreamTest extends TestCase {

	public void testReadline( int bufferSize ) throws IOException {
		FastBufferedInputStream stream;
		byte[] b;
		
		stream = new FastBufferedInputStream( new FastByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r' } ), bufferSize );
		
		b = new byte[ 4 ];
		stream.readLine( b, 0, b.length, EnumSet.of( LineTerminator.CR ) );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'C', 0 } ) );
		assertEquals( -1, stream.readLine( b, 0, b.length, EnumSet.of( LineTerminator.CR ) ) );

		stream = new FastBufferedInputStream( new FastByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r' } ), bufferSize );
		assertEquals( 4, stream.readLine( b, 0, b.length, EnumSet.of( LineTerminator.LF ) ) );

		stream = new FastBufferedInputStream( new FastByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r' } ), bufferSize );
		assertEquals( 4, stream.readLine( b, 0, b.length, EnumSet.of( LineTerminator.LF ) ) );

		stream = new FastBufferedInputStream( new FastByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r' } ), bufferSize );
		assertEquals( 4, stream.readLine( b, 0, b.length, EnumSet.of( LineTerminator.CR_LF ) ) );

		stream = new FastBufferedInputStream( new FastByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r' } ), bufferSize );
		assertEquals( 4, stream.readLine( b, 0, b.length, EnumSet.of( LineTerminator.CR_LF ) ) );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'C', '\r' } ) );
		
		b = new byte[ 4 ];
		stream = new FastBufferedInputStream( new FastByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r' } ), bufferSize );
		stream.readLine( b, 0, 2, EnumSet.of( LineTerminator.CR ) );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 0, 0 } ) );
		
		// Reads with only LF as terminator
		stream = new FastBufferedInputStream( new FastByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r', '\n', 'D' } ), bufferSize );
		assertEquals( 4, stream.readLine( b, 0, 4, EnumSet.of( LineTerminator.LF ) ) );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'C', '\r' } ) );
		assertEquals( 0, stream.readLine( b, 0, 4, EnumSet.of( LineTerminator.LF ) ) );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'C', '\r' } ) );
		assertEquals( 1, stream.readLine( b, 2, 2, EnumSet.of( LineTerminator.LF ) ) );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'D', '\r' } ) );

		// Reads with both LF and CR/LF as terminators
		b = new byte[ 4 ];
		stream = new FastBufferedInputStream( new FastByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r', '\n', 'D' } ), bufferSize );
		assertEquals( 3, stream.readLine( b, 0, 4, EnumSet.of( LineTerminator.CR, LineTerminator.CR_LF ) ) );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'C', 0 } ) );
		assertEquals( 1, stream.readLine( b, 2, 2, EnumSet.of( LineTerminator.CR, LineTerminator.CR_LF ) ) );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'D', 0 } ) );

		// Reads with only CR as terminator
		b = new byte[ 4 ];
		stream = new FastBufferedInputStream( new FastByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r', '\n', 'D' } ), bufferSize );
		assertEquals( 3, stream.readLine( b, 0, 4, EnumSet.of( LineTerminator.CR ) ) );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'C', 0 } ) );
		assertEquals( 2, stream.readLine( b, 2, 2, EnumSet.of( LineTerminator.CR ) ) );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', '\n', 'D' } ) );

		// Reads with only CR/LF as terminator
		stream = new FastBufferedInputStream( new FastByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r', '\n', 'D' } ), bufferSize );
		b = new byte[ 4 ];
		assertEquals( 3, stream.readLine( b, 0, 4, EnumSet.of( LineTerminator.CR_LF ) ) );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'C', 0 } ) );
		assertEquals( 1, stream.readLine( b, 0, 4, EnumSet.of( LineTerminator.CR_LF ) ) );
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
		testReadline( 100 );
	}
} 
