package com.davidm1a2.afraidofthedark.common.capabilities.player.dimension

import net.minecraft.nbt.ListNBT
import net.minecraft.util.RegistryKey
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class PlayerNightmareData : IPlayerNightmareData {
    override var positionalIndex = -1
    override var preTeleportPlayerInventory: ListNBT? = null
    override var preTeleportPosition: BlockPos? = null
    override var preTeleportRespawnPosition: RespawnPosition? = null
    override var preTeleportDimension: RegistryKey<World>? = null
}