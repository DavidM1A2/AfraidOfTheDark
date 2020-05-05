package com.davidm1a2.afraidofthedark.common.item.crossbow

import com.davidm1a2.afraidofthedark.client.keybindings.ModKeybindings.FIRE_WRIST_CROSSBOW
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItemWithPerItemCooldown
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * Class representing a wrist-mounted crossbow
 *
 * @constructor sets up item properties
 */
class ItemWristCrossbow : AOTDItemWithPerItemCooldown("wrist_crossbow") {
    init {
        addPropertyOverride(ResourceLocation(Constants.MOD_ID, "is_loaded"))
        { stack: ItemStack, _: World?, _: EntityLivingBase? ->
            if (isOnCooldown(stack)) 0f else 1f
        }
    }

    /**
     * Adds tooltip text to the item
     *
     * @param stack   The itemstack to add text to
     * @param world The world that this item is in
     * @param tooltip The tooltip to add to
     * @param flag  The flag telling us if advanced tooltips are on or off
     */
    @SideOnly(Side.CLIENT)
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        val player = Minecraft.getMinecraft().player
        if (player != null && player.getResearch().isResearched(ModResearches.WRIST_CROSSBOW)) {
            tooltip.add("Use ${FIRE_WRIST_CROSSBOW.displayName} to fire a bolt in the current look direction.")
            tooltip.add("Crouch & ${FIRE_WRIST_CROSSBOW.displayName} to change bolt type.")
        } else {
            tooltip.add("I'm not sure how to use this.")
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