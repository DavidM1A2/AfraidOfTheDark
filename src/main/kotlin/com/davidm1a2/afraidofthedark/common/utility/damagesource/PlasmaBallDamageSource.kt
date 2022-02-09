package com.davidm1a2.afraidofthedark.common.utility.damagesource

import net.minecraft.entity.Entity
import net.minecraft.util.IndirectEntityDamageSource

class PlasmaBallDamageSource(source: Entity, indirectSource: Entity?) : IndirectEntityDamageSource("afraidofthedark.plasma_ball", source, indirectSource) {
    init {
        setProjectile()
    }
}