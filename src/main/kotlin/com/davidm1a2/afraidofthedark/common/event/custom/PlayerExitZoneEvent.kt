package com.davidm1a2.afraidofthedark.common.event.custom

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.entity.player.PlayerEvent

class PlayerExitZoneEvent(player: PlayerEntity, val tileEntity: ResourceLocation) : PlayerEvent(player)