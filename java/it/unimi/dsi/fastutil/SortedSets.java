/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2002, 2003, 2004 Sebastiano Vigna 
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

/** A class providing static methods and objects that do useful things with sorted sets.
 *
 * @see java.util.Collections
 */

public class SortedSets {

	private SortedSets() {}

	/** An empty sorted set (immutable). It is serializable and cloneable.
	 *
	 * <P>The class of this objects represent an abstract empty sorted set
	 * that is a subset of any type of sorted set. Thus, {@link #EMPTY_SET}
	 * may be assigned to a variable of any (sorted) type-specific set.
	 *
	 * <P>Note that this is just a useful copy of {@link Sets#EMPTY_SET}.
	 */

	public static final Sets.EmptySet EMPTY_SET = Sets.EMPTY_SET;
	
	
	
}

// Local Variables:
// mode: jde
// tab-width: 4
// End:
