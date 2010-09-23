package it.unimi.dsi.fastutil;

/** An object that can swap elements specified by longs. */

public interface BigSwapper {
	/** Swaps the data at the given position.
	 * 
	 * @param a the first position to swap.
	 * @param b the second position to swap.
	 */
	void swap( long a, long b );
}
