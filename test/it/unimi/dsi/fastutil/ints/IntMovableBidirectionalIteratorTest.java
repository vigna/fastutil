/*
 * Copyright (C) 2017-2022 Sebastiano Vigna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.objects.ObjectMovableBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import org.junit.Test;

import static org.junit.Assert.*;

public class IntMovableBidirectionalIteratorTest {
    @Test
    public void testAVLTrees() {
        testMovableOn(new IntAVLTreeSet());
        testMovableOn(new Int2IntAVLTreeMap());
    }

    @Test
    public void testRBTrees() {
        testMovableOn(new IntRBTreeSet());
        testMovableOn(new Int2IntRBTreeMap());
    }

    private void testMovableOn(final IntSortedSet on) {
        testDifferentStep(on, 2, 10, 0, 100);
        testDifferentStep(on, 3, 7, 30, 333);
        testDifferentStep(on, 5, 21, 123, 1234);
    }

    private void testMovableOn(final Int2IntSortedMap on) {
        testDifferentStep(on, 2, 10, 0, 100);
        testDifferentStep(on, 3, 7, 30, 333);
        testDifferentStep(on, 5, 21, 123, 1234);
    }

    private void testDifferentStep(final IntSortedSet on, final int minStep, final int maxStep, final int lowerBond, final int upperBond) {
        final int middle = ((upperBond - lowerBond) / 2) + lowerBond;

        final int lowerBondSlice = lowerBond + maxStep;
        final int upperBondSlice = upperBond - maxStep;

        for (int step = minStep; step < maxStep; step += 1) {
            on.clear();

            final int beforeMiddle = (middle / step) * step;
            final int afterMiddle = beforeMiddle + step;

            for (int i = 0; i < upperBond; i += step) {
                on.add(i);
            }

            testMovableBidirectionalIteratorIterationImpl(on, middle, afterMiddle, beforeMiddle, false);
            testMovableBidirectionalIteratorIterationImpl(on.subSet(lowerBondSlice, upperBondSlice), middle, afterMiddle , beforeMiddle, false);
        }
    }

    private void testDifferentStep(final Int2IntSortedMap on, final int minStep, final int maxStep, final int lowerBond, final int upperBond) {
        final int middle = ((upperBond - lowerBond) / 2) + lowerBond;

        final int lowerBondSlice = lowerBond + maxStep;
        final int upperBondSlice = upperBond - maxStep;

        for (int step = minStep; step < maxStep; step += 1) {
            on.clear();

            final int beforeMiddle = (middle / step) * step;
            final int afterMiddle = beforeMiddle + step;

            for (int i = 0; i < upperBond; i += step) {
                on.put(i, i);
            }

            testMovableBidirectionalIteratorIterationImpl(on.keySet(), middle, afterMiddle, beforeMiddle, false);
            testMovableBidirectionalIteratorIterationImpl(on.int2IntEntrySet(), asEntry(middle), asEntry(afterMiddle), asEntry(beforeMiddle), false);

            testMovableBidirectionalIteratorIterationImpl(on.subMap(lowerBondSlice, upperBondSlice).keySet(), middle, afterMiddle , beforeMiddle, true);
            testMovableBidirectionalIteratorIterationImpl(on.subMap(lowerBondSlice, upperBondSlice).int2IntEntrySet(), asEntry(middle), asEntry(afterMiddle), asEntry(beforeMiddle), true);

            testMovableBidirectionalIteratorIterationImpl(on.keySet().subSet(lowerBondSlice, upperBondSlice), middle, afterMiddle , beforeMiddle, true);
            testMovableBidirectionalIteratorIterationImpl(on.int2IntEntrySet().subSet(asEntry(lowerBondSlice), asEntry(upperBondSlice)), asEntry(middle), asEntry(afterMiddle), asEntry(beforeMiddle), true);

        }
    }

    private void testMovableBidirectionalIteratorIterationImpl(final IntSortedSet s, final int movePos, final int moveNext, final int movePrev, final boolean hastReferencesOutside) {
        final IntMovableBidirectionalIterator it = s.iterator(movePos);

        assertTrue(it.hasNext());
        assertEquals(moveNext, it.nextInt());

        assertTrue(it.hasPrevious());
        assertEquals(moveNext, it.previousInt());

        assertTrue(it.hasPrevious());
        assertEquals(movePrev, it.previousInt());

        it.move(movePos);
        assertTrue(it.hasPrevious());
        assertEquals(movePrev, it.previousInt());

        assertTrue(it.hasNext());
        assertEquals(movePrev, it.nextInt());

        assertTrue(it.hasNext());
        assertEquals(moveNext, it.nextInt());

        it.move(s.firstInt());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals(s.firstInt(), it.previousInt());

        it.move(s.firstInt() - 1);
        assertTrue(it.hasNext());
        assertEquals(hastReferencesOutside, it.hasPrevious());
        assertEquals(s.firstInt(), it.nextInt());

        it.move(s.lastInt());
        assertEquals(hastReferencesOutside, it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals(s.lastInt(), it.previousInt());

        it.move(s.lastInt() + 1);
        assertEquals(hastReferencesOutside, it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals(s.lastInt(), it.previousInt());

        it.begin();
        assertTrue(it.hasNext());
        assertEquals(hastReferencesOutside, it.hasPrevious());
        assertEquals(s.firstInt(), it.nextInt());

        it.end();
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals(s.lastInt(), it.nextInt());
    }

    private Int2IntMap.Entry asEntry(final int i) {
        return new AbstractInt2IntMap.BasicEntry(i, i);
    }

    private void testMovableBidirectionalIteratorIterationImpl(final ObjectSortedSet<Int2IntMap.Entry> s, final Int2IntMap.Entry movePos, final Int2IntMap.Entry moveNext, final Int2IntMap.Entry movePrev, final boolean hastReferencesOutside) {
        final ObjectMovableBidirectionalIterator<Int2IntMap.Entry> it = s.iterator(movePos);

        assertTrue(it.hasNext());
        assertEquals(moveNext, it.next());

        assertTrue(it.hasPrevious());
        assertEquals(moveNext, it.previous());

        assertTrue(it.hasPrevious());
        assertEquals(movePrev, it.previous());

        it.move(movePos);
        assertTrue(it.hasPrevious());
        assertEquals(movePrev, it.previous());

        assertTrue(it.hasNext());
        assertEquals(movePrev, it.next());

        assertTrue(it.hasNext());
        assertEquals(moveNext, it.next());

        it.move(s.first());
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals(s.first(), it.previous());

        it.move(asEntry(s.first().getIntKey() - 1));
        assertTrue(it.hasNext());
        assertEquals(hastReferencesOutside, it.hasPrevious());
        assertEquals(s.first(), it.next());

        it.move(s.last());
        assertEquals(hastReferencesOutside, it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals(s.last(), it.previous());

        it.move(asEntry(s.last().getIntKey() + 1));
        assertEquals(hastReferencesOutside, it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals(s.last(), it.previous());

        it.begin();
        assertTrue(it.hasNext());
        assertEquals(hastReferencesOutside, it.hasPrevious());
        assertEquals(s.first(), it.next());

        it.end();
        assertTrue(it.hasNext());
        assertTrue(it.hasPrevious());
        assertEquals(s.last(), it.next());
    }
}
