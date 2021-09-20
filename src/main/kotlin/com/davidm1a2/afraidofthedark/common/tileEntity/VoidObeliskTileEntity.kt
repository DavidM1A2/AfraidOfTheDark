package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModEffects
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.event.custom.ManualResearchTriggerEvent
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDZoneTileEntity
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.play.server.SSetSlotPacket
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.PotionUtils
import net.minecraft.potion.Potions
import net.minecraft.util.math.AxisAlignedBB
import net.minecraftforge.common.MinecraftForge
import kotlin.random.Random

/**
 * Tile entity for the dark forest block that makes players drowsy
 *
 * @constructor sets the block type of the tile entity
 */
class VoidObeliskTileEntity : AOTDZoneTileEntity(ModTileEntities.VOID_OBELISK) {
    override val zone: AxisAlignedBB by lazy {
        AxisAlignedBB(
            blockPos.x.toDouble(),
            blockPos.y.toDouble(),
            blockPos.z.toDouble(),
            (blockPos.x + 1).toDouble(),
            (blockPos.y + 1).toDouble(),
            (blockPos.z + 1).toDouble()
        ).inflate(CHECK_RANGE.toDouble())
    }

    companion object {
        private const val CHECK_RANGE = 15
    }
}