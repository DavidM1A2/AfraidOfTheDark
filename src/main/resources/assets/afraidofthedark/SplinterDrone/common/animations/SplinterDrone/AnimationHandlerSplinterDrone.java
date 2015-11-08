package yourModPackage.common.animations.SplinterDrone;

import java.util.HashMap;

import yourModPackage.common.MCACommonLibrary.IMCAnimatedEntity;
import yourModPackage.common.MCACommonLibrary.animation.AnimationHandler;
import yourModPackage.common.MCACommonLibrary.animation.Channel;

public class AnimationHandlerSplinterDrone extends AnimationHandler {
	/** Map with all the animations. */
	public static HashMap<String, Channel> animChannels = new HashMap<String, Channel>();
static
{
animChannels.put("Activate", new ChannelActivate("Activate", 25.0F, 100, Channel.LINEAR));
animChannels.put("Charge", new ChannelCharge("Charge", 100.0F, 100, Channel.LOOP));
animChannels.put("Idle", new ChannelIdle("Idle", 25.0F, 100, Channel.LOOP));
}
	public AnimationHandlerSplinterDrone(IMCAnimatedEntity entity) {
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