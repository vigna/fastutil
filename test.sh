#!/bin/sh

TYPE=(boolean byte short int long char float double Object Object)
TYPE_CAP=(Boolean Byte Short Int Long Char Float Double Object Reference)
CLASS=(Boolean Byte Short Integer Long Character Float Double Object Reference)

lf=".75"

if [ "$1" != "" ]; then lf=$1; fi

for ((t=1; t<1000; t*=10)); do

    SET=(OpenHashSet LinkedOpenHashSet RBTreeSet AVLTreeSet)

    for ((f=0; f<${#SET[*]}; f++)); do
	l=${#TYPE[*]}
	if [[ ${SET[$f]} != "OpenHashSet" && ${SET[$f]} != "LinkedOpenHashSet" ]]; then l=$((l-1)); fi # Only hash sets may have reference keys.
	for ((k=1; k<l; k++)); do
		CLASSNAME=it.unimi.dsi.fastUtil.${TYPE_CAP[$k]}${SET[$f]}
		echo "Testing $CLASSNAME ($t elements, load factor $lf)..."
		java -server $CLASSNAME regressionTest $t $lf
	done
    done

    MAP=(OpenHashMap LinkedOpenHashMap RBTreeMap AVLTreeMap)

    for ((f=0; f<${#MAP[*]}; f++)); do
	l=${#TYPE[*]}
	if [[ ${MAP[$f]} != "OpenHashMap" && ${MAP[$f]} != "LinkedOpenHashMap" ]]; then l=$((l-1)); fi # Only hash maps may have reference keys.
	for ((k=1; k<l; k++)); do
	    for ((v=1; v<${#TYPE[*]}; v++)); do
		CLASSNAME=it.unimi.dsi.fastUtil.${TYPE_CAP[$k]}2${TYPE_CAP[$v]}${MAP[$f]}
		echo "Testing $CLASSNAME ($t elements, load factor $lf)..."
		java -server $CLASSNAME regressionTest $t $lf
	    done
	done
    done

done
