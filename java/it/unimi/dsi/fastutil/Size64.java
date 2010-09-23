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

/** An interface for data structures whose size can exceed {@link Integer#MAX_VALUE}.
 *
 * <P>The only method specified by this interfaces is {@link #size64()}. Interfaces such as
 * {@link BigCollection} work around the type problem of {@link java.util.Collection#size()}
 * (e.g., not being able to return more than {@link Integer#MAX_VALUE}) by implementing this
 * interfaces.
 *
 * <P>A good workaround to the excessive proliferation of "big" structures (e.g., {@link BigCollection})
 * is that of implementing this interface whenever possible: callers interested in large structures
 * can use a reflective call to <code>instanceof</code> to check for the presence of {@link #size64()}.
 */

public interface Size64 {
	/** Returns the size of this data structure as a long.
	 *
	 * @return  the size of this data structure.
	 */
	long size64();

	/** Returns the size of this data structure, minimised with {@link Integer#MAX_VALUE}.
	 * 
	 * @return the size of this data structure, minimised with {@link Integer#MAX_VALUE}.
	 * @see java.util.Collection#size()
	 * @deprecated Use {@link #size64()} instead.
	 */
	@Deprecated
	int size();
}
