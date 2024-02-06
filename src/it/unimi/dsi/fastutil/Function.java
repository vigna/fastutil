/*
 * Copyright (C) 2002-2024 Sebastiano Vigna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.unimi.dsi.fastutil;

import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;

import it.unimi.dsi.fastutil.bytes.Byte2CharFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;

/** A function mapping keys into values.
 *
 * <p>Instances of this class represent functions: the main difference with {@link java.util.Map}
 * is that functions do not in principle allow enumeration of their domain or range. The need for
 * this interface lies in the existence of several highly optimized implementations of
 * functions (e.g., minimal perfect hashes) which do not actually store their domain or range explicitly.
 * In case the domain is known, {@link #containsKey(Object)} can be used to perform membership queries.
 *
 * <p>The choice of naming all methods exactly as in {@link java.util.Map} makes it possible
 * for all type-specific maps to extend type-specific functions (e.g., {@link it.unimi.dsi.fastutil.ints.Int2IntMap Int2IntMap}
 * extends {@link it.unimi.dsi.fastutil.ints.Int2IntFunction Int2IntFunction}). However, {@link #size()} is allowed to return -1 to denote
 * that the number of keys is not available (e.g., in the case of a string hash function).
 *
 * <p>Note that there is an {@link it.unimi.dsi.fastutil.objects.Object2ObjectFunction Object2ObjectFunction} that
 * can also set its default return value.
 *
 * <h2>Relationship with {@link java.util.function.Function}</h2>
 *
 * <p>This interface predates Java 8's {@link java.util.function.Function} and it was conceived with
 * a different purpose. To ease interoperability, we extend {@link java.util.function.Function} and
 * implement a default method for {@link #apply(Object)} that delegates to {@link #get(Object)}. However,
 * while the argument of a {@link java.util.function.Function} with keys of type {@code T} is of type
 * {@code T}, the argument of {@link #get(Object)} is unparameterized (see the example below).
 *
 * <p>No attempt will be made at creating type-specific versions of {@link java.util.function.Function} as
 * the JDK already provides several specializations, such as {@link IntToLongFunction}.
 * Rather, type-specific versions of this class do implement the corresponding classes in {@link java.util.function}:
 * for example, {@link Int2LongFunction} extends {@link IntToLongFunction} and {@link Int2IntFunction} extends
 * {@link IntUnaryOperator}. For functions that do not have a corresponding JDK function we extend the
 * closest possible function (widening input and output types): for example, {@link Byte2CharFunction} extends
 * {@link IntUnaryOperator}.
 *
 * <h2>Default methods and lambda expressions</h2>
 *
 * <p>All optional operations have default methods throwing an {@link UnsupportedOperationException}, except
 * for {@link #containsKey(Object)}, which returns true, and {@link #size()}, which return -1.
 * Thus, it is possible to define an instance of this class using a lambda expression that will specify
 * {@link #get(Object)}. Note that the type signature of {@link #get(Object)} might lead to slightly
 * counter-intuitive behaviour. For example, to define the identity function on {@link Integer} objects
 * you need to write
 * <pre>
 *     it.unimi.dsi.fastutil.Function&lt;Integer, Integer&gt; f = (x) -&gt; (Integer)x;
 * </pre>
 * as the argument to {@link #get(Object)} is unparameterized.
 *
 * <p><strong>Warning</strong>: Equality of functions is <em>not specified</em>
 * by contract, and it will usually be <em>by reference</em>, as there is no way to enumerate the keys
 * and establish whether two functions represent the same mathematical entity.
 *
 * @see java.util.Map
 * @see java.util.function.Function
 */

@FunctionalInterface
public interface Function<K,V> extends java.util.function.Function<K,V> {

	/** {@inheritDoc} This is equivalent to calling {@link #get(Object)}.
	 *
	 * @param key {@inheritDoc}
	 * @return {@inheritDoc}
	 * @see java.util.function.Function#apply(Object)
	 * @see #get(Object)
	 * @since 8.0.0
	 */

	@Override
	default V apply(final K key) {
		return get(key);
	}

	/** Associates the specified value with the specified key in this function (optional operation).
	 *
	 * @param key the key.
	 * @param value the value.
	 * @return the old value, or {@code null} if no value was present for the given key.
	 * @see java.util.Map#put(Object,Object)
	 */

	default V put(final K key, final V value) {
		throw new UnsupportedOperationException();
	}

	/** Returns the value associated by this function to the specified key.
	 *
	 * @param key the key.
	 * @return the corresponding value, or {@code null} if no value was present for the given key.
	 * @see java.util.Map#get(Object)
	 */

	V get(Object key);

	/**
	 * Returns the value associated by this function to the specified key, or give the specified
	 * value if not present.
	 *
	 * @param key the key.
	 * @param defaultValue the default value to return if not present.
	 * @return the corresponding value, or {@code defaultValue} if no value was present for the
	 *         given key.
	 * @see java.util.Map#getOrDefault(Object, Object)
	 * @since 8.5.0
	 */
	default V getOrDefault(final Object key, final V defaultValue) {
		final V value = get(key);
		return (value != null || containsKey(key)) ? value : defaultValue;
	}


	/** Returns true if this function contains a mapping for the specified key.
	 *
	 * <p>Note that for some kind of functions (e.g., hashes) this method
	 * will always return true. This default implementation, in particular,
	 * always return true.
	 *
	 * @param key the key.
	 * @return true if this function associates a value to {@code key}.
	 * @see java.util.Map#containsKey(Object)
	 */

	default boolean containsKey(final Object key) {
		return true;
	}

	/** Removes this key and the associated value from this function if it is present (optional operation).
	 *
	 * @param key the key.
	 * @return the old value, or {@code null} if no value was present for the given key.
	 * @see java.util.Map#remove(Object)
	 */

	default V remove(final Object key) {
		throw new UnsupportedOperationException();
	}

	/** Returns the intended number of keys in this function, or -1 if no such number exists.
	 *
	 * <p>Most function implementations will have some knowledge of the intended number of keys
	 * in their domain. In some cases, however, this might not be possible. This default
	 * implementation, in particular, returns -1.
	 *
	 *  @return the intended number of keys in this function, or -1 if that number is not available.
	 */

	default int size() {
		return -1;
	}

	/** Removes all associations from this function (optional operation).
	 *
	 * @see java.util.Map#clear()
	 */

	default void clear() {
		throw new UnsupportedOperationException();
	}
}
