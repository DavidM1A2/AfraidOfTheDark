package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.TranslationTextComponent

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

    /**
     * Gets a description message of how cost is computed for this power source
     *
     * @return A description describing how cost is computed
     */
    override fun getCostDescription(): ITextComponent {
        return StringTextComponent("Unlimited power!")
    }

    /**
     * Computes the message describing why the power source doesn't have enough power
     *
     * @return A string describing why the power source doesn't have enough energy
     */
    override fun getOutOfPowerMsg(): ITextComponent {
        return TranslationTextComponent("message.afraidofthedark.spell.power_source.creative.invalid_msg")
    }
}