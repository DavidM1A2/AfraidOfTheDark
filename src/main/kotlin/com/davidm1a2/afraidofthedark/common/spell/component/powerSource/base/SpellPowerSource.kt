package com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base

import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponent
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent

/**
 * Entry used to store a reference to a power source
 *
 * @constructor just passes on the id and factory
 * @param id The ID of this power source
 * @param prerequisiteResearch The research required to use this component, or null if none is required
 */
abstract class SpellPowerSource(id: ResourceLocation, prerequisiteResearch: Research?) : SpellComponent<SpellPowerSource>(
    id,
    ResourceLocation(id.namespace, "textures/gui/spell_component/power_sources/${id.path}.png"),
    prerequisiteResearch
) {
    /**
     * Tries to case a given spell from an entity using this power source
     *
     * @param entity The entity to case from
     * @param spell The spell to cast
     * @return The result of the cast
     */
    abstract fun cast(entity: Entity, spell: Spell): SpellCastResult

    /**
     * Converts the spell cost to "power source" specific units
     *
     * @param rawCost The spell's cost
     * @return The cost in "power source" specific units
     */
    protected abstract fun getSourceSpecificCost(rawCost: Double): Number

    /**
     * Gets a description message of how cost is computed for this power source
     *
     * @return A description describing how cost is computed
     */
    fun getCostOverview(): ITextComponent {
        return TranslationTextComponent("${getUnlocalizedBaseName()}.cost_overview")
    }

    /**
     * Converts the raw cost double to a formatted
     *
     * @param rawCost The raw spell cost
     * @return A description of the cost as a nicely formatted string
     */
    fun getFormattedCost(rawCost: Double): ITextComponent {
        return TranslationTextComponent("${getUnlocalizedBaseName()}.formatted_cost", getSourceSpecificCost(rawCost))
    }

    /**
     * @return Gets the unlocalized name of the component
     */
    override fun getUnlocalizedBaseName(): String {
        return "power_source.${registryName!!.namespace}.${registryName!!.path}"
    }
}