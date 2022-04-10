package com.davidm1a2.afraidofthedark.common.utility.damagesource

import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.SplinterDroneEntity
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.SplinterDroneProjectileEntity
import net.minecraft.util.IndirectEntityDamageSource

class SplinterDroneProjectileDamageSource(source: SplinterDroneProjectileEntity, indirectSource: SplinterDroneEntity?) :
    IndirectEntityDamageSource("afraidofthedark.splinter_drone_projectile", source, indirectSource) {
    init {
        setProjectile()
    }
}