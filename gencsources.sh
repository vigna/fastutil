#!/bin/sh

DIR="it/unimi/dsi/fastMaps"
FILE=(Map HashMap Set HashSet)
TYPE=(boolean byte short int long char float double Object)
TYPE_CAP=(Boolean Byte Short Int Long Char Float Double Object)
CLASS=(Boolean Byte Short Integer Long Character Float Double Object)

for ((f=0; f<2; f++)); do
	 for ((k=1; k<${#TYPE[*]}; k++)); do
		  for ((v=0; v<${#TYPE[*]}; v++)); do
				if (( k < $((${#TYPE[*]}-1)) || v < $((${#TYPE[*]}-1)) )); then
					 FILENAME=$DIR/${TYPE_CAP[$k]}2${TYPE_CAP[$v]}${FILE[$f]}.c
					 rm -f $FILENAME
					 echo -e \
"#assert keyclass(${CLASS[$k]})\n"\
"#assert valueclass(${CLASS[$v]})\n"\
"#define KEY_TYPE ${TYPE[$k]}\n"\
"#define KEY_TYPE_CAP ${TYPE_CAP[$k]}\n"\
"#define KEY_CLASS ${CLASS[$k]}\n"\
"#define VALUE_TYPE ${TYPE[$v]}\n"\
"#define VALUE_TYPE_CAP ${TYPE_CAP[$v]}\n"\
"#define VALUE_CLASS ${CLASS[$v]}\n"\
"#define WRITE_KEY_TYPE_CAP write${TYPE_CAP[$k]}\n"\
"#define WRITE_VALUE_TYPE_CAP write${TYPE_CAP[$v]}\n"\
"#define READ_KEY_TYPE_CAP read${TYPE_CAP[$k]}\n"\
"#define READ_VALUE_TYPE_CAP read${TYPE_CAP[$v]}\n"\
"#define GET_VALUE_TYPE_CAP get${TYPE_CAP[$v]}\n"\
"#define REMOVE_VALUE_TYPE_CAP remove${TYPE_CAP[$v]}\n"\
"#define FIRST_KEY_TYPE_CAP_KEY first${TYPE_CAP[$k]}Key\n"\
"#define LAST_KEY_TYPE_CAP_KEY last${TYPE_CAP[$k]}Key\n"\
"#define KEY_TYPE_VALUE ${TYPE[$k]}Value\n"\
"#define VALUE_TYPE_VALUE ${TYPE[$v]}Value\n"\
"#define NEXT_KEY_TYPE_CAP next${TYPE_CAP[$k]}\n"\
"#define NEXT_VALUE_TYPE_CAP next${TYPE_CAP[$v]}\n"\
"#define MAP_NAME ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Map\n"\
"#define SORTEDMAP_NAME ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}SortedMap\n"\
"#define ENTRY_NAME ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}Entry\n"\
"#define TREEMAP_NAME ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}TreeMap\n\n"\
"#define HASHMAP_NAME ${TYPE_CAP[$k]}2${TYPE_CAP[$v]}HashMap\n\n"\
"#if #keyclass(Object)\n"\
"#define KEY2TYPE(x) (x)\n"\
"#define KEY2OBJ(x) (x)\n"\
"#define HASH(x) (x == null ? 0 : x.hashCode())\n"\
"#define KEY_EQUAL(x,y) ((x) == (y) || ((x) != null && (x).equals((y))))\n"\
"#else\n"\
"#define KEY2TYPE(x) (((KEY_CLASS)(x)).KEY_TYPE_VALUE())\n"\
"#define KEY2OBJ(x) (new KEY_CLASS(x))\n"\
"#define HASH(x) (x)\n"\
"#define KEY_EQUAL(x,y) ((x) == (y))\n"\
"#endif\n\n"\
"#if #valueclass(Object)\n"\
"#define VALUE2TYPE(x) (x)\n"\
"#define VALUE2OBJ(x) (x)\n"\
"#define NULL (null)\n"\
"#define DEF_RET_VALUE null\n"\
"#define VALUE_EQUAL(x,y) ((x) == (y) || ((x) != null && (x).equals((y))))\n"\
"#else\n"\
"#define VALUE2TYPE(x) (((VALUE_CLASS)(x)).VALUE_TYPE_VALUE())\n"\
"#define VALUE2OBJ(x) (new VALUE_CLASS(x))\n"\
"#define VALUE_EQUAL(x,y) ((x) == (y))\n"\
"#if #valueclass(Boolean)\n"\
"#define NULL (false)\n"\
"#else\n"\
"#define NULL ((VALUE_TYPE)0)\n"\
"#endif\n"\
"#define DEF_RET_VALUE defRetValue\n"\
"#endif\n\n"\
"#include \"${FILE[$f]}.drv\"\n" >$FILENAME
				fi
			 done
	  done
done

for ((f=2; f<4; f++)); do
	 for ((k=1; k<${#TYPE[*]}; k++)); do
					 FILENAME=$DIR/${TYPE_CAP[$k]}${FILE[$f]}.c
					 rm -f $FILENAME
					 echo -e \
"#assert keyclass(${CLASS[$k]})\n"\
"#define KEY_TYPE ${TYPE[$k]}\n"\
"#define KEY_TYPE_CAP ${TYPE_CAP[$k]}\n"\
"#define KEY_CLASS ${CLASS[$k]}\n"\
"#define WRITE_KEY_TYPE_CAP write${TYPE_CAP[$k]}\n"\
"#define READ_KEY_TYPE_CAP read${TYPE_CAP[$k]}\n"\
"#define KEY_TYPE_VALUE ${TYPE[$k]}Value\n"\
"#define NEXT_KEY_TYPE_CAP next${TYPE_CAP[$k]}\n"\
"#define TO_KEY_TYPE_CAP_ARRAY to${TYPE_CAP[$k]}Array\n"\
"#define SET_NAME ${TYPE_CAP[$k]}Set\n\n"\
"#define HASHSET_NAME ${TYPE_CAP[$k]}HashSet\n\n"\
"#if #keyclass(Object)\n"\
"#define KEY2TYPE(x) (x)\n"\
"#define KEY2OBJ(x) (x)\n"\
"#define HASH(x) (x == null ? 0 : x.hashCode())\n"\
"#define KEY_EQUAL(x,y) ((x) == (y) || ((x) != null && (x).equals((y))))\n"\
"#else\n"\
"#define KEY2TYPE(x) (((KEY_CLASS)(x)).KEY_TYPE_VALUE())\n"\
"#define KEY2OBJ(x) (new KEY_CLASS(x))\n"\
"#define HASH(x) (x)\n"\
"#define KEY_EQUAL(x,y) ((x) == (y))\n"\
"#endif\n\n"\
"#include \"${FILE[$f]}.drv\"\n" >$FILENAME
	  done
done

