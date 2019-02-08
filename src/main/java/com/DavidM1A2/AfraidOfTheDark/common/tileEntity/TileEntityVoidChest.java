package com.DavidM1A2.afraidofthedark.common.tileEntity;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.dimension.IAOTDPlayerVoidChestData;
import com.DavidM1A2.afraidofthedark.common.capabilities.world.VoidChestData;
import com.DavidM1A2.afraidofthedark.common.constants.ModBlocks;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModDimensions;
import com.DavidM1A2.afraidofthedark.common.dimension.voidChest.VoidChestUtility;
import com.DavidM1A2.afraidofthedark.common.packets.otherPackets.SyncVoidChest;
import com.DavidM1A2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;

import java.util.Set;
import java.util.UUID;

/**
 * Class that represents the void chest tile entity logic
 */
public class TileEntityVoidChest extends AOTDTickingTileEntity
{
	// NBT tag constants
	private static final String NBT_OWNER = "owner";
	private static final String NBT_INDEX_TO_GO_TO = "index_to_go_to";
	private static final String NBT_FRIENDS = "friends";

	// Chest constants
	private static final int MILLIS_TO_CLOSE_CHEST = 3000;
	private static final double PULL_FORCE = 0.05;
	private static final float OPEN_CLOSE_SPEED = 0.1f;
	private static final double DISTANCE_TO_SEND_PLAYER = 2;

	// The chest's current lid angle between 0 and 1
	private float lidAngle;
	// The chest's lid angle last tick
	private float previousLidAngle;
	// True if the lid should be open, false otherwise. The chest will animate the lid according to this flag
	private boolean shouldBeOpen;
	// The UUID of the player that owns this void chest
	private UUID owner;
	// A list of friends that may visit this player's void chest
	private Set<UUID> friends = Sets.newHashSet();
	// The index of this owner's void chest position that this chest will send people to
	private int indexToGoTo;
	// The current player that has opened the chest and will be sent soon
	private EntityPlayer playerToSend;
	// The last time (using system time) that the void chest was right clicked/interacted with
	private long lastInteraction;

	/**
	 * Constructor initializes the tile entity fields
	 */
	public TileEntityVoidChest()
	{
		super(ModBlocks.VOID_CHEST);
	}

	/**
	 * Called every game tick to update the tile entity
	 */
	@Override
	public void update()
	{
		super.update();
		int x = this.pos.getX();
		int y = this.pos.getY();
		int z = this.pos.getZ();

		// Check every 20 ticks if it's time to close the chest or not
		if (this.ticksExisted % 20 == 0)
			if ((System.currentTimeMillis() - this.lastInteraction) > MILLIS_TO_CLOSE_CHEST)
				this.shouldBeOpen = false;

		// Set the last lid angle to be the current lid angle
		this.previousLidAngle = this.lidAngle;

		// If the lid is closed and the chest should be open play the open sound
		if (this.shouldBeOpen && this.lidAngle == 0)
			this.world.playSound(x + 0.5, y + 0.5, z + 0.5, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5f, this.world.rand.nextFloat() * 0.1f + 0.9f, false);

		// If the chest should be open make it suck in the current player that opened the chest
		if (this.shouldBeOpen && this.playerToSend != null)
		{
			double xVelocity = this.pos.getX() + 0.5 - this.playerToSend.posX;
			double yVelocity = this.pos.getY() + 0.5 - this.playerToSend.posY;
			double zVelocity = this.pos.getZ() + 0.5 - this.playerToSend.posZ;
			xVelocity = MathHelper.clamp(xVelocity, -PULL_FORCE, PULL_FORCE);
			yVelocity = MathHelper.clamp(yVelocity, -PULL_FORCE, PULL_FORCE);
			zVelocity = MathHelper.clamp(zVelocity, -PULL_FORCE, PULL_FORCE);
			playerToSend.addVelocity(xVelocity, yVelocity, zVelocity);
		}

		// Update the lid's angle if it's in transition from open to closed or closed to open
		if ((!this.shouldBeOpen && this.lidAngle > 0) || (this.shouldBeOpen && this.lidAngle < 1))
		{
			// Either add or subtract from the lid angle depending on if we're opening or closing
			if (this.shouldBeOpen)
				this.lidAngle = this.lidAngle + OPEN_CLOSE_SPEED;
			else
				this.lidAngle = this.lidAngle - OPEN_CLOSE_SPEED;
			// Clamp the angle between 0 and 1
			this.lidAngle = MathHelper.clamp(this.lidAngle, 0f, 1f);

			// If the chest is closing play the closing sound and send the player to the void chest dimension
			if (this.lidAngle < 0.5 && this.previousLidAngle >= 0.5)
			{
				this.world.playSound(x + 0.5, y + 0.5, z + 0.5f, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5f, this.world.rand.nextFloat() * 0.1f + 0.9f, false);

				// Ensure the player we're sending is valid
				if (this.playerToSend != null)
				{
					int currentDimensionID = this.world.provider.getDimension();
					// Only allow void chest usage in the overworld
					if (currentDimensionID == 0)
					{
						// Temp, send the player to the void chest here if they are close enough to it
						if (Math.sqrt(this.playerToSend.getDistanceSq(this.getPos())) < DISTANCE_TO_SEND_PLAYER)
							if (!this.world.isRemote)
							{
								// If the player we're sending is the owner send them to their home dimension, otherwise send them to their friend's dimension
								if (!this.playerToSend.getGameProfile().getId().equals(this.owner))
									this.playerToSend.getCapability(ModCapabilities.PLAYER_VOID_CHEST_DATA, null).setFriendsIndex(this.indexToGoTo);
								this.playerToSend.changeDimension(ModDimensions.VOID_CHEST.getId(), ModDimensions.NOOP_TELEPORTER);
							}
					}
					else
					{
						if (!this.world.isRemote)
							playerToSend.sendMessage(new TextComponentString("The void chest refuses to work in this dimension."));
					}
				}
			}
		}
	}

