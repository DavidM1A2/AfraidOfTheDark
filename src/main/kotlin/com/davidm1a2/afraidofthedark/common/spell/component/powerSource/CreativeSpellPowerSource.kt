package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ResourceLocation

/**
 * Class representing the creative power source
 */
class CreativeSpellPowerSource : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "creative")) {
    /**
     * True if the given spell can be cast, false otherwise
     *
     * @param entity The entity that is casting the spell
     * @param spell The spell to attempt to cast
     * @return True if the spell can be cast, false otherwise
     */
    override fun canCast(entity: Entity, spell: Spell): Boolean {
        return (entity as? PlayerEntity)?.isCreative ?: true
    }

    /**
     * Does nothing, creative power sources don't use energy
     *
     * @param entity The entity that is casting the spell
     * @param spell the spell to attempt to cast
     */
    override fun consumePowerToCast(entity: Entity, spell: Spell) {
    }
}