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
    AnimationHandler(
        ChannelSpin("SpinSlow", 5f, 60, ChannelMode.LINEAR),
        ChannelSpin("SpinMedium", 15f, 60, ChannelMode.LINEAR),
        ChannelSpin("SpinFast", 30f, 60, ChannelMode.LINEAR)
    )
) {
    /**
     * Called every tick to update the tile entity's state
     */
    override fun update() {
        super.update()
        if (world.isRemote) {
            if (!anySpinActive()) {
                val animHandler = getAnimationHandler()
                val closestPlayer = world.getClosestPlayer(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), MEDIUM_DISTANCE) { true }
                if (closestPlayer != null) {
                    val distance = closestPlayer.getDistance(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
                    if (distance <= FAST_DISTANCE) {
                        animHandler.playAnimation("SpinFast")
                    } else {
                        animHandler.playAnimation("SpinMedium")
                    }
                } else {
                    animHandler.playAnimation("SpinSlow")
                }
            }
        }
    }

    private fun anySpinActive(): Boolean {
        val animHandler = getAnimationHandler()
        return animHandler.isAnimationActive("SpinFast") ||
                animHandler.isAnimationActive("SpinMedium") ||
                animHandler.isAnimationActive("SpinSlow")
    }

    companion object {
        private const val FAST_DISTANCE = 5.0
        private const val MEDIUM_DISTANCE = 15.0
    }
}