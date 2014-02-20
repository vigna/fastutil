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

/** A class providing static methods and objects that do useful things with indirect priority queues.
 *
 * @see IndirectPriorityQueue
 */

public class IndirectPriorityQueues {

	private IndirectPriorityQueues() {}

	/** An immutable class representing the empty indirect priority queue.
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * {@link IndirectPriorityQueue}.
	 */

	@SuppressWarnings("rawtypes")
	public static class EmptyIndirectPriorityQueue extends AbstractIndirectPriorityQueue {

		protected EmptyIndirectPriorityQueue() {}

		public void enqueue( final int i ) { throw new UnsupportedOperationException(); }
		public int dequeue() { throw new NoSuchElementException(); }
		public boolean isEmpty() { return true; }
		public int size() { return 0; }
		public boolean contains( int index ) { return false; }
		public void clear() {}
		public int first() { throw new NoSuchElementException(); }
		public int last() { throw new NoSuchElementException(); }
		public void changed() { throw new NoSuchElementException(); }
		public void allChanged() {}
		public Comparator<?> comparator() { return null; }
		public void changed( final int i ) { throw new IllegalArgumentException( "Index " + i + " is not in the queue" ); }
		public boolean remove( final int i ) { return false; }
		public int front( int[] a ) { return 0; }
		
	}

	/** An empty indirect priority queue (immutable).
	 */

	public final static EmptyIndirectPriorityQueue EMPTY_QUEUE = new EmptyIndirectPriorityQueue();


	/** A synchronized wrapper class for indirect priority queues. */

	public static class SynchronizedIndirectPriorityQueue<K> implements IndirectPriorityQueue<K> {
		
		public static final long serialVersionUID = -7046029254386353129L;

		final protected IndirectPriorityQueue<K> q;
		final protected Object sync;

		protected SynchronizedIndirectPriorityQueue( final IndirectPriorityQueue<K> q, final Object sync ) {
			this.q = q;
			this.sync = sync;
		}

		protected SynchronizedIndirectPriorityQueue( final IndirectPriorityQueue<K> q ) {
			this.q = q;
			this.sync = this;
		}

		public void enqueue( int x ) { synchronized( sync ) { q.enqueue( x ); } }
		public int dequeue() { synchronized( sync ) { return q.dequeue(); } }
		public boolean contains( final int index ) { synchronized( sync ) { return q.contains( index ); } }
		public int first() { synchronized( sync ) { return q.first(); } }
		public int last() { synchronized( sync ) { return q.last(); } }
		public boolean isEmpty() { synchronized( sync ) { return q.isEmpty(); } }
		public int size() { synchronized( sync ) { return q.size(); } }
		public void clear() { synchronized( sync ) { q.clear(); } }
		public void changed() { synchronized( sync ) { q.changed(); } }
		public void allChanged() { synchronized( sync ) { q.allChanged(); } }
		public void changed( int i ) { synchronized( sync ) { q.changed( i ); } }
		public boolean remove( int i ) { synchronized( sync ) { return q.remove( i ); } }
		public Comparator<? super K> comparator() { synchronized( sync ) { return q.comparator(); } }
		public int front( int[] a ) { return q.front( a ); }
	}


	/** Returns a synchronized type-specific indirect priority queue backed by the specified type-specific indirect priority queue.
	 *
	 * @param q the indirect priority queue to be wrapped in a synchronized indirect priority queue.
	 * @return a synchronized view of the specified indirect priority queue.
	 */
	public static <K> IndirectPriorityQueue<K> synchronize( final IndirectPriorityQueue<K> q ) {	return new SynchronizedIndirectPriorityQueue<K>( q ); }

	/** Returns a synchronized type-specific indirect priority queue backed by the specified type-specific indirect priority queue, using an assigned object to synchronize.
	 *
	 * @param q the indirect priority queue to be wrapped in a synchronized indirect priority queue.
	 * @param sync an object that will be used to synchronize the access to the indirect priority queue.
	 * @return a synchronized view of the specified indirect priority queue.
	 */

	public static <K> IndirectPriorityQueue<K> synchronize( final IndirectPriorityQueue<K> q, final Object sync ) { return new SynchronizedIndirectPriorityQueue<K>( q, sync ); }

}
