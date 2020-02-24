package com.davidm1a2.afraidofthedark.common.capabilities.player.dimension

import net.minecraft.util.math.BlockPos

/**
 * An interface that is a base for AOTD player void chest data capabilities. This specific capability is only used server side!
 * This means no packets are needed to update the player about state changes and no sync method is present
 *
 * @property positionalIndex The index corresponding to the player's void chest position in the void chest dimension
 * @property preTeleportPosition The position in world that the player was at before teleporting into the void chest
 * @property preTeleportDimensionID The ID of the dimension the player was in before teleporting to the void chest
 * @property friendsIndex The index of the friend's void chest position that the player was going to, or -1 if the player is going to their own index
 */
interface IAOTDPlayerVoidChestData : IAOTDIslandData {
    override var positionalIndex: Int
    var preTeleportPosition: BlockPos?
    var preTeleportDimensionID: Int
    var friendsIndex: Int
}