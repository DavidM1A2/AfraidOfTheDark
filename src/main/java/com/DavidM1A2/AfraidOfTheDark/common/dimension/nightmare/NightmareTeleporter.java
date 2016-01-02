/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.dimension.nightmare;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.lang3.math.Fraction;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.WorldGenerationUtility;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveHandler;

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
	public void placeInPortal(Entity entity, float entityYaw)
	{
		if (dimensionNew == Constants.NightmareWorld.NIGHTMARE_WORLD_ID)
		{
			entity.motionX = entity.motionY = entity.motionZ = 0.0D;

			if (entity instanceof EntityPlayer)
			{
				if (!entity.worldObj.isRemote)
				{
					EntityPlayer entityPlayer = (EntityPlayer) entity;
					NBTTagList inventory = new NBTTagList();
					entityPlayer.inventory.writeToNBT(inventory);
					AOTDPlayerData.get(entityPlayer).setPlayerInventory(inventory);
					AOTDPlayerData.get(entityPlayer).setPlayerLocationOverworld(new int[]
					{ entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY() + 1, entityPlayer.getPosition().getZ() });
					entityPlayer.inventory.clear();
					entityPlayer.inventoryContainer.detectAndSendChanges();

					int locationX = this.validatePlayerLocationNightmare(AOTDPlayerData.get(entityPlayer).getPlayerLocationNightmare(), entityPlayer) * Constants.NightmareWorld.BLOCKS_BETWEEN_ISLANDS + 20;

					((EntityPlayerMP) entityPlayer).playerNetServerHandler.setPlayerLocation(locationX, 74, 40, 0, 0);

					entityPlayer.setHealth(20.0F);
					entityPlayer.getFoodStats().setFoodLevel(20);
					entityPlayer.inventory.addItemStackToInventory(getNamedJournal(entityPlayer));
					entityPlayer.inventory.addItemStackToInventory(getHintBook(entityPlayer));
				}
			}
		}
		else if (dimensionOld == Constants.NightmareWorld.NIGHTMARE_WORLD_ID)
		{
			entity.motionX = entity.motionY = entity.motionZ = 0.0D;

			if (entity instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) entity;

				BlockPos playerPostionOld = this.intArrToBlockPos(AOTDPlayerData.get(entityPlayer).getPlayerLocationOverworld(), entityPlayer);

				entityPlayer.setPosition(playerPostionOld.getX(), playerPostionOld.getY(), playerPostionOld.getZ());
				entity.motionX = 0.0D;
				entity.motionY = 0.0D;
				entity.motionZ = 0.0D;

				entityPlayer.inventory.clear();
				entityPlayer.inventoryContainer.detectAndSendChanges();

				entityPlayer.inventory.readFromNBT(AOTDPlayerData.get(entityPlayer).getPlayerInventory());
				AOTDPlayerData.get(entityPlayer).setPlayerInventory(new NBTTagList());
			}
		}
	}

	private ItemStack getHintBook(EntityPlayer entityPlayer)
	{
		ItemStack toReturn = new ItemStack(Items.written_book, 1, 0);
		NBTHelper.setString(toReturn, "title", "Insanity's Heights");
		NBTHelper.setString(toReturn, "author", "Foul Ole Ron");
		NBTHelper.setBoolean(toReturn, "resolved", true);
		//NBTTagList pages = toReturn.getTagCompound().getTagList("pages", 8);

		toReturn.getTagCompound().setTag("pages", createPages());
		return toReturn;
	}

	private ItemStack getNamedJournal(EntityPlayer entityPlayer)
	{
		ItemStack toReturn = new ItemStack(ModItems.journal, 1, 0);
		NBTHelper.setString(toReturn, "owner", entityPlayer.getDisplayName().getUnformattedText());
		return toReturn;
	}

	private NBTTagList createPages()
	{
		NBTTagList pages = new NBTTagList();
		pages.appendTag(new NBTTagString("To whomever finds this: don't stay here. This place is evil. I have been stuck here for longer than I can remember. I can hear the abyss calling to me. It beckons me to jump, calling my name. I've found all of the notes, but I cannot"));
		pages.appendTag(new NBTTagString("leave with them. There are ten scrolls hidden here. Three are in the tallest tower, with two being near the top and one being near the bottom. The saw mill whispers such sweet things to be. The stone tower says that it has two"));
		pages.appendTag(new NBTTagString("gifts for me. What pretty things they have, so many rings. Enaria's bones whisper to me from her grave. I'm sorry; we tried to save you! Her whispers make me want to hide inside of the log. The roof top rooms are hiding something"));
		pages.appendTag(new NBTTagString("from me. They always stay quiet when I am near. I know they are keeping secrets from me! What has it told you? What has the monolith told you to make you stop talking to me? Answer me Enaria! Where have you gone? Have you left me?"));
		pages.appendTag(new NBTTagString("You said we would be together forever!"));
		return pages;
	}

	private BlockPos intArrToBlockPos(int[] location, EntityPlayer entityPlayer)
	{
		if (location.length == 0)
		{
			return new BlockPos(0, WorldGenerationUtility.getFirstNonAirBlock(entityPlayer.worldObj, 0, 0) + 2, 0);
		}

		if (location[1] == 0)
		{
			location[1] = WorldGenerationUtility.getFirstNonAirBlock(entityPlayer.worldObj, 0, 0) + 2;
			LogHelper.error("Player data incorrectly saved. Defaulting to 0, 0. Please report this to the mod author.");
		}
		return new BlockPos(location[0], location[1], location[2]);
	}

	private int validatePlayerLocationNightmare(int locationX, EntityPlayer entityPlayer)
	{
		if (locationX == 0)
		{
			if (!entityPlayer.worldObj.isRemote)
			{
				MinecraftServer.getServer().getCommandManager().executeCommand(MinecraftServer.getServer(), "/save-all");
			}
			
			int furthestOutPlayer = 0;
			for (NBTTagCompound entityPlayerData : NBTHelper.getOfflinePlayerNBTs())
			{
				furthestOutPlayer = Math.max(furthestOutPlayer, AOTDPlayerData.getPlayerLocationNightmareOffline(entityPlayerData));
			}			
			AOTDPlayerData.get(entityPlayer).setPlayerLocationNightmare(furthestOutPlayer + 1);

		}
		return AOTDPlayerData.get(entityPlayer).getPlayerLocationNightmare();
	}
}
