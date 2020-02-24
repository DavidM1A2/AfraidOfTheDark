package com.davidm1a2.afraidofthedark.common.capabilities.player.dimension

import net.minecraft.nbt.NBTTagList
import net.minecraft.util.math.BlockPos

/**
 * An interface that is a base for AOTD player nightmare data capabilities. This specific capability is only used server side!
 * This means no packets are needed to update the player about state changes and no sync method is present
 *
 * @property positionalIndex the index corresponding to the player's island position\
 * @property preTeleportPosition The position in world that the player was at before teleporting into the nightmare
 * @property preTeleportDimensionID The ID of the dimension the player was in before teleporting to the nightmare
 * @property preTeleportPlayerInventory The player's inventory before teleporting to the nightmare
 */
interface IAOTDPlayerNightmareData : IAOTDIslandData {
    override var positionalIndex: Int
    var preTeleportPosition: BlockPos?
    var preTeleportDimensionID: Int
    var preTeleportPlayerInventory: NBTTagList?
}