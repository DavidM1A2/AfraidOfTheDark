package com.davidm1a2.afraidofthedark.common.spell.component.effect.base

class ProcResult(val isSuccess: Boolean) {
    companion object {
        fun success(): ProcResult {
            return ProcResult(true)
        }

        fun failure(): ProcResult {
            return ProcResult(false)
        }
    }
}