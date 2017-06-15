package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import org.junit.Before;

public class Int2IntMapGenericDefaultTest extends Int2IntMapGenericTest {
	@Before
	public void setUp() {
		m = new SimpleInt2IntMap(new Int2IntArrayMap());
	}

	private static final class SimpleInt2IntMap implements Int2IntMap {
		private final Int2IntMap delegate;

		SimpleInt2IntMap(Int2IntMap delegate) {
			this.delegate = delegate;
		}

		@Override
		public void clear() {
			delegate.clear();
		}

		@Override
		public boolean containsKey(int key) {
			return delegate.containsKey(key);
		}

		@Override
		public boolean containsValue(int value) {
			return delegate.containsValue(value);
		}

		@Override
		public void defaultReturnValue(int rv) {
			delegate.defaultReturnValue(rv);
		}

		@Override
		public int defaultReturnValue() {
			return delegate.defaultReturnValue();
		}

		@Override
		public int get(int key) {
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
		public int put(int key, int value) {
			return delegate.put(key, value);
		}

		@Override
		public void putAll(Map<? extends Integer, ? extends Integer> m) {
			delegate.putAll(m);
		}

		@Override
		public int remove(int key) {
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
	}
}