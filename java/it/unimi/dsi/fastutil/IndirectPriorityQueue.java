/*		 
 * fastdata: Fast & compact type-specific data structures for Java
 *
 * Copyright (C) 2002, 2003 Paolo Boldi and Sebastiano Vigna 
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

/** An indirect priority queue.
 *
 * <P>An indirect priority queue is an extension of a semi-indirect queue
 * providing also methods to notify {@linkplain #change(int) the change of
 * <em>any</em> element of the reference list}, and to {@linkplain #remove(int)
 * remove any element}.
 *
 * <P>Note that, because of this requirements, it is not possible to {@linkplain #enqueue(int) enqueue
 * twice} the same element in an indirect priority queue.
 *
 */

public interface IndirectPriorityQueue extends SemiIndirectPriorityQueue {

	/** Notifies the queue that the specified element has changed.
	 *
	 * <P>Note that the specified element must belong to the queue.
	 *
	 * @param index the element to be removed.
	 * @throws NoSuchElementException if the specified element is not in the queue.
	 */

	public void changed( int index );

	/** Removes the specified element from the queue.
	 *
	 * <P>Note that the specified element must belong to the queue.
	 *
	 * @param index the element to be removed.
	 * @throws NoSuchElementException if the specified element is not in the queue.
	 */

	public void remove( int index );

}

// Local Variables:
// mode: jde
// tab-width: 4
// End:
