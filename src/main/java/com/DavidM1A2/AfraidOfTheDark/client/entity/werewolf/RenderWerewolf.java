package com.DavidM1A2.afraidofthedark.client.entity.werewolf;

import com.DavidM1A2.afraidofthedark.common.entity.enchantedSkeleton.EntityEnchantedSkeleton;
import com.DavidM1A2.afraidofthedark.common.entity.werewolf.EntityWerewolf;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Renderer class for the werewolf entity
 */
public class RenderWerewolf extends RenderLiving<EntityWerewolf>
{
	// The texture used by the model
	private static final ResourceLocation WEREWOLF_TEXTURE = new ResourceLocation("afraidofthedark:textures/entity/werewolf.png");
	// The werewolf model
	private static final ModelWerewolf WEREWOLF_MODEL = new ModelWerewolf();
	// The height of the werewolf model
	private static final float MODEL_HEIGHT = 2.5F;
	// The size of the shadow of the model
	private static final float MODEL_SHADOW_SIZE = 0.6F;

	/**
	 * Constructor just initializes the render living renderer
	 *
	 * @param renderManager The render manager to pass down
	 */
	public RenderWerewolf(RenderManager renderManager)
	{
		super(renderManager, WEREWOLF_MODEL, MODEL_SHADOW_SIZE);
	}

	/**
	 * Renders the entity at a given position, yaw, and partial ticks parameter
	 *
	 * @param entity The entity to render
	 * @param posX The X position of the entity to render at
	 * @param posY The Y position of the entity to render at
	 * @param posZ The Z position of the entity to render at
	 * @param entityYaw The yaw of the entity to render
	 * @param partialTicks The partial ticks that have gone by since the last frame
	 */
	@Override
	public void doRender(EntityWerewolf entity, double posX, double posY, double posZ, float entityYaw, float partialTicks)
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
	 * @param entityliving The entity to render
	 * @param partialTickTime The partial ticks that have gone by since the last frame
	 */
	@Override
	protected void preRenderCallback(EntityWerewolf entityliving, float partialTickTime)
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
	protected ResourceLocation getEntityTexture(EntityWerewolf entity)
	{
		return WEREWOLF_TEXTURE;
	}
}