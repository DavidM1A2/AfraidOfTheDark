package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import net.minecraft.util.math.AxisAlignedBB

/**
 * Class that represents a spell altar tile entity
 *
 * @constructor just sets the block type
 */
class TileEntitySpellAltar : AOTDTickingTileEntity(ModBlocks.SPELL_ALTAR), IMCAnimatedModel {
    // private val animHandler = AnimationHandlerEnaria(this)

    /**
     * Called every tick to update the tile entity's state
     */
    override fun update() {
        super.update()
        if (world.isRemote) {
            /*
            animHandler.animationsUpdate()
            if (!animHandler.isAnimationActive("armthrow")) {
                animHandler.activateAnimation("armthrow", 0f)
            }
             */
        }
    }

    override fun getRenderBoundingBox(): AxisAlignedBB {
        return AxisAlignedBB(
            pos.x.toDouble(),
            pos.y.toDouble(),
            pos.z.toDouble(),
            pos.x + 1.0,
            pos.y + 2.0,
            pos.z + 1.0
        )
    }

    override fun getAnimationHandler(): AnimationHandler? {
        return null // animHandler
    }
}