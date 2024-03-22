package com.davidm1a2.afraidofthedark.common.item.amorphousmetal

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

class AmorphousMetalArmorItem(baseName: String, equipmentSlot: EquipmentSlotType) : AOTDArmorItem(baseName, ModArmorMaterials.AMORPHOUS_METAL, equipmentSlot, Properties()) {
    override fun getArmorTexture(stack: ItemStack, entity: Entity, slot: EquipmentSlotType, type: String?): String {
        return if (slot == EquipmentSlotType.LEGS) {
            "afraidofthedark:textures/armor/amorphous_metal_2.png"
        } else {
            "afraidofthedark:textures/armor/amorphous_metal_1.png"
        }
    }

    override fun appendHoverText(stack: ItemStack, world: Level?, tooltip: MutableList<ITextComponent>, flag: ITooltipFlag) {
        val player = Minecraft.getInstance().player
        if (player != null) {
            if (player.getResearch().isResearched(ModResearches.CATALYSIS)) {
                tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.amorphous_metal_armor.full_protection"))
                tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.amorphous_metal_armor.no_protection"))
            } else {
                tooltip.add(TranslationTextComponent(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
            }
        }
    }

    override fun getDamageMultiplier(entity: LivingEntity, armorStack: ItemStack, source: DamageSource, slot: EquipmentSlotType): Double {
        // Compute armor properties for players only
        if (entity !is Player) {
            return 1.0
        }

        // Ensure the player has the right research
        if (!entity.getResearch().isResearched(ModResearches.CATALYSIS)) {
            return 1.0
        }

        // Blocks no true damage
        if (TRUE_DAMAGE_SOURCES.contains(source)) {
            return 1.0
        }

        val chance = random.nextDouble()
        // Chance to fully protect
        if (chance < FULL_PROTECTION_CHANCE) {
            return 0.0
        }
        // Chance to take full damage
        if (1.0 - NO_PROTECTION_CHANCE < chance) {
            return 1.0
        }

        // We have 75% better protection than diamond armor
        return 0.25
    }

    override fun isEnchantable(itemStack: ItemStack): Boolean {
        return true
    }

    companion object {
        private const val FULL_PROTECTION_CHANCE = 0.2
        private const val NO_PROTECTION_CHANCE = 0.05
    }
}