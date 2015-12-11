package it.unimi.dsi.fastutil.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import it.unimi.dsi.fastutil.ints.Int2IntAVLTreeMap;
import org.junit.Test;

public class ObjectAVLTreeSetTest {

	@Test
	public void testGet() {
		ObjectAVLTreeSet<Integer> s = new ObjectAVLTreeSet<Integer>();
		Integer o = new Integer( 0 );
		s.add( o );
		assertSame( o,  s.get( new Integer( 0 ) ) );
	}

	@Test
	public void testAddTo() {
		Int2IntAVLTreeMap a = new Int2IntAVLTreeMap();
		Int2IntAVLTreeMap b = new Int2IntAVLTreeMap();

		// test addTo with empty map
		a.addTo(0, 1); 			// 0 -> 1
		assertEquals(1, a.get(0));

		// test addTo with empty map and weird defaultReturnValue
		b.defaultReturnValue(100);
		a.addTo(0, 0); 			// 0 -> 100
		assertEquals(100, b.get(0));

		// test addTo with existing values
		a.addTo(0, 1); 	 	  // 0 -> 2
		b.addTo(0, -100); 	// 0 -> 0
		assertEquals(2, a.get(0));
		assertEquals(0, b.get(0));

		// test addTo with overflow values
		a.put(0, Integer.MAX_VALUE);
		a.addTo(0, 1);			// 0 -> MIN_VALUE
		assertEquals(Integer.MIN_VALUE, a.get(0));

		// test various addTo operations
		a.put(0, 0);
		a.put(1, 1);
		a.put(2, 2);

		a.addTo(0, 10);			// 0 -> 10
		a.addTo(1, 9);			// 1 -> 10
		a.addTo(2, 8);			// 2 -> 10
		assertEquals(10, a.get(0));
		assertEquals(10, a.get(1));
		assertEquals(10, a.get(2));
	}
}
