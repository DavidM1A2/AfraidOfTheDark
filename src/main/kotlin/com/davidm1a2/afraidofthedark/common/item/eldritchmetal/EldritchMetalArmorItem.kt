package com.davidm1a2.afraidofthedark.common.item.eldritchmetal

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

class EldritchMetalArmorItem(baseName: String, equipmentSlot: EquipmentSlotType) : AOTDArmorItem(baseName, ModArmorMaterials.ELDRITCH_METAL, equipmentSlot, Properties()), IEldritchItem {
    override fun inventoryTick(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        EldritchMetalCommons.processItem(itemStack, world, entity)
    }

    override fun getArmorTexture(stack: ItemStack, entity: Entity, slot: EquipmentSlotType, type: String?): String {
        return if (slot == EquipmentSlotType.LEGS) {
            "afraidofthedark:textures/armor/eldritch_metal_2.png"
        } else {
            "afraidofthedark:textures/armor/eldritch_metal_1.png"
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

        // We have 30% better protection than iron armor
        return 0.7
    }

    override fun isEnchantable(itemStack: ItemStack): Boolean {
        return true
    }
}