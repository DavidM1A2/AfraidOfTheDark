package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.particle.FeyParticleData
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import net.minecraft.util.math.vector.Vector3d
import java.awt.Color
import kotlin.random.Random

class FeyLightTileEntity : AOTDTickingTileEntity(ModTileEntities.FEY_LIGHT) {
    var color: Color = Color.MAGENTA

    override fun tick() {
        super.tick()
        if (level?.isClientSide == true) {
            val centerPosition = Vector3d(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble())
                .add(0.5, 0.1, 0.5)

            level?.addParticle(
                FeyParticleData(Random.nextFloat() * 360, color.red / 255f, color.green / 255f, color.blue / 255f),
                centerPosition.x,
                centerPosition.y,
                centerPosition.z,
                (Random.nextDouble() - 0.5) * 0.1,
                0.05 + Random.nextDouble() * 0.05,
                (Random.nextDouble() - 0.5) * 0.1
            )
        }
    }
}
