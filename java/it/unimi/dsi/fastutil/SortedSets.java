/*		 
 * fastutil: Fast & compact type-specific collections for Java
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

import it.unimi.dsi.fastutil.booleans.*;
import it.unimi.dsi.fastutil.bytes.*;
import it.unimi.dsi.fastutil.shorts.*;
import it.unimi.dsi.fastutil.chars.*;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.floats.*;
import it.unimi.dsi.fastutil.doubles.*;
import it.unimi.dsi.fastutil.objects.*;

import it.unimi.dsi.fastutil.Iterators;
import it.unimi.dsi.fastutil.Arrays;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.NoSuchElementException;

/** A class providing static methods that do useful things with type-specific sorted sets.
 *
 * @see Collections
 */

public class SortedSets {

	private SortedSets() {}

	/** An empty sorted set (immutable). It is serialisable and cloneable.
	 *
	 * <P>The class of this objects represent an abstract empty sorted set
	 * that is a subset of any type of sorted set. Thus, {@link #EMPTY_SET}
	 * may be assigned a variable of any (sorted) type-specific set.
	 *
	 * <P>Note that this is just a useful copy of {@link Sets#EMPTY_SET}.
	 */

	public static final Sets.EmptySet EMPTY_SET = Sets.EMPTY_SET;
	
	
	
}

// Local Variables:
// mode: jde
// tab-width: 4
// End:
