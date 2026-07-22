#!/bin/bash

# Release helper for the Central Publisher Portal (OSSRH Staging API bridge).
#
# Background: `ant stage-all` only *uploads* the signed artifacts; it no longer
# publishes them (the old oss.sonatype.org "Close/Release" web UI is gone). This
# script performs that final step: it finds the open staging repository that the
# deploy created and either releases it to Maven Central or drops it. It replaces
# the old hand-edited curl.sh whose repository key (which embeds your IP and is
# minted per deploy) went stale after every release.
#
# Credentials are read from ~/.m2/settings.xml (server id `sonatype-nexus-staging`),
# the same place the Ant deploy reads them, so the token lives in exactly one place.
#
# Usage:  ./curl.sh            # publish: validate then auto-release to Central
#         ./curl.sh manual     # upload to the Portal but hold for a manual Publish click
#         ./curl.sh drop       # discard the open staging repository (nothing is published)

set -euo pipefail

API="https://ossrh-staging-api.central.sonatype.com"
SETTINGS="$HOME/.m2/settings.xml"
SERVER_ID="sonatype-nexus-staging"

MODE="${1:-automatic}"
case "$MODE" in
  ""|automatic) MODE="automatic" ;;
  manual)       MODE="user_managed" ;;
  drop)         MODE="drop" ;;
  *) echo "Usage: $0 [ | manual | drop ]" >&2; exit 2 ;;
esac

# --- read username/password from the matching <server> block in settings.xml ---
BLOCK=$(awk '
  /<server>/       { f=1; b="" }
  f                { b = b $0 ORS }
  /<\/server>/     { f=0; if (b ~ /<id>'"$SERVER_ID"'<\/id>/) { printf "%s", b; exit } }
' "$SETTINGS")
USER=$(printf '%s' "$BLOCK" | sed -n 's:.*<username>\(.*\)</username>.*:\1:p')
PASS=$(printf '%s' "$BLOCK" | sed -n 's:.*<password>\(.*\)</password>.*:\1:p')
if [ -z "$USER" ] || [ -z "$PASS" ]; then
  echo "ERROR: could not read credentials for server '$SERVER_ID' from $SETTINGS" >&2
  exit 1
fi

# --- find the open staging repository key(s) ---
# For a drop, don't poll (the repo either exists or it doesn't). For a publish,
# poll a few times: the bridge may take a moment to register a fresh deploy.
attempts=1; [ "$MODE" != "drop" ] && attempts=10
KEYS=""
for i in $(seq 1 "$attempts"); do
  RESP=$(curl -sf -u "$USER:$PASS" "$API/manual/search/repositories" || true)
  KEYS=$(printf '%s' "$RESP" | grep -o '"key":"[^"]*"' | sed 's/"key":"//;s/"$//' || true)
  [ -n "$KEYS" ] && break
  [ "$i" -lt "$attempts" ] && { echo "No staging repository yet ($i/$attempts); waiting..."; sleep 3; }
done
if [ -z "$KEYS" ]; then
  if [ "$MODE" = "drop" ]; then echo "Nothing to drop: no open staging repository."; exit 0; fi
  echo "ERROR: no open staging repository found. Run 'ant stage-all' first." >&2
  echo "Server response: $RESP" >&2
  exit 1
fi

# --- act on each repository ---
rc=0
while IFS= read -r KEY; do
  [ -z "$KEY" ] && continue
  ENC=$(printf '%s' "$KEY" | sed 's:/:%2F:g')
  if [ "$MODE" = "drop" ]; then
    echo "Dropping staging repository: $KEY"
    curl -f -w '\nHTTP %{http_code}\n' -u "$USER:$PASS" -X DELETE \
      "$API/manual/drop/repository/$ENC" || rc=$?
  else
    echo "Publishing staging repository: $KEY  (publishing_type=$MODE)"
    curl -f -w '\nHTTP %{http_code}\n' -u "$USER:$PASS" -X POST \
      "$API/manual/upload/repository/$ENC?publishing_type=$MODE" || rc=$?
  fi
done <<< "$KEYS"

if [ "$rc" -ne 0 ]; then
  echo "ERROR: at least one request failed." >&2
  exit "$rc"
fi
[ "$MODE" != "drop" ] && echo "Done. Track status at https://central.sonatype.com/publishing/deployments"
