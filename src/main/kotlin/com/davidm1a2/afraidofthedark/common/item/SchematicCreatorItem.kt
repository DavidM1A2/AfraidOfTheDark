package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import com.davidm1a2.afraidofthedark.common.world.schematic.CachedSchematic
import com.davidm1a2.afraidofthedark.common.world.schematic.SchematicUtils
import net.minecraft.block.Blocks
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.DoubleNBT
import net.minecraft.nbt.ListNBT
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import org.apache.commons.lang3.RandomStringUtils
import java.io.File
import kotlin.math.max
import kotlin.math.min

/**
 * Item that allows for creation of mod schematics, not intended for player use
 *
 * @constructor sets up item properties
 */
class SchematicCreatorItem : AOTDItem("schematic_creator", Properties().maxStackSize(1), displayInCreative = false) {
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
    override fun onItemRightClick(world: World, player: PlayerEntity, hand: Hand): ActionResult<ItemStack> {
        val mainhandItem = player.heldItemMainhand

        // Server side processing only
        if (!world.isRemote) {
            // If the offhand item is a diamond save
            if (player.heldItemOffhand.item == Items.DIAMOND) {
                // Ensure pos1 and pos2 are set
                if (NBTHelper.hasTag(mainhandItem, NBT_POS_1) && NBTHelper.hasTag(mainhandItem, NBT_POS_2)) {
                    val schematicName = saveStructure(
                        world,
                        NBTUtil.readBlockPos(NBTHelper.getCompound(mainhandItem, NBT_POS_1)!!),
                        NBTUtil.readBlockPos(NBTHelper.getCompound(mainhandItem, NBT_POS_2)!!),
                        mainhandItem.displayName.formattedText
                    )

                    // If the name is empty it means the name is invalid
                    if (schematicName.isNotEmpty()) {
                        player.sendMessage(StringTextComponent("Schematic '$schematicName' saved successfully"))
                    } else {
                        player.sendMessage(StringTextComponent("Schematic '${mainhandItem.displayName}' has an invalid name (No ., \\, /, or space)"))
                    }
                } else {
                    player.sendMessage(StringTextComponent("Please set pos1 and pos2 before saving"))
                }
            } else {
                // If the player is sneaking, set pos2, otherwise set pos 1
                if (player.isSneaking) {
                    NBTHelper.setCompound(mainhandItem, NBT_POS_2, NBTUtil.writeBlockPos(player.position.down()))
                    player.sendMessage(StringTextComponent("Pos2 set: " + player.position.down().toString()))
                } else {
                    NBTHelper.setCompound(mainhandItem, NBT_POS_1, NBTUtil.writeBlockPos(player.position.down()))
                    player.sendMessage(StringTextComponent("Pos1 set: " + player.position.down().toString()))
                }
            }
        }

        return ActionResult.resultSuccess(mainhandItem)
    }

    /**
     * Saves the structure into NBT format
     *
     * @param world The world the structure is in
     * @param pos1 The first block position
     * @param pos2 The second block position
     * @return The name of the structure file, or empty string indicating error
     */
    private fun saveStructure(world: World, pos1: BlockPos, pos2: BlockPos, name: String): String {
        val smallPos = BlockPos(min(pos1.x, pos2.x), min(pos1.y, pos2.y), min(pos1.z, pos2.z))
        val largePos = BlockPos(max(pos1.x, pos2.x), max(pos1.y, pos2.y), max(pos1.z, pos2.z))

        // Compute width/height/length
        val width = largePos.x - smallPos.x + 1
        val height = largePos.y - smallPos.y + 1
        val length = largePos.z - smallPos.z + 1

        // Get all entities in the schematic, write them to NBT and save them
        val entities = world.getEntitiesInAABBexcluding(null, AxisAlignedBB(smallPos, largePos), null)
        val nbtEntites = ListNBT()
        entities.forEach {
            nbtEntites.add(it.serializeNBT().apply {
                relativizeEntityPos(this, smallPos)
            })
        }

        val nbtTileEntities = ListNBT()
        val blocks = Array(width * height * length) { Blocks.AIR.defaultState }

        var index = 0
        // Go over each x,y,z coordinate
        for (y in smallPos.y..largePos.y) {
            for (z in smallPos.z..largePos.z) {
                for (x in smallPos.x..largePos.x) {
                    val pos = BlockPos(x, y, z)
                    // Save the block, the metadata, and the tile entity if it exists
                    blocks[index] = world.getBlockState(pos)
                    world.getTileEntity(pos)?.let {
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
    private fun relativizeEntityPos(nbt: CompoundNBT, baseCorner: BlockPos) {
        val absolutePos = nbt.get("Pos") as ListNBT
        nbt.put("Pos", ListNBT().apply {
            add(DoubleNBT.valueOf(absolutePos.getDouble(0) - baseCorner.x))
            add(DoubleNBT.valueOf(absolutePos.getDouble(1) - baseCorner.y))
            add(DoubleNBT.valueOf(absolutePos.getDouble(2) - baseCorner.z))
        })
    }

    /**
     * Turns absolute x,y,z coordinates into relative ones based on a corner position
     *
     * @param nbt The compound to update
     * @param baseCorner The corner to be relative to
     */
    private fun relativizeTileEntityPos(nbt: CompoundNBT, baseCorner: BlockPos) {
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
    @OnlyIn(Dist.CLIENT)
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<ITextComponent>, flag: ITooltipFlag) {
        tooltip.add(StringTextComponent("Note: This item is for mod developer use only"))
        tooltip.add(StringTextComponent("Pos1: " + (NBTHelper.getCompound(stack, NBT_POS_1)?.let { NBTUtil.readBlockPos(it).toString() } ?: "None")))
        tooltip.add(StringTextComponent("Pos2: " + (NBTHelper.getCompound(stack, NBT_POS_2)?.let { NBTUtil.readBlockPos(it).toString() } ?: "None")))
        tooltip.add(StringTextComponent("Right click to set pos1"))
        tooltip.add(StringTextComponent("Shift+Right click to set pos2"))
        tooltip.add(StringTextComponent("Right click with a diamond in hand to save the schematic"))
    }

    companion object {
        // Strings used as keys by NBT
        private const val NBT_POS_1 = "pos1"
        private const val NBT_POS_2 = "pos2"
    }
}