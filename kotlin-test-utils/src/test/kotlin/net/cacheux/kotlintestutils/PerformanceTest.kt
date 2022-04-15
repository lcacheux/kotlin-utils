package net.cacheux.kotlintestutils

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalTime
class PerformanceTest {
    @Test
    fun testPerformanceReport() {
        val report = generatePerformanceReport(
            {
                repeat(it) {
                    println("hello")
                }
            },
            {
                repeat(it) {
                    println("hello again")
                }
            },
            arguments = 10000
        )

        assertEquals(2, report.results.size)
        println(report)
    }
}