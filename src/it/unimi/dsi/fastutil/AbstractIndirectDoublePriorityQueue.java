package it.unimi.dsi.fastutil;

/*
 * Copyright (C) 2003-2017 Paolo Boldi and Sebastiano Vigna
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

/** An abstract class providing basic methods for implementing the {@link IndirectDoublePriorityQueue} interface.
 *
 * <P>This class defines  {@link #secondaryLast()} as throwing an
 * {@link UnsupportedOperationException}.
 * @deprecated this class will be removed in release 8.
 */

@Deprecated
public abstract class AbstractIndirectDoublePriorityQueue<K> extends AbstractIndirectPriorityQueue<K> implements IndirectDoublePriorityQueue<K> {

	public int secondaryLast() { throw new UnsupportedOperationException(); }

}
