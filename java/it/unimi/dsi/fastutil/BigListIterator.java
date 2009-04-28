package it.unimi.dsi.fastutil;

/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2009 Sebastiano Vigna 
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

import java.util.Iterator;
import java.util.ListIterator;

/** A list iterator over a {@link BigList}.
 *
 * <P>This kind of iterator is essentially a {@link ListIterator} that
 * provides replacement methods {@link #previousBigIndex()} and
 * {@link #nextBigIndex()} so that {@linkplain BigList big lists} can 
 * return a proper iterator. The standard methods {@link ListIterator#previousIndex()}
 * and {@link ListIterator#nextIndex()} are deprecated.
 *
 * @see Iterator
 * @see ListIterator
 */

public interface BigListIterator<K> extends ListIterator<K>, BidirectionalIterator<K> {

	/** Returns the index of the element that would be returned by a subsequent call to next.
	 * (Returns list size if the list iterator is at the end of the list.)
	 * 
	 * @return the index of the element that would be returned by a subsequent call to next, or list
	 * size if list iterator is at end of list.
	 * @see ListIterator#nextIndex()
	 */
	long nextBigIndex();

	/** Returns the index of the element that would be returned by a subsequent call to next.
	 * (Returns list size if the list iterator is at the end of the list.)
	 * 
	 * @return the index of the element that would be returned by a subsequent call to next, or list
	 * size if list iterator is at end of list.
	 * @throws IllegalStateException if the result is larger than {@link Integer#MAX_VALUE}.
	 * @see ListIterator#nextIndex()
	 * @deprecated Use {@link #nextBigIndex()} to avoid throwing an {@link IllegalStateException}.
	 */
	@Deprecated
	int nextIndex();

	/** Returns the index of the element that would be returned by a subsequent call to previous.
	 * (Returns -1 if the list iterator is at the beginning of the list.)
	 * 
	 * @return the index of the element that would be returned by a subsequent call to previous, or
	 * -1 if list iterator is at beginning of list.
	 * @see ListIterator#previousIndex()
	 */

	long previousBigIndex();

	/** Returns the index of the element that would be returned by a subsequent call to previous.
	 * (Returns -1 if the list iterator is at the beginning of the list.)
	 * 
	 * @return the index of the element that would be returned by a subsequent call to previous, or
	 * -1 if list iterator is at beginning of list.
	 * @throws IllegalStateException if the result is larger than {@link Integer#MAX_VALUE}.
	 * @see ListIterator#previousIndex()
	 * @deprecated Use {@link #previousBigIndex()} to avoid throwing an {@link IllegalStateException}.
	 */
	@Deprecated
	int previousIndex();
}
