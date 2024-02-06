/*
 * Copyright (C) 2003-2024 Sebastiano Vigna
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

import java.io.Serializable;
import java.util.Comparator;
import java.util.NoSuchElementException;

/** A class providing static methods and objects that do useful things with priority queues.
 *
 * @see it.unimi.dsi.fastutil.PriorityQueue
 */

public class PriorityQueues {

	private PriorityQueues() {}

	/** An immutable class representing the empty priority queue.
	 *
	 * <p>This class may be useful to implement your own in case you subclass
	 * {@link PriorityQueue}.
	 */

	@SuppressWarnings("rawtypes")
	public static class EmptyPriorityQueue implements PriorityQueue, Serializable {
		private static final long serialVersionUID = 0L;

		protected EmptyPriorityQueue() {}

		@Override
		public void enqueue(final Object o) { throw new UnsupportedOperationException(); }

		@Override
		public Object dequeue() { throw new NoSuchElementException(); }

		@Override
		public boolean isEmpty() { return true; }

		@Override
		public int size() { return 0; }

		@Override
		public void clear() {}

		@Override
		public Object first() { throw new NoSuchElementException(); }

		@Override
		public Object last() { throw new NoSuchElementException(); }

		@Override
		public void changed() { throw new NoSuchElementException(); }

		@Override
		public Comparator<?> comparator() { return null; }

		@Override
		public Object clone() { return EMPTY_QUEUE; }

		@Override
		public int hashCode() { return 0; }

		@Override
		public boolean equals(final Object o) { return o instanceof PriorityQueue && ((PriorityQueue)o).isEmpty(); }

		private Object readResolve() { return EMPTY_QUEUE; }
	}

	/** An empty indirect priority queue (immutable). */

	public static final EmptyPriorityQueue EMPTY_QUEUE = new EmptyPriorityQueue();

	/** Returns an empty queue (immutable). It is serializable and cloneable.
	 *
	 * <p>This method provides a typesafe access to {@link #EMPTY_QUEUE}.
	 * @param <K> the class of the objects in the queue.
	 * @return an empty queue (immutable).
	 */
	@SuppressWarnings("unchecked")
	public static <K> PriorityQueue<K> emptyQueue() {
		return EMPTY_QUEUE;
	}

	/** A synchronized wrapper class for priority queues. */

	public static class SynchronizedPriorityQueue<K> implements PriorityQueue<K>, Serializable {
		public static final long serialVersionUID = -7046029254386353129L;

		protected final PriorityQueue <K> q;
		protected final Object sync;

		protected SynchronizedPriorityQueue(final PriorityQueue <K> q, final Object sync) {
			this.q = q;
			this.sync = sync;
		}

		protected SynchronizedPriorityQueue(final PriorityQueue <K> q) {
			this.q = q;
			this.sync = this;
		}

		@Override
		public void enqueue(final K x) { synchronized(sync) { q.enqueue(x); } }

		@Override
		public K dequeue() { synchronized(sync) { return q.dequeue(); } }

		@Override
		public K first() { synchronized(sync) { return q.first(); } }

		@Override
		public K last() { synchronized(sync) { return q.last(); } }

		@Override
		public boolean isEmpty() { synchronized(sync) { return q.isEmpty(); } }

		@Override
		public int size() { synchronized(sync) { return q.size(); } }

		@Override
		public void clear() { synchronized(sync) { q.clear(); } }

		@Override
		public void changed() { synchronized(sync) { q.changed(); } }

		@Override
		public Comparator <? super K> comparator() { synchronized(sync) { return q.comparator(); } }

		@Override
		public String toString() { synchronized(sync) { return q.toString(); } }

		@Override
		public int hashCode() { synchronized(sync) { return q.hashCode(); } }

		@Override
		public boolean equals(final Object o) { if (o == this) return true; synchronized(sync) { return q.equals(o); } }

		private void writeObject(final java.io.ObjectOutputStream s) throws java.io.IOException {
			synchronized(sync) { s.defaultWriteObject(); }
		}
	}


	/** Returns a synchronized priority queue backed by the specified priority queue.
	 *
	 * @param <K> the class of the objects in the queue.
	 * @param q the priority queue to be wrapped in a synchronized priority queue.
	 * @return a synchronized view of the specified priority queue.
	 */
	public static <K> PriorityQueue <K> synchronize(final PriorityQueue <K> q) { return new SynchronizedPriorityQueue<>(q); }

	/** Returns a synchronized priority queue backed by the specified priority queue, using an assigned object to synchronize.
	 *
	 * @param <K> the class of the objects in the queue.
	 * @param q the priority queue to be wrapped in a synchronized priority queue.
	 * @param sync an object that will be used to synchronize the access to the priority queue.
	 * @return a synchronized view of the specified priority queue.
	 */

	public static <K> PriorityQueue <K> synchronize(final PriorityQueue <K> q, final Object sync) { return new SynchronizedPriorityQueue<>(q, sync); }
}
