#!/bin/bash

#
# This script generates from driver files fake C sources to be passed
# through a C preprocessor to get the actual Java sources. It expects
# as arguments the name of the driver and the name of the file to be 
# generated.
#

# The types we specialise to (these are actual Java types, so references appear here as Object).
TYPE=(boolean byte short int long char float double Object Object)

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
class=${class#Striped}

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

if [[ $root == *Linked* ]]; then 
Linked=Linked

# Macros for transforming the bi-directional long link. Return values are 32-bit int indexes.
# SET_UPPER and SET_LOWER do a masked assignment as described at
# http://www-graphics.stanford.edu/~seander/bithacks.html#MaskedMerge

echo -e \
"#define SET_PREV( f64, p32 )       SET_UPPER( f64, p32 )\n"\
"#define SET_NEXT( f64, n32 )       SET_LOWER( f64, n32 )\n"\
"#define COPY_PREV( f64, p64 )      SET_UPPER64( f64, p64 )\n"\
"#define COPY_NEXT( f64, n64 )      SET_LOWER64( f64, n64 )\n"\
"#define GET_PREV( f64 )            GET_UPPER( f64 )\n"\
"#define GET_NEXT( f64 )            GET_LOWER( f64 )\n"\
"#define SET_UPPER_LOWER( f64, up32, low32 )    f64 = ( ( up32 & 0xFFFFFFFFL ) << 32 ) | ( low32 & 0xFFFFFFFFL )\n"\
"#define SET_UPPER( f64, up32 )     f64 ^= ( ( f64 ^ ( ( up32 & 0xFFFFFFFFL ) << 32 ) ) & 0xFFFFFFFF00000000L )\n"\
"#define SET_LOWER( f64, low32 )    f64 ^= ( ( f64 ^ ( low32 & 0xFFFFFFFFL ) ) & 0xFFFFFFFFL )\n"\
"#define SET_UPPER64( f64, up64 )   f64 ^= ( ( f64 ^ ( up64 & 0xFFFFFFFF00000000L ) ) & 0xFFFFFFFF00000000L )\n"\
"#define SET_LOWER64( f64, low64 )  f64 ^= ( ( f64 ^ ( low64 & 0xFFFFFFFFL ) ) & 0xFFFFFFFFL )\n"\
"#define GET_UPPER( f64 )           (int) ( f64 >>> 32 )\n"\
"#define GET_LOWER( f64 )           (int) f64\n"
fi

if [[ $root == *Custom* ]]; then Custom=Custom; fi

echo -e \
\
\
"/* Generic definitions */\n"\
\
\
"${Linked:+#define Linked}\n"\
"${Custom:+#define Custom}\n"\
"#define PACKAGE it.unimi.dsi.fastutil.${TYPE_LC2[$k]}s\n"\
"#define VALUE_PACKAGE it.unimi.dsi.fastutil.${TYPE_LC2[$v]}s\n"\
\
\
"/* Assertions (useful to generate conditional code) */\n"\
\
\
$(if [[ "${CLASS[$k]}" != "" ]]; then\
	echo "#unassert keyclass\\n#assert keyclass(${CLASS[$k]})\\n#unassert keys\\n";\
	if [[ "${CLASS[$k]}" != "Object" && "${CLASS[$k]}" != "Reference" ]]; then\
		echo "#assert keys(primitive)\\n";\
	else\
		echo "#assert keys(reference)\\n";\
	fi;\
 fi)\
$(if [[ "${CLASS[$v]}" != "" ]]; then\
	echo "#unassert valueclass\\n#assert valueclass(${CLASS[$v]})\\n#unassert values\\n";\
	if [[ "${CLASS[$v]}" != "Object" && "${CLASS[$v]}" != "Reference" ]]; then\
		echo "#assert values(primitive)\\n";\
	else\
		echo "#assert values(reference)\\n";\
	fi;\
 fi)\
\
\
"/* Current type and class (and size, if applicable) */\n"\
\
\
"#define KEY_TYPE ${TYPE[$k]}\n"\
"#define VALUE_TYPE ${TYPE[$v]}\n"\
"#define KEY_CLASS ${CLASS[$k]}\n"\
"#define VALUE_CLASS ${CLASS[$v]}\n"\
\
\
"#if #keyclass(Object) || #keyclass(Reference)\n"\
"#define KEY_GENERIC_CLASS K\n"\
"#define KEY_GENERIC_TYPE K\n"\
"#define KEY_GENERIC <K>\n"\
"#define KEY_GENERIC_WILDCARD <?>\n"\
"#define KEY_EXTENDS_GENERIC <? extends K>\n"\
"#define KEY_SUPER_GENERIC <? super K>\n"\
"#define KEY_GENERIC_CAST (K)\n"\
"#define KEY_GENERIC_ARRAY_CAST (K[])\n"\
"#define KEY_GENERIC_BIG_ARRAY_CAST (K[][])\n"\
"#else\n"\
"#define KEY_GENERIC_CLASS KEY_CLASS\n"\
"#define KEY_GENERIC_TYPE KEY_TYPE\n"\
"#define KEY_GENERIC\n"\
"#define KEY_GENERIC_WILDCARD\n"\
"#define KEY_EXTENDS_GENERIC\n"\
"#define KEY_SUPER_GENERIC\n"\
"#define KEY_GENERIC_CAST\n"\
"#define KEY_GENERIC_ARRAY_CAST\n"\
"#define KEY_GENERIC_BIG_ARRAY_CAST\n"\
"#endif\n"\
\
"#if #valueclass(Object) || #valueclass(Reference)\n"\
"#define VALUE_GENERIC_CLASS V\n"\
"#define VALUE_GENERIC_TYPE V\n"\
"#define VALUE_GENERIC <V>\n"\
"#define VALUE_EXTENDS_GENERIC <? extends V>\n"\
"#define VALUE_GENERIC_CAST (V)\n"\
"#define VALUE_GENERIC_ARRAY_CAST (V[])\n"\
"#else\n"\
"#define VALUE_GENERIC_CLASS VALUE_CLASS\n"\
"#define VALUE_GENERIC_TYPE VALUE_TYPE\n"\
"#define VALUE_GENERIC\n"\
"#define VALUE_EXTENDS_GENERIC\n"\
"#define VALUE_GENERIC_CAST\n"\
"#define VALUE_GENERIC_ARRAY_CAST\n"\
"#endif\n"\
\
"#if #keyclass(Object) || #keyclass(Reference)\n"\
"#if #valueclass(Object) || #valueclass(Reference)\n"\
"#define KEY_VALUE_GENERIC <K,V>\n"\
"#define KEY_VALUE_EXTENDS_GENERIC <? extends K, ? extends V>\n"\
"#else\n"\
"#define KEY_VALUE_GENERIC <K>\n"\
"#define KEY_VALUE_EXTENDS_GENERIC <? extends K>\n"\
"#endif\n"\
"#else\n"\
"#if #valueclass(Object) || #valueclass(Reference)\n"\
"#define KEY_VALUE_GENERIC <V>\n"\
"#define KEY_VALUE_EXTENDS_GENERIC <? extends V>\n"\
"#else\n"\
"#define KEY_VALUE_GENERIC\n"\
"#define KEY_VALUE_EXTENDS_GENERIC\n"\
"#endif\n"\
"#endif\n"\
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
"#define HASH ${TYPE_CAP[$k]}Hash\n\n"\
"#define SORTED_SET ${TYPE_CAP[$k]}SortedSet\n\n"\
"#define STD_SORTED_SET ${TYPE_STD[$k]}SortedSet\n\n"\
"#define FUNCTION ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Function\n"\
"#define MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Map\n"\
"#define SORTED_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}SortedMap\n"\
"#if #keyclass(Object) || #keyclass(Reference)\n"\
"#define STD_SORTED_MAP SortedMap\n\n"\
"#define STRATEGY Strategy\n\n"\
"#else\n"\
"#define STD_SORTED_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}SortedMap\n\n"\
"#define STRATEGY PACKAGE.${TYPE_CAP[$k]}Hash.Strategy\n\n"\
"#endif\n"\
"#define LIST ${TYPE_CAP[$k]}List\n\n"\
"#define BIG_LIST ${TYPE_CAP[$k]}BigList\n\n"\
"#define STACK ${TYPE_STD[$k]}Stack\n\n"\
"#define PRIORITY_QUEUE ${TYPE_STD[$k]}PriorityQueue\n\n"\
"#define INDIRECT_PRIORITY_QUEUE ${TYPE_STD[$k]}IndirectPriorityQueue\n\n"\
"#define INDIRECT_DOUBLE_PRIORITY_QUEUE ${TYPE_STD[$k]}IndirectDoublePriorityQueue\n\n"\
"#define KEY_ITERATOR ${TYPE_CAP2[$k]}Iterator\n\n"\
"#define KEY_ITERABLE ${TYPE_CAP2[$k]}Iterable\n\n"\
"#define KEY_BIDI_ITERATOR ${TYPE_CAP2[$k]}BidirectionalIterator\n\n"\
"#define KEY_LIST_ITERATOR ${TYPE_CAP2[$k]}ListIterator\n\n"\
"#define KEY_BIG_LIST_ITERATOR ${TYPE_CAP2[$k]}BigListIterator\n\n"\
"#define STD_KEY_ITERATOR ${TYPE_STD[$k]}Iterator\n\n"\
"#define KEY_COMPARATOR ${TYPE_STD[$k]}Comparator\n\n"\
\
\
"/* Interfaces (values) */\n"\
\
\
"#define VALUE_COLLECTION ${TYPE_CAP[$v]}Collection\n\n"\
"#define VALUE_ARRAY_SET ${TYPE_CAP[$v]}ArraySet\n\n"\
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
"#define ABSTRACT_FUNCTION Abstract${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Function\n"\
"#define ABSTRACT_MAP Abstract${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Map\n"\
"#define ABSTRACT_FUNCTION Abstract${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Function\n"\
"#define ABSTRACT_SORTED_MAP Abstract${TYPE_CAP[$k]}2${TYPE_CAP[$v]}SortedMap\n"\
"#define ABSTRACT_LIST Abstract${TYPE_CAP[$k]}List\n\n"\
"#define ABSTRACT_BIG_LIST Abstract${TYPE_CAP[$k]}BigList\n\n"\
"#define SUBLIST ${TYPE_CAP[$k]}SubList\n\n"\
"#define ABSTRACT_PRIORITY_QUEUE Abstract${TYPE_STD[$k]}PriorityQueue\n\n"\
"#define ABSTRACT_STACK Abstract${TYPE_STD[$k]}Stack\n\n"\
"#define KEY_ABSTRACT_ITERATOR Abstract${TYPE_CAP2[$k]}Iterator\n\n"\
"#define KEY_ABSTRACT_BIDI_ITERATOR Abstract${TYPE_CAP2[$k]}BidirectionalIterator\n\n"\
"#define KEY_ABSTRACT_LIST_ITERATOR Abstract${TYPE_CAP2[$k]}ListIterator\n\n"\
"#define KEY_ABSTRACT_BIG_LIST_ITERATOR Abstract${TYPE_CAP2[$k]}BigListIterator\n\n"\
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
"#define VALUE_ABSTRACT_BIDI_ITERATOR Abstract${TYPE_CAP2[$v]}BidirectionalIterator\n\n"\
\
\
"/* Static containers (keys) */\n"\
\
\
"#define COLLECTIONS ${TYPE_CAP[$k]}Collections\n\n"\
"#define SETS ${TYPE_CAP[$k]}Sets\n\n"\
"#define SORTED_SETS ${TYPE_CAP[$k]}SortedSets\n\n"\
"#define LISTS ${TYPE_CAP[$k]}Lists\n\n"\
"#define BIG_LISTS ${TYPE_CAP[$k]}BigLists\n\n"\
"#define MAPS ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Maps\n"\
"#define FUNCTIONS ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Functions\n"\
"#define SORTED_MAPS ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}SortedMaps\n"\
"#define PRIORITY_QUEUES ${TYPE_CAP2[$k]}PriorityQueues\n\n"\
"#define HEAPS ${TYPE_CAP2[$k]}Heaps\n\n"\
"#define SEMI_INDIRECT_HEAPS ${TYPE_CAP2[$k]}SemiIndirectHeaps\n\n"\
"#define INDIRECT_HEAPS ${TYPE_CAP2[$k]}IndirectHeaps\n\n"\
"#define ARRAYS ${TYPE_CAP2[$k]}Arrays\n\n"\
"#define BIG_ARRAYS ${TYPE_CAP2[$k]}BigArrays\n\n"\
"#define ITERATORS ${TYPE_CAP2[$k]}Iterators\n\n"\
"#define BIG_LIST_ITERATORS ${TYPE_CAP2[$k]}BigListIterators\n\n"\
"#define COMPARATORS ${TYPE_CAP2[$k]}Comparators\n\n"\
\
\
"/* Static containers (values) */\n"\
\
\
"#define VALUE_COLLECTIONS ${TYPE_CAP[$v]}Collections\n\n"\
"#define VALUE_SETS ${TYPE_CAP[$v]}Sets\n\n"\
"#define VALUE_ARRAYS ${TYPE_CAP2[$v]}Arrays\n\n"\
\
\
"/* Implementations */\n"\
\
\
"#define OPEN_HASH_SET ${TYPE_CAP[$k]}${Linked}Open${Custom}HashSet\n\n"\
"#define OPEN_HASH_BIG_SET ${TYPE_CAP[$k]}${Linked}Open${Custom}HashBigSet\n\n"\
"#define OPEN_DOUBLE_HASH_SET ${TYPE_CAP[$k]}${Linked}Open${Custom}DoubleHashSet\n\n"\
"#define OPEN_HASH_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}${Linked}Open${Custom}HashMap\n\n"\
"#define OPEN_HASH_BIG_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}${Linked}Open${Custom}HashBigMap\n\n"\
"#define STRIPED_OPEN_HASH_MAP Striped${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Open${Custom}HashMap\n\n"\
"#define OPEN_DOUBLE_HASH_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}${Linked}Open${Custom}DoubleHashMap\n\n"\
"#define ARRAY_SET ${TYPE_CAP[$k]}ArraySet\n\n"\
"#define ARRAY_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}ArrayMap\n\n"\
"#define LINKED_OPEN_HASH_SET ${TYPE_CAP[$k]}LinkedOpenHashSet\n\n"\
"#define AVL_TREE_SET ${TYPE_CAP[$k]}AVLTreeSet\n\n"\
"#define RB_TREE_SET ${TYPE_CAP[$k]}RBTreeSet\n\n"\
"#define AVL_TREE_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}AVLTreeMap\n\n"\
"#define RB_TREE_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}RBTreeMap\n\n"\
"#define ARRAY_LIST ${TYPE_CAP[$k]}ArrayList\n\n"\
"#define BIG_ARRAY_BIG_LIST ${TYPE_CAP[$k]}BigArrayBigList\n\n"\
"#define ARRAY_FRONT_CODED_LIST ${TYPE_CAP[$k]}ArrayFrontCodedList\n\n"\
"#define HEAP_PRIORITY_QUEUE ${TYPE_CAP2[$k]}HeapPriorityQueue\n\n"\
"#define HEAP_SEMI_INDIRECT_PRIORITY_QUEUE ${TYPE_CAP2[$k]}HeapSemiIndirectPriorityQueue\n\n"\
"#define HEAP_INDIRECT_PRIORITY_QUEUE ${TYPE_CAP2[$k]}HeapIndirectPriorityQueue\n\n"\
"#define HEAP_SESQUI_INDIRECT_DOUBLE_PRIORITY_QUEUE ${TYPE_CAP2[$k]}HeapSesquiIndirectDoublePriorityQueue\n\n"\
"#define HEAP_INDIRECT_DOUBLE_PRIORITY_QUEUE ${TYPE_CAP2[$k]}HeapIndirectDoublePriorityQueue\n\n"\
"#define ARRAY_FIFO_QUEUE ${TYPE_CAP2[$k]}ArrayFIFOQueue\n\n"\
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
"#define SYNCHRONIZED_FUNCTION Synchronized${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Function\n\n"\
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
"#define UNMODIFIABLE_FUNCTION Unmodifiable${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Function\n\n"\
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
"#define DEQUEUE_LAST dequeueLast${TYPE_STD[$k]}\n"\
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
"#define AS_KEY_ITERABLE as${TYPE_CAP2[$k]}Iterable\n\n"\
"#define TO_KEY_ARRAY to${TYPE_STD[$k]}Array\n"\
"#define ENTRY_GET_KEY get${TYPE_STD[$k]}Key\n"\
"#define REMOVE_FIRST_KEY removeFirst${TYPE_STD[$k]}\n"\
"#define REMOVE_LAST_KEY removeLast${TYPE_STD[$k]}\n"\
"#define PARSE_KEY parse${TYPE_STD[$k]}\n"\
"#define LOAD_KEYS load${TYPE_STD[$k]}s\n"\
"#define LOAD_KEYS_BIG load${TYPE_STD[$k]}sBig\n"\
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
"#define REMOVE_FIRST_VALUE removeFirst${TYPE_STD[$v]}\n"\
"#define REMOVE_LAST_VALUE removeLast${TYPE_STD[$v]}\n"\
\
\
"/* Methods (keys/values) */\n"\
\
\
"#define ENTRYSET ${TYPE_LC[$k]}2${TYPE_CAP[$v]}EntrySet\n"\
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
\
"#ifdef Custom\n"\
"#define KEY_EQUALS(x,y) ( strategy.equals( " KEY_GENERIC_CAST "(x), " KEY_GENERIC_CAST "(y) ) )\n"\
"#define KEY_EQUALS_NOT_NULL(x,y) ( strategy.equals( " KEY_GENERIC_CAST "(x), " KEY_GENERIC_CAST "(y) ) )\n"\
"#else\n"\
"#if #keyclass(Object)\n"\
"#define KEY_EQUALS(x,y) ( (x) == null ? (y) == null : (x).equals(y) )\n"\
"#define KEY_EQUALS_NOT_NULL(x,y) ( (x).equals(y) )\n"\
"#else\n"\
"#if #keyclass(Float)\n"\
"#define KEY_EQUALS(x,y) ( Float.floatToIntBits(x) == Float.floatToIntBits(y) )\n"\
"#define KEY_EQUALS_NOT_NULL(x,y) ( Float.floatToIntBits(x) == Float.floatToIntBits(y) )\n"\
"#elif #keyclass(Double)\n"\
"#define KEY_EQUALS(x,y) ( Double.doubleToLongBits(x) == Double.doubleToLongBits(y) )\n"\
"#define KEY_EQUALS_NOT_NULL(x,y) ( Double.doubleToLongBits(x) == Double.doubleToLongBits(y) )\n"\
"#else\n"\
"#define KEY_EQUALS(x,y) ( (x) == (y) )\n"\
"#define KEY_EQUALS_NOT_NULL(x,y) ( (x) == (y) )\n"\
"#endif\n"\
"#endif\n"\
"#endif\n\n"\
\
"#if #valueclass(Object)\n"\
"#define VALUE_EQUALS(x,y) ( (x) == null ? (y) == null : (x).equals(y) )\n"\
"#else\n"\
"#define VALUE_EQUALS(x,y) ( (x) == (y) )\n"\
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
"#define KEY_OBJ2TYPE(x) (x)\n"\
"#define KEY_CLASS2TYPE(x) (x)\n"\
"#define KEY2OBJ(x) (x)\n"\
\
"#if #keyclass(Object)\n"\
"#ifdef Custom\n"\
"#define KEY2JAVAHASH_NOT_NULL(x) ( strategy.hashCode(" KEY_GENERIC_CAST "(x)) )\n"\
"#define KEY2INTHASH(x) ( it.unimi.dsi.fastutil.HashCommon.phiMix( strategy.hashCode(" KEY_GENERIC_CAST "(x)) ) )\n"\
"#define KEY2LONGHASH(x) ( it.unimi.dsi.fastutil.HashCommon.phiMix( (long)( strategy.hashCode(" KEY_GENERIC_CAST "(x)) ) ) )\n"\
"#else\n"\
"#define KEY2JAVAHASH_NOT_NULL(x) ( (x).hashCode() )\n"\
"#define KEY2JAVAHASH(x) ( (x) == null ? 0 : (x).hashCode() )\n"\
"#define KEY2INTHASH(x) ( it.unimi.dsi.fastutil.HashCommon.phiMix( (x).hashCode() ) )\n"\
"#define KEY2LONGHASH(x) ( it.unimi.dsi.fastutil.HashCommon.phiMix( (long)( (x).hashCode() ) ) )\n"\
"#endif\n"\
"#else\n"\
"#define KEY2JAVAHASH_NOT_NULL(x) ( System.identityHashCode(x) )\n"\
"#define KEY2INTHASH(x) ( it.unimi.dsi.fastutil.HashCommon.phiMix( System.identityHashCode(x) ) )\n"\
"#define KEY2LONGHASH(x) ( it.unimi.dsi.fastutil.HashCommon.phiMix( (long)( System.identityHashCode(x) ) ) )\n"\
"#endif\n"\
\
"#define KEY_CMP(x,y) ( ((Comparable<KEY_GENERIC_CLASS>)(x)).compareTo(y) )\n"\
"#define KEY_CMP_EQ(x,y) ( ((Comparable<KEY_GENERIC_CLASS>)(x)).compareTo(y) == 0 )\n"\
"#define KEY_LESS(x,y) ( ((Comparable<KEY_GENERIC_CLASS>)(x)).compareTo(y) < 0 )\n"\
"#define KEY_LESSEQ(x,y) ( ((Comparable<KEY_GENERIC_CLASS>)(x)).compareTo(y) <= 0 )\n"\
\
"#define KEY_NULL (null)\n"\
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
"#define KEY_CLASS2TYPE(x) ((x).KEY_VALUE())\n"\
"#define KEY_OBJ2TYPE(x) (KEY_CLASS2TYPE((KEY_CLASS)(x)))\n"\
"#define KEY2OBJ(x) (KEY_CLASS.valueOf(x))\n"\
\
"#if #keyclass(Boolean)\n"\
"#define KEY_CMP_EQ(x,y) ( (x) == (y) )\n"\
"#define KEY_NULL (false)\n"\
"#define KEY_CMP(x,y) ( !(x) && (y) ? -1 : ( (x) == (y) ? 0 : 1 ) )\n"\
"#define KEY_LESS(x,y) ( !(x) && (y) )\n"\
"#define KEY_LESSEQ(x,y) ( !(x) || (y) )\n"\
"#else\n"\
"#if #keyclass(Byte) || #keyclass(Short) || #keyclass(Character)\n"\
"#define KEY_NULL ((KEY_TYPE)0)\n"\
"#else\n"\
"#define KEY_NULL (0)\n"\
"#endif\n"\
"#if #keyclass(Float) || #keyclass(Double)\n"\
"#define KEY_CMP_EQ(x,y) ( KEY_CLASS.compare((x),(y)) == 0 )\n"\
"#define KEY_CMP(x,y) ( KEY_CLASS.compare((x),(y)) )\n"\
"#define KEY_LESS(x,y) ( KEY_CLASS.compare((x),(y)) < 0 )\n"\
"#define KEY_LESSEQ(x,y) ( KEY_CLASS.compare((x),(y)) <= 0 )\n"\
"#else\n"\
"#define KEY_CMP_EQ(x,y) ( (x) == (y) )\n"\
"#define KEY_CMP(x,y) ( (x) < (y) ? -1 : ( (x) == (y) ? 0 : 1 ) )\n"\
"#define KEY_LESS(x,y) ( (x) < (y) )\n"\
"#define KEY_LESSEQ(x,y) ( (x) <= (y) )\n"\
"#endif\n"\
\
"#if #keyclass(Float)\n"\
"#define KEY2LEXINT(x) fixFloat(x)\n"\
"#elif #keyclass(Double)\n"\
"#define KEY2LEXINT(x) fixDouble(x)\n"\
"#else\n"\
"#define KEY2LEXINT(x) (x)\n"\
"#endif\n"\
\
"#endif\n"\
\
"#ifdef Custom\n"\
"#define KEY2JAVAHASH_NOT_NULL(x) ( strategy.hashCode(x) )\n"\
"#define KEY2INTHASH(x) ( it.unimi.dsi.fastutil.HashCommon.phiMix( strategy.hashCode(x) ) )\n"\
"#define KEY2LONGHASH(x) ( it.unimi.dsi.fastutil.HashCommon.phiMix( (long)( strategy.hashCode(x) ) ) )\n"\
"#else\n"\
\
"#if #keyclass(Float)\n"\
"#define KEY2JAVAHASH_NOT_NULL(x) it.unimi.dsi.fastutil.HashCommon.float2int(x)\n"\
"#define KEY2INTHASH(x) it.unimi.dsi.fastutil.HashCommon.phiMix( it.unimi.dsi.fastutil.HashCommon.float2int(x) )\n"\
"#define KEY2LONGHASH(x) it.unimi.dsi.fastutil.HashCommon.phiMix( (long)( it.unimi.dsi.fastutil.HashCommon.float2int(x) ) )\n"\
"#elif #keyclass(Double)\n"\
"#define KEY2JAVAHASH_NOT_NULL(x) it.unimi.dsi.fastutil.HashCommon.double2int(x)\n"\
"#define KEY2INTHASH(x) (int)it.unimi.dsi.fastutil.HashCommon.phiMix( Double.doubleToRawLongBits(x) )\n"\
"#define KEY2LONGHASH(x) it.unimi.dsi.fastutil.HashCommon.phiMix( Double.doubleToRawLongBits(x) )\n"\
"#elif #keyclass(Long)\n"\
"#define KEY2JAVAHASH_NOT_NULL(x) it.unimi.dsi.fastutil.HashCommon.long2int(x)\n"\
"#define KEY2INTHASH(x) (int)it.unimi.dsi.fastutil.HashCommon.phiMix( (x) )\n"\
"#define KEY2LONGHASH(x) it.unimi.dsi.fastutil.HashCommon.phiMix( (x) )\n"\
"#elif #keyclass(Boolean)\n"\
"#define KEY2JAVAHASH_NOT_NULL(x) ((x) ? 1231 : 1237)\n"\
"#define KEY2INTHASH(x) ((x) ? 0xfab5368 : 0xcba05e7b)\n"\
"#define KEY2LONGHASH(x) ((x) ? 0x74a19fc8b6428188L : 0xbaeca2031a4fd9ecL)\n"\
"#else\n"\
"#define KEY2JAVAHASH_NOT_NULL(x) (x)\n"\
"#define KEY2INTHASH(x) ( it.unimi.dsi.fastutil.HashCommon.phiMix( (x) ) )\n"\
"#define KEY2LONGHASH(x) ( it.unimi.dsi.fastutil.HashCommon.phiMix( (long)( (x) ) ) )\n"\
"#endif\n"\
"#endif\n"\
\
"#endif\n"\
\
"#ifndef KEY2JAVAHASH\n"\
"#define KEY2JAVAHASH(x) KEY2JAVAHASH_NOT_NULL(x)\n"\
"#endif\n"\
\
\
\
"/* Object/Reference-only definitions (values) */\n"\
\
\
"#if #valueclass(Object) || #valueclass(Reference)\n"\
"#define VALUE_OBJ2TYPE(x) (x)\n"\
"#define VALUE_CLASS2TYPE(x) (x)\n"\
"#define VALUE2OBJ(x) (x)\n"\
\
"#if #valueclass(Object)\n"\
"#define VALUE2JAVAHASH(x) ( (x) == null ? 0 : (x).hashCode() )\n"\
"#else\n"\
"#define VALUE2JAVAHASH(x) ( (x) == null ? 0 : System.identityHashCode(x) )\n"\
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
"#define VALUE_CLASS2TYPE(x) ((x).VALUE_VALUE())\n"\
"#define VALUE_OBJ2TYPE(x) (VALUE_CLASS2TYPE((VALUE_CLASS)(x)))\n"\
"#define VALUE2OBJ(x) (VALUE_CLASS.valueOf(x))\n"\
\
"#if #valueclass(Float) || #valueclass(Double) || #valueclass(Long)\n"\
"#define VALUE_NULL (0)\n"\
"#define VALUE2JAVAHASH(x) it.unimi.dsi.fastutil.HashCommon.${TYPE[$v]}2int(x)\n"\
"#elif #valueclass(Boolean)\n"\
"#define VALUE_NULL (false)\n"\
"#define VALUE2JAVAHASH(x) (x ? 1231 : 1237)\n"\
"#else\n"\
"#if #valueclass(Integer)\n"\
"#define VALUE_NULL (0)\n"\
"#else\n"\
"#define VALUE_NULL ((VALUE_TYPE)0)\n"\
"#endif\n"\
"#define VALUE2JAVAHASH(x) (x)\n"\
"#endif\n"\
\
"#define OBJECT_DEFAULT_RETURN_VALUE (null)\n"\
\
"#endif\n"\
\
"#include \"$1\"\n"
