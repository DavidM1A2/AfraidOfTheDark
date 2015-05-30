/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.item.crossbow;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.utility.NBTHelper;

// The renderer for the crossbow uses OpenGL to render the bow itself
public class ItemCrossbowRender implements IItemRenderer
{
	protected CrossbowModel crossbowModel;

	public ItemCrossbowRender()
	{
		this.crossbowModel = new CrossbowModel();
		this.crossbowModel.pullBow(0.0F);
	}

	// If it is equipped or first person equipped render this item
	@Override
	public boolean handleRenderType(final ItemStack item, final ItemRenderType type)
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

	// ?
	@Override
	public boolean shouldUseRenderHelper(final ItemRenderType type, final ItemStack item, final ItemRendererHelper helper)
	{
		return false;
	}

	// Use open GL matrices to render the item
	@Override
	public void renderItem(final ItemRenderType type, final ItemStack itemStack, final Object... data)
	{
		switch (type)
		{
		// If it is equipped (3rd person) render it one way
		case EQUIPPED:
		{
			// (3rd person check)
			if (!(!(((EntityPlayer) data[1] == Minecraft.getMinecraft().getRenderViewEntity()) && (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) && !(((Minecraft.getMinecraft().currentScreen instanceof GuiInventory) || (Minecraft.getMinecraft().currentScreen instanceof GuiContainerCreative))))))
			{
				GL11.glPushMatrix();

				// Get the texture
				Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("afraidofthedark:textures/models/crossbowTexture.png"));

				final float scale = 1.0F;

				// No scaling needed
				GL11.glScalef(scale, scale, scale);

				// Using an identity matrix we can set the pitch, yaw, and roll of the object
				GL11.glRotatef(90.0F, scale, 0.0F, 0.0F); // Pitch
				GL11.glRotatef(-50.0F, 0.0F, scale, 0.0F); // Yaw
				GL11.glRotatef(270.0F, 0.0F, 0.0F, scale); // Roll

				// Finally we translate the matrix
				GL11.glTranslatef(0.2F, -1.0F, -0.2F);

				// If it is cocked, we set the model accordingly
				if (NBTHelper.getBoolean(itemStack, "isCocked"))
				{
					this.crossbowModel.Bolt.isHidden = false;
					this.crossbowModel.pullBow(1.0F);
				}
				else
				{
					this.crossbowModel.Bolt.isHidden = true;
					this.crossbowModel.pullBow(NBTHelper.getFloat(itemStack, "pullLevel"));
				}

				// Call the render function and pop the matrix off of the stack enabling the renderer
				this.crossbowModel.render((Entity) data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

				GL11.glPopMatrix();
			}
		}
		case EQUIPPED_FIRST_PERSON:
		{

			// First person works the same as third except this time we have different dimensions for scale, yaw, pitch, roll, and translation
			GL11.glPushMatrix();

			Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("afraidofthedark:textures/models/crossbowTexture.png"));

			final float scaleA = 1.0F;

			GL11.glScalef(scaleA, scaleA, scaleA);

			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F); // Pitch
			GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F); // Yaw
			GL11.glRotatef(270.0F, 0.0F, 0.0F, 1.0F); // Roll

			GL11.glTranslatef(0.2F, -0.8F, -0.4F);

			// Change the model if is cocked
			if (NBTHelper.getBoolean(itemStack, "isCocked"))
			{
				this.crossbowModel.Bolt.isHidden = false;
				this.crossbowModel.pullBow(1.0F);
			}
			else
			{
				this.crossbowModel.Bolt.isHidden = true;
				this.crossbowModel.pullBow(NBTHelper.getFloat(itemStack, "pullLevel"));
			}

			this.crossbowModel.render((Entity) data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

			GL11.glPopMatrix();
		}
		default:
		{
			break;
		}
		}
	}

}
