package it.unimi.dsi.fastutil.longs;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class LongImmutableSetTest {

  @Test
  public void testEmpty() {
    LongImmutableSet set = LongImmutableSet.of();
    assertTrue(set.isEmpty());
    assertThrows(UnsupportedOperationException.class, () -> set.add((long) 10));
  }

  @Test
  public void test1() {
    LongImmutableSet set = LongImmutableSet.of((long) 1);
    assertEquals(1, set.size());
    assertTrue(set.contains((long) 1));
    assertThrows(UnsupportedOperationException.class, () -> set.add((long) 10));
  }

  @Test
  public void test5() {
    LongImmutableSet set = LongImmutableSet.of((long) 1, (long) 2, (long) 3, (long) 4, (long) 5);
    assertEquals(5, set.size());
    assertTrue(set.contains((long) 1));
    assertTrue(set.contains((long) 2));
    assertTrue(set.contains((long) 3));
    assertTrue(set.contains((long) 4));
    assertTrue(set.contains((long) 5));
    assertThrows(UnsupportedOperationException.class, () -> set.add((long) 10));
  }

  @Test
  @SuppressWarnings("UnnecessaryBoxing")
  public void testBuilder() {
    LongImmutableSet.Builder builder = new LongImmutableSet.Builder();
    builder.add((long) 0);
    builder.add((long) 1, (long) 2, (long) 3);
    builder.add(Long.valueOf((long) 4));
    builder.addAll(LongArraySet.of((long) 5, (long) 6, (long) 7));
    builder.addAll(new HashSet<>(Arrays.asList((long) 8, (long) 9)));
    LongImmutableSet set = builder.build();
    assertEquals(10, set.size());
    assertTrue(set.contains((long) 0));
    assertTrue(set.contains((long) 1));
    assertTrue(set.contains((long) 2));
    assertTrue(set.contains((long) 3));
    assertTrue(set.contains((long) 4));
    assertTrue(set.contains((long) 5));
    assertTrue(set.contains((long) 6));
    assertTrue(set.contains((long) 7));
    assertTrue(set.contains((long) 8));
    assertTrue(set.contains((long) 9));
  }

  @Test
  public void testCollector() {
    LongImmutableSet c =
        Arrays.asList((long) 1, (long) 2).stream().collect(LongImmutableSet.toLongImmutableSet());
    assertEquals(2, c.size());
    assertTrue(c.contains((long) 1));
    assertTrue(c.contains((long) 2));
  }
}

