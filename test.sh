#!/bin/sh

FILE=(OpenHashSet LinkedOpenHashSet RBTreeSet AVLTreeSet)
TYPE=(boolean byte short int long char float double Object Object)
TYPE_CAP=(Boolean Byte Short Int Long Char Float Double Object Reference)
CLASS=(Boolean Byte Short Integer Long Character Float Double Object Reference)

lf=".75"

if [ "$1" != "" ]; then lf=$1; fi

for ((t=10; t<100000; t*=10)); do
	for ((f=0; f<${#FILE[*]}; f++)); do
		l=${#TYPE[*]}
		if [[ ${FILE[$f]} != "OpenHashSet" && ${FILE[$f]} != "LinkedOpenHashSet" ]]; then l=$((l-1)); fi
		for ((k=1; k<l; k++)); do
				CLASSNAME=it.unimi.dsi.fastUtil.${TYPE_CAP[$k]}${FILE[$f]}
				echo "Testing $CLASSNAME ($t elements, load factor $lf)..."
				java -server $CLASSNAME regressionTest $t $lf
		  done
	 done

done

FILE=(OpenHashMap LinkedOpenHashMap RBTreeMap AVLTreeMap)

for ((t=10; t<1000; t*=10)); do
	 for ((f=0; f<${#FILE[*]}; f++)); do
		for ((k=1; k<${#TYPE[*]}; k++)); do
			l=${#TYPE[*]}
			if [[ ${FILE[$f]} != "OpenHashMap" && ${FILE[$f]} != "LinkedOpenHashMap" ]]; then l=$((l-1)); fi
				for ((v=1; v<${#TYPE[*]}; v++)); do
					 CLASSNAME=it.unimi.dsi.fastUtil.${TYPE_CAP[$k]}2${TYPE_CAP[$v]}${FILE[$f]}
					 echo "Testing $CLASSNAME ($t elements, load factor $lf)..."
					 java -server $CLASSNAME regressionTest $t $lf
				done
		  done
	 done
done
