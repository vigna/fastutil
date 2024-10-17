package it.unimi.dsi.fastutil.objects;

import org.junit.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class Object2ObjectMapTest {

	@Test
	public void testSmallMaps() {
		assertTrue(Object2ObjectMap.ofEntries() instanceof Object2ObjectMaps.EmptyMap);
		assertTrue(Object2ObjectMap.ofEntries(Object2ObjectMap.entry(new Object(), new Object())) instanceof Object2ObjectMaps.Singleton);
	}

	@Test
	public void testThrowOnDuplicate() {
		assertThrows(IllegalArgumentException.class, () -> Object2ObjectMap.ofEntries(
				Object2ObjectMap.entry("dupe", "foo"),
				Object2ObjectMap.entry("not a dupe", "bar"),
				Object2ObjectMap.entry("dupe", "exception")
				)
		);
	}


}
