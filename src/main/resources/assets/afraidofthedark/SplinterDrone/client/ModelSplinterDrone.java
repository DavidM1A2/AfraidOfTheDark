package yourModPackage.client.models;

import java.util.HashMap;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

import yourModPackage.client.MCAClientLibrary.MCAModelRenderer;
import yourModPackage.common.MCACommonLibrary.MCAVersionChecker;
import yourModPackage.common.MCACommonLibrary.animation.AnimationHandler;
import yourModPackage.common.MCACommonLibrary.math.Matrix4f;
import yourModPackage.common.MCACommonLibrary.math.Quaternion;
import yourModPackage.common.entities.mobs.EntitySplinterDrone;

public class ModelSplinterDrone extends ModelBase {
public final int MCA_MIN_REQUESTED_VERSION = 5;
public HashMap<String, MCAModelRenderer> parts = new HashMap<String, MCAModelRenderer>();

MCAModelRenderer body;
MCAModelRenderer bodyPlate1;
MCAModelRenderer bodyPlate3;
MCAModelRenderer bodyPlate4;
MCAModelRenderer bodyPlate2;
MCAModelRenderer bottomPlate;
MCAModelRenderer topPlate;
MCAModelRenderer sphere1;
MCAModelRenderer sphere2;
MCAModelRenderer pillar1;
MCAModelRenderer pillar2;
MCAModelRenderer pillar3;
MCAModelRenderer pillar4;
MCAModelRenderer pillar5;
MCAModelRenderer pillar6;
MCAModelRenderer pillar8;
MCAModelRenderer pillar7;

public ModelSplinterDrone()
{
MCAVersionChecker.checkForLibraryVersion(getClass(), MCA_MIN_REQUESTED_VERSION);

textureWidth = 128;
textureHeight = 128;

body = new MCAModelRenderer(this, "Body", 18, 0);
body.mirror = false;
body.addBox(-4.0F, -12.0F, -4.0F, 8, 24, 8);
body.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
body.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
body.setTextureSize(128, 128);
parts.put(body.boxName, body);

bodyPlate1 = new MCAModelRenderer(this, "BodyPlate1", 0, 34);
bodyPlate1.mirror = false;
bodyPlate1.addBox(-3.0F, -11.0F, -1.0F, 6, 22, 2);
bodyPlate1.setInitialRotationPoint(0.0F, 0.0F, 4.0F);
bodyPlate1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
bodyPlate1.setTextureSize(128, 128);
parts.put(bodyPlate1.boxName, bodyPlate1);
body.addChild(bodyPlate1);

bodyPlate3 = new MCAModelRenderer(this, "BodyPlate3", 0, 34);
bodyPlate3.mirror = false;
bodyPlate3.addBox(-3.0F, -11.0F, -1.0F, 6, 22, 2);
bodyPlate3.setInitialRotationPoint(-4.0F, 0.0F, 0.0F);
bodyPlate3.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.70710677F, 0.0F, 0.70710677F)).transpose());
bodyPlate3.setTextureSize(128, 128);
parts.put(bodyPlate3.boxName, bodyPlate3);
body.addChild(bodyPlate3);

bodyPlate4 = new MCAModelRenderer(this, "BodyPlate4", 0, 34);
bodyPlate4.mirror = false;
bodyPlate4.addBox(-3.0F, -11.0F, -1.0F, 6, 22, 2);
bodyPlate4.setInitialRotationPoint(0.0F, 0.0F, -4.0F);
bodyPlate4.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
bodyPlate4.setTextureSize(128, 128);
parts.put(bodyPlate4.boxName, bodyPlate4);
body.addChild(bodyPlate4);

bodyPlate2 = new MCAModelRenderer(this, "BodyPlate2", 0, 34);
bodyPlate2.mirror = false;
bodyPlate2.addBox(-3.0F, -11.0F, -1.0F, 6, 22, 2);
bodyPlate2.setInitialRotationPoint(4.0F, 0.0F, 0.0F);
bodyPlate2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, -0.70710677F, 0.0F, 0.70710677F)).transpose());
bodyPlate2.setTextureSize(128, 128);
parts.put(bodyPlate2.boxName, bodyPlate2);
body.addChild(bodyPlate2);

bottomPlate = new MCAModelRenderer(this, "BottomPlate", 18, 50);
bottomPlate.mirror = false;
bottomPlate.addBox(-3.0F, -1.0F, -3.0F, 6, 2, 6);
bottomPlate.setInitialRotationPoint(0.0F, -12.0F, 0.0F);
bottomPlate.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
bottomPlate.setTextureSize(128, 128);
parts.put(bottomPlate.boxName, bottomPlate);
body.addChild(bottomPlate);

