package it.unimi.dsi.fastutil;

/*		 
 * Copyright (C) 2003-2014 Paolo Boldi and Sebastiano Vigna 
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

import it.unimi.dsi.fastutil.PriorityQueue;

/**  An abstract class providing basic methods for implementing the {@link PriorityQueue} interface. 
 *
 * <P>This class defines {@link #changed()} and {@link #last()} as throwing an
 * {@link UnsupportedOperationException}.
 */

public abstract class AbstractPriorityQueue<K> implements PriorityQueue<K> {

	public void changed() { throw new UnsupportedOperationException(); }

	public K last() { throw new UnsupportedOperationException(); }

	public boolean isEmpty() { return size() == 0; }

}
