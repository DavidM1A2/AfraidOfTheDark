package yourModPackage.client.renders;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import yourModPackage.common.entities.mobs.EntityWerewolf;

public class RenderWerewolf extends RenderLiving {

	public static final ResourceLocation Werewolf_texture = new ResourceLocation("yourAssetsFolder", "textures/models/Werewolf.png");
	public static ModelWerewolf modelWerewolf = new ModelWerewolf();	
	public static float modelHeight = 2F;
	
	public RenderWerewolf()
    {
        super(modelWerewolf, 1F);
    }
	
	@Override
	public void doRender(Entity _entity, double posX, double posY, double posZ, float var8, float var9) {
		EntityWerewolf entity = (EntityWerewolf) _entity;
				
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		super.doRender(_entity, posX, posY, posZ, var8, var9);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}
	
	@Override
	protected void preRenderCallback(EntityLivingBase entityliving, float f)
	{
		GL11.glRotatef(180F, 0, 1F, 0F);
		GL11.glRotatef(180F, 0, 0, 1F);
		GL11.glTranslatef(0, modelHeight, 0);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1) {
		return Werewolf_texture;
	}
}