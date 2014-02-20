package it.unimi.dsi.fastutil;

/*		 
 * Copyright (C) 2003-2014 Sebastiano Vigna 
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

import it.unimi.dsi.fastutil.PriorityQueue;

/** A class providing static methods and objects that do useful things with priority queues.
 *
 * @see it.unimi.dsi.fastutil.PriorityQueue
 */

public class PriorityQueues {

	private PriorityQueues() {}

	/** An immutable class representing the empty priority queue.
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * {@link PriorityQueue}.
	 */

	@SuppressWarnings("rawtypes")
	public static class EmptyPriorityQueue extends AbstractPriorityQueue {

		protected EmptyPriorityQueue() {}

		public void enqueue( Object o ) { throw new UnsupportedOperationException(); }
		public Object dequeue() { throw new NoSuchElementException(); }
		public boolean isEmpty() { return true; }
		public int size() { return 0; }
		public void clear() {}
		public Object first() { throw new NoSuchElementException(); }
		public Object last() { throw new NoSuchElementException(); }
		public void changed() { throw new NoSuchElementException(); }
		public Comparator<?> comparator() { return null; }
		
	}

	/** An empty indirect priority queue (immutable).
	 */

	public final static EmptyPriorityQueue EMPTY_QUEUE = new EmptyPriorityQueue();


	/** A synchronized wrapper class for priority queues. */

	public static class SynchronizedPriorityQueue<K> implements PriorityQueue<K> {
		
		public static final long serialVersionUID = -7046029254386353129L;

		final protected PriorityQueue <K> q;
		final protected Object sync;

		protected SynchronizedPriorityQueue( final PriorityQueue <K> q, final Object sync ) {
			this.q = q;
			this.sync = sync;
		}

		protected SynchronizedPriorityQueue( final PriorityQueue <K> q ) {
			this.q = q;
			this.sync = this;
		}

		public void enqueue( K x ) { synchronized( sync ) { q.enqueue( x ); } }
		public K dequeue() { synchronized( sync ) { return q.dequeue(); } }
		public K first() { synchronized( sync ) { return q.first(); } }
		public K last() { synchronized( sync ) { return q.last(); } }
		public boolean isEmpty() { synchronized( sync ) { return q.isEmpty(); } }
		public int size() { synchronized( sync ) { return q.size(); } }
		public void clear() { synchronized( sync ) { q.clear(); } }
		public void changed() { synchronized( sync ) { q.changed(); } }
		public Comparator <? super K> comparator() { synchronized( sync ) { return q.comparator(); } }
	}


	/** Returns a synchronized priority queue backed by the specified priority queue.
	 *
	 * @param q the priority queue to be wrapped in a synchronized priority queue.
	 * @return a synchronized view of the specified priority queue.
	 */
	public static <K> PriorityQueue <K> synchronize( final PriorityQueue <K> q ) {	return new SynchronizedPriorityQueue<K>( q ); }

	/** Returns a synchronized priority queue backed by the specified priority queue, using an assigned object to synchronize.
	 *
	 * @param q the priority queue to be wrapped in a synchronized priority queue.
	 * @param sync an object that will be used to synchronize the access to the priority queue.
	 * @return a synchronized view of the specified priority queue.
	 */

	public static <K> PriorityQueue <K> synchronize( final PriorityQueue <K> q, final Object sync ) { return new SynchronizedPriorityQueue<K>( q, sync ); }
}
