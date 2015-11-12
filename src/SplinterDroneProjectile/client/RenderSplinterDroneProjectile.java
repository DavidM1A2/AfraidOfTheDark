package yourModPackage.client.renders;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import yourModPackage.common.entities.mobs.EntitySplinterDroneProjectile;

public class RenderSplinterDroneProjectile extends Render {

	public static final ResourceLocation SplinterDroneProjectile_texture = new ResourceLocation("yourAssetsFolder", "textures/models/SplinterDroneProjectile.png");
	public static ModelSplinterDroneProjectile modelSplinterDroneProjectile;	
	
	public RenderSplinterDroneProjectile()
    {
        this.modelSplinterDroneProjectile = new ModelSplinterDroneProjectile();
    }
	
	@Override
	public void doRender(Entity _entity, double posX, double posY, double posZ, float var8, float var9) {
		EntitySplinterDroneProjectile entity = (EntitySplinterDroneProjectile) _entity;
				
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glTranslatef((float)posX, (float)posY, (float)posZ);
		this.bindEntityTexture(entity);
		this.modelSplinterDroneProjectile.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1) {
		return SplinterDroneProjectile_texture;
	}
}