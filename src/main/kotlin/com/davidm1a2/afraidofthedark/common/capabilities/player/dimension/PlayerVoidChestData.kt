package com.davidm1a2.afraidofthedark.common.capabilities.player.dimension

import net.minecraft.util.RegistryKey
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class PlayerVoidChestData : IPlayerVoidChestData {
    override var positionalIndex = -1
    override var friendsIndex = -1
    override var preTeleportPosition: BlockPos? = null
    override var preTeleportRespawnPosition: RespawnPosition? = null
    override var preTeleportDimension: RegistryKey<World>? = null
}