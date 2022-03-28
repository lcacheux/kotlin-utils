package net.cacheux.kotlinutils

/**
 * Some methods and extensions to check booleans in lists.
 */

fun atLeastOneTrue(vararg elements: Boolean) = elements.contains(true)
fun atLeastOneFalse(vararg elements: Boolean) = elements.contains(false)
fun allTrue(vararg elements: Boolean) = elements.all { it }
fun allFalse(vararg elements: Boolean) = elements.all { !it }

fun <T> List<T>.atLeastOneTrue(callback: (T) -> Boolean) = map(callback).contains(true)
fun <T> List<T>.atLeastOneFalse(callback: (T) -> Boolean) = map(callback).contains(false)

fun List<Boolean>.atLeastOneTrue() = contains(true)
fun List<Boolean>.atLeastOneFalse() = contains(false)