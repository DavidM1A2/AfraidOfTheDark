package com.DavidM1A2.AfraidOfTheDark.common.recipe;

import java.lang.reflect.Field;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.DavidM1A2.AfraidOfTheDark.common.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.google.common.base.Throwables;

public class AOTDDisablableShapedRecipe extends ShapedOreRecipe
{
	private final ResearchTypes preRequisite;

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
		EntityPlayer entityPlayer = findPlayer(inventoryCrafting);
		if (entityPlayer != null)
		{
			if (!LoadResearchData.isResearched(entityPlayer, preRequisite))
			{
				return false;
			}
		}

		return super.matches(inventoryCrafting, world);
	}

	// TODO: SRG names for non-dev environment
	/*
	 * InventoryCrafting#eventHandler = field_70465_c
	 * ContainerPlayer#thePlayer = field_82862_h
	 * SlotCraftin#thePlayer = field_75238_b
	 */
	private static final Field eventHandlerField = ReflectionHelper.findField(InventoryCrafting.class, "eventHandler");
	private static final Field containerPlayerPlayerField = ReflectionHelper.findField(ContainerPlayer.class, "thePlayer");
	private static final Field slotCraftingPlayerField = ReflectionHelper.findField(SlotCrafting.class, "thePlayer");

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
