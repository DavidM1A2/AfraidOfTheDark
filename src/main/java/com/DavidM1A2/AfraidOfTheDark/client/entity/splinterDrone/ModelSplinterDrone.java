package com.DavidM1A2.afraidofthedark.client.entity.splinterDrone;

import com.DavidM1A2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.math.Matrix4f;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion;
import com.DavidM1A2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDrone;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Model class that defines the splinter drone model
 */
public class ModelSplinterDrone extends ModelBase
{
    // A map of part name to part
    private final Map<String, MCAModelRenderer> parts = new HashMap<>();

    // The different parts of the model
    private MCAModelRenderer body;

    /**
     * Constructor initializes the model
     */
    ModelSplinterDrone()
    {
        // Auto-generated from the MCAnimator software

        textureWidth = 128;
        textureHeight = 128;

        body = new MCAModelRenderer(this, "Body", 18, 0);
        body.mirror = false;
        body.addBox(-4.0F, -12.0F, -4.0F, 8, 24, 8);
        body.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        body.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        body.setTextureSize(128, 128);
        parts.put(body.boxName, body);

        MCAModelRenderer bodyPlate1 = new MCAModelRenderer(this, "BodyPlate1", 0, 34);
        bodyPlate1.mirror = false;
        bodyPlate1.addBox(-3.0F, -11.0F, -1.0F, 6, 22, 2);
        bodyPlate1.setInitialRotationPoint(0.0F, 0.0F, 4.0F);
        bodyPlate1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        bodyPlate1.setTextureSize(128, 128);
        parts.put(bodyPlate1.boxName, bodyPlate1);
        body.addChild(bodyPlate1);

        MCAModelRenderer bodyPlate3 = new MCAModelRenderer(this, "BodyPlate3", 0, 34);
        bodyPlate3.mirror = false;
        bodyPlate3.addBox(-3.0F, -11.0F, -1.0F, 6, 22, 2);
        bodyPlate3.setInitialRotationPoint(-4.0F, 0.0F, 0.0F);
        bodyPlate3.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.70710677F, 0.0F, 0.70710677F)).transpose());
        bodyPlate3.setTextureSize(128, 128);
        parts.put(bodyPlate3.boxName, bodyPlate3);
        body.addChild(bodyPlate3);

        MCAModelRenderer bodyPlate4 = new MCAModelRenderer(this, "BodyPlate4", 0, 34);
        bodyPlate4.mirror = false;
        bodyPlate4.addBox(-3.0F, -11.0F, -1.0F, 6, 22, 2);
        bodyPlate4.setInitialRotationPoint(0.0F, 0.0F, -4.0F);
        bodyPlate4.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        bodyPlate4.setTextureSize(128, 128);
        parts.put(bodyPlate4.boxName, bodyPlate4);
        body.addChild(bodyPlate4);

        MCAModelRenderer bodyPlate2 = new MCAModelRenderer(this, "BodyPlate2", 0, 34);
        bodyPlate2.mirror = false;
        bodyPlate2.addBox(-3.0F, -11.0F, -1.0F, 6, 22, 2);
        bodyPlate2.setInitialRotationPoint(4.0F, 0.0F, 0.0F);
        bodyPlate2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, -0.70710677F, 0.0F, 0.70710677F)).transpose());
        bodyPlate2.setTextureSize(128, 128);
        parts.put(bodyPlate2.boxName, bodyPlate2);
        body.addChild(bodyPlate2);

        MCAModelRenderer bottomPlate = new MCAModelRenderer(this, "BottomPlate", 18, 50);
        bottomPlate.mirror = false;
        bottomPlate.addBox(-3.0F, -1.0F, -3.0F, 6, 2, 6);
        bottomPlate.setInitialRotationPoint(0.0F, -12.0F, 0.0F);
        bottomPlate.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        bottomPlate.setTextureSize(128, 128);
        parts.put(bottomPlate.boxName, bottomPlate);
        body.addChild(bottomPlate);

        MCAModelRenderer topPlate = new MCAModelRenderer(this, "TopPlate", 18, 50);
        topPlate.mirror = false;
        topPlate.addBox(-3.0F, -1.0F, -3.0F, 6, 2, 6);
        topPlate.setInitialRotationPoint(0.0F, 12.0F, 0.0F);
        topPlate.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        topPlate.setTextureSize(128, 128);
        parts.put(topPlate.boxName, topPlate);
        body.addChild(topPlate);

        MCAModelRenderer sphere1 = new MCAModelRenderer(this, "Sphere1", 18, 40);
        sphere1.mirror = false;
        sphere1.addBox(-2.0F, 21.0F, -2.0F, 4, 4, 4);
        sphere1.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        sphere1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        sphere1.setTextureSize(128, 128);
        parts.put(sphere1.boxName, sphere1);
        body.addChild(sphere1);

        MCAModelRenderer sphere2 = new MCAModelRenderer(this, "Sphere2", 18, 40);
        sphere2.mirror = false;
        sphere2.addBox(-2.0F, -25.0F, -2.0F, 4, 4, 4);
        sphere2.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        sphere2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        sphere2.setTextureSize(128, 128);
        parts.put(sphere2.boxName, sphere2);
        body.addChild(sphere2);

        MCAModelRenderer pillar1 = new MCAModelRenderer(this, "Pillar1", 6, 0);
        pillar1.mirror = false;
        pillar1.addBox(-0.5F, -10.0F, 8.0F, 1, 20, 1);
        pillar1.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        pillar1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        pillar1.setTextureSize(128, 128);
        parts.put(pillar1.boxName, pillar1);
        body.addChild(pillar1);

        MCAModelRenderer pillar2 = new MCAModelRenderer(this, "Pillar2", 0, 0);
        pillar2.mirror = false;
        pillar2.addBox(-0.5F, -9.0F, 10.0F, 1, 18, 1);
        pillar2.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        pillar2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.38268346F, 0.0F, 0.9238795F)).transpose());
        pillar2.setTextureSize(128, 128);
        parts.put(pillar2.boxName, pillar2);
        body.addChild(pillar2);

        MCAModelRenderer pillar3 = new MCAModelRenderer(this, "Pillar3", 6, 0);
        pillar3.mirror = false;
        pillar3.addBox(-0.5F, -10.0F, 8.0F, 1, 20, 1);
        pillar3.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        pillar3.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.70710677F, 0.0F, 0.70710677F)).transpose());
        pillar3.setTextureSize(128, 128);
        parts.put(pillar3.boxName, pillar3);
        body.addChild(pillar3);

        MCAModelRenderer pillar4 = new MCAModelRenderer(this, "Pillar4", 0, 0);
        pillar4.mirror = false;
        pillar4.addBox(-0.5F, -9.0F, 10.0F, 1, 18, 1);
        pillar4.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        pillar4.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.9238795F, 0.0F, 0.38268343F)).transpose());
        pillar4.setTextureSize(128, 128);
        parts.put(pillar4.boxName, pillar4);
        body.addChild(pillar4);

        MCAModelRenderer pillar5 = new MCAModelRenderer(this, "Pillar5", 6, 0);
        pillar5.mirror = false;
        pillar5.addBox(-0.5F, -10.0F, 8.0F, 1, 20, 1);
        pillar5.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        pillar5.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, -1.0F, 0.0F, -4.371139E-8F)).transpose());
        pillar5.setTextureSize(128, 128);
        parts.put(pillar5.boxName, pillar5);
        body.addChild(pillar5);

        MCAModelRenderer pillar6 = new MCAModelRenderer(this, "Pillar6", 0, 0);
        pillar6.mirror = false;
        pillar6.addBox(-0.5F, -9.0F, 10.0F, 1, 18, 1);
        pillar6.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        pillar6.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, -0.9238795F, 0.0F, 0.38268343F)).transpose());
        pillar6.setTextureSize(128, 128);
        parts.put(pillar6.boxName, pillar6);
        body.addChild(pillar6);

        MCAModelRenderer pillar8 = new MCAModelRenderer(this, "Pillar8", 0, 0);
        pillar8.mirror = false;
        pillar8.addBox(-0.5F, -9.0F, 10.0F, 1, 18, 1);
        pillar8.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        pillar8.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, -0.38268346F, 0.0F, 0.9238795F)).transpose());
        pillar8.setTextureSize(128, 128);
        parts.put(pillar8.boxName, pillar8);
        body.addChild(pillar8);

        MCAModelRenderer pillar7 = new MCAModelRenderer(this, "Pillar7", 6, 0);
        pillar7.mirror = false;
        pillar7.addBox(-0.5F, -10.0F, 8.0F, 1, 20, 1);
        pillar7.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        pillar7.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, -0.70710677F, 0.0F, 0.70710677F)).transpose());
        pillar7.setTextureSize(128, 128);
        parts.put(pillar7.boxName, pillar7);
        body.addChild(pillar7);

    }

    /**
     * Called every game tick to render the splinter drone model
     *
     * @param entityIn        The entity to render, this must be a splinter drone
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
        // Cast the entity to a splinter drone model
        EntitySplinterDrone entity = (EntitySplinterDrone) entityIn;

        // Animate the model (moves all pieces from time t to t+1)
        AnimationHandler.performAnimationInModel(parts, entity);

        // Render the model
        body.render(scale);
    }
}