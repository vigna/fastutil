package it.unimi.dsi.fastutil;

/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2003-2008 Sebastiano Vigna 
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

	@SuppressWarnings("unchecked")
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
		public Comparator comparator() { return null; }
		
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
