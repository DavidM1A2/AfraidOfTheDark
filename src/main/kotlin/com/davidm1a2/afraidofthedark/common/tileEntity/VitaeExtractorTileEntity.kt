package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTileEntity
import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.PacketDirection
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraftforge.common.util.Constants

class VitaeExtractorTileEntity : AOTDTileEntity(ModTileEntities.VITAE_EXTRACTOR) {
    private var lantern: ItemStack = ItemStack.EMPTY
        set(value) {
            field = value
            setChanged()
            if (level?.isClientSide == false) {
                level?.sendBlockUpdated(blockPos, blockState, blockState, Constants.BlockFlags.BLOCK_UPDATE)
            }
        }

    fun insertLantern(lantern: ItemStack) {
        if (lantern.item != ModItems.VITAE_LANTERN) {
            return
        }

        if (!this.lantern.isEmpty) {
            return
        }

        this.lantern = lantern
    }

    fun clearLantern() {
        this.lantern = ItemStack.EMPTY
    }

    fun getLantern(): ItemStack {
        return this.lantern
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

    override fun save(nbt: CompoundNBT): CompoundNBT {
        super.save(nbt)
        nbt.put("lantern", this.lantern.serializeNBT())
        return nbt
    }

    override fun load(blockState: BlockState, nbt: CompoundNBT) {
        super.load(blockState, nbt)
        if (nbt.contains("lantern")) {
            lantern = ItemStack.of(nbt.getCompound("lantern"))
        }
    }
}