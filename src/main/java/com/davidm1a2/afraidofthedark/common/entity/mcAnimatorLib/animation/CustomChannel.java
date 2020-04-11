package com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation;

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer;
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel;

import java.util.Map;

/**
 * Class was provided by the MC animator library
 */
public class CustomChannel extends Channel {

    public CustomChannel(String _name) {
        super(_name);
        this.mode = Channel.CUSTOM;
    }

    @Override
    protected void initializeAllFrames() {
    }

    @Override
    public KeyFrame getPreviousRotationKeyFrameForBox(String boxName, float currentFrame) {
        return null;
    }

    @Override
    public KeyFrame getNextRotationKeyFrameForBox(String boxName, float currentFrame) {
        return null;
    }

    @Override
    public KeyFrame getPreviousTranslationKeyFrameForBox(String boxName, float currentFrame) {
        return null;
    }

    @Override
    public KeyFrame getNextTranslationKeyFrameForBox(String boxName, float currentFrame) {
        return null;
    }

    @Override
    public int getKeyFramePosition(KeyFrame keyFrame) {
        return -1;
    }

    /**
     * Write the actual behaviour of this custom animation here. It will called every tick until the animation is active.
     */
    public void update(Map<String, MCAModelRenderer> parts, IMCAnimatedModel model) {
        //This must be filled in the actual custom channels!
    }
}
