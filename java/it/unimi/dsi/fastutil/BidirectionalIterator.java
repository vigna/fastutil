/*		 
 * fastUtil 2.0: Fast & compact specialized utility classes for Java
 *
 * Copyright (C) 2002, 2003 Sebastiano Vigna 
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

import java.util.*;
import java.util.ListIterator;

/** A bidirectional {@link Iterator}.
 *
 * <P>This kind of iterator is esssentially a {@link java.util.ListIterator} that
 * does not support {@link java.util.ListIterator#previousIndex()} and {@link
 * java.util.ListIterator#nextIndex()}. It is useful for those maps that can easily
 * provide bidirectional iteration, but provide no index.
 *
 * @see Iterator
 * @see ListIterator
 */

public interface BidirectionalIterator extends Iterator {

	/** Returns the previous element from the collection.
	 *
	 * @return the previous element from the collection.
	 * @see ListIterator#previous()
	 */

	Object previous();

	/** Returns whether there is a previous element.
	 *
	 * @return whether there is a previous element.
	 * @see ListIterator#hasPrevious()
	 */

	boolean hasPrevious();

}

// Local Variables:
// mode: jde
// tab-width: 4
// End:
