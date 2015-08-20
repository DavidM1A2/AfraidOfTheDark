package com.DavidM1A2.AfraidOfTheDark.client.entities.Enaria;

import java.util.HashMap;

import com.DavidM1A2.AfraidOfTheDark.client.MCAClientLibrary.MCAModelRenderer;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.MCAVersionChecker;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.math.Matrix4f;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.math.Quaternion;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.EntityEnaria;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class ModelEnaria extends ModelBase
{
	public final int MCA_MIN_REQUESTED_VERSION = 5;
	public HashMap<String, MCAModelRenderer> parts = new HashMap<String, MCAModelRenderer>();

	MCAModelRenderer head;
	MCAModelRenderer body;
	MCAModelRenderer rightarm;
	MCAModelRenderer leftarm;
	MCAModelRenderer rightleg;
	MCAModelRenderer leftleg;

	public ModelEnaria()
	{
		MCAVersionChecker.checkForLibraryVersion(getClass(), MCA_MIN_REQUESTED_VERSION);

		textureWidth = 64;
		textureHeight = 64;

		head = new MCAModelRenderer(this, "head", 0, 0);
		head.mirror = false;
		head.addBox(-4.0F, 0.0F, -4.0F, 8, 8, 8);
		head.setInitialRotationPoint(0.0F, 2.0F, 2.0F);
		head.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		head.setTextureSize(64, 64);
		parts.put(head.boxName, head);

		body = new MCAModelRenderer(this, "body", 0, 16);
		body.mirror = false;
		body.addBox(-4.0F, -12.0F, -2.0F, 8, 12, 4);
		body.setInitialRotationPoint(0.0F, 2.0F, 2.0F);
		body.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		body.setTextureSize(64, 64);
		parts.put(body.boxName, body);

		rightarm = new MCAModelRenderer(this, "rightarm", 41, 16);
		rightarm.mirror = false;
		rightarm.addBox(-3.0F, -10.0F, -2.0F, 3, 12, 4);
		rightarm.setInitialRotationPoint(-4.0F, 0.0F, 2.0F);
		rightarm.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		rightarm.setTextureSize(64, 64);
		parts.put(rightarm.boxName, rightarm);

		leftarm = new MCAModelRenderer(this, "leftarm", 25, 16);
		leftarm.mirror = false;
		leftarm.addBox(0.0F, -10.0F, -2.0F, 3, 12, 4);
		leftarm.setInitialRotationPoint(4.0F, 0.0F, 2.0F);
		leftarm.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		leftarm.setTextureSize(64, 64);
		parts.put(leftarm.boxName, leftarm);

		rightleg = new MCAModelRenderer(this, "rightleg", 16, 32);
		rightleg.mirror = false;
		rightleg.addBox(-2.0F, -12.0F, -2.0F, 4, 12, 4);
		rightleg.setInitialRotationPoint(-2.0F, -10.0F, 2.0F);
		rightleg.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		rightleg.setTextureSize(64, 64);
		parts.put(rightleg.boxName, rightleg);

		leftleg = new MCAModelRenderer(this, "leftleg", 0, 32);
		leftleg.mirror = false;
		leftleg.addBox(-2.0F, -12.0F, -2.0F, 4, 12, 4);
		leftleg.setInitialRotationPoint(2.0F, -10.0F, 2.0F);
		leftleg.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		leftleg.setTextureSize(64, 64);
		parts.put(leftleg.boxName, leftleg);

	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		EntityEnaria entity = (EntityEnaria) par1Entity;

		AnimationHandler.performAnimationInModel(parts, entity);

		//Render every non-child part
		head.render(par7);
		body.render(par7);
		rightarm.render(par7);
		leftarm.render(par7);
		rightleg.render(par7);
		leftleg.render(par7);
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