	/**
	 * Called when a player right clicks the chest
	 *
	 * @param entityPlayer The player that opened the chest
	 */
	public void interact(EntityPlayer entityPlayer)
	{
		// Server side processing only
		if (!world.isRemote)
		{
			// If the chest has no owner attempt to set this player as the owner
			if (this.owner == null)
			{
				this.owner = entityPlayer.getGameProfile().getId();
				this.indexToGoTo = VoidChestUtility.getOrAssignPlayerPositionalIndex(world.getMinecraftServer(), entityPlayer);
				entityPlayer.sendMessage(new TextComponentString("You owner of this chest has been set to " + entityPlayer.getDisplayName().getUnformattedText()));
			}
			// If the chest has an owner test if we're the owner
			else if (entityPlayer.getGameProfile().getId().equals(this.owner))
			{
				// Test if the player is holding a name tag. If so add/remoe the friend, if not open the chest
				ItemStack heldItem = entityPlayer.getHeldItemMainhand();
				if (heldItem.getItem() instanceof ItemNameTag)
				{
					// Grab the player's UUID
					UUID friendsUUID = this.getID(heldItem.getDisplayName());
					// If it's non-null continue, otherwise tell the player the name is wrong
					if (friendsUUID != null)
					{
						// If the chest does not have the friend add the friend
						if (!this.friends.contains(friendsUUID))
						{
							this.friends.add(friendsUUID);
							entityPlayer.sendMessage(new TextComponentString("Player " + heldItem.getDisplayName() + " was added to this chest's friend list."));
						}
						// Otherwise remove the friend
						else
						{
							this.friends.remove(friendsUUID);
							entityPlayer.sendMessage(new TextComponentString("Player " + heldItem.getDisplayName() + " was removed from this chest's friend list."));
						}
					}
					else
					{
						entityPlayer.sendMessage(new TextComponentString("The account " + heldItem.getDisplayName() + " does not exist, are you sure you spelled the name correctly?"));
					}
				}
				// The player is not holding a name tag so open the chest
				else
				{
					this.openChest(entityPlayer);
					AfraidOfTheDark.INSTANCE.getPacketHandler().sendToDimension(new SyncVoidChest(this.pos.getX(), this.pos.getY(), this.pos.getZ(), entityPlayer), 0);
				}
			}
			// Test if the player is on the chest's friends list
			else if (this.friends.contains(entityPlayer.getGameProfile().getId()))
			{
				// Test if the player is trying to edit a chest's friend list that isnt theirs
				ItemStack heldItem = entityPlayer.getHeldItemMainhand();
				if (heldItem.getItem() instanceof ItemNameTag)
					entityPlayer.sendMessage(new TextComponentString("I can't edit the access of this chest"));
				else
				{
					this.openChest(entityPlayer);
					AfraidOfTheDark.INSTANCE.getPacketHandler().sendToDimension(new SyncVoidChest(this.pos.getX(), this.pos.getY(), this.pos.getZ(), entityPlayer), 0);
				}
			}
			// The player does not have chest access so tell them
			else
			{
				entityPlayer.sendMessage(new TextComponentString("I don't have access to this void chest"));
			}
		}
	}

