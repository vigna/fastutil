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

/** An indirect priority queue.
 *
 * <p>An indirect priority queue provides a way to {@linkplain #enqueue(int)
 * enqueue} by index elements taken from a given <em>reference list</em>,
 * and to {@linkplain #dequeue() dequeue} them in some specified order.
 * Elements that are <em>smaller</em> in the specified order are
 * dequeued first. It
 * is also possible to get the {@linkplain #first() index of the first element}, that
 * is, the index that would be dequeued next.
 *
 * <p>Additionally, the queue may provide a method to peek at the index of the
 * element that would be dequeued {@linkplain #last() last}.
 *
 * <p>The reference list should not change during queue operations (or, more
 * precisely, the relative order of the elements corresponding to indices in the queue should not
 * change). Nonetheless, some implementations may give the caller a way to
 * notify the queue that the {@linkplain #changed() first element has changed its
 * relative position in the order}.
 *
 * <p>Optionally, an indirect priority queue may even provide methods to notify
 * {@linkplain #changed(int) the change of <em>any</em> element of the
 * reference list}, to check {@linkplain #contains(int) the presence of
 * an index in the queue}, and to {@linkplain #remove(int) remove an index from the queue}.
 * It may even allow to notify that {@linkplain #allChanged() all elements have changed}.
 *
 * <p>It is always possible to enqueue two distinct indices corresponding to
 * equal elements of the reference list. However, depending on the
 * implementation, it may or may not be possible to enqueue twice the same
 * index.
 *
 * <p>Note that <em>all element manipulation happens via indices</em>.
 */

public interface IndirectPriorityQueue<K> {

	/** Enqueues a new element.
	 *
	 * @param index the element to enqueue.
	 */

	void enqueue(int index);

	/** Dequeues the {@linkplain #first() first} element from this queue.
	 *
	 * @return the dequeued element.
	 * @throws NoSuchElementException if this queue is empty.
	 */

	int dequeue();

	/** Checks whether this queue is empty.
	 *
	 * <P>This default implementation checks whether {@link #size()} is zero.
	 * @return true if this queue is empty.
	 */

	default boolean isEmpty() { return size() == 0; }

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
	 * <p>This default implementation just throws an {@link UnsupportedOperationException}.
	 *
	 * @return the last element.
	 * @throws NoSuchElementException if this queue is empty.
	 */

	default int last() { throw new UnsupportedOperationException(); }

	/** Notifies this queue that the {@linkplain #first() first element} has changed (optional operation).
	 *
	 * <p>This default implementation just calls {@link #changed(int)} with argument {@link #first()}.
	 */

	default void changed() {
		changed(first());
	}

	/** Returns the comparator associated with this queue, or {@code null} if it uses its elements' natural ordering.
	 *
	 * @return the comparator associated with this sorted set, or {@code null} if it uses its elements' natural ordering.
	 */
	Comparator <? super K> comparator();

	/** Notifies this queue that the specified element has changed (optional operation).
	 *
	 * <p>Note that the specified element must belong to this queue.
	 *
	 * <p>This default implementation just throws an {@link UnsupportedOperationException}.
	 *
	 * @param index the element that has changed.
	 * @throws NoSuchElementException if the specified element is not in this queue.
	 */

	default void changed(final int index) { throw new UnsupportedOperationException(); }


	/** Notifies this queue that the all elements have changed (optional operation).
	 *
	 * <p>This default implementation just throws an {@link UnsupportedOperationException}.
	 */

	default void allChanged() { throw new UnsupportedOperationException(); }

	/** Checks whether a given index belongs to this queue (optional operation).
	 *
	 * <p>This default implementation just throws an {@link UnsupportedOperationException}.
	 * @param index an index possibly in the queue.
	 * @return true if the specified index belongs to this queue.
	 */
	default boolean contains(final int index) { throw new UnsupportedOperationException(); }

	/** Removes the specified element from this queue (optional operation).
	 *
	 * <p>This default implementation just throws an {@link UnsupportedOperationException}.
	 * @param index the element to be removed.
	 * @return true if the index was in the queue.
	 */

	default boolean remove(final int index) { throw new UnsupportedOperationException(); }

	/** Retrieves the front of this queue in a given array (optional operation).
	 *
	 * <p>The <em>front</em> of an indirect queue is the set of indices whose associated elements in the reference array
	 * are equal to the element associated to the {@linkplain #first() first index}. These indices can be always obtain by dequeueing, but
	 * this method should retrieve efficiently such indices in the given array without modifying the state of this queue.
	 *
	 * <p>This default implementation just throws an {@link UnsupportedOperationException}.
	 *
	 * @param a an array large enough to hold the front (e.g., at least long as the reference array).
	 * @return the number of elements actually written (starting from the first position of {@code a}).
	 */

	default int front(final int[] a) { throw new UnsupportedOperationException(); }
}
