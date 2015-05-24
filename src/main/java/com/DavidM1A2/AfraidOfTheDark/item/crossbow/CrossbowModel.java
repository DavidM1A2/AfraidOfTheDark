/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.item.crossbow;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

// Model class for the crossbow made by Michael Albertson
public class CrossbowModel extends ModelBase
{
	// fields
	ModelRenderer Body;
	ModelRenderer Stock;
	ModelRenderer Front;
	ModelRenderer LeftArm;
	ModelRenderer RightArm;
	ModelRenderer FrontAccent;
	ModelRenderer R1;
	ModelRenderer R2;
	ModelRenderer R3;
	ModelRenderer R4;
	ModelRenderer R5;
	ModelRenderer R6;
	ModelRenderer R7;
	ModelRenderer R8;
	ModelRenderer R9;
	ModelRenderer Grip;
	ModelRenderer L4;
	ModelRenderer L5;
	ModelRenderer L6;
	ModelRenderer L7;
	ModelRenderer L8;
	ModelRenderer L9;
	ModelRenderer L3;
	ModelRenderer L2;
	ModelRenderer L1;
	ModelRenderer Bolt;

	private static final float OFFSET1 = 0.0F;
	private static final float OFFSET2 = 0.06F;
	private static final float OFFSET3 = 0.12F;
	private static final float OFFSET4 = 0.18F;
	private static final float OFFSET5 = 0.24F;
	private static final float OFFSET6 = 0.30F;
	private static final float OFFSET7 = 0.36F;
	private static final float OFFSET8 = 0.42F;
	private static final float OFFSET9 = 0.48F;

