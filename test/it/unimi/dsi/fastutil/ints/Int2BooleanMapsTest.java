package it.unimi.dsi.fastutil.ints;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class Int2BooleanMapsTest {

    static Predicate<Integer> getPredicate() {
        AtomicInteger calls = new AtomicInteger();
        return i -> {
            int c = calls.getAndIncrement();
            return c % 2 == 0;
        };
    }

    @Test
    public void testInt2BooleanMap() {
        final Int2BooleanMap result =
                Stream.of(1, 2, 3, 3)
                        .collect(
                                Int2BooleanMaps.collector(
                                        i -> i,
                                        getPredicate(),
                                        Boolean::logicalAnd,
                                        Int2BooleanArrayMap::new
                                )
                        );
        Int2BooleanMap expected =
                new Int2BooleanArrayMap(
                        new int[] {1, 2, 3},
                        new boolean[] {true, false, false}
                );

        assertEquals(result, expected);
    }

    @Test
    public void testInt2BooleanMapEmpty() {
        final Int2BooleanMap result =
                Stream.<Integer>empty()
                        .collect(
                                Int2BooleanMaps.collector(
                                        i -> i,
                                        getPredicate(),
                                        Boolean::logicalAnd,
                                        Int2BooleanArrayMap::new
                                )
                        );
        Int2BooleanMap expected = Int2BooleanMaps.EMPTY_MAP;

        assertEquals(result, expected);
    }

}
