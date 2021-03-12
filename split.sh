#!/bin/bash -e

TMPFILE=$(mktemp)

\ls -1 build/it/unimi/dsi/fastutil/*.class build/it/unimi/dsi/fastutil/io/*.class | cut -d/ -f2- >$TMPFILE
find build/it/unimi/dsi/fastutil/* -type f -iname \*.class -not -iname \*Short\* -not -iname \*Char\* -not -iname \*Boolean\* -not -iname \*Byte\* -not -iname \*Float\* -not -iname \*Reference\* | cut -d/ -f2- >>$TMPFILE
jdeps -v -R -e 'it\.unimi\.dsi\.fastutil($|.*)' -classpath build $(while read F; do echo "build/$F"; done <$TMPFILE) | awk '/build -> build/ {exit} { if (NF == 4) print gensub(/\./, "/", "g", $3) ".class" }' >>$TMPFILE
sort -u $TMPFILE >fastutil-core.txt

find build/it/unimi/dsi/fastutil/* -type f -iname \*.class -not -iname \*Short\* -not -iname \*Boolean\* -not -iname \*Float\* | cut -d/ -f2- >$TMPFILE
jdeps -v -R -e 'it\.unimi\.dsi\.fastutil($|.*)' -classpath build $(while read F; do echo "build/$F"; done <$TMPFILE) | awk '/build -> build/ {exit} { if (NF == 4) print gensub(/\./, "/", "g", $3) ".class" }' >>$TMPFILE
comm -2 -3 <(sort -u $TMPFILE) fastutil-core.txt >fastutil-refbytechar.txt

comm -2 -3 <(find build/it/unimi/dsi/fastutil/ -type f -iname \*.class | cut -d/ -f2- | sort) <(sort -u fastutil-core.txt fastutil-refbytechar.txt) >fastutil-boolshortfloat.txt

echo "Classes in build:" $(find build -iname \*.class | wc -l)
echo "Classes in txt files:" $(cat fastutil-core.txt fastutil-refbytechar.txt fastutil-boolshortfloat.txt | wc -l)

while read CLASS; do SOURCE=${CLASS%.class}.java; if [ -f src/$SOURCE ]; then echo $SOURCE; fi; done < fastutil-core.txt > fastutil-src-core.txt
while read CLASS; do SOURCE=${CLASS%.class}.java; if [ -f src/$SOURCE ]; then echo $SOURCE; fi; done < fastutil-refbytechar.txt > fastutil-src-refbytechar.txt
while read CLASS; do SOURCE=${CLASS%.class}.java; if [ -f src/$SOURCE ]; then echo $SOURCE; fi; done < fastutil-boolshortfloat.txt > fastutil-src-boolshortfloat.txt

echo "Source files in src:" $(find src -iname \*.java | wc -l)
echo "Source files in txt files:" $(cat fastutil-src-core.txt fastutil-src-refbytechar.txt fastutil-src-boolshortfloat.txt | wc -l)
