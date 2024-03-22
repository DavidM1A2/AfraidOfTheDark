package com.davidm1a2.afraidofthedark.common.spell.component

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.world.entity.player.Player
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.registries.ForgeRegistryEntry
import net.minecraftforge.registries.IForgeRegistryEntry

/**
 * Class representing a base for spell delivery methods, power sources, and effects to be registered
 * in the forge registry
 *
 * @constructor sets the entry id, icon, and factory
 * @param <T> The type that will be registered
 * @param <V> The type that this entry will create
 * @param id The ID of the entry to register
 * @property icon A resource location containing an image file with the icon to be used by the component
 * @property prerequisiteResearch The research required to use this component, or null if none is required
 */
abstract class SpellComponentBase<T : IForgeRegistryEntry<T>>(
    id: ResourceLocation,
    val icon: ResourceLocation,
    val prerequisiteResearch: Research?
) : ForgeRegistryEntry<T>() {
    init {
        registryName = id
    }

    abstract fun getUnlocalizedBaseName(): String

    open fun shouldShowInSpellEditor(player: Player): Boolean {
        return prerequisiteResearch == null || player.getResearch().isResearched(prerequisiteResearch)
    }

    fun getName(): ITextComponent {
        return TranslationTextComponent("${getUnlocalizedBaseName()}.name")
    }

    fun getDescription(): ITextComponent {
        return TranslationTextComponent("${getUnlocalizedBaseName()}.description")
    }
}