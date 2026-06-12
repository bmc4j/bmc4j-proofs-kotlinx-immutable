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
- **`PersistentSet`** — adding an element makes it a member (size one); the empty set contains nothing
  and has size zero; **idempotence**: `add(x).add(x)` is the same as `add(x)`.
- **`PersistentMap`** — `put` then `get` returns the value (size one); the empty map maps no key and has
  size zero; **last-write-wins**: `put(k, v1).put(k, v2)` keeps `v2`.

These exercise the persistent collections' real internals (the array-backed vector and the HAMT
trie/node-array copies), proven for all inputs — the immutability law is the headline.

A couple of **fail-on-purpose demos** (`@BmcProof(expect = REFUTED)`) assert deliberately-false claims
(e.g. "the empty set contains this element"); they pass by being refuted, and the PR proof-results
report shows the **counterexample** bmc4j found (e.g. `x = 15`) alongside Expected/Actual — so a real
regression (the claim becoming un-refutable) would fail the build.

### A note on shapes

The multi-op laws (idempotence, last-write-wins) use the unordered `persistentHashSetOf`/
`persistentHashMapOf`: the *ordered* `persistentSetOf`/`persistentMapOf` maintain insertion-order links
that make a second operation pathological for jbmc, and order is irrelevant to those properties.

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
