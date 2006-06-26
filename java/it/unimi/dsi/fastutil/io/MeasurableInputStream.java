package it.unimi.dsi.fastutil.io;

import java.io.IOException;
import java.io.InputStream;

/** An {@link java.io.InputStream} that provides eager access to its length,
 * and keeps track of the current position (e.g., the number of bytes read so far). */

public abstract class MeasurableInputStream extends InputStream {
	
	/** Returns the overall length of this input stream. In most cases, this will require the input
	 *  stream to perform some extra action, possibly changing the state of the input stream itself (typically, reading
	 *  all the bytes up to the end), which also implies that an unchecked exception could be thrown in this case. 
	 *  Implementing classes should always document what state will the input stream be in
	 *  after calling this method, and which kind of unchecked exception could be thrown.
	 */ 
	public abstract long length();

	/** Returns the current position in this input stream.
	 * 
	 * <p>Usually, the position is just the number of bytes read
	 * since the stream was opened, but in the case of a
	 * {@link it.unimi.dsi.fastutil.io.RepositionableStream} it
	 * represent the current position.
	 */ 
	public abstract long position();
}