topPlate = new MCAModelRenderer(this, "TopPlate", 18, 50);
topPlate.mirror = false;
topPlate.addBox(-3.0F, -1.0F, -3.0F, 6, 2, 6);
topPlate.setInitialRotationPoint(0.0F, 12.0F, 0.0F);
topPlate.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
topPlate.setTextureSize(128, 128);
parts.put(topPlate.boxName, topPlate);
body.addChild(topPlate);

sphere1 = new MCAModelRenderer(this, "Sphere1", 18, 40);
sphere1.mirror = false;
sphere1.addBox(-2.0F, 21.0F, -2.0F, 4, 4, 4);
sphere1.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
sphere1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
sphere1.setTextureSize(128, 128);
parts.put(sphere1.boxName, sphere1);
body.addChild(sphere1);

sphere2 = new MCAModelRenderer(this, "Sphere2", 18, 40);
sphere2.mirror = false;
sphere2.addBox(-2.0F, -25.0F, -2.0F, 4, 4, 4);
sphere2.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
sphere2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
sphere2.setTextureSize(128, 128);
parts.put(sphere2.boxName, sphere2);
body.addChild(sphere2);

pillar1 = new MCAModelRenderer(this, "Pillar1", 6, 0);
pillar1.mirror = false;
pillar1.addBox(-0.5F, -10.0F, 8.0F, 1, 20, 1);
pillar1.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
pillar1.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
pillar1.setTextureSize(128, 128);
parts.put(pillar1.boxName, pillar1);
body.addChild(pillar1);

pillar2 = new MCAModelRenderer(this, "Pillar2", 0, 0);
pillar2.mirror = false;
pillar2.addBox(-0.5F, -9.0F, 10.0F, 1, 18, 1);
pillar2.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
pillar2.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.38268346F, 0.0F, 0.9238795F)).transpose());
pillar2.setTextureSize(128, 128);
parts.put(pillar2.boxName, pillar2);
body.addChild(pillar2);

pillar3 = new MCAModelRenderer(this, "Pillar3", 6, 0);
pillar3.mirror = false;
pillar3.addBox(-0.5F, -10.0F, 8.0F, 1, 20, 1);
pillar3.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
pillar3.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.70710677F, 0.0F, 0.70710677F)).transpose());
pillar3.setTextureSize(128, 128);
parts.put(pillar3.boxName, pillar3);
body.addChild(pillar3);

pillar4 = new MCAModelRenderer(this, "Pillar4", 0, 0);
pillar4.mirror = false;
pillar4.addBox(-0.5F, -9.0F, 10.0F, 1, 18, 1);
pillar4.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
pillar4.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.9238795F, 0.0F, 0.38268343F)).transpose());
pillar4.setTextureSize(128, 128);
parts.put(pillar4.boxName, pillar4);
body.addChild(pillar4);

pillar5 = new MCAModelRenderer(this, "Pillar5", 6, 0);
pillar5.mirror = false;
pillar5.addBox(-0.5F, -10.0F, 8.0F, 1, 20, 1);
pillar5.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
pillar5.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, -1.0F, 0.0F, -4.371139E-8F)).transpose());
pillar5.setTextureSize(128, 128);
parts.put(pillar5.boxName, pillar5);
body.addChild(pillar5);

pillar6 = new MCAModelRenderer(this, "Pillar6", 0, 0);
pillar6.mirror = false;
pillar6.addBox(-0.5F, -9.0F, 10.0F, 1, 18, 1);
pillar6.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
pillar6.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, -0.9238795F, 0.0F, 0.38268343F)).transpose());
pillar6.setTextureSize(128, 128);
parts.put(pillar6.boxName, pillar6);
body.addChild(pillar6);

pillar8 = new MCAModelRenderer(this, "Pillar8", 0, 0);
pillar8.mirror = false;
pillar8.addBox(-0.5F, -9.0F, 10.0F, 1, 18, 1);
pillar8.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
pillar8.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, -0.38268346F, 0.0F, 0.9238795F)).transpose());
pillar8.setTextureSize(128, 128);
parts.put(pillar8.boxName, pillar8);
body.addChild(pillar8);

pillar7 = new MCAModelRenderer(this, "Pillar7", 6, 0);
pillar7.mirror = false;
pillar7.addBox(-0.5F, -10.0F, 8.0F, 1, 20, 1);
pillar7.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
pillar7.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, -0.70710677F, 0.0F, 0.70710677F)).transpose());
pillar7.setTextureSize(128, 128);
parts.put(pillar7.boxName, pillar7);
body.addChild(pillar7);

}

@Override
public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) 
{
EntitySplinterDrone entity = (EntitySplinterDrone)par1Entity;

AnimationHandler.performAnimationInModel(parts, entity);

//Render every non-child part
body.render(par7);
}
@Override
public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {}

public MCAModelRenderer getModelRendererFromName(String name)
{
return parts.get(name);
}
}