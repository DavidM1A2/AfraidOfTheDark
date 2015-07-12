package yourModPackage.common.animations.New;

import java.util.HashMap;

import yourModPackage.common.MCACommonLibrary.IMCAnimatedEntity;
import yourModPackage.common.MCACommonLibrary.animation.AnimationHandler;
import yourModPackage.common.MCACommonLibrary.animation.Channel;

public class AnimationHandlerNew extends AnimationHandler {
	/** Map with all the animations. */
	public static HashMap<String, Channel> animChannels = new HashMap<String, Channel>();
static
{
animChannels.put("Walk", new ChannelWalk("Walk", 20.0F, 40, Channel.LOOP));
animChannels.put("Attack", new ChannelAttack("Attack", 30.0F, 20, Channel.LINEAR));
animChannels.put("Spawn", new ChannelSpawn("Spawn", 20.0F, 40, Channel.LINEAR));
animChannels.put("Idle", new ChannelIdle("Idle", 10.0F, 20, Channel.LOOP));
}
	public AnimationHandlerNew(IMCAnimatedEntity entity) {
		super(entity);
	}

	@Override
	public void activateAnimation(String name, float startingFrame) {
		super.activateAnimation(animChannels, name, startingFrame);
	}

	@Override
	public void stopAnimation(String name) {
		super.stopAnimation(animChannels, name);
	}

	@Override
	public void fireAnimationEventClientSide(Channel anim, float prevFrame, float frame) {
	}

	@Override
	public void fireAnimationEventServerSide(Channel anim, float prevFrame, float frame) {
	}}