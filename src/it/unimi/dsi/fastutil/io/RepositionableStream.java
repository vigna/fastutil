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

/** A basic interface specifying positioning methods for a byte stream.
 *
 * @author Sebastiano Vigna
 * @since 4.4
 */

public interface RepositionableStream {

	/** Sets the current stream position.
	 *
	 * @param newPosition the new stream position.
	 */
	void position(long newPosition) throws java.io.IOException;

	/** Returns the current stream position.
	 *
	 * @return the current stream position.
	 */
	long position() throws java.io.IOException;

}
