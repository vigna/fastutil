#!/bin/bash

#
# This script generates from driver files fake C sources to be passed
# through a C preprocessor to get the actual Java sources.
#

# Generate links for fake source files.
ln -sf OpenHashSet.drv LinkedOpenHashSet.drv
ln -sf OpenHashMap.drv LinkedOpenHashMap.drv

DIR="java/it/unimi/dsi/fastutil"

# Driver files for maps.
MAP=(Map SortedMap AbstractMap AVLTreeMap RBTreeMap OpenHashMap LinkedOpenHashMap)

# Driver files for lists.
LIST=(List AbstractList Stack AbstractStack ArrayList)

# Driver files for sets.
SET=(AbstractCollection AbstractSet Collection Set SortedSet OpenHashSet LinkedOpenHashSet AVLTreeSet RBTreeSet)

# Driver files for interfaces.
INTERFACE=(Comparator AbstractComparator Iterator Iterators-Fragment AbstractIterator ListIterator AbstractListIterator BidirectionalIterator AbstractBidirectionalIterator)

# The types we specialise to (these are actual Java types, so references appear here as Object).
TYPE=(boolean byte short int long char float double Object Object)

# The same types, but in lower case and plural (to build package names; singular forms are reserved keywords).
TYPE_PACK=(booleans bytes shorts ints longs chars floats doubles objects objects)

# The capitalised types used to build class and method names (now references appear as Reference).
TYPE_CAP=(Boolean Byte Short Int Long Char Float Double Object Reference)

