package it.unimi.dsi.fastutil;

/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2002-2008 Sebastiano Vigna 
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
