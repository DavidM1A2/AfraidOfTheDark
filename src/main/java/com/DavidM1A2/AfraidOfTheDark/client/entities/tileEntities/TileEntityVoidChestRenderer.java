package com.DavidM1A2.AfraidOfTheDark.client.entities.tileEntities;

import com.DavidM1A2.AfraidOfTheDark.common.block.tileEntity.TileEntityVoidChest;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntityVoidChestRenderer extends TileEntitySpecialRenderer
{
	private static final ResourceLocation texturesNormal[] = new ResourceLocation[10];
	private ModelChest simpleChest = new ModelChest();
	private static int currentFrame = 0;
	private int ticksExpired = 0;

	public TileEntityVoidChestRenderer()
	{
		for (int i = 0; i < texturesNormal.length; i++)
		{
			texturesNormal[i] = new ResourceLocation("afraidofthedark:textures/blocks/voidChest/voidChest" + i + ".png");
		}
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float lidPosition, int damage)
	{
		if (tileEntity instanceof TileEntityVoidChest)
		{
			TileEntityVoidChest tileEntityVoidChest = (TileEntityVoidChest) tileEntity;

			int j;
			if (!tileEntity.hasWorldObj())
			{
				j = 0;
			}
			else
			{
				j = tileEntity.getBlockMetadata();
			}

			if (damage >= 0)
			{
				this.bindTexture(DESTROY_STAGES[damage]);
				GlStateManager.matrixMode(5890);
				GlStateManager.pushMatrix();
				GlStateManager.scale(4.0F, 4.0F, 1.0F);
				GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
				GlStateManager.matrixMode(5888);
			}
			else
			{
				this.bindTexture(this.texturesNormal[currentFrame]);
			}

			GlStateManager.pushMatrix();
			GlStateManager.enableRescaleNormal();

			if (damage < 0)
			{
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			}

			GlStateManager.translate((float) x, (float) y + 1.0F, (float) z + 1.0F);
			GlStateManager.scale(1.0F, -1.0F, -1.0F);
			GlStateManager.translate(0.5F, 0.5F, 0.5F);

			short short1 = 0;

			if (j == 2)
			{
				short1 = 180;
			}
			if (j == 3)
			{
				short1 = 0;
			}
			if (j == 4)
			{
				short1 = 90;
			}
			if (j == 5)
			{
				short1 = -90;
			}

			GlStateManager.rotate((float) short1, 0.0F, 1.0F, 0.0F);
			GlStateManager.translate(-0.5F, -0.5F, -0.5F);
			float f1 = tileEntityVoidChest.prevLidAngle + (tileEntityVoidChest.lidAngle - tileEntityVoidChest.prevLidAngle) * lidPosition;
			float f2;

			f1 = 1.0F - f1;
			f1 = 1.0F - f1 * f1 * f1;
			simpleChest.chestLid.rotateAngleX = -(f1 * (float) Math.PI / 2.0F);
			simpleChest.renderAll();
			GlStateManager.disableRescaleNormal();
			GlStateManager.popMatrix();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			if (damage >= 0)
			{
				GlStateManager.matrixMode(5890);
				GlStateManager.popMatrix();
				GlStateManager.matrixMode(5888);
			}
		}
	}

	public static void advanceFrame()
	{
		if (TileEntityVoidChestRenderer.currentFrame + 1 >= TileEntityVoidChestRenderer.texturesNormal.length)
		{
			TileEntityVoidChestRenderer.currentFrame = 0;
		}
		TileEntityVoidChestRenderer.currentFrame = TileEntityVoidChestRenderer.currentFrame + 1;
	}
}
