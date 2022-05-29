package com.davidm1a2.afraidofthedark.common.event.custom

import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraftforge.event.entity.player.PlayerEvent

class PlayerChangePowerSourceEvent(player: PlayerEntity, val oldPowerSource: SpellPowerSource<*>, val newPowerSource: SpellPowerSource<*>) : PlayerEvent(player)
