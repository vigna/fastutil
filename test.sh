#!/bin/sh

FILE=(HashMap HashSet)
TYPE=(boolean byte short int long char float double Object)
TYPE_CAP=(Boolean Byte Short Int Long Char Float Double Object)
CLASS=(Boolean Byte Short Integer Long Character Float Double Object)

lf=".75"

if [ "$1" != "" ]; then lf=$1; fi

for ((t=10; t<10000; t*=10)); do
	 for ((f=0; f<1; f++)); do
		  for ((k=1; k<${#TYPE[*]}; k++)); do
				for ((v=0; v<${#TYPE[*]}; v++)); do
					 CLASSNAME=it.unimi.dsi.fastUtil.${TYPE_CAP[$k]}2${TYPE_CAP[$v]}${FILE[$f]}
					 echo "Testing $CLASSNAME ($t elements, load factor $lf)..."
					 java -server -cp fastUtil.jar $CLASSNAME regressionTest $t $lf
				done
		  done
	 done

	 for ((f=1; f<2; f++)); do
		  for ((k=1; k<${#TYPE[*]}; k++)); do
				CLASSNAME=it.unimi.dsi.fastUtil.${TYPE_CAP[$k]}${FILE[$f]}
				echo "Testing $CLASSNAME ($t elements, load factor $lf)..."
				java -server -cp fastUtil.jar $CLASSNAME regressionTest $t $lf
		  done
	 done
done
