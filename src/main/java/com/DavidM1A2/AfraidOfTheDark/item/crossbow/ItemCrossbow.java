/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.item.crossbow;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.EntityIronBolt;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.EntitySilverBolt;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.EntityWoodenBolt;
import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.item.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateCrossbow;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.utility.NBTHelper;

// The crossbow Item
public class ItemCrossbow extends AOTDItem
{
	// Keep a loaded and unloaded icon and store reload time
	private final int RELOAD_TIME = 100;
	// This array contains available bolts
	private final String[] availableBolts =
		{ "IronBolt", "SilverBolt", "WoodenBolt" };

	public ItemCrossbow()
	{
		// 1 bow per itemstack
		super();
		this.setUnlocalizedName("crossbow");
		this.setMaxStackSize(1);
	}

	// On swing (left click) if the bow is cocked we fire
	@Override
	public boolean onEntitySwing(final EntityLivingBase entityLiving, final ItemStack itemStack)
	{
		if (entityLiving instanceof EntityPlayer)
		{
			final EntityPlayer entityPlayer = (EntityPlayer) entityLiving;

			if (NBTHelper.getBoolean(itemStack, "isCocked"))
			{
				if (!entityPlayer.worldObj.isRemote)
				{
					this.setIsCocked(itemStack, false, entityPlayer, false);
					this.fireBolt(entityPlayer, entityPlayer.getEntityWorld(), itemStack);
				}
			}
		}

		return false;
	}

	// If the player is sneaking and right clicks, we change the mode.
	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		if (entityPlayer.isSneaking())
		{
			if (!world.isRemote)
			{
				this.changeMode(itemStack, entityPlayer);
			}
		}
		else
		{
			// If the player is not sneaking, we check to see if the bow is cocked or not
			if (!NBTHelper.getBoolean(itemStack, "isCocked") && (NBTHelper.getFloat(itemStack, "pullLevel") == 0.0F))
			{
				// If we are in creative, begin cocking he bow
				if (entityPlayer.capabilities.isCreativeMode)
				{
					entityPlayer.setItemInUse(itemStack, this.RELOAD_TIME);
				}
				else
				{
					// If we are not in creative, check to see if the the player has the required bolt in his/her inventory
					if ((this.availableBolts[NBTHelper.getInt(itemStack, "mode")].equals("IronBolt") && entityPlayer.inventory.hasItem(ModItems.ironBolt)) || (this.availableBolts[NBTHelper.getInt(itemStack, "mode")].equals("WoodenBolt") && entityPlayer.inventory.hasItem(ModItems.woodenBolt))
							|| (this.availableBolts[NBTHelper.getInt(itemStack, "mode")].equals("SilverBolt") && entityPlayer.inventory.hasItem(ModItems.silverBolt)))
					{
						entityPlayer.setItemInUse(itemStack, this.RELOAD_TIME);
					}
					else
					{
						// Else we print out that the player needs bolts to fire
						entityPlayer.addChatMessage(new ChatComponentText("You do not have any bolts of type: " + this.availableBolts[NBTHelper.getInt(itemStack, "mode")]));
					}
				}
			}
		}
		return itemStack;
	}

	@Override
	public void onUsingTick(final ItemStack itemStack, final EntityPlayer entityPlayer, final int count)
	{
		LogHelper.info("Got here!");
		// On using we play a sound
		if (count == (this.RELOAD_TIME - 1))
		{
			entityPlayer.playSound("afraidofthedark:crossbowLoad", 0.9F, ((Item.itemRand.nextFloat() * 0.8F) + 1.2F));
		}
		// If the bow hase been in use for RELOADTIME, we set the bow cocked
		if (count == (this.RELOAD_TIME - 60))
		{
			if (entityPlayer.worldObj.isRemote && !NBTHelper.getBoolean(itemStack, "isCocked")
					&& (entityPlayer.capabilities.isCreativeMode || entityPlayer.inventory.hasItem(ModItems.silverBolt) || entityPlayer.inventory.hasItem(ModItems.ironBolt) || entityPlayer.inventory.hasItem(ModItems.woodenBolt)))
			{
				this.setIsCocked(itemStack, true, entityPlayer, true);
			}
		}
		// If the bow is not fully cocked, set the animation on the bow
		if (count > (this.RELOAD_TIME - 60))
		{
			NBTHelper.setFloat(itemStack, "pullLevel", (1.0F - ((count - 40.0F) / 60.0F)));
		}
	}

	// If the time in use is greater than 40, the bow was not completely cocked and we reset the pull animation
	@Override
	public void onPlayerStoppedUsing(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer, final int count)
	{
		if (count > 40.0)
		{
			NBTHelper.setFloat(itemStack, "pullLevel", 0.0F);
		}
	}

	// Change mode sets the mode NBT tag on the crossbow
	public void changeMode(final ItemStack itemStack, final EntityPlayer entityPlayer)
	{
		if (!NBTHelper.getBoolean(itemStack, "isCocked"))
		{
			if (NBTHelper.getInt(itemStack, "mode") < (this.availableBolts.length - 1))
			{
				NBTHelper.setInteger(itemStack, "mode", NBTHelper.getInt(itemStack, "mode") + 1);
				entityPlayer.addChatMessage(new ChatComponentText("Crossbow will now fire " + this.availableBolts[NBTHelper.getInt(itemStack, "mode")] + "s!"));
			}
			else
			{
				NBTHelper.setInteger(itemStack, "mode", 0);
				entityPlayer.addChatMessage(new ChatComponentText("Crossbow will now fire " + this.availableBolts[NBTHelper.getInt(itemStack, "mode")] + "s!"));
			}
		}

	}

	// When we fire, set the pull level of the bow to 0
	public void fireBolt(final EntityPlayer entityPlayer, final World world, final ItemStack itemStack)
	{
		NBTHelper.setFloat(itemStack, "pullLevel", (0.0F));
		// If the player is in creative mode, fire the appropriate bolt
		if (entityPlayer.capabilities.isCreativeMode)
		{
			if (!world.isRemote)
			{
				if (this.availableBolts[NBTHelper.getInt(itemStack, "mode")].equals("IronBolt"))
				{
					world.playSoundAtEntity(entityPlayer, "afraidofthedark:crossbowFire", 0.5F, ((Item.itemRand.nextFloat() * 0.4F) + 0.8F));
					world.spawnEntityInWorld(new EntityIronBolt(world, entityPlayer));
				}
				else if (this.availableBolts[NBTHelper.getInt(itemStack, "mode")].equals("SilverBolt"))
				{
					world.playSoundAtEntity(entityPlayer, "afraidofthedark:crossbowFire", 0.5F, ((Item.itemRand.nextFloat() * 0.4F) + 0.8F));
					world.spawnEntityInWorld(new EntitySilverBolt(world, entityPlayer));
				}
				else if (this.availableBolts[NBTHelper.getInt(itemStack, "mode")].equals("WoodenBolt"))
				{
					world.playSoundAtEntity(entityPlayer, "afraidofthedark:crossbowFire", 0.5F, ((Item.itemRand.nextFloat() * 0.4F) + 0.8F));
					world.spawnEntityInWorld(new EntityWoodenBolt(world, entityPlayer));
				}
			}
		}
		// Else check to see if the player has the correct bolt in his/her inventory and fire
		else
		{
			if (!world.isRemote)
			{
				if (this.availableBolts[NBTHelper.getInt(itemStack, "mode")].equals("IronBolt") && entityPlayer.inventory.consumeInventoryItem(ModItems.ironBolt))
				{
					world.spawnEntityInWorld(new EntityIronBolt(world, entityPlayer));
					world.playSoundAtEntity(entityPlayer, "afraidofthedark:crossbowFire", 0.5F, ((Item.itemRand.nextFloat() * 0.4F) + 0.8F));
				}
				else if (this.availableBolts[NBTHelper.getInt(itemStack, "mode")].equals("SilverBolt") && entityPlayer.inventory.consumeInventoryItem(ModItems.silverBolt))
				{
					world.spawnEntityInWorld(new EntitySilverBolt(world, entityPlayer));
					world.playSoundAtEntity(entityPlayer, "afraidofthedark:crossbowFire", 0.5F, ((Item.itemRand.nextFloat() * 0.4F) + 0.8F));
				}
				else if (this.availableBolts[NBTHelper.getInt(itemStack, "mode")].equals("WoodenBolt") && entityPlayer.inventory.consumeInventoryItem(ModItems.woodenBolt))
				{
					world.playSoundAtEntity(entityPlayer, "afraidofthedark:crossbowFire", 0.5F, ((Item.itemRand.nextFloat() * 0.4F) + 0.8F));
					world.spawnEntityInWorld(new EntityWoodenBolt(world, entityPlayer));
				}
			}
		}
	}

	// To set the cocked value of the crossbow we change the NBT value
	public void setIsCocked(final ItemStack itemStack, final boolean isCocked, final EntityPlayer entityPlayer, final boolean clientSide)
	{
		NBTHelper.setBoolean(itemStack, "isCocked", isCocked);
		if (NBTHelper.getBoolean(itemStack, "isCocked"))
		{
			NBTHelper.setInteger(itemStack, "icon", 1);
		}
		else
		{
			NBTHelper.setInteger(itemStack, "icon", 0);
		}
		if (clientSide)
		{
			AfraidOfTheDark.getSimpleNetworkWrapper().sendToServer(new UpdateCrossbow(itemStack.getTagCompound()));
		}
		else
		{
			AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateCrossbow(itemStack.getTagCompound()), (EntityPlayerMP) entityPlayer);
		}
	}

	// A message under the bow will tell us what type of arrows the bow will fire
	@Override
	public void addInformation(final ItemStack itemStack, final EntityPlayer entityPlayer, final List list, final boolean bool)
	{
		list.add("Bow will fire: " + this.availableBolts[NBTHelper.getInt(itemStack, "mode")] + "s.");
	}

	// Initialize the item when it is created
	@Override
	public void onCreated(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		NBTHelper.setBoolean(itemStack, "isCocked", false);
		NBTHelper.setInteger(itemStack, "icon", 0);
		NBTHelper.setInteger(itemStack, "mode", 0);
		NBTHelper.setFloat(itemStack, "pullLevel", 0.0F);
	}

	// Load NBT data onto an itemstack
	public NBTTagCompound loadNBTData(final ItemStack itemStack)
	{
		NBTHelper.getBoolean(itemStack, "isCocked");
		NBTHelper.getInt(itemStack, "icon");
		NBTHelper.getInt(itemStack, "mode");
		return itemStack.getTagCompound();
	}

	// This item has a custom model, therefore it is full 3D
	@Override
	public boolean isFull3D()
	{
		return true;
	}

}
