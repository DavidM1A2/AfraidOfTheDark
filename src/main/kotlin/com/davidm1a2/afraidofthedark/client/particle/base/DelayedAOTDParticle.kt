package com.davidm1a2.afraidofthedark.client.particle.base

import net.minecraft.client.multiplayer.ClientLevel

abstract class DelayedAOTDParticle(
    world: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    xSpeed: Double = 0.0,
    ySpeed: Double = 0.0,
    zSpeed: Double = 0.0,
    private val delayTicks: Int = 0,
    private val fadeTicks: Int = 0
) : AOTDParticle(world, x, y, z, xSpeed, ySpeed, zSpeed) {
    init {
        alpha = 0f
    }

    override fun tick() {
        super.tick()
        when {
            age == delayTicks -> {
                onDelayOver()
                tickPreDelay()
            }

            age > delayTicks -> tickPostDelay()
            age < delayTicks -> tickPreDelay()
        }
    }

    open fun onDelayOver() {
    }

    open fun tickPostDelay() {
        if (age <= delayTicks + fadeTicks) {
            alpha = (age - delayTicks).toFloat() / fadeTicks
        } else {
            setAlphaFadeInLastTicks(fadeTicks.toFloat())
        }
    }

    open fun tickPreDelay() {
    }

    override fun setLifetime(newLifetime: Int) {
        super.setLifetime(newLifetime + delayTicks + fadeTicks)
    }
}