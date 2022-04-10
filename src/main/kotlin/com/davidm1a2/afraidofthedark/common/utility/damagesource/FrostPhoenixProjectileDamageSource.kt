package com.davidm1a2.afraidofthedark.common.utility.damagesource

import net.minecraft.entity.Entity
import net.minecraft.util.IndirectEntityDamageSource

class FrostPhoenixProjectileDamageSource(source: Entity, indirectSource: Entity?) : IndirectEntityDamageSource("afraidofthedark.frost_phoenix_projectile", source, indirectSource) {
    init {
        setProjectile()
    }
}