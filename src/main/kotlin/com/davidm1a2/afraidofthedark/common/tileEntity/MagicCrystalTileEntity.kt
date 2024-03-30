package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.block.MagicCrystalBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.Connection
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.util.Mth
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import kotlin.math.sin

class MagicCrystalTileEntity(blockPos: BlockPos, blockState: BlockState) : AOTDTickingTileEntity(ModTileEntities.MAGIC_CRYSTAL, blockPos, blockState) {
    private var vitae: Double = 0.0
    private var prevRenderRotation: Double = 0.0
    private var renderRotation: Double = 0.0
    private var prevRenderHeightOffset: Double = 0.0
    private var renderHeightOffset: Double = 0.0
    private var master: MagicCrystalTileEntity = NO_MASTER
        get() {
            if (field.isRemoved || field == NO_MASTER) {
                field = refreshMaster()
            }
            return field
        }

    private fun refreshMaster(): MagicCrystalTileEntity {
        val world = this.level ?: return NO_MASTER

        // We are the master
        if (isMaster()) {
            return this
        }

        // Search for our master below
        for (i in 1 until MagicCrystalBlock.BLOCK_HEIGHT) {
            val belowBlockPos = blockPos.below(i)
            val blockBelow = world.getBlockState(belowBlockPos)
            if (blockBelow.block == ModBlocks.MAGIC_CRYSTAL) {
                if (blockBelow.getValue(MagicCrystalBlock.BOTTOM)) {
                    return world.getBlockEntity(belowBlockPos) as MagicCrystalTileEntity
                }
            } else {
                return NO_MASTER
            }
        }

        return NO_MASTER
    }

    fun isMaster(): Boolean {
        // Fixes rare race condition crash when the block is destroyed but the tile entity still exists
        return if (blockState.hasProperty(MagicCrystalBlock.BOTTOM)) {
            blockState.getValue(MagicCrystalBlock.BOTTOM)
        } else {
            false
        }
    }

    private fun hasMaster(): Boolean {
        return master != NO_MASTER
    }

    fun getRenderHeightOffset(partialTicks: Float): Double {
        return Mth.lerp(partialTicks.toDouble(), prevRenderHeightOffset, renderHeightOffset)
    }

    fun getRenderRotation(partialTicks: Float): Double {
        return Mth.lerp(partialTicks.toDouble(), prevRenderRotation, renderRotation)
    }

    fun setMaxVitae() {
        if (!hasMaster()) {
            return
        }
        if (!isMaster()) {
            master.setMaxVitae()
        }

        this.vitae = MAX_VITAE
        setChangedAndTellClients()
    }

    fun consumeVitae(vitae: Double): Boolean {
        if (!hasMaster()) {
            return false
        }
        if (!isMaster()) {
            return master.consumeVitae(vitae)
        }

        return if (vitae <= this.vitae) {
            this.vitae = this.vitae - vitae
            setChangedAndTellClients()
            true
        } else {
            false
        }
    }

    fun getVitae(): Double {
        if (!hasMaster()) {
            return 0.0
        }
        if (!isMaster()) {
            return master.getVitae()
        }

        return vitae
    }

    fun getMaxVitae(): Double {
        if (!hasMaster()) {
            return 1.0
        }
        if (!isMaster()) {
            return master.getMaxVitae()
        }

        return MAX_VITAE
    }

    override fun getRenderBoundingBox(): AABB {
        return super.getRenderBoundingBox().expandTowards(0.0, 16.0 * (MagicCrystalBlock.BLOCK_HEIGHT - 1), 0.0)
    }

    override fun getUpdatePacket(): ClientboundBlockEntityDataPacket {
        return ClientboundBlockEntityDataPacket(blockPos, -1, save(CompoundTag()))
    }

    override fun onDataPacket(net: Connection, pkt: ClientboundBlockEntityDataPacket) {
        // Don't accept any changes from client -> server, only process server -> client
        if (net.direction == PacketFlow.CLIENTBOUND) {
            load(pkt.tag)
        }
    }

    override fun getUpdateTag(): CompoundTag {
        return save(CompoundTag())
    }

    override fun save(compound: CompoundTag): CompoundTag {
        super.save(compound)
        compound.putDouble("vitae", vitae)
        return compound
    }

    override fun load(compound: CompoundTag) {
        super.load(compound)
        vitae = compound.getDouble("vitae")
    }

    companion object {
        private const val BOB_SPEED = 0.1
        private const val SPIN_SPEED = 1.2
        private const val MAX_VITAE = 100000.0
        private val NO_MASTER = MagicCrystalTileEntity(BlockPos.ZERO, ModBlocks.MAGIC_CRYSTAL.defaultBlockState())

        fun clientTick(level: Level, blockPos: BlockPos, blockState: BlockState, blockEntity: MagicCrystalTileEntity) {
            baseTick(level, blockPos, blockState, blockEntity)
            val percentFull = blockEntity.getVitae() / blockEntity.getMaxVitae()
            blockEntity.prevRenderHeightOffset = blockEntity.renderHeightOffset
            blockEntity.renderHeightOffset = sin((blockEntity.ticksExisted) * BOB_SPEED * percentFull) * 0.5 + 0.5
            blockEntity.prevRenderRotation = blockEntity.renderRotation
            blockEntity.renderRotation = (blockEntity.renderRotation + SPIN_SPEED * percentFull)
            // If we've gone full circle wrap around. Update prevRenderRotation to be negative temporarily to fix interpolation
            if (blockEntity.renderRotation > 360) {
                blockEntity.prevRenderRotation = blockEntity.prevRenderRotation - blockEntity.renderRotation
                blockEntity.renderRotation = blockEntity.renderRotation % 360
            }
        }
    }
}