package com.davidm1a2.afraidofthedark.client.entity.splinterDrone;

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Matrix4f;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion;
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDroneProjectile;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Model class that defines the splinter drone projectile model
 */
public class ModelSplinterDroneProjectile extends ModelBase
{
    // A map of part name to part
    private final Map<String, MCAModelRenderer> parts = new HashMap<>();

    // The different parts of the model
    private MCAModelRenderer body;

    /**
     * Constructor initializes the model
     */
    ModelSplinterDroneProjectile()
    {
        // Auto-generated from the MCAnimator software

        textureWidth = 32;
        textureHeight = 32;

        body = new MCAModelRenderer(this, "body", 0, 0);
        body.mirror = false;
        body.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6);
        body.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        body.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        body.setTextureSize(32, 32);
        parts.put(body.boxName, body);

        MCAModelRenderer part1 = new MCAModelRenderer(this, "part1", 0, 16);
        part1.mirror = false;
        part1.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 4);
        part1.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        part1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        part1.setTextureSize(32, 32);
        parts.put(part1.boxName, part1);
        body.addChild(part1);

        MCAModelRenderer part2 = new MCAModelRenderer(this, "part2", 0, 16);
        part2.mirror = false;
        part2.addBox(0.0F, -2.0F, -2.0F, 4, 4, 4);
        part2.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        part2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        part2.setTextureSize(32, 32);
        parts.put(part2.boxName, part2);
        body.addChild(part2);

        MCAModelRenderer part3 = new MCAModelRenderer(this, "part3", 0, 16);
        part3.mirror = false;
        part3.addBox(-2.0F, -2.0F, -4.0F, 4, 4, 4);
        part3.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        part3.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        part3.setTextureSize(32, 32);
        parts.put(part3.boxName, part3);
        body.addChild(part3);

        MCAModelRenderer part4 = new MCAModelRenderer(this, "part4", 0, 16);
        part4.mirror = false;
        part4.addBox(-4.0F, -2.0F, -2.0F, 4, 4, 4);
        part4.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        part4.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        part4.setTextureSize(32, 32);
        parts.put(part4.boxName, part4);
        body.addChild(part4);

        MCAModelRenderer part5 = new MCAModelRenderer(this, "part5", 0, 16);
        part5.mirror = false;
        part5.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4);
        part5.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        part5.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        part5.setTextureSize(32, 32);
        parts.put(part5.boxName, part5);
        body.addChild(part5);

        MCAModelRenderer part6 = new MCAModelRenderer(this, "part6", 0, 16);
        part6.mirror = false;
        part6.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4);
        part6.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        part6.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        part6.setTextureSize(32, 32);
        parts.put(part6.boxName, part6);
        body.addChild(part6);
    }

    /**
     * Called every game tick to render the splinter drone projectile model
     *
     * @param entityIn        The entity to render, this must be a splinter drone projectile
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
        // Cast the entity to a splinter drone projectile model
        EntitySplinterDroneProjectile entity = (EntitySplinterDroneProjectile) entityIn;

        // Animate the model (moves all pieces from time t to t+1)
        AnimationHandler.performAnimationInModel(parts, entity);

        // Render the model
        body.render(scale);
    }
}
