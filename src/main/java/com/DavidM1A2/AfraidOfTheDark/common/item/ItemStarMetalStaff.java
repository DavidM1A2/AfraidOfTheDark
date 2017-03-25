/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItemWithCooldownStatic;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.utility.UnitConverterUtility;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStarMetalStaff extends AOTDItemWithCooldownStatic
{
	private static final int MAX_TROLL_POLE_TIME_IN_TICKS = 60;

	public ItemStarMetalStaff()
	{
		super();
		this.setUnlocalizedName("star_metal_staff");
		this.setRegistryName("star_metal_staff");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer entityPlayer, EnumHand hand)
	{
		ItemStack itemStack = entityPlayer.getHeldItem(hand);
		if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.StarMetal))
		{
			if (!this.isOnCooldown(itemStack))
			{
				if (!entityPlayer.capabilities.isCreativeMode)
				{
					entityPlayer.capabilities.disableDamage = true;
					AfraidOfTheDark.instance.getDelayedExecutor().schedule(new Runnable()
					{
						@Override
						public void run()
						{
							entityPlayer.capabilities.disableDamage = false;
						}
					}, UnitConverterUtility.ticksToMilliseconds(MAX_TROLL_POLE_TIME_IN_TICKS), TimeUnit.MILLISECONDS);
				}
				entityPlayer.addVelocity(0, .5, 0);
				this.setOnCooldown(itemStack, entityPlayer);
				if (!world.isRemote)
					entityPlayer.setActiveHand(hand);
			}
			else
			{
				if (world.isRemote)
					if (entityPlayer.getActiveItemStack() != itemStack)
						entityPlayer.sendMessage(new TextComponentString("Cooldown remaining: " + this.cooldownRemaining(itemStack) + " second" + (this.cooldownRemaining(itemStack) - 1 == 0.0 ? "." : "s.")));
			}
		}
		else
		{
			if (!world.isRemote)
				entityPlayer.sendMessage(new TextComponentString("I'm not sure what this is used for."));
		}

		return ActionResult.<ItemStack>newResult(EnumActionResult.SUCCESS, itemStack);
	}

	/**
	 * Called each tick while using an item.
	 * 
	 * @param stack
	 *            The Item being used
	 * @param entityLivingBase
	 *            The Player using the item
	 * @param count
	 *            The amount of time in tick the item has been used for continuously
	 */
	@Override
	public void onUsingTick(final ItemStack stack, final EntityLivingBase entityLivingBase, int count)
	{
		if (entityLivingBase instanceof EntityPlayer)
		{
			EntityPlayer entityPlayer = (EntityPlayer) entityLivingBase;
			if (!entityPlayer.world.isRemote)
			{
				count = ItemStarMetalStaff.MAX_TROLL_POLE_TIME_IN_TICKS - count;
				if (count == 1)
					entityPlayer.fallDistance = 0.0f;
				if (count >= 3)
					((EntityPlayerMP) entityPlayer).connection.sendPacket(new SPacketEntityVelocity(entityPlayer.getEntityId(), 0, 0, 0));
				if (count == (ItemStarMetalStaff.MAX_TROLL_POLE_TIME_IN_TICKS - 1))
					entityPlayer.resetActiveHand();
			}
		}
	}

	/**
	 * Called when the player stops using an Item (stops holding the right mouse button).
	 *
	 * @param timeLeft
	 *            The amount of ticks left before the using would have been complete
	 */
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
	{
		if (entityLiving instanceof EntityPlayer)
		{
			EntityPlayer entityPlayer = (EntityPlayer) entityLiving;
			if (!entityPlayer.capabilities.isCreativeMode)
				entityPlayer.capabilities.disableDamage = false;
			if (timeLeft < 5)
			{
				List entityList = worldIn.getEntitiesWithinAABBExcludingEntity(entityPlayer, entityPlayer.getEntityBoundingBox().expand(10, 10, 10));
				for (Object entityObject : entityList)
				{
					if (entityObject instanceof EntityPlayer || entityObject instanceof EntityLiving)
					{
						EntityLivingBase entity = (EntityLivingBase) entityObject;

						double knockbackStrength = 2;

						double motionX = entityPlayer.getPosition().getX() - entity.getPosition().getX();
						double motionZ = entityPlayer.getPosition().getZ() - entity.getPosition().getZ();

						double hypotenuse = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);

						entity.addVelocity(-motionX * knockbackStrength * 0.6000000238418579D / hypotenuse, 0.1D, -motionZ * knockbackStrength * 0.6000000238418579D / hypotenuse);
					}
				}
			}
		}
	}

	@Override
	public int getMaxItemUseDuration(final ItemStack stack)
	{
		return ItemStarMetalStaff.MAX_TROLL_POLE_TIME_IN_TICKS;
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
	public void addInformation(final ItemStack stack, final EntityPlayer entityPlayer, final List tooltip, final boolean advanced)
	{
		if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.StarMetal))
		{
			tooltip.add("Right click for temporary invincibility");
			tooltip.add("followed by an AOE knockback.");
			tooltip.add("Cooldown: " + this.getItemCooldownInTicks() / 20 + " seconds.");
		}
		else
		{
			tooltip.add("I'm not sure how to use this.");
		}
	}

	@Override
	public int getItemCooldownInTicks()
	{
		return 240;
	}

	@Override
	public int getItemCooldownInTicks(ItemStack itemStack)
	{
		return 240;
	}
}
