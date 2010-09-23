package it.unimi.dsi.fastutil;

/** An object that can swap elements specified by longs. 
 * 
 * @see BigArrays#quickSort(long, long, it.unimi.dsi.fastutil.longs.LongComparator, BigSwapper) 
 */

public interface BigSwapper {
	/** Swaps the data at the given position.
	 * 
	 * @param a the first position to swap.
	 * @param b the second position to swap.
	 */
	void swap( long a, long b );
}
