package it.unimi.dsi.fastutil.objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class Object2ObjectOpenHashMapTest
{
	@Test
	public void testGetOrDefault()
	{
		Object2ObjectOpenHashMap<String, String> testing = new Object2ObjectOpenHashMap<>();
		testing.put("test0", "test0");
		testing.put("test1", "test1");
		testing.put("test2", "test2");
		testing.put("test3", "test3");
		testing.put("test4", "test4");
		testing.put("test5", "test5");
		assertEquals("test6", testing.getOrDefault("test6", "test6"));
		assertNotEquals("test6", testing.getOrDefault("test5", "test6"));
		assertEquals(null, testing.putIfAbsent("test7", "test7"));
		assertEquals("test4", testing.putIfAbsent("test4", "test7"));
	}
}
