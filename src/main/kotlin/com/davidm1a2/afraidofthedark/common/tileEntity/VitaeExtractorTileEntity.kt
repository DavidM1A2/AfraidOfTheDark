package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.item.VitaeLanternItem
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.Connection
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.tags.BlockTags
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class VitaeExtractorTileEntity(blockPos: BlockPos, blockState: BlockState) : AOTDTickingTileEntity(ModTileEntities.VITAE_EXTRACTOR, blockPos, blockState), WorldlyContainer {
    private var lantern: ItemStack = ItemStack.EMPTY
    private var fuelVitaePerTick: Float = 0f
    private var burnTicksLeft: Int = 0

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

    private fun getFuelValue(itemStack: ItemStack): Float {
        return FUEL_ITEM_TO_VITAE_PER_TICK[itemStack.item]
            ?: FUEL_TAG_TO_VITAE_PER_TICK.find { it.first.contains(itemStack.item) }?.second
            ?: 0f
    }

    override fun save(compound: CompoundTag): CompoundTag {
        super.save(compound)
        compound.put("lantern", lantern.serializeNBT())
        compound.putInt("burn_ticks_left", burnTicksLeft)
        compound.putFloat("fuel_vitae_per_tick", fuelVitaePerTick)
        return compound
    }

    override fun load(compound: CompoundTag) {
        super.load(compound)
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

    override fun stillValid(playerEntity: Player): Boolean {
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

        private const val SACRED_MANGROVE_VALUE = 1f
        private const val MANGROVE_VALUE = 0.1f
        private const val NATURE_VALUE = 0.05f

        private val FUEL_ITEM_TO_VITAE_PER_TICK = mapOf(
            ModBlocks.SACRED_MANGROVE.asItem() to SACRED_MANGROVE_VALUE * 4f,
            ModBlocks.SACRED_MANGROVE_LEAVES.asItem() to SACRED_MANGROVE_VALUE * 0.5f,
            ModBlocks.SACRED_MANGROVE_SAPLING.asItem() to SACRED_MANGROVE_VALUE * 6f,
            ModBlocks.SACRED_MANGROVE_BUTTON.asItem() to SACRED_MANGROVE_VALUE * 0.1f,
            ModBlocks.SACRED_MANGROVE_DOOR.asItem() to SACRED_MANGROVE_VALUE * 2f,
            ModBlocks.SACRED_MANGROVE_FENCE.asItem() to SACRED_MANGROVE_VALUE,
            ModBlocks.SACRED_MANGROVE_FENCE_GATE.asItem() to SACRED_MANGROVE_VALUE,
            ModBlocks.SACRED_MANGROVE_PLANKS.asItem() to SACRED_MANGROVE_VALUE,
            ModBlocks.SACRED_MANGROVE_SLAB.asItem() to SACRED_MANGROVE_VALUE * 0.5f,
            ModBlocks.SACRED_MANGROVE_STAIRS.asItem() to SACRED_MANGROVE_VALUE * 0.75f,
            ModBlocks.MANGROVE.asItem() to MANGROVE_VALUE * 4f,
            ModBlocks.MANGROVE_LEAVES.asItem() to MANGROVE_VALUE * 0.5f,
            ModBlocks.MANGROVE_SAPLING.asItem() to MANGROVE_VALUE * 6f,
            ModBlocks.MANGROVE_BUTTON.asItem() to MANGROVE_VALUE * 0.1f,
            ModBlocks.MANGROVE_DOOR.asItem() to MANGROVE_VALUE * 2f,
            ModBlocks.MANGROVE_FENCE.asItem() to MANGROVE_VALUE,
            ModBlocks.MANGROVE_FENCE_GATE.asItem() to MANGROVE_VALUE,
            ModBlocks.MANGROVE_PLANKS.asItem() to MANGROVE_VALUE,
            ModBlocks.MANGROVE_SLAB.asItem() to MANGROVE_VALUE * 0.5f,
            ModBlocks.MANGROVE_STAIRS.asItem() to MANGROVE_VALUE * 0.75f
        )
        private val FUEL_TAG_TO_VITAE_PER_TICK = listOf(
            BlockTags.LOGS to NATURE_VALUE * 4f,
            BlockTags.LEAVES to NATURE_VALUE * 0.5f,
            BlockTags.SAPLINGS to NATURE_VALUE * 6f,
            BlockTags.WOODEN_BUTTONS to NATURE_VALUE * 0.1f,
            BlockTags.WOODEN_DOORS to NATURE_VALUE * 2f,
            BlockTags.WOODEN_FENCES to NATURE_VALUE,
            BlockTags.FENCE_GATES to NATURE_VALUE,
            BlockTags.PLANKS to NATURE_VALUE,
            BlockTags.WOODEN_SLABS to NATURE_VALUE * 0.5f,
            BlockTags.WOODEN_STAIRS to NATURE_VALUE * 0.75f
        ).map {
            it.first.values.map(Block::asItem).toSet() to it.second
        }

        fun tick(level: Level, blockPos: BlockPos, blockState: BlockState, blockEntity: VitaeExtractorTileEntity) {
            baseTick(level, blockPos, blockState, blockEntity)
            if (blockEntity.isBurningFuel()) {
                if (!blockEntity.lantern.isEmpty) {
                    ModItems.VITAE_LANTERN.addVitae(blockEntity.lantern, blockEntity.fuelVitaePerTick)
                }

                blockEntity.burnTicksLeft = blockEntity.burnTicksLeft - 1
                if (blockEntity.burnTicksLeft == 0) {
                    blockEntity.fuelVitaePerTick = 0f
                    blockEntity.setChangedAndTellClients()
                } else {
                    setChanged(level, blockPos, blockState)
                }
            }
        }
    }
}