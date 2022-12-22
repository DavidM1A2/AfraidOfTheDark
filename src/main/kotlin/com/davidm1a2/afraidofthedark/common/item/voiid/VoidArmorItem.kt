package com.davidm1a2.afraidofthedark.common.item.voiid

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModArmorMaterials
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDArmorItem
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World

class VoidArmorItem(baseName: String, equipmentSlot: EquipmentSlotType) : AOTDArmorItem(baseName, ModArmorMaterials.VOID, equipmentSlot, Properties()) {
    override fun getArmorTexture(stack: ItemStack, entity: Entity, slot: EquipmentSlotType, type: String?): String {
        return if (slot == EquipmentSlotType.LEGS) {
            "afraidofthedark:textures/armor/void_2.png"
        } else {
            "afraidofthedark:textures/armor/void_1.png"
        }
    }

    override fun appendHoverText(stack: ItemStack, world: World?, tooltip: MutableList<ITextComponent>, flag: ITooltipFlag) {
        val player = Minecraft.getInstance().player
        if (player != null && !player.getResearch().isResearched(ModResearches.AN_UNSETTLING_MATERIAL)) {
            tooltip.add(TranslationTextComponent(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
        }
    }

    override fun getDamageMultiplier(entity: LivingEntity, armorStack: ItemStack, source: DamageSource, slot: EquipmentSlotType): Double {
        // Compute armor properties for players only
        if (entity !is PlayerEntity) {
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