package net.cacheux.kotlinutils

import net.cacheux.kotlintestutils.generatePerformanceReportList
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.time.ExperimentalTime

class PerformanceTest {

    /**
     * Test performances of [atLeastOneTrue] with multiple implementations.
     */
    @ExperimentalTime
    @Test
    fun testPerformances() {
        val message = "true"

        generatePerformanceReportList(
            // Result 0 -> Old code without extension
            { args -> execute(args) { list -> noExtension(list, message) } },
            // Results 1 - 3 : Multiple implementations
            { args -> execute(args) { list -> list.atLeastOneTrueWithVar { it.accept(message) } } },
            { args -> execute(args) { list -> list.atLeastOneTrueWithMap { it.accept(message) } } },
            { args -> execute(args) { list -> list.atLeastOneTrueWithMax { it.accept(message) } } },
            // Results 4 - 6 : Same multiple implementations without inline
            { args -> execute(args) { list -> list.atLeastOneTrueWithVarNoInline { it.accept(message) } } },
            { args -> execute(args) { list -> list.atLeastOneTrueWithMapNoInline { it.accept(message) } } },
            { args -> execute(args) { list -> list.atLeastOneTrueWithMaxNoInline { it.accept(message) } } },
            arguments = listOf(
                Args(listSize = 100, iterations = 100),
                Args(listSize = 100, iterations = 10000),
                Args(listSize = 10000, iterations = 100),
                Args(listSize = 10000, iterations = 10000),
            )
        ).also {
            println(it)
        }
    }

    private inline fun execute(args: Args, block: (List<ConditionsCheckTest.ContainsListener>) -> Boolean) {
        genList(args.listSize).also { list ->
            repeat(args.iterations) {
                assertTrue(block(list))
            }
        }
    }

    private inline fun <T> List<T>.atLeastOneTrueWithVar(callback: (T) -> Boolean): Boolean {
        var result = false
        forEach {
            if (callback(it)) { result = true }
        }
        return result
    }

    private inline fun <T> List<T>.atLeastOneTrueWithMap(callback: (T) -> Boolean) =
        map(callback).contains(true)

    private inline fun <T> List<T>.atLeastOneTrueWithMax(callback: (T) -> Boolean) =
        maxOf(callback)

    private fun <T> List<T>.atLeastOneTrueWithVarNoInline(callback: (T) -> Boolean) = atLeastOneTrueWithVar(callback)
    private fun <T> List<T>.atLeastOneTrueWithMapNoInline(callback: (T) -> Boolean) = atLeastOneTrueWithMap(callback)
    private fun <T> List<T>.atLeastOneTrueWithMaxNoInline(callback: (T) -> Boolean) = atLeastOneTrueWithMax(callback)

    /**
     * Same result as [atLeastOneTrue] but without Kotlin extension.
     */
    private fun noExtension(values: List<ConditionsCheckTest.AcceptListener>, message: String): Boolean {
        var hasAccepted = false
        values.forEach {
            if (it.accept(message)) {
                hasAccepted = true
            }
        }
        return hasAccepted
    }

    private fun genList(size: Int) = generateSequence { ConditionsCheckTest.ContainsListener("true") }.take(size).toList()

    data class Args(
        val listSize: Int,
        val iterations: Int
    )
}