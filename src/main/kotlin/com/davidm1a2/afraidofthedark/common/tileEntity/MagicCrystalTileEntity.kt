package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.block.MagicCrystalBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import net.minecraft.block.BlockState
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.PacketDirection
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.MathHelper
import kotlin.math.sin

class MagicCrystalTileEntity : AOTDTickingTileEntity(ModTileEntities.MAGIC_CRYSTAL) {
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
        if (blockState.getValue(MagicCrystalBlock.BOTTOM)) {
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

    private fun isMaster(): Boolean {
        return blockState.getValue(MagicCrystalBlock.BOTTOM)
    }

    private fun hasMaster(): Boolean {
        return master != NO_MASTER
    }

    override fun tick() {
        super.tick()
        if (level?.isClientSide == true) {
            val percentFull = getVitae() / getMaxVitae()
            prevRenderHeightOffset = renderHeightOffset
            renderHeightOffset = sin((ticksExisted) * BOB_SPEED * percentFull) * 0.5 + 0.5
            prevRenderRotation = renderRotation
            renderRotation = (renderRotation + SPIN_SPEED * percentFull)
            // If we've gone full circle wrap around. Update prevRenderRotation to be negative temporarily to fix interpolation
            if (renderRotation > 360) {
                prevRenderRotation = prevRenderRotation - renderRotation
                renderRotation = renderRotation % 360
            }
        }
    }

    fun getRenderHeightOffset(partialTicks: Float): Double {
        return MathHelper.lerp(partialTicks.toDouble(), prevRenderHeightOffset, renderHeightOffset)
    }

    fun getRenderRotation(partialTicks: Float): Double {
        return MathHelper.lerp(partialTicks.toDouble(), prevRenderRotation, renderRotation)
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

    override fun getRenderBoundingBox(): AxisAlignedBB {
        return super.getRenderBoundingBox().expandTowards(0.0, 16.0 * (MagicCrystalBlock.BLOCK_HEIGHT - 1), 0.0)
    }

    override fun getViewDistance(): Double {
        return 192.0
    }

    override fun getUpdatePacket(): SUpdateTileEntityPacket {
        return SUpdateTileEntityPacket(blockPos, -1, save(CompoundNBT()))
    }

    override fun onDataPacket(net: NetworkManager, pkt: SUpdateTileEntityPacket) {
        // Don't accept any changes from client -> server, only process server -> client
        if (net.direction == PacketDirection.CLIENTBOUND) {
            load(blockState, pkt.tag)
        }
    }

    override fun getUpdateTag(): CompoundNBT {
        return save(CompoundNBT())
    }

    override fun save(compound: CompoundNBT): CompoundNBT {
        super.save(compound)
        // No need to serialize slaves
        if (isMaster()) {
            compound.putDouble("vitae", vitae)
        }
        return compound
    }

    override fun load(blockState: BlockState, compound: CompoundNBT) {
        super.load(blockState, compound)
        if (compound.contains("vitae")) {
            vitae = compound.getDouble("vitae")
        }
    }

    companion object {
        private const val BOB_SPEED = 0.1
        private const val SPIN_SPEED = 1.2
        private const val MAX_VITAE = 10000.0
        private val NO_MASTER = MagicCrystalTileEntity()
    }
}