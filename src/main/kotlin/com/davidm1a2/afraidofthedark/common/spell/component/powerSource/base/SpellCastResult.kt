package com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base

import net.minecraft.network.chat.Component

class SpellCastResult private constructor(val failureMessage: Component?) {
    fun wasSuccessful(): Boolean {
        return failureMessage == null
    }

    companion object {
        fun success(): SpellCastResult {
            return SpellCastResult(null)
        }

        fun failure(message: Component): SpellCastResult {
            return SpellCastResult(message)
        }
    }
}