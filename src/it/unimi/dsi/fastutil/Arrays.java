package it.unimi.dsi.fastutil;

/*		 
 * Copyright (C) 2002-2014 Sebastiano Vigna 
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

import java.util.ArrayList;

import it.unimi.dsi.fastutil.ints.IntComparator;

/** A class providing static methods and objects that do useful things with arrays.
 *
 * <p>In addition to commodity methods, this class contains {@link Swapper}-based implementations
 * of {@linkplain #quickSort(int, int, IntComparator, Swapper) quicksort} and of
 * a stable, in-place {@linkplain #mergeSort(int, int, IntComparator, Swapper) mergesort}. These
 * generic sorting methods can be used to sort any kind of list, but they find their natural
 * usage, for instance, in sorting arrays in parallel.
 *
 * @see Arrays
 */

public class Arrays {
	
	private Arrays() {}

	/** This is a safe value used by {@link ArrayList} (as of Java 7) to avoid
	 *  throwing {@link OutOfMemoryError} on some JVMs. We adopt the same value. */
    public static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /** Ensures that a range given by its first (inclusive) and last (exclusive) elements fits an array of given length.
	 *
	 * <P>This method may be used whenever an array range check is needed.
	 *
	 * @param arrayLength an array length.
	 * @param from a start index (inclusive).
	 * @param to an end index (inclusive).
	 * @throws IllegalArgumentException if <code>from</code> is greater than <code>to</code>.
	 * @throws ArrayIndexOutOfBoundsException if <code>from</code> or <code>to</code> are greater than <code>arrayLength</code> or negative.
	 */
	public static void ensureFromTo( final int arrayLength, final int from, final int to ) {
		if ( from < 0 ) throw new ArrayIndexOutOfBoundsException( "Start index (" + from + ") is negative" );
		if ( from > to ) throw new IllegalArgumentException( "Start index (" + from + ") is greater than end index (" + to + ")" );
		if ( to > arrayLength ) throw new ArrayIndexOutOfBoundsException( "End index (" + to + ") is greater than array length (" + arrayLength + ")" );
	}

	/** Ensures that a range given by an offset and a length fits an array of given length.
	 *
	 * <P>This method may be used whenever an array range check is needed.
	 *
	 * @param arrayLength an array length.
	 * @param offset a start index for the fragment
	 * @param length a length (the number of elements in the fragment).
	 * @throws IllegalArgumentException if <code>length</code> is negative.
	 * @throws ArrayIndexOutOfBoundsException if <code>offset</code> is negative or <code>offset</code>+<code>length</code> is greater than <code>arrayLength</code>.
	 */
	public static void ensureOffsetLength( final int arrayLength, final int offset, final int length ) {
		if ( offset < 0 ) throw new ArrayIndexOutOfBoundsException( "Offset (" + offset + ") is negative" );
		if ( length < 0 ) throw new IllegalArgumentException( "Length (" + length + ") is negative" );
		if ( offset + length > arrayLength ) throw new ArrayIndexOutOfBoundsException( "Last index (" + ( offset + length ) + ") is greater than array length (" + arrayLength + ")" );
	}

	private static final int SMALL = 7;
	private static final int MEDIUM = 40;

	/**
	 * Transforms two consecutive sorted ranges into a single sorted range. The initial ranges are
	 * <code>[first..middle)</code> and <code>[middle..last)</code>, and the resulting range is
	 * <code>[first..last)</code>. Elements in the first input range will precede equal elements in
	 * the second.
	 */
	private static void inPlaceMerge( final int from, int mid, final int to, final IntComparator comp, final Swapper swapper ) {
		if ( from >= mid || mid >= to ) return;
		if ( to - from == 2 ) {
			if ( comp.compare( mid, from ) < 0 ) swapper.swap( from, mid );
			return;
		}

		int firstCut;
		int secondCut;

		if ( mid - from > to - mid ) {
			firstCut = from + ( mid - from ) / 2;
			secondCut = lowerBound( mid, to, firstCut, comp );
		}
		else {
			secondCut = mid + ( to - mid ) / 2;
			firstCut = upperBound( from, mid, secondCut, comp );
		}

		int first2 = firstCut;
		int middle2 = mid;
		int last2 = secondCut;
		if ( middle2 != first2 && middle2 != last2 ) {
			int first1 = first2;
			int last1 = middle2;
			while ( first1 < --last1 )
				swapper.swap( first1++, last1 );
			first1 = middle2;
			last1 = last2;
			while ( first1 < --last1 )
				swapper.swap( first1++, last1 );
			first1 = first2;
			last1 = last2;
			while ( first1 < --last1 )
				swapper.swap( first1++, last1 );
		}

		mid = firstCut + ( secondCut - mid );
		inPlaceMerge( from, firstCut, mid, comp, swapper );
		inPlaceMerge( mid, secondCut, to, comp, swapper );
	}

