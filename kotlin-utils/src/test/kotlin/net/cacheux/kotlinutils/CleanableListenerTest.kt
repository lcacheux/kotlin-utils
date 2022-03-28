package net.cacheux.kotlinutils

import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class CleanableListenerTest {
    @Test
    fun testCallAndCleanListeners() {
        val listener1 = mock<TestListener> { on { cleanMe() }.thenReturn(true) }
        val listener2 = mock<TestListener> { on { cleanMe() }.thenReturn(false) }
        val listener3 = mock<TestListener> { on { cleanMe() }.thenReturn(true) }

        val listenersList = mutableListOf(listener1, listener2, listener3)

        val value = "Hello"

        listenersList.callAndCleanListeners(
            cleanOn = { it.cleanMe() }
        ) {
            it.callMe(value)
        }

        assertEquals(1, listenersList.size)
        verify(listener1, times(1)).callMe(value)
        verify(listener2, times(1)).callMe(value)
        verify(listener3, times(1)).callMe(value)
    }

    @Test
    fun testCleanableListener() {
        val listener1 = mock<MyCleanableListener> { on { mustClean() }.thenReturn(true) }
        val listener2 = mock<MyCleanableListener> { on { mustClean() }.thenReturn(false) }
        val listener3 = mock<MyCleanableListener> { on { mustClean() }.thenReturn(false) }

        val listenersList = mutableListOf(listener1, listener2, listener3)

        listenersList.callAndCleanListeners { it.callback() }

        assertEquals(2, listenersList.size)
        verify(listener1, times(1)).callback()
        verify(listener2, times(1)).callback()
        verify(listener3, times(1)).callback()
    }

    private interface TestListener {
        fun callMe(value: String)
        fun cleanMe(): Boolean
    }

    private interface MyCleanableListener: CleanableListener {
        fun callback()
    }
}