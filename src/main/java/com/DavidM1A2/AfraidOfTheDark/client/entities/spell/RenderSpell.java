/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.spell;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.EntitySpell;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderSpell extends Render
{
	public final ResourceLocation SPELL_TEXTURE;
	public final ModelBase spellModel;

	public RenderSpell(ModelBase spellModel, String spellTexture)
	{
		super(Minecraft.getMinecraft().getRenderManager());
		this.spellModel = spellModel;
		this.SPELL_TEXTURE = new ResourceLocation(spellTexture);
	}

	@Override
	public void doRender(Entity _entity, double posX, double posY, double posZ, float var8, float var9)
	{
		EntitySpell entity = (EntitySpell) _entity;

		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glTranslatef((float) posX, (float) posY + entity.height / 2, (float) posZ);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.bindEntityTexture(entity);
		this.spellModel.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1)
	{
		return SPELL_TEXTURE;
	}
}