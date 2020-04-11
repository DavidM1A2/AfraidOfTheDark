package com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation;

import com.davidm1a2.afraidofthedark.AfraidOfTheDark;
import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Vector3f;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public abstract class AnimationHandler {
    /**
     * List of all the activate animations of this Entity.
     */
    public List<Channel> animCurrentChannels = new ArrayList<Channel>();
    /**
     * Previous time of every active animation.
     */
    public HashMap<String, Long> animPrevTime = new HashMap<String, Long>();
    /**
     * Current frame of every active animation.
     */
    public HashMap<String, Float> animCurrentFrame = new HashMap<String, Float>();
    /**
     * Owner of this handler.
     */
    private IMCAnimatedModel animatedModel;

    public AnimationHandler(IMCAnimatedModel model) {
        //AfraidOfTheDark.INSTANCE.getAnimationHandler().addEntity(entity);
        animatedModel = model;
    }

    /**
     * Update animation values. Return false if the animation should stop.
     */
    public static boolean updateAnimation(IMCAnimatedModel model, Channel channel, HashMap<String, Long> prevTimeAnim, HashMap<String, Float> prevFrameAnim) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer() || (FMLCommonHandler.instance().getEffectiveSide().isClient() && !isGamePaused())) {
            if (!(channel.mode == Channel.CUSTOM)) {
                long prevTime = prevTimeAnim.get(channel.name);
                float prevFrame = prevFrameAnim.get(channel.name);

                long currentTime = System.nanoTime();
                double deltaTime = (currentTime - prevTime) / 1000000000.0;
                float numberOfSkippedFrames = (float) (deltaTime * channel.fps);

                float currentFrame = prevFrame + numberOfSkippedFrames;

                if (currentFrame < channel.totalFrames - 1) //-1 as the first frame mustn't be "executed" as it is the starting situation
                {
                    prevTimeAnim.put(channel.name, currentTime);
                    prevFrameAnim.put(channel.name, currentFrame);
                    return true;
                } else {
                    if (channel.mode == Channel.LOOP) {
                        prevTimeAnim.put(channel.name, currentTime);
                        prevFrameAnim.put(channel.name, 0F);
                        return true;
                    }
                    return false;
                }
            } else {
                return true;
            }
        } else {
            long currentTime = System.nanoTime();
            prevTimeAnim.put(channel.name, currentTime);
            return true;
        }
    }

    @SideOnly(Side.CLIENT)
    private static boolean isGamePaused() {
        net.minecraft.client.Minecraft MC = net.minecraft.client.Minecraft.getMinecraft();
        return MC.isSingleplayer() && MC.currentScreen != null && MC.currentScreen.doesGuiPauseGame() && !MC.getIntegratedServer().getPublic();
    }

    /**
     * Apply animations if running or apply initial values. Must be called only by the model class.
     */
    @SideOnly(Side.CLIENT)
    public static void performAnimationInModel(Map<String, MCAModelRenderer> parts, IMCAnimatedModel model) {
        for (Map.Entry<String, MCAModelRenderer> entry : parts.entrySet()) {
            String boxName = entry.getKey();
            MCAModelRenderer box = entry.getValue();

            boolean anyRotationApplied = false;
            boolean anyTranslationApplied = false;
            boolean anyCustomAnimationRunning = false;

            for (Channel channel : model.getAnimationHandler().animCurrentChannels) {
                if (channel.mode != Channel.CUSTOM) {
                    float currentFrame = model.getAnimationHandler().animCurrentFrame.get(channel.name);

                    //Rotations
                    KeyFrame prevRotationKeyFrame = channel.getPreviousRotationKeyFrameForBox(boxName, model.getAnimationHandler().animCurrentFrame.get(channel.name));
                    int prevRotationKeyFramePosition = prevRotationKeyFrame != null ? channel.getKeyFramePosition(prevRotationKeyFrame) : 0;

                    KeyFrame nextRotationKeyFrame = channel.getNextRotationKeyFrameForBox(boxName, model.getAnimationHandler().animCurrentFrame.get(channel.name));
                    int nextRotationKeyFramePosition = nextRotationKeyFrame != null ? channel.getKeyFramePosition(nextRotationKeyFrame) : 0;

                    float SLERPProgress = (currentFrame - prevRotationKeyFramePosition) / (nextRotationKeyFramePosition - prevRotationKeyFramePosition);
                    if (SLERPProgress > 1F || SLERPProgress < 0F) {
                        SLERPProgress = 1F;
                    }

                    if (prevRotationKeyFramePosition == 0 && prevRotationKeyFrame == null && !(nextRotationKeyFramePosition == 0)) {
                        Quaternion currentQuat = new Quaternion();
                        currentQuat.slerp(parts.get(boxName).getDefaultRotationAsQuaternion(), nextRotationKeyFrame.modelRenderersRotations.get(boxName), SLERPProgress);
                        box.getRotationMatrix().set(currentQuat).transpose();

                        anyRotationApplied = true;
                    } else if (prevRotationKeyFramePosition == 0 && prevRotationKeyFrame != null && !(nextRotationKeyFramePosition == 0)) {
                        Quaternion currentQuat = new Quaternion();
                        currentQuat.slerp(prevRotationKeyFrame.modelRenderersRotations.get(boxName), nextRotationKeyFrame.modelRenderersRotations.get(boxName), SLERPProgress);
                        box.getRotationMatrix().set(currentQuat).transpose();

                        anyRotationApplied = true;
                    } else if (!(prevRotationKeyFramePosition == 0) && !(nextRotationKeyFramePosition == 0)) {
                        Quaternion currentQuat = new Quaternion();
                        currentQuat.slerp(prevRotationKeyFrame.modelRenderersRotations.get(boxName), nextRotationKeyFrame.modelRenderersRotations.get(boxName), SLERPProgress);
                        box.getRotationMatrix().set(currentQuat).transpose();

                        anyRotationApplied = true;
                    }

                    //Translations
                    KeyFrame prevTranslationKeyFrame = channel.getPreviousTranslationKeyFrameForBox(boxName, model.getAnimationHandler().animCurrentFrame.get(channel.name));
                    int prevTranslationsKeyFramePosition = prevTranslationKeyFrame != null ? channel.getKeyFramePosition(prevTranslationKeyFrame) : 0;

                    KeyFrame nextTranslationKeyFrame = channel.getNextTranslationKeyFrameForBox(boxName, model.getAnimationHandler().animCurrentFrame.get(channel.name));
                    int nextTranslationsKeyFramePosition = nextTranslationKeyFrame != null ? channel.getKeyFramePosition(nextTranslationKeyFrame) : 0;

                    float LERPProgress = (currentFrame - prevTranslationsKeyFramePosition) / (nextTranslationsKeyFramePosition - prevTranslationsKeyFramePosition);
                    if (LERPProgress > 1F) {
                        LERPProgress = 1F;
                    }

                    if (prevTranslationsKeyFramePosition == 0 && prevTranslationKeyFrame == null && !(nextTranslationsKeyFramePosition == 0)) {
                        Vector3f startPosition = parts.get(boxName).getPositionAsVector();
                        Vector3f endPosition = nextTranslationKeyFrame.modelRenderersTranslations.get(boxName);
                        Vector3f currentPosition = new Vector3f(startPosition);
                        currentPosition.interpolate(endPosition, LERPProgress);
                        box.setRotationPoint(currentPosition.x, currentPosition.y, currentPosition.z);

                        anyTranslationApplied = true;
                    } else if (prevTranslationsKeyFramePosition == 0 && prevTranslationKeyFrame != null && !(nextTranslationsKeyFramePosition == 0)) {
                        Vector3f startPosition = prevTranslationKeyFrame.modelRenderersTranslations.get(boxName);
                        Vector3f endPosition = nextTranslationKeyFrame.modelRenderersTranslations.get(boxName);
                        Vector3f currentPosition = new Vector3f(startPosition);
                        currentPosition.interpolate(endPosition, LERPProgress);
                        box.setRotationPoint(currentPosition.x, currentPosition.y, currentPosition.z);
                    } else if (!(prevTranslationsKeyFramePosition == 0) && !(nextTranslationsKeyFramePosition == 0)) {
                        Vector3f startPosition = prevTranslationKeyFrame.modelRenderersTranslations.get(boxName);
                        Vector3f endPosition = nextTranslationKeyFrame.modelRenderersTranslations.get(boxName);
                        Vector3f currentPosition = new Vector3f(startPosition);
                        currentPosition.interpolate(endPosition, LERPProgress);
                        box.setRotationPoint(currentPosition.x, currentPosition.y, currentPosition.z);

                        anyTranslationApplied = true;
                    }
                } else {
                    anyCustomAnimationRunning = true;

                    ((CustomChannel) channel).update(parts, model);
                }
            }

            //Set the initial values for each box if necessary
            if (!anyRotationApplied && !anyCustomAnimationRunning) {
                box.resetRotationMatrix();
            }
            if (!anyTranslationApplied && !anyCustomAnimationRunning) {
                box.resetRotationPoint();
            }
        }
    }

    public IMCAnimatedModel getEntity() {
        return animatedModel;
    }

    public void activateAnimation(Map<String, Channel> animChannels, String name, float startingFrame) {
        if (animChannels.get(name) != null) {
            Channel selectedChannel = animChannels.get(name);
            int indexToRemove = animCurrentChannels.indexOf(selectedChannel);
            if (indexToRemove != -1) {
                animCurrentChannels.remove(indexToRemove);
            }

            animCurrentChannels.add(selectedChannel);
            animPrevTime.put(name, System.nanoTime());
            animCurrentFrame.put(name, startingFrame);
        } else {
            System.out.println("The animation called " + name + " doesn't exist!");
        }
    }

    public abstract void activateAnimation(String name, float startingFrame);

    public void stopAnimation(Map<String, Channel> animChannels, String name) {
        Channel selectedChannel = animChannels.get(name);
        if (selectedChannel != null) {
            int indexToRemove = animCurrentChannels.indexOf(selectedChannel);
            if (indexToRemove != -1) {
                animCurrentChannels.remove(indexToRemove);
                animPrevTime.remove(name);
                animCurrentFrame.remove(name);
            }
        } else {
            System.out.println("The animation called " + name + " doesn't exist!");
        }
    }

    public abstract void stopAnimation(String name);

    public void animationsUpdate() {
        ListIterator<Channel> it = animCurrentChannels.listIterator();
        while (it.hasNext()) {
            Channel anim = it.next();
            if (animCurrentFrame == null) {
                AfraidOfTheDark.INSTANCE.getLogger().info("animCurrentFrame = null");
            }
            if (anim == null) {
                AfraidOfTheDark.INSTANCE.getLogger().info("anim = null");
            }
            float prevFrame = animCurrentFrame.get(anim.name);
            boolean animStatus = updateAnimation(animatedModel, anim, animPrevTime, animCurrentFrame);

            if (!animStatus) {
                //channelsToRemove.add(anim);
                animPrevTime.remove(anim.name);
                animCurrentFrame.remove(anim.name);
                it.remove();
            }
        }

        //		/*
        //		 * Added this
        //		 */
        //		for (Channel channel : channelsToRemove)
        //		{
        //			animCurrentChannels.remove(channel);
        //			animPrevTime.remove(channel.name);
        //			animCurrentFrame.remove(channel.name);
        //			animationEvents.get(channel.name).clear();
        //		}
        //
        //		channelsToRemove.clear();
    }

    public boolean isAnimationActive(String name) {
        for (Channel anim : animatedModel.getAnimationHandler().animCurrentChannels) {
            if (anim.name.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
