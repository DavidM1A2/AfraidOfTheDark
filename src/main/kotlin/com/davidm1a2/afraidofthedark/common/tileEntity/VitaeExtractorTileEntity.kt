package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.item.VitaeLanternItem
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTileEntity
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.ISidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.PacketDirection
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.util.Direction
import net.minecraftforge.common.util.Constants

class VitaeExtractorTileEntity : AOTDTileEntity(ModTileEntities.VITAE_EXTRACTOR), ISidedInventory {
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

    fun clearLantern(): ItemStack {
        val oldLantern = this.lantern
        this.lantern = ItemStack.EMPTY
        return oldLantern
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

    // ISidedInventory methods to enable hopper usage

    override fun clearContent() {
        lantern = ItemStack.EMPTY
    }

    override fun getContainerSize(): Int {
        return 1
    }

    override fun isEmpty(): Boolean {
        return lantern.isEmpty
    }

    override fun getItem(index: Int): ItemStack {
        if (index == 0) {
            return lantern
        }
        return ItemStack.EMPTY
    }

    override fun removeItem(index: Int, count: Int): ItemStack {
        if (index == 0 && count == 1) {
            return clearLantern()
        }
        return ItemStack.EMPTY
    }

    override fun removeItemNoUpdate(index: Int): ItemStack {
        if (index == 0) {
            return clearLantern()
        }
        return ItemStack.EMPTY
    }

    override fun setItem(index: Int, itemStack: ItemStack) {
        if (index == 0 && itemStack.item == ModItems.VITAE_LANTERN) {
            this.lantern = itemStack
        }
    }

    override fun stillValid(playerEntity: PlayerEntity): Boolean {
        return false
    }

    override fun getSlotsForFace(direction: Direction): IntArray {
        if (direction == Direction.UP || direction == Direction.DOWN) {
            return intArrayOf(0)
        }
        return intArrayOf()
    }

    override fun canPlaceItemThroughFace(index: Int, itemStack: ItemStack, direction: Direction?): Boolean {
        if (index == 0 &&
            itemStack.item == ModItems.VITAE_LANTERN &&
            direction == Direction.UP
        ) {
            return true
        }
        return false
    }

    override fun canTakeItemThroughFace(index: Int, itemStack: ItemStack, direction: Direction): Boolean {
        if (index == 0 &&
            itemStack.item == ModItems.VITAE_LANTERN &&
            ModItems.VITAE_LANTERN.getChargeLevel(itemStack) == VitaeLanternItem.ChargeLevel.FULL &&
            direction == Direction.DOWN
        ) {
            return true
        }
        return false
    }
}