	/**
	 * Performs a binary search on an already-sorted range: finds the first position where an
	 * element can be inserted without violating the ordering. Sorting is by a user-supplied
	 * comparison function.
	 * 
	 * @param from the index of the first element (inclusive) to be included in the binary search.
	 * @param to the index of the last element (exclusive) to be included in the binary search.
	 * @param pos the position of the element to be searched for.
	 * @param comp the comparison function.
	 * @return the largest index i such that, for every j in the range <code>[first..i)</code>,
	 * <code>comp.compare(j, pos)</code> is <code>true</code>.
	 */
	private static int lowerBound( int from, final int to, final int pos, final IntComparator comp ) {
		// if (comp==null) throw new NullPointerException();
		int len = to - from;
		while ( len > 0 ) {
			int half = len / 2;
			int middle = from + half;
			if ( comp.compare( middle, pos ) < 0 ) {
				from = middle + 1;
				len -= half + 1;
			}
			else {
				len = half;
			}
		}
		return from;
	}


	/**
	 * Performs a binary search on an already sorted range: finds the last position where an element
	 * can be inserted without violating the ordering. Sorting is by a user-supplied comparison
	 * function.
	 * 
	 * @param from the index of the first element (inclusive) to be included in the binary search.
	 * @param to the index of the last element (exclusive) to be included in the binary search.
	 * @param pos the position of the element to be searched for.
	 * @param comp the comparison function.
	 * @return The largest index i such that, for every j in the range <code>[first..i)</code>,
	 * <code>comp.compare(pos, j)</code> is <code>false</code>.
	 */
	private static int upperBound( int from, final int mid, final int pos, final IntComparator comp ) {
		// if (comp==null) throw new NullPointerException();
		int len = mid - from;
		while ( len > 0 ) {
			int half = len / 2;
			int middle = from + half;
			if ( comp.compare( pos, middle ) < 0 ) {
				len = half;
			}
			else {
				from = middle + 1;
				len -= half + 1;
			}
		}
		return from;
	}

	/**
	 * Returns the index of the median of the three indexed chars.
	 */
	private static int med3( final int a, final int b, final int c, final IntComparator comp ) {
		int ab = comp.compare( a, b );
		int ac = comp.compare( a, c );
		int bc = comp.compare( b, c );
		return ( ab < 0 ?
				( bc < 0 ? b : ac < 0 ? c : a ) :
				( bc > 0 ? b : ac > 0 ? c : a ) );
	}

	/** Sorts the specified range of elements using the specified swapper and according to the order induced by the specified
	 * comparator using mergesort.
	 * 
	 * <p>This sort is guaranteed to be <i>stable</i>: equal elements will not be reordered as a result
	 * of the sort. The sorting algorithm is an in-place mergesort that is significantly slower than a 
	 * standard mergesort, as its running time is <i>O</i>(<var>n</var>&nbsp;(log&nbsp;<var>n</var>)<sup>2</sup>), but it does not allocate additional memory; as a result, it can be
	 * used as a generic sorting algorithm.
	 * 
	 * @param from the index of the first element (inclusive) to be sorted.
	 * @param to the index of the last element (exclusive) to be sorted.
	 * @param c the comparator to determine the order of the generic data (arguments are positions).
	 * @param swapper an object that knows how to swap the elements at any two positions.
	 */
	public static void mergeSort( final int from, final int to, final IntComparator c, final Swapper swapper ) {
		/*
		 * We retain the same method signature as quickSort. Given only a comparator and swapper we
		 * do not know how to copy and move elements from/to temporary arrays. Hence, in contrast to
		 * the JDK mergesorts this is an "in-place" mergesort, i.e. does not allocate any temporary
		 * arrays. A non-inplace mergesort would perhaps be faster in most cases, but would require
		 * non-intuitive delegate objects...
		 */
		final int length = to - from;

		// Insertion sort on smallest arrays
		if ( length < SMALL ) {
			for ( int i = from; i < to; i++ ) {
				for ( int j = i; j > from && ( c.compare( j - 1, j ) > 0 ); j-- ) {
					swapper.swap( j, j - 1 );
				}
			}
			return;
		}

		// Recursively sort halves
		int mid = ( from + to ) >>> 1;
		mergeSort( from, mid, c, swapper );
		mergeSort( mid, to, c, swapper );

		// If list is already sorted, nothing left to do. This is an
		// optimization that results in faster sorts for nearly ordered lists.
		if ( c.compare( mid - 1, mid ) <= 0 ) return;

		// Merge sorted halves
		inPlaceMerge( from, mid, to, c, swapper );
	}

