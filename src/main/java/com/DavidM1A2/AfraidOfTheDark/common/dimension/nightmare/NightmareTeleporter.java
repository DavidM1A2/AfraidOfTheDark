/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.dimension.nightmare;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDimensions;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.playerData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Point3D;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
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
	public void placeInPortal(Entity entity, float entityYaw)
	{
		if (dimensionNew == AOTDDimensions.Nightmare.getWorldID())
		{
			entity.motionX = entity.motionY = entity.motionZ = 0.0D;

			if (entity instanceof EntityPlayer)
			{
				final EntityPlayer entityPlayer = (EntityPlayer) entity;
				if (!entity.worldObj.isRemote)
				{
					NBTTagList inventory = new NBTTagList();
					entityPlayer.inventory.writeToNBT(inventory);
					entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setPlayerInventory(inventory);
					entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setPlayerLocationPreTeleport(new Point3D(entityPlayer), this.dimensionOld);
					entityPlayer.inventory.clear();
					entityPlayer.inventoryContainer.detectAndSendChanges();

					int locationX = this.validatePlayerLocationNightmare(entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerLocationNightmare(), entityPlayer) * AOTDDimensions.getBlocksBetweenIslands() + 20;

					((EntityPlayerMP) entityPlayer).connection.setPlayerLocation(locationX, 74, 40, 0, 0);

					entityPlayer.setHealth(20.0F);
					entityPlayer.getFoodStats().setFoodLevel(20);
					entityPlayer.inventory.addItemStackToInventory(getNamedJournal(entityPlayer));
					entityPlayer.inventory.addItemStackToInventory(getHintBook(entityPlayer));

					// Check if we need to generate enarias Altar
					/*
					if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.Enaria) && !entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).hasEnariasAltar())
					{
						entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setHasEnariasAltar(true);
						SchematicGenerator.generateSchematic(AOTDSchematics.EnariasAltar.getSchematic(), MinecraftServer.getServer().worldServerForDimension(dimensionNew), (locationX - 20) + 67, 40, 179);
					}
					*/
				}
			}
		}
		else if (dimensionOld == AOTDDimensions.Nightmare.getWorldID())
		{
			entity.motionX = entity.motionY = entity.motionZ = 0.0D;

			if (entity instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) entity;

				BlockPos playerPostionOld = entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerLocationPreTeleport().toBlockPos();

				entityPlayer.setPosition(playerPostionOld.getX(), playerPostionOld.getY(), playerPostionOld.getZ());
				entity.motionX = 0.0D;
				entity.motionY = 0.0D;
				entity.motionZ = 0.0D;

				entityPlayer.inventory.clear();
				entityPlayer.inventoryContainer.detectAndSendChanges();

				entityPlayer.inventory.readFromNBT(entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerInventory());
				entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setPlayerInventory(new NBTTagList());
			}
		}
	}

	private ItemStack getHintBook(EntityPlayer entityPlayer)
	{
		ItemStack toReturn = new ItemStack(Items.WRITTEN_BOOK, 1, 0);
		NBTHelper.setString(toReturn, "title", "Insanity's Heights");
		NBTHelper.setString(toReturn, "author", "Foul Ole Ron");
		NBTHelper.setBoolean(toReturn, "resolved", true);
		// NBTTagList pages = toReturn.getTagCompound().getTagList("pages", 8);

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

	private int validatePlayerLocationNightmare(int locationX, EntityPlayer entityPlayer)
	{
		if (locationX == 0)
		{
			if (!entityPlayer.worldObj.isRemote)
			{
				entityPlayer.worldObj.getMinecraftServer().getCommandManager().executeCommand(entityPlayer.worldObj.getMinecraftServer(), "/save-all");
			}

			int furthestOutPlayer = 0;
			for (NBTTagCompound entityPlayerData : NBTHelper.getOfflinePlayerNBTs())
			{
				furthestOutPlayer = Math.max(furthestOutPlayer, AOTDPlayerData.getPlayerLocationNightmareOffline(entityPlayerData));
			}
			for (EntityPlayer entityPlayerOther : entityPlayer.worldObj.getMinecraftServer().getPlayerList().getPlayerList())
			{
				if (!entityPlayer.isEntityEqual(entityPlayerOther))
					furthestOutPlayer = Math.max(furthestOutPlayer, entityPlayerOther.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerLocationNightmare());
			}
			entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setPlayerLocationNightmare(furthestOutPlayer + 1);

		}
		return entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerLocationNightmare();
	}
}
