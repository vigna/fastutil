package it.unimi.dsi.fastutil.booleans;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * A {@link BooleanSet} implemented by storing boolean presence directly in member variable(s) state,
 * with no arbitrarily sized backing arrays.
 *
 * <p>As such, the methods in the class are implemented iteration free (except for the bulk operations if
 * given a {@link java.util.Collection} that isn't a {@link BooleanDirectSet}.
 *
 * <p>If both {@code false} and {@code true} are present, then it is assured that {@code false} will be
 * ordered first. 
 *
 * @implNote The current implementation uses two boolean variables, one for {@code false} presence, and
 * one for {@code true} presence. While this is an extremely compact representation, it also leads to
 * some rather branch heavy code, albeit still all constant time.
 * 
 * @author C. Sean Young &lt;csyoung@google.com&gt;
 *
 */
// TODO Would being backed with a byte bitfield be faster? We could use bit twiddling fun to eliminate a lot of branches.
public class BooleanDirectSet extends AbstractBooleanSet implements BooleanSet, Cloneable, Serializable {

	private static final long serialVersionUID = 5609973451981507006L;

	private boolean hasFalse;
	private boolean hasTrue;

	public BooleanDirectSet() {
		hasFalse = false;
		hasTrue = false;
	}

	public BooleanDirectSet(BooleanCollection b) {
		addAll(b);
	}
	
	public BooleanDirectSet(boolean[] a) {
		for (boolean b : a) {
			if (b) {
				hasTrue = true;
			} else {
				hasFalse = true;
			}
			if (hasFalse && hasTrue) {
				// Nothing else in our universe that could possibly be added.
				break;
			}
		}
	}
	
	public static BooleanDirectSet withGivenMemberships(boolean hasFalse, boolean hasTrue) {
		BooleanDirectSet result = new BooleanDirectSet();
		result.hasFalse = hasFalse;
		result.hasTrue = hasTrue;
		return result;
	}

	public static BooleanDirectSet of() {
		return new BooleanDirectSet();
	}
	
	public static BooleanDirectSet of(boolean value) {
		BooleanDirectSet result = new BooleanDirectSet();
		if (value) {
			result.hasTrue = true;
		} else {
			result.hasFalse = true;
		}
		return result;
	}

	public static BooleanDirectSet of(boolean b1, boolean b2) {
		if (b1 == b2) {
			throw new IllegalArgumentException("Duplicate element: " + b1);
		}
		return ofAll();
	}

	public static BooleanDirectSet ofAll() {
		BooleanDirectSet result = new BooleanDirectSet();
		result.hasFalse = true;
		result.hasTrue = true;
		return result;
	}
	
	@Override
	public boolean add(boolean k) {
		if (k && !hasTrue) {
			hasTrue = true;
			return true;
		} else if (!k && !hasFalse) {
			hasFalse = true;
			return true;	
		}
		return false;
	}
	
	public boolean addAll(BooleanDirectSet s) {
		boolean anyAdded = false;
		if (!hasFalse && s.hasFalse) {
			hasFalse = true;
			anyAdded = true;
		}
		if (!hasTrue && s.hasTrue) {
			hasTrue = true;
			anyAdded = true;
		}
		return anyAdded;
	}

	@Override
	public boolean addAll(BooleanCollection c) {
		if (c instanceof BooleanDirectSet) {
			return addAll((BooleanDirectSet) c);
		}
		if (hasFalse && hasTrue) {
			// Nothing else in our universe that could possibly be added.
			return false;
		}
		boolean anyAdded = false;
		BooleanIterator i = c.iterator();
		while (i.hasNext()) {
			boolean next = i.nextBoolean();
			anyAdded |= add(next);
			if (hasFalse && hasTrue) {
				// Nothing else in our universe that could possibly be added; no need to look further
				break;
			}
		}
		return anyAdded;
	}

	@Override
	public boolean remove(boolean k) {
		if (k) {
			if (hasTrue) {
				hasTrue = false;
				return true;
			}
		} else {
			if (hasFalse) {
				hasFalse = false;
				return true;
			}
		}
		return false;
	}
	
	public boolean removeAll(BooleanDirectSet s) {
		boolean anyRemoved = false;
		if (s.hasFalse && hasFalse) {
			hasFalse = false;
			anyRemoved = true;
		}
		if (s.hasTrue && hasTrue) {
			hasTrue = false;
			anyRemoved = true;
		}
		return anyRemoved;
	}
	
	@Override
	public boolean removeAll(BooleanCollection c) {
		if (c instanceof BooleanDirectSet) {
			return removeAll((BooleanDirectSet) c);
		}
		if (isEmpty()) {
			return false;
		}
		if (c.isEmpty()) {
			return false;
		}
		BooleanIterator i = c.iterator();
		boolean anyRemoved = false;
		while (i.hasNext()) {
			boolean next = i.nextBoolean();
			if (!next && hasFalse) {
				hasFalse = false;
				anyRemoved = true;
			}
			if (next && hasTrue) {
				hasTrue = false;
				anyRemoved = true;
			}
			if (isEmpty()) {
				break;
			}
		}
		return anyRemoved;
	}
	
	public boolean retainAll(BooleanDirectSet s) {
		boolean anyRemoved = false;
		if (hasFalse && !s.hasFalse) {
			hasFalse = false;
			anyRemoved = true;
		}
		if (hasTrue && !s.hasTrue) {
			hasTrue = false;
			anyRemoved = true;
		}
		return anyRemoved;
	}
	
	@Override
	public boolean retainAll(BooleanCollection c) {
		if (isEmpty()) {
			return false;
		}
		if (c.isEmpty()) {
			clear();
			return true;
		}
		if (c instanceof BooleanDirectSet) {
			return retainAll((BooleanDirectSet) c);
		}
		boolean seenFalse = false;
		boolean seenTrue = false;
		BooleanIterator i = c.iterator();
		while (i.hasNext()) {
			boolean next = i.nextBoolean();
			if (next) seenTrue = true;
			else seenFalse = true;
			if ((seenFalse || !hasFalse) && (seenTrue || !hasTrue)) {
				// We are already retaining everything we have, no need to look further.
				return false;
			}
		}
		boolean anyRemoved = false;
		if (!seenFalse && hasFalse) {
			hasFalse = false;
			anyRemoved = true;
		}
		if (!seenTrue && hasTrue) {
			hasTrue = false;
			anyRemoved = true;
		}
		return anyRemoved;
	}
	
	@Deprecated
	@Override
	public boolean removeIf(java.util.function.Predicate<? super java.lang.Boolean> filter) {
		boolean anyRemoved = false;
		if (hasFalse && filter.test(false)) {
			hasFalse = false;
			anyRemoved = true;
		}
		if (hasTrue && filter.test(true)) {
			hasTrue = false;
			anyRemoved = true;
		}
		return anyRemoved;
	}
	
	public boolean containsAll(BooleanDirectSet s) {
		return (!s.hasFalse || hasFalse) && (!s.hasTrue || hasTrue);
	}

	@Override
	public boolean containsAll(BooleanCollection c) {
		if (hasFalse && hasTrue) {
			// We already contain every possible value; no need to even check.
			return true;
		}
		if (c instanceof BooleanDirectSet) {
			return containsAll((BooleanDirectSet) c);
		}
		if (isEmpty()) {
			return c.isEmpty();
		}
		if (c.isEmpty()) {
			return true;
		}
		// Size 0 and 2 taken care above, so we have only one 
		boolean myOnlyElementIs = hasTrue;
		BooleanIterator i = c.iterator();
		while (i.hasNext()) {
			boolean next = i.nextBoolean();
			if (next != myOnlyElementIs) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void clear() {
		hasFalse = false;
		hasTrue = false;
	}
	
	@Override
	public boolean contains(boolean k) {
		return k ? hasTrue : hasFalse;
	}

	@Deprecated
	@Override
	public boolean contains(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Boolean)) {
			return false;
		}
		return contains(((Boolean) o).booleanValue());
	}

	@Override
	public int size() {
		return (hasFalse ? 1 : 0) + (hasTrue ? 1 : 0);
	}

	@Override
	public boolean isEmpty() {
		return !hasFalse && !hasTrue; 
	}

	@Override
	public boolean[] toBooleanArray() {
		// To be consistent with the toArray() spec, we will return a new array every time, even if empty.
		return hasFalse ? (hasTrue ? new boolean[] {false, true} : new boolean[] {false}) : (hasTrue ? new boolean[] {true} : new boolean[0]);
	}

	@Override
	public boolean[] toArray(boolean[] a) {
		int size = size();
		if (size == 0) {
			return a == null ? new boolean[0] : a;
		}
		if (a == null || a.length < size) {
			return toBooleanArray();
		}
		int i = 0;
		if (hasFalse) {
			a[i++] = false;
		}
		if (hasTrue) {
			a[i] = true;
		}
		return a;
	}

	@Override
	public void forEach(BooleanConsumer action) {
		if (hasFalse) action.accept(false);
		if (hasTrue) action.accept(true);
	}

	@Override
	public BooleanIterator iterator() {
		return new Iterator();
	}
	
	@Override
	public BooleanSpliterator spliterator() {
		return new Iterator();
	}

	@Override
	public BooleanDirectSet clone() {
		BooleanDirectSet cloned;
		try {
			cloned = (BooleanDirectSet) super.clone();
		} catch (CloneNotSupportedException impossible) {
			throw new InternalError(impossible);
		}
		return cloned;
	}


	@Override
	public String toString() {
		// To be consistent with the toArray() spec, we will return a new array every time, even if empty.
		return hasFalse ? (hasTrue ? "{false, true}" : "{false}") : (hasTrue ? "{true}" : "{}");
	}

	// This class is so dead simple it makes sense to roll the iterator and spliterator into one. 
	private class Iterator implements BooleanIterator, BooleanSpliterator {
		boolean returnedFalse = false;
		boolean returnedTrue = false;
		
		Iterator() {}

		@Override
		public boolean hasNext() {
			return (hasFalse && !returnedFalse) || (hasTrue && !returnedTrue);
		}

		@Override
		public boolean nextBoolean() {
			if (hasFalse && !returnedFalse) {
				returnedFalse = true;
				return false;
			}
			if (hasTrue && !returnedTrue) {
				returnedTrue = true;
				return true;
			}
			throw new java.util.NoSuchElementException();
		}
		
		@Override
		public void remove() {
			if (returnedTrue && hasTrue) {
				returnedTrue = false;
				hasTrue = false;
			} else if (returnedFalse && hasFalse && (!returnedTrue || !hasTrue)) {
				returnedFalse = false;
				hasFalse = false;
			}
			throw new IllegalStateException();
		}

		@Override
		public void forEachRemaining(BooleanConsumer action) {
			if (!returnedFalse && hasFalse) {
				action.accept(false);
				returnedFalse = true;
			}
			if (!returnedTrue && hasTrue) {
				action.accept(true);
				returnedTrue = true;
			}
		}

		@Override
		public boolean tryAdvance(BooleanConsumer action) {
			if (!returnedFalse && hasFalse) {
				action.accept(false);
				returnedFalse = true;
				return true;
			}
			if (!returnedTrue && hasTrue) {
				action.accept(true);
				returnedTrue = true;
				return true;
			}
			return false;
		}

		@Override
		public long estimateSize() {
			return ((hasFalse && !returnedFalse) ? 1 : 0) + ((hasTrue && !returnedTrue) ? 1 : 0); 
		}

		@Override
		public int characteristics() {
			return BooleanSpliterators.SET_SPLITERATOR_CHARACTERISTICS | java.util.Spliterator.ORDERED;
		}

		@Override
		public BooleanSpliterator trySplit() {
			// Too small to bother with splitting.
			return null;
		}

		// To resolve doubly inherited methods.
		@SuppressWarnings("deprecation")
		@Override
		public void forEachRemaining(Consumer<? super Boolean> action) {
			BooleanIterator.super.forEachRemaining(action);
		}
	}
}
