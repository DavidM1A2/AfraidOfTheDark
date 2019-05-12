package com.DavidM1A2.afraidofthedark.client.entity.splinterDrone;

import com.DavidM1A2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDroneProjectile;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Renderer class for the splinter drone projectile entity
 */
public class RenderSplinterDroneProjectile extends Render<EntitySplinterDroneProjectile>
{
    // The texture used by the model
    private static final ResourceLocation SPLINTER_DRONE_PROJECTILE_TEXTURE = new ResourceLocation("afraidofthedark:textures/entity/splinter_drone_projectile.png");
    // The splinter drone projectile model
    private static final ModelBase SPLINTER_DRONE_PROJECTILE_MODEL = new ModelSplinterDroneProjectile();

    /**
     * Constructor just passes down fields and the render manager
     *
     * @param renderManager The render manager to pass down
     */
    public RenderSplinterDroneProjectile(RenderManager renderManager)
    {
        super(renderManager);
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
    public void doRender(EntitySplinterDroneProjectile entity, double posX, double posY, double posZ, float entityYaw, float partialTicks)
    {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef((float) posX, (float) posY + 0.2f, (float) posZ);
        this.bindEntityTexture(entity);
        SPLINTER_DRONE_PROJECTILE_MODEL.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    /**
     * Gets the texture for the entity
     *
     * @param entity The entity to get the texture for
     * @return The texture to use for this entity
     */
    @Override
    protected ResourceLocation getEntityTexture(EntitySplinterDroneProjectile entity)
    {
        return SPLINTER_DRONE_PROJECTILE_TEXTURE;
    }
}
