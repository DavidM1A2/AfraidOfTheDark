package com.davidm1a2.afraidofthedark.common.block.mangrove;

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * Class representing mangrove planks
 */
public class BlockMangrovePlanks extends AOTDBlock
{
    /**
     * Constructor for mangrove planks sets the planks properties
     */
    public BlockMangrovePlanks()
    {
        super("mangrove_planks", Material.WOOD);
        this.setHardness(2.0f);
        this.setSoundType(SoundType.WOOD);
    }
}
