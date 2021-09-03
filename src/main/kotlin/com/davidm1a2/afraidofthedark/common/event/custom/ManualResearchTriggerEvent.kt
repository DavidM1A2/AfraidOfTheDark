package com.davidm1a2.afraidofthedark.common.event.custom

import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.entity.player.PlayerEntity
import net.minecraftforge.event.entity.player.PlayerEvent

class ManualResearchTriggerEvent(player: PlayerEntity, val research: Research) : PlayerEvent(player)