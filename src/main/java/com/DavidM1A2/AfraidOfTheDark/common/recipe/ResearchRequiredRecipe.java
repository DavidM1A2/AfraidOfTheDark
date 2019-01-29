package com.DavidM1A2.afraidofthedark.common.recipe;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.registry.research.Research;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.codehaus.plexus.util.ExceptionUtils;

import java.lang.reflect.Field;

/**
 * Class representing a recipe that requires a research before it can be crafted
 */
public class ResearchRequiredRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
	// 3 reflection fields used in determining which players are actually crafting a given recipe
	private static Field eventHandlerField;
	private static Field containerPlayerPlayerField;
	private static Field slotCraftingPlayerField;

	// The pre-requisite research to this recipe
	private final Research preRequisite;
	// The base shaped ore recipe to be automatically created using existing minecraft code
	private final IRecipe baseRecipe;

	static
	{
		// If we're in the development client the 3 fields will have de-obsfucated names, grab those here. If we're in the actual
		// client they will have obsfucated names so grab those instead
		try
		{
			eventHandlerField = ReflectionHelper.findField(InventoryCrafting.class, "eventHandler");
			containerPlayerPlayerField = ReflectionHelper.findField(ContainerPlayer.class, "player");
			slotCraftingPlayerField = ReflectionHelper.findField(SlotCrafting.class, "player");
		}
		catch (Exception ignored)
		{
			eventHandlerField = ReflectionHelper.findField(InventoryCrafting.class, "field_70465_c");
			containerPlayerPlayerField = ReflectionHelper.findField(ContainerPlayer.class, "field_82862_h");
			slotCraftingPlayerField = ReflectionHelper.findField(SlotCrafting.class, "field_75238_b");
		}
	}

	/**
	 * Constructor just takes a recipe as base and a pre-requisite recipe to research
	 *
	 * @param baseRecipe The base recipe to start with
	 * @param preRequisite The pre-requisite research to be required to craft this recipe
	 */
	public ResearchRequiredRecipe(IRecipe baseRecipe, Research preRequisite)
	{
		this.preRequisite = preRequisite;
		this.baseRecipe = baseRecipe;
	}

	/**
	 * Used to check if a recipe matches current crafting inventory. Also checks if the player has the correct research
	 *
	 * @param inv The current inventory state
	 * @param worldIn The current world the object is being crafted in
	 * @return True if the crafting recipe matches, false if it does not
	 */
	public boolean matches(InventoryCrafting inv, World worldIn)
	{
		// Compute if the recipe matches first
		boolean matches = this.baseRecipe.matches(inv, worldIn);

		// Grab the player who did the crafting
		EntityPlayer craftingPlayer = ResearchRequiredRecipe.findPlayer(inv);
		// Ensure the player is non-null
		if (craftingPlayer != null)
		{
			// If the player does not have the research return false
			if (!craftingPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null).isResearched(this.preRequisite))
			{
				// Before returning false notify the player why the crafting failed if the recipe matched
				if (matches && !worldIn.isRemote)
				{
					craftingPlayer.sendMessage(new TextComponentString("I'll need to do some more research before I can craft this."));
				}
				return false;
			}
		}

		// The player has the research, so return true if the recipe matches or false otherwise
		return matches;
	}

	/**
	 * Finds the player that is crafting the recipe from the InventoryCrafting class using reflection
	 *
	 * @param inventoryCrafting The crafting container to break into
	 * @return The player who is crafting the recipe or null otherwise
	 */
	private static EntityPlayer findPlayer(InventoryCrafting inventoryCrafting)
	{
		try
		{
			// Attempt to grab the container the player is crafting in
			Container container = (Container) eventHandlerField.get(inventoryCrafting);
			// Test if the container is a 2x2 grid or 'ContainerPlayer', if so return the player from the 'player' field
			if (container instanceof ContainerPlayer)
			{
				return (EntityPlayer) containerPlayerPlayerField.get(container);
			}
			// Test if the container is a 3x3 grid or 'ContainerWorkbench', if so return the player from the 'player' field
			else if (container instanceof ContainerWorkbench)
			{
				return (EntityPlayer) slotCraftingPlayerField.get(container.getSlot(0));
			}
		}
		// If something goes wrong catch the exception and log it, then return null
		catch (IllegalAccessException e)
		{
			AfraidOfTheDark.INSTANCE.getLogger().error("Could not find the player crafting the recipe, error was: " + ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Returns an Item that is the result of this recipe
	 *
	 * @param inv The inventory crafting to work with
	 * @return The item that is returned when the recipe is correct
	 */
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		return this.baseRecipe.getCraftingResult(inv);
	}

	/**
	 * Used to determine if this recipe can fit in a grid of the given width/height
	 *
	 * @param width The width of the crafting grid
	 * @param height The height of the crafting rid
	 * @return True if the recipe fits in the grid, false otherwise
	 */
	public boolean canFit(int width, int height)
	{
		return this.baseRecipe.canFit(width, height);
	}

	/**
	 * @return The output of the recipe
	 */
	public ItemStack getRecipeOutput()
	{
		return this.baseRecipe.getRecipeOutput();
	}

	/**
	 * Returns the items that remain after the crafting is complete given the inventory crafting to start with
	 *
	 * @param inv The inventory to start with
	 * @return A list of items to return to the player after the items are consumed from the crafting grid
	 */
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		return this.baseRecipe.getRemainingItems(inv);
	}

	/**
	 * @return The ingredients required for the recipe
	 */
	public NonNullList<Ingredient> getIngredients()
	{
		return this.baseRecipe.getIngredients();
	}

	/**
	 * @return True if the recipe is dynamic, false otherwise
	 */
	public boolean isDynamic()
	{
		return this.baseRecipe.isDynamic();
	}

	/**
	 * @return The group the recipe belongs to
	 */
	public String getGroup()
	{
		return this.baseRecipe.getGroup();
	}
}
