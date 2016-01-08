package com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.DavidM1A2.AfraidOfTheDark.client.MCAClientLibrary.MCAModelRenderer;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.math.Quaternion;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.math.Vector3f;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AnimationHandler
{
	public static AnimTickHandler animTickHandler = new AnimTickHandler();
	/** Owner of this handler. */
	private IMCAnimatedEntity animatedEntity;
	/** List of all the activate animations of this Entity. */
	public List<Channel> animCurrentChannels = new ArrayList<Channel>();

	/** Previous time of every active animation. */
	public HashMap<String, Long> animPrevTime = new HashMap<String, Long>();
	/** Current frame of every active animation. */
	public HashMap<String, Float> animCurrentFrame = new HashMap<String, Float>();

	public AnimationHandler(IMCAnimatedEntity entity)
	{
		animTickHandler.addEntity(entity);
		animatedEntity = entity;
	}

	public IMCAnimatedEntity getEntity()
	{
		return animatedEntity;
	}

	public void activateAnimation(HashMap<String, Channel> animChannels, String name, float startingFrame)
	{
		if (animChannels.get(name) != null)
		{
			Channel selectedChannel = animChannels.get(name);
			int indexToRemove = animCurrentChannels.indexOf(selectedChannel);
			if (indexToRemove != -1)
			{
				animCurrentChannels.remove(indexToRemove);
			}

			animCurrentChannels.add(selectedChannel);
			animPrevTime.put(name, System.nanoTime());
			animCurrentFrame.put(name, startingFrame);
		}
		else
		{
			System.out.println("The animation called " + name + " doesn't exist!");
		}
	}

	public abstract void activateAnimation(String name, float startingFrame);

	public void stopAnimation(HashMap<String, Channel> animChannels, String name)
	{
		Channel selectedChannel = animChannels.get(name);
		if (selectedChannel != null)
		{
			int indexToRemove = animCurrentChannels.indexOf(selectedChannel);
			if (indexToRemove != -1)
			{
				animCurrentChannels.remove(indexToRemove);
				animPrevTime.remove(name);
				animCurrentFrame.remove(name);
			}
		}
		else
		{
			System.out.println("The animation called " + name + " doesn't exist!");
		}
	}

	public abstract void stopAnimation(String name);

	public void animationsUpdate()
	{
		ListIterator<Channel> it = animCurrentChannels.listIterator();
		while (it.hasNext())
		{
			Channel anim = it.next();
			if (animCurrentFrame == null)
				LogHelper.info("animCurrentFrame = null");
			if (anim == null)
				LogHelper.info("anim = null");
			float prevFrame = animCurrentFrame.get(anim.name);
			boolean animStatus = updateAnimation(animatedEntity, anim, animPrevTime, animCurrentFrame);

			if (!animStatus)
			{
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

	public boolean isAnimationActive(String name)
	{
		for (Channel anim : animatedEntity.getAnimationHandler().animCurrentChannels)
		{
			if (anim.name.equals(name))
			{
				return true;
			}
		}
		return false;
	}

	/** Update animation values. Return false if the animation should stop. */
	public static boolean updateAnimation(IMCAnimatedEntity entity, Channel channel, HashMap<String, Long> prevTimeAnim, HashMap<String, Float> prevFrameAnim)
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isServer() || (FMLCommonHandler.instance().getEffectiveSide().isClient() && !isGamePaused()))
		{
			if (!(channel.mode == Channel.CUSTOM))
			{
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
				}
				else
				{
					if (channel.mode == Channel.LOOP)
					{
						prevTimeAnim.put(channel.name, currentTime);
						prevFrameAnim.put(channel.name, 0F);
						return true;
					}
					return false;
				}
			}
			else
			{
				return true;
			}
		}
		else
		{
			long currentTime = System.nanoTime();
			prevTimeAnim.put(channel.name, currentTime);
			return true;
		}
	}

	@SideOnly(Side.CLIENT)
	private static boolean isGamePaused()
	{
		net.minecraft.client.Minecraft MC = net.minecraft.client.Minecraft.getMinecraft();
		return MC.isSingleplayer() && MC.currentScreen != null && MC.currentScreen.doesGuiPauseGame() && !MC.getIntegratedServer().getPublic();
	}

	/**
	 * Apply animations if running or apply initial values. Must be called only by the model class.
	 */
	@SideOnly(Side.CLIENT)
	public static void performAnimationInModel(HashMap<String, MCAModelRenderer> parts, IMCAnimatedEntity entity)
	{
		for (Map.Entry<String, MCAModelRenderer> entry : parts.entrySet())
		{
			String boxName = entry.getKey();
			MCAModelRenderer box = entry.getValue();

			boolean anyRotationApplied = false;
			boolean anyTranslationApplied = false;
			boolean anyCustomAnimationRunning = false;

			for (Channel channel : entity.getAnimationHandler().animCurrentChannels)
			{
				if (channel.mode != Channel.CUSTOM)
				{
					float currentFrame = entity.getAnimationHandler().animCurrentFrame.get(channel.name);

					//Rotations
					KeyFrame prevRotationKeyFrame = channel.getPreviousRotationKeyFrameForBox(boxName, entity.getAnimationHandler().animCurrentFrame.get(channel.name));
					int prevRotationKeyFramePosition = prevRotationKeyFrame != null ? channel.getKeyFramePosition(prevRotationKeyFrame) : 0;

					KeyFrame nextRotationKeyFrame = channel.getNextRotationKeyFrameForBox(boxName, entity.getAnimationHandler().animCurrentFrame.get(channel.name));
					int nextRotationKeyFramePosition = nextRotationKeyFrame != null ? channel.getKeyFramePosition(nextRotationKeyFrame) : 0;

					float SLERPProgress = (currentFrame - prevRotationKeyFramePosition) / (nextRotationKeyFramePosition - prevRotationKeyFramePosition);
					if (SLERPProgress > 1F || SLERPProgress < 0F)
					{
						SLERPProgress = 1F;
					}

					if (prevRotationKeyFramePosition == 0 && prevRotationKeyFrame == null && !(nextRotationKeyFramePosition == 0))
					{
						Quaternion currentQuat = new Quaternion();
						currentQuat.slerp(parts.get(boxName).getDefaultRotationAsQuaternion(), nextRotationKeyFrame.modelRenderersRotations.get(boxName), SLERPProgress);
						box.getRotationMatrix().set(currentQuat).transpose();

						anyRotationApplied = true;
					}
					else if (prevRotationKeyFramePosition == 0 && prevRotationKeyFrame != null && !(nextRotationKeyFramePosition == 0))
					{
						Quaternion currentQuat = new Quaternion();
						currentQuat.slerp(prevRotationKeyFrame.modelRenderersRotations.get(boxName), nextRotationKeyFrame.modelRenderersRotations.get(boxName), SLERPProgress);
						box.getRotationMatrix().set(currentQuat).transpose();

						anyRotationApplied = true;
					}
					else if (!(prevRotationKeyFramePosition == 0) && !(nextRotationKeyFramePosition == 0))
					{
						Quaternion currentQuat = new Quaternion();
						currentQuat.slerp(prevRotationKeyFrame.modelRenderersRotations.get(boxName), nextRotationKeyFrame.modelRenderersRotations.get(boxName), SLERPProgress);
						box.getRotationMatrix().set(currentQuat).transpose();

						anyRotationApplied = true;
					}

					//Translations
					KeyFrame prevTranslationKeyFrame = channel.getPreviousTranslationKeyFrameForBox(boxName, entity.getAnimationHandler().animCurrentFrame.get(channel.name));
					int prevTranslationsKeyFramePosition = prevTranslationKeyFrame != null ? channel.getKeyFramePosition(prevTranslationKeyFrame) : 0;

					KeyFrame nextTranslationKeyFrame = channel.getNextTranslationKeyFrameForBox(boxName, entity.getAnimationHandler().animCurrentFrame.get(channel.name));
					int nextTranslationsKeyFramePosition = nextTranslationKeyFrame != null ? channel.getKeyFramePosition(nextTranslationKeyFrame) : 0;

					float LERPProgress = (currentFrame - prevTranslationsKeyFramePosition) / (nextTranslationsKeyFramePosition - prevTranslationsKeyFramePosition);
					if (LERPProgress > 1F)
					{
						LERPProgress = 1F;
					}

					if (prevTranslationsKeyFramePosition == 0 && prevTranslationKeyFrame == null && !(nextTranslationsKeyFramePosition == 0))
					{
						Vector3f startPosition = parts.get(boxName).getPositionAsVector();
						Vector3f endPosition = nextTranslationKeyFrame.modelRenderersTranslations.get(boxName);
						Vector3f currentPosition = new Vector3f(startPosition);
						currentPosition.interpolate(endPosition, LERPProgress);
						box.setRotationPoint(currentPosition.x, currentPosition.y, currentPosition.z);

						anyTranslationApplied = true;
					}
					else if (prevTranslationsKeyFramePosition == 0 && prevTranslationKeyFrame != null && !(nextTranslationsKeyFramePosition == 0))
					{
						Vector3f startPosition = prevTranslationKeyFrame.modelRenderersTranslations.get(boxName);
						Vector3f endPosition = nextTranslationKeyFrame.modelRenderersTranslations.get(boxName);
						Vector3f currentPosition = new Vector3f(startPosition);
						currentPosition.interpolate(endPosition, LERPProgress);
						box.setRotationPoint(currentPosition.x, currentPosition.y, currentPosition.z);
					}
					else if (!(prevTranslationsKeyFramePosition == 0) && !(nextTranslationsKeyFramePosition == 0))
					{
						Vector3f startPosition = prevTranslationKeyFrame.modelRenderersTranslations.get(boxName);
						Vector3f endPosition = nextTranslationKeyFrame.modelRenderersTranslations.get(boxName);
						Vector3f currentPosition = new Vector3f(startPosition);
						currentPosition.interpolate(endPosition, LERPProgress);
						box.setRotationPoint(currentPosition.x, currentPosition.y, currentPosition.z);

						anyTranslationApplied = true;
					}
				}
				else
				{
					anyCustomAnimationRunning = true;

					((CustomChannel) channel).update(parts, entity);
				}
			}

			//Set the initial values for each box if necessary
			if (!anyRotationApplied && !anyCustomAnimationRunning)
			{
				box.resetRotationMatrix();
			}
			if (!anyTranslationApplied && !anyCustomAnimationRunning)
			{
				box.resetRotationPoint();
			}
		}
	}
}
