/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity;

import java.util.ArrayList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.block.core.AOTDTileEntity;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncVoidChest;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.AOTDDimensions;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.playerData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class TileEntityVoidChest extends AOTDTileEntity
{
	/** The current angle of the lid (between 0 and 1) */
	public float lidAngle;
	/** The angle of the lid last tick */
	public float prevLidAngle;

	private boolean shouldBeOpen = false;

	/** Server sync counter (once per 20 ticks) */
	private int ticksSinceSync;
	private int cachedChestType;

	private String owner = "";
	private List<String> friends = new ArrayList<String>();

	private int locationToGoTo = -1;

	private EntityPlayer entityPlayerToSend = null;
	private int coordinateToSendTo = -1;

	private long lastInteraction = -1;

	public TileEntityVoidChest()
	{
		super(ModBlocks.voidChest);
	}

	public void readFromNBT(NBTTagCompound compound)
	{
		this.owner = compound.getString("owner");
		this.locationToGoTo = compound.getInteger("location");

		NBTTagList friends = compound.getTagList("friends", net.minecraftforge.common.util.Constants.NBT.TAG_STRING);

		for (int i = 0; i < friends.tagCount(); i++)
		{
			NBTBase friend = friends.get(i);
			if (friend instanceof NBTTagString)
			{
				this.friends.add(((NBTTagString) friend).getString());
			}
		}

		super.readFromNBT(compound);
	}

	public void writeToNBT(NBTTagCompound compound)
	{
		compound.setString("owner", this.owner);
		compound.setInteger("location", this.locationToGoTo);

		NBTTagList friends = new NBTTagList();

		for (int i = 0; i < this.friends.size(); i++)
		{
			String string = this.friends.get(i);
			if (string != null)
			{
				NBTTagString friend = new NBTTagString(string);
				friends.appendTag(friend);
			}
		}
		compound.setTag("friends", friends);

		super.writeToNBT(compound);
	}

	@Override
	public void update()
	{
		int i = this.pos.getX();
		int j = this.pos.getY();
		int k = this.pos.getZ();
		this.ticksSinceSync = this.ticksSinceSync + 1;
		;
		float f;

		if (ticksSinceSync % 20 == 0)
		{
			if ((System.currentTimeMillis() - this.lastInteraction) > 3000)
			{
				this.shouldBeOpen = false;
			}
		}

		this.prevLidAngle = this.lidAngle;
		f = 0.1F;
		double d2;

		// Opening chest
		if (shouldBeOpen && this.lidAngle == 0.0F)
		{
			double d1 = i + 0.5D;
			d2 = k + 0.5D;

			this.worldObj.playSoundEffect(d1, j + 0.5D, d2, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (shouldBeOpen)
		{
			double xVelocity = this.pos.getX() + 0.5 - entityPlayerToSend.posX;
			double yVelocity = this.pos.getY() + 0.5 - entityPlayerToSend.posY;
			double zVelocity = this.pos.getZ() + 0.5 - entityPlayerToSend.posZ;
			xVelocity = MathHelper.clamp_double(xVelocity, -0.05, 0.05);
			yVelocity = MathHelper.clamp_double(yVelocity, -0.05, 0.05);
			zVelocity = MathHelper.clamp_double(zVelocity, -0.05, 0.05);
			entityPlayerToSend.addVelocity(xVelocity, yVelocity, zVelocity);
		}

		// Closing chest
		if (!shouldBeOpen && this.lidAngle > 0.0F || shouldBeOpen && this.lidAngle < 1.0F)
		{
			float f1 = this.lidAngle;

			if (shouldBeOpen)
			{
				this.lidAngle += f;
			}
			else
			{
				this.lidAngle -= f;
			}

			if (this.lidAngle > 1.0F)
			{
				this.lidAngle = 1.0F;
			}

			float f2 = 0.5F;

			if (this.lidAngle < f2 && f1 >= f2)
			{
				d2 = i + 0.5D;
				double d0 = k + 0.5D;

				this.worldObj.playSoundEffect(d2, j + 0.5D, d0, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);

				if (this.worldObj.provider.getDimensionId() != AOTDDimensions.VoidChest.getWorldID())
				{
					for (Object object : this.worldObj.getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(this.pos, this.pos.add(.625D, .625D, .625D)).expand(2.0D, 2.0D, 2.0D)))
					{
						EntityPlayerMP entityPlayerMP = (EntityPlayerMP) object;
						if (entityPlayerMP == entityPlayerToSend)
						{
							Utility.sendPlayerToVoidChest(entityPlayerMP, this.locationToGoTo);
						}
					}
				}
			}

			if (this.lidAngle < 0.0F)
			{
				this.lidAngle = 0.0F;
			}
		}
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public void interact(EntityPlayer entityPlayer)
	{
		if (!entityPlayer.worldObj.isRemote)
		{
			if (this.owner.equals(""))
			{
				this.owner = entityPlayer.getDisplayName().getUnformattedText();
				this.locationToGoTo = this.validatePlayerLocationVoidChest(entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerLocationVoidChest(), entityPlayer);
				entityPlayer.addChatMessage(new ChatComponentText("The owner of this chest has been set to " + entityPlayer.getDisplayName().getUnformattedText() + "."));
			}
			else if (entityPlayer.getDisplayName().getUnformattedText().equals(owner))
			{
				if (entityPlayer.getCurrentEquippedItem() != null)
				{
					ItemStack itemStack = entityPlayer.getCurrentEquippedItem();
					if (itemStack.getItem() instanceof ItemNameTag)
					{
						if (!friends.contains(itemStack.getDisplayName()))
						{
							friends.add(itemStack.getDisplayName());
							if (!worldObj.isRemote)
							{
								entityPlayer.addChatMessage(new ChatComponentText("Player " + itemStack.getDisplayName() + " was added to this chest's friend list."));
							}
						}
						else
						{
							friends.remove(itemStack.getDisplayName());
							if (!worldObj.isRemote)
							{
								entityPlayer.addChatMessage(new ChatComponentText("Player " + itemStack.getDisplayName() + " was removed from this chest's friend list."));
							}
						}
						return;
					}
				}
				this.openChest(entityPlayer);
				AfraidOfTheDark.getPacketHandler().sendToAllAround(new SyncVoidChest(this.pos.getX(), this.pos.getY(), this.pos.getZ(), entityPlayer.getEntityId()), new TargetPoint(this.worldObj.provider.getDimensionId(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 30));
			}
			else if (friends.contains(entityPlayer.getDisplayName().getUnformattedText()))
			{
				if (entityPlayer.getCurrentEquippedItem() != null)
				{
					ItemStack itemStack = entityPlayer.getCurrentEquippedItem();
					if (itemStack.getItem() instanceof ItemNameTag)
					{
						entityPlayer.addChatMessage(new ChatComponentText("I can't edit access to this chest"));
						return;
					}
				}
				this.openChest(entityPlayer);
				AfraidOfTheDark.getPacketHandler().sendToAllAround(new SyncVoidChest(this.pos.getX(), this.pos.getY(), this.pos.getZ(), entityPlayer.getEntityId()), new TargetPoint(this.worldObj.provider.getDimensionId(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 30));
			}
			else
			{
				entityPlayer.addChatMessage(new ChatComponentText("I don't have access to this chest."));
			}
		}
	}

	public void openChest(EntityPlayer entityPlayer)
	{
		this.lastInteraction = System.currentTimeMillis();
		this.shouldBeOpen = true;
		this.entityPlayerToSend = entityPlayer;
	}

	private int validatePlayerLocationVoidChest(int locationX, EntityPlayer entityPlayer)
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
				furthestOutPlayer = Math.max(furthestOutPlayer, AOTDPlayerData.getPlayerLocationVoidChestOffline(entityPlayerData));
			}
			entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setPlayerLocationVoidChest(furthestOutPlayer + 1);
		}
		return entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerLocationVoidChest();
	}
}
