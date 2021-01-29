/*
 * Copyright (C) 2002-2021 Sebastiano Vigna
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

package it.unimi.dsi.fastutil;

import java.util.NoSuchElementException;

/** A stack.
 *
 * <p>A stack must provide the classical {@link #push(Object)} and
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

	void push(K o);

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
	 * <p>This default implementation returns {@link #peek(int) peek(0)}.
	 *
	 * @return the top of the stack.
	 * @throws NoSuchElementException if the stack is empty.
	 */

	default K top()  {
		return peek(0);
	}

	/** Peeks at an element on the stack (optional operation).
	 *
	 * <p>This default implementation just throws an {@link UnsupportedOperationException}.
	 *
	 * @param i an index from the stop of the stack (0 represents the top).
	 * @return the {@code i}-th element on the stack.
	 * @throws IndexOutOfBoundsException if the designated element does not exist..
	 */

	default K peek(final int i) {
		throw new UnsupportedOperationException();
	}
}
