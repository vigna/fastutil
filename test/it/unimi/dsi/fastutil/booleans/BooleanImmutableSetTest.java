package it.unimi.dsi.fastutil.booleans;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BooleanImmutableSetTest {

  @Test
  public void testEmpty() {
    BooleanImmutableSet set = BooleanImmutableSet.of();
    assertTrue(set.isEmpty());
    assertThrows(UnsupportedOperationException.class, () -> set.add(false));
  }

  @Test
  public void test1() {
    BooleanImmutableSet set = BooleanImmutableSet.of(true);
    assertEquals(1, set.size());
    assertTrue(set.contains(true));
    assertThrows(UnsupportedOperationException.class, () -> set.add(false));
  }

  @Test
  @SuppressWarnings("UnnecessaryBoxing")
  public void testBuilder() {
    BooleanImmutableSet.Builder builder = new BooleanImmutableSet.Builder();
    builder.add(true);
    builder.addAll(BooleanArraySet.of(false));
    BooleanImmutableSet set = builder.build();
    assertEquals(2, set.size());
    assertTrue(set.contains(true));
    assertTrue(set.contains(false));
  }

  @Test
  public void testCollector() {
    BooleanImmutableSet c =
        Arrays.asList(true, false).stream().collect(BooleanImmutableSet.toBooleanImmutableSet());
    assertEquals(2, c.size());
    assertTrue(c.contains(true));
    assertTrue(c.contains(false));
  }
}
