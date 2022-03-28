package net.cacheux.kotlinutils

import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class PerformanceTest {

    companion object {
        const val ITERATION = 1000
        const val LIST_SIZE = 1000
    }

    /**
     * Test performances of [atLeastOneTrue] vs the usual old fashioned implementation with a temporary value.
     */
    @ExperimentalTime
    @Test
    fun testPerformances() {
        val listenersList = generateSequence { ConditionsCheckTest.ContainsListener("true") }
            .take(LIST_SIZE).toList()
        val message = "true"

        val oldFashionedTime = measureTime {
            repeat(ITERATION) {
                assertTrue(oldFashionedWay(listenersList, message))
            }
        }

        val oldFashionedExtensionTime = measureTime {
            repeat(ITERATION) {
                assertTrue(listenersList.atLeastOneTrueOldFashioned { it.accept(message) })
            }
        }

        val newFashionedTime = measureTime {
            repeat(ITERATION) {
                assertTrue(listenersList.atLeastOneTrue { it.accept(message) })
            }
        }

        println("Old fashioned duration: ${oldFashionedTime.inWholeMicroseconds}")
        println("Old fashioned duration with extension: ${oldFashionedExtensionTime.inWholeMicroseconds}")
        println("New fashioned duration: ${newFashionedTime.inWholeMicroseconds}")
    }

    private fun <T> List<T>.atLeastOneTrueOldFashioned(callback: (T) -> Boolean): Boolean {
        var hasAccepted = false
        forEach {
            if (callback(it)) {
                hasAccepted = true
            }
        }
        return hasAccepted
    }

    private fun oldFashionedWay(values: List<ConditionsCheckTest.AcceptListener>, message: String): Boolean {
        var hasAccepted = false
        values.forEach {
            if (it.accept(message)) {
                hasAccepted = true
            }
        }
        return hasAccepted
    }
}