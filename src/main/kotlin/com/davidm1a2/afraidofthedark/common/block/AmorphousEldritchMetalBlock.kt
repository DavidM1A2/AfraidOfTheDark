package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.event.custom.ManualResearchTriggerEvent
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import net.minecraftforge.common.MinecraftForge

/**
 * Class representing amorphous eldritch metal, this can be walked through
 *
 * @constructor initializes the block's properties
 */
class AmorphousEldritchMetalBlock : AOTDBlock(
    "amorphous_eldritch_metal",
    Properties.of(Material.PORTAL)
        .noCollission()
        .lightLevel { 1 }
        .strength(10.0f, 50.0f)
        .requiresCorrectToolForDrops()
) {
    override fun entityInside(blockState: BlockState, world: Level, blockPos: BlockPos, entity: Entity) {
        super.entityInside(blockState, world, blockPos, entity)
        if (!world.isClientSide && entity is Player) {
            MinecraftForge.EVENT_BUS.post(ManualResearchTriggerEvent(entity, ModResearches.SNOWY_ANOMALY))
        }
    }
}