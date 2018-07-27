/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

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

public class ItemCloakOfAgility extends AOTDItemWithCooldownStatic
{
	protected static double cooldown = 0;

	public ItemCloakOfAgility()
	{
		super();
		this.setUnlocalizedName("cloak_of_agility");
		this.setRegistryName("cloak_of_agility");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if (Minecraft.getMinecraft().player.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.CloakOfAgility))
		{
			tooltip.add("Use " + Keyboard.getKeyName(Keybindings.rollWithCloakOfAgility.getKeyCode()) + " to perform a roll in the current direction of movement.");
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
