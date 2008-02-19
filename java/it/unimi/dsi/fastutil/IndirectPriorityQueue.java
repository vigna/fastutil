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

/** An indirect priority queue.
 *
 * <P>An indirect priority queue provides a way to {@linkplain #enqueue(int)
 * enqueue} by index elements taken from a given <em>reference list</em>,
 * and to {@linkplain #dequeue() dequeue} them in some specified order.
 * Elements that are <em>smaller</em> in the specified order are
 * dequeued first. It
 * is also possible to get the {@linkplain #first() index of the first element}, that
 * is, the index that would be dequeued next.
 *
 * <P>Additionally, the queue may provide a method to peek at the index of the
 * element that would be dequeued {@linkplain #last() last}.
 *
 * <P>The reference list should not change during queue operations (or, more
 * precisely, the relative order of the elements in the queue should not
 * change). Nonetheless, some implementations may give the caller a way to
 * notify the queue that the {@linkplain #changed() first element has changed its 
 * relative position in the order}.
 *
 * <P>Optionally, an indirect priority queue may even provide methods to notify
 * {@linkplain #changed(int) the change of <em>any</em> element of the
 * reference list}, and to {@linkplain #remove(int) remove from the queue} any
 * element of the reference list presently in the queue. It may even allow to
 * notify that {@linkplain #allChanged() all elements have changed}.
 *
 * <P>It is always possible to enqueue two distinct indices corresponding to
 * equal elements of the reference list. However, depending on the
 * implementation, it may or may not be possible to enqueue twice the same
 * index.
 *
 * <P>Note that <em>all element manipulation happens via indices</em>.
 */

public interface IndirectPriorityQueue<K> {

	/** Enqueues a new element.
	 *
	 * @param index the element to enqueue..
	 */

	void enqueue( int index );

	/** Dequeues the {@linkplain #first() first} element from the queue.
	 *
	 * @return the dequeued element.
	 * @throws NoSuchElementException if the queue is empty.
	 */

	int dequeue();

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

	int first();

	/** Returns the last element of the queue, that is, the element the would be dequeued last (optional operation).
	 *
	 * @return the last element.
	 * @throws NoSuchElementException if the queue is empty.
	 */

	int last();

	/** Notifies the queue that the {@linkplain #first() first element} has changed (optional operation).
	 *
	 */

	void changed();

    /** Returns the comparator associated with this queue, or <code>null</code> if it uses its elements' natural ordering.
	 *
	 * @return the comparator associated with this sorted set, or <code>null</code> if it uses its elements' natural ordering.
	 */
	Comparator <? super K> comparator();

	/** Notifies the queue that the specified element has changed (optional operation).
	 *
	 * <P>Note that the specified element must belong to the queue.
	 *
	 * @param index the element that has changed.
	 * @throws NoSuchElementException if the specified element is not in the queue.
	 */

	public void changed( int index );

	/** Notifies the queue that the all elements have changed (optional operation).
	 */

	public void allChanged();

	/** Removes the specified element from the queue (optional operation).
	 *
	 * <P>Note that the specified element must belong to the queue.
	 *
	 * @param index the element to be removed.
	 * @throws NoSuchElementException if the specified element is not in the queue.
	 */

	public void remove( int index );

    /** Retrieves the front of the queue in a given array (optional operation).
     *
     * <p>The <em>front</em> of an indirect queue is the set of indices whose associated elements in the reference array 
     * are equal to the element associated to the {@linkplain #first() first index}. These indices can be always obtain by dequeueing, but 
     * this method should retrieve efficiently such indices in the given array without modifying the state of the queue.
     * 
     * @param a an array large enough to hold the front (e.g., at least long as the reference array).
     * @return the number of elements actually written (starting from the first position of <code>a</code>).
     */

	public int front( final int[] a );

}
