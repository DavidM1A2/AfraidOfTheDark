/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.entities.DeeeSyft;

import java.util.HashMap;

import com.DavidM1A2.AfraidOfTheDark.client.MCAClientLibrary.MCAModelRenderer;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.MCAVersionChecker;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.math.Matrix4f;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.math.Quaternion;
import com.DavidM1A2.AfraidOfTheDark.common.entities.DeeeSyft.EntityDeeeSyft;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class ModelDeeeSyft extends ModelBase
{
	public final int MCA_MIN_REQUESTED_VERSION = 5;
	public HashMap<String, MCAModelRenderer> parts = new HashMap<String, MCAModelRenderer>();

	MCAModelRenderer body;
	MCAModelRenderer body2;
	MCAModelRenderer body3;
	MCAModelRenderer body4;
	MCAModelRenderer body5;
	MCAModelRenderer body6;
	MCAModelRenderer tentacle2;
	MCAModelRenderer tentacle4;
	MCAModelRenderer tentacle1;
	MCAModelRenderer body1;
	MCAModelRenderer tentacle3;

	public ModelDeeeSyft()
	{
		MCAVersionChecker.checkForLibraryVersion(getClass(), MCA_MIN_REQUESTED_VERSION);

		textureWidth = 128;
		textureHeight = 128;

		body = new MCAModelRenderer(this, "Body", 0, 48);
		body.mirror = false;
		body.addBox(0.0F, 0.0F, 0.0F, 32, 32, 32);
		body.setInitialRotationPoint(0.0F, 50.0F, 0.0F);
		body.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		body.setTextureSize(128, 128);
		parts.put(body.boxName, body);

		body2 = new MCAModelRenderer(this, "body2", 0, 0);
		body2.mirror = false;
		body2.addBox(-12.0F, -12.0F, -4.0F, 24, 24, 24);
		body2.setInitialRotationPoint(16.0F, 16.0F, 0.0F);
		body2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		body2.setTextureSize(128, 128);
		parts.put(body2.boxName, body2);
		body.addChild(body2);

		body3 = new MCAModelRenderer(this, "body3", 0, 0);
		body3.mirror = false;
		body3.addBox(-12.0F, -4.0F, -12.0F, 24, 24, 24);
		body3.setInitialRotationPoint(16.0F, 0.0F, 16.0F);
		body3.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		body3.setTextureSize(128, 128);
		parts.put(body3.boxName, body3);
		body.addChild(body3);

		body4 = new MCAModelRenderer(this, "body4", 0, 0);
		body4.mirror = false;
		body4.addBox(-20.0F, -12.0F, -12.0F, 24, 24, 24);
		body4.setInitialRotationPoint(32.0F, 16.0F, 16.0F);
		body4.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		body4.setTextureSize(128, 128);
		parts.put(body4.boxName, body4);
		body.addChild(body4);

		body5 = new MCAModelRenderer(this, "body5", 0, 0);
		body5.mirror = false;
		body5.addBox(-12.0F, -20.0F, -12.0F, 24, 24, 24);
		body5.setInitialRotationPoint(16.0F, 32.0F, 16.0F);
		body5.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		body5.setTextureSize(128, 128);
		parts.put(body5.boxName, body5);
		body.addChild(body5);

		body6 = new MCAModelRenderer(this, "body6", 0, 0);
		body6.mirror = false;
		body6.addBox(-12.0F, -12.0F, -20.0F, 24, 24, 24);
		body6.setInitialRotationPoint(16.0F, 16.0F, 32.0F);
		body6.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		body6.setTextureSize(128, 128);
		parts.put(body6.boxName, body6);
		body.addChild(body6);

		tentacle2 = new MCAModelRenderer(this, "tentacle2", 96, 0);
		tentacle2.mirror = false;
		tentacle2.addBox(-2.0F, -36.0F, -2.0F, 4, 36, 4);
		tentacle2.setInitialRotationPoint(5.0F, 4.0F, 27.0F);
		tentacle2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		tentacle2.setTextureSize(128, 128);
		parts.put(tentacle2.boxName, tentacle2);
		body.addChild(tentacle2);

		tentacle4 = new MCAModelRenderer(this, "tentacle4", 96, 0);
		tentacle4.mirror = false;
		tentacle4.addBox(-2.0F, -36.0F, -2.0F, 4, 36, 4);
		tentacle4.setInitialRotationPoint(5.0F, 4.0F, 5.0F);
		tentacle4.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		tentacle4.setTextureSize(128, 128);
		parts.put(tentacle4.boxName, tentacle4);
		body.addChild(tentacle4);

		tentacle1 = new MCAModelRenderer(this, "tentacle1", 96, 0);
		tentacle1.mirror = false;
		tentacle1.addBox(-2.0F, -36.0F, -2.0F, 4, 36, 4);
		tentacle1.setInitialRotationPoint(27.0F, 4.0F, 27.0F);
		tentacle1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		tentacle1.setTextureSize(128, 128);
		parts.put(tentacle1.boxName, tentacle1);
		body.addChild(tentacle1);

		body1 = new MCAModelRenderer(this, "body1", 0, 0);
		body1.mirror = false;
		body1.addBox(-4.0F, -12.0F, -12.0F, 24, 24, 24);
		body1.setInitialRotationPoint(0.0F, 16.0F, 16.0F);
		body1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		body1.setTextureSize(128, 128);
		parts.put(body1.boxName, body1);
		body.addChild(body1);

		tentacle3 = new MCAModelRenderer(this, "tentacle3", 96, 0);
		tentacle3.mirror = false;
		tentacle3.addBox(-2.0F, -36.0F, -2.0F, 4, 36, 4);
		tentacle3.setInitialRotationPoint(27.0F, 4.0F, 5.0F);
		tentacle3.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		tentacle3.setTextureSize(128, 128);
		parts.put(tentacle3.boxName, tentacle3);
		body.addChild(tentacle3);

	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		EntityDeeeSyft entity = (EntityDeeeSyft) par1Entity;

		AnimationHandler.performAnimationInModel(parts, entity);

		//Render every non-child part
		body.render(par7);
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