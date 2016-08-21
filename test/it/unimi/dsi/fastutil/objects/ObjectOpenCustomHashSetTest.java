package it.unimi.dsi.fastutil.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.bytes.ByteArrays;

import org.junit.Test;

public class ObjectOpenCustomHashSetTest {

	@Test
	public void testGetNullKey() {
		final ObjectOpenCustomHashSet<Integer> s = new ObjectOpenCustomHashSet<Integer>( new Hash.Strategy<Integer>() {

			@Override
			public int hashCode( Integer o ) {
				return o == null ? 0 : o.intValue() % 10;
			}

			@Override
			public boolean equals( Integer a, Integer b ) {
				if ( ( ( a == null ) != ( b == null ) ) || a == null ) return false;
				return ( a.intValue() - b.intValue() % 10 ) == 0;
			}
		});
		
		s.add( Integer.valueOf( 10 ) );
		assertTrue( s.contains( Integer.valueOf( 0 ) ) );
		assertEquals( 10, s.iterator().next().intValue() );
	}

	@Test
	public void testNullKey() {
		Random random = new Random(0);
		ObjectOpenCustomHashSet<byte[]> s = new ObjectOpenCustomHashSet<byte[]>(ByteArrays.HASH_STRATEGY);
		for(int i = 0; i < 1000000; i++) {
			byte[] a = new byte[random.nextInt(10)];
			for(int j = a.length; j-- != 0; ) a[j] = (byte) random.nextInt(4);
			s.add(a);
		}
	}

}
