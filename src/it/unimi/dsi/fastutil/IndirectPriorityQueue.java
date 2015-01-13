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
 * precisely, the relative order of the elements corresponding to indices in the queue should not
 * change). Nonetheless, some implementations may give the caller a way to
 * notify the queue that the {@linkplain #changed() first element has changed its 
 * relative position in the order}.
 *
 * <P>Optionally, an indirect priority queue may even provide methods to notify
 * {@linkplain #changed(int) the change of <em>any</em> element of the
 * reference list}, to check {@linkplain #contains(int) the presence of
 * an index in the queue}, and to {@linkplain #remove(int) remove an index from the queue}. 
 * It may even allow to notify that {@linkplain #allChanged() all elements have changed}.
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
	 * @param index the element to enqueue.
	 */

	void enqueue( int index );

	/** Dequeues the {@linkplain #first() first} element from this queue.
	 *
	 * @return the dequeued element.
	 * @throws NoSuchElementException if this queue is empty.
	 */

	int dequeue();

	/** Checks whether this queue is empty.
	 *
	 * @return true if this queue is empty.
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

	/** Returns the first element of this queue.
	 *
	 * @return the first element.
	 * @throws NoSuchElementException if this queue is empty.
	 */

	int first();

	/** Returns the last element of this queue, that is, the element the would be dequeued last (optional operation).
	 *
	 * @return the last element.
	 * @throws NoSuchElementException if this queue is empty.
	 */

	int last();

	/** Notifies this queue that the {@linkplain #first() first element} has changed (optional operation).
	 *
	 */

	void changed();

    /** Returns the comparator associated with this queue, or <code>null</code> if it uses its elements' natural ordering.
	 *
	 * @return the comparator associated with this sorted set, or <code>null</code> if it uses its elements' natural ordering.
	 */
	Comparator <? super K> comparator();

	/** Notifies this queue that the specified element has changed (optional operation).
	 *
	 * <P>Note that the specified element must belong to this queue.
	 *
	 * @param index the element that has changed.
	 * @throws NoSuchElementException if the specified element is not in this queue.
	 */

	public void changed( int index );

	/** Notifies this queue that the all elements have changed (optional operation).
	 */

	public void allChanged();

	/** Checks whether a given index belongs to this queue (optional operation).
	 * 
	 * @param index an index possibly in the queue.
	 * @return true if the specified index belongs to this queue.
	 */
	public boolean contains( int index );
	
	/** Removes the specified element from this queue (optional operation).
	 *
	 * @param index the element to be removed.
	 * @return true if the index was in the queue.
	 */

	public boolean remove( int index );

    /** Retrieves the front of this queue in a given array (optional operation).
     *
     * <p>The <em>front</em> of an indirect queue is the set of indices whose associated elements in the reference array 
     * are equal to the element associated to the {@linkplain #first() first index}. These indices can be always obtain by dequeueing, but 
     * this method should retrieve efficiently such indices in the given array without modifying the state of this queue.
     * 
     * @param a an array large enough to hold the front (e.g., at least long as the reference array).
     * @return the number of elements actually written (starting from the first position of <code>a</code>).
     */

	public int front( final int[] a );

}
