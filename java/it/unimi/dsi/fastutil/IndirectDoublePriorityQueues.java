/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2003, 2004, 2005, 2006 Sebastiano Vigna 
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

import java.util.Comparator;
import java.util.NoSuchElementException;

/** A class providing static methods and objects that do useful things with indirect priority queues.
 *
 * @see IndirectDoublePriorityQueue
 */

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
		public Comparator secondaryComparator() { return null; }
		
	}

	/** An empty indirect double priority queue (immutable).
	 */

	public final static EmptyIndirectDoublePriorityQueue EMPTY_QUEUE = new EmptyIndirectDoublePriorityQueue();


	/** A synchronized wrapper class for indirect double priority queues. */

	public static class SynchronizedIndirectDoublePriorityQueue implements IndirectDoublePriorityQueue {
		
		public static final long serialVersionUID = -7046029254386353129L;

		final protected IndirectDoublePriorityQueue q;
		final protected Object sync;

		protected SynchronizedIndirectDoublePriorityQueue( final IndirectDoublePriorityQueue q, final Object sync ) {
			this.q = q;
			this.sync = sync;
		}

		protected SynchronizedIndirectDoublePriorityQueue( final IndirectDoublePriorityQueue q ) {
			this.q = q;
			this.sync = this;
		}

		public void enqueue( int x ) { synchronized( sync ) { q.enqueue( x ); } }
		public int dequeue() { synchronized( sync ) { return q.dequeue(); } }
		public int first() { synchronized( sync ) { return q.first(); } }
		public int last() { synchronized( sync ) { return q.last(); } }
		public int secondaryFirst() { synchronized( sync ) { return q.secondaryFirst(); } }
		public int secondaryLast() { synchronized( sync ) { return q.secondaryLast(); } }
		public boolean isEmpty() { synchronized( sync ) { return q.isEmpty(); } }
		public int size() { synchronized( sync ) { return q.size(); } }
		public void clear() { synchronized( sync ) { q.clear(); } }
		public void changed() { synchronized( sync ) { q.changed(); } }
		public void allChanged() { synchronized( sync ) { q.allChanged(); } }
		public void changed( int i ) { synchronized( sync ) { q.changed( i ); } }
		public void remove( int i ) { synchronized( sync ) { q.remove( i ); } }
		public Comparator comparator() { synchronized( sync ) { return q.comparator(); } }
		public Comparator secondaryComparator() { synchronized( sync ) { return q.secondaryComparator(); } }
	}


	/** Returns a synchronized type-specific indirect double priority queue backed by the specified type-specific indirect double priority queue.
	 *
	 * @param q the indirect double priority queue to be wrapped in a synchronized indirect double priority queue.
	 * @return a synchronized view of the specified indirect double priority queue.
	 */
	public static IndirectDoublePriorityQueue synchronize( final IndirectDoublePriorityQueue q ) {	return new SynchronizedIndirectDoublePriorityQueue( q ); }

	/** Returns a synchronized type-specific indirect double priority queue backed by the specified type-specific indirect double priority queue, using an assigned object to synchronize.
	 *
	 * @param q the indirect double priority queue to be wrapped in a synchronized indirect double priority queue.
	 * @param sync an object that will be used to synchronize the access to the indirect double priority queue.
	 * @return a synchronized view of the specified indirect double priority queue.
	 */

	public static IndirectDoublePriorityQueue synchronize( final IndirectDoublePriorityQueue q, final Object sync ) { return new SynchronizedIndirectDoublePriorityQueue( q, sync ); }

}
