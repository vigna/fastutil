#!/bin/sh

#
# This script generates from driver files fake C sources to be passed
# through a C preprocessor to get the actual Java sources.
#

DIR="it/unimi/dsi/fastUtil"

# Names of driver files.
FILE=(Map AbstractMap HashMap AbstractCollection AbstractSet Collection Set HashSet Iterator)

# The primitive types we specialize to.
TYPE=(boolean byte short int long char float double Object)

# The same types, but capitalized (to build method names).
TYPE_CAP=(Boolean Byte Short Int Long Char Float Double Object)

# The corresponding classes (in few cases, there are differences with $TYPE_CAP).
CLASS=(Boolean Byte Short Integer Long Character Float Double Object)

#
# This loop generates maps. Note that the index in the key
# array starts from 1, so we avoid boolean keys.
#

for ((f=0; f<3; f++)); do
	 for ((k=1; k<${#TYPE[*]}; k++)); do
		  for ((v=0; v<${#TYPE[*]}; v++)); do
				FILENAME=$DIR/${TYPE_CAP[$k]}2${TYPE_CAP[$v]}${FILE[$f]}.c
				rm -f $FILENAME
				echo -e \
"#assert keyclass(${CLASS[$k]})\n"\
"#assert valueclass(${CLASS[$v]})\n"\
"#define KEY_TYPE ${TYPE[$k]}\n"\
"#define KEY_CLASS ${CLASS[$k]}\n"\
"#define VALUE_TYPE ${TYPE[$v]}\n"\
"#define VALUE_CLASS ${CLASS[$v]}\n"\
"#define WRITE_KEY write${TYPE_CAP[$k]}\n"\
"#define WRITE_VALUE write${TYPE_CAP[$v]}\n"\
"#define READ_KEY read${TYPE_CAP[$k]}\n"\
"#define READ_VALUE read${TYPE_CAP[$v]}\n"\
"#define GET_VALUE get${TYPE_CAP[$v]}\n"\
"#define REMOVE_VALUE remove${TYPE_CAP[$v]}\n"\
"#define KEY_VALUE ${TYPE[$k]}Value\n"\
"#define VALUE_VALUE ${TYPE[$v]}Value\n"\
"#define MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Map\n"\
"#define ABSTRACT_MAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}AbstractMap\n"\
"#define KEY_ABSTRACT_SET ${TYPE_CAP[$k]}AbstractSet\n\n"\
"#define VALUE_ABSTRACT_COLLECTION ${TYPE_CAP[$v]}AbstractCollection\n\n"\
"#define HASHMAP ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}HashMap\n\n"\
"#if #keyclass(Object)\n"\
"#define KEY2TYPE(x) (x)\n"\
"#define KEY2OBJ(x) (x)\n"\
"#define ENTRY_GET_KEY getKey\n"\
"#define KEY_NULL (null)\n"\
"#define HASH(x) (x == null ? 0 : x.hashCode())\n"\
"#define KEY_EQUAL(x,y) ((x) == null ? (y) == null : (x).equals((y)))\n"\
"#define KEY_ITERATOR Iterator\n\n"\
"#define NEXT_KEY next\n"\
"#else\n"\
"#define KEY2TYPE(x) (((KEY_CLASS)(x)).KEY_VALUE())\n"\
"#define KEY2OBJ(x) (new KEY_CLASS(x))\n"\
"#define ENTRY_GET_KEY get${TYPE_CAP[$k]}Key\n"\
"#define HASH(x) (x)\n"\
"#define KEY_EQUAL(x,y) ((x) == (y))\n"\
"#define KEY_ITERATOR ${TYPE_CAP[$k]}Iterator\n\n"\
"#define NEXT_KEY next${TYPE_CAP[$k]}\n"\
"#if #keyclass(Boolean)\n"\
"#define KEY_NULL (false)\n"\
"#else\n"\
"#define KEY_NULL ((KEY_TYPE)0)\n"\
"#endif\n"\
"#endif\n\n"\
"#if #valueclass(Object)\n"\
"#define VALUE2TYPE(x) (x)\n"\
"#define VALUE2OBJ(x) (x)\n"\
"#define ENTRY_GET_VALUE getValue\n"\
"#define VALUE_NULL (null)\n"\
"#define DEF_RET_VALUE null\n"\
"#define VALUE_EQUAL(x,y) ((x) == null ? (y) == null : (x).equals((y)))\n"\
"#define VALUE_ITERATOR Iterator\n\n"\
"#define NEXT_VALUE next\n"\
"#else\n"\
"#define VALUE2TYPE(x) (((VALUE_CLASS)(x)).VALUE_VALUE())\n"\
"#define VALUE2OBJ(x) (new VALUE_CLASS(x))\n"\
"#define ENTRY_GET_VALUE get${TYPE_CAP[$v]}Value\n"\
"#define VALUE_EQUAL(x,y) ((x) == (y))\n"\
"#define VALUE_ITERATOR ${TYPE_CAP[$v]}Iterator\n\n"\
"#define NEXT_VALUE next${TYPE_CAP[$v]}\n"\
"#if #valueclass(Boolean)\n"\
"#define VALUE_NULL (false)\n"\
"#else\n"\
"#define VALUE_NULL ((VALUE_TYPE)0)\n"\
"#endif\n"\
"#define DEF_RET_VALUE defRetValue\n"\
"#endif\n\n"\
"#include \"${FILE[$f]}.drv\"\n" >$FILENAME
			 done
	  done
done


#
# This loop generates sets. Note that the index in the key
# array starts from 0, because we need boolean Collections, 
# so we manually delete boolean sets definitions later.
#

for ((f=3; f<8; f++)); do
	 for ((k=0; k<${#TYPE[*]}; k++)); do
					 FILENAME=$DIR/${TYPE_CAP[$k]}${FILE[$f]}.c
					 rm -f $FILENAME
					 echo -e \
"#assert keyclass(${CLASS[$k]})\n"\
"#define KEY_TYPE ${TYPE[$k]}\n"\
"#define KEY_CLASS ${CLASS[$k]}\n"\
"#define WRITE_KEY write${TYPE_CAP[$k]}\n"\
"#define READ_KEY read${TYPE_CAP[$k]}\n"\
"#define KEY_VALUE ${TYPE[$k]}Value\n"\
"#define SET ${TYPE_CAP[$k]}Set\n\n"\
"#define COLLECTION ${TYPE_CAP[$k]}Collection\n\n"\
"#define ABSTRACT_SET ${TYPE_CAP[$k]}AbstractSet\n\n"\
"#define ABSTRACT_COLLECTION ${TYPE_CAP[$k]}AbstractCollection\n\n"\
"#define HASHSET ${TYPE_CAP[$k]}HashSet\n\n"\
"#if #keyclass(Object)\n"\
"#define KEY_ITERATOR Iterator\n\n"\
"#define NEXT_KEY next\n"\
"#define KEY2TYPE(x) (x)\n"\
"#define KEY2OBJ(x) (x)\n"\
"#define ENTRY_GET_KEY getKey\n"\
"#define TO_KEY_ARRAY toArray\n"\
"#define KEY_NULL (null)\n"\
"#define HASH(x) (x == null ? 0 : x.hashCode())\n"\
"#define KEY_EQUAL(x,y) ((x) == null ? (y) == null : (x).equals((y)))\n"\
"#else\n"\
"#define KEY_ITERATOR ${TYPE_CAP[$k]}Iterator\n\n"\
"#define NEXT_KEY next${TYPE_CAP[$k]}\n"\
"#define KEY2TYPE(x) (((KEY_CLASS)(x)).KEY_VALUE())\n"\
"#define KEY2OBJ(x) (new KEY_CLASS(x))\n"\
"#define ENTRY_GET_KEY get${TYPE_CAP[$k]}Key\n"\
"#define TO_KEY_ARRAY to${TYPE_CAP[$k]}Array\n"\
"#define HASH(x) (x)\n"\
"#define KEY_EQUAL(x,y) ((x) == (y))\n"\
"#if #keyclass(Boolean)\n"\
"#define KEY_NULL (false)\n"\
"#else\n"\
"#define KEY_NULL ((KEY_TYPE)0)\n"\
"#endif\n"\
"#endif\n\n"\
"#include \"${FILE[$f]}.drv\"\n" >$FILENAME
	  done
done

rm -f $DIR/BooleanSet.c
rm -f $DIR/BooleanAbstractSet.c
rm -f $DIR/BooleanHashSet.c



#
# This loop generates iterator interfaces. Note that we need
# boolean iterators for maps with booleans as codomain.
#

for ((f=8; f<9; f++)); do
	 for ((k=0; k<$((${#TYPE[*]}-1)); k++)); do
					 FILENAME=$DIR/${TYPE_CAP[$k]}${FILE[$f]}.c
					 rm -f $FILENAME
					 echo -e \
"#assert keyclass(${CLASS[$k]})\n"\
"#define KEY_TYPE ${TYPE[$k]}\n"\
"#define KEY_CLASS ${CLASS[$k]}\n"\
"#define NEXT_KEY next${TYPE_CAP[$k]}\n"\
"#define KEY_ITERATOR ${TYPE_CAP[$k]}Iterator\n\n"\
"#include \"${FILE[$f]}.drv\"\n" >$FILENAME

	  done
done

