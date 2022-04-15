package net.cacheux.kotlintestutils

import kotlin.time.Duration

data class PerformanceReport<T>(
    val arguments: T?,
    val results: List<Duration>
) {
    override fun toString() = """
Report with arguments $arguments:
${results.mapIndexed { index, duration -> "- $index : ${duration.inWholeMilliseconds}ms ${duration.inWholeNanoseconds}ns" }.joinToString("\n") }

""".trimIndent()
}