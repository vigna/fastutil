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

package it.unimi.dsi.fastutil;

/** A function mapping keys into values.
 * 
 * <p>Instances of this class represent functions: the main difference with {@link java.util.Map}
 * is that functions do not in principle allow enumeration of their domain or range. The need for
 * this interface lies in the existence of several highly optimized implementations of 
 * functions (e.g., minimal perfect hashes) which do not actually store their domain or range explicitly.
 * In case the domain is known, {@link #containsKey(Object)} can be used to perform membership queries. 
 *
 * <p>The choice of naming all methods exactly as in {@link java.util.Map} makes it possible
 * for all type-specific maps to extend type-specific functions (e.g., {@link it.unimi.dsi.fastutil.ints.Int2IntMap}
 * extends {@link it.unimi.dsi.fastutil.ints.Int2IntFunction}). However, {@link #size()} is allowed to return -1 to denote
 * that the number of keys is not available (e.g., in the case of a string hash function).
 * 
 * <p>Note that there is an {@link it.unimi.dsi.fastutil.objects.Object2ObjectFunction} that
 * can also set its default return value.
 * 
 * <p><strong>Warning</strong>: Equality of functions is <em>not specified</em>
 * by contract, and it will usually be <em>by reference</em>, as there is no way to enumerate the keys
 * and establish whether two functions represent the same mathematical entity.
 *
 * @see java.util.Map
 */

public interface Function<K,V> {

	/** Associates the specified value with the specified key in this function (optional operation).
	 *
	 * @param key the key.
	 * @param value the value.
	 * @return the old value, or <code>null</code> if no value was present for the given key.
	 * @see java.util.Map#put(Object,Object)
	 */

	V put( K key, V value );

	/** Returns the value associated by this function to the specified key. 
	 *
	 * @param key the key.
	 * @return the corresponding value, or <code>null</code> if no value was present for the given key.
	 * @see java.util.Map#get(Object)
	 */

	V get( Object key );

	/** Returns true if this function contains a mapping for the specified key. 
	 *
	 * <p>Note that for some kind of functions (e.g., hashes) this method
	 * will always return true.
	 *
	 * @param key the key.
	 * @return true if this function associates a value to <code>key</code>.
	 * @see java.util.Map#containsKey(Object)
	 */

	 boolean containsKey( Object key );

	/** Removes this key and the associated value from this function if it is present (optional operation).
	 *  
	 * @param key
	 * @return the old value, or <code>null</code> if no value was present for the given key.
	 * @see java.util.Map#remove(Object)
	 */
	
	V remove( Object key );

	/** Returns the intended number of keys in this function, or -1 if no such number exists.
	 * 
	 * <p>Most function implementations will have some knowledge of the intended number of keys
	 * in their domain. In some cases, however, this might not be possible.
	 * 
	 *  @return the intended number of keys in this function, or -1 if that number is not available.
	 */
    int size();

	/** Removes all associations from this function (optional operation).
	 *  
	 * @see java.util.Map#clear()
	 */
	
	void clear();

}
