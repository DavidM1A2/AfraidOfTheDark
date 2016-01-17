package yourModPackage.client.models;

import java.util.HashMap;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

import yourModPackage.client.MCAClientLibrary.MCAModelRenderer;
import yourModPackage.common.MCACommonLibrary.MCAVersionChecker;
import yourModPackage.common.MCACommonLibrary.animation.AnimationHandler;
import yourModPackage.common.MCACommonLibrary.math.Matrix4f;
import yourModPackage.common.MCACommonLibrary.math.Quaternion;
import yourModPackage.common.entities.mobs.EntitySpellProjectile;

public class ModelSpellProjectile extends ModelBase {
public final int MCA_MIN_REQUESTED_VERSION = 5;
public HashMap<String, MCAModelRenderer> parts = new HashMap<String, MCAModelRenderer>();

MCAModelRenderer center;
MCAModelRenderer 1;
MCAModelRenderer 2;
MCAModelRenderer 3;
MCAModelRenderer 4;
MCAModelRenderer 5;
MCAModelRenderer 6;

public ModelSpellProjectile()
{
MCAVersionChecker.checkForLibraryVersion(getClass(), MCA_MIN_REQUESTED_VERSION);

textureWidth = 32;
textureHeight = 32;

center = new MCAModelRenderer(this, "Center", 0, 10);
center.mirror = false;
center.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6);
center.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
center.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
center.setTextureSize(32, 32);
parts.put(center.boxName, center);

1 = new MCAModelRenderer(this, "1", 0, 0);
1.mirror = false;
1.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 4);
1.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
1.setTextureSize(32, 32);
parts.put(1.boxName, 1);
center.addChild(1);

2 = new MCAModelRenderer(this, "2", 0, 0);
2.mirror = false;
2.addBox(0.0F, -2.0F, -2.0F, 4, 4, 4);
2.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
2.setTextureSize(32, 32);
parts.put(2.boxName, 2);
center.addChild(2);

3 = new MCAModelRenderer(this, "3", 0, 0);
3.mirror = false;
3.addBox(-2.0F, -2.0F, -4.0F, 4, 4, 4);
3.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
3.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
3.setTextureSize(32, 32);
parts.put(3.boxName, 3);
center.addChild(3);

4 = new MCAModelRenderer(this, "4", 0, 0);
4.mirror = false;
4.addBox(-4.0F, -2.0F, -2.0F, 4, 4, 4);
4.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
4.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
4.setTextureSize(32, 32);
parts.put(4.boxName, 4);
center.addChild(4);

5 = new MCAModelRenderer(this, "5", 0, 0);
5.mirror = false;
5.addBox(-2.0F, -4.0F, -2.0F, 4, 4, 4);
5.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
5.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
5.setTextureSize(32, 32);
parts.put(5.boxName, 5);
center.addChild(5);

6 = new MCAModelRenderer(this, "6", 0, 0);
6.mirror = false;
6.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4);
6.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
6.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
6.setTextureSize(32, 32);
parts.put(6.boxName, 6);
center.addChild(6);

}

@Override
public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) 
{
EntitySpellProjectile entity = (EntitySpellProjectile)par1Entity;

AnimationHandler.performAnimationInModel(parts, entity);

//Render every non-child part
center.render(par7);
}
@Override
public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {}

public MCAModelRenderer getModelRendererFromName(String name)
{
return parts.get(name);
}
}