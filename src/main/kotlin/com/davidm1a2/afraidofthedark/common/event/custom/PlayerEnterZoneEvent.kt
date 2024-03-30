package com.davidm1a2.afraidofthedark.common.event.custom

import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.event.entity.player.PlayerEvent

class PlayerEnterZoneEvent(player: Player, val tileEntity: BlockEntityType<*>) : PlayerEvent(player)