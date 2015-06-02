/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.playerData.Vitae;
import com.DavidM1A2.AfraidOfTheDark.utility.NBTHelper;

public class ItemVitaeLantern extends AOTDItem
{
	private static final int MAX_VITAE = 100;

	public ItemVitaeLantern()
	{
		super();
		this.setUnlocalizedName("vitaeLantern");
	}

	// If the player is sneaking and right clicks, we change the mode.
	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		if (Vitae.get(entityPlayer) > 5 && NBTHelper.getInt(itemStack, "vitaeLevel") + 5 <= MAX_VITAE && !entityPlayer.isSneaking())
		{
			NBTHelper.setInteger(itemStack, "vitaeLevel", NBTHelper.getInt(itemStack, "vitaeLevel") + 5);
			Vitae.set(entityPlayer, Vitae.get(entityPlayer) - 5, FMLCommonHandler.instance().getEffectiveSide());
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
		tooltip.add("Stored Vitae: " + NBTHelper.getInt(stack, "vitaeLevel"));
	}
}
