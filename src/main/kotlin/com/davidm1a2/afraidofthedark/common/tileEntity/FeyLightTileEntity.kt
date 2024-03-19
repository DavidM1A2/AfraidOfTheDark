package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.particle.FeyParticleData
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import net.minecraft.block.BlockState
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.PacketDirection
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.level.block.state.BlockState
import java.awt.Color
import kotlin.random.Random

class FeyLightTileEntity(blockPos: BlockPos, blockState: BlockState) : AOTDTickingTileEntity(ModTileEntities.FEY_LIGHT) {
    var color: Color = Color.MAGENTA

    override fun tick() {
        super.tick()
        if (level?.isClientSide == true) {
            val centerPosition = Vector3d(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble())
                .add(0.5, 0.1, 0.5)

            level?.addParticle(
                FeyParticleData(Random.nextFloat() * 360, color.red / 255f, color.green / 255f, color.blue / 255f),
                centerPosition.x,
                centerPosition.y,
                centerPosition.z,
                (Random.nextDouble() - 0.5) * 0.1,
                0.05 + Random.nextDouble() * 0.05,
                (Random.nextDouble() - 0.5) * 0.1
            )
        }
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
        compound.putInt("color_r", color.red)
        compound.putInt("color_g", color.green)
        compound.putInt("color_b", color.blue)
        return compound
    }

    override fun load(blockState: BlockState, compound: CompoundNBT) {
        super.load(blockState, compound)
        color = Color(compound.getInt("color_r"), compound.getInt("color_g"), compound.getInt("color_b"))
    }
}
