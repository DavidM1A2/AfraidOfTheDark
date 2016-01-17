package yourModPackage.common.animations.SpellProjectile;

import yourModPackage.common.MCACommonLibrary.animation.*;
import yourModPackage.common.MCACommonLibrary.math.*;

public class ChannelIdle extends Channel {
	public ChannelIdle(String _name, float _fps, int _totalFrames, byte _mode) {
		super(_name, _fps, _totalFrames, _mode);
	}

	@Override
	protected void initializeAllFrames() {
KeyFrame frame0 = new KeyFrame();
frame0.modelRenderersRotations.put("Center", new Quaternion(0.19001609F, 0.14813289F, -0.31068635F, 0.91946965F));
frame0.modelRenderersTranslations.put("Center", new Vector3f(0.0F, 0.0F, 0.0F));
keyFrames.put(0, frame0);

KeyFrame frame17 = new KeyFrame();
frame17.modelRenderersRotations.put("Center", new Quaternion(0.7796292F, 0.61504066F, 0.11790937F, -7.956326E-4F));
frame17.modelRenderersTranslations.put("Center", new Vector3f(0.0F, 0.0F, 0.0F));
keyFrames.put(17, frame17);

KeyFrame frame39 = new KeyFrame();
frame39.modelRenderersRotations.put("Center", new Quaternion(-0.5435533F, 0.589524F, -0.5917744F, -0.0825492F));
frame39.modelRenderersTranslations.put("Center", new Vector3f(0.0F, 0.0F, 0.0F));
keyFrames.put(39, frame39);

KeyFrame frame59 = new KeyFrame();
frame59.modelRenderersRotations.put("Center", new Quaternion(0.4235078F, -0.40492296F, 0.3203981F, 0.74432755F));
frame59.modelRenderersTranslations.put("Center", new Vector3f(0.0F, 0.0F, 0.0F));
keyFrames.put(59, frame59);

}
}