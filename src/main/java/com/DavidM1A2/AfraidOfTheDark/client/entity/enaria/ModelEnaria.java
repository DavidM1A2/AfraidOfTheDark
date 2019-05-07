package com.DavidM1A2.afraidofthedark.client.entity.enaria;

import com.DavidM1A2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.math.Matrix4f;
import com.DavidM1A2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Model for the enaria and ghastly enaria entity
 */
public class ModelEnaria extends ModelBase
{
    // A map of part name to part
    private final Map<String, MCAModelRenderer> parts = new HashMap<>();

    // The different parts of the model
    private MCAModelRenderer body;

    /**
     * Constructor initializes the model parts, this is created from MCAnimator
     */
    ModelEnaria()
    {
        textureWidth = 64;
        textureHeight = 64;

        body = new MCAModelRenderer(this, "body", 0, 16);
        body.mirror = false;
        body.addBox(-4.0F, -12.0F, -2.0F, 8, 12, 4);
        body.setInitialRotationPoint(0.0F, 2.0F, 2.0F);
        body.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        body.setTextureSize(64, 64);
        parts.put(body.boxName, body);

        MCAModelRenderer rightarm = new MCAModelRenderer(this, "rightarm", 41, 16);
        rightarm.mirror = false;
        rightarm.addBox(-3.0F, -10.0F, -2.0F, 3, 12, 4);
        rightarm.setInitialRotationPoint(-4.0F, -2.0F, 0.0F);
        rightarm.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        rightarm.setTextureSize(64, 64);
        parts.put(rightarm.boxName, rightarm);
        body.addChild(rightarm);

        MCAModelRenderer leftarm = new MCAModelRenderer(this, "leftarm", 25, 16);
        leftarm.mirror = false;
        leftarm.addBox(0.0F, -10.0F, -2.0F, 3, 12, 4);
        leftarm.setInitialRotationPoint(4.0F, -2.0F, 0.0F);
        leftarm.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        leftarm.setTextureSize(64, 64);
        parts.put(leftarm.boxName, leftarm);
        body.addChild(leftarm);

        MCAModelRenderer rightleg = new MCAModelRenderer(this, "rightleg", 16, 32);
        rightleg.mirror = false;
        rightleg.addBox(-2.0F, -12.0F, -2.0F, 4, 12, 4);
        rightleg.setInitialRotationPoint(-2.0F, -12.0F, 0.0F);
        rightleg.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        rightleg.setTextureSize(64, 64);
        parts.put(rightleg.boxName, rightleg);
        body.addChild(rightleg);

        MCAModelRenderer leftleg = new MCAModelRenderer(this, "leftleg", 0, 32);
        leftleg.mirror = false;
        leftleg.addBox(-2.0F, -12.0F, -2.0F, 4, 12, 4);
        leftleg.setInitialRotationPoint(2.0F, -12.0F, 0.0F);
        leftleg.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        leftleg.setTextureSize(64, 64);
        parts.put(leftleg.boxName, leftleg);
        body.addChild(leftleg);

        MCAModelRenderer head = new MCAModelRenderer(this, "head", 0, 0);
        head.mirror = false;
        head.addBox(-4.0F, 0.0F, -4.0F, 8, 8, 8);
        head.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
        head.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
        head.setTextureSize(64, 64);
        parts.put(head.boxName, head);
        body.addChild(head);
    }

    /**
     * Called every game tick to render the enaria model
     *
     * @param entityIn        The entity to render, this must be an enaria entity
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
        // Grab the enaria entity
        IMCAnimatedEntity enariaEntity = (IMCAnimatedEntity) entityIn;

        // Perform the animation
        AnimationHandler.performAnimationInModel(parts, enariaEntity);

        // Render every non-child part
        body.render(scale);
    }
}
