package net.cacheux.kotlinutils

import org.junit.Assert.*
import org.junit.Test

class ConditionsCheckTest {

    @Test
    fun testBasicMethods() {
        assertTrue(atLeastOneTrue(true, false, false, true))
        assertTrue(atLeastOneTrue(true, true, true, true))
        assertFalse(atLeastOneTrue(false, false, false))

        assertTrue(atLeastOneFalse(true, true, false))
        assertTrue(atLeastOneFalse(false, false, false, false))
        assertFalse(atLeastOneFalse(true, true, true))

        assertTrue(allTrue(true, true, true, true))
        assertFalse(allTrue(false, false, false, false))
        assertFalse(allTrue(false, false, false, true))
        assertTrue(allFalse(false, false, false))
        assertFalse(allFalse(false, false, true, false))
        assertFalse(allFalse(true, true))
    }

    @Test
    fun testListeners() {
        val listenersList = listOf(
            ContainsListener("alwaystrue"),
            ContainsListener("alwaysfalse"),
            ContainsListener("alwaysnone")
        )

        assertTrue(listenersList.atLeastOneTrue { it.accept("true") })
        assertTrue(listenersList.atLeastOneTrue { it.accept("false") })
        assertTrue(listenersList.atLeastOneTrue { it.accept("none") })
        assertFalse(listenersList.atLeastOneTrue { it.accept("test") })

        assertTrue(listenersList.atLeastOneFalse { it.accept("true") })
        assertTrue(listenersList.atLeastOneFalse { it.accept("never") })
        assertFalse(listenersList.atLeastOneFalse { it.accept("always") })
    }

    @Test
    fun testTrueWithCleanableListeners() {
        val listenersList = mutableListOf(
            ContainsListener("true"),
            ContainsListener("false"),
            ContainsListener("none")
        )

        val message = "true"
        assertTrue(listenersList.callAndCleanListeners(cleanOn = { it.accept(message) }) {
            it.accept(message)
        }.atLeastOneTrue())

        assertEquals(2, listenersList.size)
    }

    @Test
    fun testFalseWithCleanableListeners() {
        val listenersList = mutableListOf(
            ContainsListener("true"),
            ContainsListener("false"),
            ContainsListener("none")
        )

        val message = "true"
        assertTrue(listenersList.callAndCleanListeners(cleanOn = { it.accept(message) }) {
            it.accept(message)
        }.atLeastOneFalse())

        assertEquals(2, listenersList.size)
    }

    interface AcceptListener {
        fun accept(message: String): Boolean
    }

    class ContainsListener(private val value: String): AcceptListener {
        override fun accept(message: String) = value.contains(message)
    }
}