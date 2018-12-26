/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.spell;

import java.util.HashMap;

import com.DavidM1A2.AfraidOfTheDark.client.MCAClientLibrary.MCAModelRenderer;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.MCAVersionChecker;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.math.Matrix4f;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.math.Quaternion;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.projectile.EntitySpellProjectile;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class ModelSpellProjectile extends ModelBase
{
	public final int MCA_MIN_REQUESTED_VERSION = 5;
	public HashMap<String, MCAModelRenderer> parts = new HashMap<String, MCAModelRenderer>();

	MCAModelRenderer center;
	MCAModelRenderer one;
	MCAModelRenderer two;
	MCAModelRenderer three;
	MCAModelRenderer four;
	MCAModelRenderer five;
	MCAModelRenderer six;

	public ModelSpellProjectile()
	{
		MCAVersionChecker.checkForLibraryVersion(getClass(), MCA_MIN_REQUESTED_VERSION);

		textureWidth = 32;
		textureHeight = 32;

		center = new MCAModelRenderer(this, "Center", 0, 10);
		center.mirror = false;
		center.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6);
		center.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		center.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		center.setTextureSize(32, 32);
		parts.put(center.boxName, center);

		one = new MCAModelRenderer(this, "one", 0, 0);
		one.mirror = false;
		one.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 4);
		one.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		one.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		one.setTextureSize(32, 32);
		parts.put(one.boxName, one);
		center.addChild(one);

		two = new MCAModelRenderer(this, "two", 0, 0);
		two.mirror = false;
		two.addBox(0.0F, -2.0F, -2.0F, 4, 4, 4);
		two.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		two.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		two.setTextureSize(32, 32);
		parts.put(two.boxName, two);
		center.addChild(two);

		three = new MCAModelRenderer(this, "three", 0, 0);
		three.mirror = false;
		three.addBox(-2.0F, -2.0F, -4.0F, 4, 4, 4);
		three.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		three.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		three.setTextureSize(32, 32);
		parts.put(three.boxName, three);
		center.addChild(three);

		four = new MCAModelRenderer(this, "four", 0, 0);
		four.mirror = false;
		four.addBox(-4.0F, -2.0F, -2.0F, 4, 4, 4);
		four.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		four.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		four.setTextureSize(32, 32);
		parts.put(four.boxName, four);
		center.addChild(four);

		five = new MCAModelRenderer(this, "five", 0, 0);
		five.mirror = false;
		five.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4);
		five.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		five.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		five.setTextureSize(32, 32);
		parts.put(five.boxName, five);
		center.addChild(five);

		six = new MCAModelRenderer(this, "six", 0, 0);
		six.mirror = false;
		six.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4);
		six.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		six.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		six.setTextureSize(32, 32);
		parts.put(six.boxName, six);
		center.addChild(six);

	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		EntitySpellProjectile entity = (EntitySpellProjectile) par1Entity;

		AnimationHandler.performAnimationInModel(parts, entity);

		//Render every non-child part
		center.render(par7);
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
	{
	}

	public MCAModelRenderer getModelRendererFromName(String name)
	{
		return parts.get(name);
	}
}