#!/bin/bash

#
# This script generates from driver files fake C sources to be passed
# through a C preprocessor to get the actual Java sources. It expects
# as arguments the name of the driver and the name of the file to be 
# generated.
#

# The types we specialise to (these are actual Java types, so references appear here as Object).
TYPE=(boolean byte short int long char float double Object Object)

# The same types, but in lower case and plural (to build package names; singular forms are reserved keywords).
PACKAGE=(booleans bytes shorts ints longs chars floats doubles objects objects)

# The capitalized types used to build class and method names (now references appear as Reference).
TYPE_CAP=(Boolean Byte Short Int Long Char Float Double Object Reference)

# The upper case types used to build class and method names.
TYPE_UC=(BOOLEAN BYTE SHORT INT LONG CHAR FLOAT DOUBLE OBJECT REFERENCE)

# The downcased types used to build method names.
TYPE_LC=(boolean byte short int long char float double object reference)

# The corresponding classes (in few cases, there are differences with $TYPE_CAP).
CLASS=(Boolean Byte Short Integer Long Character Float Double Object Reference)

# We perform some basic parsing on the filename.

export LC_ALL=C
shopt -s extglob

file=${2##*/}
name=${file%.*}
class=${name#Abstract}
if [[ "$class" == "$name" ]]; then
    abstract=
else
    abstract=Abstract
fi

# Now we rip off the types.
rem=${class##[A-Z]+([a-z])}
keylen=$(( ${#class} - ${#rem} ))
root=$rem

KEY_TYPE_CAP=${class:0:$keylen}
VALUE_TYPE_CAP=Object # Just for filling holes

if [[ "${rem:0:1}" == "2" ]]; then
    isFunction=true
    rem=${rem:1}
    rem2=${rem##[A-Z]+([a-z])}
    valuelen=$(( ${#rem} - ${#rem2} ))
    VALUE_TYPE_CAP=${rem:0:$valuelen}
    root=$rem2
else
    isFunction=false
fi

for((k=0; k<${#TYPE_CAP[*]}; k++)); do
    if [[ ${TYPE_CAP[$k]} == $KEY_TYPE_CAP ]]; then break; fi;
done

for((v=0; v<${#TYPE_CAP[*]}; v++)); do
    if [[ ${TYPE_CAP[$v]} == $VALUE_TYPE_CAP ]]; then break; fi;
done

if [[ $root != ${root#Linked} ]]; then linked=linked; else linked=unlinked; fi

echo -e \
"#define $linked\n"\
\
"#define KEY_TYPE ${TYPE[$k]}\n"\
"#define VALUE_TYPE ${TYPE[$v]}\n"\
"#define KEY_CLASS ${CLASS[$k]}\n"\
"#define VALUE_CLASS ${CLASS[$v]}\n"\
\
"#assert keyclass(${CLASS[$k]})\n"\
"#assert valueclass(${CLASS[$v]})\n"\
\
"#define PACKAGE it.unimi.dsi.fastutil.${PACKAGE[$k]}\n"\
"#define IMPORT_VALUES import it.unimi.dsi.fastutil.${PACKAGE[$v]}.*\n"\
"#define KEY_TEST test${TYPE_CAP[$k]}\n"\
\
"#define KEY_VALUE ${TYPE[$k]}Value\n"\
"#define VALUE_VALUE ${TYPE[$v]}Value\n"\
\
"#define COLLECTION ${TYPE_CAP[$k]}Collection\n\n"\
"#define COLLECTIONS ${TYPE_CAP[$k]}Collections\n\n"\
"#define SET ${TYPE_CAP[$k]}Set\n\n"\
"#define SETS ${TYPE_CAP[$k]}Sets\n\n"\
"#define VALUE_SETS ${TYPE_CAP[$v]}Sets\n\n"\
"#define SORTEDSET ${TYPE_CAP[$k]}SortedSet\n\n"\
"#define SORTEDSETS ${TYPE_CAP[$k]}SortedSets\n\n"\
"#define LIST ${TYPE_CAP[$k]}List\n\n"\
"#define LISTS ${TYPE_CAP[$k]}Lists\n\n"\
"#define ARRAYS ${TYPE_CAP[$k]}Arrays\n\n"\
\
"#define MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Map\n"\
"#define MAPS ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Maps\n"\
"#define SORTEDMAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}SortedMap\n"\
"#define SORTEDMAPS ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}SortedMaps\n"\
"#define ABSTRACT_MAP Abstract${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Map\n"\
\
"#define ABSTRACT_COLLECTION Abstract${TYPE_CAP[$k]}Collection\n\n"\
"#define VALUE_ABSTRACT_COLLECTION Abstract${TYPE_CAP[$v]}Collection\n\n"\
"#define VALUE_COLLECTION ${TYPE_CAP[$v]}Collection\n\n"\
"#define VALUE_COLLECTIONS ${TYPE_CAP[$v]}Collections\n\n"\
"#define ABSTRACT_SET Abstract${TYPE_CAP[$k]}Set\n\n"\
"#define ABSTRACT_LIST Abstract${TYPE_CAP[$k]}List\n\n"\
\
"#define KEY_ABSTRACT_SET Abstract${TYPE_CAP[$k]}Set\n\n"\
"#define AVLTREESET ${TYPE_CAP[$k]}AVLTreeSet\n\n"\
"#define RBTREESET ${TYPE_CAP[$k]}RBTreeSet\n\n"\
"#define LINKEDOPENHASHSET ${TYPE_CAP[$k]}LinkedOpenHashSet\n\n"\
"#ifdef linked\n"\
"#define OPENHASHSET ${TYPE_CAP[$k]}LinkedOpenHashSet\n\n"\
"#define OPENHASHMAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}LinkedOpenHashMap\n\n"\
"#else\n"\
"#define OPENHASHSET ${TYPE_CAP[$k]}OpenHashSet\n\n"\
"#define OPENHASHMAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}OpenHashMap\n\n"\
"#endif\n"\
"#define AVLTREEMAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}AVLTreeMap\n\n"\
"#define RBTREEMAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}RBTreeMap\n\n"\
"#define ARRAY_LIST ${TYPE_CAP[$k]}ArrayList\n\n"\
\
"#define SYNCHRONIZED_COLLECTION Synchronized${TYPE_CAP[$k]}Collection\n\n"\
"#define SYNCHRONIZED_SET Synchronized${TYPE_CAP[$k]}Set\n\n"\
"#define SYNCHRONIZED_MAP Synchronized${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Map\n\n"\
"#define SYNCHRONIZED_SORTEDSET Synchronized${TYPE_CAP[$k]}SortedSet\n\n"\
"#define SYNCHRONIZED_LIST Synchronized${TYPE_CAP[$k]}List\n\n"\
\
"#define SUBLIST ${TYPE_CAP[$k]}SubList\n"\
"#define SUBLIST_METHOD ${TYPE_LC[$k]}SubList\n"\
"#define SINGLETON ${TYPE_CAP[$k]}Singleton\n\n"\
"#define SINGLETON_METHOD ${TYPE_LC[$k]}Singleton\n\n"\
"#define KEY_ITERATOR_CONCATENATOR ${TYPE_CAP[$k]}IteratorConcatenator\n\n"\
"#define GEN_KEY gen${TYPE_CAP[$k]}\n\n"\
\
\
"#if #keyclass(Object) || #keyclass(Reference)\n"\
"#define STDSORTEDSET SortedSet\n\n"\
"#define STDSORTEDMAP SortedMap\n\n"\
"#define STDARRAYLIST ArrayList\n\n"\
"#define STD_KEY_ITERATOR Iterator\n\n"\
"#define STACK Stack\n\n"\
"#define ABSTRACT_STACK AbstractStack\n\n"\
"#define ITERATORS ObjectIterators\n\n"\
\
"#define REMOVE remove\n"\
"#define REMOVE_KEY remove\n"\
"#define GET_KEY get\n"\
"#define WRITE_KEY writeObject\n"\
"#define READ_KEY readObject\n"\
"#define NEXT_KEY next\n"\
"#define PREV_KEY previous\n"\
"#define FIRST_KEY firstKey\n"\
"#define LAST_KEY lastKey\n"\
\
"#define KEY_COMPARATOR Comparator\n\n"\
\
"#define KEY_ITERATOR ObjectIterator\n\n"\
"#define KEY_BIDI_ITERATOR ObjectBidirectionalIterator\n\n"\
"#define KEY_LIST_ITERATOR ObjectListIterator\n\n"\
\
"#define KEY_ABSTRACT_ITERATOR AbstractObjectIterator\n\n"\
"#define KEY_ABSTRACT_BIDI_ITERATOR AbstractObjectBidirectionalIterator\n\n"\
"#define KEY_ABSTRACT_LIST_ITERATOR AbstractObjectListIterator\n\n"\
\
"#define KEY_ARRAY_ITERATOR ObjectArrayIterator\n\n"\
"#define KEY_ITERATOR_WRAPPER ObjectIteratorWrapper\n\n"\
\
"#define KEY_ITERATOR_METHOD objectIterator\n\n"\
"#define KEY_EMPTY_ITERATOR_METHOD emptyObjectIterator\n\n"\
"#define KEY_LIST_ITERATOR_METHOD objectListIterator\n\n"\
"#define AS_KEY_ITERATOR asObjectIterator\n\n"\
\
"#define FIRST first\n"\
"#define LAST last\n"\
"#define INDEXOF_KEY indexOf\n"\
"#define LASTINDEXOF_KEY lastIndexOf\n"\
\
"#define TOP top\n"\
"#define PEEK peek\n"\
"#define POP pop\n"\
\
"#define KEY2TYPE(x) (x)\n"\
"#define KEY2OBJ(x) (x)\n"\
\
"#define ENTRY_GET_KEY getKey\n"\
"#define TO_KEY_ARRAY toArray\n"\
\
"#define KEY_NULL (null)\n"\
\
"#if #keyclass(Object)\n"\
"#define KEY2INT(x) (x == null ? 0 : x.hashCode())\n"\
"#else\n"\
"#define KEY2INT(x) (System.identityHashCode(x))\n"\
"#endif\n"\
\
"#if #valueclass(Object) || #valueclass(Reference)\n"\
"#define GET_VALUE get\n"\
"#define REMOVE_VALUE remove\n"\
"#else\n"\
"#define GET_VALUE get${TYPE_CAP[$v]}\n"\
"#define REMOVE_VALUE remove${TYPE_CAP[$v]}\n"\
"#endif\n"\
"#define KEY_CMP(x,y) (((Comparable)(x)).compareTo(y))\n"\
\
\
"#else\n"\
\
\
"#define STDSORTEDSET SORTEDSET\n\n"\
"#define STDSORTEDMAP SORTEDMAP\n\n"\
"#define STDARRAYLIST ARRAY_LIST\n\n"\
"#define KEY_EMPTY_SET ${TYPE_UC[$k]}_EMPTY_SET\n\n"\
"#define KEY_EMPTY_LIST ${TYPE_UC[$k]}_EMPTY_LIST\n\n"\
"#define KEY_EMPTY_ARRAY ${TYPE_UC[$k]}_EMPTY_ARRAY\n\n"\
"#define KEY_EMPTY_ITERATOR ${TYPE_UC[$k]}_EMPTY_ITERATOR\n\n"\
"#define STD_KEY_ITERATOR ${TYPE_CAP[$k]}Iterator\n\n"\
"#define STACK ${TYPE_CAP[$k]}Stack\n\n"\
"#define ABSTRACT_STACK Abstract${TYPE_CAP[$k]}Stack\n\n"\
"#define ITERATORS ${TYPE_CAP[$k]}Iterators\n\n"\
\
"#define REMOVE_KEY remove${TYPE_CAP[$k]}\n"\
"#define REMOVE rem\n"\
"#define GET_KEY get${TYPE_CAP[$k]}\n"\
"#define WRITE_KEY write${TYPE_CAP[$k]}\n"\
"#define READ_KEY read${TYPE_CAP[$k]}\n"\
"#define NEXT_KEY next${TYPE_CAP[$k]}\n"\
"#define PREV_KEY previous${TYPE_CAP[$k]}\n"\
"#define FIRST_KEY first${TYPE_CAP[$k]}Key\n"\
"#define LAST_KEY last${TYPE_CAP[$k]}Key\n"\
\
"#define KEY_COMPARATOR ${TYPE_CAP[$k]}Comparator\n\n"\
"#define KEY_ABSTRACT_COMPARATOR Abstract${TYPE_CAP[$k]}Comparator\n\n"\
\
"#define KEY_ITERATOR ${TYPE_CAP[$k]}Iterator\n\n"\
"#define KEY_BIDI_ITERATOR ${TYPE_CAP[$k]}BidirectionalIterator\n\n"\
"#define KEY_LIST_ITERATOR ${TYPE_CAP[$k]}ListIterator\n\n"\
\
"#define KEY_ABSTRACT_ITERATOR Abstract${TYPE_CAP[$k]}Iterator\n\n"\
"#define KEY_ABSTRACT_BIDI_ITERATOR Abstract${TYPE_CAP[$k]}BidirectionalIterator\n\n"\
"#define KEY_ABSTRACT_LIST_ITERATOR Abstract${TYPE_CAP[$k]}ListIterator\n\n"\
\
"#define KEY_ARRAY_ITERATOR ${TYPE_CAP[$k]}ArrayIterator\n\n"\
"#define KEY_ITERATOR_WRAPPER ${TYPE_CAP[$k]}IteratorWrapper\n\n"\
"#define KEY_INTERVAL_ITERATOR ${TYPE_CAP[$k]}IntervalIterator\n\n"\
\
"#define KEY_ITERATOR_METHOD ${TYPE[$k]}Iterator\n\n"\
"#define KEY_EMPTY_ITERATOR_METHOD empty${TYPE_CAP[$k]}Iterator\n\n"\
"#define KEY_LIST_ITERATOR_METHOD ${TYPE[$k]}ListIterator\n\n"\
"#define AS_KEY_ITERATOR as${TYPE_CAP[$k]}Iterator\n\n"\
\
"#define FIRST first${TYPE_CAP[$k]}\n"\
"#define LAST last${TYPE_CAP[$k]}\n"\
"#define INDEXOF_KEY indexOf${TYPE_CAP[$k]}\n"\
"#define LASTINDEXOF_KEY lastIndexOf${TYPE_CAP[$k]}\n"\
\
"#define TOP top${TYPE_CAP[$k]}\n"\
"#define PEEK peek${TYPE_CAP[$k]}\n"\
"#define POP pop${TYPE_CAP[$k]}\n"\
\
"#define KEY2TYPE(x) (((KEY_CLASS)(x)).KEY_VALUE())\n"\
"#define KEY2OBJ(x) (new KEY_CLASS(x))\n"\
\
"#define ENTRY_GET_KEY get${TYPE_CAP[$k]}Key\n"\
"#define TO_KEY_ARRAY to${TYPE_CAP[$k]}Array\n"\
\
"#if #keyclass(Boolean)\n"\
"#define KEY_NULL (false)\n"\
"#else\n"\
"#define KEY_NULL ((KEY_TYPE)0)\n"\
"#endif\n"\
\
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
"#include \"$1\"\n"
