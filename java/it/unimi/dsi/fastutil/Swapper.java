package it.unimi.dsi.fastutil;

/** An object that can swap elements whose position is specified by integers 
 *
 * @see Arrays#quickSort(int, int, it.unimi.dsi.fastutil.ints.IntComparator, Swapper)
 */

public interface Swapper {
	/** Swaps the data at the given positions.
	 * 
	 * @param a the first position to swap.
	 * @param b the second position to swap.
	 */
	void swap( int a, int b );
}
