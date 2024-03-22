package com.davidm1a2.afraidofthedark.common.event.custom

import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.entity.player.PlayerEvent

class ManualResearchTriggerEvent(player: Player, val research: Research) : PlayerEvent(player)