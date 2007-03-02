package test.it.unimi.dsi.fastutil.io;

import it.unimi.dsi.fastutil.io.FastBufferedInputStream;
import it.unimi.dsi.fastutil.io.FastBufferedOutputStream;
import it.unimi.dsi.fastutil.io.FastBufferedInputStream.LineTerminator;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Random;

import junit.framework.TestCase;

public class FastBufferedInputStreamTest extends TestCase {

	/** A byte array input stream that will return its data in small chunks,
	 * even it could actually return more data, and skips less bytes than it could.
	 */
	
	private static class BastardByteArrayInputStream extends ByteArrayInputStream {
		private final static long seed = System.currentTimeMillis();
		private final static Random r = new Random( seed );
		static {
			System.err.println( "Seed: " + seed );
		}

		int limit;
		
		public BastardByteArrayInputStream( byte[] array ) {
			super( array );
		}

		@Override
		public int read( byte[] buffer, int offset, int length ) {
			int k = r.nextInt( 2 ) + 1;
			return super.read( buffer, offset, length < k ? length : k );
		}
		
		public long skip( long n ) {
			int k = r.nextInt( 2 );
			return super.skip( n < k ? n : k );
		}

	}

	
	public void testReadline( int bufferSize ) throws IOException {
		FastBufferedInputStream stream;
		byte[] b;
		
		stream = new FastBufferedInputStream( new BastardByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r' } ), bufferSize );
		
		b = new byte[ 4 ];
		stream.readLine( b, 0, b.length, EnumSet.of( LineTerminator.CR ) );
		assertTrue( Arrays.toString( b ), Arrays.equals( b, new byte[] { 'A', 'B', 'C', 0 } ) );
		assertEquals( 4, stream.position() );
		assertEquals( -1, stream.readLine( b, 0, b.length, EnumSet.of( LineTerminator.CR ) ) );

		stream = new FastBufferedInputStream( new BastardByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r' } ), bufferSize );
		assertEquals( 4, stream.readLine( b, 0, b.length, EnumSet.of( LineTerminator.LF ) ) );
		assertEquals( 4, stream.position() );

		stream = new FastBufferedInputStream( new BastardByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r' } ), bufferSize );
		assertEquals( 4, stream.readLine( b, 0, b.length, EnumSet.of( LineTerminator.LF ) ) );
		assertEquals( 4, stream.position() );

		stream = new FastBufferedInputStream( new BastardByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r' } ), bufferSize );
		assertEquals( 4, stream.readLine( b, 0, b.length, EnumSet.of( LineTerminator.CR_LF ) ) );
		assertEquals( 4, stream.position() );

		stream = new FastBufferedInputStream( new BastardByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r' } ), bufferSize );
		assertEquals( 4, stream.readLine( b, 0, b.length, EnumSet.of( LineTerminator.CR_LF ) ) );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'C', '\r' } ) );
		assertEquals( 4, stream.position() );
		
		b = new byte[ 4 ];
		stream = new FastBufferedInputStream( new BastardByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r' } ), bufferSize );
		stream.readLine( b, 0, 2, EnumSet.of( LineTerminator.CR ) );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 0, 0 } ) );
		assertEquals( 2, stream.position() );
		
		// Reads with only LF as terminator
		stream = new FastBufferedInputStream( new BastardByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r', '\n', 'D' } ), bufferSize );
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
		stream = new FastBufferedInputStream( new BastardByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r', '\n', 'D' } ), bufferSize );
		assertEquals( 3, stream.readLine( b, 0, 4, EnumSet.of( LineTerminator.CR, LineTerminator.CR_LF ) ) );
		assertEquals( 5, stream.position() );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'C', 0 } ) );
		assertEquals( 1, stream.readLine( b, 2, 2, EnumSet.of( LineTerminator.CR, LineTerminator.CR_LF ) ) );
		assertEquals( 6, stream.position() );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'D', 0 } ) );

		// Reads with only CR as terminator
		b = new byte[ 4 ];
		stream = new FastBufferedInputStream( new BastardByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r', '\n', 'D' } ), bufferSize );
		assertEquals( 3, stream.readLine( b, 0, 4, EnumSet.of( LineTerminator.CR ) ) );
		assertEquals( 4, stream.position() );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', 'C', 0 } ) );
		assertEquals( 2, stream.readLine( b, 2, 2, EnumSet.of( LineTerminator.CR ) ) );
		assertEquals( 6, stream.position() );
		assertTrue( Arrays.equals( b, new byte[] { 'A', 'B', '\n', 'D' } ) );

		// Reads with only CR/LF as terminator
		stream = new FastBufferedInputStream( new BastardByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r', '\n', 'D' } ), bufferSize );
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
		
		stream = new FastBufferedInputStream( new BastardByteArrayInputStream( new byte[] { 'A', 'B', 'C', '\r', '\n', 'D' } ), bufferSize );
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
		stream.flush();
		stream.position( 0 );
		assertEquals( 0, stream.read() );
		stream.close();
		
		stream = new FastBufferedInputStream( new FileInputStream( temp ) );
		b = new byte[ 1 ];
		stream.read( b );
		stream.flush();
		stream.position( 0 );
		assertEquals( 0, stream.read() );
		stream.close();

		stream = new FastBufferedInputStream( new FileInputStream( temp ) );
		b = new byte[ 5 ];
		stream.read( b );
		stream.flush();
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
	
	int MAXTALKLEN = 1024*1024;
	int BLOCK_SIZE = 1024;
	byte[] command = new byte[ 1024 ];
	long fileLen = new File( "/tmp/NVCPENG.HL_" ).length(); 
	byte[] answer = new byte[ 1024 ];
	//		format makes it 1024 bytes long
	{
		System.arraycopy( "REQUEST FILE abc.txt".getBytes(), 0, command, 0, "REQUEST FILE abc.txt".getBytes().length );
		System.arraycopy( "Sending file abc.txt OF SIZE >10000<BYTES".getBytes(), 0, answer, 0, "Sending file abc.txt OF SIZE >10000<BYTES".getBytes().length );
	}

	public void testSocket() throws IOException {
		
		ServerSocket serverSocket = new ServerSocket( 11111 );
		int BLOCKSIZE = 131072;

		Thread clientThread = new Thread() {
			public void run() {
				try {
					Socket client = new Socket( "localhost", 11111 );
					BufferedInputStream in = new BufferedInputStream( client.getInputStream() );
					FastBufferedOutputStream out = new FastBufferedOutputStream( client.getOutputStream() );
					//	 	read 1024 bytes
					int t = -1;
					for( ;; ) {
						t++;
						out.write( command );
						out.flush();
						System.out.println( "Command sent (" + t + ")" );
						
						byte[] received = new byte[ 1024 ];
						int len = in.read( received, 0, 1024 );
						System.out.println("Received this:" + new String(received));
						//	 OK received? or not
						
						assertTrue( Arrays.equals( answer, received ) );

						byte[] block = new byte[BLOCK_SIZE];
						long bytesRemaining = fileLen;

						// Accurate transport of bytes from socket to file
						while (bytesRemaining > 0) {
							System.out.println("waiting.");
							if(bytesRemaining >= BLOCK_SIZE) {
								len = in.read(block, 0, BLOCK_SIZE);
							} else { // read precise amount left
								// int required in read() method, casting is ok, value < 130000
								len = in.read(block, 0, (int)bytesRemaining);
							}
							bytesRemaining -= len;

							System.out.println("currLen:" + len);
							System.out.println("bytesRemaining:" + bytesRemaining);
						}
					}
				}
				catch( Exception e ) {
					fail( e.toString() );
				}

			}
		};

		clientThread.start();

		Socket server = serverSocket.accept();
		FastBufferedOutputStream out = new FastBufferedOutputStream( server.getOutputStream() );
		BufferedInputStream in = new BufferedInputStream( server.getInputStream() );
		
		for(;;) {
			BufferedInputStream bis = new BufferedInputStream( new FileInputStream( "/tmp/NVCPENG.HL_" ) );

			long bytesRemaining = fileLen;

			byte[] received = new byte[ 1024 ];
			System.out.println( "Waiting for command..." );
			in.read( received );
			System.out.println( "Command received" );
			assertTrue( Arrays.equals( command, received ) );
			
			// build response and send e.g. SENDING FILE abc.text OF SIZE >10000<BYTES

			out.write( answer );
			out.flush();

			byte[] block = new byte[BLOCK_SIZE];
			int len;
			while(bytesRemaining > 0) {
				len = bis.read(block,0,BLOCK_SIZE);
				out.write(block,0,len);
				out.flush();
				bytesRemaining -= len;
			}
			bis.close();
		}
	}

}

