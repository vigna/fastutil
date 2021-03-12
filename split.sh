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
