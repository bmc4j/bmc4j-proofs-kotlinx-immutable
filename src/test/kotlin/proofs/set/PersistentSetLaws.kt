package proofs.set

import kotlinx.collections.immutable.persistentSetOf
import org.bmc4j.Bmc
import org.bmc4j.BmcProof

/**
 * Proofs over `kotlinx.collections.immutable.PersistentSet` (HAMT-backed), analyzed as shipped.
 * Element-inserting laws exercise the trie node arrays and currently meet the jbmc 6.9.0 array-symex
 * boundary (bmc4j #178), so the asserted laws here stay on the empty-set lookup path.
 */
class PersistentSetLaws {

    @BmcProof
    fun empty_set_has_size_zero() {
        val s = persistentSetOf<Int>()
        Bmc.check(s.size == 0 && s.isEmpty())
    }

    @BmcProof
    fun empty_set_contains_nothing() {
        val s = persistentSetOf<Int>()
        val x = Bmc.anyInt()
        Bmc.check(!s.contains(x))
    }
}
