/*
 * Copyright (C) 2005-2024 Sebastiano Vigna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.unimi.dsi.fastutil.io;

import java.io.IOException;

/** An stream that provides eager access to its length,
 * and keeps track of the current position (e.g., the number of bytes read so far, or the current
 * position of the file pointer).
 *
 * <p>This class has two methods, both specified as optional. This apparently bizarre
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
	long length() throws IOException;

	/** Returns the current position in this stream (optional operation).
	 *
	 * <p>Usually, the position is just the number of bytes read or written
	 * since the stream was opened, but in the case of a
	 * {@link it.unimi.dsi.fastutil.io.RepositionableStream} it
	 * represent the current position.
	 */
	long position() throws IOException;
}
