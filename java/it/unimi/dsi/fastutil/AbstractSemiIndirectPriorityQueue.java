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

/** An abstract class providing basic methods for implementing the {@link SemiIndirectPriorityQueue} interface.
 *
 * <P>This class just defines {@link #changed()} and
 * {@link #last()} as throwing an {@link UnsupportedOperationException}.
 *
 */

public abstract class AbstractSemiIndirectPriorityQueue implements SemiIndirectPriorityQueue {

	public int last() { throw new UnsupportedOperationException(); }

	public void changed() { throw new UnsupportedOperationException(); }

	public boolean isEmpty() { return size() == 0; }

}

// Local Variables:
// mode: jde
// tab-width: 4
// End:
