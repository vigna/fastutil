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

import java.util.NoSuchElementException;

/** A stack.
 *
 * <P>A stack must provide the classical {@link #push(Object)} and 
 * {@link #pop()} operations, but may be also <em>peekable</em>
 * to some extent: it may provide just the {@link #top()} function,
 * or even a more powerful {@link #peek(int)} method that provides
 * access to all elements on the stack (indexed from the top, which
 * has index 0).
 */

public interface Stack<K> {

	/** Pushes the given object on the stack.
	 *
	 * @param o the object that will become the new top of the stack.
	 */

	void push( K o );

	/** Pops the top off the stack.
	 *
	 * @return the top of the stack.
	 * @throws NoSuchElementException if the stack is empty.
	 */

	K pop();

	/** Checks whether the stack is empty.
	 *
	 * @return true if the stack is empty.
	 */

	boolean isEmpty();

	/** Peeks at the top of the stack (optional operation).
	 *
	 * @return the top of the stack.
	 * @throws NoSuchElementException if the stack is empty.
	 */

	K top();

	/** Peeks at an element on the stack (optional operation).
	 *
	 * @return the <code>i</code>-th element on the stack; 0 represents the top.
	 * @throws IndexOutOfBoundsException if the designated element does not exist..
	 */

	K peek( int i );

}
