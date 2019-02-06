package com.DavidM1A2.afraidofthedark.client.tileEntity.voidChest;

import com.DavidM1A2.afraidofthedark.common.tileEntity.TileEntityVoidChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Class used to render the void chest. Most of this code is from 'TileEntityEnderChestRenderer'
 */
@SideOnly(Side.CLIENT)
public class TileEntityVoidChestRenderer extends TileEntitySpecialRenderer<TileEntityVoidChest>
{
	// The texture for the void chest
	private static final ResourceLocation VOID_CHEST_TEXTURE = new ResourceLocation("afraidofthedark:textures/blocks/void_chest/void_chest.png");
	// Create a chest model that we render our void chest with
	private final ModelChest modelChest = new ModelChest();

	/**
	 * Called to render the tile entity
	 *
	 * @param te The tile entity to render
	 * @param x The x position of the tile entity
	 * @param y The y position of the tile entity
	 * @param z The z position of the tile entity
	 * @param partialTicks How much time has passed since the last tick
	 * @param destroyStage How far the chest is destroyed
	 * @param alpha The alpha value to render with
	 */
	@Override
	public void render(TileEntityVoidChest te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		///
		/// All code below is from TileEntityEnderChestRender.class::render()
		///

		int i = 0;

		if (te.hasWorld())
		{
			i = te.getBlockMetadata();
		}

		if (destroyStage >= 0)
		{
			this.bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F, 4.0F, 1.0F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(5888);
		}
		else
		{
			this.bindTexture(VOID_CHEST_TEXTURE);
		}

		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
		GlStateManager.translate((float)x, (float)y + 1.0F, (float)z + 1.0F);
		GlStateManager.scale(1.0F, -1.0F, -1.0F);
		GlStateManager.translate(0.5F, 0.5F, 0.5F);
		int j = 0;

		if (i == 2)
		{
			j = 180;
		}

		if (i == 3)
		{
			j = 0;
		}

		if (i == 4)
		{
			j = 90;
		}

		if (i == 5)
		{
			j = -90;
		}

		GlStateManager.rotate((float)j, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(-0.5F, -0.5F, -0.5F);
		float f = te.getPreviousLidAngle() + (te.getLidAngle() - te.getPreviousLidAngle()) * partialTicks;
		f = 1.0F - f;
		f = 1.0F - f * f * f;
		this.modelChest.chestLid.rotateAngleX = -(f * ((float)Math.PI / 2F));
		this.modelChest.renderAll();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		if (destroyStage >= 0)
		{
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
	}
}