# The downcased types used to build method names.
TYPE_LC=(boolean byte short int long char float double object reference)

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
		&& ${MAP[$f]} != "SortedMap" 
		&& ${MAP[$f]} != "OpenHashMap" 
		&& ${MAP[$f]} != "LinkedOpenHashMap" ]]; then l=$((l-1)); fi # Only hash maps may have reference keys.
	for ((k=1; k<l; k++)); do
		for ((v=0; v<${#TYPE[*]}; v++)); do
			if [[ ${MAP[$f]:0:8} == "Abstract" ]]; then
			    FILENAME=$DIR/${TYPE_PACK[$k]}/Abstract${TYPE_CAP[$k]}2${TYPE_CAP[$v]}${MAP[$f]:8}.c
			else
			    FILENAME=$DIR/${TYPE_PACK[$k]}/${TYPE_CAP[$k]}2${TYPE_CAP[$v]}${MAP[$f]}.c
			fi
			rm -f $FILENAME
			if [[ ${MAP[$f]} == "LinkedOpenHashMap" ]]; then linked=linked; else linked=unlinked; fi
			echo -e \
"#define $linked\n"\
"#assert keyclass(${CLASS[$k]})\n"\
"#assert valueclass(${CLASS[$v]})\n"\
"#define PACKAGE it.unimi.dsi.fastutil.${TYPE_PACK[$k]}\n"\
"#define IMPORT_VALUES import it.unimi.dsi.fastutil.${TYPE_PACK[$v]}.*\n"\
"#define KEY_TYPE ${TYPE[$k]}\n"\
"#define KEY_CLASS ${CLASS[$k]}\n"\
"#define VALUE_TYPE ${TYPE[$v]}\n"\
"#define VALUE_CLASS ${CLASS[$v]}\n"\
"#define KEY_VALUE ${TYPE[$k]}Value\n"\
"#define VALUE_VALUE ${TYPE[$v]}Value\n"\
"#define SORTEDSET ${TYPE_CAP[$k]}SortedSet\n\n"\
"#define MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Map\n"\
"#define SORTEDMAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}SortedMap\n"\
"#define ABSTRACT_MAP Abstract${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Map\n"\
"#define KEY_ABSTRACT_SET Abstract${TYPE_CAP[$k]}Set\n\n"\
"#define VALUE_ABSTRACT_COLLECTION Abstract${TYPE_CAP[$v]}Collection\n\n"\
"#ifdef linked\n"\
"#define OPENHASHMAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}LinkedOpenHashMap\n\n"\
"#else\n"\
"#define OPENHASHMAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}OpenHashMap\n\n"\
"#endif\n"\
"#define AVLTREEMAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}AVLTreeMap\n\n"\
"#define RBTREEMAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}RBTreeMap\n\n"\
"#if #keyclass(Object) || #keyclass(Reference)\n"\
"#define STDSORTEDSET SortedSet\n\n"\
"#define STDSORTEDMAP SortedMap\n\n"\
"#define WRITE_KEY writeObject\n"\
"#define READ_KEY readObject\n"\
"#define KEY_COMPARATOR Comparator\n\n"\
"#define KEY_ITERATOR ObjectIterator\n\n"\
"#define KEY_ITERATOR_METHOD objectIterator\n\n"\
"#define KEY_ITERATOR ObjectIterator\n\n"\
"#define KEY_ABSTRACT_ITERATOR AbstractObjectIterator\n\n"\
"#define KEY_LIST_ITERATOR ObjectListIterator\n\n"\
"#define KEY_BIDI_ITERATOR ObjectBidirectionalIterator\n\n"\
"#define NEXT_KEY next\n"\
"#define PREV_KEY previous\n"\
"#define FIRST_KEY firstKey\n"\
"#define LAST_KEY lastKey\n"\
"#define FIRST first\n"\
"#define LAST last\n"\
"#define KEY2TYPE(x) (x)\n"\
"#define KEY2OBJ(x) (x)\n"\
"#define ENTRY_GET_KEY getKey\n"\
"#define KEY_NULL (null)\n"\
"#if #keyclass(Object)\n"\
"#define KEY2INT(x) (x == null ? 0 : x.hashCode())\n"\
"#else\n"\
"#define KEY2INT(x) (System.identityHashCode(x))\n"\
"#endif\n"\
"#if #valueclass(Object) || #valueclass(Reference)\n"\
"#define GET_VALUE get\n"\
"#define REMOVE_VALUE remove\n"\
"#else\n"\
"#define GET_VALUE get${TYPE_CAP[$v]}\n"\
"#define REMOVE_VALUE remove${TYPE_CAP[$v]}\n"\
"#endif\n"\
"#define KEY_CMP(x,y) (((Comparable)(x)).compareTo(y))\n"\
"#else\n"\
"#define STDSORTEDSET SORTEDSET\n\n"\
"#define STDSORTEDMAP SORTEDMAP\n\n"\
"#define WRITE_KEY write${TYPE_CAP[$k]}\n"\
"#define READ_KEY read${TYPE_CAP[$k]}\n"\
"#define KEY_COMPARATOR ${TYPE_CAP[$k]}Comparator\n\n"\
"#define KEY_ITERATOR ${TYPE_CAP[$k]}Iterator\n\n"\
"#define KEY_ABSTRACT_ITERATOR Abstract${TYPE_CAP[$k]}Iterator\n\n"\
"#define KEY_ITERATOR_METHOD ${TYPE[$k]}Iterator\n\n"\
"#define KEY_ITERATOR ${TYPE_CAP[$k]}Iterator\n\n"\
"#define KEY_LIST_ITERATOR ${TYPE_CAP[$k]}ListIterator\n\n"\
"#define KEY_BIDI_ITERATOR ${TYPE_CAP[$k]}BidirectionalIterator\n\n"\
"#define NEXT_KEY next${TYPE_CAP[$k]}\n"\
"#define PREV_KEY previous${TYPE_CAP[$k]}\n"\
"#define FIRST_KEY first${TYPE_CAP[$k]}Key\n"\
"#define LAST_KEY last${TYPE_CAP[$k]}Key\n"\
"#define FIRST first${TYPE_CAP[$k]}\n"\
"#define LAST last${TYPE_CAP[$k]}\n"\
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
"#define VALUE_ITERATOR ObjectIterator\n\n"\
"#define VALUE_ITERATOR_METHOD objectIterator\n\n"\
"#define VALUE_LIST_ITERATOR ObjectListIterator\n\n"\
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
"#define VALUE_ITERATOR_METHOD ${TYPE[$v]}Iterator\n\n"\
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
"#if #keyclass(Integer)\n"\
"#define REMOVE rem\n"\
"#else\n"\
"#define REMOVE remove\n"\
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
		&& ${SET[$f]} != "SortedSet" 
		&& ${SET[$f]} != "AbstractSet" 
		&& ${SET[$f]} != "OpenHashSet" 
		&& ${SET[$f]} != "LinkedOpenHashSet" ]]; then l=$((l-1)); fi # Only hash sets may have reference keys.
	for ((k=0; k<l; k++)); do
		if [[ ${SET[$f]:0:8} == "Abstract" ]]; then
		    FILENAME=$DIR/${TYPE_PACK[$k]}/Abstract${TYPE_CAP[$k]}${SET[$f]:8}.c
		else
		    FILENAME=$DIR/${TYPE_PACK[$k]}/${TYPE_CAP[$k]}${SET[$f]}.c
		fi
		 rm -f $FILENAME
		 if [[ ${SET[$f]} == "LinkedOpenHashSet" ]]; then linked=linked; else linked=unlinked; fi
		 echo -e \
"#define $linked\n"\
"#assert keyclass(${CLASS[$k]})\n"\
"#define PACKAGE it.unimi.dsi.fastutil.${TYPE_PACK[$k]}\n"\
"#define KEY_TYPE ${TYPE[$k]}\n"\
"#define KEY_CLASS ${CLASS[$k]}\n"\
"#define KEY_VALUE ${TYPE[$k]}Value\n"\
"#define SET ${TYPE_CAP[$k]}Set\n\n"\
"#define SORTEDSET ${TYPE_CAP[$k]}SortedSet\n\n"\
"#define COLLECTION ${TYPE_CAP[$k]}Collection\n\n"\
"#define ABSTRACT_SET Abstract${TYPE_CAP[$k]}Set\n\n"\
"#define ABSTRACT_COLLECTION Abstract${TYPE_CAP[$k]}Collection\n\n"\
"#ifdef linked\n"\
"#define OPENHASHSET ${TYPE_CAP[$k]}LinkedOpenHashSet\n\n"\
"#else\n"\
"#define OPENHASHSET ${TYPE_CAP[$k]}OpenHashSet\n\n"\
"#endif\n"\
"#define AVLTREESET ${TYPE_CAP[$k]}AVLTreeSet\n\n"\
"#define RBTREESET ${TYPE_CAP[$k]}RBTreeSet\n\n"\
"#if #keyclass(Object) || #keyclass(Reference)\n"\
"#define STDSORTEDSET SortedSet\n\n"\
"#define WRITE_KEY writeObject\n"\
"#define READ_KEY readObject\n"\
"#define KEY_COMPARATOR Comparator\n\n"\
"#define KEY_ITERATOR ObjectIterator\n\n"\
"#define KEY_ABSTRACT_ITERATOR AbstractObjectIterator\n\n"\
"#define KEY_ITERATOR_METHOD objectIterator\n\n"\
"#define AS_KEY_ITERATOR asObjectIterator\n\n"\
"#define KEY_LIST_ITERATOR ObjectListIterator\n\n"\
"#define KEY_ABSTRACT_LIST_ITERATOR AbstractObjectListIterator\n\n"\
"#define KEY_BIDI_ITERATOR ObjectBidirectionalIterator\n\n"\
"#define KEY_ABSTRACT_BIDI_ITERATOR AbstractObjectBidirectionalIterator\n\n"\
"#define NEXT_KEY next\n"\
"#define PREV_KEY previous\n"\
"#define FIRST first\n"\
"#define LAST last\n"\
"#define KEY2TYPE(x) (x)\n"\
"#define KEY2OBJ(x) (x)\n"\
"#define ENTRY_GET_KEY getKey\n"\
"#define TO_KEY_ARRAY toArray\n"\
"#define KEY_NULL (null)\n"\
"#if #keyclass(Object)\n"\
"#define KEY2INT(x) (x == null ? 0 : x.hashCode())\n"\
"#else\n"\
"#define KEY2INT(x) (System.identityHashCode(x))\n"\
"#endif\n"\
"#define KEY_CMP(x,y) (((Comparable)(x)).compareTo(y))\n"\
"#else\n"\
"#define STDSORTEDSET SORTEDSET\n\n"\
"#define WRITE_KEY write${TYPE_CAP[$k]}\n"\
"#define READ_KEY read${TYPE_CAP[$k]}\n"\
"#define KEY_COMPARATOR ${TYPE_CAP[$k]}Comparator\n\n"\
"#define KEY_ITERATOR ${TYPE_CAP[$k]}Iterator\n\n"\
"#define KEY_ABSTRACT_ITERATOR Abstract${TYPE_CAP[$k]}Iterator\n\n"\
"#define KEY_ITERATOR_METHOD ${TYPE[$k]}Iterator\n\n"\
"#define AS_KEY_ITERATOR as${TYPE_CAP[$k]}Iterator\n\n"\
"#define KEY_LIST_ITERATOR ${TYPE_CAP[$k]}ListIterator\n\n"\
"#define KEY_ABSTRACT_LIST_ITERATOR Abstract${TYPE_CAP[$k]}ListIterator\n\n"\
"#define KEY_BIDI_ITERATOR ${TYPE_CAP[$k]}BidirectionalIterator\n\n"\
"#define KEY_ABSTRACT_BIDI_ITERATOR Abstract${TYPE_CAP[$k]}BidirectionalIterator\n\n"\
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
"#if #keyclass(Integer)\n"\
"#define REMOVE rem\n"\
"#else\n"\
"#define REMOVE remove\n"\
"#endif\n\n"\
"#include \"${SET[$f]}.drv\"\n" >$FILENAME
	  done
done

#
# This loop generates lists and stacks.
#

for ((f=0; f<${#LIST[*]}; f++)); do
	l=${#TYPE[*]}
	if [[ ${LIST[$f]} == "Stack" || ${LIST[$f]} == "AbstractStack" ]]; then l=$((l-2)); fi 

	for ((k=0; k<l; k++)); do
		if [[ ${LIST[$f]:0:8} == "Abstract" ]]; then
		    FILENAME=$DIR/${TYPE_PACK[$k]}/Abstract${TYPE_CAP[$k]}${LIST[$f]:8}.c
		else
		    FILENAME=$DIR/${TYPE_PACK[$k]}/${TYPE_CAP[$k]}${LIST[$f]}.c
		fi
		rm -f $FILENAME
		echo -e \
"#assert keyclass(${CLASS[$k]})\n"\
"#define PACKAGE it.unimi.dsi.fastutil.${TYPE_PACK[$k]}\n"\
"#define KEY_TYPE ${TYPE[$k]}\n"\
"#define KEY_CLASS ${CLASS[$k]}\n"\
"#define KEY_VALUE ${TYPE[$k]}Value\n"\
"#define COLLECTION ${TYPE_CAP[$k]}Collection\n\n"\
"#define ABSTRACT_COLLECTION Abstract${TYPE_CAP[$k]}Collection\n\n"\
"#define LIST ${TYPE_CAP[$k]}List\n\n"\
"#define SUBLIST ${TYPE_CAP[$k]}SubList\n"\
"#define SUBLIST_METHOD ${TYPE_LC[$k]}SubList\n"\
"#define ARRAY_LIST ${TYPE_CAP[$k]}ArrayList\n\n"\
"#define ABSTRACT_LIST Abstract${TYPE_CAP[$k]}List\n\n"\
"#if #keyclass(Object) || #keyclass(Reference)\n"\
"#define STACK Stack\n\n"\
"#define ABSTRACT_STACK AbstractStack\n\n"\
"#define STDARRAYLIST ArrayList\n\n"\
"#define REMOVE_KEY remove\n"\
"#define WRITE_KEY writeObject\n"\
"#define READ_KEY readObject\n"\
"#define GET_KEY get\n"\
"#define KEY_ITERATOR ObjectIterator\n\n"\
"#define KEY_ABSTRACT_ITERATOR AbstractObjectIterator\n\n"\
"#define KEY_ITERATOR_METHOD objectIterator\n\n"\
"#define KEY_LIST_ITERATOR ObjectListIterator\n\n"\
"#define KEY_LIST_ITERATOR_METHOD objectListIterator\n\n"\
"#define KEY_ABSTRACT_LIST_ITERATOR AbstractObjectListIterator\n\n"\
"#define KEY_BIDI_ITERATOR ObjectBidirectionalIterator\n\n"\
"#define KEY_ABSTRACT_BIDI_ITERATOR AbstractObjectBidirectionalIterator\n\n"\
"#define AS_KEY_ITERATOR asObjectIterator\n\n"\
"#define NEXT_KEY next\n"\
"#define PREV_KEY previous\n"\
"#define INDEXOF_KEY indexOf\n"\
"#define LASTINDEXOF_KEY lastIndexOf\n"\
"#define TOP top\n"\
"#define PEEK peek\n"\
"#define POP pop\n"\
"#define KEY2TYPE(x) (x)\n"\
"#define KEY2OBJ(x) (x)\n"\
"#define TO_KEY_ARRAY toArray\n"\
"#define KEY_NULL (null)\n"\
"#if #keyclass(Object)\n"\
"#define KEY2INT(x) (x == null ? 0 : x.hashCode())\n"\
"#else\n"\
"#define KEY2INT(x) (System.identityHashCode(x))\n"\
"#endif\n"\
"#define KEY_CMP(x,y) (((Comparable)(x)).compareTo(y))\n"\
"#else\n"\
"#define STACK ${TYPE_CAP[$k]}Stack\n\n"\
"#define ABSTRACT_STACK Abstract${TYPE_CAP[$k]}Stack\n\n"\
"#define STDARRAYLIST ARRAY_LIST\n\n"\
"#define REMOVE_KEY remove${TYPE_CAP[$k]}\n"\
"#define WRITE_KEY write${TYPE_CAP[$k]}\n"\
"#define READ_KEY read${TYPE_CAP[$k]}\n"\
"#define GET_KEY get${TYPE_CAP[$k]}\n"\
"#define KEY_ITERATOR ${TYPE_CAP[$k]}Iterator\n\n"\
"#define KEY_ABSTRACT_ITERATOR Abstract${TYPE_CAP[$k]}Iterator\n\n"\
"#define KEY_ITERATOR_METHOD ${TYPE[$k]}Iterator\n\n"\
"#define KEY_LIST_ITERATOR ${TYPE_CAP[$k]}ListIterator\n\n"\
"#define KEY_LIST_ITERATOR_METHOD ${TYPE[$k]}ListIterator\n\n"\
"#define KEY_ABSTRACT_LIST_ITERATOR Abstract${TYPE_CAP[$k]}ListIterator\n\n"\
"#define KEY_BIDI_ITERATOR ${TYPE_CAP[$k]}BidirectionalIterator\n\n"\
"#define KEY_ABSTRACT_BIDI_ITERATOR Abstract${TYPE_CAP[$k]}BidirectionalIterator\n\n"\
"#define AS_KEY_ITERATOR as${TYPE_CAP[$k]}Iterator\n\n"\
"#define NEXT_KEY next${TYPE_CAP[$k]}\n"\
"#define PREV_KEY previous${TYPE_CAP[$k]}\n"\
"#define INDEXOF_KEY indexOf${TYPE_CAP[$k]}\n"\
"#define LASTINDEXOF_KEY lastIndexOf${TYPE_CAP[$k]}\n"\
"#define TOP top${TYPE_CAP[$k]}\n"\
"#define PEEK peek${TYPE_CAP[$k]}\n"\
"#define POP pop${TYPE_CAP[$k]}\n"\
"#define KEY2TYPE(x) (((KEY_CLASS)(x)).KEY_VALUE())\n"\
"#define KEY2OBJ(x) (new KEY_CLASS(x))\n"\
"#define TO_KEY_ARRAY to${TYPE_CAP[$k]}Array\n"\
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
"#define KEY_CMP(x,y) ( (x) < (y) ? -1 : ( (x) == (y) ? 0 : 1 ) )\n"\
"#endif\n\n"\
"#if #keyclass(Object)\n"\
"#define KEY_EQUAL_HASH(x,h,y) ((x) == (y) || ((y) != null && h == (y).hashCode() && (y).equals((x))))\n"\
"#define KEY_EQUAL(x,y) ((x) == (y) || ((x) != null && (x).equals((y))))\n"\
"#else\n"\
"#define KEY_EQUAL_HASH(x,h,y) ((x) == (y))\n"\
"#define KEY_EQUAL(x,y) ((x) == (y))\n"\
"#endif\n\n"\
"#if #keyclass(Integer)\n"\
"#define REMOVE rem\n"\
"#else\n"\
"#define REMOVE remove\n"\
"#endif\n\n"\
"#include \"${LIST[$f]}.drv\"\n" >$FILENAME
	  done
done


#
# This loop generates iterator and comparator interfaces. Note that we need
# boolean iterators for maps with booleans as codomain.
#

t=${#INTERFACE[*]}
for ((f=0; f<t; f++)); do
	l=${#TYPE[*]}
	if [[ ${INTERFACE[$f]} == "Comparator" 
	    || ${INTERFACE[$f]} == "AbstractComparator" ]]; then l=$((l-2)); fi # Comparators use only primitive types.
	if [[ ${INTERFACE[$f]} == "Iterators-Fragment"
	    || ${INTERFACE[$f]} == "Iterator"
	    || ${INTERFACE[$f]} == "AbstractIterator"
	    || ${INTERFACE[$f]} == "ListIterator"
	    || ${INTERFACE[$f]} == "AbstractListIterator"
	    || ${INTERFACE[$f]} == "BidirectionalIterator"
	    || ${INTERFACE[$f]} == "AbstractBidirectionalIterator" ]]; then l=$((l-1)); fi # There are no specific iterators for references.
	 for ((k=0; k<l; k++)); do
		if [[ ${INTERFACE[$f]:0:8} == "Abstract" ]]; then
		    FILENAME=$DIR/${TYPE_PACK[$k]}/Abstract${TYPE_CAP[$k]}${INTERFACE[$f]:8}.c
		elif [[ ${INTERFACE[$f]##*-} == "Fragment" ]]; then
		    FILENAME=$DIR/${TYPE_CAP[$k]}${INTERFACE[$f]}.h
		else
		    FILENAME=$DIR/${TYPE_PACK[$k]}/${TYPE_CAP[$k]}${INTERFACE[$f]}.c
		fi
		rm -f $FILENAME
		echo -e \
"#assert keyclass(${CLASS[$k]})\n"\
"#define PACKAGE it.unimi.dsi.fastutil.${TYPE_PACK[$k]}\n"\
"#define KEY_TYPE ${TYPE[$k]}\n"\
"#define KEY_CLASS ${CLASS[$k]}\n"\
"#define SET ${TYPE_CAP[$k]}Set\n\n"\
"#define LINKEDOPENHASHSET ${TYPE_CAP[$k]}LinkedOpenHashSet\n\n"\
"#define COLLECTION ${TYPE_CAP[$k]}Collection\n\n"\
"#define KEY_ITERATOR_CONCATENATOR ${TYPE_CAP[$k]}IteratorConcatenator\n\n"\
"#if #keyclass(Object) || #keyclass(Reference)\n"\
"#define KEY2TYPE(x) (x)\n"\
"#define KEY2OBJ(x) (x)\n"\
"#define NEXT_KEY next\n"\
"#define PREV_KEY previous\n"\
"#define STD_KEY_ITERATOR Iterator\n\n"\
"#define KEY_ITERATOR ObjectIterator\n\n"\
"#define KEY_ABSTRACT_ITERATOR AbstractObjectIterator\n\n"\
"#define KEY_ABSTRACT_BIDI_ITERATOR AbstractObjectBidirectionalIterator\n\n"\
"#define KEY_ARRAY_ITERATOR ObjectArrayIterator\n\n"\
"#define KEY_EMPTY_ITERATOR_METHOD emptyObjectIterator\n\n"\
"#define KEY_LIST_ITERATOR ObjectListIterator\n\n"\
"#define KEY_ABSTRACT_LIST_ITERATOR AbstractObjectListIterator\n\n"\
"#define KEY_ITERATOR_WRAPPER ObjectIteratorWrapper\n\n"\
"#define AS_KEY_ITERATOR asObjectIterator\n\n"\
"#define KEY_BIDI_ITERATOR ObjectBidirectionalIterator\n\n"\
"#else\n"\
"#define KEY2TYPE(x) (((KEY_CLASS)(x)).KEY_VALUE())\n"\
"#define KEY2OBJ(x) (new KEY_CLASS(x))\n"\
"#define NEXT_KEY next${TYPE_CAP[$k]}\n"\
"#define PREV_KEY previous${TYPE_CAP[$k]}\n"\
"#define STD_KEY_ITERATOR ${TYPE_CAP[$k]}Iterator\n\n"\
"#define KEY_ITERATOR ${TYPE_CAP[$k]}Iterator\n\n"\
"#define KEY_ABSTRACT_ITERATOR Abstract${TYPE_CAP[$k]}Iterator\n\n"\
"#define KEY_ABSTRACT_BIDI_ITERATOR Abstract${TYPE_CAP[$k]}BidirectionalIterator\n\n"\
"#define KEY_ARRAY_ITERATOR ${TYPE_CAP[$k]}ArrayIterator\n\n"\
"#define KEY_EMPTY_ITERATOR_METHOD empty${TYPE_CAP[$k]}Iterator\n\n"\
"#define KEY_LIST_ITERATOR ${TYPE_CAP[$k]}ListIterator\n\n"\
"#define KEY_ABSTRACT_LIST_ITERATOR Abstract${TYPE_CAP[$k]}ListIterator\n\n"\
"#define KEY_ITERATOR_WRAPPER ${TYPE_CAP[$k]}IteratorWrapper\n\n"\
"#define AS_KEY_ITERATOR as${TYPE_CAP[$k]}Iterator\n\n"\
"#define KEY_BIDI_ITERATOR ${TYPE_CAP[$k]}BidirectionalIterator\n\n"\
"#define KEY_INTERVAL_ITERATOR ${TYPE_CAP[$k]}IntervalIterator\n\n"\
"#define KEY_COMPARATOR ${TYPE_CAP[$k]}Comparator\n\n"\
"#define KEY_ABSTRACT_COMPARATOR Abstract${TYPE_CAP[$k]}Comparator\n\n"\
"#define KEY_VALUE ${TYPE[$k]}Value\n"\
"#define KEY_CLASS ${CLASS[$k]}\n"\
"#define KEY2TYPE(x) (((KEY_CLASS)(x)).KEY_VALUE())\n"\
"#endif\n\n"\
"#include \"${INTERFACE[$f]}.drv\"\n" >$FILENAME

	  done
done


cp Iterators.drv $DIR/Iterators.c

rm -f $DIR/booleans/BooleanLinkedOpenHashSet.c
rm -f $DIR/booleans/BooleanSortedSet.c
rm -f $DIR/booleans/BooleanComparator.c
rm -f $DIR/booleans/AbstractBooleanComparator.c
rm -f $DIR/booleans/BooleanAVLTreeSet.c
rm -f $DIR/booleans/BooleanRBTreeSet.c
