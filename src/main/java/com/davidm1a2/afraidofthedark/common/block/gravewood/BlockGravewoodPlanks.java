package com.davidm1a2.afraidofthedark.common.block.gravewood;

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * Class representing gravewood planks
 */
public class BlockGravewoodPlanks extends AOTDBlock
{
    /**
     * Constructor for gravewood planks sets the planks properties
     */
    public BlockGravewoodPlanks()
    {
        super("gravewood_planks", Material.WOOD);
        this.setHardness(2.0f);
        this.setSoundType(SoundType.WOOD);
    }
}
