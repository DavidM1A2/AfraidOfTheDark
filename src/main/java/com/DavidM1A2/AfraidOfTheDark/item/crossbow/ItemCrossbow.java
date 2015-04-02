package com.DavidM1A2.AfraidOfTheDark.item.crossbow;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.EntityIronBolt;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.EntitySilverBolt;
import com.DavidM1A2.AfraidOfTheDark.entities.Bolts.EntityWoodenBolt;
import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.item.ItemBase;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateCrossbow;
import com.DavidM1A2.AfraidOfTheDark.utility.NBTHelper;

public class ItemCrossbow extends ItemBase
{
	private IIcon loaded;
	private IIcon unloaded;
	private final int RELOAD_TIME = 100;
	private final String[] availableBolts =
	{ "IronBolt", "SilverBolt", "WoodenBolt" };

	public ItemCrossbow()
	{
		super();
		this.setUnlocalizedName("crossbow");
		this.setMaxStackSize(1);
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack itemStack)
	{
		if (entityLiving instanceof EntityPlayer)
		{
			EntityPlayer entityPlayer = (EntityPlayer) entityLiving;

			if (NBTHelper.getBoolean(itemStack, "isCocked"))
			{
				if (!entityPlayer.worldObj.isRemote)
				{
					setIsCocked(itemStack, false, entityPlayer, false);
					fireBolt(entityPlayer, entityPlayer.getEntityWorld(), itemStack);
				}
			}
		}

		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		if (entityPlayer.isSneaking())
		{
			if (!world.isRemote)
			{
				changeMode(itemStack, entityPlayer);
			}
		}
		else
		{
			if (!NBTHelper.getBoolean(itemStack, "isCocked") && NBTHelper.getFloat(itemStack, "pullLevel") == 0.0F)
			{
				if (entityPlayer.capabilities.isCreativeMode)
				{
					entityPlayer.setItemInUse(itemStack, RELOAD_TIME);
				}
				else
				{
					if (availableBolts[NBTHelper.getInt(itemStack, "mode")].equals("IronBolt") && entityPlayer.inventory.hasItem(ModItems.ironBolt)
							|| availableBolts[NBTHelper.getInt(itemStack, "mode")].equals("WoodenBolt") && entityPlayer.inventory.hasItem(ModItems.woodenBolt)
							|| availableBolts[NBTHelper.getInt(itemStack, "mode")].equals("SilverBolt") && entityPlayer.inventory.hasItem(ModItems.silverBolt))
					{
						entityPlayer.setItemInUse(itemStack, RELOAD_TIME);
					}
					else
					{
						entityPlayer.addChatMessage(new ChatComponentText("You do not have any bolts of type: "
								+ availableBolts[NBTHelper.getInt(itemStack, "mode")]));
					}
				}
			}
		}
		return itemStack;
	}

