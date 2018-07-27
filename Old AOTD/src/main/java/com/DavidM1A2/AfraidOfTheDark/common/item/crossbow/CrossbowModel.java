/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item.crossbow;

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
		this.textureWidth = 64;
		this.textureHeight = 32;

		this.Body = new ModelRenderer(this, 0, 0);
		this.Body.addBox(0F, 0F, 0F, 2, 2, 10);
		this.Body.setRotationPoint(-1F, 14F, -8F);
		this.Body.setTextureSize(64, 32);
		this.Body.mirror = true;
		this.setRotation(this.Body, 0F, 0F, 0F);
		this.Stock = new ModelRenderer(this, 0, 0);
		this.Stock.addBox(0F, 0F, 0F, 2, 1, 5);
		this.Stock.setRotationPoint(-1F, 14F, 2F);
		this.Stock.setTextureSize(64, 32);
		this.Stock.mirror = true;
		this.setRotation(this.Stock, 0F, 0F, 0F);
		this.Front = new ModelRenderer(this, 0, 0);
		this.Front.addBox(0F, 0F, 0F, 10, 1, 1);
		this.Front.setRotationPoint(-5F, 14F, -9F);
		this.Front.setTextureSize(64, 32);
		this.Front.mirror = true;
		this.setRotation(this.Front, 0F, 0F, 0F);
		this.LeftArm = new ModelRenderer(this, 0, 0);
		this.LeftArm.addBox(0F, 0F, 0F, 5, 1, 1);
		this.LeftArm.setRotationPoint(5F, 14F, -9F);
		this.LeftArm.setTextureSize(64, 32);
		this.LeftArm.mirror = true;
		this.setRotation(this.LeftArm, 0F, -0.5235988F, 0F);
		this.RightArm = new ModelRenderer(this, 0, 0);
		this.RightArm.addBox(-5F, 0F, 0F, 5, 1, 1);
		this.RightArm.setRotationPoint(-5F, 14F, -9F);
		this.RightArm.setTextureSize(64, 32);
		this.RightArm.mirror = true;
		this.setRotation(this.RightArm, 0F, 0.5235988F, 0F);
		this.FrontAccent = new ModelRenderer(this, 10, 15);
		this.FrontAccent.addBox(0F, 0F, 0F, 8, 1, 1);
		this.FrontAccent.setRotationPoint(-4F, 14F, -10F);
		this.FrontAccent.setTextureSize(64, 32);
		this.FrontAccent.mirror = true;
		this.setRotation(this.FrontAccent, 0F, 0F, 0F);
		this.R1 = new ModelRenderer(this, 5, 15);
		this.R1.addBox(0F, 0F, 0F, 1, 1, 1);
		this.R1.setRotationPoint(-9F, 13.5F, -6F);
		this.R1.setTextureSize(64, 32);
		this.R1.mirror = true;
		this.setRotation(this.R1, 0F, 0F, 0F);
		this.R2 = new ModelRenderer(this, 5, 15);
		this.R2.addBox(0F, 0F, 0F, 1, 1, 1);
		this.R2.setRotationPoint(-8F, 13.5F, -6F);
		this.R2.setTextureSize(64, 32);
		this.R2.mirror = true;
		this.setRotation(this.R2, 0F, 0F, 0F);
		this.R3 = new ModelRenderer(this, 5, 15);
		this.R3.addBox(0F, 0F, 0F, 1, 1, 1);
		this.R3.setRotationPoint(-7F, 13.5F, -6F);
		this.R3.setTextureSize(64, 32);
		this.R3.mirror = true;
		this.setRotation(this.R3, 0F, 0F, 0F);
		this.R4 = new ModelRenderer(this, 5, 15);
		this.R4.addBox(0F, 0F, 0F, 1, 1, 1);
		this.R4.setRotationPoint(-6F, 13.5F, -6F);
		this.R4.setTextureSize(64, 32);
		this.R4.mirror = true;
		this.setRotation(this.R4, 0F, 0F, 0F);
		this.R5 = new ModelRenderer(this, 5, 15);
		this.R5.addBox(0F, 0F, 0F, 1, 1, 1);
		this.R5.setRotationPoint(-5F, 13.5F, -6F);
		this.R5.setTextureSize(64, 32);
		this.R5.mirror = true;
		this.setRotation(this.R5, 0F, 0F, 0F);
		this.R6 = new ModelRenderer(this, 5, 15);
		this.R6.addBox(0F, 0F, 0F, 1, 1, 1);
		this.R6.setRotationPoint(-4F, 13.5F, -6F);
		this.R6.setTextureSize(64, 32);
		this.R6.mirror = true;
		this.setRotation(this.R6, 0F, 0F, 0F);
		this.R7 = new ModelRenderer(this, 5, 15);
		this.R7.addBox(0F, 0F, 0F, 1, 1, 1);
		this.R7.setRotationPoint(-3F, 13.5F, -6F);
		this.R7.setTextureSize(64, 32);
		this.R7.mirror = true;
		this.setRotation(this.R7, 0F, 0F, 0F);
		this.R8 = new ModelRenderer(this, 5, 15);
		this.R8.addBox(0F, 0F, 0F, 1, 1, 1);
		this.R8.setRotationPoint(-2F, 13.5F, -6F);
		this.R8.setTextureSize(64, 32);
		this.R8.mirror = true;
		this.setRotation(this.R8, 0F, 0F, 0F);
		this.R9 = new ModelRenderer(this, 5, 15);
		this.R9.addBox(0F, 0F, 0F, 1, 1, 1);
		this.R9.setRotationPoint(-1F, 13.5F, -6F);
		this.R9.setTextureSize(64, 32);
		this.R9.mirror = true;
		this.setRotation(this.R9, 0F, 0F, 0F);
		this.Grip = new ModelRenderer(this, 0, 0);
		this.Grip.addBox(0F, 0F, 0F, 1, 2, 1);
		this.Grip.setRotationPoint(-0.5F, 16F, -7F);
		this.Grip.setTextureSize(64, 32);
		this.Grip.mirror = true;
		this.setRotation(this.Grip, 0F, 0F, 0F);
		this.L4 = new ModelRenderer(this, 5, 15);
		this.L4.addBox(0F, 0F, 0F, 1, 1, 1);
		this.L4.setRotationPoint(5F, 13.5F, -6F);
		this.L4.setTextureSize(64, 32);
		this.L4.mirror = true;
		this.setRotation(this.L4, 0F, 0F, 0F);
		this.L5 = new ModelRenderer(this, 5, 15);
		this.L5.addBox(0F, 0F, 0F, 1, 1, 1);
		this.L5.setRotationPoint(4F, 13.5F, -6F);
		this.L5.setTextureSize(64, 32);
		this.L5.mirror = true;
		this.setRotation(this.L5, 0F, 0F, 0F);
		this.L6 = new ModelRenderer(this, 5, 15);
		this.L6.addBox(0F, 0F, 0F, 1, 1, 1);
		this.L6.setRotationPoint(3F, 13.5F, -6F);
		this.L6.setTextureSize(64, 32);
		this.L6.mirror = true;
		this.setRotation(this.L6, 0F, 0F, 0F);
		this.L7 = new ModelRenderer(this, 5, 15);
		this.L7.addBox(0F, 0F, 0F, 1, 1, 1);
		this.L7.setRotationPoint(2F, 13.5F, -6F);
		this.L7.setTextureSize(64, 32);
		this.L7.mirror = true;
		this.setRotation(this.L7, 0F, 0F, 0F);
		this.L8 = new ModelRenderer(this, 5, 15);
		this.L8.addBox(0F, 0F, 0F, 1, 1, 1);
		this.L8.setRotationPoint(1F, 13.5F, -6F);
		this.L8.setTextureSize(64, 32);
		this.L8.mirror = true;
		this.setRotation(this.L8, 0F, 0F, 0F);
		this.L9 = new ModelRenderer(this, 5, 15);
		this.L9.addBox(0F, 0F, 0F, 1, 1, 1);
		this.L9.setRotationPoint(0F, 13.5F, -6F);
		this.L9.setTextureSize(64, 32);
		this.L9.mirror = true;
		this.setRotation(this.L9, 0F, 0F, 0F);
		this.L3 = new ModelRenderer(this, 5, 15);
		this.L3.addBox(0F, 0F, 0F, 1, 1, 1);
		this.L3.setRotationPoint(6F, 13.5F, -6F);
		this.L3.setTextureSize(64, 32);
		this.L3.mirror = true;
		this.setRotation(this.L3, 0F, 0F, 0F);
		this.L2 = new ModelRenderer(this, 5, 15);
		this.L2.addBox(0F, 0F, 0F, 1, 1, 1);
		this.L2.setRotationPoint(7F, 13.5F, -6F);
		this.L2.setTextureSize(64, 32);
		this.L2.mirror = true;
		this.setRotation(this.L2, 0F, 0F, 0F);
		this.L1 = new ModelRenderer(this, 5, 15);
		this.L1.addBox(0F, 0F, 0F, 1, 1, 1);
		this.L1.setRotationPoint(8F, 13.5F, -6F);
		this.L1.setTextureSize(64, 32);
		this.L1.mirror = true;
		this.setRotation(this.L1, 0F, 0F, 0F);
		this.Bolt = new ModelRenderer(this, 27, 0);
		this.Bolt.addBox(0F, 0F, 0F, 1, 1, 3);
		this.Bolt.setRotationPoint(-0.5F, 13F, -1F);
		this.Bolt.setTextureSize(64, 32);
		this.Bolt.mirror = true;
		this.Bolt.isHidden = true;
		this.setRotation(this.Bolt, 0F, 0F, 0F);

	}

	@Override
	public void render(final Entity entity, final float f, final float f1, final float f2, final float f3, final float f4, final float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.Body.render(f5);
		this.Stock.render(f5);
		this.Front.render(f5);
		this.LeftArm.render(f5);
		this.RightArm.render(f5);
		this.FrontAccent.render(f5);
		this.R1.render(f5);
		this.R2.render(f5);
		this.R3.render(f5);
		this.R4.render(f5);
		this.R5.render(f5);
		this.R6.render(f5);
		this.R7.render(f5);
		this.R8.render(f5);
		this.R9.render(f5);
		this.Grip.render(f5);
		this.L4.render(f5);
		this.L5.render(f5);
		this.L6.render(f5);
		this.L7.render(f5);
		this.L8.render(f5);
		this.L9.render(f5);
		this.L3.render(f5);
		this.L2.render(f5);
		this.L1.render(f5);
		this.Bolt.render(f5);
	}

	private void setRotation(final ModelRenderer model, final float x, final float y, final float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(final float f, final float f1, final float f2, final float f3, final float f4, final float f5, final Entity entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

	// when number = 1, not cocked
	// when number = 0, fully cocked
	// Pulling the bow sets the string cubes of the model
	public void pullBow(final float number)
	{
		this.L1.offsetZ = number * CrossbowModel.OFFSET1;
		this.L2.offsetZ = number * CrossbowModel.OFFSET2;
		this.L3.offsetZ = number * CrossbowModel.OFFSET3;
		this.L4.offsetZ = number * CrossbowModel.OFFSET4;
		this.L5.offsetZ = number * CrossbowModel.OFFSET5;
		this.L6.offsetZ = number * CrossbowModel.OFFSET6;
		this.L7.offsetZ = number * CrossbowModel.OFFSET7;
		this.L8.offsetZ = number * CrossbowModel.OFFSET8;
		this.L9.offsetZ = number * CrossbowModel.OFFSET9;

		this.R1.offsetZ = number * CrossbowModel.OFFSET1;
		this.R2.offsetZ = number * CrossbowModel.OFFSET2;
		this.R3.offsetZ = number * CrossbowModel.OFFSET3;
		this.R4.offsetZ = number * CrossbowModel.OFFSET4;
		this.R5.offsetZ = number * CrossbowModel.OFFSET5;
		this.R6.offsetZ = number * CrossbowModel.OFFSET6;
		this.R7.offsetZ = number * CrossbowModel.OFFSET7;
		this.R8.offsetZ = number * CrossbowModel.OFFSET8;
		this.R9.offsetZ = number * CrossbowModel.OFFSET9;
	}

}
