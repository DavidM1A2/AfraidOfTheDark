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
import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponentEnum;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;

public enum DeliveryMethods implements ISpellComponentEnum
{
	Projectile(1, EntitySpellProjectile.class, Projectile.class, new ModelSpellProjectile(), "afraidofthedark:textures/gui/spellCrafting/deliveryMethods/projectile.png", "afraidofthedark:textures/entity/spell/projectile.png");

	private int id;
	private String iconTexture;
	private ModelBase model;
	private String texture;
	private Class<? extends EntitySpell> deliveryEntity;
	private Class<? extends DeliveryMethod> deliveryClass;

	private DeliveryMethods(int id, Class<? extends EntitySpell> deliveryEntity, Class<? extends DeliveryMethod> deliveryClass, ModelBase model, String iconTexture, String texture)
	{
		this.id = id;
		this.iconTexture = iconTexture;
		this.model = model;
		this.texture = texture;
		this.deliveryEntity = deliveryEntity;
		this.deliveryClass = deliveryClass;
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

	@Override
	public String getIcon()
	{
		return this.iconTexture;
	}

	@Override
	public String getName()
	{
		return this.toString();
	}

	@Override
	public DeliveryMethod newInstance()
	{
		try
		{
			return this.deliveryClass.newInstance();
		}
		catch (InstantiationException e)
		{
			LogHelper.error("Failed to create a new instance of a delivery method...");
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			LogHelper.error("Failed to create a new instance of a delivery method...");
			e.printStackTrace();
		}
		return null;
	}
}
