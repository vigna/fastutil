package it.unimi.dsi.fastutil;

/*		 
 * Copyright (C) 2002-2014 Sebastiano Vigna 
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


/** An abstract class providing basic methods for implementing the {@link Stack} interface.
 *
 * <P>This class just defines {@link Stack#top()} as {@link Stack#peek(int) peek(0)}, and
 * {@link Stack#peek(int)} as throwing an {@link UnsupportedOperationException}.
 *
 * Subclasses of this class may choose to implement just {@link Stack#push(Object)},
 * {@link Stack#pop()} and {@link Stack#isEmpty()}, or (but this is not
 * required) go farther and implement {@link Stack#top()}, or even {@link
 * Stack#peek(int)}.
 */

public abstract class AbstractStack<K> implements Stack<K> {

	public K top() {
		return peek( 0 );
	}

	public K peek( int i ) {
		throw new UnsupportedOperationException();
	}

}
