/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity;

import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDTileEntity;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityDarkForest extends AOTDTileEntity
{
	private int ticksExisted = 0;
	private static final int TICKS_INBETWEEN_CHECKS = 60;
	private static final int CHECK_RANGE = 14;

	public TileEntityDarkForest()
	{
		super(ModBlocks.darkForest);
	}

	@Override
	public void update()
	{
		if (!this.worldObj.isRemote)
		{
			if (ticksExisted % TICKS_INBETWEEN_CHECKS == 0)
			{
				ticksExisted = 1;
				for (Object object : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.fromBounds(this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.pos.getX() + 1, this.pos.getY() + 1, this.pos.getZ() + 1).expand(CHECK_RANGE, CHECK_RANGE, CHECK_RANGE)))
				{
					if (object instanceof EntityPlayer)
					{
						EntityPlayer entityPlayer = (EntityPlayer) object;
						if (AOTDPlayerData.get(entityPlayer).canResearch(ResearchTypes.DarkForest))
						{
							AOTDPlayerData.get(entityPlayer).unlockResearch(ResearchTypes.DarkForest, true);
						}

						if (AOTDPlayerData.get(entityPlayer).isResearched(ResearchTypes.DarkForest))
						{
							entityPlayer.addPotionEffect(new PotionEffect(30, 120, 0, true, false));
							if (entityPlayer.inventory.hasItem(Items.potionitem))
							{
								for (int i = 0; i < entityPlayer.inventory.mainInventory.length; i++)
								{
									ItemStack itemStack = entityPlayer.inventory.mainInventory[i];
									if (itemStack != null)
									{
										if (itemStack.getItem() instanceof ItemPotion)
										{
											if (itemStack.getMetadata() == 0)
											{
												if (AOTDPlayerData.get(entityPlayer).canResearch(ResearchTypes.SleepingPotion))
												{
													AOTDPlayerData.get(entityPlayer).unlockResearch(ResearchTypes.SleepingPotion, true);
												}

												entityPlayer.inventory.setInventorySlotContents(i, new ItemStack(ModItems.sleepingPotion, itemStack.stackSize));
											}
										}
									}
								}
							}
						}
					}
				}
			}
			else
			{
				ticksExisted = ticksExisted + 1;
			}
		}
	}
}
