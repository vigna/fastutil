package it.unimi.dsi.fastutil;

/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2002-2008 Sebastiano Vigna 
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

/** A bidirectional {@link Iterator}.
 *
 * <P>This kind of iterator is essentially a {@link ListIterator} that
 * does not support {@link ListIterator#previousIndex()} and {@link
 * ListIterator#nextIndex()}. It is useful for those maps that can easily
 * provide bidirectional iteration, but provide no index.
 *
 * <P>Note that iterators returned by <code>fastutil</code> classes are more
 * specific, and support skipping. This class serves the purpose of organising
 * in a cleaner way the relationships between various iterators.
 *
 * @see Iterator
 * @see ListIterator
 */

public interface BidirectionalIterator<K> extends Iterator<K> {

	/** Returns the previous element from the collection.
	 *
	 * @return the previous element from the collection.
	 * @see java.util.ListIterator#previous()
	 */

	K previous();

	/** Returns whether there is a previous element.
	 *
	 * @return whether there is a previous element.
	 * @see java.util.ListIterator#hasPrevious()
	 */

	boolean hasPrevious();
}
