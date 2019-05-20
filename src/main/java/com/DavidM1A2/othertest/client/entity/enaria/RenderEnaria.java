package com.DavidM1A2.afraidofthedark.client.entity.enaria;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.entity.enaria.EntityEnaria;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

/**
 * Renders the enaria model
 */
public class RenderEnaria extends RenderLiving<EntityEnaria>
{
    // The texture to apply to the model
    private static final ResourceLocation ENARIA_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entity/enaria.png");
    // The model to render
    private static final ModelBase ENARIA_MODEL = new ModelEnaria();
    // The height of the model to render at
    private static final float MODEL_HEIGHT = 2.8F;

    /**
     * Constructor just initializes the render living renderer
     *
     * @param renderManager The render manager to pass down
     */
    public RenderEnaria(RenderManager renderManager)
    {
        super(renderManager, ENARIA_MODEL, 0f);
    }

    /**
     * Renders the entity at a given position, yaw, and partial ticks parameter
     *
     * @param entity       The entity to render
     * @param posX         The X position of the entity to render at
     * @param posY         The Y position of the entity to render at
     * @param posZ         The Z position of the entity to render at
     * @param entityYaw    The yaw of the entity to render
     * @param partialTicks The partial ticks that have gone by since the last frame
     */
    @Override
    public void doRender(EntityEnaria entity, double posX, double posY, double posZ, float entityYaw, float partialTicks)
    {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        super.doRender(entity, posX, posY, posZ, entityYaw, partialTicks);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    /**
     * Before rendering the entity transform the rendering openGL state
     *
     * @param entityLiving    The entity to render
     * @param partialTickTime The partial ticks that have gone by since the last frame
     */
    @Override
    protected void preRenderCallback(EntityEnaria entityLiving, float partialTickTime)
    {
        GL11.glRotatef(180F, 0, 1F, 0F);
        GL11.glRotatef(180F, 0, 0, 1F);
        GL11.glTranslatef(0, MODEL_HEIGHT, 0);
    }

    /**
     * Gets the texture for the entity
     *
     * @param entity The entity to get the texture for
     * @return The texture to use for this entity
     */
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityEnaria entity)
    {
        return ENARIA_TEXTURE;
    }
}
