package proofs.demos

import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.persistentSetOf
import org.bmc4j.Bmc
import org.bmc4j.BmcProof
import org.bmc4j.Verdict

/**
 * Fail-on-purpose demos: each asserts a deliberately-FALSE claim and pins `expect = REFUTED`, so the
 * proof PASSES by being refuted. The point is to show what a caught violation looks like — the
 * proof-results report surfaces the counterexample (the witnessing input bmc4j found) for these even
 * though they pass, so a real regression (the claim becoming un-refutable) would fail the build.
 */
class CounterexampleDemos {

    /** The empty set has no members, so "it contains x" is refutable for every x. */
    @BmcProof(expect = Verdict.REFUTED)
    fun empty_set_does_not_contain_an_arbitrary_element() {
        val s = persistentSetOf<Int>()
        val x = Bmc.anyInt()
        Bmc.check(s.contains(x))
    }

    /** The empty map maps no key, so "it has a mapping for k" is refutable for every k. */
    @BmcProof(expect = Verdict.REFUTED)
    fun empty_map_does_not_map_an_arbitrary_key() {
        val m = persistentMapOf<Int, Int>()
        val k = Bmc.anyInt()
        Bmc.check(m.containsKey(k))
    }
}
