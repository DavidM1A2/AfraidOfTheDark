package com.davidm1a2.afraidofthedark.common.block.core

/**
 * Interface we can implement to tell the registerer to show or hide our block in the creative tab
 */
interface AOTDShowBlockCreative {
    fun displayInCreative(): Boolean {
        return true
    }
}