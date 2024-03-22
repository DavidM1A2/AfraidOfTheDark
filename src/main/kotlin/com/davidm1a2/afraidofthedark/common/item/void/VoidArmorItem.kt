package com.davidm1a2.afraidofthedark.common.item.void

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

class VoidArmorItem(baseName: String, equipmentSlot: EquipmentSlot) : AOTDArmorItem(baseName, ModArmorMaterials.VOID, equipmentSlot, Properties()) {
    override fun inventoryTick(itemStack: ItemStack, world: Level, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        VoidCommons.processItem(itemStack, world)
    }

    override fun getArmorTexture(stack: ItemStack, entity: Entity, slot: EquipmentSlot, type: String?): String {
        return if (slot == EquipmentSlot.LEGS) {
            "afraidofthedark:textures/armor/void_2.png"
        } else {
            "afraidofthedark:textures/armor/void_1.png"
        }
    }

    override fun appendHoverText(
        stack: ItemStack,
        world: Level?,
        tooltip: MutableList<Component>,
        flag: TooltipFlag
    ) {
        val player = Minecraft.getInstance().player
        if (player != null) {
            if (!player.getResearch().isResearched(ModResearches.AN_UNSETTLING_MATERIAL)) {
                tooltip.add(TranslatableComponent(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
            } else {
                tooltip.add(TranslatableComponent("tooltip.afraidofthedark.void_tool.autorepair"))
            }
        }
    }

    override fun getDamageMultiplier(entity: LivingEntity, armorStack: ItemStack, source: DamageSource, slot: EquipmentSlot): Double {
        // Compute armor properties for players only
        if (entity !is Player) {
            return 1.0
        }

        // Ensure the player has the right research
        if (!entity.getResearch().isResearched(ModResearches.AN_UNSETTLING_MATERIAL)) {
            return 5.0
        }

        // Blocks no true damage
        if (TRUE_DAMAGE_SOURCES.contains(source)) {
            return 1.0
        }

        // We have 99% better protection than diamond armor
        return 0.01
    }

    override fun isEnchantable(itemStack: ItemStack): Boolean {
        return true
    }
}