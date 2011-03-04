package it.unimi.dsi.fastutil.io;

/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2005-2011 Sebastiano Vigna 
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


/** A basic interface specifying positioning methods for a byte stream.
 *
 * @author Sebastiano Vigna
 * @since 4.4
 */

public interface RepositionableStream {

	/** Sets the current stream position.
	 *
	 * @param newPosition the new stream position.
	 */
	void position( long newPosition ) throws java.io.IOException;

	/** Returns the current stream position.
	 *
	 * @return the current stream position.
	 */
	long position() throws java.io.IOException;

}
