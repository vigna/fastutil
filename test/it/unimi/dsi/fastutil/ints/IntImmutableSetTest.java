package it.unimi.dsi.fastutil.ints;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class IntImmutableSetTest {

  @Test
  public void testEmpty() {
    IntImmutableSet set = IntImmutableSet.of();
    assertTrue(set.isEmpty());
    assertThrows(UnsupportedOperationException.class, () -> set.add(10));
  }

  @Test
  public void test1() {
    IntImmutableSet set = IntImmutableSet.of(1);
    assertEquals(1, set.size());
    assertTrue(set.contains(1));
    assertThrows(UnsupportedOperationException.class, () -> set.add(10));
  }

  @Test
  public void test5() {
    IntImmutableSet set = IntImmutableSet.of(1, 2, 3, 4, 5);
    assertEquals(5, set.size());
    assertTrue(set.contains(1));
    assertTrue(set.contains(2));
    assertTrue(set.contains(3));
    assertTrue(set.contains(4));
    assertTrue(set.contains(5));
    assertThrows(UnsupportedOperationException.class, () -> set.add(10));
  }

  @Test
  @SuppressWarnings("UnnecessaryBoxing")
  public void testBuilder() {
    IntImmutableSet.Builder builder = new IntImmutableSet.Builder();
    builder.add(0);
    builder.add(1, 2, 3);
    builder.add(Integer.valueOf(4));
    builder.addAll(IntArraySet.of(5, 6, 7));
    builder.addAll(new HashSet<>(Arrays.asList(8, 9)));
    IntImmutableSet set = builder.build();
    assertEquals(10, set.size());
    assertTrue(set.contains(0));
    assertTrue(set.contains(1));
    assertTrue(set.contains(2));
    assertTrue(set.contains(3));
    assertTrue(set.contains(4));
    assertTrue(set.contains(5));
    assertTrue(set.contains(6));
    assertTrue(set.contains(7));
    assertTrue(set.contains(8));
    assertTrue(set.contains(9));
  }

  @Test
  public void testCollector() {
    IntImmutableSet c = Arrays.asList(1, 2).stream().collect(IntImmutableSet.toIntImmutableSet());
    assertEquals(2, c.size());
    assertTrue(c.contains(1));
    assertTrue(c.contains(2));
  }
}
