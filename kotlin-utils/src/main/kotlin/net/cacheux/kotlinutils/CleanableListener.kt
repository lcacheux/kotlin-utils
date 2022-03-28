package net.cacheux.kotlinutils

/**
 * A simple interface for a listener that can be cleaned from a list with [callAndCleanListeners] once it have been
 * called.
 */
interface CleanableListener {
    /**
     * Implement to set up the condition for cleaning.
     */
    fun mustClean(): Boolean = true
}

/**
 * Call a collection of listeners and remove those who meet the required condition for cleaning.
 * Work by default with [CleanableListener] but can be overridden to match any other listener type.
 *
 * @param cleanOn Lambda to use for the cleaning condition.
 * @param block Listener call.
 */
fun <U, V> MutableCollection<U>.callAndCleanListeners(
    cleanOn: (U) -> Boolean = { it is CleanableListener && it.mustClean() },
    block: (U) -> V
) =
    synchronized(this) {
        map {
            block(it)
        }.also {
            removeAll(filter { cleanOn(it) })
        }
    }