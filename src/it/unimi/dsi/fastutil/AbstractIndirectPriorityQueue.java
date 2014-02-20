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

/** An abstract class providing basic methods for implementing the {@link IndirectPriorityQueue} interface.
 *
 * <P>This class defines {@link #changed(int)}, {@link #allChanged()},  {@link #remove(int)} and {@link #last()} as throwing an
 * {@link UnsupportedOperationException}.
 */

public abstract class AbstractIndirectPriorityQueue<K> implements IndirectPriorityQueue<K> {

	public int last() { throw new UnsupportedOperationException(); }

	public void changed() { changed( first() ); }
	
	public void changed( int index ) { throw new UnsupportedOperationException(); }

	public void allChanged() { throw new UnsupportedOperationException(); }

	public boolean remove( int index ) { throw new UnsupportedOperationException(); }

	public boolean contains( int index ) { throw new UnsupportedOperationException(); }

	public boolean isEmpty() { return size() == 0; }

}
