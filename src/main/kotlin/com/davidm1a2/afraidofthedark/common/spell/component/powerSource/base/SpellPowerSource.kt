package com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base

import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponent
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation

/**
 * Entry used to store a reference to a power source
 *
 * @constructor just passes on the id and factory
 * @param id The ID of this power source
 */
abstract class SpellPowerSource(id: ResourceLocation) :
    SpellComponent<SpellPowerSource>(
        id,
        ResourceLocation(id.namespace, "textures/gui/spell_component/power_sources/${id.path}.png")
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
     * Computes the message describing why the power source doesn't have enough power
     *
     * @return A string describing why the power source doesn't have enough energy
     */
    abstract fun getUnlocalizedOutOfPowerMsg(): String

    /**
     * Gets a description message of how cost is computed for this power source
     *
     * @return A description describing how cost is computed
     */
    abstract fun getCostDescription(): String

    /**
     * @return Gets the unlocalized name of the component
     */
    override fun getUnlocalizedName(): String {
        return "power_source.${registryName!!.namespace}.${registryName!!.path}"
    }
}