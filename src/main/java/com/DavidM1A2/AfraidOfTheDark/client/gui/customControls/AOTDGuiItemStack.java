/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

public class AOTDGuiItemStack extends AOTDGuiContainer
{
	private ItemStack itemStack;

	public AOTDGuiItemStack(int x, int y, int width, int height, ItemStack itemStack)
	{
		super(x, y, width, height);
		this.itemStack = itemStack;
	}

	/**
	 * Render an ItemStack. Args : stack, x, y, format
	 */
	@Override
	public void draw()
	{
		if (this.isVisible())
		{
			RenderHelper.enableGUIStandardItemLighting();
			GL11.glPushMatrix();

			GL11.glTranslated(this.getXScaled(), this.getYScaled(), 1.0);
			GL11.glScaled(this.getScaleX(), this.getScaleY(), 1.0D);
			GL11.glTranslated(3 - this.getXScaled(), 3 - this.getYScaled(), 1.0);

			RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
			renderItem.zLevel = 100.0F;
			FontRenderer font = null;
			if (itemStack != null)
				font = itemStack.getItem().getFontRenderer(itemStack);
			if (font == null)
				font = Minecraft.getMinecraft().fontRendererObj;
			renderItem.renderItemAndEffectIntoGUI(itemStack, this.getXScaled(), this.getYScaled());
			renderItem.renderItemOverlayIntoGUI(font, itemStack, this.getXScaled(), this.getYScaled(), null);
			renderItem.zLevel = 0.0F;

			GL11.glPopMatrix();
			RenderHelper.disableStandardItemLighting();
		}
	}

	public void setItemStack(ItemStack itemStack)
	{
		this.itemStack = itemStack;
	}

	public ItemStack getItemStack()
	{
		return this.itemStack;
	}
}
