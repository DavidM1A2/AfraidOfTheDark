package com.davidm1a2.afraidofthedark.common.tileEntity.core

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import net.minecraft.tileentity.TileEntityType

/**
 * Base class for animated ticking tile entities
 */
abstract class AOTDAnimatedTileEntity(tileEntityType: TileEntityType<*>, private val animationHandler: AnimationHandler) :
    AOTDTickingTileEntity(tileEntityType), IMCAnimatedModel {
    /**
     * Called every tick to update the tile entity's state
     */
    override fun tick() {
        super.tick()
        if (world?.isRemote == true) {
            animationHandler.update()
        }
    }

    /**
     * @return the animation handler for this model
     */
    override fun getAnimationHandler(): AnimationHandler {
        return this.animationHandler
    }
}