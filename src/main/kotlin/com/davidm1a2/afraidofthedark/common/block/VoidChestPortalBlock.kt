package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.capabilities.getVoidChestData
import com.davidm1a2.afraidofthedark.common.constants.ModDimensions
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.dimension.teleport
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.entity.Entity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * Class that defines a void chest portal block
 *
 * @constructor constructor sets item properties
 */
class VoidChestPortalBlock : AOTDBlock(
    "void_chest_portal",
    Properties.create(Material.PORTAL)
        // This block can't be broken
        .hardnessAndResistance(60000f)
        .lightValue(5)
        .doesNotBlockMovement()
) {
    override fun displayInCreative(): Boolean {
        return false
    }

    override fun onEntityCollision(state: BlockState, world: World, blockPos: BlockPos, entity: Entity) {
        // Server side processing only
        if (!world.isRemote) {
            // Test if the entity is a player
            if (entity is ServerPlayerEntity) {
                // Grab the player's research and void chest data
                val playerResearch = entity.getResearch()
                val playerVoidChestData = entity.getVoidChestData()

                // If the player is in the void chest send them to their stored dimension
                if (world.dimension.type == ModDimensions.VOID_CHEST_TYPE) {
                    // Send the player to their previously stored dimension
                    entity.teleport(playerVoidChestData.preTeleportDimension!!)
                } else {
                    // If we can research the research research it
                    if (playerResearch.canResearch(ModResearches.VOID_CHEST)) {
                        playerResearch.setResearch(ModResearches.VOID_CHEST, true)
                        playerResearch.setResearch(ModResearches.ELDRITCH_DECORATION, true)
                        playerResearch.sync(entity, true)
                    }

                    // If the player has the void chest research then move the player
                    if (playerResearch.isResearched(ModResearches.VOID_CHEST)) {
                        // Make sure no friends index is set since the portal can only send to the player's dimension
                        playerVoidChestData.friendsIndex = -1
                        entity.teleport(ModDimensions.VOID_CHEST_TYPE)
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    override fun getRenderLayer(): BlockRenderLayer {
        return BlockRenderLayer.TRANSLUCENT
    }
}