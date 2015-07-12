package yourModPackage.client.models;

import java.util.HashMap;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

import yourModPackage.client.MCAClientLibrary.MCAModelRenderer;
import yourModPackage.common.MCACommonLibrary.MCAVersionChecker;
import yourModPackage.common.MCACommonLibrary.animation.AnimationHandler;
import yourModPackage.common.MCACommonLibrary.math.Matrix4f;
import yourModPackage.common.MCACommonLibrary.math.Quaternion;
import yourModPackage.common.entities.mobs.EntityNew;

public class ModelNew extends ModelBase {
public final int MCA_MIN_REQUESTED_VERSION = 5;
public HashMap<String, MCAModelRenderer> parts = new HashMap<String, MCAModelRenderer>();

MCAModelRenderer body;
MCAModelRenderer head;
MCAModelRenderer rightarm;
MCAModelRenderer leftarm;
MCAModelRenderer rightleg;
MCAModelRenderer leftleg;
MCAModelRenderer heart;

public ModelNew()
{
MCAVersionChecker.checkForLibraryVersion(getClass(), MCA_MIN_REQUESTED_VERSION);

textureWidth = 64;
textureHeight = 32;

body = new MCAModelRenderer(this, "body", 16, 16);
body.mirror = false;
body.addBox(-4.0F, -12.0F, -2.0F, 8, 12, 4);
body.setInitialRotationPoint(0.0F, 2.0F, 2.0F);
body.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
body.setTextureSize(64, 32);
parts.put(body.boxName, body);

head = new MCAModelRenderer(this, "head", 0, 0);
head.mirror = false;
head.addBox(-4.0F, 0.0F, -4.0F, 8, 8, 8);
head.setInitialRotationPoint(0.0F, 0.0F, 0.0F);
head.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
head.setTextureSize(64, 32);
parts.put(head.boxName, head);
body.addChild(head);

rightarm = new MCAModelRenderer(this, "rightarm", 40, 16);
rightarm.mirror = false;
rightarm.addBox(-2.0F, -10.0F, -1.0F, 2, 12, 2);
rightarm.setInitialRotationPoint(-4.0F, -2.0F, 0.0F);
rightarm.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
rightarm.setTextureSize(64, 32);
parts.put(rightarm.boxName, rightarm);
body.addChild(rightarm);

leftarm = new MCAModelRenderer(this, "leftarm", 40, 16);
leftarm.mirror = false;
leftarm.addBox(0.0F, -10.0F, -1.0F, 2, 12, 2);
leftarm.setInitialRotationPoint(4.0F, -2.0F, 0.0F);
leftarm.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
leftarm.setTextureSize(64, 32);
parts.put(leftarm.boxName, leftarm);
body.addChild(leftarm);

rightleg = new MCAModelRenderer(this, "rightleg", 0, 16);
rightleg.mirror = false;
rightleg.addBox(-1.0F, -12.0F, -1.0F, 2, 12, 2);
rightleg.setInitialRotationPoint(-2.0F, -12.0F, 0.0F);
rightleg.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
rightleg.setTextureSize(64, 32);
parts.put(rightleg.boxName, rightleg);
body.addChild(rightleg);

leftleg = new MCAModelRenderer(this, "leftleg", 0, 16);
leftleg.mirror = false;
leftleg.addBox(-1.0F, -12.0F, -1.0F, 2, 12, 2);
leftleg.setInitialRotationPoint(2.0F, -12.0F, 0.0F);
leftleg.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
leftleg.setTextureSize(64, 32);
parts.put(leftleg.boxName, leftleg);
body.addChild(leftleg);

heart = new MCAModelRenderer(this, "heart", 48, 4);
heart.mirror = false;
heart.addBox(-1.5F, -2.0F, -1.0F, 3, 3, 2);
heart.setInitialRotationPoint(0.0F, -3.0F, 0.0F);
heart.setInitialRotationMatrix(new Matrix4f().set(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F)).transpose());
heart.setTextureSize(64, 32);
parts.put(heart.boxName, heart);
body.addChild(heart);

}

@Override
public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) 
{
EntityNew entity = (EntityNew)par1Entity;

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