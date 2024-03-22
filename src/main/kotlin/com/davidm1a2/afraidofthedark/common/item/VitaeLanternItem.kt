package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.item.core.IHasModelProperties
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction
import net.minecraft.core.NonNullList
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import kotlin.math.floor
import kotlin.math.roundToInt

class VitaeLanternItem : AOTDItem("vitae_lantern", Properties().stacksTo(1)), IHasModelProperties {
    override fun getProperties(): List<Pair<ResourceLocation, ClampedItemPropertyFunction>> {
        return listOf(ResourceLocation(Constants.MOD_ID, "vitae_step") to ClampedItemPropertyFunction { itemStack, _, _, _ -> getChargeLevel(itemStack).ordinal.toFloat() })
    }

    fun addVitae(itemStack: ItemStack, vitae: Float): Float {
        if (vitae < 0) {
            throw IllegalStateException("Can't add negative vitae to the lantern")
        }

        val currentVitae = getVitae(itemStack)
        val maxVitae = getMaxVitae(itemStack)
        val newVitae = (currentVitae + vitae).coerceAtMost(maxVitae)
        NBTHelper.setFloat(itemStack, "vitae", newVitae)
        // Return overflow vitae
        return vitae - (newVitae - currentVitae)
    }

    fun removeVitae(itemStack: ItemStack, vitae: Float): Boolean {
        if (vitae < 0) {
            throw IllegalStateException("Can't remove negative vitae to the lantern")
        }

        val currentVitae = getVitae(itemStack)
        val newVitae = currentVitae - vitae
        if (newVitae < 0) {
            return false
        }
        NBTHelper.setFloat(itemStack, "vitae", newVitae)
        return true
    }

    fun getChargeLevel(itemStack: ItemStack): ChargeLevel {
        val percentFull = getVitae(itemStack) / getMaxVitae(itemStack)
        return when {
            percentFull == 0f -> ChargeLevel.EMPTY
            percentFull < 0.33f -> ChargeLevel.QUARTER
            percentFull < 0.66f -> ChargeLevel.HALF
            percentFull < 1f -> ChargeLevel.THREE_QUARTERS
            else -> ChargeLevel.FULL
        }
    }

    fun getVitae(itemStack: ItemStack): Float {
        return NBTHelper.getFloat(itemStack, "vitae") ?: 0f
    }

    fun getMaxVitae(itemStack: ItemStack): Float {
        return 1000f
    }

    override fun isBarVisible(itemStack: ItemStack): Boolean {
        return true
    }

    override fun getBarWidth(itemStack: ItemStack): Int {
        return (13*(1.0 - getVitae(itemStack) / getMaxVitae(itemStack).toDouble())).roundToInt()
    }

    override fun fillItemCategory(group: CreativeModeTab, items: NonNullList<ItemStack>) {
        if (allowdedIn(group)) {
            items.add(ItemStack(this, 1))
            // Show a filled lantern too in creative
            items.add(ItemStack(this, 1).apply { addVitae(this, getMaxVitae(this)) })
        }
    }

    override fun appendHoverText(itemStack: ItemStack, world: Level?, tooltip: MutableList<Component>, iTooltipFlag: TooltipFlag) {
        val player = Minecraft.getInstance().player
        if (player != null && player.getResearch().isResearched(ModResearches.VITAE_LANTERN)) {
            val percentFull = floor(getVitae(itemStack) / getMaxVitae(itemStack) * 100).toInt()
            tooltip.add(TranslatableComponent("tooltip.afraidofthedark.vitae_lantern.capacity", getVitae(itemStack).toInt(), getMaxVitae(itemStack).toInt(), percentFull))
        } else {
            tooltip.add(TranslatableComponent(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
        }
    }

    enum class ChargeLevel {
        EMPTY,
        QUARTER,
        HALF,
        THREE_QUARTERS,
        FULL;
    }
}