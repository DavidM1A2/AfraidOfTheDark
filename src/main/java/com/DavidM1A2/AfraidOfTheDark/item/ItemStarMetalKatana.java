package com.DavidM1A2.AfraidOfTheDark.item;

import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

public class ItemStarMetalKatana extends AOTDSword implements IHasCooldown
{
	private static final int HITRANGE = 5;
	private int cooldownRemaining = 0;

	public ItemStarMetalKatana()
	{
		super(Refrence.starMetalTool);
		this.setUnlocalizedName("starMetalKatana");
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		if (!world.isRemote)
		{
			if (cooldownRemaining == 0)
			{
				List entityList = world.getEntitiesWithinAABBExcludingEntity(entityPlayer, entityPlayer.getEntityBoundingBox().expand(HITRANGE, HITRANGE, HITRANGE));
				for (Object entityObject : entityList)
				{
					if (entityObject instanceof EntityPlayer || entityObject instanceof EntityLiving)
					{
						EntityLivingBase entity = (EntityLivingBase) entityObject;
						double knockbackStrength = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180313_o.effectId, itemStack) + 2;

						double motionX = entityPlayer.getPosition().getX() - entity.getPosition().getX();
						double motionZ = entityPlayer.getPosition().getZ() - entity.getPosition().getZ();

						double hypotenuse = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);

						entity.attackEntityFrom(DamageSource.causePlayerDamage(entityPlayer), Refrence.starMetalTool.getDamageVsEntity() + 4.0F);
						entity.addVelocity(-motionX * knockbackStrength * 0.6000000238418579D / hypotenuse, 0.1D, -motionZ * knockbackStrength * 0.6000000238418579D / hypotenuse);
					}
				}
				this.cooldownRemaining = this.getItemCooldownInTicks();
			}
			else
			{
				entityPlayer.addChatMessage(new ChatComponentText(this.cooldownRemaining != 0 ? ("Cooldown remaining: " + (this.cooldownRemaining / 20 + 1) + " second" + (this.cooldownRemaining / 20 == 0.0 ? "." : "s.")) : "Ready to Use"));
			}
		}
		return super.onItemRightClick(itemStack, world, entityPlayer);
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
	public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List tooltip, final boolean advanced)
	{
		tooltip.add("Right click to use an AOE knockback and damage attack.");
		tooltip.add("Max cooldown: " + this.getItemCooldownInTicks() / 20 + " seconds.");
	}

	/**
	 * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and update it's contents.
	 */
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
	{
		if (this.cooldownRemaining > 0)
		{
			cooldownRemaining = cooldownRemaining - 1;
		}
	}

	@Override
	public int getItemCooldownInTicks()
	{
		return 200;
	}

}
