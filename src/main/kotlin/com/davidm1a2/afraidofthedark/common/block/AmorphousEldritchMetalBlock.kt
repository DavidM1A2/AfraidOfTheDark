package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraftforge.common.ToolType

/**
 * Class representing amorphous eldritch metal, this can be walked through
 *
 * @constructor initializes the block's properties
 */
class AmorphousEldritchMetalBlock : AOTDBlock(
    "amorphous_eldritch_metal",
    Properties.create(Material.PORTAL)
        .doesNotBlockMovement()
        .lightValue(1)
        .hardnessAndResistance(10.0f, 50.0f)
) {
    override fun getHarvestLevel(state: BlockState): Int {
        return 2
    }

    override fun getHarvestTool(state: BlockState): ToolType {
        return ToolType.PICKAXE
    }
}