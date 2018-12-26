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
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemVitaeLantern;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntitySpring extends AOTDTickingTileEntity
{
	private static final int TICKS_INBETWEEN_CHECKS = 120;
	private static final int CHECK_RANGE = 5;
	private static final int MAX_VITAE_FROM_LANTERN = 100;

	public TileEntitySpring()
	{
		super(ModBlocks.spring);
	}

	@Override
	public void update()
	{
		super.update();
		if (!this.world.isRemote)
		{
			if (this.ticksExisted % TICKS_INBETWEEN_CHECKS == 0)
			{
				for (Object object : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.pos.getX() + 1, this.pos.getY() + 1, this.pos.getZ() + 1).expand(CHECK_RANGE, CHECK_RANGE, CHECK_RANGE)))
				{
					if (object instanceof EntityPlayer)
					{
						EntityPlayer entityPlayer = (EntityPlayer) object;

						Boolean hasLantern = false;

						for (ItemStack itemStack : entityPlayer.inventory.mainInventory)
							if (itemStack.getItem() == ModItems.vitaeLantern)
							{
								hasLantern = true;
								break;
							}

						if (hasLantern)
						{
							if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).canResearch(ResearchTypes.VitaeLanternI))
							{
								entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).unlockResearch(ResearchTypes.VitaeLanternI, true);
							}

							if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.VitaeLanternI))
							{
								for (ItemStack current : entityPlayer.inventoryContainer.getInventory())
								{
									if (current.getItem() instanceof ItemVitaeLantern)
									{
										ItemVitaeLantern lantern = (ItemVitaeLantern) current.getItem();
										int vitaeToAdd = MAX_VITAE_FROM_LANTERN - lantern.getStoredVitae(current);
										vitaeToAdd = (int) (vitaeToAdd * 0.1);
										if (vitaeToAdd <= 0)
											vitaeToAdd = 1;
										if (vitaeToAdd + lantern.getStoredVitae(current) <= MAX_VITAE_FROM_LANTERN)
										{
											lantern.addVitae(current, vitaeToAdd);
											break;
										}
									}
								}
							}
							// 10 = regeneration
							entityPlayer.addPotionEffect(new PotionEffect(Potion.getPotionById(10), 100, 1, true, true));
						}
					}
				}
			}
		}
	}
}
