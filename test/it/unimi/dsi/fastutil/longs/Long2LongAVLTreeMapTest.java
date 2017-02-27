package it.unimi.dsi.fastutil.longs;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.junit.Test;

import it.unimi.dsi.fastutil.objects.ObjectSortedSet;

public class Long2LongAVLTreeMapTest {
	@Test
	public void testSubSet() {
		final Long2LongAVLTreeMap long2LongAVLTreeMap =
				new Long2LongAVLTreeMap();
		final Long2ObjectAVLTreeMap<Long> long2ObjectAVLTreeMap =
				new Long2ObjectAVLTreeMap<>();
		testSubSet(long2LongAVLTreeMap, long2ObjectAVLTreeMap);
	}

	public void testSubSet(AbstractLong2LongSortedMap long2LongAVLTreeMap, AbstractLong2ObjectSortedMap long2ObjectAVLTreeMap) {
		final TreeMap<Long, Long> treeMap = new TreeMap<>();
		final long[] input = {1, 2, 3, 4, 5};
		for (long l : input) {
		treeMap.put(l, l);
		long2LongAVLTreeMap.put(l, l);
		long2ObjectAVLTreeMap.put(l, (Long) l);
	}

	for (long fromKey = 0; fromKey < 7; fromKey++) {
	  for (long toKey = fromKey; toKey < 8; toKey++) {

	    // SubMap
	    final Long2LongSortedMap long2LongSubmap = long2LongAVLTreeMap.subMap(fromKey, toKey);
	    final Long2ObjectSortedMap<Long> long2ObjectSubmap = long2ObjectAVLTreeMap.subMap(fromKey, toKey);
	    final NavigableMap<Long, Long> treeSubmap = treeMap.subMap(fromKey, true, toKey, false);
	    compare(long2LongSubmap, long2ObjectSubmap, treeSubmap);

	    // Head
	    final Long2LongSortedMap long2LongHead = long2LongAVLTreeMap.headMap(fromKey);
	    final Long2ObjectSortedMap<Long> long2ObjectHead = long2ObjectAVLTreeMap.headMap(fromKey);
	    final NavigableMap<Long, Long> treeHead = treeMap.headMap(fromKey, false);
	    compare(long2LongHead, long2ObjectHead, treeHead);

	    // Tail
	    final Long2LongSortedMap long2LongTail = long2LongAVLTreeMap.tailMap(toKey);
	    final Long2ObjectSortedMap<Long> long2ObjectTail = long2ObjectAVLTreeMap.tailMap(toKey);
	    final NavigableMap<Long, Long> treeTail = treeMap.tailMap(toKey, true);
	    compare(long2LongTail, long2ObjectTail, treeTail);

	    // Subset Subset/Had/Tail
	    if (treeSubmap.isEmpty()) continue;
	    final ObjectSortedSet<Map.Entry<Long, Long>> long2LongEntries = long2LongSubmap.entrySet();
	    final ObjectSortedSet<Map.Entry<Long, Long>> long2ObjectEntries = long2ObjectSubmap.entrySet();

	    final Map.Entry<Long, Long> firstL = long2LongEntries.first();
	    final Map.Entry<Long, Long> lastL = long2LongEntries.last();
	    assertNotNull(firstL);
	    assertNotNull(lastL);

	    final Map.Entry<Long, Long> firstO = long2ObjectEntries.first();
	    final Map.Entry<Long, Long> lastO = long2ObjectEntries.last();
	    assertNotNull(firstL);
	    assertNotNull(lastL);

	    // Subset
	    final List<Long> ss1 = long2LongEntries.subSet(firstL, lastL)
	      .stream().map(kv -> kv.getKey()).collect(Collectors.toList());
	    final List<Long> ss2 = long2ObjectEntries.subSet(firstO, lastO)
	      .stream().map(kv -> kv.getKey()).collect(Collectors.toList());
	    assertEquals(ss1, ss2);

	    // Head
	    final List<Long> h1 = long2LongEntries.headSet(firstL)
	      .stream().map(kv -> kv.getKey()).collect(Collectors.toList());
	    final List<Long> h2 = long2ObjectEntries.headSet(firstO)
	      .stream().map(kv -> kv.getKey()).collect(Collectors.toList());
	    assertEquals(h1, h2);

	    // Tail
	    final List<Long> t1 = long2LongEntries.tailSet(firstL)
	      .stream().map(kv -> kv.getKey()).collect(Collectors.toList());
	    final List<Long> t2 = long2ObjectEntries.tailSet(lastO)
	      .stream().map(kv -> kv.getKey()).collect(Collectors.toList());
	    assertEquals(t1, t2);}}
	}

	private void compare(Long2LongSortedMap long2LongAVLTreeMap, SortedMap<Long, Long> long2ObjectAVLTreeMap, NavigableMap<Long, Long> treeMap) {
		List<Long> list1 = new ArrayList<>(long2LongAVLTreeMap.keySet());
		List<Long> list2 = new ArrayList<>(long2ObjectAVLTreeMap.keySet());
		List<Long> list3 = new ArrayList<>(treeMap.keySet());
		assertEquals(list1, list2);
		assertEquals(list2, list3);
	}

}
