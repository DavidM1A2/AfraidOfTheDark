package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.item.VitaeLanternItem
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.ISidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.PacketDirection
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tags.BlockTags
import net.minecraft.util.Direction

class VitaeExtractorTileEntity : AOTDTickingTileEntity(ModTileEntities.VITAE_EXTRACTOR), ISidedInventory {
    private var lantern: ItemStack = ItemStack.EMPTY
    private var fuelVitaePerTick: Float = 0f
    private var burnTicksLeft: Int = 0

    override fun tick() {
        super.tick()

        if (isBurningFuel()) {
            if (!lantern.isEmpty) {
                ModItems.VITAE_LANTERN.addVitae(lantern, fuelVitaePerTick)
            }

            burnTicksLeft = burnTicksLeft - 1
            if (burnTicksLeft == 0) {
                fuelVitaePerTick = 0f
                setChangedAndTellClients()
            } else {
                setChanged()
            }
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
        setChangedAndTellClients()
    }

    fun clearLantern(): ItemStack {
        val oldLantern = this.lantern
        this.lantern = ItemStack.EMPTY
        setChangedAndTellClients()
        return oldLantern
    }

    fun getLantern(): ItemStack {
        return this.lantern
    }

    fun insertFuel(itemStack: ItemStack) {
        if (burnTicksLeft > 0) {
            return
        }

        if (!isValidFuel(itemStack)) {
            return
        }

        this.fuelVitaePerTick = getFuelValue(itemStack)
        this.burnTicksLeft = MAX_BURN_TICKS
        setChangedAndTellClients()
    }

    fun isValidFuel(itemStack: ItemStack): Boolean {
        return getFuelValue(itemStack) != 0f
    }

    fun isBurningFuel(): Boolean {
        return burnTicksLeft > 0
    }

    private fun clearFuel() {
        this.fuelVitaePerTick = 0f
        this.burnTicksLeft = 0
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

    private fun getFuelValue(itemStack: ItemStack): Float {
        return FUEL_ITEM_TO_VITAE_PER_TICK[itemStack.item]
            ?: FUEL_TAG_TO_VITAE_PER_TICK.find { it.first.contains(itemStack.item) }?.second
            ?: 0f
    }

    override fun save(compound: CompoundNBT): CompoundNBT {
        super.save(compound)
        compound.put("lantern", lantern.serializeNBT())
        compound.putInt("burn_ticks_left", burnTicksLeft)
        compound.putFloat("fuel_vitae_per_tick", fuelVitaePerTick)
        return compound
    }

    override fun load(blockState: BlockState, compound: CompoundNBT) {
        super.load(blockState, compound)
        lantern = ItemStack.of(compound.getCompound("lantern"))
        burnTicksLeft = compound.getInt("burn_ticks_left")
        fuelVitaePerTick = compound.getFloat("fuel_vitae_per_tick")
    }

    // ISidedInventory methods to enable hopper usage

    override fun clearContent() {
        clearLantern()
        clearFuel()
    }

    override fun getContainerSize(): Int {
        return 2 // LANTERN_INDEX and FUEL_INDEX
    }

    override fun isEmpty(): Boolean {
        return getLantern().isEmpty && !isBurningFuel()
    }

    override fun getItem(index: Int): ItemStack {
        if (index == LANTERN_INDEX) {
            return getLantern()
        }
        return ItemStack.EMPTY
    }

    override fun removeItem(index: Int, count: Int): ItemStack {
        if (index == LANTERN_INDEX && count == 1) {
            return clearLantern()
        }
        if (index == FUEL_INDEX && count == 1) {
            clearFuel()
        }
        return ItemStack.EMPTY
    }

    override fun removeItemNoUpdate(index: Int): ItemStack {
        if (index == LANTERN_INDEX) {
            return clearLantern()
        }
        if (index == FUEL_INDEX) {
            clearFuel()
        }
        return ItemStack.EMPTY
    }

    override fun setItem(index: Int, itemStack: ItemStack) {
        if (index == LANTERN_INDEX) {
            insertLantern(itemStack)
        }
        if (index == FUEL_INDEX) {
            insertFuel(itemStack)
        }
    }

    override fun stillValid(playerEntity: PlayerEntity): Boolean {
        return false
    }

    override fun getSlotsForFace(direction: Direction): IntArray {
        return if (direction == Direction.UP || direction == Direction.DOWN) {
            intArrayOf(LANTERN_INDEX)
        } else {
            intArrayOf(FUEL_INDEX)
        }
    }

    override fun canPlaceItemThroughFace(index: Int, itemStack: ItemStack, direction: Direction?): Boolean {
        if (index == LANTERN_INDEX &&
            itemStack.item == ModItems.VITAE_LANTERN &&
            direction == Direction.UP
        ) {
            return true
        }
        if (index == FUEL_INDEX &&
            isValidFuel(itemStack) &&
            !getLantern().isEmpty &&
            !isBurningFuel() &&
            (direction == Direction.NORTH || direction == Direction.SOUTH || direction == Direction.EAST || direction == Direction.WEST)
        ) {
            return true
        }
        return false
    }

    override fun canTakeItemThroughFace(index: Int, itemStack: ItemStack, direction: Direction): Boolean {
        if (index == LANTERN_INDEX &&
            itemStack.item == ModItems.VITAE_LANTERN &&
            ModItems.VITAE_LANTERN.getChargeLevel(itemStack) == VitaeLanternItem.ChargeLevel.FULL &&
            direction == Direction.DOWN
        ) {
            return true
        }
        return false
    }

    companion object {
        private const val LANTERN_INDEX = 0
        private const val FUEL_INDEX = 1
        private const val MAX_BURN_TICKS = 20 * 10
        private val FUEL_ITEM_TO_VITAE_PER_TICK = mapOf(
            ModBlocks.SACRED_MANGROVE.asItem() to 3f,
            ModBlocks.SACRED_MANGROVE_LEAVES.asItem() to 3f,
            ModBlocks.SACRED_MANGROVE_SAPLING.asItem() to 3f,
            ModBlocks.SACRED_MANGROVE_BUTTON.asItem() to 3f,
            ModBlocks.SACRED_MANGROVE_DOOR.asItem() to 3f,
            ModBlocks.SACRED_MANGROVE_FENCE.asItem() to 3f,
            ModBlocks.SACRED_MANGROVE_FENCE_GATE.asItem() to 3f,
            ModBlocks.SACRED_MANGROVE_PLANKS.asItem() to 3f,
            ModBlocks.SACRED_MANGROVE_SLAB.asItem() to 3f,
            ModBlocks.SACRED_MANGROVE_STAIRS.asItem() to 3f,
            ModBlocks.MANGROVE.asItem() to 0.1f,
            ModBlocks.MANGROVE_LEAVES.asItem() to 0.1f,
            ModBlocks.MANGROVE_SAPLING.asItem() to 0.1f,
            ModBlocks.MANGROVE_BUTTON.asItem() to 0.1f,
            ModBlocks.MANGROVE_DOOR.asItem() to 0.1f,
            ModBlocks.MANGROVE_FENCE.asItem() to 0.1f,
            ModBlocks.MANGROVE_FENCE_GATE.asItem() to 0.1f,
            ModBlocks.MANGROVE_PLANKS.asItem() to 0.1f,
            ModBlocks.MANGROVE_SLAB.asItem() to 0.1f,
            ModBlocks.MANGROVE_STAIRS.asItem() to 0.1f
        )
        private val FUEL_TAG_TO_VITAE_PER_TICK = listOf(
            BlockTags.LOGS to 0.02f,
            BlockTags.LEAVES to 0.02f,
            BlockTags.SAPLINGS to 0.02f,
            BlockTags.WOODEN_BUTTONS to 0.02f,
            BlockTags.WOODEN_DOORS to 0.02f,
            BlockTags.WOODEN_FENCES to 0.02f,
            BlockTags.FENCE_GATES to 0.02f,
            BlockTags.PLANKS to 0.02f,
            BlockTags.WOODEN_SLABS to 0.02f,
            BlockTags.WOODEN_STAIRS to 0.02f
        ).map {
            it.first.values.map(Block::asItem).toSet() to it.second
        }
    }
}