/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDTickingTileEntity;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.ghastly.EntityGhastlyEnaria;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDimensions;

import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityGhastlyEnariaSpawner extends AOTDTickingTileEntity
{
	public TileEntityGhastlyEnariaSpawner()
	{
		super(ModBlocks.enariaSpawner);
	}

	@Override
	public void update()
	{
		super.update();
		if (this.ticksExisted % 100 == 0)
		{
			if (!this.world.isRemote)
			{
				List<EntityGhastlyEnaria> entities = this.world.<EntityGhastlyEnaria> getEntitiesWithinAABB(EntityGhastlyEnaria.class, new AxisAlignedBB(this.getPos(), this.getPos().up()).expand(AOTDDimensions.getBlocksBetweenIslands() / 2, AOTDDimensions.getBlocksBetweenIslands() / 2,
						AOTDDimensions.getBlocksBetweenIslands() / 2));

				boolean oneAlive = false;
				for (EntityGhastlyEnaria enaria : entities)
					if (!enaria.isDead)
						oneAlive = true;

				if (!oneAlive)
				{
					EntityGhastlyEnaria enariaSpawn = new EntityGhastlyEnaria(this.world);
					enariaSpawn.forceSpawn = true;
					enariaSpawn.setPositionAndRotation(this.getPos().getX() + 0.5, this.getPos().getY() + 10.2, this.getPos().getZ() + 0.5, this.world.rand.nextFloat(), 0);
					this.world.spawnEntity(enariaSpawn);
				}
			}
		}
	}
}
