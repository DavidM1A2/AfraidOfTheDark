package com.davidm1a2.afraidofthedark.common.capabilities.player.dimension

import net.minecraft.util.RegistryKey
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * Default implementation of the AOTD player void chest capability
 *
 * @property positionalIndex Int telling us what index in the void chest dimension this player owns. Initialize to -1 so that we can test if the player has a position so far or not
 * @property friendsIndex Int telling us what index in the void chest dimension the player's friend is at. This will get set prior to teleportation by a void chest so that the player knows where to go. -1 means the player is going to their own index and not a friends
 * @property preTeleportPosition The position of the player before teleporting to the void chest
 * @property preTeleportDimension The dimension id of the player before teleporting to the void chest
 */
class PlayerVoidChestData : IPlayerVoidChestData {
    override var positionalIndex = -1
    override var friendsIndex = -1
    override var preTeleportPosition: BlockPos? = null
    override var preTeleportDimension: RegistryKey<World>? = null
}