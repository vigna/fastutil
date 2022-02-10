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

# The downcased types used to build method names.
TYPE_LC=(boolean byte short int long char float double object reference)

# Much like $TYPE_LC, by the place occupied by reference is now occupied by object.
TYPE_LC2=(boolean byte short int long char float double object object)

# The corresponding classes (in few cases, there are differences with $TYPE_CAP).
CLASS=(Boolean Byte Short Integer Long Character Float Double Object Reference)

# The capitalized widened type (needed for interaction with some JDK primitive operators)
WIDENED_TYPE_CAP=(Boolean Int Int Int Long Int Double Double Object Reference)

export LC_ALL=C
shopt -s extglob

# Derive from the filename the key (and possibly value) types, and whether the
# class is abstract or a linked hash-based container.

file=${2##*/}
name=${file%.*}

class=${name#Abstract}

# Now we rip off the types.
rem=${class##[A-Z]+([a-z])}
keylen=$(( ${#class} - ${#rem} ))
root=$rem

KEY_TYPE_CAP=${class:0:$keylen}
VALUE_TYPE_CAP=Object # Just for filling holes

if [[ "${rem:0:1}" == "2" ]]; then
    rem=${rem:1}
    rem2=${rem##[A-Z]+([a-z])}
    valuelen=$(( ${#rem} - ${#rem2} ))
    VALUE_TYPE_CAP=${rem:0:$valuelen}
    root=$rem2
fi

if [[ "$class" == *Pair ]]; then
    rem2=${rem##[A-Z]+([a-z])}
    valuelen=$(( ${#rem} - ${#rem2} ))
    VALUE_TYPE_CAP=${rem:0:$valuelen}
fi

# Compute indices k (position of the key type in TYPE), v (position of the value type in TYPE),
# wk (position of the widened key type in TYPE), and wv (position of the widened value type in TYPE).
# Widening happens for non-boolean primitive types, and promotes a type to the smallest of
# int/long/double (i.e., the types for which the JDK offers special treatment) that can represent it.

for((k=0; k<${#TYPE_CAP[*]}; k++)); do
    if [[ ${TYPE_CAP[$k]} == ${KEY_TYPE_CAP} ]]; then break; fi;
done

for((v=0; v<${#TYPE_CAP[*]}; v++)); do
    if [[ ${TYPE_CAP[$v]} == ${VALUE_TYPE_CAP} ]]; then break; fi;
done

for((wk=0; wk<${#TYPE_CAP[*]}; wk++)); do
    if [[ ${TYPE_CAP[$wk]} == ${WIDENED_TYPE_CAP[$k]} ]]; then break; fi;
done

for((wv=0; wv<${#TYPE_CAP[*]}; wv++)); do
    if [[ ${TYPE_CAP[$wv]} == ${WIDENED_TYPE_CAP[$v]} ]]; then break; fi;
done

# Additional setup for hash-based containers.

# Macros for transforming the bi-directional long link. Return values are 32-bit int indexes.
# SET_UPPER and SET_LOWER do a masked assignment as described at
# http://www-graphics.stanford.edu/~seander/bithacks.html#MaskedMerge

if [[ $root == *Linked* ]]; then
Linked=Linked

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
"#define WIDENED_PACKAGE it.unimi.dsi.fastutil.${TYPE_LC2[$wk]}s\n"\
\
\
"/* Assertions (useful to generate conditional code) */\n"\
\
\
$(if [[ "${CLASS[$k]}" != "" ]]; then\
	echo "#define KEY_CLASS_${CLASS[$k]} 1\\n";\
	if [[ "${CLASS[$k]}" != "Object" && "${CLASS[$k]}" != "Reference" ]]; then\
		echo "#define KEYS_PRIMITIVE 1\\n";\
	else\
		echo "#define KEYS_REFERENCE 1\\n";\
	fi;\
	if [[ "${CLASS[$k]}" == "Integer" || "${CLASS[$k]}" == "Long" || "${CLASS[$k]}" == "Double" ]]; then\
		echo "#define KEYS_INT_LONG_DOUBLE 1\\n";\
	fi;\
	if [[ "${CLASS[$k]}" == "Byte" || "${CLASS[$k]}" == "Character" || "${CLASS[$k]}" == "Short" || "${CLASS[$k]}" == "Float" ]]; then\
		echo "#define KEYS_BYTE_CHAR_SHORT_FLOAT 1\\n";\
	fi;\
 fi)\
$(if [[ "${CLASS[$v]}" != "" ]]; then\
	echo "#define VALUE_CLASS_${CLASS[$v]} 1\\n";\
	if [[ "${CLASS[$v]}" != "Object" && "${CLASS[$v]}" != "Reference" ]]; then\
		echo "#define VALUES_PRIMITIVE 1\\n";\
	else\
		echo "#define VALUES_REFERENCE 1\\n";\
	fi;\
	if [[ "${CLASS[$v]}" == "Integer" || "${CLASS[$v]}" == "Long" || "${CLASS[$v]}" == "Double" ]]; then\
		echo "#define VALUES_INT_LONG_DOUBLE 1\\n";\
	fi;\
	if [[ "${CLASS[$v]}" == "Byte" || "${CLASS[$v]}" == "Character" || "${CLASS[$v]}" == "Short" || "${CLASS[$v]}" == "Float" ]]; then\
		echo "#define VALUES_BYTE_CHAR_SHORT_FLOAT 1\\n";\
	fi;\
 fi)\
\
\
"/* Narrowing and widening */\n"\
\
\
$(if [[ "${CLASS[$k]}" != "" ]]; then\
	if [[ "${TYPE[$wk]}" == "${TYPE[$k]}" ]]; then\
		echo "#define KEY_NARROWING(x) x\\n";\
	else\
		echo "#define KEY_NARROWING(x) it.unimi.dsi.fastutil.SafeMath.safe${TYPE_CAP[$wk]}To${TYPE_CAP[$k]}(x)\\n";\
	fi;\
fi)\
$(if [[ "${CLASS[$k]}" != "" ]]; then\
	if [[ "${TYPE_CAP[$k]}" == "Long" ]]; then\
		echo "#define KEY_LONG_NARROWING(x) x\\n";\
	elif [[ "${TYPE_CAP[$k]}" != "Double" && "${TYPE_CAP[$k]}" != "Float" ]]; then\
		echo "#define KEY_LONG_NARROWING(x) it.unimi.dsi.fastutil.SafeMath.safeLongTo${TYPE_CAP[$k]}(x)\\n";\
	fi;\
fi)\
$(if [[ "${CLASS[$v]}" != "" ]]; then\
	if [[ "${TYPE[$wv]}" == "${TYPE[$v]}" ]]; then\
		echo "#define VALUE_NARROWING(x) x\\n";\
	else\
		echo "#define VALUE_NARROWING(x) it.unimi.dsi.fastutil.SafeMath.safe${TYPE_CAP[$wv]}To${TYPE_CAP[$v]}(x)\\n";\
	fi;\
fi)\
\
\
"/* Current type and class (and size, if applicable) */\n"\
\
\
"#define KEY_TYPE ${TYPE[$k]}\n"\
"#define KEY_TYPE_CAP ${TYPE_CAP[$k]}\n"\
"#define VALUE_TYPE ${TYPE[$v]}\n"\
"#define VALUE_TYPE_CAP ${TYPE_CAP[$v]}\n"\
"#define KEY_INDEX $k\n"\
"#define KEY_TYPE_WIDENED ${TYPE[$wk]}\n"\
"#define VALUE_TYPE_WIDENED ${TYPE[$wv]}\n"\
"#define KEY_CLASS ${CLASS[$k]}\n"\
"#define VALUE_CLASS ${CLASS[$v]}\n"\
"#define VALUE_INDEX $v\n"\
"#define KEY_CLASS_WIDENED ${CLASS[$wk]}\n"\
"#define VALUE_CLASS_WIDENED ${CLASS[$wv]}\n"\
\
"#define KEYS_USE_REFERENCE_EQUALITY KEY_CLASS_Reference\n"\
"#define VALUES_USE_REFERENCE_EQUALITY VALUE_CLASS_Reference\n"\
\
"#if KEYS_REFERENCE\n"\
"#define KEY_GENERIC_CLASS K\n"\
"#define KEY_GENERIC_TYPE K\n"\
"#define KEY_GENERIC_CLASS_WIDENED K\n"\
"#define KEY_GENERIC_TYPE_WIDENED K\n"\
"#define KEY_GENERIC <K>\n"\
"#define KEY_GENERIC_DIAMOND <>\n"\
"#define KEY_GENERIC_WILDCARD <?>\n"\
"#define KEY_EXTENDS_GENERIC <? extends K>\n"\
"#define KEY_SUPER_GENERIC <? super K>\n"\
"#define KEY_CLASS_CAST (K)\n"\
"#define KEY_GENERIC_CAST (K)\n"\
"#define KEY_GENERIC_ARRAY_CAST (K[])\n"\
"#define KEY_GENERIC_BIG_ARRAY_CAST (K[][])\n"\
"#define DEPRECATED_IF_KEYS_REFERENCE @Deprecated\n"\
"#define DEPRECATED_IF_KEYS_PRIMITIVE\n"\
"#define SUPPRESS_WARNINGS_KEY_UNCHECKED @SuppressWarnings(\"unchecked\")\n"\
"#define SUPPRESS_WARNINGS_KEY_RAWTYPES @SuppressWarnings(\"rawtypes\")\n"\
"#define SUPPRESS_WARNINGS_KEY_UNCHECKED_RAWTYPES @SuppressWarnings({\"unchecked\",\"rawtypes\"})\n"\
"#define SAFE_VARARGS @SafeVarargs\n"\
"#if defined(Custom)\n"\
"#define SUPPRESS_WARNINGS_CUSTOM_KEY_UNCHECKED @SuppressWarnings(\"unchecked\")\n"\
"#else\n"\
"#define SUPPRESS_WARNINGS_CUSTOM_KEY_UNCHECKED\n"\
"#endif\n"\
"#else\n"\
"#define KEY_GENERIC_CLASS KEY_CLASS\n"\
"#define KEY_GENERIC_TYPE KEY_TYPE\n"\
"#define KEY_GENERIC_CLASS_WIDENED KEY_CLASS_WIDENED\n"\
"#define KEY_GENERIC_TYPE_WIDENED KEY_TYPE_WIDENED\n"\
"#define KEY_GENERIC\n"\
"#define KEY_GENERIC_DIAMOND\n"\
"#define KEY_GENERIC_WILDCARD\n"\
"#define KEY_EXTENDS_GENERIC\n"\
"#define KEY_SUPER_GENERIC\n"\
"#define KEY_CLASS_CAST (KEY_CLASS)\n"\
"#define KEY_GENERIC_CAST\n"\
"#define KEY_GENERIC_ARRAY_CAST\n"\
"#define KEY_GENERIC_BIG_ARRAY_CAST\n"\
"#define DEPRECATED_IF_KEYS_REFERENCE\n"\
"#define DEPRECATED_IF_KEYS_PRIMITIVE @Deprecated\n"\
"#define SUPPRESS_WARNINGS_KEY_UNCHECKED\n"\
"#define SUPPRESS_WARNINGS_KEY_RAWTYPES\n"\
"#define SUPPRESS_WARNINGS_KEY_UNCHECKED_RAWTYPES\n"\
"#define SUPPRESS_WARNINGS_CUSTOM_KEY_UNCHECKED\n"\
"#define SAFE_VARARGS\n"\
"#endif\n"\
\
"#if VALUES_REFERENCE\n"\
"#define VALUE_GENERIC_CLASS V\n"\
"#define VALUE_GENERIC_TYPE V\n"\
"#define VALUE_GENERIC_CLASS_WIDENED V\n"\
"#define VALUE_GENERIC_TYPE_WIDENED V\n"\
"#define VALUE_GENERIC <V>\n"\
"#define VALUE_GENERIC_DIAMOND <>\n"\
"#define VALUE_EXTENDS_GENERIC <? extends V>\n"\
"#define VALUE_SUPER_GENERIC <? super V>\n"\
"#define VALUE_GENERIC_CAST (V)\n"\
"#define VALUE_GENERIC_ARRAY_CAST (V[])\n"\
"#define DEPRECATED_IF_VALUES_REFERENCE @Deprecated\n"\
"#define DEPRECATED_IF_VALUES_PRIMITIVE\n"\
"#define SUPPRESS_WARNINGS_VALUE_UNCHECKED @SuppressWarnings(\"unchecked\")\n"\
"#define SUPPRESS_WARNINGS_VALUE_RAWTYPES @SuppressWarnings(\"rawtypes\")\n"\
"#else\n"\
"#define VALUE_GENERIC_CLASS VALUE_CLASS\n"\
"#define VALUE_GENERIC_TYPE VALUE_TYPE\n"\
"#define VALUE_GENERIC_CLASS_WIDENED VALUE_CLASS_WIDENED\n"\
"#define VALUE_GENERIC_TYPE_WIDENED VALUE_TYPE_WIDENED\n"\
"#define VALUE_GENERIC\n"\
"#define VALUE_GENERIC_DIAMOND\n"\
"#define VALUE_EXTENDS_GENERIC\n"\
"#define VALUE_SUPER_GENERIC\n"\
"#define VALUE_GENERIC_CAST\n"\
"#define VALUE_GENERIC_ARRAY_CAST\n"\
"#define DEPRECATED_IF_VALUES_REFERENCE\n"\
"#define DEPRECATED_IF_VALUES_PRIMITIVE @Deprecated\n"\
"#define SUPPRESS_WARNINGS_VALUE_UNCHECKED\n"\
"#define SUPPRESS_WARNINGS_VALUE_RAWTYPES\n"\
"#endif\n"\
\
"#if KEYS_REFERENCE\n"\
"#if VALUES_REFERENCE\n"\
"#define KEY_VALUE_GENERIC <K,V>\n"\
"#define KEY_VALUE_GENERIC_DIAMOND <>\n"\
"#define KEY_VALUE_EXTENDS_GENERIC <? extends K, ? extends V>\n"\
"#define KEY_GENERIC_VALUE_EXTENDS_GENERIC <K, ? extends V>\n"\
"#define KEY_SUPER_GENERIC_VALUE_EXTENDS_GENERIC <? super K, ? extends V>\n"\
"#else\n"\
"#define KEY_VALUE_GENERIC <K>\n"\
"#define KEY_VALUE_GENERIC_DIAMOND <>\n"\
"#define KEY_VALUE_EXTENDS_GENERIC <? extends K>\n"\
"#define KEY_GENERIC_VALUE_EXTENDS_GENERIC <K>\n"\
"#define KEY_SUPER_GENERIC_VALUE_EXTENDS_GENERIC <? super K>\n"\
"#endif\n"\
"#else\n"\
"#if VALUES_REFERENCE\n"\
"#define KEY_VALUE_GENERIC <V>\n"\
"#define KEY_VALUE_GENERIC_DIAMOND <>\n"\
"#define KEY_VALUE_EXTENDS_GENERIC <? extends V>\n"\
"#define KEY_GENERIC_VALUE_EXTENDS_GENERIC <? extends V>\n"\
"#define KEY_SUPER_GENERIC_VALUE_EXTENDS_GENERIC <? extends V>\n"\
"#else\n"\
"#define KEY_VALUE_GENERIC\n"\
"#define KEY_VALUE_GENERIC_DIAMOND\n"\
"#define KEY_VALUE_EXTENDS_GENERIC\n"\
"#define KEY_GENERIC_VALUE_EXTENDS_GENERIC\n"\
"#define KEY_SUPER_GENERIC_VALUE_EXTENDS_GENERIC\n"\
"#endif\n"\
"#endif\n"\
\
"#if KEYS_REFERENCE || VALUES_REFERENCE\n"\
"#define SUPPRESS_WARNINGS_KEY_VALUE_UNCHECKED @SuppressWarnings(\"unchecked\")\n"\
"#define SUPPRESS_WARNINGS_KEY_VALUE_RAWTYPES @SuppressWarnings(\"rawtypes\")\n"\
"#define SUPPRESS_WARNINGS_KEY_VALUE_UNCHECKED_RAWTYPES @SuppressWarnings({\"rawtypes\", \"unchecked\"})\n"\
"#else\n"\
"#define SUPPRESS_WARNINGS_KEY_VALUE_UNCHECKED\n"\
"#define SUPPRESS_WARNINGS_KEY_VALUE_RAWTYPES\n"\
"#define SUPPRESS_WARNINGS_KEY_VALUE_UNCHECKED_RAWTYPES\n"\
"#endif\n"\
\
\
"/* Value methods */\n"\
\
\
"#define KEY_VALUE ${TYPE[$k]}Value\n"\
"#define KEY_WIDENED_VALUE ${TYPE[$wk]}Value\n"\
"#define VALUE_VALUE ${TYPE[$v]}Value\n"\
"#define VALUE_WIDENED_VALUE ${TYPE[$wv]}Value\n"\
\
\
"/* Interfaces (keys) */\n"\
\
\
"#define COLLECTION ${TYPE_CAP[$k]}Collection\n"\
"#define STD_KEY_COLLECTION ${TYPE_STD[$k]}Collection\n"\
"#define SET ${TYPE_CAP[$k]}Set\n"\
"#define HASH ${TYPE_CAP[$k]}Hash\n"\
"#define SORTED_SET ${TYPE_CAP[$k]}SortedSet\n"\
"#define STD_SORTED_SET ${TYPE_STD[$k]}SortedSet\n"\
"#define FUNCTION ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Function\n"\
"#define MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Map\n"\
"#define SORTED_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}SortedMap\n"\
"#if KEY_CLASS_Object && VALUE_CLASS_Object\n"\
"#define PAIR it.unimi.dsi.fastutil.Pair\n"\
"#define SORTED_PAIR it.unimi.dsi.fastutil.SortedPair\n"\
"#else\n"\
"#define PAIR ${TYPE_CAP[$k]}${TYPE_CAP[$v]}Pair\n"\
"#define SORTED_PAIR ${TYPE_CAP[$k]}${TYPE_CAP[$v]}SortedPair\n"\
"#endif\n"\
"#define MUTABLE_PAIR ${TYPE_CAP[$k]}${TYPE_CAP[$v]}MutablePair\n"\
"#define IMMUTABLE_PAIR ${TYPE_CAP[$k]}${TYPE_CAP[$v]}ImmutablePair\n"\
"#define IMMUTABLE_SORTED_PAIR ${TYPE_CAP[$k]}${TYPE_CAP[$k]}ImmutableSortedPair\n"\
"#if KEYS_REFERENCE\n"\
"#define STD_SORTED_MAP SortedMap\n"\
"#define STRATEGY Strategy\n"\
"#else\n"\
"#define STD_SORTED_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}SortedMap\n"\
"#define STRATEGY PACKAGE.${TYPE_CAP[$k]}Hash.Strategy\n"\
"#endif\n"\
"#define LIST ${TYPE_CAP[$k]}List\n"\
"#define BIG_LIST ${TYPE_CAP[$k]}BigList\n"\
"#define STACK ${TYPE_STD[$k]}Stack\n"\
"#define ATOMIC_ARRAY Atomic${CLASS[$k]}Array\n"\
"#define PRIORITY_QUEUE ${TYPE_STD[$k]}PriorityQueue\n"\
"#define INDIRECT_PRIORITY_QUEUE ${TYPE_STD[$k]}IndirectPriorityQueue\n"\
"#define INDIRECT_DOUBLE_PRIORITY_QUEUE ${TYPE_STD[$k]}IndirectDoublePriorityQueue\n"\
"#define KEY_CONSUMER ${TYPE_STD[$k]}Consumer\n"\
"#define KEY_PREDICATE ${TYPE_STD[$k]}Predicate\n"\
"#define KEY_UNARY_OPERATOR ${TYPE_STD[$k]}UnaryOperator\n"\
"#define KEY_BINARY_OPERATOR ${TYPE_STD[$k]}BinaryOperator\n"\
"#define KEY_ITERATOR ${TYPE_CAP2[$k]}Iterator\n"\
"#define KEY_WIDENED_ITERATOR ${TYPE_CAP[$wk]}Iterator\n"\
"#define KEY_ITERABLE ${TYPE_CAP2[$k]}Iterable\n"\
"#define KEY_SPLITERATOR ${TYPE_CAP2[$k]}Spliterator\n"\
"#define KEY_WIDENED_SPLITERATOR ${TYPE_CAP[$wk]}Spliterator\n"\
"#define KEY_BIDI_ITERATOR ${TYPE_CAP2[$k]}BidirectionalIterator\n"\
"#define KEY_BIDI_ITERABLE ${TYPE_CAP2[$k]}BidirectionalIterable\n"\
"#define KEY_LIST_ITERATOR ${TYPE_CAP2[$k]}ListIterator\n"\
"#define KEY_BIG_LIST_ITERATOR ${TYPE_CAP2[$k]}BigListIterator\n"\
"#define STD_KEY_ITERATOR ${TYPE_STD[$k]}Iterator\n"\
"#define STD_KEY_SPLITERATOR ${TYPE_STD[$k]}Spliterator\n"\
"#define STD_KEY_ITERABLE ${TYPE_STD[$k]}Iterable\n"\
"#define KEY_COMPARATOR ${TYPE_STD[$k]}Comparator\n"\
\
\
"/* Interfaces (values) */\n"\
\
\
"#define VALUE_COLLECTION ${TYPE_CAP[$v]}Collection\n"\
"#define VALUE_ARRAY_SET ${TYPE_CAP[$v]}ArraySet\n"\
"#define VALUE_CONSUMER ${TYPE_STD[$v]}Consumer\n"\
"#define VALUE_BINARY_OPERATOR ${TYPE_STD[$v]}BinaryOperator\n"\
"#define VALUE_ITERATOR ${TYPE_CAP2[$v]}Iterator\n"\
"#define VALUE_SPLITERATOR ${TYPE_CAP2[$v]}Spliterator\n"\
"#define VALUE_LIST_ITERATOR ${TYPE_CAP2[$v]}ListIterator\n"\
\
\
"/* Types and methods related to primitive-type support in the JDK */\n"\
\
\
"#if KEYS_PRIMITIVE && ! KEY_CLASS_Boolean\n"\
"#define JDK_PRIMITIVE_KEY_CONSUMER java.util.function.${TYPE_CAP[$wk]}Consumer\n"\
"#define JDK_PRIMITIVE_PREDICATE java.util.function.${TYPE_CAP[$wk]}Predicate\n"\
"#define JDK_PRIMITIVE_BINARY_OPERATOR java.util.function.${TYPE_CAP[$wk]}BinaryOperator\n"\
"#define JDK_PRIMITIVE_BINARY_OPERATOR_APPLY applyAs${TYPE_CAP[$wk]}\n"\
"#define JDK_PRIMITIVE_ITERATOR PrimitiveIterator.Of${TYPE_CAP[$wk]}\n"\
"#define JDK_PRIMITIVE_SPLITERATOR Spliterator.Of${TYPE_CAP[$wk]}\n"\
"#define JDK_PRIMITIVE_STREAM java.util.stream.${TYPE_CAP[$wk]}Stream\n"\
"#define JDK_PRIMITIVE_UNARY_OPERATOR java.util.function.${TYPE_CAP[$wk]}UnaryOperator\n"\
"#define JDK_PRIMITIVE_KEY_APPLY applyAs${TYPE_CAP[$wk]}\n"\
"#define JDK_KEY_TO_GENERIC_FUNCTION java.util.function.${TYPE_CAP[$wk]}Function\n"\
"#else\n"\
"#define JDK_KEY_TO_GENERIC_FUNCTION java.util.function.Function\n"\
"#endif\n"\
\
"#if VALUES_PRIMITIVE && ! VALUE_CLASS_Boolean\n"\
"#define JDK_PRIMITIVE_VALUE_CONSUMER java.util.function.${TYPE_CAP[$wv]}Consumer\n"\
"#define JDK_PRIMITIVE_VALUE_BINARY_OPERATOR java.util.function.${TYPE_CAP[$wv]}BinaryOperator\n"\
"#define JDK_PRIMITIVE_VALUE_OPERATOR_APPLY applyAs${TYPE_CAP[$wv]}\n"\
"#endif\n"\
\
$(if [[ "${CLASS[$k]}" != "" && "${CLASS[$v]}" != "" ]]; then\
	if [[ "${TYPE_CAP[$wk]}" == "Int" || "${TYPE_CAP[$wk]}" == "Long" || "${TYPE_CAP[$wk]}" == "Double" ]]; then\
		if [[ "${TYPE_CAP[$wk]}" == "${TYPE_CAP[$wv]}" ]]; then\
			echo "#define JDK_PRIMITIVE_FUNCTION java.util.function.${TYPE_CAP[$wk]}UnaryOperator\\n";\
			echo "#define JDK_PRIMITIVE_FUNCTION_APPLY applyAs${TYPE_CAP[$wv]}\\n";\
		elif [[ "${TYPE_CAP[$wv]}" == "Boolean" ]]; then\
			echo "#define JDK_PRIMITIVE_FUNCTION java.util.function.${TYPE_CAP[$wk]}Predicate\\n";\
			echo "#define JDK_PRIMITIVE_FUNCTION_APPLY test\\n";\
		elif [[ "${TYPE_CAP[$wv]}" == "Object" || "${TYPE_CAP[$wv]}" == "Reference" ]]; then\
			echo "#define JDK_PRIMITIVE_FUNCTION java.util.function.${TYPE_CAP[$wk]}Function\\n";\
			echo "#define JDK_PRIMITIVE_FUNCTION_APPLY apply\\n";\
		elif [[ "${TYPE_CAP[$wv]}" == "Int" || "${TYPE_CAP[$wv]}" == "Long" || "${TYPE_CAP[$wv]}" == "Double" ]]; then\
			echo "#define JDK_PRIMITIVE_FUNCTION java.util.function.${TYPE_CAP[$wk]}To${TYPE_CAP[$wv]}Function\\n";\
			echo "#define JDK_PRIMITIVE_FUNCTION_APPLY applyAs${TYPE_CAP[$wv]}\\n";\
		fi;\
	elif [[ "${TYPE_CAP[$wk]}" == "Object" || "${TYPE_CAP[$wk]}" == "Reference" ]]; then\
		if [[ "${TYPE_CAP[$wv]}" == "Int" || "${TYPE_CAP[$wv]}" == "Long" || "${TYPE_CAP[$wv]}" == "Double" ]]; then\
			echo "#define JDK_PRIMITIVE_FUNCTION java.util.function.To${TYPE_CAP[$wv]}Function\\n";\
			echo "#define JDK_PRIMITIVE_FUNCTION_APPLY applyAs${TYPE_CAP[$wv]}\\n";\
		elif [[ "${TYPE_CAP[$wv]}" == "Boolean" ]]; then\
			echo "#define JDK_PRIMITIVE_FUNCTION java.util.function.Predicate\\n";\
			echo "#define JDK_PRIMITIVE_FUNCTION_APPLY test\\n";\
		fi;\
	fi;\
 fi)\
\
"#if KEYS_INT_LONG_DOUBLE\n"\
"#define METHOD_ARG_KEY_CONSUMER JDK_PRIMITIVE_KEY_CONSUMER\n"\
"#define METHOD_ARG_PREDICATE JDK_PRIMITIVE_PREDICATE\n"\
"#define METHOD_ARG_KEY_UNARY_OPERATOR JDK_PRIMITIVE_UNARY_OPERATOR\n"\
"#define METHOD_ARG_KEY_BINARY_OPERATOR JDK_PRIMITIVE_BINARY_OPERATOR\n"\
"#define KEY_OPERATOR_APPLY applyAs${TYPE_CAP[$k]}\n"\
"#else\n"\
"#define METHOD_ARG_KEY_CONSUMER KEY_CONSUMER KEY_SUPER_GENERIC\n"\
"#define METHOD_ARG_PREDICATE KEY_PREDICATE KEY_SUPER_GENERIC\n"\
"#define METHOD_ARG_KEY_UNARY_OPERATOR KEY_UNARY_OPERATOR KEY_GENERIC\n"\
"#define METHOD_ARG_KEY_BINARY_OPERATOR KEY_BINARY_OPERATOR KEY_GENERIC\n"\
"#define KEY_OPERATOR_APPLY apply\n"\
"#endif\n"\
\
"#if VALUES_INT_LONG_DOUBLE\n"\
"#define METHOD_ARG_VALUE_CONSUMER JDK_PRIMITIVE_VALUE_CONSUMER\n"\
"#define METHOD_ARG_VALUE_BINARY_OPERATOR JDK_PRIMITIVE_VALUE_BINARY_OPERATOR\n"\
"#define VALUE_OPERATOR_APPLY applyAs${TYPE_CAP[$v]}\n"\
"#else\n"\
"#define METHOD_ARG_VALUE_CONSUMER VALUE_CONSUMER VALUE_SUPER_GENERIC\n"\
"#define METHOD_ARG_VALUE_BINARY_OPERATOR VALUE_PACKAGE.VALUE_BINARY_OPERATOR VALUE_GENERIC\n"\
"#define VALUE_OPERATOR_APPLY apply\n"\
"#endif\n"\
\
\
"/* Abstract implementations (keys) */\n"\
\
\
"#define ABSTRACT_COLLECTION Abstract${TYPE_CAP[$k]}Collection\n"\
"#define ABSTRACT_SET Abstract${TYPE_CAP[$k]}Set\n"\
"#define ABSTRACT_SORTED_SET Abstract${TYPE_CAP[$k]}SortedSet\n"\
"#define ABSTRACT_FUNCTION Abstract${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Function\n"\
"#define ABSTRACT_MAP Abstract${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Map\n"\
"#define ABSTRACT_FUNCTION Abstract${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Function\n"\
"#define ABSTRACT_SORTED_MAP Abstract${TYPE_CAP[$k]}2${TYPE_CAP[$v]}SortedMap\n"\
"#define ABSTRACT_LIST Abstract${TYPE_CAP[$k]}List\n"\
"#define ABSTRACT_BIG_LIST Abstract${TYPE_CAP[$k]}BigList\n"\
"#define SUBLIST ${TYPE_CAP[$k]}SubList\n"\
"#define SUBLIST_RANDOM_ACCESS ${TYPE_CAP[$k]}RandomAccessSubList\n"\
"#define ABSTRACT_PRIORITY_QUEUE Abstract${TYPE_STD[$k]}PriorityQueue\n"\
"#define ABSTRACT_STACK Abstract${TYPE_STD[$k]}Stack\n"\
"#define KEY_ABSTRACT_ITERATOR Abstract${TYPE_CAP2[$k]}Iterator\n"\
"#define KEY_ABSTRACT_SPLITERATOR Abstract${TYPE_CAP2[$k]}Spliterator\n"\
"#define KEY_ABSTRACT_BIDI_ITERATOR Abstract${TYPE_CAP2[$k]}BidirectionalIterator\n"\
"#define KEY_ABSTRACT_LIST_ITERATOR Abstract${TYPE_CAP2[$k]}ListIterator\n"\
"#define KEY_ABSTRACT_BIG_LIST_ITERATOR Abstract${TYPE_CAP2[$k]}BigListIterator\n"\
"#if KEY_CLASS_Object\n"\
"#define KEY_ABSTRACT_COMPARATOR Comparator\n"\
"#else\n"\
"#define KEY_ABSTRACT_COMPARATOR Abstract${TYPE_CAP[$k]}Comparator\n"\
"#endif\n"\
\
\
"/* Abstract implementations (values) */\n"\
\
\
"#define VALUE_ABSTRACT_COLLECTION Abstract${TYPE_CAP[$v]}Collection\n"\
"#define VALUE_ABSTRACT_ITERATOR Abstract${TYPE_CAP2[$v]}Iterator\n"\
"#define VALUE_ABSTRACT_BIDI_ITERATOR Abstract${TYPE_CAP2[$v]}BidirectionalIterator\n"\
\
\
"/* Static containers (keys) */\n"\
\
\
"#define COLLECTIONS ${TYPE_CAP[$k]}Collections\n"\
"#define SETS ${TYPE_CAP[$k]}Sets\n"\
"#define SORTED_SETS ${TYPE_CAP[$k]}SortedSets\n"\
"#define LISTS ${TYPE_CAP[$k]}Lists\n"\
"#define BIG_LISTS ${TYPE_CAP[$k]}BigLists\n"\
"#define MAPS ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Maps\n"\
"#define FUNCTIONS ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Functions\n"\
"#define SORTED_MAPS ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}SortedMaps\n"\
"#define PRIORITY_QUEUES ${TYPE_CAP2[$k]}PriorityQueues\n"\
"#define HEAPS ${TYPE_CAP2[$k]}Heaps\n"\
"#define SEMI_INDIRECT_HEAPS ${TYPE_CAP2[$k]}SemiIndirectHeaps\n"\
"#define INDIRECT_HEAPS ${TYPE_CAP2[$k]}IndirectHeaps\n"\
"#define ARRAYS ${TYPE_CAP2[$k]}Arrays\n"\
"#define BIG_ARRAYS ${TYPE_CAP2[$k]}BigArrays\n"\
"#define ITERABLES ${TYPE_CAP2[$k]}Iterables\n"\
"#define ITERATORS ${TYPE_CAP2[$k]}Iterators\n"\
"#define WIDENED_ITERATORS ${TYPE_CAP[$wk]}Iterators\n"\
"#define SPLITERATORS ${TYPE_CAP2[$k]}Spliterators\n"\
"#define WIDENED_SPLITERATORS ${TYPE_CAP[$wk]}Spliterators\n"\
"#define BIG_LIST_ITERATORS ${TYPE_CAP2[$k]}BigListIterators\n"\
"#define BIG_SPLITERATORS ${TYPE_CAP2[$k]}BigSpliterators\n"\
"#define COMPARATORS ${TYPE_CAP2[$k]}Comparators\n"\
\
\
"/* Static containers (values) */\n"\
\
\
"#define VALUE_COLLECTIONS ${TYPE_CAP[$v]}Collections\n"\
"#define VALUE_SETS ${TYPE_CAP[$v]}Sets\n"\
"#define VALUE_ARRAYS ${TYPE_CAP2[$v]}Arrays\n"\
"#define VALUE_ITERATORS ${TYPE_CAP2[$v]}Iterators\n"\
"#define VALUE_SPLITERATORS ${TYPE_CAP2[$v]}Spliterators\n"\
\
\
"/* Implementations */\n"\
\
\
"#define OPEN_HASH_SET ${TYPE_CAP[$k]}${Linked}Open${Custom}HashSet\n"\
"#define OPEN_HASH_BIG_SET ${TYPE_CAP[$k]}${Linked}Open${Custom}HashBigSet\n"\
"#define OPEN_DOUBLE_HASH_SET ${TYPE_CAP[$k]}${Linked}Open${Custom}DoubleHashSet\n"\
"#define OPEN_HASH_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}${Linked}Open${Custom}HashMap\n"\
"#define OPEN_HASH_BIG_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}${Linked}Open${Custom}HashBigMap\n"\
"#define STRIPED_OPEN_HASH_MAP Striped${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Open${Custom}HashMap\n"\
"#define OPEN_DOUBLE_HASH_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}${Linked}Open${Custom}DoubleHashMap\n"\
"#define ARRAY_SET ${TYPE_CAP[$k]}ArraySet\n"\
"#define ARRAY_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}ArrayMap\n"\
"#define LINKED_OPEN_HASH_SET ${TYPE_CAP[$k]}LinkedOpenHashSet\n"\
"#define AVL_TREE_SET ${TYPE_CAP[$k]}AVLTreeSet\n"\
"#define RB_TREE_SET ${TYPE_CAP[$k]}RBTreeSet\n"\
"#define AVL_TREE_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}AVLTreeMap\n"\
"#define RB_TREE_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}RBTreeMap\n"\
"#define ARRAY_LIST ${TYPE_CAP[$k]}ArrayList\n"\
"#define IMMUTABLE_LIST ${TYPE_CAP[$k]}ImmutableList\n"\
"#define BIG_ARRAY_BIG_LIST ${TYPE_CAP[$k]}BigArrayBigList\n"\
"#define MAPPED_BIG_LIST ${TYPE_CAP[$k]}MappedBigList\n"\
"#define ARRAY_FRONT_CODED_LIST ${TYPE_CAP[$k]}ArrayFrontCodedList\n"\
"#define ARRAY_FRONT_CODED_BIG_LIST ${TYPE_CAP[$k]}ArrayFrontCodedBigList\n"\
"#define HEAP_PRIORITY_QUEUE ${TYPE_CAP2[$k]}HeapPriorityQueue\n"\
"#define HEAP_SEMI_INDIRECT_PRIORITY_QUEUE ${TYPE_CAP2[$k]}HeapSemiIndirectPriorityQueue\n"\
"#define HEAP_INDIRECT_PRIORITY_QUEUE ${TYPE_CAP2[$k]}HeapIndirectPriorityQueue\n"\
"#define HEAP_SESQUI_INDIRECT_DOUBLE_PRIORITY_QUEUE ${TYPE_CAP2[$k]}HeapSesquiIndirectDoublePriorityQueue\n"\
"#define HEAP_INDIRECT_DOUBLE_PRIORITY_QUEUE ${TYPE_CAP2[$k]}HeapIndirectDoublePriorityQueue\n"\
"#define ARRAY_FIFO_QUEUE ${TYPE_CAP2[$k]}ArrayFIFOQueue\n"\
"#define ARRAY_PRIORITY_QUEUE ${TYPE_CAP2[$k]}ArrayPriorityQueue\n"\
"#define ARRAY_INDIRECT_PRIORITY_QUEUE ${TYPE_CAP2[$k]}ArrayIndirectPriorityQueue\n"\
"#define ARRAY_INDIRECT_DOUBLE_PRIORITY_QUEUE ${TYPE_CAP2[$k]}ArrayIndirectDoublePriorityQueue\n"\
"#define KEY_BUFFER ${TYPE_CAP[$k]}Buffer\n"\
\
\
"/* Synchronized wrappers */\n"\
\
\
"#define SYNCHRONIZED_COLLECTION Synchronized${TYPE_CAP[$k]}Collection\n"\
"#define SYNCHRONIZED_SET Synchronized${TYPE_CAP[$k]}Set\n"\
"#define SYNCHRONIZED_SORTED_SET Synchronized${TYPE_CAP[$k]}SortedSet\n"\
"#define SYNCHRONIZED_FUNCTION Synchronized${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Function\n"\
"#define SYNCHRONIZED_MAP Synchronized${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Map\n"\
"#define SYNCHRONIZED_LIST Synchronized${TYPE_CAP[$k]}List\n"\
\
\
"/* Unmodifiable wrappers */\n"\
\
\
"#define UNMODIFIABLE_COLLECTION Unmodifiable${TYPE_CAP[$k]}Collection\n"\
"#define UNMODIFIABLE_SET Unmodifiable${TYPE_CAP[$k]}Set\n"\
"#define UNMODIFIABLE_SORTED_SET Unmodifiable${TYPE_CAP[$k]}SortedSet\n"\
"#define UNMODIFIABLE_FUNCTION Unmodifiable${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Function\n"\
"#define UNMODIFIABLE_MAP Unmodifiable${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Map\n"\
"#define UNMODIFIABLE_LIST Unmodifiable${TYPE_CAP[$k]}List\n"\
"#define UNMODIFIABLE_KEY_ITERATOR Unmodifiable${TYPE_CAP[$k]}Iterator\n"\
"#define UNMODIFIABLE_KEY_BIDI_ITERATOR Unmodifiable${TYPE_CAP[$k]}BidirectionalIterator\n"\
"#define UNMODIFIABLE_KEY_LIST_ITERATOR Unmodifiable${TYPE_CAP[$k]}ListIterator\n"\
\
\
"/* Other wrappers */\n"\
\
\
"#define KEY_READER_WRAPPER ${TYPE_CAP[$k]}ReaderWrapper\n"\
"#define KEY_DATA_INPUT_WRAPPER ${TYPE_CAP[$k]}DataInputWrapper\n"\
"#define KEY_DATA_NIO_INPUT_WRAPPER ${TYPE_CAP[$k]}DataNioInputWrapper\n"\
\
\
"/* Methods (keys) */\n"\
\
\
"#define NEXT_KEY next${TYPE_STD[$k]}\n"\
"#define PREV_KEY previous${TYPE_STD[$k]}\n"\
"#define NEXT_KEY_WIDENED next${TYPE_STD[$wk]}\n"\
"#define PREV_KEY_WIDENED previous${TYPE_STD[$wk]}\n"\
"#define KEY_WIDENED_ITERATOR_METHOD ${TYPE_LC[$wk]}Iterator\n"\
"#define KEY_WIDENED_SPLITERATOR_METHOD ${TYPE_LC[$wk]}Spliterator\n"\
"#define KEY_WIDENED_STREAM_METHOD ${TYPE_LC[$wk]}Stream\n"\
"#define KEY_WIDENED_PARALLEL_STREAM_METHOD ${TYPE_LC[$wk]}ParallelStream\n"\
"#define FIRST_KEY first${TYPE_STD[$k]}Key\n"\
"#define LAST_KEY last${TYPE_STD[$k]}Key\n"\
"#define GET_KEY get${TYPE_STD[$k]}\n"\
"#define AS_KEY_BUFFER as${TYPE_STD[$k]}Buffer\n"\
"#define PAIR_LEFT left${TYPE_STD[$k]}\n"\
"#define PAIR_FIRST first${TYPE_STD[$k]}\n"\
"#define PAIR_KEY key${TYPE_STD[$k]}\n"\
"#define REMOVE_KEY remove${TYPE_STD[$k]}\n"\
"#define READ_KEY read${TYPE_CAP2[$k]}\n"\
"#define WRITE_KEY write${TYPE_CAP2[$k]}\n"\
"#define DEQUEUE dequeue${TYPE_STD[$k]}\n"\
"#define DEQUEUE_LAST dequeueLast${TYPE_STD[$k]}\n"\
"#define SINGLETON_METHOD ${TYPE_LC[$k]}Singleton\n"\
"#define FIRST first${TYPE_STD[$k]}\n"\
"#define LAST last${TYPE_STD[$k]}\n"\
"#define TOP top${TYPE_STD[$k]}\n"\
"#define PEEK peek${TYPE_STD[$k]}\n"\
"#define POP pop${TYPE_STD[$k]}\n"\
"#define KEY_EMPTY_ITERATOR_METHOD empty${TYPE_CAP2[$k]}Iterator\n"\
"#define KEY_EMPTY_SPLITERATOR_METHOD empty${TYPE_CAP2[$k]}Spliterator\n"\
"#define AS_KEY_ITERATOR as${TYPE_CAP2[$k]}Iterator\n"\
"#define AS_KEY_SPLITERATOR as${TYPE_CAP2[$k]}Spliterator\n"\
"#define AS_KEY_COMPARATOR as${TYPE_CAP2[$k]}Comparator\n"\
"#define AS_KEY_ITERABLE as${TYPE_CAP2[$k]}Iterable\n"\
"#define AS_KEY_WIDENED_ITERATOR as${TYPE_CAP2[$wk]}Iterator\n"\
"#define AS_KEY_WIDENED_SPLITERATOR as${TYPE_CAP2[$wk]}Spliterator\n"\
"#define TO_KEY_ARRAY to${TYPE_STD[$k]}Array\n"\
"#define ENTRY_GET_KEY get${TYPE_STD[$k]}Key\n"\
"#define REMOVE_FIRST_KEY removeFirst${TYPE_STD[$k]}\n"\
"#define REMOVE_LAST_KEY removeLast${TYPE_STD[$k]}\n"\
"#define PARSE_KEY parse${TYPE_STD[$k]}\n"\
"#define LOAD_KEYS load${TYPE_STD[$k]}s\n"\
"#define LOAD_KEYS_BIG load${TYPE_STD[$k]}sBig\n"\
"#define STORE_KEYS store${TYPE_STD[$k]}s\n"\
"#if KEYS_REFERENCE\n"\
"#define MAP_TO_KEY map\n"\
"#define MAP_TO_KEY_WIDENED map\n"\
"#define RETURN_FALSE_IF_KEY_NULL(k) if (k == null) return false;\n"\
"#define REQUIRE_KEY_NON_NULL(k) java.util.Objects.requireNonNull(k);\n"\
"#else\n"\
"#define MAP_TO_KEY mapTo${TYPE_CAP2[$k]}\n"\
"#define MAP_TO_KEY_WIDENED mapTo${TYPE_CAP2[$wk]}\n"\
"#define REQUIRE_KEY_NON_NULL(k)\n"\
"#define RETURN_FALSE_IF_KEY_NULL(k)\n"\
"#endif\n"\
\
\
"/* Methods (values) */\n"\
\
\
"#define MERGE_VALUE merge${TYPE_STD[$v]}\n"\
"#define NEXT_VALUE next${TYPE_STD[$v]}\n"\
"#define PREV_VALUE previous${TYPE_STD[$v]}\n"\
"#define READ_VALUE read${TYPE_CAP2[$v]}\n"\
"#define WRITE_VALUE write${TYPE_CAP2[$v]}\n"\
"#define ENTRY_GET_VALUE get${TYPE_STD[$v]}Value\n"\
"#define REMOVE_FIRST_VALUE removeFirst${TYPE_STD[$v]}\n"\
"#define REMOVE_LAST_VALUE removeLast${TYPE_STD[$v]}\n"\
"#define AS_VALUE_ITERATOR as${TYPE_CAP2[$v]}Iterator\n"\
"#define AS_VALUE_SPLITERATOR as${TYPE_CAP2[$v]}Spliterator\n"\
"#define PAIR_RIGHT right${TYPE_STD[$v]}\n"\
"#define PAIR_SECOND second${TYPE_STD[$v]}\n"\
"#define PAIR_VALUE value${TYPE_STD[$v]}\n"\
"#if VALUES_REFERENCE\n"\
"#define REQUIRE_VALUE_NON_NULL(v) java.util.Objects.requireNonNull(v);\n"\
"#else\n"\
"#define REQUIRE_VALUE_NON_NULL(v)\n"\
"#endif\n"\
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
"#if KEYS_REFERENCE\n"\
"#define GET_VALUE get${TYPE_STD[$v]}\n"\
"#define REMOVE_VALUE remove${TYPE_STD[$v]}\n"\
"#define COMPUTE_IF_ABSENT_JDK compute${TYPE_CAP[$v]}IfAbsent\n"\
"#define COMPUTE_IF_ABSENT_NULLABLE compute${TYPE_CAP[$v]}IfAbsentNullable\n"\
"#define COMPUTE_IF_ABSENT_PARTIAL compute${TYPE_CAP[$v]}IfAbsentPartial\n"\
"#define COMPUTE compute${TYPE_STD[$v]}\n"\
"#define COMPUTE_IF_PRESENT compute${TYPE_STD[$v]}IfPresent\n"\
"#define MERGE merge${TYPE_STD[$v]}\n"\
"#else\n"\
"#define GET_VALUE get\n"\
"#define REMOVE_VALUE remove\n"\
"#define COMPUTE_IF_ABSENT_JDK computeIfAbsent\n"\
"#define COMPUTE_IF_ABSENT_NULLABLE computeIfAbsentNullable\n"\
"#define COMPUTE_IF_ABSENT_PARTIAL computeIfAbsentPartial\n"\
"#define COMPUTE compute\n"\
"#define COMPUTE_IF_PRESENT computeIfPresent\n"\
"#endif\n"\
\
\
\
"/* Equality */\n"\
\
\
\
"#define KEY_EQUALS_NOT_NULL_CAST(x,y) KEY_EQUALS_NOT_NULL(x,y)\n"\
"#define KEY2INTHASH_CAST(x) KEY2INTHASH(x)\n"\
"#if KEY_CLASS_Object\n"\
"#define KEY_EQUALS(x,y) java.util.Objects.equals(x, y)\n"\
"#define KEY_EQUALS_NOT_NULL(x,y) ( (x).equals(y) )\n"\
"#define KEY_IS_NULL(x) ( (x) == null )\n"\
"#elif KEY_CLASS_Float\n"\
"#define KEY_EQUALS(x,y) ( Float.floatToIntBits(x) == Float.floatToIntBits(y) )\n"\
"#define KEY_EQUALS_NOT_NULL(x,y) ( Float.floatToIntBits(x) == Float.floatToIntBits(y) )\n"\
"#define KEY_IS_NULL(x) ( Float.floatToIntBits(x) == 0 )\n"\
"#elif KEY_CLASS_Double\n"\
"#define KEY_EQUALS(x,y) ( Double.doubleToLongBits(x) == Double.doubleToLongBits(y) )\n"\
"#define KEY_EQUALS_NOT_NULL(x,y) ( Double.doubleToLongBits(x) == Double.doubleToLongBits(y) )\n"\
"#define KEY_IS_NULL(x) ( Double.doubleToLongBits(x) == 0 )\n"\
"#else\n"\
"#define KEY_EQUALS(x,y) ( (x) == (y) )\n"\
"#define KEY_EQUALS_NOT_NULL(x,y) ( (x) == (y) )\n"\
"#define KEY_IS_NULL(x) ( (x) == KEY_NULL )\n"\
"#endif\n"\
\
"#ifdef Custom\n"\
"#undef KEY_EQUALS\n"\
"#define KEY_EQUALS(x,y) ( strategy.equals( (x), (y) ) )\n"\
"#undef KEY_EQUALS_NOT_NULL\n"\
"#define KEY_EQUALS_NOT_NULL(x,y) ( strategy.equals( (x), (y) ) )\n"\
"#undef KEY_EQUALS_NOT_NULL_CAST\n"\
"#define KEY_EQUALS_NOT_NULL_CAST(x,y) ( strategy.equals( " KEY_GENERIC_CAST "(x), (y) ) )\n"\
"#define KEY_EQUALS_NULL(x) ( strategy.equals( (x), KEY_NULL ) )\n"\
"#else\n"\
"#define KEY_EQUALS_NULL(x) KEY_IS_NULL(x)\n"\
"#endif\n"\
\
"#define VALUE_EQUALS_NOT_NULL_CAST(x,y) VALUE_EQUALS_NOT_NULL(x,y)\n"\
"#define VALUE2INTHASH_CAST(x) VALUE2INTHASH(x)\n"\
"#if VALUE_CLASS_Object\n"\
"#define VALUE_EQUALS(x,y) java.util.Objects.equals(x, y)\n"\
"#define VALUE_EQUALS_NOT_NULL(x,y) ( (x).equals(y) )\n"\
"#define VALUE_IS_NULL(x) ( (x) == null )\n"\
"#elif VALUE_CLASS_Float\n"\
"#define VALUE_EQUALS(x,y) ( Float.floatToIntBits(x) == Float.floatToIntBits(y) )\n"\
"#define VALUE_EQUALS_NOT_NULL(x,y) ( Float.floatToIntBits(x) == Float.floatToIntBits(y) )\n"\
"#define VALUE_IS_NULL(x) ( Float.floatToIntBits(x) == 0 )\n"\
"#elif VALUE_CLASS_Double\n"\
"#define VALUE_EQUALS(x,y) ( Double.doubleToLongBits(x) == Double.doubleToLongBits(y) )\n"\
"#define VALUE_EQUALS_NOT_NULL(x,y) ( Double.doubleToLongBits(x) == Double.doubleToLongBits(y) )\n"\
"#define VALUE_IS_NULL(x) ( Double.doubleToLongBits(x) == 0 )\n"\
"#else\n"\
"#define VALUE_EQUALS(x,y) ( (x) == (y) )\n"\
"#define VALUE_EQUALS_NOT_NULL(x,y) ( (x) == (y) )\n"\
"#define VALUE_IS_NULL(x) ( (x) == VALUE_NULL )\n"\
"#endif\n"\
\
\
\
"/* Object/Reference-only definitions (keys) */\n"\
\
\
"#if KEYS_REFERENCE\n"\
\
"#define REMOVE remove\n"\
\
"#define KEY_OBJ2TYPE(x) (x)\n"\
"#define KEY_CLASS2TYPE(x) (x)\n"\
"#define KEY2OBJ(x) (x)\n"\
\
"#ifdef Custom\n"\
"#define KEY2JAVAHASH_NOT_NULL(x) ( strategy.hashCode(x) )\n"\
"#define KEY2INTHASH(x) ( it.unimi.dsi.fastutil.HashCommon.mix( strategy.hashCode(x) ) )\n"\
"#undef KEY2INTHASH_CAST\n"\
"#define KEY2INTHASH_CAST(x) ( it.unimi.dsi.fastutil.HashCommon.mix( strategy.hashCode( " KEY_GENERIC_CAST " x) ) )\n"\
"#define KEY2LONGHASH(x) ( it.unimi.dsi.fastutil.HashCommon.mix( (long)( strategy.hashCode(x)) ) ) )\n"\
"#elif KEY_CLASS_Object\n"\
"#define KEY2JAVAHASH_NOT_NULL(x) ( (x).hashCode() )\n"\
"#define KEY2JAVAHASH(x) ( (x) == null ? 0 : (x).hashCode() )\n"\
"#define KEY2INTHASH(x) ( it.unimi.dsi.fastutil.HashCommon.mix( (x).hashCode() ) )\n"\
"#define KEY2LONGHASH(x) ( it.unimi.dsi.fastutil.HashCommon.mix( (long)( (x).hashCode() ) ) )\n"\
"#else\n"\
"#define KEY2JAVAHASH_NOT_NULL(x) ( System.identityHashCode(x) )\n"\
"#define KEY2INTHASH(x) ( it.unimi.dsi.fastutil.HashCommon.mix( System.identityHashCode(x) ) )\n"\
"#define KEY2LONGHASH(x) ( it.unimi.dsi.fastutil.HashCommon.mix( (long)( System.identityHashCode(x) ) ) )\n"\
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
"#define KEY_CLASS2TYPE(x) (x).KEY_VALUE()\n"\
"#define KEY_OBJ2TYPE(x) KEY_CLASS2TYPE((KEY_CLASS)(x))\n"\
"#define KEY2OBJ(x) KEY_CLASS.valueOf(x)\n"\
\
"#if KEY_CLASS_Boolean\n"\
"#define KEY_CMP_EQ(x,y) ( (x) == (y) )\n"\
"#define KEY_NULL (false)\n"\
"#define KEY_CMP(x,y) ( KEY_CLASS.compare((x),(y)) )\n"\
"#define KEY_LESS(x,y) ( !(x) && (y) )\n"\
"#define KEY_LESSEQ(x,y) ( !(x) || (y) )\n"\
"#else\n"\
"#if KEY_CLASS_Byte || KEY_CLASS_Short || KEY_CLASS_Character\n"\
"#define KEY_NULL ((KEY_TYPE)0)\n"\
"#else\n"\
"#define KEY_NULL (0)\n"\
"#endif\n"\
"#if KEY_CLASS_Float || KEY_CLASS_Double\n"\
"#define KEY_CMP_EQ(x,y) ( KEY_CLASS.compare((x),(y)) == 0 )\n"\
"#define KEY_CMP(x,y) ( KEY_CLASS.compare((x),(y)) )\n"\
"#define KEY_LESS(x,y) ( KEY_CLASS.compare((x),(y)) < 0 )\n"\
"#define KEY_LESSEQ(x,y) ( KEY_CLASS.compare((x),(y)) <= 0 )\n"\
"#else\n"\
"#define KEY_CMP_EQ(x,y) ( (x) == (y) )\n"\
"#define KEY_CMP(x,y) ( KEY_CLASS.compare((x),(y)) )\n"\
"#define KEY_LESS(x,y) ( (x) < (y) )\n"\
"#define KEY_LESSEQ(x,y) ( (x) <= (y) )\n"\
"#endif\n"\
\
"#if KEY_CLASS_Float\n"\
"#define KEY2LEXINT(x) fixFloat(x)\n"\
"#elif KEY_CLASS_Double\n"\
"#define KEY2LEXINT(x) fixDouble(x)\n"\
"#else\n"\
"#define KEY2LEXINT(x) (x)\n"\
"#endif\n"\
\
"#endif\n"\
\
"#ifdef Custom\n"\
"#define KEY2JAVAHASH_NOT_NULL(x) ( strategy.hashCode(x) )\n"\
"#define KEY2INTHASH(x) ( it.unimi.dsi.fastutil.HashCommon.mix( strategy.hashCode(x) ) )\n"\
"#define KEY2LONGHASH(x) ( it.unimi.dsi.fastutil.HashCommon.mix( (long)( strategy.hashCode(x) ) ) )\n"\
"#else\n"\
\
"#if KEY_CLASS_Float\n"\
"#define KEY2JAVAHASH_NOT_NULL(x) it.unimi.dsi.fastutil.HashCommon.float2int(x)\n"\
"#define KEY2INTHASH(x) it.unimi.dsi.fastutil.HashCommon.mix( it.unimi.dsi.fastutil.HashCommon.float2int(x) )\n"\
"#define KEY2LONGHASH(x) it.unimi.dsi.fastutil.HashCommon.mix( (long)( it.unimi.dsi.fastutil.HashCommon.float2int(x) ) )\n"\
"#define INT(x) (x)\n"\
"#elif KEY_CLASS_Double\n"\
"#define KEY2JAVAHASH_NOT_NULL(x) it.unimi.dsi.fastutil.HashCommon.double2int(x)\n"\
"#define KEY2INTHASH(x) (int)it.unimi.dsi.fastutil.HashCommon.mix( Double.doubleToRawLongBits(x) )\n"\
"#define KEY2LONGHASH(x) it.unimi.dsi.fastutil.HashCommon.mix( Double.doubleToRawLongBits(x) )\n"\
"#define INT(x) (int)(x)\n"\
"#elif KEY_CLASS_Long\n"\
"#define KEY2JAVAHASH_NOT_NULL(x) it.unimi.dsi.fastutil.HashCommon.long2int(x)\n"\
"#define KEY2INTHASH(x) (int)it.unimi.dsi.fastutil.HashCommon.mix( (x) )\n"\
"#define KEY2LONGHASH(x) it.unimi.dsi.fastutil.HashCommon.mix( (x) )\n"\
"#define INT(x) (int)(x)\n"\
"#elif KEY_CLASS_Boolean\n"\
"#define KEY2JAVAHASH_NOT_NULL(x) ((x) ? 1231 : 1237)\n"\
"#define KEY2INTHASH(x) ((x) ? 0xfab5368 : 0xcba05e7b)\n"\
"#define KEY2LONGHASH(x) ((x) ? 0x74a19fc8b6428188L : 0xbaeca2031a4fd9ecL)\n"\
"#else\n"\
"#define KEY2JAVAHASH_NOT_NULL(x) (x)\n"\
"#define KEY2INTHASH(x) ( it.unimi.dsi.fastutil.HashCommon.mix( (x) ) )\n"\
"#define KEY2LONGHASH(x) ( it.unimi.dsi.fastutil.HashCommon.mix( (long)( (x) ) ) )\n"\
"#define INT(x) (x)\n"\
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
"#if VALUES_REFERENCE\n"\
"#define VALUE_OBJ2TYPE(x) (x)\n"\
"#define VALUE_CLASS2TYPE(x) (x)\n"\
"#define VALUE2OBJ(x) (x)\n"\
\
"#if VALUE_CLASS_Object\n"\
"#define VALUE2JAVAHASH(x) ( (x) == null ? 0 : (x).hashCode() )\n"\
"#else\n"\
"#define VALUE2JAVAHASH(x) ( (x) == null ? 0 : System.identityHashCode(x) )\n"\
"#endif\n"\
\
"#define VALUE_NULL (null)\n"\
\
"#else\n"\
\
\
"/* Primitive-type-only definitions (values) */\n"\
\
\
"#define VALUE_CLASS2TYPE(x) (x).VALUE_VALUE()\n"\
"#define VALUE_OBJ2TYPE(x) VALUE_CLASS2TYPE((VALUE_CLASS)(x))\n"\
"#define VALUE2OBJ(x) VALUE_CLASS.valueOf(x)\n"\
\
"#if VALUE_CLASS_Float || VALUE_CLASS_Double || VALUE_CLASS_Long\n"\
"#define VALUE_NULL (0)\n"\
"#define VALUE2JAVAHASH(x) it.unimi.dsi.fastutil.HashCommon.${TYPE[$v]}2int(x)\n"\
"#elif VALUE_CLASS_Boolean\n"\
"#define VALUE_NULL (false)\n"\
"#define VALUE2JAVAHASH(x) (x ? 1231 : 1237)\n"\
"#else\n"\
"#if VALUE_CLASS_Integer\n"\
"#define VALUE_NULL (0)\n"\
"#else\n"\
"#define VALUE_NULL ((VALUE_TYPE)0)\n"\
"#endif\n"\
"#define VALUE2JAVAHASH(x) (x)\n"\
"#endif\n"\
\
"#endif\n"\
\
"/* START_OF_JAVA_SOURCE */\n"\
"#include \"$1\"\n"
