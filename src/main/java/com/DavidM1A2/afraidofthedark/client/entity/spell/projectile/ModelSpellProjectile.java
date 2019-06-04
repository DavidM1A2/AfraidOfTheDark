package com.DavidM1A2.afraidofthedark.client.entity.spell.projectile;

import com.DavidM1A2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.math.Matrix4f;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion;
import com.DavidM1A2.afraidofthedark.common.entity.spell.projectile.EntitySpellProjectile;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing the spell projectile entity model
 */
public class ModelSpellProjectile extends ModelBase
{
    // A map of part name to part
    public Map<String, MCAModelRenderer> parts = new HashMap<>();

    // Different parts of the model
    private MCAModelRenderer center;

    /**
     * Constructor initializes the model
     */
    ModelSpellProjectile()
    {
        // Auto-generated from the MCAnimator software

        textureWidth = 32;
        textureHeight = 32;

        center = new MCAModelRenderer(this, "Center", 0, 10);
        center.mirror = false;
        center.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6);
        center.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        center.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        center.setTextureSize(32, 32);
        parts.put(center.boxName, center);

        MCAModelRenderer one = new MCAModelRenderer(this, "one", 0, 0);
        one.mirror = false;
        one.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 4);
        one.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        one.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        one.setTextureSize(32, 32);
        parts.put(one.boxName, one);
        center.addChild(one);

        MCAModelRenderer two = new MCAModelRenderer(this, "two", 0, 0);
        two.mirror = false;
        two.addBox(0.0F, -2.0F, -2.0F, 4, 4, 4);
        two.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        two.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        two.setTextureSize(32, 32);
        parts.put(two.boxName, two);
        center.addChild(two);

        MCAModelRenderer three = new MCAModelRenderer(this, "three", 0, 0);
        three.mirror = false;
        three.addBox(-2.0F, -2.0F, -4.0F, 4, 4, 4);
        three.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        three.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        three.setTextureSize(32, 32);
        parts.put(three.boxName, three);
        center.addChild(three);

        MCAModelRenderer four = new MCAModelRenderer(this, "four", 0, 0);
        four.mirror = false;
        four.addBox(-4.0F, -2.0F, -2.0F, 4, 4, 4);
        four.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        four.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        four.setTextureSize(32, 32);
        parts.put(four.boxName, four);
        center.addChild(four);

        MCAModelRenderer five = new MCAModelRenderer(this, "five", 0, 0);
        five.mirror = false;
        five.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4);
        five.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        five.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        five.setTextureSize(32, 32);
        parts.put(five.boxName, five);
        center.addChild(five);

        MCAModelRenderer six = new MCAModelRenderer(this, "six", 0, 0);
        six.mirror = false;
        six.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4);
        six.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        six.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        six.setTextureSize(32, 32);
        parts.put(six.boxName, six);
        center.addChild(six);
    }

    /**
     * Called every game tick to render the delivery method projectile model
     *
     * @param entityIn        The entity to render, this must be a delivery method projectile
     * @param limbSwing       ignored, used only by default MC
     * @param limbSwingAmount ignored, used only by default MC
     * @param ageInTicks      ignored, used only by default MC
     * @param netHeadYaw      ignored, used only by default MC
     * @param headPitch       ignored, used only by default MC
     * @param scale           The scale to render the model at
     */
    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        // Cast the entity to a delivery method projectile model
        EntitySpellProjectile entity = (EntitySpellProjectile) entityIn;

        // Animate the model (moves all pieces from time t to t+1)
        AnimationHandler.performAnimationInModel(parts, entity);

        // Render the model
        center.render(scale);
    }
}
