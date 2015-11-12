package yourModPackage.common.animations.SplinterDroneProjectile;

import yourModPackage.common.MCACommonLibrary.animation.*;
import yourModPackage.common.MCACommonLibrary.math.*;

public class ChannelSping extends Channel {
	public ChannelSping(String _name, float _fps, int _totalFrames, byte _mode) {
		super(_name, _fps, _totalFrames, _mode);
	}

	@Override
	protected void initializeAllFrames() {
KeyFrame frame0 = new KeyFrame();
frame0.modelRenderersRotations.put("body", new Quaternion(0.083405174F, -0.372392F, 0.21956569F, 0.8978634F));
frame0.modelRenderersTranslations.put("body", new Vector3f(0.0F, 0.0F, 0.0F));
keyFrames.put(0, frame0);

KeyFrame frame68 = new KeyFrame();
frame68.modelRenderersRotations.put("body", new Quaternion(-0.18436967F, -0.70473063F, 0.5144693F, 0.4524201F));
frame68.modelRenderersTranslations.put("body", new Vector3f(0.0F, 0.0F, 0.0F));
keyFrames.put(68, frame68);

KeyFrame frame81 = new KeyFrame();
frame81.modelRenderersRotations.put("body", new Quaternion(-0.5597586F, 0.1650148F, 0.80388933F, 0.1149019F));
frame81.modelRenderersTranslations.put("body", new Vector3f(0.0F, 0.0F, 0.0F));
keyFrames.put(81, frame81);

KeyFrame frame54 = new KeyFrame();
frame54.modelRenderersRotations.put("body", new Quaternion(-0.5691636F, 0.43343335F, -0.5302429F, 0.45500636F));
frame54.modelRenderersTranslations.put("body", new Vector3f(0.0F, 0.0F, 0.0F));
keyFrames.put(54, frame54);

KeyFrame frame99 = new KeyFrame();
frame99.modelRenderersRotations.put("body", new Quaternion(-0.81073284F, 0.077038616F, 0.52543205F, 0.24637026F));
frame99.modelRenderersTranslations.put("body", new Vector3f(0.0F, 0.0F, 0.0F));
keyFrames.put(99, frame99);

KeyFrame frame41 = new KeyFrame();
frame41.modelRenderersRotations.put("body", new Quaternion(-0.14718205F, 0.24816278F, -0.38480353F, 0.8767433F));
frame41.modelRenderersTranslations.put("body", new Vector3f(0.0F, 0.0F, 0.0F));
keyFrames.put(41, frame41);

KeyFrame frame26 = new KeyFrame();
frame26.modelRenderersRotations.put("body", new Quaternion(-0.75852734F, 0.26399457F, -0.5914923F, 0.07127336F));
frame26.modelRenderersTranslations.put("body", new Vector3f(0.0F, 0.0F, 0.0F));
keyFrames.put(26, frame26);

KeyFrame frame13 = new KeyFrame();
frame13.modelRenderersRotations.put("body", new Quaternion(0.3145363F, -0.04252225F, 0.76564854F, 0.5595007F));
frame13.modelRenderersTranslations.put("body", new Vector3f(0.0F, 0.0F, 0.0F));
keyFrames.put(13, frame13);

KeyFrame frame90 = new KeyFrame();
frame90.modelRenderersRotations.put("body", new Quaternion(0.604039F, -0.683623F, -0.30258617F, 0.27611208F));
frame90.modelRenderersTranslations.put("body", new Vector3f(0.0F, 0.0F, 0.0F));
keyFrames.put(90, frame90);

}
}