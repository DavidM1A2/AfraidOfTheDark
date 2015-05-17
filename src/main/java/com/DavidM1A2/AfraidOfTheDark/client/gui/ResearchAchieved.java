package com.DavidM1A2.AfraidOfTheDark.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.DavidM1A2.AfraidOfTheDark.research.ResearchTypes;

public class ResearchAchieved extends Gui
{
	private static final ResourceLocation achievementBg = new ResourceLocation("textures/gui/achievement/achievement_background.png");
	private Minecraft mc;
	private int width;
	private int height;
	private String achievementTitle;
	private String achievementDescription;
	private ResearchTypes theType;
	private long notificationTime;
	private RenderItem renderItem;
	private boolean permanentNotification;
	private ItemStack icon;

	public ResearchAchieved(Minecraft mc)
	{
		super();
		this.mc = mc;
		this.renderItem = mc.getRenderItem();
	}

	public void displayResearch(ResearchTypes research, ItemStack icon)
	{
		this.achievementTitle = "New Research!";
		this.achievementDescription = research.toString();
		this.notificationTime = Minecraft.getSystemTime();
		this.theType = research;
		this.permanentNotification = false;
		this.icon = icon;
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
		ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
		this.width = scaledresolution.getScaledWidth();
		this.height = scaledresolution.getScaledHeight();
		GlStateManager.clear(256);
		GlStateManager.matrixMode(5889);
		GlStateManager.loadIdentity();
		GlStateManager.ortho(0.0D, (double) this.width, (double) this.height, 0.0D, 1000.0D, 3000.0D);
		GlStateManager.matrixMode(5888);
		GlStateManager.loadIdentity();
		GlStateManager.translate(0.0F, 0.0F, -2000.0F);
	}

	public void updateResearchAchievedWindow()
	{
		if (this.theType != null && this.notificationTime != 0L && Minecraft.getMinecraft().thePlayer != null)
		{
			double d0 = (double) (Minecraft.getSystemTime() - this.notificationTime) / 3000.0D;

			if (!this.permanentNotification)
			{
				if (d0 < 0.0D || d0 > 1.0D)
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
			int i = this.width - 160;
			int j = 0 - (int) (d1 * 36.0D);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.func_179098_w();
			this.mc.getTextureManager().bindTexture(achievementBg);
			GlStateManager.disableLighting();
			this.drawTexturedModalRect(i, j, 96, 202, 160, 32);

			if (this.permanentNotification)
			{
				this.mc.fontRendererObj.drawSplitString(this.achievementDescription, i + 30, j + 7, 120, -1);
			}
			else
			{
				this.mc.fontRendererObj.drawString(this.achievementTitle, i + 30, j + 7, -256);
				this.mc.fontRendererObj.drawString(this.achievementDescription, i + 30, j + 18, -1);
			}

			RenderHelper.enableGUIStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.enableRescaleNormal();
			GlStateManager.enableColorMaterial();
			GlStateManager.enableLighting();
			this.renderItem.func_180450_b(icon, i + 8, j + 8);
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
