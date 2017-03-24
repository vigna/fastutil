package it.unimi.dsi.fastutil;

/*
 * Copyright (C) 2003-2017 Paolo Boldi and Sebastiano Vigna
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

/** An indirect double priority queue.
 *
 * <P>An indirect double priority queue uses two distinct comparators (called <em>primary</em>
 * and <em>secondary</em>) to keep its elements ordered. It makes it possible to access the
 * first element w.r.t. the secondary comparatory using {@link #secondaryFirst()} (and, optionally,
 * the last element using  {@link #secondaryLast()}). The remaining methods
 * work like those of an {@linkplain it.unimi.dsi.fastutil.IndirectPriorityQueue indirect priority queue} based on the
 * primary comparator.
 *
 * @deprecated this interface will be removed in release 8.
 */

@Deprecated
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

	public int secondaryFront(final int[] a);
}
