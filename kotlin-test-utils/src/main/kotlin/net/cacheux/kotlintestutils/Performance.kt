package net.cacheux.kotlintestutils

import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * Generate a single [PerformanceReport] for a specified argument value.
 * @param blocks Multiple blocks of code to test
 * @param arguments An argument value
 * @return A [PerformanceReport] with the results
 */
@ExperimentalTime
fun <T> generatePerformanceReport(vararg blocks: (T) -> Unit, arguments: T): PerformanceReport<T> {
    return PerformanceReport(
        arguments = arguments,
        results = blocks.map {
            measureTime {
                it(arguments)
            }
        }
    )
}

/**
 * Generate a list of [PerformanceReport], one for each argument value, to compare performances in multiple conditions.
 * @param blocks Multiple blocks of code to test
 * @param arguments A list of multiple argument values
 * @return A list of [PerformanceReport] with the results
 */
@ExperimentalTime
fun <T> generatePerformanceReportList(vararg blocks: (T) -> Unit, arguments: List<T>): List<PerformanceReport<T>> =
    arguments.map { args -> generatePerformanceReport(blocks = blocks, arguments = args) }