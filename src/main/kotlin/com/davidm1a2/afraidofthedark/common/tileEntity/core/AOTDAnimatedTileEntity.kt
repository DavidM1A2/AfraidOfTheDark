package com.davidm1a2.afraidofthedark.common.tileEntity.core

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

/**
 * Base class for animated ticking tile entities
 */
abstract class AOTDAnimatedTileEntity(type: BlockEntityType<*>, blockPos: BlockPos, blockState: BlockState, private val animationHandler: AnimationHandler) :
    AOTDTickingTileEntity(type, blockPos, blockState), IMCAnimatedModel {

    /**
     * @return the animation handler for this model
     */
    override fun getAnimationHandler(): AnimationHandler {
        return this.animationHandler
    }
}