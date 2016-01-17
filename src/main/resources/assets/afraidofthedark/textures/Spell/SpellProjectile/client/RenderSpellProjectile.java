package yourModPackage.client.renders;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import yourModPackage.common.entities.mobs.EntitySpellProjectile;

public class RenderSpellProjectile extends Render {

	public static final ResourceLocation SpellProjectile_texture = new ResourceLocation("yourAssetsFolder", "textures/models/SpellProjectile.png");
	public static ModelSpellProjectile modelSpellProjectile;	
	
	public RenderSpellProjectile()
    {
        this.modelSpellProjectile = new ModelSpellProjectile();
    }
	
	@Override
	public void doRender(Entity _entity, double posX, double posY, double posZ, float var8, float var9) {
		EntitySpellProjectile entity = (EntitySpellProjectile) _entity;
				
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glTranslatef((float)posX, (float)posY, (float)posZ);
		this.bindEntityTexture(entity);
		this.modelSpellProjectile.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1) {
		return SpellProjectile_texture;
	}
}