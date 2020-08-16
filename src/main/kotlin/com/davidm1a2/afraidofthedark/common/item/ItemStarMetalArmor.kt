package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModArmorMaterials
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItemArmor
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * Class representing the 4 different pieces of star metal armor
 *
 * @constructor sets up armor item properties
 * @param baseName        The name of the item to be used by the game registry
 * @param equipmentSlot The slot that this armor pieces goes on, can be one of 4 options
 */
class ItemStarMetalArmor(baseName: String, equipmentSlot: EntityEquipmentSlot) :
    AOTDItemArmor(baseName, ModArmorMaterials.STAR_METAL, equipmentSlot, Properties().defaultMaxDamage(0)) {
    private val percentOfDamageBlocked = 0.85f

    /**
     * Gets the resource location path of the texture for the armor when worn by the player
     *
     * @param stack  The armor itemstack
     * @param entity The entity that is wearing the  armor
     * @param slot   The slot the armor is in
     * @param type   The subtype, can be null or "overlay"
     * @return Path of texture to bind, or null to use default
     */
    override fun getArmorTexture(stack: ItemStack, entity: Entity, slot: EntityEquipmentSlot, type: String?): String? {
        // Star metal 1 is for helm, boots, and chest while Star metal 2 is for leggings
        return if (slot == EntityEquipmentSlot.LEGS) {
            "afraidofthedark:textures/armor/star_metal_2.png"
        } else {
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
    @OnlyIn(Dist.CLIENT)
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<ITextComponent>, flag: ITooltipFlag) {
        val player = Minecraft.getInstance().player
        if (player != null && player.getResearch().isResearched(ModResearches.STAR_METAL)) {
            tooltip.add(TextComponentTranslation(LocalizationConstants.TOOLTIP_MAGIC_ARMOR_NEVER_BREAKS))
            tooltip.add(TextComponentTranslation("tooltip.afraidofthedark.star_metal_armor.effect"))
        } else {
            tooltip.add(TextComponentTranslation(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
        }
    }

    /**
     * Called every tick the armor is being worn
     *
     * @param itemStack The itemstack of the armor item
     * @param world     The world the player is in
     * @param player    The player that is wearing the armor
     */
    override fun onArmorTick(itemStack: ItemStack, world: World, player: EntityPlayer) {
        // Dead players don't have capabilities
        if (player.isAlive) {
            // We have to test client and server side since absorption is client side :(
            // Test if the player has the star metal research
            if (player.getResearch().isResearched(ModResearches.STAR_METAL)) {
                // If the stack is ready to proc absorption, add 2 absorption hearts
                if (readyToProcAbsorption(itemStack)) {
                    // The number of star metal armor pieces worn
                    val numberStarMetalPiecesWorn =
                        player.inventory.armorInventory.map { it.item }.filterIsInstance<ItemStarMetalArmor>().count()
                    if (numberStarMetalPiecesWorn * ABSORPTION_PER_PIECE >= player.absorptionAmount) {
                        // Add 4 to absorption up to a max for the proc
                        player.absorptionAmount = (player.absorptionAmount + ABSORPTION_PER_PIECE).coerceIn(
                            0f,
                            numberStarMetalPiecesWorn * ABSORPTION_PER_PIECE.toFloat()
                        )
                    }

                    // Update the last proc time to now
                    NBTHelper.setLong(itemStack, NBT_LAST_ABSORPTION_PROC, System.currentTimeMillis())
                }
            }
        }
    }

    /**
     * True if the armor is ready to proc absorption, false otherwise
     *
     * @param itemStack The itemstack to test
     * @return True if absorption can be proc'd, false otherwise
     */
    private fun readyToProcAbsorption(itemStack: ItemStack): Boolean {
        return !NBTHelper.hasTag(itemStack, NBT_LAST_ABSORPTION_PROC) ||
                System.currentTimeMillis() > NBTHelper.getLong(itemStack, NBT_LAST_ABSORPTION_PROC)!! + ABSORPTION_PROC_CD_MILLIS
    }

    override fun processDamage(entity: EntityLivingBase, armorStack: ItemStack, source: DamageSource, amount: Float, slot: EntityEquipmentSlot): Double {
        // Compute armor properties for players only
        if (entity is EntityPlayer) {
            // Ensure the player has the right research
            if (entity.getResearch().isResearched(ModResearches.STAR_METAL)) {
                // No damage reduction against true sources
                if (TRUE_DAMAGE_SOURCES.contains(source)) {
                    return 0.0
                }
            } else {
                // Armor is useless without research
                return 0.0
            }
        } else {
            // Armor is useless without research
            return 0.0
        }

        // Default armor protection if no special set bonus applies
        return getRatio(slot) * percentOfDamageBlocked
    }

    /**
     * Returns the ratio of protection each pieces gives
     *
     * @param slot The slot the armor is in
     * @return The ratio of protection of each piece reduced by the percent damage blocked
     */
    private fun getRatio(slot: EntityEquipmentSlot): Double {
        // Total protection of each piece
        val totalProtection = 3 + 6 + 8 + 3
        return when (slot) {
            EntityEquipmentSlot.HEAD, EntityEquipmentSlot.FEET -> 3.0 / totalProtection * percentOfDamageBlocked
            EntityEquipmentSlot.LEGS -> 6.0 / totalProtection * percentOfDamageBlocked
            EntityEquipmentSlot.CHEST -> 8.0 / totalProtection * percentOfDamageBlocked
            else -> 0.0
        }
    }

    companion object {
        // When this armor piece last proc'd absorption
        private const val NBT_LAST_ABSORPTION_PROC = "last_absorption_proc"

        // Number of milliseconds until the absoroption proc
        private const val ABSORPTION_PROC_CD_MILLIS = 120000 // 60000 millis aka 120s

        // Amount of absorption from each armor piece in hearts
        private const val ABSORPTION_PER_PIECE = 4

        // Damage sources that relate to unblockable damage
        private val TRUE_DAMAGE_SOURCES = setOf(
            DamageSource.DROWN,
            DamageSource.FALL,
            DamageSource.IN_WALL,
            DamageSource.OUT_OF_WORLD,
            DamageSource.STARVE,
            DamageSource.LAVA
        )
    }
}