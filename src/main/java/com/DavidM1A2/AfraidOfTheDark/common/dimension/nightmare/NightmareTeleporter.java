/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.dimension.nightmare;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.InventorySaver;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class NightmareTeleporter extends Teleporter
{
	private final WorldServer worldServerInstance;
	private final int dimensionOld;
	private final int dimensionNew;

	public NightmareTeleporter(WorldServer worldIn, int dimensionOld, int dimensionNew)
	{
		super(worldIn);
		this.worldServerInstance = worldIn;
		this.dimensionOld = dimensionOld;
		this.dimensionNew = dimensionNew;
	}

	@Override
	public void func_180266_a(Entity entity, float entityYaw)
	{
		if (dimensionNew == Constants.NightmareWorld.NIGHTMARE_WORLD_ID)
		{
			entity.motionX = entity.motionY = entity.motionZ = 0.0D;

			if (entity instanceof EntityPlayer)
			{
				if (!entity.worldObj.isRemote)
				{
					EntityPlayer entityPlayer = (EntityPlayer) entity;
					InventorySaver.saveInventory(entityPlayer);
					entityPlayer.inventory.clear();
					entityPlayer.inventoryContainer.detectAndSendChanges();

					((EntityPlayerMP) entityPlayer).playerNetServerHandler.setPlayerLocation(InventorySaver.getPlayerLocationNightmare(entityPlayer) * Constants.NightmareWorld.BLOCKS_BETWEEN_ISLANDS + 20, 77, 40, 0, 0);

					entityPlayer.setHealth(20.0F);
					entityPlayer.getFoodStats().setFoodLevel(20);
					entityPlayer.getFoodStats().setFoodSaturationLevel(20.0F);
					entityPlayer.inventory.addItemStackToInventory(getNamedJournal(entityPlayer));
					entityPlayer.inventory.addItemStackToInventory(getHintBook(entityPlayer));
				}
			}
		}
		else if (dimensionOld == Constants.NightmareWorld.NIGHTMARE_WORLD_ID)
		{
			if (entity instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) entity;

				BlockPos playerPostionOld = InventorySaver.getPlayerLocationOverworld(entityPlayer);
				entityPlayer.setPosition(playerPostionOld.getX(), playerPostionOld.getY(), playerPostionOld.getZ());
				entity.motionX = 0.0D;
				entity.motionY = 0.0D;
				entity.motionZ = 0.0D;

				InventorySaver.loadInventory(entityPlayer);
				InventorySaver.resetSavedInventory(entityPlayer);
			}
		}
	}

	private ItemStack getHintBook(EntityPlayer entityPlayer)
	{
		ItemStack toReturn = new ItemStack(Items.written_book, 1, 0);
		NBTHelper.setString(toReturn, "title", "Insanity's Heights");
		NBTHelper.setString(toReturn, "author", "Foul Ole Ron");
		NBTHelper.setBoolean(toReturn, "resolved", true);
		NBTTagList pages = toReturn.getTagCompound().getTagList("pages", 8);
		for (int i = 0; i < 10; i++)
		{
			pages.appendTag(new NBTTagString("Asdf 123"));
		}
		toReturn.getTagCompound().setTag("pages", pages);
		return toReturn;
	}

	private ItemStack getNamedJournal(EntityPlayer entityPlayer)
	{
		ItemStack toReturn = new ItemStack(ModItems.journal, 1, 0);
		NBTHelper.setString(toReturn, "owner", entityPlayer.getDisplayName().getUnformattedText());
		return toReturn;
	}
}
