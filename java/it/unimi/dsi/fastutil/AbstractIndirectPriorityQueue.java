package it.unimi.dsi.fastutil;

/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2003-2008 Paolo Boldi and Sebastiano Vigna 
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

/** An abstract class providing basic methods for implementing the {@link IndirectPriorityQueue} interface.
 *
 * <P>This class defines {@link #changed(int)}, {@link #allChanged()},  {@link #remove(int)} and {@link #last()} as throwing an
 * {@link UnsupportedOperationException}.
 */

public abstract class AbstractIndirectPriorityQueue<K> implements IndirectPriorityQueue<K> {

	public int last() { throw new UnsupportedOperationException(); }

	public void changed() { changed( first() ); }

	public void changed( int index ) { throw new UnsupportedOperationException(); }

	public void allChanged() { throw new UnsupportedOperationException(); }

	public void remove( int index ) { throw new UnsupportedOperationException(); }

	public boolean isEmpty() { return size() == 0; }

}