	@Override
	public void onUsingTick(ItemStack itemStack, EntityPlayer entityPlayer, int count)
	{
		if (count == RELOAD_TIME - 1)
		{
			entityPlayer.playSound("afraidofthedark:crossbowLoad", 0.9F, (itemRand.nextFloat() * 0.8F + 1.2F));
		}
		if (count == RELOAD_TIME - 60)
		{
			if (entityPlayer.worldObj.isRemote
					&& !NBTHelper.getBoolean(itemStack, "isCocked")
					&& (entityPlayer.capabilities.isCreativeMode || entityPlayer.inventory.hasItem(ModItems.silverBolt)
							|| entityPlayer.inventory.hasItem(ModItems.ironBolt) || entityPlayer.inventory.hasItem(ModItems.woodenBolt)))
			{
				setIsCocked(itemStack, true, entityPlayer, true);
			}
		}
		if (count > RELOAD_TIME - 60)
		{
			NBTHelper.setFloat(itemStack, "pullLevel", ((float) (1.0F - (count - 40.0F) / 60.0F)));
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer entityPlayer, int count)
	{
		if (count > 40.0)
		{
			NBTHelper.setFloat(itemStack, "pullLevel", 0.0F);
		}
	}

	public void changeMode(ItemStack itemStack, EntityPlayer entityPlayer)
	{
		if (!NBTHelper.getBoolean(itemStack, "isCocked"))
		{
			if (NBTHelper.getInt(itemStack, "mode") < availableBolts.length - 1)
			{
				NBTHelper.setInteger(itemStack, "mode", NBTHelper.getInt(itemStack, "mode") + 1);
				entityPlayer.addChatMessage(new ChatComponentText("Crossbow will now fire " + availableBolts[NBTHelper.getInt(itemStack, "mode")] + "s!"));
			}
			else
			{
				NBTHelper.setInteger(itemStack, "mode", 0);
				entityPlayer.addChatMessage(new ChatComponentText("Crossbow will now fire " + availableBolts[NBTHelper.getInt(itemStack, "mode")] + "s!"));
			}
		}

	}

	public void fireBolt(EntityPlayer entityPlayer, World world, ItemStack itemStack)
	{
		NBTHelper.setFloat(itemStack, "pullLevel", (0.0F));
		if (entityPlayer.capabilities.isCreativeMode)
		{
			if (!world.isRemote)
			{
				if (availableBolts[NBTHelper.getInt(itemStack, "mode")].equals("IronBolt"))
				{
					world.playSoundAtEntity(entityPlayer, "afraidofthedark:crossbowFire", 0.5F, (itemRand.nextFloat() * 0.4F + 0.8F));
					world.spawnEntityInWorld(new EntityIronBolt(world, entityPlayer));
				}
				else if (availableBolts[NBTHelper.getInt(itemStack, "mode")].equals("SilverBolt"))
				{
					world.playSoundAtEntity(entityPlayer, "afraidofthedark:crossbowFire", 0.5F, (itemRand.nextFloat() * 0.4F + 0.8F));
					world.spawnEntityInWorld(new EntitySilverBolt(world, entityPlayer));
				}
				else if (availableBolts[NBTHelper.getInt(itemStack, "mode")].equals("WoodenBolt"))
				{
					world.playSoundAtEntity(entityPlayer, "afraidofthedark:crossbowFire", 0.5F, (itemRand.nextFloat() * 0.4F + 0.8F));
					world.spawnEntityInWorld(new EntityWoodenBolt(world, entityPlayer));
				}
			}
		}
		else
		{
			if (!world.isRemote)
			{
				if (availableBolts[NBTHelper.getInt(itemStack, "mode")].equals("IronBolt") && entityPlayer.inventory.consumeInventoryItem(ModItems.ironBolt))
				{
					world.spawnEntityInWorld(new EntityIronBolt(world, entityPlayer));
					world.playSoundAtEntity(entityPlayer, "afraidofthedark:crossbowFire", 0.5F, (itemRand.nextFloat() * 0.4F + 0.8F));
				}
				else if (availableBolts[NBTHelper.getInt(itemStack, "mode")].equals("SilverBolt")
						&& entityPlayer.inventory.consumeInventoryItem(ModItems.silverBolt))
				{
					world.spawnEntityInWorld(new EntitySilverBolt(world, entityPlayer));
					world.playSoundAtEntity(entityPlayer, "afraidofthedark:crossbowFire", 0.5F, (itemRand.nextFloat() * 0.4F + 0.8F));
				}
				else if (availableBolts[NBTHelper.getInt(itemStack, "mode")].equals("WoodenBolt")
						&& entityPlayer.inventory.consumeInventoryItem(ModItems.woodenBolt))
				{
					world.playSoundAtEntity(entityPlayer, "afraidofthedark:crossbowFire", 0.5F, (itemRand.nextFloat() * 0.4F + 0.8F));
					world.spawnEntityInWorld(new EntityWoodenBolt(world, entityPlayer));
				}
			}
		}
	}

	public void setIsCocked(ItemStack itemStack, boolean isCocked, EntityPlayer entityPlayer, boolean clientSide)
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
			AfraidOfTheDark.channelNew.sendToServer(new UpdateCrossbow(itemStack.getTagCompound()));
		}
		else
		{
			// CreatePacketServerSide.sendCrossbowDataToClient(itemStack,
			// entityPlayer.inventory.currentItem, entityPlayer);
		}
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean bool)
	{
		list.add("Bow will fire: " + availableBolts[NBTHelper.getInt(itemStack, "mode")] + "s.");
	}

	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		loaded = iconRegister.registerIcon("afraidofthedark:crossbow");
		unloaded = iconRegister.registerIcon("afraidofthedark:crossbowUnloaded");
	}

	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		NBTHelper.setBoolean(itemStack, "isCocked", false);
		NBTHelper.setInteger(itemStack, "icon", 0);
		NBTHelper.setInteger(itemStack, "mode", 0);
		NBTHelper.setFloat(itemStack, "pullLevel", 0.0F);
	}

	public NBTTagCompound loadNBTData(ItemStack itemStack)
	{
		NBTHelper.getBoolean(itemStack, "isCocked");
		NBTHelper.getInt(itemStack, "icon");
		NBTHelper.getInt(itemStack, "mode");
		return itemStack.stackTagCompound;
	}

	@Override
	public IIcon getIconIndex(ItemStack itemStack)
	{
		switch (NBTHelper.getInt(itemStack, "icon"))
		{
			case 1:
			{
				return loaded;
			}
			default:
			{
				return unloaded;
			}
		}
	}

	@Override
	public boolean isFull3D()
	{
		return true;
	}

}
