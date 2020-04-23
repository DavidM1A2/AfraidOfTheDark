package com.davidm1a2.afraidofthedark.common.tileEntity.core

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import net.minecraft.block.Block

/**
 * Base class for animated ticking tile entities
 */
abstract class AOTDAnimatedTileEntity(block: Block, private val animationHandler: AnimationHandler) : AOTDTickingTileEntity(block), IMCAnimatedModel {
    /**
     * Called every tick to update the tile entity's state
     */
    override fun update() {
        super.update()
        if (world.isRemote) {
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