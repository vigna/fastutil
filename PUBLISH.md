# Publishing fastutil to Maven Central

Since Sonatype retired the old OSSRH Nexus (`oss.sonatype.org`, with its "Close/Release"
web UI), releases go through the **Central Publisher Portal**. Our Ant build talks to the
Portal's compatibility bridge, the **OSSRH Staging API**, at
`ossrh-staging-api.central.sonatype.com`.

Publishing is now **two steps**:

1. **`ant stage-all`** — builds, GPG-signs, and *uploads* the artifacts. This only stages
   them; it creates an **open staging repository** on the bridge. Nothing is on Central yet.
2. **`./publish.sh`** — finds that staging repository and *releases* it to Central. This is the
   replacement for the old Nexus "Close + Release" buttons.

`ant publish` runs both in sequence.

## Prerequisites (one-time)

- **A Central Portal account that owns the `it.unimi` namespace** (the reverse of the
  `unimi.it` domain). fastutil's group id `it.unimi.dsi` publishes underneath it.
  Check at <https://central.sonatype.com> → **Namespaces**; it must be listed as **Verified**.
- **A Portal user token.** Generate it at central.sonatype.com → **View Account →
  Generate User Token**. It gives a `username:password` pair. Put it in
  `~/.m2/settings.xml`:

  ```xml
  <settings>
    <servers>
      <server>
        <id>sonatype-nexus-staging</id>
        <username>TOKEN_USERNAME</username>
        <password>TOKEN_PASSWORD</password>
      </server>
    </servers>
  </settings>
  ```

  Both `ant stage-all` and `publish.sh` read the token from here — it lives in exactly one
  place, and never in the repository.

  > Regenerating a token on the Portal **invalidates the old one**. A stale token, or one
  > from an account that does not own the namespace, fails the upload with HTTP 400
  > *"File is not related to an authorized namespace."* (The bridge authenticates the token
  > but finds no namespace for its account — it is not a signing or path problem.)

- **GPG** configured with a published key (the `stage` targets sign every artifact with
  `gpg -sba`).

## Releasing a version

1. Set `version=` in `build.properties`.

2. Build the tarballs and stage the artifacts (this runs the tests, then signs and
   uploads via `ant stage-all`):

   ```bash
   make source binary stage
   ```

   Both `fastutil` and `fastutil-core` land in a single open staging repository named
   `it.unimi--default-repository`. List it to sanity-check what got staged:

   ```bash
   curl -s -u TOKEN_USERNAME:TOKEN_PASSWORD \
     https://ossrh-staging-api.central.sonatype.com/manual/search/repositories
   ```

3. Release to Central:

   ```bash
   ./publish.sh
   ```

   To review the deployment on the Portal **before** it goes live, use `./publish.sh manual`
   instead: it pushes to <https://central.sonatype.com/publishing/deployments>, where you
   can inspect every artifact and click **Publish** by hand.

4. **Releasing is irreversible** — a released version can never be deleted from Central,
   only superseded by a new version. It appears on
   <https://repo1.maven.org/maven2/it/unimi/dsi/fastutil/> within ~10–30 minutes and is
   searchable on <https://central.sonatype.com> a bit later.

## publish.sh reference

`publish.sh` reads the token from `~/.m2/settings.xml`, auto-discovers the current staging
repository key (no more hand-editing the IP-bearing URL), and acts on it:

| Command            | Effect                                                             |
| ------------------ | ----------------------------------------------------------------- |
| `./publish.sh`        | Validate, then **auto-release** the staging repo to Central.      |
| `./publish.sh manual` | Upload to the Portal but **hold** for a manual Publish click.     |
| `./publish.sh drop`   | **Discard** the open staging repo (use to redo a bad `stage-all`).|

The script contains no secrets, so it is safe to commit.

## If something goes wrong

- **HTTP 400 "not related to an authorized namespace"** on upload — token is stale or from
  the wrong account. Regenerate it under the account that owns `it.unimi`, update
  `~/.m2/settings.xml`, and re-stage.
- **Staged the wrong thing** — `./publish.sh drop`, then fix and re-run `ant stage-all`.
- **`publish.sh` says "no open staging repository found"** — `ant stage-all` did not complete;
  re-run it and watch for upload errors.
