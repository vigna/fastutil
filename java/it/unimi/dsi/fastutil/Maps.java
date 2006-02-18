/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2003, 2004, 2005, 2006 Sebastiano Vigna 
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

/** A class providing static methods and objects that do useful things with maps.
 *
 * @see Collections
 */

public class Maps {

	private Maps() {}

	/** A standard default return value to be used in maps contaning <code>null</code> values.
	 *
	 * <P>Maps with object values containing <code>null</code> values are usually
	 * problematic because there is no way to tell whether <code>get()</code>,
	 * <code>put()</code> and <code>remove()</code> did not find a key or the
	 * key was found but the associated value is <code>null</code>. This object can be used
	 * as a default return value to solve this problem:
	 *
	 * <pre>
	 * m = new Object2ObjectAVLTreeMap();
	 * m.defaultReturnValue(MISSING);
	 * [...]
	 * v = m.get(k);
	 * if (MISSING == v) ... // not found
	 * else ... // found
	 * </pre>
	 */

	public static final Object MISSING = new Object();
}

// Local Variables:
// mode: jde
// tab-width: 4
// End:
