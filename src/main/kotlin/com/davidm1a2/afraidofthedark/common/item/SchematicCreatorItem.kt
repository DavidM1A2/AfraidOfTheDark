package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.schematic.CachedSchematic
import com.davidm1a2.afraidofthedark.common.schematic.SchematicUtils
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.DoubleTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.AABB
import org.apache.commons.lang3.RandomStringUtils
import java.io.File
import kotlin.math.max
import kotlin.math.min

/**
 * Item that allows for creation of mod schematics, not intended for player use
 *
 * @constructor sets up item properties
 */
class SchematicCreatorItem : AOTDItem("schematic_creator", Properties().stacksTo(1), displayInCreative = false) {
    /**
     * Called when the user right clicks with the journal. We show the research UI if they have started the mod
     *
     * @param world  The world that the item was right clicked in
     * @param player The player that right clicked the item
     * @param hand   The hand that the item is in
     * @return An action result that determines if the right click was.
     * Success = The call has succeeded in doing what was needed and should stop here.
     * Pass    = The call succeeded, but more calls can be made farther down the call stack.
     * Fail    = The call has failed to do what was intended and should stop here.
     */
    override fun use(world: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val mainhandItem = player.mainHandItem

        // Server side processing only
        if (!world.isClientSide) {
            // If the offhand item is a diamond save
            if (player.offhandItem.item == Items.DIAMOND) {
                // Ensure pos1 and pos2 are set
                if (NBTHelper.hasTag(mainhandItem, NBT_POS_1) && NBTHelper.hasTag(mainhandItem, NBT_POS_2)) {
                    val schematicName = saveStructure(
                        world,
                        NbtUtils.readBlockPos(NBTHelper.getCompound(mainhandItem, NBT_POS_1)!!),
                        NbtUtils.readBlockPos(NBTHelper.getCompound(mainhandItem, NBT_POS_2)!!),
                        mainhandItem.hoverName.string
                    )

                    // If the name is empty it means the name is invalid
                    if (schematicName.isNotEmpty()) {
                        player.sendMessage(TextComponent("Schematic '$schematicName' saved successfully"))
                    } else {
                        player.sendMessage(
                            TextComponent("Schematic '${mainhandItem.hoverName.string}' has an invalid name (No ., \\, /, or space)")
                        )
                    }
                } else {
                    player.sendMessage(TextComponent("Please set pos1 and pos2 before saving"))
                }
            } else {
                // If the player is sneaking, set pos2, otherwise set pos 1
                if (player.isCrouching) {
                    NBTHelper.setCompound(mainhandItem, NBT_POS_2, NbtUtils.writeBlockPos(player.blockPosition().below()))
                    player.sendMessage(TextComponent("Pos2 set: " + player.blockPosition().below().toString()))
                } else {
                    NBTHelper.setCompound(mainhandItem, NBT_POS_1, NbtUtils.writeBlockPos(player.blockPosition().below()))
                    player.sendMessage(TextComponent("Pos1 set: " + player.blockPosition().below()))
                }
            }
        }

        return InteractionResultHolder.success(mainhandItem)
    }

    /**
     * Saves the structure into NBT format
     *
     * @param world The world the structure is in
     * @param pos1 The first block position
     * @param pos2 The second block position
     * @return The name of the structure file, or empty string indicating error
     */
    private fun saveStructure(world: Level, pos1: BlockPos, pos2: BlockPos, name: String): String {
        val smallPos = BlockPos(min(pos1.x, pos2.x), min(pos1.y, pos2.y), min(pos1.z, pos2.z))
        val largePos = BlockPos(max(pos1.x, pos2.x), max(pos1.y, pos2.y), max(pos1.z, pos2.z))

        // Compute width/height/length
        val width = largePos.x - smallPos.x + 1
        val height = largePos.y - smallPos.y + 1
        val length = largePos.z - smallPos.z + 1

        // Get all entities in the schematic, write them to NBT and save them
        val entities = world.getEntities(null, AABB(smallPos, largePos)) { true }
        val nbtEntites = ListTag()
        entities.forEach {
            nbtEntites.add(it.serializeNBT().apply {
                relativizeEntityPos(this, smallPos)
            })
        }

        val nbtTileEntities = ListTag()
        val blocks = Array(width * height * length) { Blocks.AIR.defaultBlockState() }

        var index = 0
        // Go over each x,y,z coordinate
        for (y in smallPos.y..largePos.y) {
            for (z in smallPos.z..largePos.z) {
                for (x in smallPos.x..largePos.x) {
                    val pos = BlockPos(x, y, z)
                    // Save the block, the metadata, and the tile entity if it exists
                    blocks[index] = world.getBlockState(pos)
                    world.getBlockEntity(pos)?.let {
                        nbtTileEntities.add(it.serializeNBT().apply {
                            relativizeTileEntityPos(this, smallPos)
                        })
                    }

                    index++
                }
            }
        }

        // Create the schematic and write it to a file
        val schematic = CachedSchematic(
            name,
            nbtTileEntities,
            width.toShort(),
            height.toShort(),
            length.toShort(),
            blocks,
            nbtEntites
        )

        val schematicName = "$name${RandomStringUtils.randomAlphabetic(10)}"
        val illegalStrings = setOf(".", "/", "\\", " ")

        // Don't allow players to use ../../ to write arbitrary files
        if (illegalStrings.any { schematicName.contains(it) }) {
            return ""
        }

        SchematicUtils.writeToFile(schematic, File("./aotd_schematics/$schematicName.schematic"))
        return schematicName
    }

    /**
     * Turns absolute x,y,z coordinates into relative ones based on a corner position
     *
     * @param nbt The compound to update
     * @param baseCorner The corner to be relative to
     */
    private fun relativizeEntityPos(nbt: CompoundTag, baseCorner: BlockPos) {
        val absolutePos = nbt.get("Pos") as ListTag
        nbt.put("Pos", ListTag().apply {
            add(DoubleTag.valueOf(absolutePos.getDouble(0) - baseCorner.x))
            add(DoubleTag.valueOf(absolutePos.getDouble(1) - baseCorner.y))
            add(DoubleTag.valueOf(absolutePos.getDouble(2) - baseCorner.z))
        })
    }

    /**
     * Turns absolute x,y,z coordinates into relative ones based on a corner position
     *
     * @param nbt The compound to update
     * @param baseCorner The corner to be relative to
     */
    private fun relativizeTileEntityPos(nbt: CompoundTag, baseCorner: BlockPos) {
        // Make x, y, z relative to the corner
        nbt.putInt("x", nbt.getInt("x") - baseCorner.x)
        nbt.putInt("y", nbt.getInt("y") - baseCorner.y)
        nbt.putInt("z", nbt.getInt("z") - baseCorner.z)
    }

    /**
     * Adds a tooltip to the schematic creator
     *
     * @param stack   The itemstack to add tooltips for
     * @param world The world the itemstack is in
     * @param tooltip The tooltip of the item
     * @param flag  If the advanced details is on or off
     */
    override fun appendHoverText(stack: ItemStack, world: Level?, tooltip: MutableList<Component>, flag: TooltipFlag) {
        tooltip.add(TextComponent("Note: This item is for mod developer use only"))
        tooltip.add(TextComponent("Pos1: " + (NBTHelper.getCompound(stack, NBT_POS_1)?.let { NbtUtils.readBlockPos(it).toString() } ?: "None")))
        tooltip.add(TextComponent("Pos2: " + (NBTHelper.getCompound(stack, NBT_POS_2)?.let { NbtUtils.readBlockPos(it).toString() } ?: "None")))
        tooltip.add(TextComponent("Right click to set pos1"))
        tooltip.add(TextComponent("Shift+Right click to set pos2"))
        tooltip.add(TextComponent("Right click with a diamond in hand to save the schematic"))
    }

    companion object {
        // Strings used as keys by NBT
        private const val NBT_POS_1 = "pos1"
        private const val NBT_POS_2 = "pos2"
    }
}