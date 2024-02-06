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

public interface BidirectionalIterator<K> extends Iterator<K> {

	/** Returns the previous element from the collection.
	 *
	 * @return the previous element from the collection.
	 * @see java.util.ListIterator#previous()
	 */

	K previous();

	/** Returns whether there is a previous element.
	 *
	 * @return whether there is a previous element.
	 * @see java.util.ListIterator#hasPrevious()
	 */

	boolean hasPrevious();
}
