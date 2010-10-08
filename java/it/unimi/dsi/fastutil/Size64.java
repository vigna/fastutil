package it.unimi.dsi.fastutil;

/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2010 Sebastiano Vigna 
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

import java.util.Collection;

/** An interface for data structures whose size can exceed {@link Integer#MAX_VALUE}.
 *
 * <P>The only methods specified by this interfaces are {@link #size64()}, and 
 * a deprecated {@link #size()} identical to {@link Collection#size()}. Implementations
 * can work around the type problem of {@link java.util.Collection#size()}
 * (e.g., not being able to return more than {@link Integer#MAX_VALUE}) by implementing this
 * interface. Callers interested in large structures
 * can use a reflective call to <code>instanceof</code> to check for the presence of {@link #size64()}.
 * 
 * <p>We remark that it is always a good idea to implement both {@link #size()} <em>and</em> {@link #size64()},
 * as the former might be implemented by a superclass in an incompatible way. If you implement this interface,
 * just implement {@link #size()} as a <em>deprecated</em> method returning <code>Math.min(Integer.MAX_VALUE, size64())</code>.
 */

public interface Size64 {
	/** Returns the size of this data structure as a long.
	 *
	 * @return  the size of this data structure.
	 */
	long size64();

	/** Returns the size of this data structure, minimized with {@link Integer#MAX_VALUE}.
	 * 
	 * @return the size of this data structure, minimized with {@link Integer#MAX_VALUE}.
	 * @see java.util.Collection#size()
	 * @deprecated Use {@link #size64()} instead.
	 */
	@Deprecated
	int size();
}
