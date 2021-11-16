package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.event.custom.ManualResearchTriggerEvent
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.ToolType

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
        .harvestLevel(2)
        .harvestTool(ToolType.PICKAXE)
        .strength(10.0f, 50.0f)
        .requiresCorrectToolForDrops()
) {
    override fun entityInside(blockState: BlockState, world: World, blockPos: BlockPos, entity: Entity) {
        super.entityInside(blockState, world, blockPos, entity)
        if (!world.isClientSide && entity is PlayerEntity) {
            MinecraftForge.EVENT_BUS.post(ManualResearchTriggerEvent(entity, ModResearches.SNOWY_ANOMALY))
        }
    }
}