/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.utility.recipe;

import java.lang.reflect.Field;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;
import com.google.common.base.Throwables;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
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
		EntityPlayer entityPlayer = RecipeUtility.getRecipeCrafter(inventoryCrafting);
		if (entityPlayer != null)
		{
			if (!entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(preRequisite))
			{
				if (matches)
				{
					if (!entityPlayer.world.isRemote)
					{
						entityPlayer.sendMessage(new TextComponentString("I'll need to do some more research before I can craft this."));
					}
				}
				return false;
			}
		}

		return matches;
	}
}
