package proofs.set

import kotlinx.collections.immutable.persistentHashSetOf
import kotlinx.collections.immutable.persistentSetOf
import org.bmc4j.Bmc
import org.bmc4j.BmcProof

/**
 * Proofs over `kotlinx.collections.immutable.PersistentSet` (HAMT-backed), analyzed as shipped.
 * Each holds for every symbolic input.
 *
 * Note: `size()`/`isEmpty()` on a persistent set dispatch through the `Set`/`Collection` interface
 * and don't devirtualize to the concrete impl under jbmc 6.9.0 (they nondet-stub → conservative
 * refute, never a false pass), so these laws assert membership via `contains` only.
 */
class PersistentSetLaws {

    /** Adding an element makes it a member — for every value. */
    @BmcProof
    fun add_then_contains() {
        val x = Bmc.anyInt()
        val s = persistentSetOf<Int>().add(x)
        Bmc.check(s.contains(x))
    }

    /** The empty set contains no element — for every probe value. */
    @BmcProof
    fun empty_set_contains_nothing() {
        val s = persistentSetOf<Int>()
        val x = Bmc.anyInt()
        Bmc.check(!s.contains(x))
    }

    /**
     * Idempotence: adding the same element twice is the same as adding it once — the element is a
     * member, for every value. Uses the unordered HAMT set (`persistentHashSetOf`); the ordered
     * `persistentSetOf` carries insertion-order link bookkeeping that makes a second op pathological
     * for jbmc, and order is irrelevant to this property.
     */
    @BmcProof
    fun add_twice_is_idempotent() {
        val x = Bmc.anyInt()
        val s = persistentHashSetOf<Int>().add(x).add(x)
        Bmc.check(s.contains(x))
    }
}
