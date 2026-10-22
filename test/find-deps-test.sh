#!/usr/bin/env bash
set -euo pipefail

script_dir=$(cd "$(dirname "$0")" && pwd)
find_deps=${1:-"$script_dir/../find-deps.sh"}
tmp_dir=$(mktemp -d -t 'dependency-test.XXXX')
trap 'rm -rf "$tmp_dir"' EXIT
mkdir -p "$tmp_dir/src/it/unimi/dsi/fastutil/example" "$tmp_dir/library classes" "$tmp_dir/app classes"

cat > "$tmp_dir/src/it/unimi/dsi/fastutil/example/Root.java" <<'JAVA'
package it.unimi.dsi.fastutil.example;
public class Root {
    public int value() { return new Nested().value(); }
    private static class Nested {
        int value() { return new Middle().value(); }
    }
}
JAVA
cat > "$tmp_dir/src/it/unimi/dsi/fastutil/example/Middle.java" <<'JAVA'
package it.unimi.dsi.fastutil.example;
class Middle { int value() { return new Leaf().value(); } }
JAVA
cat > "$tmp_dir/src/it/unimi/dsi/fastutil/example/Leaf.java" <<'JAVA'
package it.unimi.dsi.fastutil.example;
class Leaf { int value() { return 42; } }
JAVA
cat > "$tmp_dir/src/it/unimi/dsi/fastutil/example/Unused.java" <<'JAVA'
package it.unimi.dsi.fastutil.example;
class Unused {}
JAVA
cat > "$tmp_dir/src/App.java" <<'JAVA'
public class App {
    public static void main(String[] args) {
        System.out.println(new it.unimi.dsi.fastutil.example.Root().value());
    }
}
JAVA

javac -d "$tmp_dir/library classes" "$tmp_dir"/src/it/unimi/dsi/fastutil/example/*.java
jar cf "$tmp_dir/library archive.jar" -C "$tmp_dir/library classes" .
javac -cp "$tmp_dir/library archive.jar" -d "$tmp_dir/app classes" "$tmp_dir/src/App.java"

bash "$find_deps" find "$tmp_dir/app classes" > "$tmp_dir/class list.txt"
expected='it/unimi/dsi/fastutil/example/Root.class'
if [ "$(cat "$tmp_dir/class list.txt")" != "$expected" ]; then
    echo 'find did not return the referenced class' >&2
    cat "$tmp_dir/class list.txt" >&2
    exit 1
fi
[ "$(bash "$find_deps" find --src "$tmp_dir/app classes")" = 'it/unimi/dsi/fastutil/example/Root.java' ]

bash "$find_deps" minimize "$tmp_dir/library archive.jar" "$tmp_dir/class list.txt"
jar tf "$tmp_dir/library archive-min.jar" > "$tmp_dir/entries.txt"
grep -qx 'it/unimi/dsi/fastutil/example/Root.class' "$tmp_dir/entries.txt"
grep -qx 'it/unimi/dsi/fastutil/example/Root\$Nested.class' "$tmp_dir/entries.txt"
grep -qx 'it/unimi/dsi/fastutil/example/Middle.class' "$tmp_dir/entries.txt"
grep -qx 'it/unimi/dsi/fastutil/example/Leaf.class' "$tmp_dir/entries.txt"
if grep -q 'Unused.class' "$tmp_dir/entries.txt"; then
    echo 'minimize included an unreachable class' >&2
    exit 1
fi
[ "$(java -cp "$tmp_dir/app classes:$tmp_dir/library archive-min.jar" App)" = '42' ]
echo 'find-deps integration test passed'
