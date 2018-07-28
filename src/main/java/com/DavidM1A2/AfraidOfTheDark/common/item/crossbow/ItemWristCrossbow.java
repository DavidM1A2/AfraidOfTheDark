/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item.crossbow;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import com.DavidM1A2.AfraidOfTheDark.client.settings.Keybindings;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItemWithCooldownStatic;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemWristCrossbow extends AOTDItemWithCooldownStatic
{
	public ItemWristCrossbow()
	{
		// 1 bow per itemstack
		super();
		this.setUnlocalizedName("wrist_crossbow");
		this.setRegistryName("wrist_crossbow");
		this.setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if (Minecraft.getMinecraft().player.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.WristCrossbow))
		{
			tooltip.add("Use " + Keyboard.getKeyName(Keybindings.fireWristCrossbow.getKeyCode()) + " to fire a bolt in the current look direction.");
			tooltip.add("Shift + " + Keyboard.getKeyName(Keybindings.fireWristCrossbow.getKeyCode()) + " to change bolt type.");
		}
		else
		{
			tooltip.add("I'm not sure how to use this.");
		}
	}

	@Override
	public int getItemCooldownInTicks()
	{
		return 80;
	}

	@Override
	public int getItemCooldownInTicks(ItemStack itemStack)
	{
		return 80;
	}
}
