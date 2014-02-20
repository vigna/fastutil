package it.unimi.dsi.fastutil;

/*		 
 * Copyright (C) 2003-2014 Paolo Boldi and Sebastiano Vigna 
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


import java.util.Comparator;
import java.util.NoSuchElementException;

/** A priority queue.
 *
 * <P>A priority queue provides a way to {@linkplain #enqueue(Object) enqueue}
 * elements, and to {@linkplain #dequeue() dequeue} them in some specified
 * order. Elements that are <em>smaller</em> in the specified order are
 * dequeued first.  It is also possible to get the {@linkplain #first() first
 * element}, that is, the element that would be dequeued next.
 *
 * <P>Additionally, the queue may provide a method to peek at 
 * element that would be dequeued {@linkplain #last() last}.
 *
 * <P>The relative order of the elements enqueued should not change during
 * queue operations. Nonetheless, some implementations may give the caller a
 * way to notify the queue that the {@linkplain #changed() first element has
 * changed its relative position in the order}.
 */

public interface PriorityQueue<K> {

	/** Enqueues a new element.
	 *
	 * @param x the element to enqueue..
	 */

	void enqueue( K x );

	/** Dequeues the {@linkplain #first() first} element from the queue.
	 *
	 * @return the dequeued element.
	 * @throws NoSuchElementException if the queue is empty.
	 */

	K dequeue();

	/** Checks whether the queue is empty.
	 *
	 * @return true if the queue is empty.
	 */

	boolean isEmpty();

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
	 * @return the last element.
	 * @throws NoSuchElementException if the queue is empty.
	 */

	K last();

	/** Notifies the queue that the {@linkplain #first() first} element has changed (optional operation).
	 */

	void changed();

    /** Returns the comparator associated with this queue, or <code>null</code> if it uses its elements' natural ordering.
	 *
	 * @return the comparator associated with this sorted set, or <code>null</code> if it uses its elements' natural ordering.
	 */
	Comparator<? super K> comparator();
}
