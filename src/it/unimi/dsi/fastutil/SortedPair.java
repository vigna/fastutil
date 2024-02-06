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

import java.util.Objects;

import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutableSortedPair;

/**
 * A pair of sorted elements.
 *
 * <p>
 * This interface strengthen {@link Pair}, without adding methods. It assumes that both elements of
 * the pair are of the same type, and that they are primitive or comparable. It guarantees that the
 * {@linkplain #left() left element} is smaller than or equal to the {@linkplain #right() right
 * element}.
 *
 * <p>
 * Implementations of this class can be used to represent <em>unordered pairs</em> by
 * canonicalization. Note that, in particular, if you build a sorted pair using a left and right
 * element in the wrong order they will be exchanged. To support this usage, this interface provides
 * a {@link #contains(Object)} method that can be used to check whether a given object is equal to
 * either element of the sorted pair.
 *
 * @param <K> the type of the elements.
 */

public interface SortedPair<K extends Comparable<K>> extends Pair<K, K> {

	/**
	 * Returns a new immutable {@link it.unimi.dsi.fastutil.SortedPair SortedPair} with given left
	 * and right value.
	 *
	 * <p>
	 * Note that if {@code left} and {@code right} are in the wrong order, they will be exchanged.
	 *
	 * @param l the left value.
	 * @param r the right value.
	 *
	 * @implNote This factory method delegates to
	 *           {@link ObjectObjectImmutablePair#of(Object, Object)}.
	 */
	public static <K extends Comparable<K>> SortedPair<K> of(final K l, final K r) {
		return ObjectObjectImmutableSortedPair.of(l, r);
	}

	/**
	 * Returns true if one of the two elements of this sorted pair is equal to a given object.
	 *
	 * @param o an object, or {@code null}-
	 * @return true if one of the two elements of this sorted pair is equal to {@code o}.
	 */
	default boolean contains(final Object o) {
		return Objects.equals(o, left()) || Objects.equals(o, right());
	}
}
