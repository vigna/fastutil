/*
 * Copyright (C) 2003-2024 Paolo Boldi and Sebastiano Vigna
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

import java.util.Comparator;
import java.util.NoSuchElementException;

/** A priority queue.
 *
 * <p>A priority queue provides a way to {@linkplain #enqueue(Object) enqueue}
 * elements, and to {@linkplain #dequeue() dequeue} them in some specified
 * order. Elements that are <em>smaller</em> in the specified order are
 * dequeued first.  It is also possible to get the {@linkplain #first() first
 * element}, that is, the element that would be dequeued next.
 *
 * <p>Additionally, the queue may provide a method to peek at
 * element that would be dequeued {@linkplain #last() last}.
 *
 * <p>The relative order of the elements enqueued should not change during
 * queue operations. Nonetheless, some implementations may give the caller a
 * way to notify the queue that the {@linkplain #changed() first element has
 * changed its relative position in the order}.
 */

public interface PriorityQueue<K> {

	/** Enqueues a new element.
	 *
	 * @param x the element to enqueue.
	 */

	void enqueue(K x);

	/** Dequeues the {@linkplain #first() first} element from the queue.
	 *
	 * @return the dequeued element.
	 * @throws NoSuchElementException if the queue is empty.
	 */

	K dequeue();

	/** Checks whether this queue is empty.
	 *
	 * <P>This default implementation checks whether {@link #size()} is zero.
	 * @return true if this queue is empty.
	 */

	default boolean isEmpty() {
		return size() == 0;
	}

	/** Returns the number of elements in this queue.
	 *
	 * @return the number of elements in this queue.
	 */

	int size();

	/** Removes all elements from this queue.
	 */

	void clear();

	/** Returns the first element of the queue.
	 *
	 * @return the first element.
	 * @throws NoSuchElementException if the queue is empty.
	 */

	K first();

	/** Returns the last element of the queue, that is, the element the would be dequeued last (optional operation).
	 *
	 * <p>This default implementation just throws an {@link UnsupportedOperationException}.
	 * @return the last element.
	 * @throws NoSuchElementException if the queue is empty.
	 */

	default K last() { throw new UnsupportedOperationException(); }

	/** Notifies the queue that the {@linkplain #first() first} element has changed (optional operation).
	 * <p>This default implementation just throws an {@link UnsupportedOperationException}.
	 */

	default void changed() { throw new UnsupportedOperationException(); }


	/** Returns the comparator associated with this queue, or {@code null} if it uses its elements' natural ordering.
	 *
	 * @return the comparator associated with this sorted set, or {@code null} if it uses its elements' natural ordering.
	 */
	Comparator<? super K> comparator();
}
