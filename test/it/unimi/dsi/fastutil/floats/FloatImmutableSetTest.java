package it.unimi.dsi.fastutil.floats;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class FloatImmutableSetTest {

  @Test
  public void testEmpty() {
    FloatImmutableSet set = FloatImmutableSet.of();
    assertTrue(set.isEmpty());
    assertThrows(UnsupportedOperationException.class, () -> set.add((float) 10));
  }

  @Test
  public void test1() {
    FloatImmutableSet set = FloatImmutableSet.of((float) 1);
    assertEquals(1, set.size());
    assertTrue(set.contains((float) 1));
    assertThrows(UnsupportedOperationException.class, () -> set.add((float) 10));
  }

  @Test
  public void test5() {
    FloatImmutableSet set =
        FloatImmutableSet.of((float) 1, (float) 2, (float) 3, (float) 4, (float) 5);
    assertEquals(5, set.size());
    assertTrue(set.contains((float) 1));
    assertTrue(set.contains((float) 2));
    assertTrue(set.contains((float) 3));
    assertTrue(set.contains((float) 4));
    assertTrue(set.contains((float) 5));
    assertThrows(UnsupportedOperationException.class, () -> set.add((float) 10));
  }

  @Test
  @SuppressWarnings("UnnecessaryBoxing")
  public void testBuilder() {
    FloatImmutableSet.Builder builder = new FloatImmutableSet.Builder();
    builder.add((float) 0);
    builder.add((float) 1, (float) 2, (float) 3);
    builder.add(Float.valueOf((float) 4));
    builder.addAll(FloatArraySet.of((float) 5, (float) 6, (float) 7));
    builder.addAll(new HashSet<>(Arrays.asList((float) 8, (float) 9)));
    FloatImmutableSet set = builder.build();
    assertEquals(10, set.size());
    assertTrue(set.contains((float) 0));
    assertTrue(set.contains((float) 1));
    assertTrue(set.contains((float) 2));
    assertTrue(set.contains((float) 3));
    assertTrue(set.contains((float) 4));
    assertTrue(set.contains((float) 5));
    assertTrue(set.contains((float) 6));
    assertTrue(set.contains((float) 7));
    assertTrue(set.contains((float) 8));
    assertTrue(set.contains((float) 9));
  }

  @Test
  public void testCollector() {
    FloatImmutableSet c =
        Arrays.asList((float) 1, (float) 2).stream()
            .collect(FloatImmutableSet.toFloatImmutableSet());
    assertEquals(2, c.size());
    assertTrue(c.contains((float) 1));
    assertTrue(c.contains((float) 2));
  }
}
