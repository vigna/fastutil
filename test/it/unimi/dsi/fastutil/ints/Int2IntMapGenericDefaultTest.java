package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Supplier;
import org.junit.runners.Parameterized.Parameters;

public class Int2IntMapGenericDefaultTest extends Int2IntMapGenericTest {
	@Parameters
	public static Iterable<Object[]> data() {
		final EnumSet<Capability> capabilities = EnumSet.allOf(Capability.class);
		capabilities.remove(Capability.ITERATOR_MODIFY);
		capabilities.remove(Capability.KEY_SET_MODIFY);
		return Collections.singletonList(new Object[] {
				(Supplier<Int2IntMap>) () -> new SimpleInt2IntMap(new Int2IntArrayMap()), capabilities
		});
	}

	private static final class SimpleInt2IntMap implements Int2IntMap {
		private final Int2IntMap delegate;

		SimpleInt2IntMap(final Int2IntMap delegate) {
			this.delegate = delegate;
		}

		@Override
		public void clear() {
			delegate.clear();
		}

		@Override
		public boolean containsKey(final int key) {
			return delegate.containsKey(key);
		}

		@Override
		public boolean containsValue(final int value) {
			return delegate.containsValue(value);
		}

		@Override
		public void defaultReturnValue(final int rv) {
			delegate.defaultReturnValue(rv);
		}

		@Override
		public int defaultReturnValue() {
			return delegate.defaultReturnValue();
		}

		@Override
		public int get(final int key) {
			return delegate.get(key);
		}

		@Override
		public ObjectSet<Entry> int2IntEntrySet() {
			return delegate.int2IntEntrySet();
		}

		@Override
		public boolean isEmpty() {
			return delegate.isEmpty();
		}

		@Override
		public IntSet keySet() {
			return delegate.keySet();
		}

		@Override
		public int put(final int key, final int value) {
			return delegate.put(key, value);
		}

		@Override
		public void putAll(final Map<? extends Integer, ? extends Integer> m) {
			delegate.putAll(m);
		}

		@Override
		public int remove(final int key) {
			return delegate.remove(key);
		}

		@Override
		public int size() {
			return delegate.size();
		}

		@Override
		public IntCollection values() {
			return delegate.values();
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o) return true;
			return delegate.equals(o);
		}

		@Override
		public int hashCode() {
			return delegate.hashCode();
		}
	}
}