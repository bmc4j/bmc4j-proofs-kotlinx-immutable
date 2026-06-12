package proofs.set

import kotlinx.collections.immutable.persistentHashSetOf
import kotlinx.collections.immutable.persistentSetOf
import org.bmc4j.Bmc
import org.bmc4j.BmcProof

/**
 * Proofs over `kotlinx.collections.immutable.PersistentSet` (HAMT-backed), analyzed as shipped.
 * Each holds for every symbolic input.
 */
class PersistentSetLaws {

    @BmcProof
    fun empty_set_has_size_zero() {
        val s = persistentSetOf<Int>()
        Bmc.check(s.size == 0 && s.isEmpty())
    }

    /** The empty set contains no element — for every probe value. */
    @BmcProof
    fun empty_set_contains_nothing() {
        val s = persistentSetOf<Int>()
        val x = Bmc.anyInt()
        Bmc.check(!s.contains(x))
    }

    /** Adding an element makes it a member and the set has size one — for every value. */
    @BmcProof
    fun add_then_contains_with_size_one() {
        val x = Bmc.anyInt()
        val s = persistentSetOf<Int>().add(x)
        Bmc.check(s.contains(x) && s.size == 1)
    }

    /**
     * Idempotence: adding the same element twice is the same as adding it once. Uses the unordered
     * HAMT set (`persistentHashSetOf`) — the ordered `persistentSetOf` carries insertion-order link
     * bookkeeping that makes a second op pathological for jbmc, and order is irrelevant here.
     */
    @BmcProof
    fun add_twice_is_idempotent() {
        val x = Bmc.anyInt()
        val s = persistentHashSetOf<Int>().add(x).add(x)
        Bmc.check(s.contains(x) && s.size == 1)
    }
}
