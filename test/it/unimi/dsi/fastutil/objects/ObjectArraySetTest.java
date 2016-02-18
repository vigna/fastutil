package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.io.BinIO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectArraySetTest {

	@SuppressWarnings("boxing")
	@Test
	public void testNullInEquals() {
		assertFalse( new ObjectArraySet<Integer>( Arrays.asList( 42 ) ).equals( Collections.singleton( null ) ) );
	}

	@SuppressWarnings("boxing")
	@Test
	public void testSet() {
		for( int i = 0; i <= 1; i++ ) {
			final ObjectArraySet<Integer> s = i == 0 ? new ObjectArraySet<Integer>() : new ObjectArraySet<Integer>( new Integer[] { 0 } );
			assertTrue( s.add( 1 ) );
			assertEquals( 1 + i, s.size() );
			assertTrue( s.contains( 1 ) );
			assertTrue( s.add(  2  ) );
			assertTrue( s.contains( 2 ) );
			assertEquals( 2 + i, s.size() );
			assertFalse( s.add( 1 ) );
			assertFalse( s.remove( 3 ) );
			assertTrue( s.add( 3 ) );
			assertEquals( 3 + i, s.size() );
			assertTrue( s.contains( 1 ) );
			assertTrue( s.contains( 2 ) );
			assertTrue( s.contains( 2 ) );
			assertEquals( new ObjectOpenHashSet<Integer>( i == 0 ? new Integer[] { 1, 2, 3 } : new Integer[] { 0, 1, 2, 3 } ), new ObjectOpenHashSet<Integer>( s.iterator() ) );
			assertTrue( s.remove( 3 ) );
			assertEquals( 2 + i, s.size() );
			assertTrue( s.remove( 1 ) );
			assertEquals( 1 + i, s.size() );
			assertFalse( s.contains( 1 ) );
			assertTrue( s.remove( 2 ) );
			assertEquals( 0 + i, s.size() );
			assertFalse( s.contains( 1 ) );
		}
	}
	
	@SuppressWarnings("boxing")
	@Test
	public void testClone() {
		ObjectArraySet<Integer> s = new ObjectArraySet<Integer>();
		assertEquals( s, s.clone() );
		s.add( 0 );
		assertEquals( s, s.clone() );
		s.add( 0 );
		assertEquals( s, s.clone() );
		s.add( 1 );
		assertEquals( s, s.clone() );
		s.add( 2 );
		assertEquals( s, s.clone() );
		s.remove( 0 );
		assertEquals( s, s.clone() );
	}

	@SuppressWarnings("boxing")
	@Test
	public void testSerialisation() throws IOException, ClassNotFoundException {
		ObjectArraySet<Integer> s = new ObjectArraySet<Integer>();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream( baos );
		oos.writeObject( s );
		oos.close();
		assertEquals( s, BinIO.loadObject( new ByteArrayInputStream( baos.toByteArray() ) ) );
		
		s.add( 0 );
		s.add( 1 );

		baos.reset();
		oos = new ObjectOutputStream( baos );
		oos.writeObject( s );
		oos.close();
		assertEquals( s, BinIO.loadObject( new ByteArrayInputStream( baos.toByteArray() ) ) );
	}

	@Test
	@SuppressWarnings("boxing")
	public void testRemove() {
		ObjectSet<Integer> set = new ObjectArraySet<Integer>( new Integer[] { 42 } );

		Iterator<Integer> iterator = set.iterator();
		assertTrue(iterator.hasNext());
		iterator.next();
		iterator.remove();
		assertFalse( iterator.hasNext() );
		assertEquals( 0, set.size() );

		set = new ObjectArraySet<Integer>( new Integer[] { 42, 43, 44 } );

		iterator = set.iterator();
		assertTrue(iterator.hasNext());
		iterator.next();
		iterator.next();
		iterator.remove();
		assertEquals( Integer.valueOf( 44 ), iterator.next() );
		assertFalse( iterator.hasNext() );
		assertEquals( new ObjectArraySet<Integer>( new Integer[] { 42, 44 } ), set );
	}
}
