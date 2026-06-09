package proofs.map

import kotlinx.collections.immutable.persistentMapOf
import org.bmc4j.Bmc
import org.bmc4j.BmcProof

/**
 * Proofs over `kotlinx.collections.immutable.PersistentMap` (HAMT-backed), analyzed as shipped.
 * Entry-inserting laws exercise the trie node arrays and currently meet the jbmc 6.9.0 array-symex
 * boundary (bmc4j #178), so the asserted laws here stay on the empty-map lookup path.
 */
class PersistentMapLaws {

    @BmcProof
    fun empty_map_has_size_zero() {
        val m = persistentMapOf<Int, Int>()
        Bmc.check(m.size == 0 && m.isEmpty())
    }

    @BmcProof
    fun empty_map_has_no_key() {
        val m = persistentMapOf<Int, Int>()
        val k = Bmc.anyInt()
        Bmc.check(!m.containsKey(k) && m[k] == null)
    }

    /** Counterexample demo: the empty map has no mapping for any key, so this claim is refuted for
     *  every key. Left un-pinned (no expect) so the REFUTED verdict + counterexample surface in the
     *  proof-results report. */
    @BmcProof
    fun empty_map_has_an_arbitrary_key() {
        val m = persistentMapOf<Int, Int>()
        val k = Bmc.anyInt()
        Bmc.check(m.containsKey(k))
    }
}
