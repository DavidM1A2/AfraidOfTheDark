/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItemWithCooldownPerItem;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;

import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFlaskOfSouls extends AOTDItemWithCooldownPerItem
{
	public static Map<String, Integer> flaskKillRequirements = new HashMap<String, Integer>();
	public static final String FLASK_TYPE = "flaskType";
	public static final String KILLS = "flaskKills";

	static
	{
		flaskKillRequirements.put("Bat", 5);
		flaskKillRequirements.put("Chicken", 5);
		flaskKillRequirements.put("Cow", 5);
		flaskKillRequirements.put("Mooshroom", 5);
		flaskKillRequirements.put("Pig", 5);
		flaskKillRequirements.put("Rabbit", 5);
		flaskKillRequirements.put("Sheep", 5);
		flaskKillRequirements.put("Squid", 5);
		flaskKillRequirements.put("Villager", 5);
		flaskKillRequirements.put("CaveSpider", 5);
		flaskKillRequirements.put("Enderman", 5);
		flaskKillRequirements.put("Spider", 5);
		flaskKillRequirements.put("PigZombie", 5);
		flaskKillRequirements.put("Blaze", 5);
		flaskKillRequirements.put("Creeper", 5);
		flaskKillRequirements.put("Guardian", 5);
		flaskKillRequirements.put("Endermite", 5);
		flaskKillRequirements.put("Ghast", 5);
		flaskKillRequirements.put("MagmaCube", 5);
		flaskKillRequirements.put("Silverfish", 5);
		flaskKillRequirements.put("Skeleton", 5);
		flaskKillRequirements.put("Slime", 5);
		flaskKillRequirements.put("Witch", 5);
		flaskKillRequirements.put("Zombie", 5);
		flaskKillRequirements.put("Horse", 5);
		flaskKillRequirements.put("Ocelot", 5);
		flaskKillRequirements.put("Wolf", 5);
		flaskKillRequirements.put("IronGolem", 5);
		flaskKillRequirements.put("Snowman", 5);
		flaskKillRequirements.put("Dragon", 5);
		flaskKillRequirements.put("Wither", 5);
		flaskKillRequirements.put("Werewolf", 5);
		flaskKillRequirements.put("DeeeSyft", 5);
	}

	public ItemFlaskOfSouls()
	{
		super();
		this.setUnlocalizedName("flaskOfSouls");
	}

	@Override
	public boolean onItemUse(final ItemStack itemStack, final EntityPlayer entityPlayer, final World world, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ)
	{
		final int blockX = pos.getX();
		final int blockY = pos.getY();
		final int blockZ = pos.getZ();
		// When we use the item, we check the block that was clicked on and spawn an entity on that block
		if (world.isRemote)
		{
			return true;
		}
		else
		{
			double y = 0.0D;

			if (world.getBlockState(pos).getBlock().isSolidFullCube())
			{
				y = 1.0D;
			}

			if (this.spawnEntity(world, blockX + 0.5D, blockY + y, blockZ + 0.5D, itemStack) == null)
			{
				if (!world.isRemote)
				{
					entityPlayer.addChatMessage(new ChatComponentText("Flask is incomplete or on cooldown."));
				}
			}

			return true;
		}
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		if (world.isRemote)
		{
			return itemStack;
		}
		else
		{
			final MovingObjectPosition movingObjectPosition = this.getMovingObjectPositionFromPlayer(world, entityPlayer, true);

			if (movingObjectPosition == null)
			{
				return itemStack;
			}
			else
			{
				if (movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
				{
					final BlockPos thisPos = entityPlayer.getPosition();

					if (!world.canMineBlockBody(entityPlayer, thisPos))
					{
						return itemStack;
					}

					if (world.getBlockState(thisPos) instanceof BlockLiquid)
					{
						if (this.spawnEntity(world, thisPos.getX(), thisPos.getY(), thisPos.getZ(), itemStack) == null)
						{
							if (!world.isRemote)
							{
								entityPlayer.addChatMessage(new ChatComponentText("Flask is incomplete or on cooldown."));
							}
						}
					}
				}
				return itemStack;
			}
		}
	}

	// To spawn the entity we get it's name and spawn one with a random rotation
	private Entity spawnEntity(final World world, final double x, final double y, final double z, ItemStack itemStack)
	{
		EntityLiving entityToSpawn = null;
		if (!world.isRemote)
		{
			if (ItemFlaskOfSouls.flaskKillRequirements.containsKey(NBTHelper.getString(itemStack, FLASK_TYPE)))
			{
				if (itemStack.getItemDamage() == 1)
				{
					if (!this.isOnCooldown(itemStack))
					{
						String entityToSpawnName = NBTHelper.getString(itemStack, FLASK_TYPE);
						String aotdEntity = Refrence.MOD_ID + "." + entityToSpawnName.substring(0, 1).toLowerCase() + entityToSpawnName.substring(1);
						if (EntityList.stringToClassMapping.containsKey(entityToSpawnName))
						{
							entityToSpawn = (EntityLiving) EntityList.createEntityByName(entityToSpawnName, world);
						}
						else if (EntityList.stringToClassMapping.containsKey(aotdEntity))
						{
							entityToSpawn = (EntityLiving) EntityList.createEntityByName(aotdEntity, world);
						}

						if (entityToSpawn != null)
						{
							entityToSpawn.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360), 0.0F);
							world.spawnEntityInWorld(entityToSpawn);
							entityToSpawn.playLivingSound();
						}
						this.setOnCooldown(itemStack);
					}
				}
			}
		}

		return entityToSpawn;
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 * 
	 * @param tooltip
	 *            All lines to display in the Item's tooltip. This is a List of Strings.
	 * @param advanced
	 *            Whether the setting "Advanced tooltips" is enabled
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List tooltip, boolean advanced)
	{
		if (NBTHelper.getString(itemStack, FLASK_TYPE).equals(""))
		{
			tooltip.add("Flask unbound.");
			tooltip.add("Hold this in your hotbar while killing a mob to bind this flask.");
		}
		else
		{
			tooltip.add("Entity bound to: " + NBTHelper.getString(itemStack, FLASK_TYPE));
			tooltip.add("Entity kills: " + NBTHelper.getInt(itemStack, KILLS) + "/" + ItemFlaskOfSouls.flaskKillRequirements.get(NBTHelper.getString(itemStack, FLASK_TYPE)));
		}
	}

	@Override
	/**
	 * Queries the percentage of the 'Durability' bar that should be drawn.
	 *
	 * @param stack
	 *            The current ItemStack
	 * @return 1.0 for 100% 0 for 0%
	 */
	public double getDurabilityForDisplay(ItemStack itemStack)
	{
		if (itemStack.getItemDamage() == 0)
		{
			if (NBTHelper.getString(itemStack, FLASK_TYPE).equals(""))
			{
				return 1;
			}
			else
			{
				return 1 - (double) NBTHelper.getInt(itemStack, KILLS) / (double) ItemFlaskOfSouls.flaskKillRequirements.get(NBTHelper.getString(itemStack, FLASK_TYPE));
			}
		}
		return super.getDurabilityForDisplay(itemStack);
	}

	@Override
	public int getItemCooldownInTicks()
	{
		return 100;
	}
}
