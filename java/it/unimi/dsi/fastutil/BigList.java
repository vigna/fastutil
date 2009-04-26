package it.unimi.dsi.fastutil;

/*		 
 * fastutil: Fast & compact type-specific collections for Java
 *
 * Copyright (C) 2009 Sebastiano Vigna 
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
import java.util.List;

/** A list with big (i.e., long) indices.
 *
 * <P>Instances of this class implement the same semantics as that of {@link List}: however,
 * additional methods provide access to long indices, and returned iterators are actually
 * of type {@link BigListIterator}.
 */

public interface BigList<K> extends List<K> {

	/** Returns the element at the specified position.
	 * 
	 * @param index a position in the list.
	 * @return the element at the specified position.
	 * @see List#get(int)
	 */
	public K get( long index );

	/** Removes the element at the specified position.
	 * 
	 * @param index a position in the list.
	 * @return the element previously at the specified position.
	 * @see List#remove(int)
	 */
	public K remove( long index );

	/** Replaces the element at the specified position in this list with the specified element (optional operation). 
	 * 
	 * @param index a position in the list.
	 * @param element the element to be stored at the specified position.
	 * @return the element previously at the specified positions.
	 * @see List#set(int,Object)
	 */
	public K set( long index, K element );

	/** Inserts the specified element at the specified position in this list (optional operation).
	 * 
	 * @param index a position in the list.
	 * @param element an element to be inserted.
	 * @see List#add(int,Object)
	 */
	public void add( long index, K element );
	
	/** Returns the number of elements in this big list.
	 * 
	 * @return the number of elements in this big list.
	 * @see List#size()
	 */
	public long bigSize();
	
	/** Returns the number of elements in this big list.
	 * 
	 * @return the number of elements in this big list.
	 * @see List#size()
	 * @throws IllegalStateException if called when this big list contains
	 * more than {@link Integer#MAX_VALUE} elements.
	 * @deprecated Use {@link #bigSize()} to avoid throwing an {@link IllegalStateException}. 
	 */
	@Override
	@Deprecated
	public int size();

	
	/** Inserts all of the elements in the specified collection into this list at the specified position (optional operation).
	 * 
	 * @param index index at which to insert the first element from the specified collection.
	 * @param c collection containing elements to be added to this list.
	 * @return <code>true</code> if this list changed as a result of the call
	 * @see List#addAll(int, Collection) 
	 */	
	public boolean addAll(int index, Collection<? extends K> c);

	/** Returns the index of the first occurrence of the specified element in this list, or -1 if this list does not contain the element.
	 * 
	 * @param o the object to search for.
	 * @return the index of the first occurrence of the specified element in this list, or -1 if this list does not contain the element.
	 * @see List#indexOf(Object)
	 */
	public long bigIndexOf( Object o );
	
	/** Returns the index of the first occurrence of the specified element in this list, or -1 if this list does not contain the element.
	 * 
	 * @param o the object to search for.
	 * @return the index of the first occurrence of the specified element in this list, or -1 if this list does not contain the element.
	 * @see List#indexOf(Object)
	 * @throws IllegalStateException if the result is larger than {@link Integer#MAX_VALUE}.
	 * @deprecated Use {@link #bigIndexOf(Object)} to avoid throwing an {@link IllegalStateException}.
	 */
	@Override
	@Deprecated
	public int indexOf( Object o );
	
	/** Returns the index of the last occurrence of the specified element in this list, or -1 if this list does not contain the element. 
	 * 
	 * @param o the object to search for.
	 * @return the index of the last occurrence of the specified element in this list, or -1 if this list does not contain the element.
 	 * @see List#lastIndexOf(Object)
	 */
	public long lastBigIndexOf( Object o );
	
	/** Returns the index of the last occurrence of the specified element in this list, or -1 if this list does not contain the element. 
	 * 
	 * @param o the object to search for.
	 * @see List#lastIndexOf(Object)
	 * @return the index of the last occurrence of the specified element in this list, or -1 if this list does not contain the element.
	 * @throws IllegalStateException if the result is larger than {@link Integer#MAX_VALUE}.
	 * @deprecated Use {@link #lastBigIndexOf(Object)} to avoid throwing an {@link IllegalStateException}.
	 */
	@Override
	@Deprecated
	public int lastIndexOf( Object o );
	
	/** Returns a list iterator over the elements in this list.
	 * 
	 * @return a list iterator over the elements in this list.
	 * @see List#listIterator()
	 */

	public BigListIterator<K> listIterator();

	/** Returns a list iterator of the elements in this list, starting at the specified position in this list.
	 * 
	 * @param index index of first element to be returned from the list iterator.
	 * @return a list iterator of the elements in this list, starting at the specified position in
	 * this list.
	 * @see List#listIterator(int)
	 */
	public BigListIterator<K> listIterator( long index );

	/** Returns a list iterator of the elements in this list, starting at the specified position in this list.
	 * 
	 * @param index index of first element to be returned from the list iterator.
	 * @return a list iterator of the elements in this list, starting at the specified position in
	 * this list.
	 * @see List#listIterator(int)
	 */
	@Override
	public BigListIterator<K> listIterator( int index );

	/** Returns a big sublist view of this big list.
	 * 
	 * @param from the starting element (inclusive).
	 * @param to the ending element (exclusive).
	 * @return a big sublist view of this big list.
	 * @see List#subList(int, int)
	 */
	public BigList<K> subList( long from, long to );
}
