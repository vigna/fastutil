package it.unimi.dsi.fastutil.chars;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class CharImmutableSetTest {

  @Test
  public void testEmpty() {
    CharImmutableSet set = CharImmutableSet.of();
    assertTrue(set.isEmpty());
    assertThrows(UnsupportedOperationException.class, () -> set.add((char) 10));
  }

  @Test
  public void test1() {
    CharImmutableSet set = CharImmutableSet.of((char) 1);
    assertEquals(1, set.size());
    assertTrue(set.contains((char) 1));
    assertThrows(UnsupportedOperationException.class, () -> set.add((char) 10));
  }

  @Test
  public void test5() {
    CharImmutableSet set = CharImmutableSet.of((char) 1, (char) 2, (char) 3, (char) 4, (char) 5);
    assertEquals(5, set.size());
    assertTrue(set.contains((char) 1));
    assertTrue(set.contains((char) 2));
    assertTrue(set.contains((char) 3));
    assertTrue(set.contains((char) 4));
    assertTrue(set.contains((char) 5));
    assertThrows(UnsupportedOperationException.class, () -> set.add((char) 10));
  }

  @Test
  @SuppressWarnings("UnnecessaryBoxing")
  public void testBuilder() {
    CharImmutableSet.Builder builder = new CharImmutableSet.Builder();
    builder.add((char) 0);
    builder.add((char) 1, (char) 2, (char) 3);
    builder.add(Character.valueOf((char) 4));
    builder.addAll(CharArraySet.of((char) 5, (char) 6, (char) 7));
    builder.addAll(new HashSet<>(Arrays.asList((char) 8, (char) 9)));
    CharImmutableSet set = builder.build();
    assertEquals(10, set.size());
    assertTrue(set.contains((char) 0));
    assertTrue(set.contains((char) 1));
    assertTrue(set.contains((char) 2));
    assertTrue(set.contains((char) 3));
    assertTrue(set.contains((char) 4));
    assertTrue(set.contains((char) 5));
    assertTrue(set.contains((char) 6));
    assertTrue(set.contains((char) 7));
    assertTrue(set.contains((char) 8));
    assertTrue(set.contains((char) 9));
  }

  @Test
  public void testCollector() {
    CharImmutableSet c =
        Arrays.asList((char) 1, (char) 2).stream().collect(CharImmutableSet.toCharImmutableSet());
    assertEquals(2, c.size());
    assertTrue(c.contains((char) 1));
    assertTrue(c.contains((char) 2));
  }
}
