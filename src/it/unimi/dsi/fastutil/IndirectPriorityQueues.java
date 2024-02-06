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

import java.util.Comparator;
import java.util.NoSuchElementException;

/** A class providing static methods and objects that do useful things with indirect priority queues.
 *
 * @see IndirectPriorityQueue
 */

public class IndirectPriorityQueues {

	private IndirectPriorityQueues() {}

	/** An immutable class representing the empty indirect priority queue.
	 *
	 * <p>This class may be useful to implement your own in case you subclass
	 * {@link IndirectPriorityQueue}.
	 */

	@SuppressWarnings("rawtypes")
	public static class EmptyIndirectPriorityQueue implements IndirectPriorityQueue {

		protected EmptyIndirectPriorityQueue() {}

		@Override
		public void enqueue(final int i) { throw new UnsupportedOperationException(); }
		@Override
		public int dequeue() { throw new NoSuchElementException(); }
		@Override
		public boolean isEmpty() { return true; }
		@Override
		public int size() { return 0; }
		@Override
		public boolean contains(final int index) { return false; }
		@Override
		public void clear() {}
		@Override
		public int first() { throw new NoSuchElementException(); }
		@Override
		public int last() { throw new NoSuchElementException(); }
		@Override
		public void changed() { throw new NoSuchElementException(); }
		@Override
		public void allChanged() {}
		@Override
		public Comparator<?> comparator() { return null; }
		@Override
		public void changed(final int i) { throw new IllegalArgumentException("Index " + i + " is not in the queue"); }
		@Override
		public boolean remove(final int i) { return false; }
		@Override
		public int front(final int[] a) { return 0; }

	}

	/** An empty indirect priority queue (immutable).
	 */

	public static final EmptyIndirectPriorityQueue EMPTY_QUEUE = new EmptyIndirectPriorityQueue();


	/** A synchronized wrapper class for indirect priority queues. */

	public static class SynchronizedIndirectPriorityQueue<K> implements IndirectPriorityQueue<K> {

		public static final long serialVersionUID = -7046029254386353129L;

		protected final IndirectPriorityQueue<K> q;
		protected final Object sync;

		protected SynchronizedIndirectPriorityQueue(final IndirectPriorityQueue<K> q, final Object sync) {
			this.q = q;
			this.sync = sync;
		}

		protected SynchronizedIndirectPriorityQueue(final IndirectPriorityQueue<K> q) {
			this.q = q;
			this.sync = this;
		}

		@Override
		public void enqueue(final int x) { synchronized(sync) { q.enqueue(x); } }
		@Override
		public int dequeue() { synchronized(sync) { return q.dequeue(); } }
		@Override
		public boolean contains(final int index) { synchronized(sync) { return q.contains(index); } }
		@Override
		public int first() { synchronized(sync) { return q.first(); } }
		@Override
		public int last() { synchronized(sync) { return q.last(); } }
		@Override
		public boolean isEmpty() { synchronized(sync) { return q.isEmpty(); } }
		@Override
		public int size() { synchronized(sync) { return q.size(); } }
		@Override
		public void clear() { synchronized(sync) { q.clear(); } }
		@Override
		public void changed() { synchronized(sync) { q.changed(); } }
		@Override
		public void allChanged() { synchronized(sync) { q.allChanged(); } }
		@Override
		public void changed(final int i) { synchronized(sync) { q.changed(i); } }
		@Override
		public boolean remove(final int i) { synchronized(sync) { return q.remove(i); } }
		@Override
		public Comparator<? super K> comparator() { synchronized(sync) { return q.comparator(); } }
		@Override
		public int front(final int[] a) { return q.front(a); }
	}

	/** Returns a synchronized type-specific indirect priority queue backed by the specified type-specific indirect priority queue.
	 *
	 * @param q the indirect priority queue to be wrapped in a synchronized indirect priority queue.
	 * @return a synchronized view of the specified indirect priority queue.
	 */

	public static <K> IndirectPriorityQueue<K> synchronize(final IndirectPriorityQueue<K> q) {	return new SynchronizedIndirectPriorityQueue<>(q); }

	/** Returns a synchronized type-specific indirect priority queue backed by the specified type-specific indirect priority queue, using an assigned object to synchronize.
	 *
	 * @param q the indirect priority queue to be wrapped in a synchronized indirect priority queue.
	 * @param sync an object that will be used to synchronize the access to the indirect priority queue.
	 * @return a synchronized view of the specified indirect priority queue.
	 */

	public static <K> IndirectPriorityQueue<K> synchronize(final IndirectPriorityQueue<K> q, final Object sync) { return new SynchronizedIndirectPriorityQueue<>(q, sync); }

}
