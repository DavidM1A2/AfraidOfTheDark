/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDTickingTileEntity;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.ghastly.EntityGhastlyEnaria;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDimensions;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityGhastlyEnariaSpawner extends AOTDTickingTileEntity
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
	public void update()
	{
		super.update();
		if (this.ticksExisted == 1)
		{
			if (!this.worldObj.isRemote)
			{
				for (EntityGhastlyEnaria enaria : this.worldObj.getEntitiesWithinAABB(EntityGhastlyEnaria.class, new AxisAlignedBB(this.getPos(), this.getPos().add(1, 1, 1)).expand(AOTDDimensions.getBlocksBetweenIslands() / 2, AOTDDimensions.getBlocksBetweenIslands() / 2, AOTDDimensions
						.getBlocksBetweenIslands() / 2)))
					enaria.setDead();
				EntityGhastlyEnaria enariaSpawn = new EntityGhastlyEnaria(this.worldObj);
				enariaSpawn.forceSpawn = true;
				enariaSpawn.setPositionAndRotation(this.getPos().getX() + 0.5, this.getPos().getY() + 10.2, this.getPos().getZ() + 0.5, this.worldObj.rand.nextFloat(), 0);
				this.worldObj.spawnEntityInWorld(enariaSpawn);
			}
		}
	}
}
