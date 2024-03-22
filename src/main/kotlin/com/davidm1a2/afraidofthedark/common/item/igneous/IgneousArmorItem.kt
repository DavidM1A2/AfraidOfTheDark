package com.davidm1a2.afraidofthedark.common.item.igneous

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModArmorMaterials
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDArmorItem
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.level.Level
import kotlin.math.max

/**
 * Class representing the 4 different pieces of igneous armor
 *
 * @constructor sets up armor item properties
 * @param baseName        The name of the item to be used by the game registry
 * @param equipmentSlot The slot that this armor pieces goes on, can be one of 4 options
 */
class IgneousArmorItem(baseName: String, equipmentSlot: EquipmentSlotType) :
    AOTDArmorItem(baseName, ModArmorMaterials.IGNEOUS, equipmentSlot, Properties()) {
    /**
     * Gets the resource location path of the texture for the armor when worn by the player
     *
     * @param stack  The armor itemstack
     * @param entity The entity that is wearing the  armor
     * @param slot   The slot the armor is in
     * @param type   The subtype, can be null or "overlay"
     * @return Path of texture to bind, or null to use default
     */
    override fun getArmorTexture(stack: ItemStack, entity: Entity, slot: EquipmentSlotType, type: String?): String {
        // Igneous 1 is for helm, boots, and chest while igneous 2 is for leggings
        return if (slot == EquipmentSlotType.LEGS) {
            "afraidofthedark:textures/armor/igneous_2.png"
        } else {
            "afraidofthedark:textures/armor/igneous_1.png"
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
    override fun appendHoverText(stack: ItemStack, world: Level?, tooltip: MutableList<ITextComponent>, flag: ITooltipFlag) {
        val player = Minecraft.getInstance().player
        if (player != null && player.getResearch().isResearched(ModResearches.IGNEOUS)) {
            tooltip.add(TranslationTextComponent(LocalizationConstants.TOOLTIP_MAGIC_ARMOR_NEVER_BREAKS))
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.igneous_armor.effect"))
        } else {
            tooltip.add(TranslationTextComponent(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
        }
    }

    /**
     * Called every tick the armor is being worn
     *
     * @param stack The itemstack of the armor item
     * @param player    The player that is wearing the armor
     * @param world     The world the player is in
     */
    override fun onArmorTick(stack: ItemStack, world: Level, player: Player) {
        // Dead players don't have capabilities
        if (player.isAlive) {
            // If the armor wearer is burning extinguish the fire
            if (player.isOnFire) {
                // Ensure the player has the right research
                if (player.getResearch().isResearched(ModResearches.IGNEOUS)) {
                    // If the player is wearing full armor then add armor set bonuses
                    if (isWearingFullArmor(player)) {
                        player.clearFire()
                    }
                }
            }
        }
    }

    override fun getDamageMultiplier(entity: LivingEntity, armorStack: ItemStack, source: DamageSource, slot: EquipmentSlotType): Double {
        // Compute armor properties for players only
        if (entity !is Player) {
            return 1.0
        }

        // Ensure the player has the right research
        if (!entity.getResearch().isResearched(ModResearches.IGNEOUS)) {
            return 1.0
        }

        // Blocks no true damage
        if (TRUE_DAMAGE_SOURCES.contains(source)) {
            return 1.0
        }

        // Blocks all fire damage
        if (FIRE_SOURCES.contains(source)) {
            return 0.0
        }

        // Block 80% of all other damage
        return 0.2
    }

    override fun canBeDepleted(): Boolean {
        return false
    }

    override fun isEnchantable(itemStack: ItemStack): Boolean {
        return true
    }

    companion object {
        // Damage sources that relate to fire damage
        private val FIRE_SOURCES = setOf(DamageSource.IN_FIRE, DamageSource.ON_FIRE, DamageSource.LAVA)
    }
}