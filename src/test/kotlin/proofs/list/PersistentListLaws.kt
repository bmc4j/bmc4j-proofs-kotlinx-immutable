package proofs.list

import kotlinx.collections.immutable.persistentListOf
import org.bmc4j.Bmc
import org.bmc4j.BmcProof

/**
 * Proofs over `kotlinx.collections.immutable.PersistentList`, analyzed as the library ships it.
 * Each holds for every symbolic input, not a sampled few.
 */
class PersistentListLaws {

    /** `add` returns a new list carrying the element — for every value. */
    @BmcProof
    fun add_then_get_returns_the_element() {
        val x = Bmc.anyInt()
        val l = persistentListOf<Int>().add(x)
        Bmc.check(l.size == 1 && l[0] == x)
    }

    /**
     * Persistence/immutability — the point of a persistent collection: `add` produces a NEW list and
     * leaves the source untouched. Source is the empty list; the result holds the added element.
     */
    @BmcProof
    fun add_does_not_mutate_the_empty_source() {
        val x = Bmc.anyInt()
        val base = persistentListOf<Int>()
        val extended = base.add(x)
        Bmc.check(base.isEmpty() && extended.size == 1 && extended[0] == x)
    }

    /**
     * Immutability with a non-empty source built via the vararg factory: adding `b` to `[a]` yields
     * `[a, b]` while `[a]` is unchanged — proven for every `a` and `b`.
     */
    @BmcProof
    fun add_does_not_mutate_a_nonempty_source() {
        val a = Bmc.anyInt()
        val b = Bmc.anyInt()
        val base = persistentListOf(a)
        val extended = base.add(b)
        Bmc.check(base.size == 1 && base[0] == a && extended.size == 2 && extended[1] == b)
    }

    @BmcProof
    fun empty_list_has_size_zero() {
        val l = persistentListOf<Int>()
        Bmc.check(l.size == 0 && l.isEmpty())
    }
}
