# bmc4j-proofs-kotlinx-immutable

Real-world [bmc4j](https://github.com/bmc4j/bmc4j) proofs against
[`kotlinx.collections.immutable`](https://github.com/Kotlin/kotlinx.collections.immutable) — proving a
third-party Kotlin library's functions correct for *every* input, using the library **exactly as
shipped** (no fork).

## How it works

bmc4j analyzes JVM **bytecode**, not source, and runs as an ordinary Gradle plugin in this consumer
project. So we just depend on the published `kotlinx-collections-immutable` artifact and write
`@BmcProof` functions (plain JUnit 5 tests) that call its public API. bmc4j puts the library jar on the
analysis classpath, its jar-mirroring rewrite makes the shipped bytecode sound (invokedynamic
string-concat, lambdas, …), and substitutes its JDK/kotlin-stdlib models — so we're proving the library
as a real consumer gets it.

This repo is intentionally **one library per repo** (spin up a sibling repo for the next target).

## Status

Structural proofs over `PersistentList` / `PersistentSet` / `PersistentMap`, each holding for *every*
symbolic input over the library's shipped bytecode:

- **`PersistentList`** — `add` then read returns the element; **immutability**: `add` produces a new
  list and leaves the source unchanged (both from an empty source and a non-empty `persistentListOf(a)`);
  the empty list has size zero.
- **`PersistentSet`** — adding an element makes it a member; the empty set contains nothing.
- **`PersistentMap`** — `put` then `get` returns the value; the empty map maps no key.

These exercise the persistent collections' real internals (the array-backed vector and the HAMT
trie/node-array copies), proven for all inputs — the immutability law is the headline.

### Engine boundaries we steer around

jbmc 6.9.0 limits a couple of shapes (all conservative — they fail to UNKNOWN/refute, never a false
pass), so the proofs avoid them:

- **`size()` / `isEmpty()` on a persistent set or map** dispatch through the `Set`/`Map`/`Collection`
  interface and don't devirtualize to the concrete impl, so set/map laws assert via `contains` / `get`
  only (list `size`/index resolve fine).
- **Large multi-element construction** (e.g. two-element sets/maps built op-by-op) can time out; the
  laws stay on small, bounded shapes.

## Requirements & running

This consumes bmc4j from a **GitHub Packages snapshot** (the pre-Central channel), which needs an
authenticated token with `read:packages` even though the packages are public:

```bash
# token via env (GITHUB_ACTOR + GITHUB_TOKEN) or -Pgpr.user=<you> -Pgpr.token=<PAT with read:packages>
./gradlew test          # every @BmcProof runs as a JUnit 5 test
```

In CI the workflow's own `GITHUB_TOKEN` provides this automatically. Consumer **Kotlin 2.4** is the
floor (bmc4j 0.4.x). Each proof is symbolic — it holds for all inputs, not a sampled few.

## License

Apache-2.0 (matching bmc4j).
