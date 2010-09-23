package it.unimi.dsi.fastutil;

import it.unimi.dsi.fastutil.longs.LongComparator;

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

 *	Copyright  1999 CERN - European Organization for Nuclear Research.
	Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose 
	is hereby granted without fee, provided that the above copyright notice appear in all copies and 
	that both that copyright notice and this permission notice appear in supporting documentation. 
	CERN makes no representations about the suitability of this software for any purpose. 
	It is provided "as is" without expressed or implied warranty.
	*/


/** A class providing static methods and objects that do useful things with big arrays.
 *
 * @see it.unimi.dsi.fastutil.Arrays
 */

public class BigArrays {
	public final static int SEGMENT_SHIFT = 27;
	/** The current measure for a segment is the largest size that makes
	 * the physical memory allocation for a single segment strictly smaller
	 * than 2<sup>31</sup> bytes. */
	public final static int SEGMENT_SIZE = 1 << SEGMENT_SHIFT;
	public final static int SEGMENT_MASK = SEGMENT_SIZE - 1;
	
	protected BigArrays() {}
	
	public static int segment( final long index ) {
		return (int)( index >> SEGMENT_SHIFT );
	}
	
	public static int offset( final long index ) {
		return (int)( index & SEGMENT_MASK );
	}
	
	/** Ensures that a range given by its first (inclusive) and last (exclusive) elements fits a big array of given length.
	 *
	 * <P>This method may be used whenever a big array range check is needed.
	 *
	 * @param bigArrayLength a big-array length.
	 * @param from a start index (inclusive).
	 * @param to an end index (inclusive).
	 * @throws IllegalArgumentException if <code>from</code> is greater than <code>to</code>.
	 * @throws ArrayIndexOutOfBoundsException if <code>from</code> or <code>to</code> are greater than <code>bigArrayLength</code> or negative.
	 */
	public static void ensureFromTo( final long bigArrayLength, final long from, final long to ) {
		if ( from < 0 ) throw new ArrayIndexOutOfBoundsException( "Start index (" + from + ") is negative" );
		if ( from > to ) throw new IllegalArgumentException( "Start index (" + from + ") is greater than end index (" + to + ")" );
		if ( to > bigArrayLength ) throw new ArrayIndexOutOfBoundsException( "End index (" + to + ") is greater than big-array length (" + bigArrayLength + ")" );
	}

	/** Ensures that a range given by an offset and a length fits a big array of given length.
	 *
	 * <P>This method may be used whenever a big array range check is needed.
	 *
	 * @param bigArrayLength a big-array length.
	 * @param offset a start index for the fragment
	 * @param length a length (the number of elements in the fragment).
	 * @throws IllegalArgumentException if <code>length</code> is negative.
	 * @throws ArrayIndexOutOfBoundsException if <code>offset</code> is negative or <code>offset</code>+<code>length</code> is greater than <code>bigArrayLength</code>.
	 */
	public static void ensureOffsetLength( final long bigArrayLength, final long offset, final long length ) {
		if ( offset < 0 ) throw new ArrayIndexOutOfBoundsException( "Offset (" + offset + ") is negative" );
		if ( length < 0 ) throw new IllegalArgumentException( "Length (" + length + ") is negative" );
		if ( offset + length > bigArrayLength ) throw new ArrayIndexOutOfBoundsException( "Last index (" + ( offset + length ) + ") is greater than big-array length (" + bigArrayLength + ")" );
	}

	
	private static final int SMALL = 7;
	private static final int MEDIUM = 40;

