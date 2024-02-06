/*
 * Copyright (C) 2020-2024 Sebastiano Vigna
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

import java.util.Comparator;

import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;

/**
 * A pair of elements.
 *
 * <p>
 * This inferface gives access to a pair of elements &lt;<var>l</var>, <var>r</var>&gt;, where
 * <var>l</var> is the {@linkplain #left() <em>left element</em>} and <var>r</var> is the
 * {@linkplain #right() <em>right element</em>}. Mutability is optional.
 *
 * <p>
 * Since pairs have many different interpretation depending on the context, this interface offers
 * alternative but equivalent access methods based on {@linkplain #first()
 * first}/{@linkplain #second() second} and {@linkplain #key() key}/{@linkplain #value() value}. All
 * such methods have default implementations that delegates to the standard methods. Implementations
 * need only to provide {@link #left()} and {@link #right()}, and possibly {@link #left(Object)} and
 * {@link #right(Object)} for mutability.
 *
 * <p>
 * Setters return the instance, and are thus chainable. You can write
 *
 * <pre>
 * pair.left(0).right(1)
 * </pre>
 *
 * and, if necessary, pass this value to a method.
 *
 * @param <L> the type of the left element.
 * @param <R> the type of the right element.
 */

public interface Pair<L, R> {

	/**
	 * Returns the left element of this pair.
	 *
	 * @return the left element of this pair.
	 */
	public L left();

	/**
	 * Returns the right element of this pair.
	 *
	 * @return the right element of this pair.
	 */
	public R right();

	/**
	 * Sets the left element of this pair (optional operation).
	 *
	 * @param l a new value for the left element.
	 *
	 * @implNote This implementation throws an {@link UnsupportedOperationException}.
	 */
	public default Pair<L, R> left(final L l) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets the right element of this pair (optional operation).
	 *
	 * @param r a new value for the right element.
	 *
	 * @implNote This implementation throws an {@link UnsupportedOperationException}.
	 */
	public default Pair<L, R> right(final R r) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the left element of this pair.
	 *
	 * @return the left element of this pair.
	 *
	 * @implNote This implementation delegates to {@link #left()}.
	 *
	 */
	public default L first() {
		return left();
	}

	/**
	 * Returns the right element of this pair.
	 *
	 * @return the right element of this pair.
	 *
	 * @implNote This implementation delegates to {@link #right()}.
	 *
	 */
	public default R second() {
		return right();
	}

	/**
	 * Sets the left element of this pair (optional operation).
	 *
	 * @param l a new value for the left element.
	 *
	 * @implNote This implementation delegates to {@link #left(Object)}.
	 */
	public default Pair<L, R> first(final L l) {
		return left(l);
	}

	/**
	 * Sets the right element of this pair (optional operation).
	 *
	 * @param r a new value for the right element.
	 *
	 * @implNote This implementation delegates to {@link #right(Object)}.
	 */
	public default Pair<L, R> second(final R r) {
		return right(r);
	}

	/**
	 * Sets the left element of this pair (optional operation).
	 *
	 * @param l a new value for the left element.
	 *
	 * @implNote This implementation delegates to {@link #left(Object)}.
	 */
	public default Pair<L, R> key(final L l) {
		return left(l);
	}

	/**
	 * Sets the right element of this pair (optional operation).
	 *
	 * @param r a new value for the right element.
	 *
	 * @implNote This implementation delegates to {@link #right(Object)}.
	 */
	public default Pair<L, R> value(final R r) {
		return right(r);
	}

	/**
	 * Returns the left element of this pair.
	 *
	 * @return the left element of this pair.
	 *
	 * @implNote This implementation delegates to {@link #left()}.
	 *
	 */
	public default L key() {
		return left();
	}

	/**
	 * Returns the right element of this pair.
	 *
	 * @return the right element of this pair.
	 *
	 * @implNote This implementation delegates to {@link #right()}.
	 *
	 */
	public default R value() {
		return right();
	}

	/**
	 * Returns a new immutable {@link it.unimi.dsi.fastutil.Pair Pair} with given left and right
	 * value.
	 *
	 * @param l the left value.
	 * @param r the right value.
	 *
	 * @implNote This factory method returns an instance of {@link ObjectObjectImmutablePair}.
	 */
	public static <L, R> Pair<L, R> of(final L l, final R r) {
		return new ObjectObjectImmutablePair<>(l, r);
	}

	/**
	 * Returns a lexicographical comparator for pairs.
	 *
	 * <p>
	 * The comparator returned by this method implements lexicographical order. It compares first
	 * the left elements: if the result of the comparison is nonzero, it returns said result.
	 * Otherwise, this comparator returns the result of the comparison of the right elements.
	 *
	 * @return a lexicographical comparator for pairs.
	 */
	@SuppressWarnings("unchecked")
	public static <L, R> Comparator<Pair<L, R>> lexComparator() {
		return (x, y) -> {
			final int t = ((Comparable<L>)x.left()).compareTo(y.left());
			if (t != 0) return t;
			return ((Comparable<R>)x.right()).compareTo(y.right());
		};
	}
}
