#!/bin/sh

FILE=(HashMap)
TYPE=(boolean byte short int long char float double Object)
TYPE_CAP=(Boolean Byte Short Int Long Char Float Double Object)
CLASS=(Boolean Byte Short Integer Long Character Float Double Object)

for ((t=10; t<1000000; t*=10)); do
	 for ((f=0; f<1; f++)); do
		  for ((k=1; k<${#TYPE[*]}; k++)); do
				for ((v=0; v<${#TYPE[*]}; v++)); do
					 if (( k < $((${#TYPE[*]}-1)) || v < $((${#TYPE[*]}-1)) )); then
						  CLASSNAME=it.unimi.dsi.fastMaps.${TYPE_CAP[$k]}2${TYPE_CAP[$v]}${FILE[$f]}
						  echo "Testing $CLASSNAME ($t elements)..."
						  java -server -cp fastMaps.jar $CLASSNAME regressionTest $t
					 fi
				done
		  done
	 done

#	 for ((f=2; f<4; f++)); do
#		  for ((v=1; v<8; v++)); do
#				CLASSNAME=$DIR/${TYPE_CAP[$v]}${FILE[$f]}
#				echo "Testing $CLASSNAME ($t elements)..."
#				java -server $CLASSNAME regressionTest $t
#		  done
#	 done
done
