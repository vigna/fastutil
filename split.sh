#!/bin/bash -e

TMPFILE=$(mktemp)

\ls -1 build/it/unimi/dsi/fastutil/*.class build/it/unimi/dsi/fastutil/io/*.class | cut -d/ -f2- >$TMPFILE
find build/it/unimi/dsi/fastutil/* -type f -iname \*.class -not -iname \*Short\* -not -iname \*Char\* -not -iname \*Boolean\* -not -iname \*Byte\* -not -iname \*Float\* -not -iname \*Reference\* | cut -d/ -f2- >>$TMPFILE

# Expand direct dependencies until stability

export N=0
while [[ $N != $(sort -u $TMPFILE | wc -l) ]]; do
	N=$(sort -u $TMPFILE | wc -l)
	jdeps -v -R -e 'it\.unimi\.dsi\.fastutil($|.*)' -classpath build $(sort -u $TMPFILE | while read F; do echo "build/$F"; done) | awk '/build -> build/ {exit} { if (NF == 4) print gensub(/\./, "/", "g", $3) ".class" }' >>$TMPFILE
done

sort -u $TMPFILE >fastutil-core.txt

echo "Classes in build:" $(find build -iname \*.class | wc -l)
echo "Classes in core:" $(wc -l fastutil-core.txt)

while read CLASS; do SOURCE=${CLASS%.class}.java; if [ -f src/$SOURCE ]; then echo $SOURCE; fi; done < fastutil-core.txt > fastutil-src-core.txt

echo "Source files in src:" $(find src -iname \*.java | wc -l)
echo "Source files in core:" $(wc -l fastutil-src-core.txt)
