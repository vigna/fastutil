package it.unimi.dsi.fastutil;

/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2003-2008 Paolo Boldi and Sebastiano Vigna 
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

	/** Dequeues the {@link #first()} element from the queue.
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

	/** Notifies the queue that the {@linkplain #first() first element} has changed (optional operation).
	 */

	void changed();

    /** Returns the comparator associated with this queue, or <code>null</code> if it uses its elements' natural ordering.
	 *
	 * @return the comparator associated with this sorted set, or <code>null</code> if it uses its elements' natural ordering.
	 */
	Comparator<? super K> comparator();
}
