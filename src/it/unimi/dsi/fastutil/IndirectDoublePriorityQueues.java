package it.unimi.dsi.fastutil;

/*
 * Copyright (C) 2003-2017 Sebastiano Vigna
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

/** A class providing static methods and objects that do useful things with indirect priority queues.
 *
 * @see IndirectDoublePriorityQueue
 * @deprecated this class will be removed in release 8.
 */

@Deprecated
public class IndirectDoublePriorityQueues {

	private IndirectDoublePriorityQueues() {}

	/** An immutable class representing the empty indirect double priority queue.
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * {@link IndirectDoublePriorityQueue}.
	 */

	public static class EmptyIndirectDoublePriorityQueue extends IndirectPriorityQueues.EmptyIndirectPriorityQueue {

		protected EmptyIndirectDoublePriorityQueue() {}

		public int secondaryFirst() { throw new NoSuchElementException(); }
		public int secondaryLast() { throw new NoSuchElementException(); }
		public Comparator<?> secondaryComparator() { return null; }

	}

	/** An empty indirect double priority queue (immutable).
	 */

	public final static EmptyIndirectDoublePriorityQueue EMPTY_QUEUE = new EmptyIndirectDoublePriorityQueue();


	/** A synchronized wrapper class for indirect double priority queues. */

	public static class SynchronizedIndirectDoublePriorityQueue<K> implements IndirectDoublePriorityQueue<K> {

		public static final long serialVersionUID = -7046029254386353129L;

		final protected IndirectDoublePriorityQueue<K> q;
		final protected Object sync;

		protected SynchronizedIndirectDoublePriorityQueue(final IndirectDoublePriorityQueue<K> q, final Object sync) {
			this.q = q;
			this.sync = sync;
		}

		protected SynchronizedIndirectDoublePriorityQueue(final IndirectDoublePriorityQueue<K> q) {
			this.q = q;
			this.sync = this;
		}

		public void enqueue(int index) { synchronized(sync) { q.enqueue(index); } }
		public int dequeue() { synchronized(sync) { return q.dequeue(); } }
		public int first() { synchronized(sync) { return q.first(); } }
		public int last() { synchronized(sync) { return q.last(); } }
		public boolean contains(final int index) { synchronized(sync) { return q.contains(index); } }
		public int secondaryFirst() { synchronized(sync) { return q.secondaryFirst(); } }
		public int secondaryLast() { synchronized(sync) { return q.secondaryLast(); } }
		public boolean isEmpty() { synchronized(sync) { return q.isEmpty(); } }
		public int size() { synchronized(sync) { return q.size(); } }
		public void clear() { synchronized(sync) { q.clear(); } }
		public void changed() { synchronized(sync) { q.changed(); } }
		public void allChanged() { synchronized(sync) { q.allChanged(); } }
		public void changed(int i) { synchronized(sync) { q.changed(i); } }
		public boolean remove(int i) { synchronized(sync) { return q.remove(i); } }
		public Comparator<? super K> comparator() { synchronized(sync) { return q.comparator(); } }
		public Comparator<? super K> secondaryComparator() { synchronized(sync) { return q.secondaryComparator(); } }
		public int secondaryFront(int[] a) { return q.secondaryFront(a); }
		public int front(int[] a) { return q.front(a); }
	}


	/** Returns a synchronized type-specific indirect double priority queue backed by the specified type-specific indirect double priority queue.
	 *
	 * @param q the indirect double priority queue to be wrapped in a synchronized indirect double priority queue.
	 * @return a synchronized view of the specified indirect double priority queue.
	 */
	public static <K> IndirectDoublePriorityQueue<K> synchronize(final IndirectDoublePriorityQueue<K> q) {	return new SynchronizedIndirectDoublePriorityQueue<K>(q); }

	/** Returns a synchronized type-specific indirect double priority queue backed by the specified type-specific indirect double priority queue, using an assigned object to synchronize.
	 *
	 * @param q the indirect double priority queue to be wrapped in a synchronized indirect double priority queue.
	 * @param sync an object that will be used to synchronize the access to the indirect double priority queue.
	 * @return a synchronized view of the specified indirect double priority queue.
	 */

	public static <K> IndirectDoublePriorityQueue<K> synchronize(final IndirectDoublePriorityQueue<K> q, final Object sync) { return new SynchronizedIndirectDoublePriorityQueue<K>(q, sync); }

}
