/*		 
 * fastdata: Fast & compact type-specific data structures for Java
 *
 * Copyright (C) 2002, 2003 Paolo Boldi and Sebastiano Vigna 
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

package it.unimi.dsi.fastutil;

/** A priority queue.
 *
 * <P>A priority queue provides a way to {@linkplain #enqueue(int)
 * enqueue} elements,
 * and to {@linkplain #dequeue() dequeue} them in some specified order.  It
 * is also possible to get the {@linkolain #front() front element}, that
 * is, the element that would be dequeued next.
 *
 * <P>Additionally, the queue may provide a method to peek at 
 * element that would be dequeued {@linkplain #rear() last}.
 *
 * <P>Relative comparisons should not change during queue operations. Nonetheless,
 * some implementations may give the caller a way to notify the queue that 
 * the {@linkplain #changed() front element has changed}.
 */

public interface PriorityQueue {

	/** Enqueues a new element.
	 *
	 * @param x the element to enqueue..
	 */

	void enqueue( KEY_TYPE x );

	/** Dequeues the {@link #front()} element from the queue.
	 *
	 * @return the dequeued element.
	 * @throw NoSuchElementException if the queue is empty.
	 */

	Object dequeue();

	/** Checks whether the queue is empty.
	 *
	 * @return true if the queue is empty.
	 */

	boolean isEmpty();

	/** Removes all elements from this queue.
	 */

	void clear();

	/** Returns the front element of the queue.
	 *
	 * @return the front element.
	 * @throws NoSuchElementException if the queue is empty.
	 */

	Object front();

	/** Returns the rear element of the queue, that is, the element the would be dequeued last (optional operation).
	 *
	 * @return the rear element.
	 * @throws NoSuchElementException if the queue is empty.
	 */

	Object rear();

	/** Notifies the queue that the {@linkplain #front() front element} has changed (optional operation).
	 */

	void changed();

    /** Returns the comparator associated with this queue, or <code>null</code> if it uses its elements' natural ordering.
	 *
	 * @return the comparator associated with this sorted set, or <code>null</code> if it uses its elements' natural ordering.
	 */
	Comparator comparator();
}

// Local Variables:
// mode: jde
// tab-width: 4
// End:
