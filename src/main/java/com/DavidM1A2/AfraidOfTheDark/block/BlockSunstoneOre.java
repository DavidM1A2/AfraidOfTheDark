package com.DavidM1A2.AfraidOfTheDark.block;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class BlockSunstoneOre extends AOTDBlock
{
	public BlockSunstoneOre(Material material)
	{
		super(material);
		this.setUnlocalizedName("sunstoneOre");
		this.setLightLevel(1.0f);
		this.setHardness(10.0F);
		this.setResistance(50.0F);
	}
	
	/**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
    	return ModItems.sunstoneIngot;
    }
}
