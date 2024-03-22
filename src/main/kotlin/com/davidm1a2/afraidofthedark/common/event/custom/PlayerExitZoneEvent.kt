package com.davidm1a2.afraidofthedark.common.event.custom

import net.minecraft.world.entity.player.Player
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.event.entity.player.PlayerEvent

class PlayerExitZoneEvent(player: Player, val tileEntity: TileEntityType<*>) : PlayerEvent(player)