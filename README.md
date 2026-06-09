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

First proofs landed — emptiness/size/lookup invariants for `PersistentList` / `PersistentSet` /
`PersistentMap`, each proven symbolically over the library's shipped bytecode.

Two jbmc 6.9.0 engine boundaries currently bound what we assert (both are engine limitations, not
library bugs, and both fail conservatively — never a false pass):

- **element-array mutation** (`add`/`set`/`removeAt`/`put`) copies the persistent collections'
  internal `Array<Any?>`, which meets the array-symex boundary
  ([bmc4j#178](https://github.com/bmc4j/bmc4j/issues/178), exit 6 → UNKNOWN);
- **list `contains`/`indexOf`** dispatches through the list iterator (a devirt-fragile path); the trie
  lookups behind `PersistentSet.contains` / `PersistentMap.containsKey` prove cleanly.

Mutation/iteration laws will be reclaimed as the engine moves past these boundaries.

## Running (once proofs exist)

```bash
./gradlew test          # every @BmcProof runs as a JUnit 5 test
```

Each proof is symbolic — it holds for all inputs, not a sampled few — and deliberate failures are pinned
with `expect = REFUTED / UNKNOWN`.

## License

Apache-2.0 (matching bmc4j).
