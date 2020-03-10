package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import com.davidm1a2.afraidofthedark.common.worldGeneration.schematic.CachedSchematic
import com.davidm1a2.afraidofthedark.common.worldGeneration.schematic.SchematicDebugUtils
import net.minecraft.block.Block
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagDouble
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.commons.lang3.RandomStringUtils
import java.io.File
import kotlin.math.max
import kotlin.math.min

/**
 * Item that allows for creation of mod schematics, not intended for player use
 *
 * @constructor sets up item properties
 */
class ItemSchematicCreator : AOTDItem("schematic_creator", displayInCreative = false) {
    init {
        setMaxStackSize(1)
    }

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
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val mainhandItem = player.heldItemMainhand

        // Server side processing only
        if (!world.isRemote) {
            // If the offhand item is a diamond save
            if (player.heldItemOffhand.item == Items.DIAMOND) {
                // Ensure pos1 and pos2 are set
                if (NBTHelper.hasTag(mainhandItem, NBT_POS_1) && NBTHelper.hasTag(mainhandItem, NBT_POS_2)) {
                    val schematicName = saveStructure(
                        world,
                        NBTUtil.getPosFromTag(NBTHelper.getCompound(mainhandItem, NBT_POS_1)!!),
                        NBTUtil.getPosFromTag(NBTHelper.getCompound(mainhandItem, NBT_POS_2)!!),
                        mainhandItem.displayName
                    )

                    // If the name is empty it means the name is invalid
                    if (schematicName.isNotEmpty()) {
                        player.sendMessage(TextComponentString("Schematic '$schematicName' saved successfully"))
                    } else {
                        player.sendMessage(TextComponentString("Schematic '${mainhandItem.displayName}' has an invalid name (No ., \\, /, or space)"))
                    }
                } else {
                    player.sendMessage(TextComponentString("Please set pos1 and pos2 before saving"))
                }
            } else {
                // If the player is sneaking, set pos2, otherwise set pos 1
                if (player.isSneaking) {
                    NBTHelper.setCompound(mainhandItem, NBT_POS_2, NBTUtil.createPosTag(player.position.down()))
                    player.sendMessage(TextComponentString("Pos2 set: " + player.position.down().toString()))
                } else {
                    NBTHelper.setCompound(mainhandItem, NBT_POS_1, NBTUtil.createPosTag(player.position.down()))
                    player.sendMessage(TextComponentString("Pos1 set: " + player.position.down().toString()))
                }
            }
        }

        return ActionResult.newResult(EnumActionResult.SUCCESS, mainhandItem)
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
        val nbtEntites = NBTTagList()
        entities.forEach {
            nbtEntites.appendTag(it.serializeNBT().apply {
                relativizeEntityPos(this, smallPos)
            })
        }

        val nbtTileEntities = NBTTagList()
        val blocks = Array<Block>(width * height * length) { Blocks.AIR }
        val data = IntArray(width * height * length)

        var index = 0
        // Go over each x,y,z coordinate
        for (y in smallPos.y..largePos.y) {
            for (z in smallPos.z..largePos.z) {
                for (x in smallPos.x..largePos.x) {
                    val pos = BlockPos(x, y, z)
                    val block = world.getBlockState(pos)

                    // Save the block, the metadata, and the tile entity if it exists
                    blocks[index] = block.block
                    data[index] = block.block.getMetaFromState(block)
                    world.getTileEntity(pos)?.let {
                        nbtTileEntities.appendTag(it.serializeNBT().apply {
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
            data,
            nbtEntites
        )

        val schematicName = "$name${RandomStringUtils.randomAlphabetic(10)}"
        val illegalStrings = setOf(".", "/", "\\", " ")

        // Don't allow players to use ../../ to write arbitrary files
        if (illegalStrings.any { schematicName.contains(it) }) {
            return ""
        }

        SchematicDebugUtils.writeToFile(schematic, File("./aotd_schematics/$schematicName.schematic"))
        return schematicName
    }

    /**
     * Turns absolute x,y,z coordinates into relative ones based on a corner position
     *
     * @param nbt The compound to update
     * @param baseCorner The corner to be relative to
     */
    private fun relativizeEntityPos(nbt: NBTTagCompound, baseCorner: BlockPos) {
        val absolutePos = nbt.getTag("Pos") as NBTTagList
        nbt.setTag("Pos", NBTTagList().apply {
            appendTag(NBTTagDouble(absolutePos.getDoubleAt(0) - baseCorner.x))
            appendTag(NBTTagDouble(absolutePos.getDoubleAt(1) - baseCorner.y))
            appendTag(NBTTagDouble(absolutePos.getDoubleAt(2) - baseCorner.z))
        })
    }

    /**
     * Turns absolute x,y,z coordinates into relative ones based on a corner position
     *
     * @param nbt The compound to update
     * @param baseCorner The corner to be relative to
     */
    private fun relativizeTileEntityPos(nbt: NBTTagCompound, baseCorner: BlockPos) {
        // Make x, y, z relative to the corner
        nbt.setInteger("x", nbt.getInteger("x") - baseCorner.x)
        nbt.setInteger("y", nbt.getInteger("y") - baseCorner.y)
        nbt.setInteger("z", nbt.getInteger("z") - baseCorner.z)
    }

    /**
     * Adds a tooltip to the schematic creator
     *
     * @param stack   The itemstack to add tooltips for
     * @param world The world the itemstack is in
     * @param tooltip The tooltip of the item
     * @param flag  If the advanced details is on or off
     */
    @SideOnly(Side.CLIENT)
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        tooltip.add("Note: This item is for mod developer use only")
        tooltip.add("Pos1: " + (NBTHelper.getCompound(stack, NBT_POS_1)?.let { NBTUtil.getPosFromTag(it).toString() }
            ?: "None"))
        tooltip.add("Pos2: " + (NBTHelper.getCompound(stack, NBT_POS_2)?.let { NBTUtil.getPosFromTag(it).toString() }
            ?: "None"))
        tooltip.add("Right click to set pos1")
        tooltip.add("Shift+Right click to set pos2")
        tooltip.add("Right click with a diamond in hand to save the schematic")
    }

    companion object {
        // Strings used as keys by NBT
        private const val NBT_POS_1 = "pos1"
        private const val NBT_POS_2 = "pos2"
    }
}