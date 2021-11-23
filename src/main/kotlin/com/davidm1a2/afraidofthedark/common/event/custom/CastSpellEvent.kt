package com.davidm1a2.afraidofthedark.common.event.custom

import com.davidm1a2.afraidofthedark.common.spell.Spell
import net.minecraft.entity.Entity
import net.minecraftforge.event.entity.EntityEvent

class CastSpellEvent(entity: Entity, val spell: Spell) : EntityEvent(entity)