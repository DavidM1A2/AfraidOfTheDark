package com.davidm1a2.afraidofthedark.common.item.crossbow

import com.davidm1a2.afraidofthedark.client.keybindings.ModKeybindings
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDPerItemCooldownItem
import com.davidm1a2.afraidofthedark.common.item.core.IHasModelProperties
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.LivingEntity
import net.minecraft.item.IItemPropertyGetter
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

/**
 * Class representing a wrist-mounted crossbow
 *
 * @constructor sets up item properties
 */
class WristCrossbowItem : AOTDPerItemCooldownItem("wrist_crossbow", Properties()), IHasModelProperties {
    override fun getProperties(): List<Pair<ResourceLocation, IItemPropertyGetter>> {
        return listOf(
            ResourceLocation(Constants.MOD_ID, "is_loaded") to IItemPropertyGetter { stack: ItemStack, _: World?, _: LivingEntity? ->
                if (isOnCooldown(stack)) 0f else 1f
            }
        )
    }

    /**
     * Adds tooltip text to the item
     *
     * @param stack   The itemstack to add text to
     * @param world The world that this item is in
     * @param tooltip The tooltip to add to
     * @param flag  The flag telling us if advanced tooltips are on or off
     */
    @OnlyIn(Dist.CLIENT)
    override fun appendHoverText(stack: ItemStack, world: World?, tooltip: MutableList<ITextComponent>, flag: ITooltipFlag) {
        val player = Minecraft.getInstance().player
        if (player != null && player.getResearch().isResearched(ModResearches.WRIST_CROSSBOW)) {
            tooltip.add(
                TranslationTextComponent(
                    "tooltip.afraidofthedark.wrist_crossbow.how_to_fire",
                    ModKeybindings.FIRE_WRIST_CROSSBOW.translatedKeyMessage
                )
            )
            tooltip.add(
                TranslationTextComponent(
                    "tooltip.afraidofthedark.wrist_crossbow.change_bolt_type",
                    ModKeybindings.FIRE_WRIST_CROSSBOW.translatedKeyMessage
                )
            )
        } else {
            tooltip.add(TranslationTextComponent(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
        }
    }

    /**
     * Returns the number of milliseconds required for this item to get off cooldown
     *
     * @param itemStack The itemstack to get the cooldown for
     * @return The number of milliseconds required to finish the cooldown
     */
    override fun getCooldownInMilliseconds(itemStack: ItemStack): Int {
        return 3000
    }
}