	/**
	 * Called to open the chest with a given player in mind
	 *
	 * @param entityPlayer The player that opened the chest
	 */
	public void openChest(EntityPlayer entityPlayer)
	{
		this.lastInteraction = System.currentTimeMillis();
		this.shouldBeOpen = true;
		this.playerToSend = entityPlayer;
	}

	/**
	 * Gets the UUID for a given player username
	 *
	 * @param playerName The player's username
	 * @return The player's UUID
	 */
	private UUID getID(String playerName)
	{
		GameProfile gameProfileForUsername = this.world.getMinecraftServer().getPlayerProfileCache().getGameProfileForUsername(playerName);
		return gameProfileForUsername == null ? null : gameProfileForUsername.getId();
	}

	/**
	 * Called to write this tile entities state to NBT data format
	 *
	 * @param compound The NBT compound to write to
	 * @return The NBT compound representing this void chest tile entity
	 */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		// Start by writing any default MC things to the NBT
		compound = super.writeToNBT(compound);

		// Write the owner as an ID
		if (this.owner != null)
			compound.setUniqueId(NBT_OWNER, this.owner);
		// Write the index to go to as an integer
		compound.setInteger(NBT_INDEX_TO_GO_TO, this.indexToGoTo);
		NBTTagList friendNBT = new NBTTagList();
		// For each friend append two new NBTLongs, one for most sig bits and one for least sig bits
		this.friends.forEach(friend ->
		{
			friendNBT.appendTag(new NBTTagLong(friend.getLeastSignificantBits()));
			friendNBT.appendTag(new NBTTagLong(friend.getMostSignificantBits()));
		});
		// Set the friends NBT to be this array
		compound.setTag(NBT_FRIENDS, friendNBT);

		return compound;
	}

	/**
	 * Reads this tile entity's state in from an NBT compound
	 *
	 * @param compound The NBT compound to read from
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		// Start by reading any default MC things from the NBT
		super.readFromNBT(compound);

		// Read the owner tag, it could potentially not exist...
		if (compound.hasKey(NBT_OWNER + "Most") && compound.hasKey(NBT_OWNER + "Least"))
			this.owner = compound.getUniqueId(NBT_OWNER);
		else
			this.owner = null;
		// Read the index to go to tag
		this.indexToGoTo = compound.getInteger(NBT_INDEX_TO_GO_TO);
		// Read all the chest's friends list in
		NBTTagList friendIDParts = compound.getTagList(NBT_FRIENDS, Constants.NBT.TAG_LONG);
		for (int i = 0; i < friendIDParts.tagCount(); i = i + 2)
		{
			NBTBase friendIDLeast = friendIDParts.get(i);
			NBTBase friendIDMost = friendIDParts.get(i + 1);
			// Ensure the tags are the correct type
			if (friendIDLeast instanceof NBTTagLong && friendIDMost instanceof NBTTagLong)
				// Add the friend back in from least and most significant bits
				this.friends.add(new UUID(((NBTTagLong) friendIDMost).getLong(), ((NBTTagLong) friendIDLeast).getLong()));
		}
	}

	/**
	 * @return The angle of the lid this tick
	 */
	public float getLidAngle()
	{
		return this.lidAngle;
	}

	/**
	 * @return The angle of the lid last tick, used only in rendering
	 */
	public float getPreviousLidAngle()
	{
		return this.previousLidAngle;
	}
}
