package it.unimi.dsi.fastutil.io;

/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2010-2011 Sebastiano Vigna 
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */


import java.io.IOException;

/** An stream that provides eager access to its length,
 * and keeps track of the current position (e.g., the number of bytes read so far, or the current
 * position of the file pointer). 
 *
 * <P>This class has two methods, both specified as optional. This apparently bizarre
 * behaviour is necessary because of wrapper classes which use reflection 
 * to support those methods (see, e.g., {@link MeasurableInputStream}, {@link FastBufferedInputStream} and {@link FastBufferedOutputStream}).
 * 
 * @since 6.0.0
 */

public interface MeasurableStream  {
	
	/** Returns the overall length of this stream (optional operation). In most cases, this will require the
	 *  stream to perform some extra action, possibly changing the state of the input stream itself (typically, reading
	 *  all the bytes up to the end, or flushing on output stream).
	 *  Implementing classes should always document what state will the input stream be in
	 *  after calling this method, and which kind of exception could be thrown.
	 */ 
	public long length() throws IOException;

	/** Returns the current position in this stream (optional operation).
	 * 
	 * <p>Usually, the position is just the number of bytes read or written
	 * since the stream was opened, but in the case of a
	 * {@link it.unimi.dsi.fastutil.io.RepositionableStream} it
	 * represent the current position.
	 */ 
	public long position() throws IOException;
}
