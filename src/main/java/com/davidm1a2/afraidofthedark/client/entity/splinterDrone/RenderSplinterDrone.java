package com.davidm1a2.afraidofthedark.client.entity.splinterDrone;

import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDrone;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Renderer class for the splinter drone entity
 */
public class RenderSplinterDrone extends RenderLiving<EntitySplinterDrone>
{
    // The texture used by the model
    private static final ResourceLocation SPLINTER_DRONE_TEXTURE = new ResourceLocation("afraidofthedark:textures/entity/splinter_drone.png");
    // The splinter drone model
    private static final ModelBase SPLINTER_DRONE_MODEL = new ModelSplinterDrone();
    // The height of the splinter drone model
    private static final float MODEL_HEIGHT = 3.1f;
    // The size of the shadow of the model
    private static final float MODEL_SHADOW_SIZE = 0.3f;

    /**
     * Constructor just passes down fields and the render manager
     *
     * @param renderManager The render manager to pass down
     */
    public RenderSplinterDrone(RenderManager renderManager)
    {
        super(renderManager, SPLINTER_DRONE_MODEL, MODEL_SHADOW_SIZE);
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
    public void doRender(EntitySplinterDrone entity, double posX, double posY, double posZ, float entityYaw, float partialTicks)
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
     * @param entityliving    The entity to render
     * @param partialTickTime The partial ticks that have gone by since the last frame
     */
    @Override
    protected void preRenderCallback(EntitySplinterDrone entityliving, float partialTickTime)
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
    @Override
    protected ResourceLocation getEntityTexture(EntitySplinterDrone entity)
    {
        return SPLINTER_DRONE_TEXTURE;
    }
}
