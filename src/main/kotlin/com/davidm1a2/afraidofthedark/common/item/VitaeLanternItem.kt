package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.item.core.IHasModelProperties
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.IItemPropertyGetter
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World
import kotlin.math.floor

class VitaeLanternItem : AOTDItem("vitae_lantern", Properties().stacksTo(1)), IHasModelProperties {
    override fun getProperties(): List<Pair<ResourceLocation, IItemPropertyGetter>> {
        return listOf(ResourceLocation(Constants.MOD_ID, "vitae_step") to IItemPropertyGetter { itemStack, _, _ -> getChargeLevel(itemStack).ordinal.toFloat() })
    }

    fun addVitae(itemStack: ItemStack, vitae: Int): Int {
        if (vitae < 0) {
            throw IllegalStateException("Can't add negative vitae to the lantern")
        }

        val currentVitae = getVitae(itemStack)
        val maxVitae = getMaxVitae(itemStack)
        val newVitae = (currentVitae + vitae).coerceAtMost(maxVitae)
        NBTHelper.setInteger(itemStack, "vitae", newVitae)
        // Return overflow vitae
        return vitae - (newVitae - currentVitae)
    }

    fun removeVitae(itemStack: ItemStack, vitae: Int): Boolean {
        if (vitae < 0) {
            throw IllegalStateException("Can't remove negative vitae to the lantern")
        }

        val currentVitae = getVitae(itemStack)
        val newVitae = currentVitae - vitae
        if (newVitae < 0) {
            return false
        }
        NBTHelper.setInteger(itemStack, "vitae", newVitae)
        return true
    }

    fun getChargeLevel(itemStack: ItemStack): ChargeLevel {
        val percentFull = getVitae(itemStack) / getMaxVitae(itemStack).toFloat()
        return when {
            percentFull == 0f -> ChargeLevel.EMPTY
            percentFull < 0.33f -> ChargeLevel.QUARTER
            percentFull < 0.66f -> ChargeLevel.HALF
            percentFull < 1f -> ChargeLevel.THREE_QUARTERS
            else -> ChargeLevel.FULL
        }
    }

    fun getVitae(itemStack: ItemStack): Int {
        return NBTHelper.getInteger(itemStack, "vitae") ?: 0
    }

    private fun getMaxVitae(itemStack: ItemStack): Int {
        // TODO: Make this upgradable?
        return 300
    }

    override fun showDurabilityBar(itemStack: ItemStack?): Boolean {
        return true
    }

    override fun getDurabilityForDisplay(itemStack: ItemStack?): Double {
        return if (itemStack == null) {
            0.0
        } else {
            1.0 - getVitae(itemStack) / getMaxVitae(itemStack).toDouble()
        }
    }

    override fun fillItemCategory(group: ItemGroup, items: NonNullList<ItemStack>) {
        if (allowdedIn(group)) {
            items.add(ItemStack(this, 1))
            // Show a filled lantern too in creative
            items.add(ItemStack(this, 1).apply { addVitae(this, getMaxVitae(this)) })
        }
    }

    override fun appendHoverText(itemStack: ItemStack, world: World?, tooltip: MutableList<ITextComponent>, iTooltipFlag: ITooltipFlag) {
        val player = Minecraft.getInstance().player
        if (player != null && player.getResearch().isResearched(ModResearches.VITAE_LANTERN)) {
            val percentFull = floor(getVitae(itemStack) / getMaxVitae(itemStack).toFloat() * 100).toInt()
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.vitae_lantern.capacity", getVitae(itemStack), getMaxVitae(itemStack), percentFull))
        } else {
            tooltip.add(TranslationTextComponent(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
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