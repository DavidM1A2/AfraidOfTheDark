/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.afraidofthedark.client.entity.enchantedSkeleton;


import com.DavidM1A2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer;
import com.DavidM1A2.afraidofthedark.common.entity.enchantedSkeleton.EntityEnchantedSkeleton;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.math.Matrix4f;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Model class that defines the enchanted skeleton model
 */
public class ModelEnchantedSkeleton extends ModelBase
{
	// A map of part name to part
	private final Map<String, MCAModelRenderer> parts = new HashMap<>();

	// The different parts of the model
	private MCAModelRenderer body;

	/**
	 * Constructor initializes the model
	 */
	public ModelEnchantedSkeleton()
	{
		// Auto-generated from the MCAnimator software

		textureWidth = 64;
		textureHeight = 32;

		body = new MCAModelRenderer(this, "body", 16, 16);
		body.mirror = false;
		body.addBox(-4.0F, -12.0F, -2.0F, 8, 12, 4);
		body.setInitialRotationPoint(0.0F, 2.0F, 2.0F);
		body.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		body.setTextureSize(64, 32);
		parts.put(body.boxName, body);

		MCAModelRenderer head = new MCAModelRenderer(this, "head", 0, 0);
		head.mirror = false;
		head.addBox(-4.0F, 0.0F, -4.0F, 8, 8, 8);
		head.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
		head.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		head.setTextureSize(64, 32);
		parts.put(head.boxName, head);
		body.addChild(head);

		MCAModelRenderer rightarm = new MCAModelRenderer(this, "rightarm", 40, 16);
		rightarm.mirror = false;
		rightarm.addBox(-2.0F, -10.0F, -1.0F, 2, 12, 2);
		rightarm.setInitialRotationPoint(-4.0F, -2.0F, 0.0F);
		rightarm.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		rightarm.setTextureSize(64, 32);
		parts.put(rightarm.boxName, rightarm);
		body.addChild(rightarm);

		MCAModelRenderer leftarm = new MCAModelRenderer(this, "leftarm", 40, 16);
		leftarm.mirror = false;
		leftarm.addBox(0.0F, -10.0F, -1.0F, 2, 12, 2);
		leftarm.setInitialRotationPoint(4.0F, -2.0F, 0.0F);
		leftarm.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		leftarm.setTextureSize(64, 32);
		parts.put(leftarm.boxName, leftarm);
		body.addChild(leftarm);

		MCAModelRenderer rightleg = new MCAModelRenderer(this, "rightleg", 0, 16);
		rightleg.mirror = false;
		rightleg.addBox(-1.0F, -12.0F, -1.0F, 2, 12, 2);
		rightleg.setInitialRotationPoint(-2.0F, -12.0F, 0.0F);
		rightleg.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		rightleg.setTextureSize(64, 32);
		parts.put(rightleg.boxName, rightleg);
		body.addChild(rightleg);

		MCAModelRenderer leftleg = new MCAModelRenderer(this, "leftleg", 0, 16);
		leftleg.mirror = false;
		leftleg.addBox(-1.0F, -12.0F, -1.0F, 2, 12, 2);
		leftleg.setInitialRotationPoint(2.0F, -12.0F, 0.0F);
		leftleg.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		leftleg.setTextureSize(64, 32);
		parts.put(leftleg.boxName, leftleg);
		body.addChild(leftleg);

		MCAModelRenderer heart = new MCAModelRenderer(this, "heart", 48, 4);
		heart.mirror = false;
		heart.addBox(-1.5F, -2.0F, -1.0F, 3, 3, 2);
		heart.setInitialRotationPoint(0.0F, -3.0F, 0.0F);
		heart.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
		heart.setTextureSize(64, 32);
		parts.put(heart.boxName, heart);
		body.addChild(heart);
	}

	/**
	 * Called every game tick to render the skeleton model
	 *
	 * @param entityIn The entity to render, this must be an enchanted skeleton
	 * @param limbSwing ignored, used only by default MC
	 * @param limbSwingAmount ignored, used only by default MC
	 * @param ageInTicks ignored, used only by default MC
	 * @param netHeadYaw ignored, used only by default MC
	 * @param headPitch ignored, used only by default MC
	 * @param scale The scale to render the model at
	 */
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		// Case the entity to a skeleton model
		EntityEnchantedSkeleton entity = (EntityEnchantedSkeleton) entityIn;

		// Animate the model (moves all pieces from time t to
		AnimationHandler.performAnimationInModel(parts, entity);

		// Render the model
		body.render(scale);
	}
}