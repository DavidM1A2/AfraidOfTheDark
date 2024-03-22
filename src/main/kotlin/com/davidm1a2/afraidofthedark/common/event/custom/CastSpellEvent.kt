package com.davidm1a2.afraidofthedark.common.event.custom

import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource
import net.minecraft.world.entity.Entity
import net.minecraftforge.event.entity.EntityEvent

class CastSpellEvent(entity: Entity, val spell: Spell, val powerSourceUsed: SpellPowerSource<*>) : EntityEvent(entity)