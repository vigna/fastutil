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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IntFixedArrayFIFOQueueTest {

    @Test
    public void testWorkflow() {
        IntFixedArrayFIFOQueue queue = new IntFixedArrayFIFOQueue(8);

        assertEquals(0, queue.size());
        assertEquals(Integer.MIN_VALUE, queue.dequeueInt(Integer.MIN_VALUE));

        assertTrue(queue.enqueue(1));
        assertTrue(queue.enqueue(2));
        assertTrue(queue.enqueue(3));
        assertTrue(queue.enqueue(4));

        assertEquals(4, queue.size());

        assertEquals(1, queue.firstInt());
        assertEquals(1, queue.firstInt(Integer.MIN_VALUE));
        assertEquals(4, queue.lastInt());
        assertEquals(4, queue.lastInt(Integer.MIN_VALUE));

        assertEquals(4, queue.size());

        assertEquals(1, queue.dequeueInt());
        assertEquals(2, queue.dequeueInt());
        assertEquals(3, queue.dequeueInt());
        assertEquals(4, queue.dequeueInt());

        assertEquals(0, queue.size());

        assertEquals(Integer.MIN_VALUE, queue.firstInt(Integer.MIN_VALUE));
        assertEquals(Integer.MIN_VALUE, queue.lastInt(Integer.MIN_VALUE));
        assertEquals(Integer.MIN_VALUE, queue.dequeueInt(Integer.MIN_VALUE));

        assertTrue(queue.enqueue(13));
        assertTrue(queue.enqueue(14));

        assertEquals(2, queue.size());
        assertEquals(13, queue.firstInt());
        assertEquals(14, queue.lastInt());

        assertTrue(queue.enqueueFirst(15));
        assertTrue(queue.enqueueFirst(16));

        assertEquals(4, queue.size());
        assertEquals(16, queue.firstInt());
        assertEquals(14, queue.lastInt());

        assertEquals(16, queue.dequeueInt());
        assertEquals(15, queue.dequeueInt());
        assertEquals(13, queue.dequeueInt());
        assertEquals(14, queue.dequeueInt());

        assertEquals(0, queue.size());

        assertEquals(Integer.MIN_VALUE, queue.firstInt(Integer.MIN_VALUE));
        assertEquals(Integer.MIN_VALUE, queue.lastInt(Integer.MIN_VALUE));
        assertEquals(Integer.MIN_VALUE, queue.dequeueInt(Integer.MIN_VALUE));

        assertTrue(queue.enqueue(21));
        assertTrue(queue.enqueue(22));
        assertTrue(queue.enqueue(23));
        assertTrue(queue.enqueue(24));

        assertEquals(4, queue.size());
        assertEquals(21, queue.firstInt());
        assertEquals(24, queue.lastInt());

        assertEquals(24, queue.dequeueLastInt());
        assertEquals(23, queue.dequeueLastInt());
        assertEquals(22, queue.dequeueLastInt());
        assertEquals(21, queue.dequeueLastInt());
    }

    @Test
    public void testForward() {
        IntFixedArrayFIFOQueue queue = new IntFixedArrayFIFOQueue(32);

        for (int i = 0; i < queue.capacity(); i++) {
            final int lim = queue.capacity() / 2;

            assertEquals(0, queue.size());

            for (int j = 0; j < lim; j++) {
                assertTrue(queue.enqueue(j));
                assertTrue(queue.enqueue(-j));

                assertEquals(0, queue.firstInt());
                assertEquals(0, queue.firstInt(Integer.MAX_VALUE));

                assertEquals(-j, queue.lastInt());
                assertEquals(-j, queue.lastInt(Integer.MAX_VALUE));
            }

            assertEquals(lim * 2, queue.size());


            for (int j = 0; j < lim; j++) {
                assertEquals(j, queue.firstInt());
                assertEquals(j, queue.firstInt(Integer.MAX_VALUE));

                assertEquals(1 - lim, queue.lastInt());
                assertEquals(1 - lim, queue.lastInt(Integer.MAX_VALUE));

                assertEquals(j, queue.dequeueInt());
                assertEquals(-j, queue.dequeueInt(Integer.MAX_VALUE));
            }

            assertEquals(0, queue.size());
        }
    }

    @Test
    public void testBackward() {
        IntFixedArrayFIFOQueue queue = new IntFixedArrayFIFOQueue(32);

        for (int i = 0; i < queue.capacity(); i++) {
            final int lim = queue.capacity() / 2;

            assertEquals(0, queue.size());

            for (int j = 0; j < lim; j++) {
                assertTrue(queue.enqueueFirst(j));
                assertTrue(queue.enqueueFirst(-j));

                assertEquals(-j, queue.firstInt());
                assertEquals(-j, queue.firstInt(Integer.MAX_VALUE));

                assertEquals(0, queue.lastInt());
                assertEquals(0, queue.lastInt(Integer.MAX_VALUE));
            }

            assertEquals(lim * 2, queue.size());


            for (int j = 0; j < lim; j++) {
                assertEquals(1 - lim, queue.firstInt());
                assertEquals(1 - lim, queue.firstInt(Integer.MAX_VALUE));

                assertEquals(j, queue.lastInt());
                assertEquals(j, queue.lastInt(Integer.MAX_VALUE));

                assertEquals(j, queue.dequeueLastInt());
                assertEquals(-j, queue.dequeueLastInt(Integer.MAX_VALUE));
            }

            assertEquals(0, queue.size());
        }
    }

    @Test
    public void testBothDirection() {
        IntFixedArrayFIFOQueue queue = new IntFixedArrayFIFOQueue(32);

        for (int i = 0; i < queue.capacity(); i++) {
            final int lim = queue.capacity() / 2;

            assertEquals(0, queue.size());

            for (int j = 0; j < lim; j++) {
                assertTrue(queue.enqueue(j));
                assertTrue(queue.enqueueFirst(-j));

                assertEquals(-j, queue.firstInt());
                assertEquals(-j, queue.firstInt(Integer.MAX_VALUE));

                assertEquals(j, queue.lastInt());
                assertEquals(j, queue.lastInt(Integer.MAX_VALUE));
            }

            assertEquals(lim * 2, queue.size());


            for (int j = lim - 1; j >= 0; j--) {
                assertEquals(-j, queue.firstInt());
                assertEquals(-j, queue.firstInt(Integer.MAX_VALUE));

                assertEquals(j, queue.lastInt());
                assertEquals(j, queue.lastInt(Integer.MAX_VALUE));

                assertEquals(j, queue.dequeueLastInt());
                assertEquals(-j, queue.dequeueInt(Integer.MAX_VALUE));
            }

            assertEquals(0, queue.size());
        }
    }

    @Test
    public void testMixedDirection() {
        IntFixedArrayFIFOQueue queue = new IntFixedArrayFIFOQueue(32);

        for (int i = 0; i < queue.capacity(); i++) {
            final int lim = queue.capacity() / 2;

            assertEquals(0, queue.size());

            for (int j = 0; j < lim; j++) {
                assertTrue(queue.enqueue(j));
                assertTrue(queue.enqueue(-j));

                assertEquals(0, queue.firstInt());
                assertEquals(0, queue.firstInt(Integer.MAX_VALUE));

                assertEquals(-j, queue.lastInt());
                assertEquals(-j, queue.lastInt(Integer.MAX_VALUE));
            }

            assertEquals(lim * 2, queue.size());


            for (int j = lim - 1; j >= 0; j--) {
                assertEquals(0, queue.firstInt());
                assertEquals(0, queue.firstInt(Integer.MAX_VALUE));

                assertEquals(-j, queue.lastInt());
                assertEquals(-j, queue.lastInt(Integer.MAX_VALUE));

                assertEquals(-j, queue.dequeueLastInt());
                assertEquals(j, queue.dequeueLastInt(Integer.MAX_VALUE));
            }

            assertEquals(0, queue.size());
        }
    }

    @Test
    public void testMixedDirection2() {
        IntFixedArrayFIFOQueue queue = new IntFixedArrayFIFOQueue(32);

        for (int i = 0; i < queue.capacity(); i++) {
            final int lim = queue.capacity() / 2;

            assertEquals(0, queue.size());

            for (int j = 0; j < lim; j++) {
                assertTrue(queue.enqueueFirst(j));
                assertTrue(queue.enqueueFirst(-j));

                assertEquals(-j, queue.firstInt());
                assertEquals(-j, queue.firstInt(Integer.MAX_VALUE));

                assertEquals(0, queue.lastInt());
                assertEquals(0, queue.lastInt(Integer.MAX_VALUE));
            }

            assertEquals(lim * 2, queue.size());


            for (int j = lim - 1; j >= 0; j--) {
                assertEquals(-j, queue.firstInt());
                assertEquals(-j, queue.firstInt(Integer.MAX_VALUE));

                assertEquals(0, queue.lastInt());
                assertEquals(0, queue.lastInt(Integer.MAX_VALUE));

                assertEquals(-j, queue.dequeueInt());
                assertEquals(j, queue.dequeueInt(Integer.MAX_VALUE));
            }

            assertEquals(0, queue.size());
        }
    }

    @Test
    public void testClean() {
        IntFixedArrayFIFOQueue queue = new IntFixedArrayFIFOQueue(16);

        assertEquals(16, queue.capacity());
        assertEquals(0, queue.size());

        for (int i = 0; i < queue.capacity(); i++) {
            assertTrue(queue.enqueue(i + queue.capacity()));
        }

        assertEquals(16, queue.capacity());
        assertEquals(16, queue.size());

        queue.clear();

        assertEquals(16, queue.capacity());
        assertEquals(0, queue.size());

        for (int i = 0; i < queue.capacity(); i++) {
            assertTrue(queue.enqueue(i));
        }

        for (int i = 0; i < queue.capacity(); i++) {
            assertEquals(i, queue.dequeueInt());
        }
    }
}
