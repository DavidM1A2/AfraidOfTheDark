package com.davidm1a2.afraidofthedark.common.event.custom

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.biome.Biome
import net.minecraftforge.event.entity.player.PlayerEvent

class PlayerInBiomeEvent(player: PlayerEntity, val biome: Biome) : PlayerEvent(player)