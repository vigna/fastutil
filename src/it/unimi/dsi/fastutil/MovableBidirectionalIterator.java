/*
 * Copyright (C) 2002-2023 Sebastiano Vigna
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

import java.util.Iterator;
import java.util.ListIterator;

/** A bidirectional {@link Iterator}.
 *
 * <p>This kind of iterator is essentially a {@link ListIterator} that
 * does not support {@link ListIterator#previousIndex()} and {@link
 * ListIterator#nextIndex()}. It is useful for those maps that can easily
 * provide bidirectional iteration, but provide no index.
 *
 * <p>Note that iterators returned by {@code fastutil} classes are more
 * specific, and support skipping. This class serves the purpose of organising
 * in a cleaner way the relationships between various iterators.
 *
 * @see Iterator
 * @see ListIterator
 */

public interface MovableBidirectionalIterator<K> extends BidirectionalIterator<K> {

    /** Moves back or forward to the elements in the collection,
     * starting from a given element of the domain.
     *
     * <p>This method moves an iterator to the specified starting point.
     * The starting point is any element comparable to the elements of the collection
     * (even if it does not actually belong to the collection).
     * The next element of the returned iterator is the least element of
     * the collection that is greater than the starting point (if there are no
     * elements greater than the starting point, {@link
     * it.unimi.dsi.fastutil.BidirectionalIterator#hasNext() hasNext()} will return
     * {@code false}). The previous element of the returned iterator is
     * the greatest element of the collection that is smaller than or equal to the
     * starting point (if there are no elements smaller than or equal to the
     * starting point, {@link it.unimi.dsi.fastutil.BidirectionalIterator#hasPrevious()
     * hasPrevious()} will return {@code false}).
     *
     * @param fromElement an element to start from.
     * @throws UnsupportedOperationException if the collection does not support move at iterator.
     */
    void move(final K fromElement);

    /** Moves iterator to the first element of the collection.
     *
     * @throws UnsupportedOperationException if the collection does not support move at iterator.
     */
    void begin();

    /** Moves iterator to the last element of the collection.
     *
     * @throws UnsupportedOperationException if the collection does not support move at iterator.
     */
    void end();
}
