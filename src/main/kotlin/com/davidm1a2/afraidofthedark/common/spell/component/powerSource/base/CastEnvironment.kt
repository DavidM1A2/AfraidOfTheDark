package com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base

class CastEnvironment<T> private constructor(val vitaeAvailable: Double, val vitaeMaximum: Double, internal val context: T) {
    companion object {
        fun <T> noVitae(context: T): CastEnvironment<T> {
            return CastEnvironment(0.0, 0.0, context)
        }

        fun <T> infiniteVitae(context: T): CastEnvironment<T> {
            return CastEnvironment(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, context)
        }

        fun <T> withVitae(vitaeAvailable: Double, context: T): CastEnvironment<T> {
            return CastEnvironment(vitaeAvailable, Double.POSITIVE_INFINITY, context)
        }

        fun <T> withVitae(vitaeAvailable: Double, vitaeMaximum: Double, context: T): CastEnvironment<T> {
            return CastEnvironment(vitaeAvailable, vitaeMaximum, context)
        }
    }
}
