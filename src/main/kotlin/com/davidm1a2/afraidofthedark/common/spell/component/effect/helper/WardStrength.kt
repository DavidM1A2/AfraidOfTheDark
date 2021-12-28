package com.davidm1a2.afraidofthedark.common.spell.component.effect.helper

enum class WardStrength(
    val explodeChance: Double,
    val miningSpeedReductionPercent: Float
) {
    WEAK(0.9, 0.5f),
    MEDIUM(0.5, 0.75f),
    STRONG(0.2, 0.95f),
    INVINCIBLE(0.001, 0.999f);

    init {
        assert(explodeChance in 0.0..1.0)
        assert(miningSpeedReductionPercent in 0f..1f)
    }

    companion object {
        fun from(ordinal: Int): WardStrength {
            if (ordinal in 0 until values().size) {
                return values()[ordinal]
            }
            return WEAK
        }
    }
}