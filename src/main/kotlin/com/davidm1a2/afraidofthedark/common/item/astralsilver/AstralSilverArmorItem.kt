package com.davidm1a2.afraidofthedark.common.item.astralsilver

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModArmorMaterials
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDArmorItem
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class AstralSilverArmorItem(baseName: String, equipmentSlot: EquipmentSlot) :
    AOTDArmorItem(baseName, ModArmorMaterials.ASTRAL_SILVER, equipmentSlot, Properties()) {
    override fun getArmorTexture(stack: ItemStack, entity: Entity, slot: EquipmentSlot, type: String?): String {
        // Star metal 1 is for helm, boots, and chest while star metal 2 is for leggings
        return if (slot == EquipmentSlot.LEGS) {
            "afraidofthedark:textures/armor/astral_silver_2.png"
        } else {
            "afraidofthedark:textures/armor/astral_silver_1.png"
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
    override fun appendHoverText(stack: ItemStack, world: Level?, tooltip: MutableList<Component>, flag: TooltipFlag) {
        val player = Minecraft.getInstance().player
        if (player != null && !player.getResearch().isResearched(ModResearches.SILVER_SLAYER)) {
            tooltip.add(TranslatableComponent(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
        }
    }

    override fun getDamageMultiplier(entity: LivingEntity, armorStack: ItemStack, source: DamageSource, slot: EquipmentSlot): Double {
        // Compute armor properties for players only
        if (entity !is Player) {
            return 1.0
        }

        // Ensure the player has the right research
        if (!entity.getResearch().isResearched(ModResearches.SILVER_SLAYER)) {
            return 1.0
        }

        // Blocks no true damage
        if (TRUE_DAMAGE_SOURCES.contains(source)) {
            return 1.0
        }

        // We have 15% better protection than iron armor
        return 0.85
    }

    override fun isEnchantable(itemStack: ItemStack): Boolean {
        return true
    }
}