/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDTickingTileEntity;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityDarkForest extends AOTDTickingTileEntity
{
	private static final int TICKS_INBETWEEN_CHECKS = 60;
	private static final int CHECK_RANGE = 14;

	public TileEntityDarkForest()
	{
		super(ModBlocks.darkForest);
	}

	@Override
	public void update()
	{
		super.update();
		if (!this.worldObj.isRemote)
		{
			if (this.ticksExisted % TICKS_INBETWEEN_CHECKS == 0)
			{
				for (Object object : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.pos.getX() + 1, this.pos.getY() + 1, this.pos.getZ() + 1).expand(CHECK_RANGE, CHECK_RANGE, CHECK_RANGE)))
				{
					if (object instanceof EntityPlayer)
					{
						EntityPlayer entityPlayer = (EntityPlayer) object;
						if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).canResearch(ResearchTypes.DarkForest))
						{
							entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).unlockResearch(ResearchTypes.DarkForest, true);
						}

						if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.DarkForest))
						{
							entityPlayer.addPotionEffect(new PotionEffect(Potion.getPotionById(30), 120, 0, true, false));
							for (int i = 0; i < entityPlayer.inventory.mainInventory.size(); i++)
							{
								ItemStack itemStack = entityPlayer.inventory.getStackInSlot(i);
								if (itemStack != null)
								{
									if (itemStack.getItem() instanceof ItemPotion)
									{
										if (itemStack.getMetadata() == 0)
										{
											if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).canResearch(ResearchTypes.SleepingPotion))
											{
												entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).unlockResearch(ResearchTypes.SleepingPotion, true);
											}

											// func_190916_E = stackSize
											entityPlayer.inventory.setInventorySlotContents(i, new ItemStack(ModItems.sleepingPotion, itemStack.func_190916_E()));
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
