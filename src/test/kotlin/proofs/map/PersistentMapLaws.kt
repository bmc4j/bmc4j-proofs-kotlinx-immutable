package proofs.map

import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.persistentMapOf
import org.bmc4j.Bmc
import org.bmc4j.BmcProof

/**
 * Proofs over `kotlinx.collections.immutable.PersistentMap` (HAMT-backed), analyzed as shipped.
 * Each holds for every symbolic key/value.
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

    /** `put` then look the key back up — the inserted value comes back and the map has size one. */
    @BmcProof
    fun put_then_get_with_size_one() {
        val k = Bmc.anyInt()
        val v = Bmc.anyInt()
        val m = persistentMapOf<Int, Int>().put(k, v)
        Bmc.check(m[k] == v && m.size == 1)
    }

    /**
     * Last-write-wins: putting the same key twice keeps the latest value. Uses the unordered HAMT
     * map (`persistentHashMapOf`) — the ordered `persistentMapOf` carries insertion-order link
     * bookkeeping that makes a second op pathological for jbmc, and order is irrelevant here.
     */
    @BmcProof
    fun put_twice_keeps_the_latest_value() {
        val k = Bmc.anyInt()
        val v1 = Bmc.anyInt()
        val v2 = Bmc.anyInt()
        val m = persistentHashMapOf<Int, Int>().put(k, v1).put(k, v2)
        Bmc.check(m[k] == v2 && m.size == 1)
    }
}
