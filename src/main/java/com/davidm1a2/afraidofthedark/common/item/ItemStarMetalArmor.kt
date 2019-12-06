package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.constants.ModArmorMaterials
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDArmor
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.world.World
import net.minecraftforge.common.ISpecialArmor
import net.minecraftforge.common.ISpecialArmor.ArmorProperties
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * Class representing the 4 different pieces of star metal armor
 *
 * @constructor sets up armor item properties
 * @param baseName        The name of the item to be used by the game registry
 * @param equipmentSlotIn The slot that this armor pieces goes on, can be one of 4 options
 */
class ItemStarMetalArmor(baseName: String, equipmentSlot: EntityEquipmentSlot) : AOTDArmor(baseName, ModArmorMaterials.STAR_METAL, 3, equipmentSlot), ISpecialArmor
{
    init
    {
        // Makes the armor invincible
        maxDamage = 0
        // Block 70% of the damage up to 20
        maxDamageBlocked = 20
        percentOfDamageBlocked = 0.7
    }

    /**
     * Gets the resource location path of the texture for the armor when worn by the player
     *
     * @param stack  The armor itemstack
     * @param entity The entity that is wearing the  armor
     * @param slot   The slot the armor is in
     * @param type   The subtype, can be null or "overlay"
     * @return Path of texture to bind, or null to use default
     */
    override fun getArmorTexture(stack: ItemStack, entity: Entity, slot: EntityEquipmentSlot, type: String): String?
    {
        // Star metal 1 is for helm, boots, and chest while Star metal 2 is for leggings
        return if (slot == EntityEquipmentSlot.LEGS)
        {
            "afraidofthedark:textures/armor/star_metal_2.png"
        }
        else
        {
            "afraidofthedark:textures/armor/star_metal_1.png"
        }
    }