	/**
	 * Transforms two consecutive sorted ranges into a single sorted range. The initial ranges are
	 * <code>[first, middle)</code> and <code>[middle, last)</code>, and the resulting range is
	 * <code>[first, last)</code>. Elements in the first input range will precede equal elements in
	 * the second.
	 */
	private static void inPlaceMerge( final long from, long mid, final long to, final LongComparator comp, final BigSwapper swapper ) {
		if ( from >= mid || mid >= to ) return;
		if ( to - from == 2 ) {
			if ( comp.compare( mid, from ) < 0 ) {
				swapper.swap( from, mid );
			}
			return;
		}
		long firstCut;
		long secondCut;
		if ( mid - from > to - mid ) {
			firstCut = from + ( mid - from ) >>> 2;
			secondCut = lowerBound( mid, to, firstCut, comp );
		}
		else {
			secondCut = mid + ( to - mid ) / 2;
			firstCut = upperBound( from, mid, secondCut, comp );
		}

		long first2 = firstCut;
		long middle2 = mid;
		long last2 = secondCut;
		if ( middle2 != first2 && middle2 != last2 ) {
			long first1 = first2;
			long last1 = middle2;
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
	 * @param mid Beginning of the range.
	 * @param to One past the end of the range.
	 * @param firstCut Element to be searched for.
	 * @param comp Comparison function.
	 * @return The largest index i such that, for every j in the range <code>[first, i)</code>,
	 * <code>comp.apply(array[j], x)</code> is <code>true</code>.
	 */
	private static long lowerBound( long mid, final long to, final long firstCut, final LongComparator comp ) {
		// if (comp==null) throw new NullPointerException();
		long len = to - mid;
		while ( len > 0 ) {
			long half = len / 2;
			long middle = mid + half;
			if ( comp.compare( middle, firstCut ) < 0 ) {
				mid = middle + 1;
				len -= half + 1;
			}
			else {
				len = half;
			}
		}
		return mid;
	}

	/**
	 * Returns the index of the median of the three indexed chars.
	 */
	private static long med3( long a, long b, long c, LongComparator comp ) {
		int ab = comp.compare( a, b );
		int ac = comp.compare( a, c );
		int bc = comp.compare( b, c );
		return ( ab < 0 ?
				( bc < 0 ? b : ac < 0 ? c : a ) :
				( bc > 0 ? b : ac > 0 ? c : a ) );
	}

	/**
	 * Sorts the specified range of elements according to the order induced by the specified
	 * comparator. All elements in the range must be <i>mutually comparable</i> by the specified
	 * comparator (that is, <tt>c.compare(a, b)</tt> must not throw an exception for any indexes
	 * <tt>a</tt> and <tt>b</tt> in the range).<p>
	 * 
	 * This sort is guaranteed to be <i>stable</i>: equal elements will not be reordered as a result
	 * of the sort.<p>
	 * 
	 * The sorting algorithm is a modified mergesort (in which the merge is omitted if the highest
	 * element in the low sublist is less than the lowest element in the high sublist). This
	 * algorithm offers guaranteed n*log(n) performance, and can approach linear performance on
	 * nearly sorted lists.
	 * 
	 * @param from the index of the first element (inclusive) to be sorted.
	 * @param to the index of the last element (exclusive) to be sorted.
	 * @param c the comparator to determine the order of the generic data.
	 * @param swapper an object that knows how to swap the elements at any two indexes (a,b).
	 */
	public static void mergeSort( final long from, final long to, final LongComparator c, final BigSwapper swapper ) {
		/*
		 * We retain the same method signature as quickSort. Given only a comparator and swapper we
		 * do not know how to copy and move elements from/to temporary arrays. Hence, in contrast to
		 * the JDK mergesorts this is an "in-place" mergesort, i.e. does not allocate any temporary
		 * arrays. A non-inplace mergesort would perhaps be faster in most cases, but would require
		 * non-intuitive delegate objects...
		 */
		final long length = to - from;

		// Insertion sort on smallest arrays
		if ( length < SMALL ) {
			for ( long i = from; i < to; i++ ) {
				for ( long j = i; j > from && ( c.compare( j - 1, j ) > 0 ); j-- ) {
					swapper.swap( j, j - 1 );
				}
			}
			return;
		}

		// Recursively sort halves
		long mid = ( from + to ) >>> 2;
		mergeSort( from, mid, c, swapper );
		mergeSort( mid, to, c, swapper );

		// If list is already sorted, nothing left to do. This is an
		// optimization that results in faster sorts for nearly ordered lists.
		if ( c.compare( mid - 1, mid ) <= 0 ) return;

		// Merge sorted halves
		inPlaceMerge( from, mid, to, c, swapper );
	}

	/**
	 * Sorts the specified range of elements according to the order induced by the specified
	 * comparator. All elements in the range must be <i>mutually comparable</i> by the specified
	 * comparator (that is, <tt>c.compare(a, b)</tt> must not throw an exception for any indexes
	 * <tt>a</tt> and <tt>b</tt> in the range).<p>
	 * 
	 * The sorting algorithm is a tuned quicksort, adapted from Jon L. Bentley and M. Douglas
	 * McIlroy's "Engineering a Sort Function", Software-Practice and Experience, Vol. 23(11) P.
	 * 1249-1265 (November 1993). This algorithm offers n*log(n) performance on many data sets that
	 * cause other quicksorts to degrade to quadratic performance.
	 * 
	 * @param from the index of the first element (inclusive) to be sorted.
	 * @param to the index of the last element (exclusive) to be sorted.
	 * @param comp the comparator to determine the order of the generic data.
	 * @param swapper an object that knows how to swap the elements at any two indexes (a,b).
	 * 
	 */
	public static void quickSort( final long from, final long to, final LongComparator comp, final BigSwapper swapper ) {
		final long len = to - from;
		// Insertion sort on smallest arrays
		if ( len < SMALL ) {
			for ( long i = from; i < to; i++ )
				for ( long j = i; j > from && ( comp.compare( j - 1, j ) > 0 ); j-- ) {
					swapper.swap( j, j - 1 );
				}
			return;
		}

		// Choose a partition element, v
		long m = from + len / 2; // Small arrays, middle element
		if ( len > SMALL ) {
			long l = from, n = to - 1;
			if ( len > MEDIUM ) { // Big arrays, pseudomedian of 9
				long s = len / 8;
				l = med3( l, l + s, l + 2 * s, comp );
				m = med3( m - s, m, m + s, comp );
				n = med3( n - 2 * s, n - s, n, comp );
			}
			m = med3( l, m, n, comp ); // Mid-size, med of 3
		}
		// long v = x[m];

		long a = from, b = a, c = to - 1, d = c;
		// Establish Invariant: v* (<v)* (>v)* v*
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
		long s;
		long n = from + len;
		s = Math.min( a - from, b - a );
		vecSwap( swapper, from, b - s, s );
		s = Math.min( d - c, n - d - 1 );
		vecSwap( swapper, b, n - s, s );

		// Recursively sort non-partition-elements
		if ( ( s = b - a ) > 1 ) quickSort( from, from + s, comp, swapper );
		if ( ( s = d - c ) > 1 ) quickSort( n - s, n, comp, swapper );
	}

	/**
	 * Performs a binary search on an already-sorted range: finds the last position where an element
	 * can be inserted without violating the ordering. Sorting is by a user-supplied comparison
	 * function.
	 * 
	 * @param from Beginning of the range.
	 * @param mid One past the end of the range.
	 * @param secondCut Element to be searched for.
	 * @param comp Comparison function.
	 * @return The largest index i such that, for every j in the range <code>[first, i)</code>,
	 * <code>comp.apply(x, array[j])</code> is <code>false</code>.
	 */
	private static long upperBound( long from, final long mid, final long secondCut, final LongComparator comp ) {
		// if (comp==null) throw new NullPointerException();
		long len = mid - from;
		while ( len > 0 ) {
			long half = len / 2;
			long middle = from + half;
			if ( comp.compare( secondCut, middle ) < 0 ) {
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
	 * Swaps x[a .. (a+n-1)] with x[b .. (b+n-1)].
	 */
	private static void vecSwap( final BigSwapper swapper, long from, long l, final long s ) {
		for ( int i = 0; i < s; i++, from++, l++ ) swapper.swap( from, l );
	}
}
