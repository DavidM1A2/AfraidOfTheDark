package com.DavidM1A2.AfraidOfTheDark.client.entities.SplinterDrone;

import java.util.HashMap;

import com.DavidM1A2.AfraidOfTheDark.client.MCAClientLibrary.MCAModelRenderer;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.MCAVersionChecker;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.math.Matrix4f;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.math.Quaternion;
import com.DavidM1A2.AfraidOfTheDark.common.entities.SplinterDrone.EntitySplinterDroneProjectile;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class ModelSplinterDroneProjectile extends ModelBase
{
	public final int MCA_MIN_REQUESTED_VERSION = 5;
	public HashMap<String, MCAModelRenderer> parts = new HashMap<String, MCAModelRenderer>();

	MCAModelRenderer body;
	MCAModelRenderer part1;
	MCAModelRenderer part2;
	MCAModelRenderer part3;
	MCAModelRenderer part4;
	MCAModelRenderer part5;
	MCAModelRenderer part6;

	public ModelSplinterDroneProjectile()
	{
		MCAVersionChecker.checkForLibraryVersion(getClass(), MCA_MIN_REQUESTED_VERSION);

		textureWidth = 32;
		textureHeight = 32;

		body = new MCAModelRenderer(this, "body", 0, 0);
		body.mirror = false;
		body.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6);
		body.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		body.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		body.setTextureSize(32, 32);
		parts.put(body.boxName, body);

		part1 = new MCAModelRenderer(this, "part1", 0, 16);
		part1.mirror = false;
		part1.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 4);
		part1.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		part1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		part1.setTextureSize(32, 32);
		parts.put(part1.boxName, part1);
		body.addChild(part1);

		part2 = new MCAModelRenderer(this, "part2", 0, 16);
		part2.mirror = false;
		part2.addBox(0.0F, -2.0F, -2.0F, 4, 4, 4);
		part2.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		part2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		part2.setTextureSize(32, 32);
		parts.put(part2.boxName, part2);
		body.addChild(part2);

		part3 = new MCAModelRenderer(this, "part3", 0, 16);
		part3.mirror = false;
		part3.addBox(-2.0F, -2.0F, -4.0F, 4, 4, 4);
		part3.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		part3.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		part3.setTextureSize(32, 32);
		parts.put(part3.boxName, part3);
		body.addChild(part3);

		part4 = new MCAModelRenderer(this, "part4", 0, 16);
		part4.mirror = false;
		part4.addBox(-4.0F, -2.0F, -2.0F, 4, 4, 4);
		part4.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		part4.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		part4.setTextureSize(32, 32);
		parts.put(part4.boxName, part4);
		body.addChild(part4);

		part5 = new MCAModelRenderer(this, "part5", 0, 16);
		part5.mirror = false;
		part5.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4);
		part5.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		part5.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		part5.setTextureSize(32, 32);
		parts.put(part5.boxName, part5);
		body.addChild(part5);

		part6 = new MCAModelRenderer(this, "part6", 0, 16);
		part6.mirror = false;
		part6.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4);
		part6.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		part6.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		part6.setTextureSize(32, 32);
		parts.put(part6.boxName, part6);
		body.addChild(part6);
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		EntitySplinterDroneProjectile entity = (EntitySplinterDroneProjectile) par1Entity;

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