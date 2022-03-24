package com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base

import net.minecraft.util.text.ITextComponent

class SpellCastResult private constructor(val failureMessage: ITextComponent?) {
    fun wasSuccessful(): Boolean {
        return failureMessage == null
    }

    companion object {
        fun success(): SpellCastResult {
            return SpellCastResult(null)
        }

        fun failure(message: ITextComponent): SpellCastResult {
            return SpellCastResult(message)
        }
    }
}