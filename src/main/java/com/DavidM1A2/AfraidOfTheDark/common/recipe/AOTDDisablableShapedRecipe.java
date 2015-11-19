/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.recipe;

import java.lang.reflect.Field;

import com.DavidM1A2.AfraidOfTheDark.common.playerData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.google.common.base.Throwables;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class AOTDDisablableShapedRecipe extends ShapedOreRecipe
{
	private final ResearchTypes preRequisite;
	private static Field eventHandlerField;
	private static Field containerPlayerPlayerField;
	private static Field slotCraftingPlayerField;

	public AOTDDisablableShapedRecipe(ItemStack result, ResearchTypes preRequisite, Object... recipe)
	{
		super(result, recipe);
		this.preRequisite = preRequisite;
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	@Override
	public boolean matches(InventoryCrafting inventoryCrafting, World world)
	{
		boolean matches = super.matches(inventoryCrafting, world);
		EntityPlayer entityPlayer = findPlayer(inventoryCrafting);
		if (entityPlayer != null)
		{
			if (!AOTDPlayerData.get(entityPlayer).isResearched(preRequisite))
			{
				return false;
			}
			else if (matches)
			{
				entityPlayer.addChatMessage(new ChatComponentText("I'll need to do some more research before I can craft this."));
			}
		}

		return matches;
	}

	static
	{
		try
		{
			eventHandlerField = ReflectionHelper.findField(InventoryCrafting.class, "eventHandler");
			containerPlayerPlayerField = ReflectionHelper.findField(ContainerPlayer.class, "thePlayer");
			slotCraftingPlayerField = ReflectionHelper.findField(SlotCrafting.class, "thePlayer");
		}
		catch (Exception e)
		{
			eventHandlerField = ReflectionHelper.findField(InventoryCrafting.class, "field_70465_c");
			containerPlayerPlayerField = ReflectionHelper.findField(ContainerPlayer.class, "field_82862_h");
			slotCraftingPlayerField = ReflectionHelper.findField(SlotCrafting.class, "field_75238_b");
		}
	}

	private static EntityPlayer findPlayer(InventoryCrafting inv)
	{
		try
		{
			Container container = (Container) eventHandlerField.get(inv);
			if (container instanceof ContainerPlayer)
			{
				return (EntityPlayer) containerPlayerPlayerField.get(container);
			}
			else if (container instanceof ContainerWorkbench)
			{
				return (EntityPlayer) slotCraftingPlayerField.get(container.getSlot(0));
			}
			else
			{
				return null;
			}
		}
		catch (Exception e)
		{
			throw Throwables.propagate(e);
		}
	}
}
