package com.DavidM1A2.AfraidOfTheDark.common.item.crossbow;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import com.DavidM1A2.AfraidOfTheDark.client.settings.Keybindings;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItemWithCooldown;

public class ItemWristCrossbow extends AOTDItemWithCooldown
{
	protected static double cooldown = 0;

	public ItemWristCrossbow()
	{
		// 1 bow per itemstack
		super();
		this.setUnlocalizedName("wristCrossbow");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack itemStack, final EntityPlayer entityPlayer, final List list, final boolean bool)
	{
		list.add("Use " + Keyboard.getKeyName(Keybindings.fireWristCrossbow.getKeyCode()) + " to fire a bolt in the current look direction.");
		list.add("Shift + " + Keyboard.getKeyName(Keybindings.fireWristCrossbow.getKeyCode()) + " to change bolt type.");
	}

	@Override
	public int getItemCooldownInTicks()
	{
		return 200;
	}
}