    /**
     * Adds a tooltip to the armor piece
     *
     * @param stack   The itemstack to add a tooltip to
     * @param world The world the item is in
     * @param tooltip The tooltip list to add to
     * @param flag  The flag telling us if advanced tooltips are on or not
     */
    @SideOnly(Side.CLIENT)
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag)
    {
        val player = Minecraft.getMinecraft().player
        if (player != null && player.getCapability(ModCapabilities.PLAYER_RESEARCH, null)!!.isResearched(ModResearches.STAR_METAL))
        {
            tooltip.add("Magical armor will never break.")
            tooltip.add("Gives you two absorption hearts per piece.")
        }
        else
        {
            tooltip.add("I dont know how to use this.")
        }
    }

    /**
     * Called every tick the armor is being worn
     *
     * @param world     The world the player is in
     * @param player    The player that is wearing the armor
     * @param itemStack The itemstack of the armor item
     */
    override fun onArmorTick(world: World, player: EntityPlayer, itemStack: ItemStack)
    {
        // We have to test client and server side since absorption is client side :(
        // Test if the player has the star metal research
        if (player.getCapability(ModCapabilities.PLAYER_RESEARCH, null)!!.isResearched(ModResearches.STAR_METAL))
        {
            // If the stack is ready to proc absorption, add 2 absorption hearts
            if (readyToProcAbsorption(itemStack))
            {
                // The number of star metal armor pieces worn
                val numberStarMetalPiecesWorn = player.inventory.armorInventory.map { it.item }.filterIsInstance<ItemStarMetalArmor>().count()
                if (numberStarMetalPiecesWorn * ABSORPTION_PER_PIECE >= player.absorptionAmount)
                {
                    // Add 4 to absorption up to a max for the proc
                    player.absorptionAmount = (player.absorptionAmount + ABSORPTION_PER_PIECE).coerceIn(0f, numberStarMetalPiecesWorn * ABSORPTION_PER_PIECE.toFloat())
                }

                // Update the last proc time to now
                NBTHelper.setLong(itemStack, NBT_LAST_ABSORPTION_PROC, System.currentTimeMillis())
            }
        }
    }

    /**
     * True if the armor is ready to proc absorption, false otherwise
     *
     * @param itemStack The itemstack to test
     * @return True if absorption can be proc'd, false otherwise
     */
    private fun readyToProcAbsorption(itemStack: ItemStack): Boolean
    {
        return !NBTHelper.hasTag(itemStack, NBT_LAST_ABSORPTION_PROC) || System.currentTimeMillis() > NBTHelper.getLong(
            itemStack,
            NBT_LAST_ABSORPTION_PROC
        ) + ABSORPTION_PROC_CD_MILLIS
    }

    /**
     * Armor can't be damaged, just return
     *
     * @param entity THe entity that is wearing the armor
     * @param stack  The itemstack that is being worn
     * @param source The damage source that is hitting the armor
     * @param damage The amount of damage to apply
     * @param slot   The slot that is damaged
     */
    override fun damageArmor(entity: EntityLivingBase, stack: ItemStack, source: DamageSource, damage: Int, slot: Int)
    {
    }

    /**
     * Returns the number of additional shields to display when wearing the armor
     *
     * @param player The player wearing the armor
     * @param armor  The armor itemstack
     * @param slot   The slot the item is in
     * @return THe number of shields to display when wearing the armor
     */
    override fun getArmorDisplay(player: EntityPlayer, armor: ItemStack, slot: Int): Int
    {
        // 0, since we only want to display default armor values
        return 0
    }

    /**
     * Called when unblockable damage is applied, just return the default false
     *
     * @param entity The entity wearing the armor
     * @param armor  The armor itemstack
     * @param source The damage source applied
     * @param damage The damage inflicted
     * @param slot   The armor slot
     * @return False, let the damage be handled
     */
    override fun handleUnblockableDamage(entity: EntityLivingBase, armor: ItemStack, source: DamageSource, damage: Double, slot: Int): Boolean
    {
        return false
    }

    /**
     * Returns the armor properties for a given item in the player's armor inventory
     *
     * @param entity The player that is wearing the armor
     * @param armor  The armor item that is being worn
     * @param source The damage source that hit the player
     * @param damage The damage inflicted
     * @param slot   The slot containing the armor block
     * @return The armor's properties for these damage types
     */
    override fun getProperties(entity: EntityLivingBase, armor: ItemStack, source: DamageSource, damage: Double, slot: Int): ArmorProperties
    {
        // Compute armor properties for players only
        if (entity is EntityPlayer)
        {
            // Ensure the player has the right research
            if (entity.getCapability(ModCapabilities.PLAYER_RESEARCH, null)!!.isResearched(ModResearches.STAR_METAL))
            {
                // No damage reduction against true sources
                if (TRUE_DAMAGE_SOURCES.contains(source))
                {
                    return ArmorProperties(0, getRatio(slot), 0)
                }
            }
            else
            {
                // Armor is useless without research
                return ArmorProperties(0, getRatio(slot), 0)
            }
        }
        else
        {
            // Armor is useless without research
            return ArmorProperties(0, getRatio(slot), 0)
        }

        // Default armor protection if no special set bonus applies
        return getDefaultProperties(slot)
    }

    /**
     * Returns the ratio of protection each pieces gives
     *
     * @param slot The slot the armor is in
     * @return The ratio of protection of each piece reduced by the percent damage blocked
     */
    private fun getRatio(slot: Int): Double
    {
        // Total protection of each piece
        val totalProtection = 3 + 6 + 8 + 3
        return when (slot)
        {
            0, 3 -> 3.0 / totalProtection * percentOfDamageBlocked
            1 -> 6.0 / totalProtection * percentOfDamageBlocked
            2 -> 8.0 / totalProtection * percentOfDamageBlocked
            else -> 0.0
        }
    }

    /**
     * Gets the default armor properties when taking damage
     *
     * @param slot The slot that the armor piece is in
     * @return default armor properties for this set
     */
    private fun getDefaultProperties(slot: Int): ArmorProperties
    {
        return ArmorProperties(0, getRatio(slot), maxDamageBlocked)
    }

    companion object
    {
        // When this armor piece last proc'd absorption
        private const val NBT_LAST_ABSORPTION_PROC = "last_absorption_proc"
        // Number of milliseconds until the absoroption proc
        private const val ABSORPTION_PROC_CD_MILLIS = 120000 // 60000 millis aka 120s
        // Amount of absorption from each armor piece in hearts
        private const val ABSORPTION_PER_PIECE = 4
        // Damage sources that relate to unblockable damage
        private val TRUE_DAMAGE_SOURCES = setOf(DamageSource.DROWN, DamageSource.FALL, DamageSource.IN_WALL, DamageSource.OUT_OF_WORLD, DamageSource.STARVE, DamageSource.LAVA)
    }
}