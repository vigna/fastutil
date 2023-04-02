package it.unimi.dsi.fastutil.objects;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ObjectImmutableSetTest {

  @Test
  public void testEmpty() {
    ObjectImmutableSet set = ObjectImmutableSet.of();
    assertTrue(set.isEmpty());
    assertThrows(UnsupportedOperationException.class, () -> set.add(10));
  }

  @Test
  public void test1() {
    ObjectImmutableSet set = ObjectImmutableSet.of(1);
    assertEquals(1, set.size());
    assertTrue(set.contains(1));
    assertThrows(UnsupportedOperationException.class, () -> set.add(10));
  }

  @Test
  public void test5() {
    ObjectImmutableSet set = ObjectImmutableSet.of(1, 2, 3, 4, 5);
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
    ObjectImmutableSet.Builder builder = new ObjectImmutableSet.Builder();
    builder.add(0);
    builder.add(1, 2, 3);
    builder.add(Integer.valueOf(4));
    builder.addAll(ObjectArraySet.of(5, 6, 7));
    builder.addAll(new HashSet<>(Arrays.asList(8, 9)));
    ObjectImmutableSet set = builder.build();
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
    ObjectImmutableSet c =
        Arrays.asList(1, 2).stream().collect(ObjectImmutableSet.toObjectImmutableSet());
    assertEquals(2, c.size());
    assertTrue(c.contains(1));
    assertTrue(c.contains(2));
  }
}
