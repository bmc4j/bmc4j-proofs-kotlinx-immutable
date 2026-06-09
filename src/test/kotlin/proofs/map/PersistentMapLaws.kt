package proofs.map

import kotlinx.collections.immutable.persistentMapOf
import org.bmc4j.Bmc
import org.bmc4j.BmcProof

/**
 * Proofs over `kotlinx.collections.immutable.PersistentMap` (HAMT-backed), analyzed as shipped.
 * Each holds for every symbolic key/value.
 *
 * Note: `size()`/`isEmpty()` on a persistent map dispatch through the `Map` interface and don't
 * devirtualize under jbmc 6.9.0 (they nondet-stub → conservative refute, never a false pass), so
 * these laws assert via `get`/`containsKey` only.
 */
class PersistentMapLaws {

    /** `put` then look the key back up — the inserted value comes back, for every key and value. */
    @BmcProof
    fun put_then_get_returns_the_value() {
        val k = Bmc.anyInt()
        val v = Bmc.anyInt()
        val m = persistentMapOf<Int, Int>().put(k, v)
        Bmc.check(m[k] == v)
    }

    /** The empty map has no mapping for any key — for every probe key. */
    @BmcProof
    fun empty_map_has_no_key() {
        val m = persistentMapOf<Int, Int>()
        val k = Bmc.anyInt()
        Bmc.check(!m.containsKey(k) && m[k] == null)
    }
}
