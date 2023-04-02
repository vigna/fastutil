package it.unimi.dsi.fastutil.bytes;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ByteImmutableSetTest {

  @Test
  public void testEmpty() {
    ByteImmutableSet set = ByteImmutableSet.of();
    assertTrue(set.isEmpty());
    assertThrows(UnsupportedOperationException.class, () -> set.add((byte) 10));
  }

  @Test
  public void test1() {
    ByteImmutableSet set = ByteImmutableSet.of((byte) 1);
    assertEquals(1, set.size());
    assertTrue(set.contains((byte) 1));
    assertThrows(UnsupportedOperationException.class, () -> set.add((byte) 10));
  }

  @Test
  public void test5() {
    ByteImmutableSet set = ByteImmutableSet.of((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);
    assertEquals(5, set.size());
    assertTrue(set.contains((byte) 1));
    assertTrue(set.contains((byte) 2));
    assertTrue(set.contains((byte) 3));
    assertTrue(set.contains((byte) 4));
    assertTrue(set.contains((byte) 5));
    assertThrows(UnsupportedOperationException.class, () -> set.add((byte) 10));
  }

  @Test
  @SuppressWarnings("UnnecessaryBoxing")
  public void testBuilder() {
    ByteImmutableSet.Builder builder = new ByteImmutableSet.Builder();
    builder.add((byte) 0);
    builder.add((byte) 1, (byte) 2, (byte) 3);
    builder.add(Byte.valueOf((byte) 4));
    builder.addAll(ByteArraySet.of((byte) 5, (byte) 6, (byte) 7));
    builder.addAll(new HashSet<>(Arrays.asList((byte) 8, (byte) 9)));
    ByteImmutableSet set = builder.build();
    assertEquals(10, set.size());
    assertTrue(set.contains((byte) 0));
    assertTrue(set.contains((byte) 1));
    assertTrue(set.contains((byte) 2));
    assertTrue(set.contains((byte) 3));
    assertTrue(set.contains((byte) 4));
    assertTrue(set.contains((byte) 5));
    assertTrue(set.contains((byte) 6));
    assertTrue(set.contains((byte) 7));
    assertTrue(set.contains((byte) 8));
    assertTrue(set.contains((byte) 9));
  }

  @Test
  public void testCollector() {
    ByteImmutableSet c =
        Arrays.asList((byte) 1, (byte) 2).stream().collect(ByteImmutableSet.toByteImmutableSet());
    assertEquals(2, c.size());
    assertTrue(c.contains((byte) 1));
    assertTrue(c.contains((byte) 2));
  }
}
