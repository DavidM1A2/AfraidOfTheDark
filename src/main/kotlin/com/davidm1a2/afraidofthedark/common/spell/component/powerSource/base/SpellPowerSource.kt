package com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base

import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentBase
import net.minecraft.world.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslatableComponent

/**
 * Entry used to store a reference to a power source
 *
 * @constructor just passes on the id and factory
 * @param id The ID of this power source
 * @param prerequisiteResearch The research required to use this component, or null if none is required
 */
abstract class SpellPowerSource<T>(id: ResourceLocation, prerequisiteResearch: Research?) : SpellComponentBase<SpellPowerSource<*>>(
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
    fun cast(entity: Entity, spell: Spell): SpellCastResult {
        return cast(entity, spell, computeCastEnvironment(entity))
    }

    /**
     * Tries to case a given spell from an entity using this power source
     *
     * @param entity The entity to case from
     * @param spell The spell to cast
     * @param environment The environment that the spell is being cast in
     * @return The result of the cast
     */
    abstract fun cast(entity: Entity, spell: Spell, environment: CastEnvironment<T>): SpellCastResult

    /**
     * Computes the cast environment for the given entity. Note: This call may be expensive
     *
     * @param entity The entity to compute the environment for
     * @return The cast environment, containing info like current/max vitae and optional metadata
     */
    abstract fun computeCastEnvironment(entity: Entity): CastEnvironment<T>

    /**
     * Converts the spell cost to "power source" specific units
     *
     * @param vitae The spell's raw cost in base vitae units
     * @return The cost in "power source" specific units
     */
    protected abstract fun getSourceSpecificCost(vitae: Double): Number

    /**
     * Gets a description message of how cost is computed for this power source
     *
     * @return A description describing how cost is computed
     */
    fun getCostOverview(): ITextComponent {
        return TranslatableComponent("${getUnlocalizedBaseName()}.cost_overview")
    }

    /**
     * Converts the raw cost double to a formatted
     *
     * @param rawCost The raw spell cost
     * @return A description of the cost as a nicely formatted string
     */
    fun getFormattedCost(rawCost: Double): ITextComponent {
        return TranslatableComponent("${getUnlocalizedBaseName()}.formatted_cost", getSourceSpecificCost(rawCost))
    }

    /**
     * @return Gets the unlocalized name of the component
     */
    override fun getUnlocalizedBaseName(): String {
        return "power_source.${registryName!!.namespace}.${registryName!!.path}"
    }
}