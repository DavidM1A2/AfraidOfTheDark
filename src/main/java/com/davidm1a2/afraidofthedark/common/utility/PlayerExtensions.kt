package com.davidm1a2.afraidofthedark.common.utility

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import net.minecraft.entity.player.EntityPlayer

// Extension functions to access player methods

/**
 * Opens a GUI from the afraid of the dark mod
 *
 * @param guiId The ID of the gui to open
 */
fun EntityPlayer.openGui(guiId: Int)
{
    this.openGui(AfraidOfTheDark.INSTANCE, guiId, this.world, this.position.x, this.position.y, this.position.z)
}