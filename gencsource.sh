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

# Much like $TYPE_CAP, by the place occupied by Reference is now occupied by Object.
TYPE_CAP2=(Boolean Byte Short Int Long Char Float Double Object Object)

# Much like $TYPE_CAP, but object type get the empty string.
TYPE_STD=(Boolean Byte Short Int Long Char Float Double "" "")

# The upper case types used to build class and method names.
TYPE_UC=(BOOLEAN BYTE SHORT INT LONG CHAR FLOAT DOUBLE OBJECT REFERENCE)

# The downcased types used to build method names.
TYPE_LC=(boolean byte short int long char float double object reference)

# Much like $TYPE_LC, by the place occupied by reference is now occupied by object.
TYPE_LC2=(boolean byte short int long char float double object object)

# The corresponding classes (in few cases, there are differences with $TYPE_CAP).
CLASS=(Boolean Byte Short Integer Long Character Float Double Object Reference)

# The corresponding byte size or 0 for unknown.
SIZE=(0 1 2 4 8 2 4 8 0 0)

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

if [[ $root == *Linked* ]]; then Linked=Linked; fi
if [[ $root == *Custom* ]]; then Custom=Custom; fi

echo -e \
\
\
"/* Generic definitions */\n"\
\
\
"${Linked:+#define Linked}\n"\
"${Custom:+#define Custom}\n"\
"#define PACKAGE it.unimi.dsi.fastutil.${PACKAGE[$k]}\n"\
"#define VALUE_PACKAGE it.unimi.dsi.fastutil.${PACKAGE[$v]}\n"\
\
\
"/* Assertions (useful to generate conditional code) */\n"\
\
\
$(if [[ "${CLASS[$k]}" != "" ]]; then echo "#assert keyclass(${CLASS[$k]})\\n"; fi)\
$(if [[ "${CLASS[$v]}" != "" ]]; then echo "#assert valueclass(${CLASS[$v]})\\n"; fi)\
\
\
"/* Current type and class (and size, if applicable) */\n"\
\
\
"#define KEY_TYPE ${TYPE[$k]}\n"\
"#define VALUE_TYPE ${TYPE[$v]}\n"\
"#define KEY_CLASS ${CLASS[$k]}\n"\
"#define VALUE_CLASS ${CLASS[$v]}\n"\
"#define KEY_SIZE ${SIZE[$k]}\n"\
"#define VALUE_SIZE ${SIZE[$v]}\n"\
\
\
"/* Value methods */\n"\
\
\
"#define KEY_VALUE ${TYPE[$k]}Value\n"\
"#define VALUE_VALUE ${TYPE[$v]}Value\n"\
\
\
"/* Interfaces (keys) */\n"\
\
\
"#define COLLECTION ${TYPE_CAP[$k]}Collection\n\n"\
"#define SET ${TYPE_CAP[$k]}Set\n\n"\
"#define SORTED_SET ${TYPE_CAP[$k]}SortedSet\n\n"\
"#define STD_SORTED_SET ${TYPE_STD[$k]}SortedSet\n\n"\
"#define MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Map\n"\
"#define SORTED_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}SortedMap\n"\
"#if #keyclass(Object) || #keyclass(Reference)\n"\
"#define STD_SORTED_MAP SortedMap\n\n"\
"#else\n"\
"#define STD_SORTED_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}SortedMap\n\n"\
"#endif\n"\
"#define LIST ${TYPE_CAP[$k]}List\n\n"\
"#define STACK ${TYPE_STD[$k]}Stack\n\n"\
"#define PRIORITY_QUEUE ${TYPE_STD[$k]}PriorityQueue\n\n"\
"#define KEY_ITERATOR ${TYPE_CAP2[$k]}Iterator\n\n"\
"#define KEY_BIDI_ITERATOR ${TYPE_CAP2[$k]}BidirectionalIterator\n\n"\
"#define KEY_LIST_ITERATOR ${TYPE_CAP2[$k]}ListIterator\n\n"\
"#define STD_KEY_ITERATOR ${TYPE_STD[$k]}Iterator\n\n"\
"#define KEY_COMPARATOR ${TYPE_STD[$k]}Comparator\n\n"\
\
\
"/* Interfaces (values) */\n"\
\
\
"#define VALUE_COLLECTION ${TYPE_CAP[$v]}Collection\n\n"\
"#define VALUE_ITERATOR ${TYPE_CAP2[$v]}Iterator\n\n"\
"#define VALUE_LIST_ITERATOR ${TYPE_CAP2[$v]}ListIterator\n\n"\
\
\
"/* Abstract implementations (keys) */\n"\
\
\
"#define ABSTRACT_COLLECTION Abstract${TYPE_CAP[$k]}Collection\n\n"\
"#define ABSTRACT_SET Abstract${TYPE_CAP[$k]}Set\n\n"\
"#define ABSTRACT_SORTED_SET Abstract${TYPE_CAP[$k]}SortedSet\n"\
"#define ABSTRACT_MAP Abstract${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Map\n"\
"#define ABSTRACT_SORTED_MAP Abstract${TYPE_CAP[$k]}2${TYPE_CAP[$v]}SortedMap\n"\
"#define ABSTRACT_LIST Abstract${TYPE_CAP[$k]}List\n\n"\
"#define SUBLIST ${TYPE_CAP[$k]}SubList\n\n"\
"#define ABSTRACT_PRIORITY_QUEUE Abstract${TYPE_STD[$k]}PriorityQueue\n\n"\
"#define ABSTRACT_STACK Abstract${TYPE_STD[$k]}Stack\n\n"\
"#define KEY_ABSTRACT_ITERATOR Abstract${TYPE_CAP2[$k]}Iterator\n\n"\
"#define KEY_ABSTRACT_BIDI_ITERATOR Abstract${TYPE_CAP2[$k]}BidirectionalIterator\n\n"\
"#define KEY_ABSTRACT_LIST_ITERATOR Abstract${TYPE_CAP2[$k]}ListIterator\n\n"\
"#if #keyclass(Object)\n"\
"#define KEY_ABSTRACT_COMPARATOR Comparator\n\n"\
"#else\n"\
"#define KEY_ABSTRACT_COMPARATOR Abstract${TYPE_CAP[$k]}Comparator\n\n"\
"#endif\n"\
\
\
"/* Abstract implementations (values) */\n"\
\
\
"#define VALUE_ABSTRACT_COLLECTION Abstract${TYPE_CAP[$v]}Collection\n\n"\
"#define VALUE_ABSTRACT_ITERATOR Abstract${TYPE_CAP2[$v]}Iterator\n\n"\
\
\
"/* Static containers (keys) */\n"\
\
\
"#define COLLECTIONS ${TYPE_CAP[$k]}Collections\n\n"\
"#define SETS ${TYPE_CAP[$k]}Sets\n\n"\
"#define SORTED_SETS ${TYPE_CAP[$k]}SortedSets\n\n"\
"#define LISTS ${TYPE_CAP[$k]}Lists\n\n"\
"#define MAPS ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Maps\n"\
"#define SORTED_MAPS ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}SortedMaps\n"\
"#define PRIORITY_QUEUES ${TYPE_CAP2[$k]}PriorityQueues\n\n"\
"#define HEAPS ${TYPE_CAP2[$k]}Heaps\n\n"\
"#define SEMI_INDIRECT_HEAPS ${TYPE_CAP2[$k]}SemiIndirectHeaps\n\n"\
"#define INDIRECT_HEAPS ${TYPE_CAP2[$k]}IndirectHeaps\n\n"\
"#define ARRAYS ${TYPE_CAP2[$k]}Arrays\n\n"\
"#define ITERATORS ${TYPE_CAP2[$k]}Iterators\n\n"\
"#define COMPARATORS ${TYPE_CAP2[$k]}Comparators\n\n"\
\
\
"/* Static containers (values) */\n"\
\
\
"#define VALUE_COLLECTIONS ${TYPE_CAP[$v]}Collections\n\n"\
"#define VALUE_SETS ${TYPE_CAP[$v]}Sets\n\n"\
\
\
"/* Implementations */\n"\
\
\
"#define OPEN_HASH_SET ${TYPE_CAP[$k]}${Linked}Open${Custom}HashSet\n\n"\
"#define OPEN_HASH_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}${Linked}Open${Custom}HashMap\n\n"\
"#define LINKED_OPEN_HASH_SET ${TYPE_CAP[$k]}LinkedOpenHashSet\n\n"\
"#define AVL_TREE_SET ${TYPE_CAP[$k]}AVLTreeSet\n\n"\
"#define RB_TREE_SET ${TYPE_CAP[$k]}RBTreeSet\n\n"\
"#define AVL_TREE_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}AVLTreeMap\n\n"\
"#define RB_TREE_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}RBTreeMap\n\n"\
"#define ARRAY_LIST ${TYPE_CAP[$k]}ArrayList\n\n"\
"#define ARRAY_FRONT_CODED_LIST ${TYPE_CAP[$k]}ArrayFrontCodedList\n\n"\
"#define HEAP_PRIORITY_QUEUE ${TYPE_CAP2[$k]}HeapPriorityQueue\n\n"\
"#define HEAP_SEMI_INDIRECT_PRIORITY_QUEUE ${TYPE_CAP2[$k]}HeapSemiIndirectPriorityQueue\n\n"\
"#define HEAP_INDIRECT_PRIORITY_QUEUE ${TYPE_CAP2[$k]}HeapIndirectPriorityQueue\n\n"\
"#define HEAP_SESQUI_INDIRECT_DOUBLE_PRIORITY_QUEUE ${TYPE_CAP2[$k]}HeapSesquiIndirectDoublePriorityQueue\n\n"\
"#define HEAP_INDIRECT_DOUBLE_PRIORITY_QUEUE ${TYPE_CAP2[$k]}HeapIndirectDoublePriorityQueue\n\n"\
"#define ARRAY_PRIORITY_QUEUE ${TYPE_CAP2[$k]}ArrayPriorityQueue\n\n"\
"#define ARRAY_INDIRECT_PRIORITY_QUEUE ${TYPE_CAP2[$k]}ArrayIndirectPriorityQueue\n\n"\
"#define ARRAY_INDIRECT_DOUBLE_PRIORITY_QUEUE ${TYPE_CAP2[$k]}ArrayIndirectDoublePriorityQueue\n\n"\
\
\
"/* Synchronized wrappers */\n"\
\
\
"#define SYNCHRONIZED_COLLECTION Synchronized${TYPE_CAP[$k]}Collection\n\n"\
"#define SYNCHRONIZED_SET Synchronized${TYPE_CAP[$k]}Set\n\n"\
"#define SYNCHRONIZED_SORTED_SET Synchronized${TYPE_CAP[$k]}SortedSet\n\n"\
"#define SYNCHRONIZED_MAP Synchronized${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Map\n\n"\
"#define SYNCHRONIZED_LIST Synchronized${TYPE_CAP[$k]}List\n\n"\
\
\
"/* Unmodifiable wrappers */\n"\
\
\
"#define UNMODIFIABLE_COLLECTION Unmodifiable${TYPE_CAP[$k]}Collection\n\n"\
"#define UNMODIFIABLE_SET Unmodifiable${TYPE_CAP[$k]}Set\n\n"\
"#define UNMODIFIABLE_SORTED_SET Unmodifiable${TYPE_CAP[$k]}SortedSet\n\n"\
"#define UNMODIFIABLE_MAP Unmodifiable${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Map\n\n"\
"#define UNMODIFIABLE_LIST Unmodifiable${TYPE_CAP[$k]}List\n\n"\
"#define UNMODIFIABLE_KEY_ITERATOR Unmodifiable${TYPE_CAP[$k]}Iterator\n\n"\
"#define UNMODIFIABLE_KEY_BIDI_ITERATOR Unmodifiable${TYPE_CAP[$k]}BidirectionalIterator\n\n"\
"#define UNMODIFIABLE_KEY_LIST_ITERATOR Unmodifiable${TYPE_CAP[$k]}ListIterator\n\n"\
\
\
"/* Other wrappers */\n"\
\
\
"#define KEY_READER_WRAPPER ${TYPE_CAP[$k]}ReaderWrapper\n\n"\
"#define KEY_DATA_INPUT_WRAPPER ${TYPE_CAP[$k]}DataInputWrapper\n\n"\
\
\
"/* Methods (keys) */\n"\
\
\
"#define NEXT_KEY next${TYPE_STD[$k]}\n"\
"#define PREV_KEY previous${TYPE_STD[$k]}\n"\
"#define FIRST_KEY first${TYPE_STD[$k]}Key\n"\
"#define LAST_KEY last${TYPE_STD[$k]}Key\n"\
"#define GET_KEY get${TYPE_STD[$k]}\n"\
"#define REMOVE_KEY remove${TYPE_STD[$k]}\n"\
"#define READ_KEY read${TYPE_CAP2[$k]}\n"\
"#define WRITE_KEY write${TYPE_CAP2[$k]}\n"\
"#define DEQUEUE dequeue${TYPE_STD[$k]}\n"\
"#define SUBLIST_METHOD ${TYPE_LC[$k]}SubList\n"\
"#define SINGLETON_METHOD ${TYPE_LC[$k]}Singleton\n\n"\
"#define FIRST first${TYPE_STD[$k]}\n"\
"#define LAST last${TYPE_STD[$k]}\n"\
"#define TOP top${TYPE_STD[$k]}\n"\
"#define PEEK peek${TYPE_STD[$k]}\n"\
"#define POP pop${TYPE_STD[$k]}\n"\
"#define KEY_ITERATOR_METHOD ${TYPE_LC2[$k]}Iterator\n\n"\
"#define KEY_LIST_ITERATOR_METHOD ${TYPE_LC2[$k]}ListIterator\n\n"\
"#define KEY_EMPTY_ITERATOR_METHOD empty${TYPE_CAP2[$k]}Iterator\n\n"\
"#define AS_KEY_ITERATOR as${TYPE_CAP2[$k]}Iterator\n\n"\
"#define TO_KEY_ARRAY to${TYPE_STD[$k]}Array\n"\
"#define ENTRY_GET_KEY get${TYPE_STD[$k]}Key\n"\
"#define PARSE_KEY parse${TYPE_STD[$k]}\n"\
"#define LOAD_KEYS load${TYPE_STD[$k]}s\n"\
"#define STORE_KEYS store${TYPE_STD[$k]}s\n"\
\
\
"/* Methods (values) */\n"\
\
\
"#define NEXT_VALUE next${TYPE_STD[$v]}\n"\
"#define PREV_VALUE previous${TYPE_STD[$v]}\n"\
"#define READ_VALUE read${TYPE_CAP2[$v]}\n"\
"#define WRITE_VALUE write${TYPE_CAP2[$v]}\n"\
"#define VALUE_ITERATOR_METHOD ${TYPE_LC2[$v]}Iterator\n\n"\
"#define ENTRY_GET_VALUE get${TYPE_STD[$v]}Value\n"\
\
\
"/* Methods that have special names depending on keys (but the special names depend on values) */\n"\
\
\
"#if #keyclass(Object) || #keyclass(Reference)\n"\
"#define GET_VALUE get${TYPE_STD[$v]}\n"\
"#define REMOVE_VALUE remove${TYPE_STD[$v]}\n"\
"#else\n"\
"#define GET_VALUE get\n"\
"#define REMOVE_VALUE remove\n"\
"#endif\n"\
\
\
\
"/* Equality */\n"\
\
\
"#if #keyclass(Object)\n"\
"#ifdef Custom\n"\
"#define KEY_EQUALS(x,y) ( strategy.equals( (x), (y) ) )\n"\
"#define KEY_EQUALS_HASH(x,h,y) ( (h) == strategy.hashCode(y) && strategy.equals((x), (y)) )\n"\
"#else\n"\
"#define KEY_EQUALS(x,y) ( (x) == null ? (y) == null : (x).equals(y) )\n"\
"#define KEY_EQUALS_HASH(x,h,y) ( (x) == null ? (y) == null : (h) == (y).hashCode() && (x).equals(y) )\n"\
"#endif\n"\
"#else\n"\
"#define KEY_EQUALS(x,y) ( (x) == (y) )\n"\
"#define KEY_EQUALS_HASH(x,h,y) ( (x) == (y) )\n"\
"#endif\n\n"\
\
"#if #valueclass(Object)\n"\
"#define VALUE_EQUAL(x,y) ( (x) == null ? (y) == null : (x).equals(y) )\n"\
"#else\n"\
"#define VALUE_EQUAL(x,y) ( (x) == (y) )\n"\
"#endif\n\n"\
\
\
\
"/* Object/Reference-only definitions (keys) */\n"\
\
\
"#if #keyclass(Object) || #keyclass(Reference)\n"\
\
"#define REMOVE remove\n"\
\
"#define KEY2TYPE(x) (x)\n"\
"#define KEY2OBJ(x) (x)\n"\
\
"#define KEY_NULL (null)\n"\
\
"#if #keyclass(Object)\n"\
"#ifdef Custom\n"\
"#define KEY2INT(x) ( strategy.hashCode(x) )\n"\
"#else\n"\
"#define KEY2INT(x) ( (x) == null ? 0 : (x).hashCode() )\n"\
"#endif\n"\
"#else\n"\
"#define KEY2INT(x) (System.identityHashCode(x))\n"\
"#endif\n"\
\
"#define KEY_CMP(x,y) ( ((Comparable)(x)).compareTo(y) )\n"\
"#define KEY_LESS(x,y) ( ((Comparable)(x)).compareTo(y) < 0 )\n"\
"#define KEY_LESSEQ(x,y) ( ((Comparable)(x)).compareTo(y) <= 0 )\n"\
\
\
"#else\n"\
\
\
"/* Primitive-type-only definitions (keys) */\n"\
\
\
"#define REMOVE rem\n"\
\
"#define KEY2TYPE(x) (((KEY_CLASS)(x)).KEY_VALUE())\n"\
\
"#if #keyclass(Boolean)\n"\
"#define KEY2OBJ(x) (Boolean.valueOf(x))\n"\
"#define KEY_NULL (false)\n"\
"#define KEY_CMP(x,y) ( !(x) && (y) ? -1 : ( (x) == (y) ? 0 : 1 ) )\n"\
"#define KEY_LESS(x,y) ( !(x) && (y) )\n"\
"#define KEY_LESSEQ(x,y) ( !(x) || (y) )\n"\
"#else\n"\
"#define KEY2OBJ(x) (new KEY_CLASS(x))\n"\
"#define KEY_NULL ((KEY_TYPE)0)\n"\
"#define KEY_CMP(x,y) ( (x) < (y) ? -1 : ( (x) == (y) ? 0 : 1 ) )\n"\
"#define KEY_LESS(x,y) ( (x) < (y) )\n"\
"#define KEY_LESSEQ(x,y) ( (x) <= (y) )\n"\
"#endif\n"\
\
"#if #keyclass(Float) || #keyclass(Double) || #keyclass(Long)\n"\
"#define KEY2INT(x) HashCommon.${TYPE[$k]}2int(x)\n"\
"#elif #keyclass(Boolean)\n"\
"#define KEY2INT(x) (x ? 1231 : 1237)\n"\
"#else\n"\
"#define KEY2INT(x) ((int)(x))\n"\
"#endif\n"\
\
"#endif\n"\
\
\
\
"/* Object/Reference-only definitions (values) */\n"\
\
\
"#if #valueclass(Object) || #valueclass(Reference)\n"\
"#define VALUE2TYPE(x) (x)\n"\
"#define VALUE2OBJ(x) (x)\n"\
\
"#if #valueclass(Object)\n"\
"#define VALUE2INT(x) ( (x) == null ? 0 : (x).hashCode() )\n"\
"#else\n"\
"#define VALUE2INT(x) (System.identityHashCode(x))\n"\
"#endif\n"\
\
"#define VALUE_NULL (null)\n"\
"#define OBJECT_DEFAULT_RETURN_VALUE (this.defRetValue)\n"\
\
"#else\n"\
\
\
"/* Primitive-type-only definitions (values) */\n"\
\
\
"#define VALUE2TYPE(x) (((VALUE_CLASS)(x)).VALUE_VALUE())\n"\
\
"#if #valueclass(Float) || #valueclass(Double) || #valueclass(Long)\n"\
"#define VALUE2INT(x) HashCommon.${TYPE[$v]}2int(x)\n"\
"#elif #valueclass(Boolean)\n"\
"#define VALUE2INT(x) (x ? 1231 : 1237)\n"\
"#else\n"\
"#define VALUE2INT(x) ((int)(x))\n"\
"#endif\n"\
\
"#if #valueclass(Boolean)\n"\
"#define VALUE2OBJ(x) (Boolean.valueOf(x))\n"\
"#define VALUE_NULL (false)\n"\
"#else\n"\
"#define VALUE2OBJ(x) (new VALUE_CLASS(x))\n"\
"#define VALUE_NULL ((VALUE_TYPE)0)\n"\
"#endif\n"\
\
"#define OBJECT_DEFAULT_RETURN_VALUE (null)\n"\
\
"#endif\n"\
\
"#include \"$1\"\n"
