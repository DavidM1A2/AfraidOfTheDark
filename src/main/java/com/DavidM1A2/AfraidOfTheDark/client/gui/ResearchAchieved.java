/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ResearchAchieved extends Gui
{
	private static final ResourceLocation achievementBg = new ResourceLocation("textures/gui/achievement/achievement_background.png");
	private final Minecraft mc;
	private int width;
	private int height;
	private String achievementTitle;
	private String achievementDescription;
	private ResearchTypes theType;
	private ResearchTypes nextType;
	private long notificationTime;
	private final RenderItem renderItem;
	private boolean permanentNotification;
	private ItemStack icon;
	private ItemStack nextIcon;
	private Thread previousResearchDisplayed;

	public ResearchAchieved(final Minecraft mc)
	{
		super();
		this.mc = mc;
		this.renderItem = mc.getRenderItem();
		this.previousResearchDisplayed = new Thread();
	}

	public void displayResearch(final ResearchTypes research, final ItemStack icon, final boolean queue)
	{
		this.achievementTitle = "New Research!";
		if (queue)
		{
			if (research != null)
			{
				this.achievementDescription = research.formattedString();
				this.notificationTime = Minecraft.getSystemTime();
				this.theType = research;
				this.permanentNotification = false;
				this.icon = icon;
				this.nextIcon = null;
				this.nextType = null;
			}
		}
		else if (!this.previousResearchDisplayed.isAlive())
		{
			this.achievementDescription = research.formattedString();
			this.notificationTime = Minecraft.getSystemTime();
			this.theType = research;
			this.permanentNotification = false;
			this.icon = icon;
			this.previousResearchDisplayed = new Thread()
			{
				@Override
				public void run()
				{
					try
					{
						Thread.sleep(4000);
						ResearchAchieved.this.displayResearch(ResearchAchieved.this.nextType, ResearchAchieved.this.nextIcon, true);
					}
					catch (final InterruptedException e)
					{
					}
				}
			};
			this.previousResearchDisplayed.start();
		}
		else
		{
			this.nextIcon = icon;
			this.nextType = research;
		}
	}

	private void updateResearchAchievedWindowScale()
	{
		GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		GlStateManager.matrixMode(5889);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(5888);
		GlStateManager.loadIdentity();
		this.width = this.mc.displayWidth;
		this.height = this.mc.displayHeight;
		final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
		this.width = scaledresolution.getScaledWidth();
		this.height = scaledresolution.getScaledHeight();
		GlStateManager.clear(256);
		GlStateManager.matrixMode(5889);
		GlStateManager.loadIdentity();
		GlStateManager.ortho(0.0D, this.width, this.height, 0.0D, 1000.0D, 3000.0D);
		GlStateManager.matrixMode(5888);
		GlStateManager.loadIdentity();
		GlStateManager.translate(0.0F, 0.0F, -2000.0F);
	}

	public void updateResearchAchievedWindow()
	{
		if ((this.theType != null) && (this.notificationTime != 0L) && (Minecraft.getMinecraft().player != null))
		{
			double d0 = (Minecraft.getSystemTime() - this.notificationTime) / 3000.0D;

			if (!this.permanentNotification)
			{
				if ((d0 < 0.0D) || (d0 > 1.0D))
				{
					this.notificationTime = 0L;
					return;
				}
			}
			else if (d0 > 0.5D)
			{
				d0 = 0.5D;
			}

			this.updateResearchAchievedWindowScale();
			GlStateManager.disableDepth();
			GlStateManager.depthMask(false);
			double d1 = d0 * 2.0D;

			if (d1 > 1.0D)
			{
				d1 = 2.0D - d1;
			}

			d1 *= 4.0D;
			d1 = 1.0D - d1;

			if (d1 < 0.0D)
			{
				d1 = 0.0D;
			}

			d1 *= d1;
			d1 *= d1;
			final int i = this.width - 160;
			final int j = 0 - (int) (d1 * 36.0D);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableTexture2D();
			this.mc.getTextureManager().bindTexture(ResearchAchieved.achievementBg);
			GlStateManager.disableLighting();
			this.drawTexturedModalRect(i, j, 96, 202, 160, 32);

			if (this.permanentNotification)
			{
				this.mc.fontRenderer.drawSplitString(this.achievementDescription, i + 30, j + 7, 120, -1);
			}
			else
			{
				this.mc.fontRenderer.drawString(this.achievementTitle, i + 30, j + 7, -256);
				this.mc.fontRenderer.drawString(this.achievementDescription, i + 30, j + 18, -1);
			}

			RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.enableRescaleNormal();
			GlStateManager.enableColorMaterial();
			GlStateManager.enableLighting();
			this.renderItem.renderItemAndEffectIntoGUI(this.icon, i + 8, j + 8);
			GlStateManager.disableLighting();
			GlStateManager.depthMask(true);
			GlStateManager.enableDepth();
		}
	}

	public void clearAchievements()
	{
		this.theType = null;
		this.notificationTime = 0L;
	}
}
