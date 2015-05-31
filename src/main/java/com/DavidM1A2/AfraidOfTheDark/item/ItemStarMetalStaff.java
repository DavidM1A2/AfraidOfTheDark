/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.item;

import java.util.List;

import scala.collection.parallel.ParIterableLike.Forall;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.threads.Cooldown;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;

public class ItemStarMetalStaff extends AOTDItem implements IHasCooldown
{
	private static final int MAX_TROLL_POLE_TIME_IN_TICKS = 60;
	private Cooldown cooldown;

	public ItemStarMetalStaff()
	{
		super();
		this.setUnlocalizedName("starMetalStaff");
		this.cooldown = new Cooldown(this);
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		if (!this.cooldown.onCooldown())
		{
			if (!entityPlayer.capabilities.isCreativeMode)
			{
				entityPlayer.capabilities.disableDamage = true;
			}
			if (entityPlayer.worldObj.isRemote)
			{
				entityPlayer.addVelocity(0, .5,	0);
			}
			entityPlayer.setItemInUse(itemStack, ItemStarMetalStaff.MAX_TROLL_POLE_TIME_IN_TICKS);
		}
		else
		{
			if (entityPlayer.worldObj.isRemote)
			{
				entityPlayer.addChatMessage(new ChatComponentText("This item will be on cooldown for another " + (this.cooldown.getSecondsRemaining() + 1) + " second" + ((this.cooldown.getSecondsRemaining() == 0) ? "." : "s.")));
			}
		}

		return itemStack;
	}

	/**
	 * Called each tick while using an item.
	 * @param stack The Item being used
	 * @param player The Player using the item
	 * @param count The amount of time in tick the item has been used for continuously
	 */
	@Override
	public void onUsingTick(final ItemStack stack, final EntityPlayer player, int count)
	{
		count = ItemStarMetalStaff.MAX_TROLL_POLE_TIME_IN_TICKS - count;
		if (count >= 3)
		{
			if (player.worldObj.isRemote)
			{
				player.setVelocity(0, 0, 0);
			}
		}
		if (count == (ItemStarMetalStaff.MAX_TROLL_POLE_TIME_IN_TICKS - 1))
		{
			player.stopUsingItem();
		}
	}

	/**
	 * Called when the player stops using an Item (stops holding the right mouse button).
	 *
	 * @param timeLeft The amount of ticks left before the using would have been complete
	 */
	@Override
	public void onPlayerStoppedUsing(final ItemStack stack, final World worldIn, final EntityPlayer entityPlayer, final int timeLeft)
	{
		entityPlayer.fallDistance = 0.0f;
		if (!entityPlayer.capabilities.isCreativeMode)
		{
			entityPlayer.capabilities.disableDamage = false;
		}
		if (timeLeft < 5)
		{
			List entityList = worldIn.getEntitiesWithinAABBExcludingEntity(entityPlayer, entityPlayer.getEntityBoundingBox().expand(10, 10, 10));
			for(Object entityObject : entityList)
			{
				if (entityObject instanceof EntityPlayer || entityObject instanceof EntityLiving)
				{
					EntityLivingBase entity = (EntityLivingBase) entityObject;
					
					double knockbackStrength = 2;
					
					double motionX = entityPlayer.getPosition().getX() - entity.getPosition().getX();
					double motionZ = entityPlayer.getPosition().getZ() - entity.getPosition().getZ();
					
					double hypotenuse = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
										
					entity.addVelocity(-motionX * knockbackStrength * 0.6000000238418579D / hypotenuse, 0.1D, -motionZ * knockbackStrength * 0.6000000238418579D / hypotenuse);
				}
			}
		}
		this.cooldown = new Cooldown(this);
		this.cooldown.start();
	}

	@Override
	public int getMaxItemUseDuration(final ItemStack stack)
	{
		return ItemStarMetalStaff.MAX_TROLL_POLE_TIME_IN_TICKS;
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 *
	 * @param tooltip All lines to display in the Item's tooltip. This is a List of Strings.
	 * @param advanced Whether the setting "Advanced tooltips" is enabled
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List tooltip, final boolean advanced)
	{
		tooltip.add(this.cooldown.onCooldown() ? ("Cooldown remaining: " + (this.cooldown.getSecondsRemaining() + 1) + " second" + ((this.cooldown.getSecondsRemaining() == 0) ? "." : "s.")) : "Ready to Use");
	}

	@Override
	public void cooldownCallback() 
	{
		// Nothing right now
	}

	@Override
	public int getItemCooldownInMillis() 
	{
		return 20000;
	}
}
