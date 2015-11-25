/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item.core;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AOTDChargableSword extends AOTDSword
{
	private final String itemName;

	public AOTDChargableSword(ToolMaterial material, String itemName)
	{
		super(material);
		this.itemName = itemName;
		this.setUnlocalizedName(itemName);
	}

	/**
	 * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise the damage on the stack.
	 * 
	 * @param target
	 *            The Entity being hit
	 * @param attacker
	 *            the attacking entity
	 */
	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker)
	{
		if (target instanceof EntityPlayer || target instanceof EntityLiving)
		{
			if (NBTHelper.getInt(itemStack, "charge") < 100)
			{
				NBTHelper.setInteger(itemStack, "charge", NBTHelper.getInt(itemStack, "charge") + this.percentChargePerAttack());
			}
			if (NBTHelper.getInt(itemStack, "charge") > 100)
			{
				NBTHelper.setInteger(itemStack, "charge", 100);
			}
		}
		return true;
	}

	/**
	 * Queries the percentage of the 'Durability' bar that should be drawn.
	 *
	 * @param stack
	 *            The current ItemStack
	 * @return 1.0 for 100% 0 for 0%
	 */
	@Override
	public double getDurabilityForDisplay(ItemStack itemStack)
	{
		return 1.0 - (double) NBTHelper.getInt(itemStack, "charge") / (double) 100;
	}

	@Override
	public boolean isDamageable()
	{
		return false;
	}

	@Override
	public boolean showDurabilityBar(ItemStack itemStack)
	{
		return true;
	}

	/**
	 * This used to be 'display damage' but its really just 'aux' data in the ItemStack, usually shares the same variable as damage.
	 * 
	 * @param stack
	 * @return
	 */
	@Override
	public int getMetadata(ItemStack itemStack)
	{
		return NBTHelper.getInt(itemStack, "charge") == 100 ? 1 : 0;
	}

	/**
	 * Player, Render pass, and item usage sensitive version of getIconIndex.
	 *
	 * @param stack
	 *            The item stack to get the icon for.
	 * @param player
	 *            The player holding the item
	 * @param useRemaining
	 *            The ticks remaining for the active item.
	 * @return Null to use default model, or a custom ModelResourceLocation for the stage of use.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining)
	{
		return stack.getMetadata() == 1 ? new ModelResourceLocation(Refrence.MOD_ID + ":" + this.itemName + "FullCharge", "inventory") : new ModelResourceLocation(Refrence.MOD_ID + ":" + this.itemName, "inventory");
	}

	/**
	 * Can't block with this sword
	 */
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.NONE;
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		if (!world.isRemote)
		{
			if (NBTHelper.getInt(itemStack, "charge") >= 100)
			{
				NBTHelper.setInteger(itemStack, "charge", 0);
				this.performChargeAttack(itemStack, world, entityPlayer);
			}
			else
			{
				entityPlayer.addChatMessage(new ChatComponentText("I'll need more energy to perform the ability."));
			}
		}

		return itemStack;
	}

	/**
	 * Does not check isDamagable and check if it cannot be stacked
	 */
	@Override
	public boolean isItemTool(ItemStack stack)
	{
		return this.getItemStackLimit(stack) == 1;
	}

	public abstract int percentChargePerAttack();

	public abstract void performChargeAttack(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer);
}
