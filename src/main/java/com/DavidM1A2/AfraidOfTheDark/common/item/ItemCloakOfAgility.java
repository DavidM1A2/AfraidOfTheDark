package com.DavidM1A2.AfraidOfTheDark.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import com.DavidM1A2.AfraidOfTheDark.client.settings.Keybindings;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItemWithCooldown;

public class ItemCloakOfAgility extends AOTDItemWithCooldown
{
	protected static double cooldown = 0;

	public ItemCloakOfAgility()
	{
		super();
		this.setUnlocalizedName("cloakOfAgility");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack itemStack, final EntityPlayer entityPlayer, final List list, final boolean bool)
	{
		list.add("Use " + Keyboard.getKeyName(Keybindings.rollWithCloakOfAgility.getKeyCode()) + " to perform a roll in the current direction of movement.");
	}

	@Override
	public int getItemCooldownInTicks()
	{
		return 160;
	}
}
