/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2003 Sebastiano Vigna 
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

import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.shorts.*;
import it.unimi.dsi.fastutil.chars.*;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.floats.*;
import it.unimi.dsi.fastutil.doubles.*;
import it.unimi.dsi.fastutil.objects.*;

import java.util.Comparator;
import java.util.NoSuchElementException;

/** A class providing static methods and objects that do useful things with priority queues.
 *
 * @see PriorityQueue
 */

public class PriorityQueues {

	private PriorityQueues() {}

	/** An immutable class representing the empty priority queue and implementing all type-specific priority queue interfaces.
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific priority queue.
	 */

	public static class EmptyPriorityQueue implements 
		BytePriorityQueue, ShortPriorityQueue, IntPriorityQueue, LongPriorityQueue, CharPriorityQueue,
		FloatPriorityQueue, DoublePriorityQueue, PriorityQueue {
		
		public static final long serialVersionUID = -7046029254386353129L;

		protected EmptyPriorityQueue() {}

		public void enqueue( Object ok ) { throw new UnsupportedOperationException(); }

		public void enqueue( byte k ) { throw new UnsupportedOperationException(); }
		public void enqueue( char k ) { throw new UnsupportedOperationException(); }
		public void enqueue( short k ) { throw new UnsupportedOperationException(); }
		public void enqueue( int k ) { throw new UnsupportedOperationException(); }
		public void enqueue( long k ) { throw new UnsupportedOperationException(); }
		public void enqueue( float k ) { throw new UnsupportedOperationException(); }
		public void enqueue( double k ) { throw new UnsupportedOperationException(); }

		public Object dequeue() { throw new NoSuchElementException(); }

		public byte dequeueByte() { throw new NoSuchElementException(); }
		public char dequeueChar() { throw new NoSuchElementException(); }
		public short dequeueShort() { throw new NoSuchElementException(); }
		public int dequeueInt() { throw new NoSuchElementException(); }
		public long dequeueLong() { throw new NoSuchElementException(); }
		public float dequeueFloat() { throw new NoSuchElementException(); }
		public double dequeueDouble() { throw new NoSuchElementException(); }

		public Object first() { throw new NoSuchElementException(); }

		public byte firstByte() { throw new NoSuchElementException(); }
		public char firstChar() { throw new NoSuchElementException(); }
		public short firstShort() { throw new NoSuchElementException(); }
		public int firstInt() { throw new NoSuchElementException(); }
		public long firstLong() { throw new NoSuchElementException(); }
		public float firstFloat() { throw new NoSuchElementException(); }
		public double firstDouble() { throw new NoSuchElementException(); }

		public Object last() { throw new NoSuchElementException(); }

		public byte lastByte() { throw new NoSuchElementException(); }
		public char lastChar() { throw new NoSuchElementException(); }
		public short lastShort() { throw new NoSuchElementException(); }
		public int lastInt() { throw new NoSuchElementException(); }
		public long lastLong() { throw new NoSuchElementException(); }
		public float lastFloat() { throw new NoSuchElementException(); }
		public double lastDouble() { throw new NoSuchElementException(); }

		public Comparator comparator() { return null; }

		public void changed() { throw new NoSuchElementException(); }
		public int size() { return 0; }
		public void clear() {}
		public boolean isEmpty() { return true; }

	}


	/** An empty priority queue (immutable).
	 *
	 * <P>The class of this objects represent an abstract empty priority queue
	 * that implements any type of priority queue. Thus, {@link #EMPTY_QUEUE}
	 * may be assigned to a variable of any type-specific priority queue.
	 */

	public static final EmptyPriorityQueue EMPTY_QUEUE = new EmptyPriorityQueue();

}

// Local Variables:
// mode: jde
// tab-width: 4
// End:
