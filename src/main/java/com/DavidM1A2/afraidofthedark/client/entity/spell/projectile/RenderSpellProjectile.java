package com.DavidM1A2.afraidofthedark.client.entity.spell.projectile;

import com.DavidM1A2.afraidofthedark.common.entity.spell.projectile.EntitySpellProjectile;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Renderer class for the spell projectile entity
 */
public class RenderSpellProjectile extends Render<EntitySpellProjectile>
{
    // The texture used by the model
    private static final ResourceLocation SPELL_PROJECTILE_TEXTURE = new ResourceLocation("afraidofthedark:textures/entity/spell/projectile.png");
    // The spell projectile model
    private static final ModelBase SPELL_PROJECTILE_MODEL = new ModelSpellProjectile();

    /**
     * Constructor just passes down fields and the render manager
     *
     * @param renderManager The render manager to pass down
     */
    public RenderSpellProjectile(RenderManager renderManager)
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
    public void doRender(EntitySpellProjectile entity, double posX, double posY, double posZ, float entityYaw, float partialTicks)
    {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef((float) posX, (float) posY + 0.2f, (float) posZ);
        this.bindEntityTexture(entity);
        SPELL_PROJECTILE_MODEL.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
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
    protected ResourceLocation getEntityTexture(EntitySpellProjectile entity)
    {
        return SPELL_PROJECTILE_TEXTURE;
    }
}
