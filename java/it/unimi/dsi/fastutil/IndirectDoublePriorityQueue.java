package it.unimi.dsi.fastutil;

/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2003-2008 Paolo Boldi and Sebastiano Vigna 
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

/** An indirect double priority queue.
 *
 * <P>An indirect double priority queue uses two distinct comparators (called <em>primary</em>
 * and <em>secondary</em>) to keep its elements ordered. It makes it possible to access the
 * first element w.r.t. the secondary comparatory using {@link #secondaryFirst()} (and, optionally,
 * the last element using  {@link #secondaryLast()}). The remaining methods
 * work like those of an {@linkplain it.unimi.dsi.fastutil.IndirectPriorityQueue indirect priority queue} based on the
 * primary comparator.
 */

public interface IndirectDoublePriorityQueue<K> extends IndirectPriorityQueue<K> {

	/** Returns the secondary comparator of this queue.
	 *
	 * @return the secondary comparator of this queue.
	 * @see #secondaryFirst()
	 */
	public Comparator<? super K> secondaryComparator();

	/** Returns the first element of this queue with respect to the {@linkplain #secondaryComparator() secondary comparator}.
	 *
	 * @return the first element of this queue w.r.t. the {@linkplain #secondaryComparator() secondary comparator}.
	 */
	public int secondaryFirst();

	/** Returns the last element of this queue with respect to the {@linkplain #secondaryComparator() secondary comparator} (optional operation).
	 *
	 * @return the last element of this queue w.r.t. the {@linkplain #secondaryComparator() secondary comparator}.
	 */
	public int secondaryLast();

    /** Retrieves the secondary front of the queue in a given array (optional operation).
    *
    * @param a an array large enough to hold the secondary front (e.g., at least long as the reference array).
    * @return the number of elements actually written (starting from the first position of <code>a</code>).
    * @see IndirectPriorityQueue#front(int[])
    */

	public int secondaryFront( final int[] a );
}
