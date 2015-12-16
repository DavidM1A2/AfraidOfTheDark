/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener;
import com.DavidM1A2.AfraidOfTheDark.common.recipe.ConvertedRecipe;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class AOTDGuiRecipe extends AOTDGuiPanel
{
	private final AOTDGuiImage CRAFTING_GRID;
	private final AOTDGuiItemStack[] guiItemStacks;
	private final AOTDGuiItemStack output;
	private final AOTDGuiImage itemStackHighlight;

	public AOTDGuiRecipe(int x, int y, int width, int height, ConvertedRecipe recipe)
	{
		super(x, y, width, height, false);
		CRAFTING_GRID = new AOTDGuiImage(0, 0, width, height, "textures/gui/journalCrafting2.png");
		this.add(CRAFTING_GRID);
		itemStackHighlight = new AOTDGuiImage(0, 0, 0, 0, "textures/gui/slotHighlight.png");
		itemStackHighlight.setVisible(false);
		itemStackHighlight.addActionListener(new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, ActionType actionType)
			{
				if (actionType == actionType.MouseEnterBoundingBox)
					component.setVisible(true);
				else if (actionType == actionType.MouseExitBoundingBox)
					component.setVisible(false);
			}
		});
		this.add(itemStackHighlight);
		guiItemStacks = new AOTDGuiItemStack[9];

		AOTDActionListener onItemHover = new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, ActionType actionType)
			{
				if (actionType == ActionType.MouseHover)
				{
					if (component instanceof AOTDGuiItemStack)
					{
						if (component.isVisible() && AOTDGuiRecipe.this.isVisible())
						{
							ItemStack itemStack = ((AOTDGuiItemStack) component).getItemStack();
							if (itemStack != null)
							{
								itemStackHighlight.setX(component.getX());
								itemStackHighlight.setY(component.getY());
								itemStackHighlight.setWidth(component.getWidth());
								itemStackHighlight.setHeight(component.getHeight());
								Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(itemStack.getDisplayName() + " x" + itemStack.stackSize, component.getXScaled(), component.getYScaled() - 5, 0xFFFFFFFF);
							}
						}
					}
				}
			}
		};

		for (int i = 0; i < this.guiItemStacks.length; i++)
			guiItemStacks[i] = new AOTDGuiItemStack(5 + (i % 3) * 24, 6 + 26 * (i > 5 ? 2 : i > 2 ? 1 : 0), (int) (width / 5.0), (int) (height / 4.0), null);
		output = new AOTDGuiItemStack(83, 31, 24, 24, null);
		output.addActionListener(onItemHover);
		for (int i = 0; i < this.guiItemStacks.length; i++)
		{
			guiItemStacks[i].addActionListener(onItemHover);
			this.add(guiItemStacks[i]);
		}
		this.add(output);

		this.setRecipe(recipe);
	}

	@Override
	public void draw()
	{
		if (this.isVisible())
		{
			if (this.output.getItemStack() != null)
			{
				super.draw();
			}
		}
	}

	public void setRecipe(ConvertedRecipe recipe)
	{
		if (recipe == null)
		{
			this.output.setItemStack(null);
			this.setVisible(false);
			return;
		}

		this.setVisible(true);

		for (int i = 0; i < this.guiItemStacks.length; i++)
			guiItemStacks[i].setItemStack(null);

		if (recipe.getWidth() == -1)
		{
			for (int i = 0; i < recipe.getInput().length; i++)
				guiItemStacks[i].setItemStack(recipe.getInput()[i]);
		}
		else
		{
			for (int i = 0; i < recipe.getHeight(); i++)
				for (int j = 0; j < recipe.getWidth(); j++)
					guiItemStacks[i * 3 + j].setItemStack(recipe.getInput()[i * recipe.getWidth() + j]);
		}
		this.output.setItemStack(recipe.getOutput());
	}

	@Override
	public void setVisible(boolean isVisible)
	{
		super.setVisible(isVisible);
		this.itemStackHighlight.setVisible(false);
	}
}
