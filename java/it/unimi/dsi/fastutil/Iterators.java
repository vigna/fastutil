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

import it.unimi.dsi.fastutil.booleans.BooleanListIterator;
import it.unimi.dsi.fastutil.bytes.ByteListIterator;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.longs.LongListIterator;
import it.unimi.dsi.fastutil.chars.CharListIterator;
import it.unimi.dsi.fastutil.floats.FloatListIterator;
import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;

import it.unimi.dsi.fastutil.objects.AbstractObjectListIterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** A class providing static methods and objects that do useful things with iterators.
 *
 * @see Iterator
 */

public class Iterators {

	private Iterators() {}

	/** A class returning no elements and implementing all type-specific iterator interfaces.
	 *
	 * <P>This class may be useful to implement your own in case you subclass
	 * a type-specific iterator.
	 */
	
	public static class EmptyIterator extends AbstractObjectListIterator implements 
		BooleanListIterator, ByteListIterator, ShortListIterator, IntListIterator,
		LongListIterator, CharListIterator, FloatListIterator, DoubleListIterator,
		ObjectListIterator {

		public static final long serialVersionUID = -7046029254386353129L;

		protected EmptyIterator() {}

		public boolean hasNext() { return false; }
		public boolean hasPrevious() { return false; }

		public boolean nextBoolean() { throw new NoSuchElementException(); }
		public boolean previousBoolean() { throw new NoSuchElementException(); }
		public byte nextByte() { throw new NoSuchElementException(); }
		public byte previousByte() { throw new NoSuchElementException(); }
		public short nextShort() { throw new NoSuchElementException(); }
		public short previousShort() { throw new NoSuchElementException(); }
		public int nextInt() { throw new NoSuchElementException(); }
		public int previousInt() { throw new NoSuchElementException(); }
		public long nextLong() { throw new NoSuchElementException(); }
		public long previousLong() { throw new NoSuchElementException(); }
		public char nextChar() { throw new NoSuchElementException(); }
		public char previousChar() { throw new NoSuchElementException(); }
		public float nextFloat() { throw new NoSuchElementException(); }
		public float previousFloat() { throw new NoSuchElementException(); }
		public double nextDouble() { throw new NoSuchElementException(); }
		public double previousDouble() { throw new NoSuchElementException(); }
		public Object next() { throw new NoSuchElementException(); }
		public Object previous() { throw new NoSuchElementException(); }

		public int nextIndex() { return 0; }
		public int previousIndex() { return -1; }

		public int skip( int n ) { return 0; };

		public void set( boolean x ) { throw new UnsupportedOperationException(); }
		public void add( boolean x ) { throw new UnsupportedOperationException(); }
		public void set( byte x ) { throw new UnsupportedOperationException(); }
		public void add( byte x ) { throw new UnsupportedOperationException(); }
		public void set( short x ) { throw new UnsupportedOperationException(); }
		public void add( short x ) { throw new UnsupportedOperationException(); }
		public void set( int x ) { throw new UnsupportedOperationException(); }
		public void add( int x ) { throw new UnsupportedOperationException(); }
		public void set( long x ) { throw new UnsupportedOperationException(); }
		public void add( long x ) { throw new UnsupportedOperationException(); }
		public void set( char x ) { throw new UnsupportedOperationException(); }
		public void add( char x ) { throw new UnsupportedOperationException(); }
		public void set( float x ) { throw new UnsupportedOperationException(); }
		public void add( float x ) { throw new UnsupportedOperationException(); }
		public void set( double x ) { throw new UnsupportedOperationException(); }
		public void add( double x ) { throw new UnsupportedOperationException(); }

		public Object clone() { return EMPTY_ITERATOR; }

        private Object readResolve() { return EMPTY_ITERATOR; }
	
	}

	/** An empty iterator (immutable). It is serializable and cloneable.
	 *
	 * <P>The class of this objects represent an abstract empty iterator
	 * that can iterate as any type-specific (list) iterator. Thus, {@link #EMPTY_ITERATOR}
	 * may be assigned to a variable of any type-specific (list) iterator.
	 */

	public final static EmptyIterator EMPTY_ITERATOR = new EmptyIterator();
		
}

// Local Variables:
// mode: jde
// tab-width: 4
// End:
