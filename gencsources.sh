#!/bin/bash

#
# This script generates from driver files fake C sources to be passed
# through a C preprocessor to get the actual Java sources.
#

DIR="src/it/unimi/dsi/fastUtil"

# Driver files for maps.
MAP=(Map SortedMap AbstractMap AVLTreeMap RBTreeMap OpenHashMap)

# Driver files for sets.
SET=(AbstractCollection AbstractSet Collection Set SortedSet OpenHashSet AVLTreeSet RBTreeSet)

# Driver files for interfaces.
INTERFACE=(Comparator AbstractComparator Iterator ListIterator BidirectionalIterator)

# The primitive types we specialize to.
TYPE=(boolean byte short int long char float double Object Object)

# The same types, but capitalized (to build method names).
TYPE_CAP=(Boolean Byte Short Int Long Char Float Double Object Reference)

# The corresponding classes (in few cases, there are differences with $TYPE_CAP).
CLASS=(Boolean Byte Short Integer Long Character Float Double Object Reference)

#
# This loop generates maps. Note that the index in the key
# array starts from 1, so we avoid boolean keys, and that
# the index in the value array stops one item before the
# end, so we avoid reference values.
#

for ((f=0; f<${#MAP[*]}; f++)); do
	l=${#TYPE[*]}
	if [[ ${MAP[$f]} != "Map" 
		&& ${MAP[$f]} != "AbstractMap" 
		&& ${MAP[$f]} != "OpenHashMap" ]]; then l=$((l-1)); fi # Only hash maps may have reference keys.
	for ((k=1; k<l; k++)); do
		for ((v=0; v<${#TYPE[*]}; v++)); do
			FILENAME=$DIR/${TYPE_CAP[$k]}2${TYPE_CAP[$v]}${MAP[$f]}.c
			rm -f $FILENAME
			echo -e \
"#assert keyclass(${CLASS[$k]})\n"\
"#assert valueclass(${CLASS[$v]})\n"\
"#define KEY_TYPE ${TYPE[$k]}\n"\
"#define KEY_CLASS ${CLASS[$k]}\n"\
"#define VALUE_TYPE ${TYPE[$v]}\n"\
"#define VALUE_CLASS ${CLASS[$v]}\n"\
"#define KEY_VALUE ${TYPE[$k]}Value\n"\
"#define VALUE_VALUE ${TYPE[$v]}Value\n"\
"#define SORTEDSET ${TYPE_CAP[$k]}SortedSet\n\n"\
"#define MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Map\n"\
"#define SORTEDMAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}SortedMap\n"\
"#define ABSTRACT_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}AbstractMap\n"\
"#define KEY_ABSTRACT_SET ${TYPE_CAP[$k]}AbstractSet\n\n"\
"#define VALUE_ABSTRACT_COLLECTION ${TYPE_CAP[$v]}AbstractCollection\n\n"\
"#define OPENHASHMAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}OpenHashMap\n\n"\
"#define AVLTREEMAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}AVLTreeMap\n\n"\
"#define RBTREEMAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}RBTreeMap\n\n"\
"#if #keyclass(Object) || #keyclass(Reference)\n"\
"#define WRITE_KEY writeObject\n"\
"#define READ_KEY readObject\n"\
"#define KEY_ITERATOR Iterator\n\n"\
"#define KEY_LIST_ITERATOR ListIterator\n\n"\
"#define KEY_BIDI_ITERATOR BidirectionalIterator\n\n"\
"#define KEY_COMPARATOR Comparator\n\n"\
"#define NEXT_KEY next\n"\
"#define PREV_KEY previous\n"\
"#define FIRST_KEY firstKey\n"\
"#define LAST_KEY lastKey\n"\
"#define KEY2TYPE(x) (x)\n"\
"#define KEY2OBJ(x) (x)\n"\
"#define ENTRY_GET_KEY getKey\n"\
"#define KEY_NULL (null)\n"\
"#define KEY2INT(x) (x == null ? 0 : x.hashCode())\n"\
"#define GET_VALUE get${TYPE_CAP[$v]}\n"\
"#define REMOVE_VALUE remove${TYPE_CAP[$v]}\n"\
"#define KEY_CMP(x,y) (((Comparable)(x)).compareTo(y))\n"\
"#else\n"\
"#define WRITE_KEY write${TYPE_CAP[$k]}\n"\
"#define READ_KEY read${TYPE_CAP[$k]}\n"\
"#define KEY_ITERATOR ${TYPE_CAP[$k]}Iterator\n\n"\
"#define KEY_LIST_ITERATOR ${TYPE_CAP[$k]}ListIterator\n\n"\
"#define KEY_BIDI_ITERATOR ${TYPE_CAP[$k]}BidirectionalIterator\n\n"\
"#define KEY_COMPARATOR ${TYPE_CAP[$k]}Comparator\n\n"\
"#define NEXT_KEY next${TYPE_CAP[$k]}\n"\
"#define PREV_KEY previous${TYPE_CAP[$k]}\n"\
"#define FIRST_KEY first${TYPE_CAP[$k]}Key\n"\
"#define LAST_KEY last${TYPE_CAP[$k]}Key\n"\
"#define KEY2TYPE(x) (((KEY_CLASS)(x)).KEY_VALUE())\n"\
"#define KEY2OBJ(x) (new KEY_CLASS(x))\n"\
"#define ENTRY_GET_KEY get${TYPE_CAP[$k]}Key\n"\
"#if #keyclass(Boolean)\n"\
"#define KEY_NULL (false)\n"\
"#else\n"\
"#define KEY_NULL ((KEY_TYPE)0)\n"\
"#endif\n"\
"#if #keyclass(Float) || #keyclass(Double) || #keyclass(Long)\n"\
"#define KEY2INT(x) HashCommon.${TYPE[$k]}2int(x)\n"\
"#elif #keyclass(Boolean)\n"\
"#define KEY2INT(x) (x ? 1231 : 1237)\n"\
"#else\n"\
"#define KEY2INT(x) ((int)(x))\n"\
"#endif\n"\
"#define GET_VALUE get\n"\
"#define REMOVE_VALUE remove\n"\
"#define FIRST first${TYPE_CAP[$k]}\n"\
"#define LAST last${TYPE_CAP[$k]}\n"\
"#define KEY_CMP(x,y) ( (x) < (y) ? -1 : ( (x) == (y) ? 0 : 1 ) )\n"\
"#endif\n"\
"#if #keyclass(Object)\n"\
"#define KEY_EQUAL_HASH(x,h,y) ((x) == (y) || ((y) != null && h == (y).hashCode() && (y).equals((x))))\n"\
"#define KEY_EQUAL(x,y) ((x) == (y) || ((x) != null && (x).equals((y))))\n"\
"#else\n"\
"#define KEY_EQUAL_HASH(x,h,y) ((x) == (y))\n"\
"#define KEY_EQUAL(x,y) ((x) == (y))\n"\
"#endif\n\n"\
"#if #valueclass(Object) || #valueclass(Reference)\n"\
"#define WRITE_VALUE writeObject\n"\
"#define READ_VALUE readObject\n"\
"#define VALUE2TYPE(x) (x)\n"\
"#define VALUE2OBJ(x) (x)\n"\
"#define ENTRY_GET_VALUE getValue\n"\
"#define VALUE_NULL (null)\n"\
"#define OBJDEFRETVALUE (this.defRetValue)\n"\
"#define VALUE2INT(x) (x == null ? 0 : x.hashCode())\n"\
"#define VALUE_ITERATOR Iterator\n\n"\
"#define VALUE_LIST_ITERATOR ListIterator\n\n"\
"#define NEXT_VALUE next\n"\
"#define PREV_VALUE previous\n"\
"#else\n"\
"#define WRITE_VALUE write${TYPE_CAP[$v]}\n"\
"#define READ_VALUE read${TYPE_CAP[$v]}\n"\
"#define VALUE2TYPE(x) (((VALUE_CLASS)(x)).VALUE_VALUE())\n"\
"#define VALUE2OBJ(x) (new VALUE_CLASS(x))\n"\
"#define ENTRY_GET_VALUE get${TYPE_CAP[$v]}Value\n"\
"#if #valueclass(Float) || #valueclass(Double) || #valueclass(Long)\n"\
"#define VALUE2INT(x) HashCommon.${TYPE[$v]}2int(x)\n"\
"#elif #valueclass(Boolean)\n"\
"#define VALUE2INT(x) ((x) ? 1 : 0)\n"\
"#else\n"\
"#define VALUE2INT(x) ((int)(x))\n"\
"#endif\n"\
"#define VALUE_ITERATOR ${TYPE_CAP[$v]}Iterator\n\n"\
"#define VALUE_LIST_ITERATOR ${TYPE_CAP[$v]}ListIterator\n\n"\
"#define NEXT_VALUE next${TYPE_CAP[$v]}\n"\
"#define PREV_VALUE previous${TYPE_CAP[$v]}\n"\
"#if #valueclass(Boolean)\n"\
"#define VALUE_NULL (false)\n"\
"#else\n"\
"#define VALUE_NULL ((VALUE_TYPE)0)\n"\
"#endif\n"\
"#define OBJDEFRETVALUE (null)\n"\
"#endif\n"\
"#if #valueclass(Object)\n"\
"#define VALUE_EQUAL(x,y) ((x) == null ? (y) == null : (x).equals((y)))\n"\
"#else\n"\
"#define VALUE_EQUAL(x,y) ((x) == (y))\n"\
"#endif\n\n"\
"#include \"${MAP[$f]}.drv\"\n" >$FILENAME
			 done
	  done
done


#
# This loop generates sets. Note that the index in the key
# array starts from 0, because we need boolean Collections, 
# so we manually delete boolean sets definitions later.
#

for ((f=0; f<${#SET[*]}; f++)); do
	l=${#TYPE[*]}
	if [[ ${SET[$f]} != "Collection" 
		&& ${SET[$f]} != "AbstractCollection" 
		&& ${SET[$f]} != "Set" 
		&& ${SET[$f]} != "AbstractSet" 
		&& ${SET[$f]} != "OpenHashSet" ]]; then l=$((l-1)); fi # Only hash sets may have reference keys.
	for ((k=0; k<l; k++)); do
		 FILENAME=$DIR/${TYPE_CAP[$k]}${SET[$f]}.c
		 rm -f $FILENAME
		 echo -e \
"#assert keyclass(${CLASS[$k]})\n"\
"#define KEY_TYPE ${TYPE[$k]}\n"\
"#define KEY_CLASS ${CLASS[$k]}\n"\
"#define KEY_VALUE ${TYPE[$k]}Value\n"\
"#define SET ${TYPE_CAP[$k]}Set\n\n"\
"#define SORTEDSET ${TYPE_CAP[$k]}SortedSet\n\n"\
"#define COLLECTION ${TYPE_CAP[$k]}Collection\n\n"\
"#define ABSTRACT_SET ${TYPE_CAP[$k]}AbstractSet\n\n"\
"#define ABSTRACT_COLLECTION ${TYPE_CAP[$k]}AbstractCollection\n\n"\
"#define OPENHASHSET ${TYPE_CAP[$k]}OpenHashSet\n\n"\
"#define AVLTREESET ${TYPE_CAP[$k]}AVLTreeSet\n\n"\
"#define RBTREESET ${TYPE_CAP[$k]}RBTreeSet\n\n"\
"#if #keyclass(Object) || #keyclass(Reference)\n"\
"#define WRITE_KEY writeObject\n"\
"#define READ_KEY readObject\n"\
"#define KEY_ITERATOR Iterator\n\n"\
"#define KEY_LIST_ITERATOR ListIterator\n\n"\
"#define KEY_BIDI_ITERATOR BidirectionalIterator\n\n"\
"#define KEY_COMPARATOR Comparator\n\n"\
"#define NEXT_KEY next\n"\
"#define PREV_KEY previous\n"\
"#define FIRST first\n"\
"#define LAST last\n"\
"#define KEY2TYPE(x) (x)\n"\
"#define KEY2OBJ(x) (x)\n"\
"#define ENTRY_GET_KEY getKey\n"\
"#define TO_KEY_ARRAY toArray\n"\
"#define KEY_NULL (null)\n"\
"#define KEY2INT(x) (x == null ? 0 : x.hashCode())\n"\
"#define KEY_CMP(x,y) (((Comparable)(x)).compareTo(y))\n"\
"#else\n"\
"#define WRITE_KEY write${TYPE_CAP[$k]}\n"\
"#define READ_KEY read${TYPE_CAP[$k]}\n"\
"#define KEY_ITERATOR ${TYPE_CAP[$k]}Iterator\n\n"\
"#define KEY_LIST_ITERATOR ${TYPE_CAP[$k]}ListIterator\n\n"\
"#define KEY_BIDI_ITERATOR ${TYPE_CAP[$k]}BidirectionalIterator\n\n"\
"#define KEY_COMPARATOR ${TYPE_CAP[$k]}Comparator\n\n"\
"#define NEXT_KEY next${TYPE_CAP[$k]}\n"\
"#define PREV_KEY previous${TYPE_CAP[$k]}\n"\
"#define FIRST first${TYPE_CAP[$k]}\n"\
"#define LAST last${TYPE_CAP[$k]}\n"\
"#define KEY2TYPE(x) (((KEY_CLASS)(x)).KEY_VALUE())\n"\
"#define KEY2OBJ(x) (new KEY_CLASS(x))\n"\
"#define ENTRY_GET_KEY get${TYPE_CAP[$k]}Key\n"\
"#define TO_KEY_ARRAY to${TYPE_CAP[$k]}Array\n"\
"#if #keyclass(Boolean)\n"\
"#define KEY_NULL (false)\n"\
"#else\n"\
"#define KEY_NULL ((KEY_TYPE)0)\n"\
"#endif\n"\
"#if #keyclass(Float) || #keyclass(Double) || #keyclass(Long)\n"\
"#define KEY2INT(x) HashCommon.${TYPE[$k]}2int(x)\n"\
"#elif #keyclass(Boolean)\n"\
"#define KEY2INT(x) ((x) ? 1231 : 1237)\n"\
"#else\n"\
"#define KEY2INT(x) ((int)(x))\n"\
"#endif\n"\
"#define KEY_CMP(x,y) ( (x) < (y) ? -1 : ( (x) == (y) ? 0 : 1 ) )\n"\
"#endif\n"\
"#if #keyclass(Object)\n"\
"#define KEY_EQUAL_HASH(x,h,y) ((x) == (y) || ((y) != null && h == (y).hashCode() && (y).equals((x))))\n"\
"#define KEY_EQUAL(x,y) ((x) == (y) || ((x) != null && (x).equals((y))))\n"\
"#else\n"\
"#define KEY_EQUAL_HASH(x,h,y) ((x) == (y))\n"\
"#define KEY_EQUAL(x,y) ((x) == (y))\n"\
"#endif\n\n"\
"#include \"${SET[$f]}.drv\"\n" >$FILENAME
	  done
done


#
# This loop generates iterator and comparator interfaces. Note that we need
# boolean iterators for maps with booleans as codomain.
#

t=${#INTERFACE[*]}
for ((f=0; f<t; f++)); do
	 for ((k=0; k<$((${#TYPE[*]}-2)); k++)); do
					 FILENAME=$DIR/${TYPE_CAP[$k]}${INTERFACE[$f]}.c
					 rm -f $FILENAME
					 echo -e \
"#assert keyclass(${CLASS[$k]})\n"\
"#define KEY_TYPE ${TYPE[$k]}\n"\
"#define KEY_CLASS ${CLASS[$k]}\n"\
"#define NEXT_KEY next${TYPE_CAP[$k]}\n"\
"#define PREV_KEY previous${TYPE_CAP[$k]}\n"\
"#define KEY_ITERATOR ${TYPE_CAP[$k]}Iterator\n\n"\
"#define KEY_LIST_ITERATOR ${TYPE_CAP[$k]}ListIterator\n\n"\
"#define KEY_BIDI_ITERATOR ${TYPE_CAP[$k]}BidirectionalIterator\n\n"\
"#define KEY_COMPARATOR ${TYPE_CAP[$k]}Comparator\n\n"\
"#define KEY_ABSTRACT_COMPARATOR ${TYPE_CAP[$k]}AbstractComparator\n\n"\
"#define KEY_VALUE ${TYPE[$k]}Value\n"\
"#define KEY_CLASS ${CLASS[$k]}\n"\
"#define KEY2TYPE(x) (((KEY_CLASS)(x)).KEY_VALUE())\n"\
"#include \"${INTERFACE[$f]}.drv\"\n" >$FILENAME

	  done
done

rm -f $DIR/BooleanAbstractSet.c
rm -f $DIR/BooleanSortedSet.c
rm -f $DIR/BooleanSet.c
rm -f $DIR/BooleanComparator.c
rm -f $DIR/BooleanAbstractComparator.c
rm -f $DIR/BooleanOpenHashSet.c
rm -f $DIR/BooleanAVLTreeSet.c
rm -f $DIR/BooleanRBTreeSet.c

