package proofs.list

import kotlinx.collections.immutable.persistentListOf
import org.bmc4j.Bmc
import org.bmc4j.BmcProof

/**
 * Proofs over `kotlinx.collections.immutable.PersistentList`, analyzed as the library ships it.
 * Each holds for every symbolic input, not a sampled few.
 *
 * Two engine boundaries currently bound what we assert on the List path (both are jbmc 6.9.0
 * limitations, not library bugs, and both fail conservatively — never a false VERIFIED):
 *  - element-array mutation (`add`/`set`/`removeAt`) copies `SmallPersistentVector`'s `Array<Any?>`
 *    and trips the array-symex boundary (exit 6 -> UNKNOWN; bmc4j #178);
 *  - `contains`/`indexOf` dispatch through the list iterator, which is the devirt/link-failure-fragile
 *    path (Set/Map lookups, by contrast, hit the trie directly and prove cleanly).
 * So the asserted List law here is the iterator-free size invariant.
 */
class PersistentListLaws {

    @BmcProof
    fun empty_list_has_size_zero() {
        val l = persistentListOf<Int>()
        Bmc.check(l.size == 0 && l.isEmpty())
    }
}
