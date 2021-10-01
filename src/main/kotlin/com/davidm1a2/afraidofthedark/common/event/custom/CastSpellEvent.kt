package com.davidm1a2.afraidofthedark.common.event.custom

import com.davidm1a2.afraidofthedark.common.spell.Spell
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraftforge.event.entity.EntityEvent
import net.minecraftforge.event.entity.player.PlayerEvent

class CastSpellEvent(entity: Entity, val spell: Spell) : EntityEvent(entity)