	public CrossbowModel()
	{
		textureWidth = 64;
		textureHeight = 32;

		Body = new ModelRenderer(this, 0, 0);
		Body.addBox(0F, 0F, 0F, 2, 2, 10);
		Body.setRotationPoint(-1F, 14F, -8F);
		Body.setTextureSize(64, 32);
		Body.mirror = true;
		setRotation(Body, 0F, 0F, 0F);
		Stock = new ModelRenderer(this, 0, 0);
		Stock.addBox(0F, 0F, 0F, 2, 1, 5);
		Stock.setRotationPoint(-1F, 14F, 2F);
		Stock.setTextureSize(64, 32);
		Stock.mirror = true;
		setRotation(Stock, 0F, 0F, 0F);
		Front = new ModelRenderer(this, 0, 0);
		Front.addBox(0F, 0F, 0F, 10, 1, 1);
		Front.setRotationPoint(-5F, 14F, -9F);
		Front.setTextureSize(64, 32);
		Front.mirror = true;
		setRotation(Front, 0F, 0F, 0F);
		LeftArm = new ModelRenderer(this, 0, 0);
		LeftArm.addBox(0F, 0F, 0F, 5, 1, 1);
		LeftArm.setRotationPoint(5F, 14F, -9F);
		LeftArm.setTextureSize(64, 32);
		LeftArm.mirror = true;
		setRotation(LeftArm, 0F, -0.5235988F, 0F);
		RightArm = new ModelRenderer(this, 0, 0);
		RightArm.addBox(-5F, 0F, 0F, 5, 1, 1);
		RightArm.setRotationPoint(-5F, 14F, -9F);
		RightArm.setTextureSize(64, 32);
		RightArm.mirror = true;
		setRotation(RightArm, 0F, 0.5235988F, 0F);
		FrontAccent = new ModelRenderer(this, 10, 15);
		FrontAccent.addBox(0F, 0F, 0F, 8, 1, 1);
		FrontAccent.setRotationPoint(-4F, 14F, -10F);
		FrontAccent.setTextureSize(64, 32);
		FrontAccent.mirror = true;
		setRotation(FrontAccent, 0F, 0F, 0F);
		R1 = new ModelRenderer(this, 5, 15);
		R1.addBox(0F, 0F, 0F, 1, 1, 1);
		R1.setRotationPoint(-9F, 13.5F, -6F);
		R1.setTextureSize(64, 32);
		R1.mirror = true;
		setRotation(R1, 0F, 0F, 0F);
		R2 = new ModelRenderer(this, 5, 15);
		R2.addBox(0F, 0F, 0F, 1, 1, 1);
		R2.setRotationPoint(-8F, 13.5F, -6F);
		R2.setTextureSize(64, 32);
		R2.mirror = true;
		setRotation(R2, 0F, 0F, 0F);
		R3 = new ModelRenderer(this, 5, 15);
		R3.addBox(0F, 0F, 0F, 1, 1, 1);
		R3.setRotationPoint(-7F, 13.5F, -6F);
		R3.setTextureSize(64, 32);
		R3.mirror = true;
		setRotation(R3, 0F, 0F, 0F);
		R4 = new ModelRenderer(this, 5, 15);
		R4.addBox(0F, 0F, 0F, 1, 1, 1);
		R4.setRotationPoint(-6F, 13.5F, -6F);
		R4.setTextureSize(64, 32);
		R4.mirror = true;
		setRotation(R4, 0F, 0F, 0F);
		R5 = new ModelRenderer(this, 5, 15);
		R5.addBox(0F, 0F, 0F, 1, 1, 1);
		R5.setRotationPoint(-5F, 13.5F, -6F);
		R5.setTextureSize(64, 32);
		R5.mirror = true;
		setRotation(R5, 0F, 0F, 0F);
		R6 = new ModelRenderer(this, 5, 15);
		R6.addBox(0F, 0F, 0F, 1, 1, 1);
		R6.setRotationPoint(-4F, 13.5F, -6F);
		R6.setTextureSize(64, 32);
		R6.mirror = true;
		setRotation(R6, 0F, 0F, 0F);
		R7 = new ModelRenderer(this, 5, 15);
		R7.addBox(0F, 0F, 0F, 1, 1, 1);
		R7.setRotationPoint(-3F, 13.5F, -6F);
		R7.setTextureSize(64, 32);
		R7.mirror = true;
		setRotation(R7, 0F, 0F, 0F);
		R8 = new ModelRenderer(this, 5, 15);
		R8.addBox(0F, 0F, 0F, 1, 1, 1);
		R8.setRotationPoint(-2F, 13.5F, -6F);
		R8.setTextureSize(64, 32);
		R8.mirror = true;
		setRotation(R8, 0F, 0F, 0F);
		R9 = new ModelRenderer(this, 5, 15);
		R9.addBox(0F, 0F, 0F, 1, 1, 1);
		R9.setRotationPoint(-1F, 13.5F, -6F);
		R9.setTextureSize(64, 32);
		R9.mirror = true;
		setRotation(R9, 0F, 0F, 0F);
		Grip = new ModelRenderer(this, 0, 0);
		Grip.addBox(0F, 0F, 0F, 1, 2, 1);
		Grip.setRotationPoint(-0.5F, 16F, -7F);
		Grip.setTextureSize(64, 32);
		Grip.mirror = true;
		setRotation(Grip, 0F, 0F, 0F);
		L4 = new ModelRenderer(this, 5, 15);
		L4.addBox(0F, 0F, 0F, 1, 1, 1);
		L4.setRotationPoint(5F, 13.5F, -6F);
		L4.setTextureSize(64, 32);
		L4.mirror = true;
		setRotation(L4, 0F, 0F, 0F);
		L5 = new ModelRenderer(this, 5, 15);
		L5.addBox(0F, 0F, 0F, 1, 1, 1);
		L5.setRotationPoint(4F, 13.5F, -6F);
		L5.setTextureSize(64, 32);
		L5.mirror = true;
		setRotation(L5, 0F, 0F, 0F);
		L6 = new ModelRenderer(this, 5, 15);
		L6.addBox(0F, 0F, 0F, 1, 1, 1);
		L6.setRotationPoint(3F, 13.5F, -6F);
		L6.setTextureSize(64, 32);
		L6.mirror = true;
		setRotation(L6, 0F, 0F, 0F);
		L7 = new ModelRenderer(this, 5, 15);
		L7.addBox(0F, 0F, 0F, 1, 1, 1);
		L7.setRotationPoint(2F, 13.5F, -6F);
		L7.setTextureSize(64, 32);
		L7.mirror = true;
		setRotation(L7, 0F, 0F, 0F);
		L8 = new ModelRenderer(this, 5, 15);
		L8.addBox(0F, 0F, 0F, 1, 1, 1);
		L8.setRotationPoint(1F, 13.5F, -6F);
		L8.setTextureSize(64, 32);
		L8.mirror = true;
		setRotation(L8, 0F, 0F, 0F);
		L9 = new ModelRenderer(this, 5, 15);
		L9.addBox(0F, 0F, 0F, 1, 1, 1);
		L9.setRotationPoint(0F, 13.5F, -6F);
		L9.setTextureSize(64, 32);
		L9.mirror = true;
		setRotation(L9, 0F, 0F, 0F);
		L3 = new ModelRenderer(this, 5, 15);
		L3.addBox(0F, 0F, 0F, 1, 1, 1);
		L3.setRotationPoint(6F, 13.5F, -6F);
		L3.setTextureSize(64, 32);
		L3.mirror = true;
		setRotation(L3, 0F, 0F, 0F);
		L2 = new ModelRenderer(this, 5, 15);
		L2.addBox(0F, 0F, 0F, 1, 1, 1);
		L2.setRotationPoint(7F, 13.5F, -6F);
		L2.setTextureSize(64, 32);
		L2.mirror = true;
		setRotation(L2, 0F, 0F, 0F);
		L1 = new ModelRenderer(this, 5, 15);
		L1.addBox(0F, 0F, 0F, 1, 1, 1);
		L1.setRotationPoint(8F, 13.5F, -6F);
		L1.setTextureSize(64, 32);
		L1.mirror = true;
		setRotation(L1, 0F, 0F, 0F);
		Bolt = new ModelRenderer(this, 27, 0);
		Bolt.addBox(0F, 0F, 0F, 1, 1, 3);
		Bolt.setRotationPoint(-0.5F, 13F, -1F);
		Bolt.setTextureSize(64, 32);
		Bolt.mirror = true;
		Bolt.isHidden = true;
		setRotation(Bolt, 0F, 0F, 0F);

	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Body.render(f5);
		Stock.render(f5);
		Front.render(f5);
		LeftArm.render(f5);
		RightArm.render(f5);
		FrontAccent.render(f5);
		R1.render(f5);
		R2.render(f5);
		R3.render(f5);
		R4.render(f5);
		R5.render(f5);
		R6.render(f5);
		R7.render(f5);
		R8.render(f5);
		R9.render(f5);
		Grip.render(f5);
		L4.render(f5);
		L5.render(f5);
		L6.render(f5);
		L7.render(f5);
		L8.render(f5);
		L9.render(f5);
		L3.render(f5);
		L2.render(f5);
		L1.render(f5);
		Bolt.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

	// when number = 1, not cocked
	// when number = 0, fully cocked
	// Pulling the bow sets the string cubes of the model
	public void pullBow(float number)
	{
		L1.offsetZ = number * OFFSET1;
		L2.offsetZ = number * OFFSET2;
		L3.offsetZ = number * OFFSET3;
		L4.offsetZ = number * OFFSET4;
		L5.offsetZ = number * OFFSET5;
		L6.offsetZ = number * OFFSET6;
		L7.offsetZ = number * OFFSET7;
		L8.offsetZ = number * OFFSET8;
		L9.offsetZ = number * OFFSET9;

		R1.offsetZ = number * OFFSET1;
		R2.offsetZ = number * OFFSET2;
		R3.offsetZ = number * OFFSET3;
		R4.offsetZ = number * OFFSET4;
		R5.offsetZ = number * OFFSET5;
		R6.offsetZ = number * OFFSET6;
		R7.offsetZ = number * OFFSET7;
		R8.offsetZ = number * OFFSET8;
		R9.offsetZ = number * OFFSET9;
	}

}
