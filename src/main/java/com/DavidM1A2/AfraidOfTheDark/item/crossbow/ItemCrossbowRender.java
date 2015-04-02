package com.DavidM1A2.AfraidOfTheDark.item.crossbow;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.utility.NBTHelper;

public class ItemCrossbowRender implements IItemRenderer
{
	protected CrossbowModel crossbowModel;

	public ItemCrossbowRender()
	{
		crossbowModel = new CrossbowModel();
		crossbowModel.pullBow(0.0F);
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		switch (type)
		{
			case EQUIPPED:
			{
				return true;
			}
			case EQUIPPED_FIRST_PERSON:
			{
				return true;
			}
			default:
			{
				return false;
			}
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data)
	{
		switch (type)
		{
			case EQUIPPED:
			{
				if (!(!((EntityPlayer) data[1] == Minecraft.getMinecraft().renderViewEntity && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 && !((Minecraft
						.getMinecraft().currentScreen instanceof GuiInventory || Minecraft.getMinecraft().currentScreen instanceof GuiContainerCreative) && RenderManager.instance.playerViewY == 180.0F))))
				{
					GL11.glPushMatrix();

					Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("afraidofthedark:textures/models/crossbowTexture.png"));

					float scaleB = 1.0F;

					GL11.glScalef(scaleB, scaleB, scaleB);

					GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F); // Pitch
					GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F); // Yaw
					GL11.glRotatef(270.0F, 0.0F, 0.0F, 1.0F); // Roll

					GL11.glTranslatef(0.2F, -1.0F, -0.2F);

					if (NBTHelper.getBoolean(itemStack, "isCocked"))
					{
						crossbowModel.Bolt.isHidden = false;
						crossbowModel.pullBow(1.0F);
					}
					else
					{
						crossbowModel.Bolt.isHidden = true;
						crossbowModel.pullBow(NBTHelper.getFloat(itemStack, "pullLevel"));
					}

					crossbowModel.render((Entity) data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

					GL11.glPopMatrix();
				}
			}
			case EQUIPPED_FIRST_PERSON:
			{

				GL11.glPushMatrix();

				Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("afraidofthedark:textures/models/crossbowTexture.png"));

				float scaleA = 1.0F;

				GL11.glScalef(scaleA, scaleA, scaleA);

				GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F); // Pitch
				GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F); // Yaw
				GL11.glRotatef(270.0F, 0.0F, 0.0F, 1.0F); // Roll

				GL11.glTranslatef(0.2F, -0.8F, -0.4F);

				if (NBTHelper.getBoolean(itemStack, "isCocked"))
				{
					crossbowModel.Bolt.isHidden = false;
					crossbowModel.pullBow(1.0F);
				}
				else
				{
					crossbowModel.Bolt.isHidden = true;
					crossbowModel.pullBow(NBTHelper.getFloat(itemStack, "pullLevel"));
				}

				crossbowModel.render((Entity) data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

				GL11.glPopMatrix();
			}
			default:
			{
				break;
			}
		}
	}

}
