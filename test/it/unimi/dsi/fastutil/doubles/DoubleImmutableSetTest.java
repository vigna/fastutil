package it.unimi.dsi.fastutil.doubles;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class DoubleImmutableSetTest {

  @Test
  public void testEmpty() {
    DoubleImmutableSet set = DoubleImmutableSet.of();
    assertTrue(set.isEmpty());
    assertThrows(UnsupportedOperationException.class, () -> set.add((double) 10));
  }

  @Test
  public void test1() {
    DoubleImmutableSet set = DoubleImmutableSet.of((double) 1);
    assertEquals(1, set.size());
    assertTrue(set.contains((double) 1));
    assertThrows(UnsupportedOperationException.class, () -> set.add((double) 10));
  }

  @Test
  public void test5() {
    DoubleImmutableSet set =
        DoubleImmutableSet.of((double) 1, (double) 2, (double) 3, (double) 4, (double) 5);
    assertEquals(5, set.size());
    assertTrue(set.contains((double) 1));
    assertTrue(set.contains((double) 2));
    assertTrue(set.contains((double) 3));
    assertTrue(set.contains((double) 4));
    assertTrue(set.contains((double) 5));
    assertThrows(UnsupportedOperationException.class, () -> set.add((double) 10));
  }

  @Test
  @SuppressWarnings("UnnecessaryBoxing")
  public void testBuilder() {
    DoubleImmutableSet.Builder builder = new DoubleImmutableSet.Builder();
    builder.add((double) 0);
    builder.add((double) 1, (double) 2, (double) 3);
    builder.add(Double.valueOf((double) 4));
    builder.addAll(DoubleArraySet.of((double) 5, (double) 6, (double) 7));
    builder.addAll(new HashSet<>(Arrays.asList((double) 8, (double) 9)));
    DoubleImmutableSet set = builder.build();
    assertEquals(10, set.size());
    assertTrue(set.contains((double) 0));
    assertTrue(set.contains((double) 1));
    assertTrue(set.contains((double) 2));
    assertTrue(set.contains((double) 3));
    assertTrue(set.contains((double) 4));
    assertTrue(set.contains((double) 5));
    assertTrue(set.contains((double) 6));
    assertTrue(set.contains((double) 7));
    assertTrue(set.contains((double) 8));
    assertTrue(set.contains((double) 9));
  }

  @Test
  public void testCollector() {
    DoubleImmutableSet c =
        Arrays.asList((double) 1, (double) 2).stream()
            .collect(DoubleImmutableSet.toDoubleImmutableSet());
    assertEquals(2, c.size());
    assertTrue(c.contains((double) 1));
    assertTrue(c.contains((double) 2));
  }
}
