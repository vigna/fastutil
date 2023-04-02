package it.unimi.dsi.fastutil.shorts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ShortImmutableSetTest {

  @Test
  public void testEmpty() {
    ShortImmutableSet set = ShortImmutableSet.of();
    assertTrue(set.isEmpty());
    assertThrows(UnsupportedOperationException.class, () -> set.add((short) 10));
  }

  @Test
  public void test1() {
    ShortImmutableSet set = ShortImmutableSet.of((short) 1);
    assertEquals(1, set.size());
    assertTrue(set.contains((short) 1));
    assertThrows(UnsupportedOperationException.class, () -> set.add((short) 10));
  }

  @Test
  public void test5() {
    ShortImmutableSet set =
        ShortImmutableSet.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5);
    assertEquals(5, set.size());
    assertTrue(set.contains((short) 1));
    assertTrue(set.contains((short) 2));
    assertTrue(set.contains((short) 3));
    assertTrue(set.contains((short) 4));
    assertTrue(set.contains((short) 5));
    assertThrows(UnsupportedOperationException.class, () -> set.add((short) 10));
  }

  @Test
  @SuppressWarnings("UnnecessaryBoxing")
  public void testBuilder() {
    ShortImmutableSet.Builder builder = new ShortImmutableSet.Builder();
    builder.add((short) 0);
    builder.add((short) 1, (short) 2, (short) 3);
    builder.add(Short.valueOf((short) 4));
    builder.addAll(ShortArraySet.of((short) 5, (short) 6, (short) 7));
    builder.addAll(new HashSet<>(Arrays.asList((short) 8, (short) 9)));
    ShortImmutableSet set = builder.build();
    assertEquals(10, set.size());
    assertTrue(set.contains((short) 0));
    assertTrue(set.contains((short) 1));
    assertTrue(set.contains((short) 2));
    assertTrue(set.contains((short) 3));
    assertTrue(set.contains((short) 4));
    assertTrue(set.contains((short) 5));
    assertTrue(set.contains((short) 6));
    assertTrue(set.contains((short) 7));
    assertTrue(set.contains((short) 8));
    assertTrue(set.contains((short) 9));
  }

  @Test
  public void testCollector() {
    ShortImmutableSet c =
        Arrays.asList((short) 1, (short) 2).stream()
            .collect(ShortImmutableSet.toShortImmutableSet());
    assertEquals(2, c.size());
    assertTrue(c.contains((short) 1));
    assertTrue(c.contains((short) 2));
  }
}
