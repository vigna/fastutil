package it.unimi.dsi.fastutil.ints;

import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class Int2CharMapsTest {

    @Test
    public void testInt2CharMap() {
        final Int2CharMap result =
                Stream.of(1, 2, 3, 3)
                        .collect(
                                Int2CharMaps.collector(
                                        i -> (char) i.intValue(),
                                        i -> (Character) i,
                                        (char c0, char c1) -> (char) ((int) c0 + (int) c1),
                                        Int2CharArrayMap::new
                                )
                        );
        Int2CharMap expected =
                new Int2CharArrayMap(
                        new int[] {1, 2, 3},
                        new char[] {'\u0001', '\u0002', '\u0006'}
                );

        assertEquals(result, expected);
    }

    @Test
    public void testInt2CharMapEmpty() {
        final Int2CharMap result =
                Stream.<Integer>empty()
                        .collect(
                                Int2CharMaps.collector(
                                        i -> (char) i.intValue(),
                                        i -> (Character) i,
                                        (char c0, char c1) -> (char) ((int) c0 + (int) c1),
                                        Int2CharArrayMap::new
                                )
                        );
        Int2CharMap expected = Int2CharMaps.EMPTY_MAP;

        assertEquals(result, expected);
    }

}
