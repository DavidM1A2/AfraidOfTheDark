package com.davidm1a2.afraidofthedark.common.item.amorphousmetal

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
import kotlin.random.Random

class AmorphousMetalArmorItem(baseName: String, equipmentSlot: EquipmentSlot) : AOTDArmorItem(baseName, ModArmorMaterials.AMORPHOUS_METAL, equipmentSlot, Properties()) {
    override fun getArmorTexture(stack: ItemStack, entity: Entity, slot: EquipmentSlot, type: String?): String {
        return if (slot == EquipmentSlot.LEGS) {
            "afraidofthedark:textures/armor/amorphous_metal_2.png"
        } else {
            "afraidofthedark:textures/armor/amorphous_metal_1.png"
        }
    }

    override fun appendHoverText(stack: ItemStack, world: Level?, tooltip: MutableList<Component>, flag: TooltipFlag) {
        val player = Minecraft.getInstance().player
        if (player != null) {
            if (player.getResearch().isResearched(ModResearches.CATALYSIS)) {
                tooltip.add(TranslatableComponent("tooltip.afraidofthedark.amorphous_metal_armor.full_protection"))
                tooltip.add(TranslatableComponent("tooltip.afraidofthedark.amorphous_metal_armor.no_protection"))
            } else {
                tooltip.add(TranslatableComponent(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
            }
        }
    }

    override fun getDamageMultiplier(entity: LivingEntity, armorStack: ItemStack, source: DamageSource, slot: EquipmentSlot): Double {
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

        val chance = Random.nextDouble()
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