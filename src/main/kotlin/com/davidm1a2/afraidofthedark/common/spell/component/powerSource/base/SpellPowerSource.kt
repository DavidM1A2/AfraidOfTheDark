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
     * True if the given spell can be cast, false otherwise
     *
     * @param entity The entity that is casting the spell
     * @param spell The spell to attempt to cast
     * @return True if the spell can be cast, false otherwise
     */
    abstract fun canCast(entity: Entity, spell: Spell): Boolean

    /**
     * Consumes power to cast a given spell. canCast must return true first to ensure there is
     * enough power to cast the spell
     *
     * @param entity The entity that is casting the spell
     * @param spell the spell to attempt to cast
     */
    abstract fun consumePowerToCast(entity: Entity, spell: Spell)

    /**
     * Converts the spell cost to "power source" specific units
     *
     * @param rawCost The spell's cost
     * @return The cost in "power source" specific units
     */
    protected abstract fun getSourceSpecificCost(rawCost: Double): Number

    /**
     * Computes the message describing why the power source doesn't have enough power
     *
     * @return A string describing why the power source doesn't have enough energy
     */
    fun getNotEnoughPowerMessage(): ITextComponent {
        return TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power")
    }

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