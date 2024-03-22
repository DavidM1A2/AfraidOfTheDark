package com.davidm1a2.afraidofthedark.common.event.custom

import net.minecraft.world.entity.player.Player
import net.minecraft.world.biome.Biome
import net.minecraftforge.event.entity.player.PlayerEvent

class PlayerInBiomeEvent(player: Player, val biome: Biome) : PlayerEvent(player)