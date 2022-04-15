package net.cacheux.kotlinutils

/**
 * Some methods and extensions to check booleans in lists.
 */
fun atLeastOneTrue(vararg elements: Boolean) = elements.contains(true)
fun atLeastOneFalse(vararg elements: Boolean) = elements.contains(false)
fun allTrue(vararg elements: Boolean) = elements.all { it }
fun allFalse(vararg elements: Boolean) = elements.all { !it }

fun <U,V> List<U>.atLeastOne(value: V, callback: (U) -> V): Boolean {
    var result = false
    forEach { if (callback(it) == value) result = true }
    return result
}

fun <T> List<T>.atLeastOneTrue(callback: (T) -> Boolean) = atLeastOne(true, callback)
fun <T> List<T>.atLeastOneFalse(callback: (T) -> Boolean) = atLeastOne(false, callback)
// Two other acceptable implementations could be
// fun <T> List<T>.atLeastOneTrue(callback: (T) -> Boolean) = maxOf(callback)
// fun <T> List<T>.atLeastOneFalse(callback: (T) -> Boolean) = !minOf(callback)

fun List<Boolean>.atLeastOneTrue() = contains(true)
fun List<Boolean>.atLeastOneFalse() = contains(false)