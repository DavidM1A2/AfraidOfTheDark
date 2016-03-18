/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods;

import com.DavidM1A2.AfraidOfTheDark.client.entities.spell.ModelSpellProjectile;
import com.DavidM1A2.AfraidOfTheDark.client.entities.spell.RenderSpell;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.EntitySpell;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.projectile.EntitySpellProjectile;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;

public enum DeliveryMethods
{
	Projectile(1, new ModelSpellProjectile(), "afraidofthedark:textures/entity/spell/projectile.png", EntitySpellProjectile.class);

	private int id;
	private ModelBase model;
	private String texture;
	private Class<? extends EntitySpell> deliveryEntity;

	private DeliveryMethods(int id, ModelBase model, String texture, Class<? extends EntitySpell> deliveryEntity)
	{
		this.model = model;
		this.texture = texture;
		this.deliveryEntity = deliveryEntity;
	}

	public <T extends EntitySpell> RenderSpell<T> getDeliveryRenderer(RenderManager renderManager)
	{
		return new RenderSpell<T>(renderManager, this.model, this.texture);
	}

	public Class<? extends EntitySpell> getDeliveryEntity()
	{
		return this.deliveryEntity;
	}

	public int getID()
	{
		return this.id;
	}
}
