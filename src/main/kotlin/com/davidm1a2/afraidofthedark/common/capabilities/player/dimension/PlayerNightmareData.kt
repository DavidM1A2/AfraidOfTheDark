package com.davidm1a2.afraidofthedark.common.capabilities.player.dimension

import net.minecraft.nbt.ListNBT
import net.minecraft.util.math.BlockPos
import net.minecraft.world.dimension.DimensionType

/**
 * Default implementation of the AOTD player nightmare capability
 *
 * @property positionalIndex Int telling us what index in the void chest dimension this player owns. Initialize to -1 so that we can test if the player has a position so far or not
 * @property preTeleportPlayerInventory NBTTagList containing all player inventory data before teleporting to the nightmare
 * @property preTeleportPosition The position of the player before teleporting to the nightmare
 * @property preTeleportDimension The dimension id of the player before teleporting to the nightmare
 */
class PlayerNightmareData : IPlayerNightmareData {
    override var positionalIndex = -1
    override var preTeleportPlayerInventory: ListNBT? = null
    override var preTeleportPosition: BlockPos? = null
    override var preTeleportDimension: DimensionType? = null
}