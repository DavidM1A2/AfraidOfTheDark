package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.particle.FeyParticleData
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.Connection
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import java.awt.Color
import kotlin.random.Random

class FeyLightTileEntity(blockPos: BlockPos, blockState: BlockState) : AOTDTickingTileEntity(ModTileEntities.FEY_LIGHT, blockPos, blockState) {
    var color: Color = Color.MAGENTA

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
        compound.putInt("color_r", color.red)
        compound.putInt("color_g", color.green)
        compound.putInt("color_b", color.blue)
        return compound
    }

    override fun load(compound: CompoundTag) {
        super.load(compound)
        color = Color(compound.getInt("color_r"), compound.getInt("color_g"), compound.getInt("color_b"))
    }

    companion object {
        fun clientTick(level: Level, blockPos: BlockPos, blockState: BlockState, blockEntity: FeyLightTileEntity) {
            baseTick(level, blockPos, blockState, blockEntity)
            val centerPosition = Vec3(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble())
                .add(0.5, 0.1, 0.5)

            level.addParticle(
                FeyParticleData(Random.nextFloat() * 360, blockEntity.color.red / 255f, blockEntity.color.green / 255f, blockEntity.color.blue / 255f),
                centerPosition.x,
                centerPosition.y,
                centerPosition.z,
                (Random.nextDouble() - 0.5) * 0.1,
                0.05 + Random.nextDouble() * 0.05,
                (Random.nextDouble() - 0.5) * 0.1
            )
        }
    }
}
