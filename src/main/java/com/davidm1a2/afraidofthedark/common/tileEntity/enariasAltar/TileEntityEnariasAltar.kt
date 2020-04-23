package com.davidm1a2.afraidofthedark.common.tileEntity.enariasAltar

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.ChannelMode
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDAnimatedTileEntity

/**
 * Enaria's altar tile entity which renders the animation
 */
class TileEntityEnariasAltar : AOTDAnimatedTileEntity(
    ModBlocks.ENARIAS_ALTAR,
    AnimationHandler(ChannelSpin("Spin", 5f, 60, ChannelMode.LINEAR))
) {
    /**
     * Called every tick to update the tile entity's state
     */
    override fun update() {
        super.update()
        if (world.isRemote) {
            val animHandler = getAnimationHandler()
            if (!animHandler.isAnimationActive("Spin")) {
                animHandler.playAnimation("Spin")
            }
        }
    }
}