/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDTileEntity;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.ghastly.EntityGhastlyEnaria;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDimensions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityGhastlyEnariaSpawner extends AOTDTileEntity
{
	public TileEntityGhastlyEnariaSpawner()
	{
		super(ModBlocks.enariaSpawner);
	}

	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
		for (EntityGhastlyEnaria enaria : this.worldObj.getEntitiesWithinAABB(EntityGhastlyEnaria.class, new AxisAlignedBB(this.getPos(), this.getPos().add(1, 1, 1)).expand(AOTDDimensions.getBlocksBetweenIslands() / 2, AOTDDimensions.getBlocksBetweenIslands() / 2, AOTDDimensions
				.getBlocksBetweenIslands() / 2)))
			enaria.setDead();
	}

	@Override
	public void onLoad()
	{
		super.onLoad();
		for (EntityGhastlyEnaria enaria : this.worldObj.getEntitiesWithinAABB(EntityGhastlyEnaria.class, new AxisAlignedBB(this.getPos(), this.getPos().add(1, 1, 1)).expand(AOTDDimensions.getBlocksBetweenIslands() / 2, AOTDDimensions.getBlocksBetweenIslands() / 2, AOTDDimensions
				.getBlocksBetweenIslands() / 2)))
			enaria.setDead();
		EntityGhastlyEnaria enariaSpawn = new EntityGhastlyEnaria(this.worldObj);
		EntityPlayer closest = this.worldObj.getClosestPlayer(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), AOTDDimensions.getBlocksBetweenIslands() / 2);
		if (closest == null)
			enariaSpawn.setBenign(true);
		else
			enariaSpawn.setBenign(!closest.getCapability(ModCapabilities.PLAYER_DATA, null).getHasBeatenEnaria());
		enariaSpawn.forceSpawn = true;
		enariaSpawn.setPosition(this.getPos().getX() + 0.5, this.getPos().getY() + 10, this.getPos().getZ() + 0.5);
		this.worldObj.spawnEntityInWorld(enariaSpawn);
	}
}
