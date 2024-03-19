package com.davidm1a2.afraidofthedark.common.utility.damagesource

import net.minecraft.world.damagesource.IndirectEntityDamageSource
import net.minecraft.world.entity.Entity

class FrostPhoenixProjectileDamageSource(source: Entity, indirectSource: Entity?) : IndirectEntityDamageSource("afraidofthedark.frost_phoenix_projectile", source, indirectSource) {
    init {
        setProjectile()
    }
}