	/** Sorts the specified range of elements using the specified swapper and according to the order induced by the specified
	 * comparator using quicksort. 
	 * 
	 * <p>The sorting algorithm is a tuned quicksort adapted from Jon L. Bentley and M. Douglas
	 * McIlroy, &ldquo;Engineering a Sort Function&rdquo;, <i>Software: Practice and Experience</i>, 23(11), pages
	 * 1249&minus;1265, 1993.
	 * 
	 * @param from the index of the first element (inclusive) to be sorted.
	 * @param to the index of the last element (exclusive) to be sorted.
	 * @param comp the comparator to determine the order of the generic data.
	 * @param swapper an object that knows how to swap the elements at any two positions.
	 * 
	 */
	public static void quickSort( final int from, final int to, final IntComparator comp, final Swapper swapper ) {
		final int len = to - from;
		// Insertion sort on smallest arrays
		if ( len < SMALL ) {
			for ( int i = from; i < to; i++ )
				for ( int j = i; j > from && ( comp.compare( j - 1, j ) > 0 ); j-- ) {
					swapper.swap( j, j - 1 );
				}
			return;
		}

		// Choose a partition element, v
		int m = from + len / 2; // Small arrays, middle element
		if ( len > SMALL ) {
			int l = from;
			int n = to - 1;
			if ( len > MEDIUM ) { // Big arrays, pseudomedian of 9
				int s = len / 8;
				l = med3( l, l + s, l + 2 * s, comp );
				m = med3( m - s, m, m + s, comp );
				n = med3( n - 2 * s, n - s, n, comp );
			}
			m = med3( l, m, n, comp ); // Mid-size, med of 3
		}
		// int v = x[m];

		int a = from;
		int b = a;
		int c = to - 1;
		// Establish Invariant: v* (<v)* (>v)* v*
		int d = c;
		while ( true ) {
			int comparison;
			while ( b <= c && ( ( comparison = comp.compare( b, m ) ) <= 0 ) ) {
				if ( comparison == 0 ) {
					if ( a == m ) m = b; // moving target; DELTA to JDK !!!
					else if ( b == m ) m = a; // moving target; DELTA to JDK !!!
					swapper.swap( a++, b );
				}
				b++;
			}
			while ( c >= b && ( ( comparison = comp.compare( c, m ) ) >= 0 ) ) {
				if ( comparison == 0 ) {
					if ( c == m ) m = d; // moving target; DELTA to JDK !!!
					else if ( d == m ) m = c; // moving target; DELTA to JDK !!!
					swapper.swap( c, d-- );
				}
				c--;
			}
			if ( b > c ) break;
			if ( b == m ) m = d; // moving target; DELTA to JDK !!!
			else if ( c == m ) m = c; // moving target; DELTA to JDK !!!
			swapper.swap( b++, c-- );
		}

		// Swap partition elements back to middle
		int s;
		int n = to;
		s = Math.min( a - from, b - a );
		vecSwap( swapper, from, b - s, s );
		s = Math.min( d - c, n - d - 1 );
		vecSwap( swapper, b, n - s, s );

		// Recursively sort non-partition-elements
		if ( ( s = b - a ) > 1 ) quickSort( from, from + s, comp, swapper );
		if ( ( s = d - c ) > 1 ) quickSort( n - s, n, comp, swapper );
	}


	/**
	 * Swaps x[a .. (a+n-1)] with x[b .. (b+n-1)].
	 */
	private static void vecSwap( final Swapper swapper, int from, int l, final int s ) {
		for ( int i = 0; i < s; i++, from++, l++ ) swapper.swap( from, l );
	}
}
