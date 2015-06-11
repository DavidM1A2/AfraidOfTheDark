package com.DavidM1A2.AfraidOfTheDark.common.debug;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.common.item.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.Schematic;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicGenerator;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicLoader;

public class ItemWorldGenTest extends AOTDItem
{
	public ItemWorldGenTest()
	{
		super();
		this.setUnlocalizedName("worldGenTest");
	}
	
	/**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
    {
    	if (!world.isRemote)
    	{
    		Schematic tree = SchematicLoader.load("TreeBranchyType2.schematic");
    		SchematicGenerator.generateSchematic(tree, world, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());
    	}
        return super.onItemRightClick(itemStack, world, entityPlayer);
    }
}
