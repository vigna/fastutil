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

import java.util.Iterator;
import java.util.ListIterator;
import java.util.List;
import java.util.Collection;
import java.util.NoSuchElementException;

/** A class providing static methods that do useful things with lists.
 *
 * @see Lists
 */

public class Lists {

	private Lists() {}


	protected static class EmptyList extends Collections.EmptyCollection implements 
	   BooleanList, ByteList, ShortList, IntList, LongList, CharList,
	   FloatList, DoubleList, ObjectList, ReferenceList, java.io.Serializable, Cloneable {
		
		public static final long serialVersionUID = -7046029254386353129L;

		private EmptyList() {}

		public boolean add( Object k ) { throw new UnsupportedOperationException(); }
		public boolean remove( Object k ) { throw new UnsupportedOperationException(); }

		public boolean add( boolean k ) { throw new UnsupportedOperationException(); }
		public boolean add( byte k ) { throw new UnsupportedOperationException(); }
		public boolean add( char k ) { throw new UnsupportedOperationException(); }
		public boolean add( short k ) { throw new UnsupportedOperationException(); }
		public boolean add( int k ) { throw new UnsupportedOperationException(); }
		public boolean add( long k ) { throw new UnsupportedOperationException(); }
		public boolean add( float k ) { throw new UnsupportedOperationException(); }
		public boolean add( double k ) { throw new UnsupportedOperationException(); }
           
		public void add( int i, Object k ) { throw new UnsupportedOperationException(); }

		public void add( int i, boolean k ) { throw new UnsupportedOperationException(); }
		public void add( int i, byte k ) { throw new UnsupportedOperationException(); }
		public void add( int i, char k ) { throw new UnsupportedOperationException(); }
		public void add( int i, short k ) { throw new UnsupportedOperationException(); }
		public void add( int i, int k ) { throw new UnsupportedOperationException(); }
		public void add( int i, long k ) { throw new UnsupportedOperationException(); }
		public void add( int i, float k ) { throw new UnsupportedOperationException(); }
		public void add( int i, double k ) { throw new UnsupportedOperationException(); }

		public Object set( int i, Object k ) { throw new UnsupportedOperationException(); }

		public boolean set( int i, boolean k ) { throw new UnsupportedOperationException(); }
		public byte set( int i, byte k ) { throw new UnsupportedOperationException(); }
		public char set( int i, char k ) { throw new UnsupportedOperationException(); }
		public short set( int i, short k ) { throw new UnsupportedOperationException(); }
		public int set( int i, int k ) { throw new UnsupportedOperationException(); }
		public long set( int i, long k ) { throw new UnsupportedOperationException(); }
		public float set( int i, float k ) { throw new UnsupportedOperationException(); }
		public double set( int i, double k ) { throw new UnsupportedOperationException(); }

		public Object get( int i ) { throw new IndexOutOfBoundsException(); }

		public boolean getBoolean( int i ) { throw new IndexOutOfBoundsException(); }
		public byte getByte( int i ) { throw new IndexOutOfBoundsException(); }
		public char getChar( int i ) { throw new IndexOutOfBoundsException(); }
		public short getShort( int i ) { throw new IndexOutOfBoundsException(); }
		public int getInt( int i ) { throw new IndexOutOfBoundsException(); }
		public long getLong( int i ) { throw new IndexOutOfBoundsException(); }
		public float getFloat( int i ) { throw new IndexOutOfBoundsException(); }
		public double getDouble( int i ) { throw new IndexOutOfBoundsException(); }
           
		public int indexOf( Object k ) { return -1; }

		public int indexOf( boolean k ) { return -1; }
		public int indexOf( byte k ) { return -1; }
		public int indexOf( char k ) { return -1; }
		public int indexOf( short k ) { return -1; }
		public int indexOf( int k ) { return -1; }
		public int indexOf( long k ) { return -1; }
		public int indexOf( float k ) { return -1; }
		public int indexOf( double k ) { return -1; }

		public int lastIndexOf( Object k ) { return -1; }

		public int lastIndexOf( boolean k ) { return -1; }
		public int lastIndexOf( byte k ) { return -1; }
		public int lastIndexOf( char k ) { return -1; }
		public int lastIndexOf( short k ) { return -1; }
		public int lastIndexOf( int k ) { return -1; }
		public int lastIndexOf( long k ) { return -1; }
		public int lastIndexOf( float k ) { return -1; }
		public int lastIndexOf( double k ) { return -1; }

		public boolean addAll( int i, Collection c ) { throw new UnsupportedOperationException(); }

		public boolean addAll( int i, BooleanCollection c ) { throw new UnsupportedOperationException(); }
		public boolean addAll( int i, ByteCollection c ) { throw new UnsupportedOperationException(); }
		public boolean addAll( int i, CharCollection c ) { throw new UnsupportedOperationException(); }
		public boolean addAll( int i, ShortCollection c ) { throw new UnsupportedOperationException(); }
		public boolean addAll( int i, IntCollection c ) { throw new UnsupportedOperationException(); }
		public boolean addAll( int i, LongCollection c ) { throw new UnsupportedOperationException(); }
		public boolean addAll( int i, FloatCollection c ) { throw new UnsupportedOperationException(); }
		public boolean addAll( int i, DoubleCollection c ) { throw new UnsupportedOperationException(); }

		public boolean addAll( int i, List c ) { throw new UnsupportedOperationException(); }

		public boolean addAll( int i, BooleanList c ) { throw new UnsupportedOperationException(); }
		public boolean addAll( int i, ByteList c ) { throw new UnsupportedOperationException(); }
		public boolean addAll( int i, CharList c ) { throw new UnsupportedOperationException(); }
		public boolean addAll( int i, ShortList c ) { throw new UnsupportedOperationException(); }
		public boolean addAll( int i, IntList c ) { throw new UnsupportedOperationException(); }
		public boolean addAll( int i, LongList c ) { throw new UnsupportedOperationException(); }
		public boolean addAll( int i, FloatList c ) { throw new UnsupportedOperationException(); }
		public boolean addAll( int i, DoubleList c ) { throw new UnsupportedOperationException(); }

		public Object remove( int k ) { throw new UnsupportedOperationException(); }
		public boolean removeBoolean( int k ) { throw new UnsupportedOperationException(); }
		public byte removeByte( int k ) { throw new UnsupportedOperationException(); }
		public char removeChar( int k ) { throw new UnsupportedOperationException(); }
		public short removeShort( int k ) { throw new UnsupportedOperationException(); }
		public int removeInt( int k ) { throw new UnsupportedOperationException(); }
		public long removeLong( int k ) { throw new UnsupportedOperationException(); }
		public float removeFloat( int k ) { throw new UnsupportedOperationException(); }
		public double removeDouble( int k ) { throw new UnsupportedOperationException(); }

		public ByteBidirectionalIterator iterator( int i ) { if ( i == 0 ) return Iterators.EMPTY_ITERATOR; else throw new IndexOutOfBoundsException( i + "" ); }

		public BooleanListIterator booleanListIterator() { return Iterators.EMPTY_ITERATOR; }
		public ByteListIterator byteListIterator() { return Iterators.EMPTY_ITERATOR; }
		public CharListIterator charListIterator() { return Iterators.EMPTY_ITERATOR; }
		public ShortListIterator shortListIterator() { return Iterators.EMPTY_ITERATOR; }
		public IntListIterator intListIterator() { return Iterators.EMPTY_ITERATOR; }
		public LongListIterator longListIterator() { return Iterators.EMPTY_ITERATOR; }
		public FloatListIterator floatListIterator() { return Iterators.EMPTY_ITERATOR; }
		public DoubleListIterator doubleListIterator() { return Iterators.EMPTY_ITERATOR; }
		public ObjectListIterator objectListIterator() { return Iterators.EMPTY_ITERATOR; }
		public ObjectListIterator referenceListIterator() { return Iterators.EMPTY_ITERATOR; }
		public ListIterator listIterator() { return Iterators.EMPTY_ITERATOR; }


		public ListIterator listIterator( int i ) { if ( i == 0 ) return Iterators.EMPTY_ITERATOR; else throw new IndexOutOfBoundsException( i + "" ); }

		public BooleanListIterator booleanListIterator( int i ) { if ( i == 0 ) return Iterators.EMPTY_ITERATOR; else throw new IndexOutOfBoundsException( i + "" ); }
		public ByteListIterator byteListIterator( int i ) { if ( i == 0 ) return Iterators.EMPTY_ITERATOR; else throw new IndexOutOfBoundsException( i + "" ); }
		public CharListIterator charListIterator( int i ) { if ( i == 0 ) return Iterators.EMPTY_ITERATOR; else throw new IndexOutOfBoundsException( i + "" ); }
		public ShortListIterator shortListIterator( int i ) { if ( i == 0 ) return Iterators.EMPTY_ITERATOR; else throw new IndexOutOfBoundsException( i + "" ); }
		public IntListIterator intListIterator( int i ) { if ( i == 0 ) return Iterators.EMPTY_ITERATOR; else throw new IndexOutOfBoundsException( i + "" ); }
		public LongListIterator longListIterator( int i ) { if ( i == 0 ) return Iterators.EMPTY_ITERATOR; else throw new IndexOutOfBoundsException( i + "" ); }
		public FloatListIterator floatListIterator( int i ) { if ( i == 0 ) return Iterators.EMPTY_ITERATOR; else throw new IndexOutOfBoundsException( i + "" ); }
		public DoubleListIterator doubleListIterator( int i ) { if ( i == 0 ) return Iterators.EMPTY_ITERATOR; else throw new IndexOutOfBoundsException( i + "" ); }
		public ObjectListIterator objectListIterator( int i ) { if ( i == 0 ) return Iterators.EMPTY_ITERATOR; else throw new IndexOutOfBoundsException( i + "" ); }

		public List subList( int from, int to ) { if ( from == 0 && to == 0 ) return this; else throw new IndexOutOfBoundsException(); }

		public BooleanList booleanSubList( int from, int to ) { if ( from == 0 && to == 0 ) return this; else throw new IndexOutOfBoundsException(); }
		public ByteList byteSubList( int from, int to ) { if ( from == 0 && to == 0 ) return this; else throw new IndexOutOfBoundsException(); }
		public CharList charSubList( int from, int to ) { if ( from == 0 && to == 0 ) return this; else throw new IndexOutOfBoundsException(); }
		public ShortList shortSubList( int from, int to ) { if ( from == 0 && to == 0 ) return this; else throw new IndexOutOfBoundsException(); }
		public IntList intSubList( int from, int to ) { if ( from == 0 && to == 0 ) return this; else throw new IndexOutOfBoundsException(); }
		public LongList longSubList( int from, int to ) { if ( from == 0 && to == 0 ) return this; else throw new IndexOutOfBoundsException(); }
		public FloatList floatSubList( int from, int to ) { if ( from == 0 && to == 0 ) return this; else throw new IndexOutOfBoundsException(); }
		public DoubleList doubleSubList( int from, int to ) { if ( from == 0 && to == 0 ) return this; else throw new IndexOutOfBoundsException(); }
		public ObjectList objectSubList( int from, int to ) { if ( from == 0 && to == 0 ) return this; else throw new IndexOutOfBoundsException(); }
		public ReferenceList referenceSubList( int from, int to ) { if ( from == 0 && to == 0 ) return this; else throw new IndexOutOfBoundsException(); }

		public void getElements( int from, boolean[] a, int offset, int length ) { if ( from == 0 && length == 0 && offset >= 0 && offset <= a.length ) return; else throw new IndexOutOfBoundsException(); }
		public void getElements( int from, byte[] a, int offset, int length ) { if ( from == 0 && length == 0 && offset >= 0 && offset <= a.length ) return; else throw new IndexOutOfBoundsException(); }
		public void getElements( int from, char[] a, int offset, int length ) { if ( from == 0 && length == 0 && offset >= 0 && offset <= a.length ) return; else throw new IndexOutOfBoundsException(); }
		public void getElements( int from, short[] a, int offset, int length ) { if ( from == 0 && length == 0 && offset >= 0 && offset <= a.length ) return; else throw new IndexOutOfBoundsException(); }
		public void getElements( int from, int[] a, int offset, int length ) { if ( from == 0 && length == 0 && offset >= 0 && offset <= a.length ) return; else throw new IndexOutOfBoundsException(); }
		public void getElements( int from, long[] a, int offset, int length ) { if ( from == 0 && length == 0 && offset >= 0 && offset <= a.length ) return; else throw new IndexOutOfBoundsException(); }
		public void getElements( int from, float[] a, int offset, int length ) { if ( from == 0 && length == 0 && offset >= 0 && offset <= a.length ) return; else throw new IndexOutOfBoundsException(); }
		public void getElements( int from, double[] a, int offset, int length ) { if ( from == 0 && length == 0 && offset >= 0 && offset <= a.length ) return; else throw new IndexOutOfBoundsException(); }
		public void getElements( int from, Object[] a, int offset, int length ) { if ( from == 0 && length == 0 && offset >= 0 && offset <= a.length ) return; else throw new IndexOutOfBoundsException(); }

		public void removeElements( int from, int to ) { throw new UnsupportedOperationException(); }

		public void addElements( int index, final boolean a[], int offset, int length ) { throw new UnsupportedOperationException(); }
		public void addElements( int index, final boolean a[] ) { throw new UnsupportedOperationException(); }
		public void addElements( int index, final byte a[], int offset, int length ) { throw new UnsupportedOperationException(); }
		public void addElements( int index, final byte a[] ) { throw new UnsupportedOperationException(); }
		public void addElements( int index, final char a[], int offset, int length ) { throw new UnsupportedOperationException(); }
		public void addElements( int index, final char a[] ) { throw new UnsupportedOperationException(); }
		public void addElements( int index, final short a[], int offset, int length ) { throw new UnsupportedOperationException(); }
		public void addElements( int index, final short a[] ) { throw new UnsupportedOperationException(); }
		public void addElements( int index, final int a[], int offset, int length ) { throw new UnsupportedOperationException(); }
		public void addElements( int index, final int a[] ) { throw new UnsupportedOperationException(); }
		public void addElements( int index, final long a[], int offset, int length ) { throw new UnsupportedOperationException(); }
		public void addElements( int index, final long a[] ) { throw new UnsupportedOperationException(); }
		public void addElements( int index, final float a[], int offset, int length ) { throw new UnsupportedOperationException(); }
		public void addElements( int index, final float a[] ) { throw new UnsupportedOperationException(); }
		public void addElements( int index, final double a[], int offset, int length ) { throw new UnsupportedOperationException(); }
		public void addElements( int index, final double a[] ) { throw new UnsupportedOperationException(); }
		public void addElements( int index, final Object a[], int offset, int length ) { throw new UnsupportedOperationException(); }
		public void addElements( int index, final Object a[] ) { throw new UnsupportedOperationException(); }

		public void size( int s )  { throw new UnsupportedOperationException(); }

		public int compareTo( final Object o ) {
			if ( o == this ) return 0;
			return ((List)o).isEmpty() ? 0 : -1;
		}

        private Object readResolve() { return EMPTY_LIST; }

		public Object clone() { return EMPTY_LIST; }
	}


	/** An empty list (immutable). It is serializable and cloneable. 
	 *
	 * <P>The class of this objects represent an abstract empty list
	 * that is a sublist of any type of list. Thus, {@link #EMPTY_LIST}
	 * may be assigned a variable of any (sorted) type-specific list.
	 */

	public static final EmptyList EMPTY_LIST = new EmptyList();
	
}

// Local Variables:
// mode: jde
// tab-width: 4
// End:
