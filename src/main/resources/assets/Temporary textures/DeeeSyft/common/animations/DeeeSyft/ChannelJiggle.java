package yourModPackage.common.animations.DeeeSyft;

import yourModPackage.common.MCACommonLibrary.animation.*;
import yourModPackage.common.MCACommonLibrary.math.*;

public class ChannelJiggle extends Channel {
	public ChannelJiggle(String _name, float _fps, int _totalFrames, byte _mode) {
		super(_name, _fps, _totalFrames, _mode);
	}

	@Override
	protected void initializeAllFrames() {
KeyFrame frame0 = new KeyFrame();
frame0.modelRenderersRotations.put("body2", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
frame0.modelRenderersRotations.put("body5", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
frame0.modelRenderersRotations.put("body6", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
frame0.modelRenderersRotations.put("tentacle4", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
frame0.modelRenderersRotations.put("body1", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
frame0.modelRenderersRotations.put("body4", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
frame0.modelRenderersRotations.put("tentacle2", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
frame0.modelRenderersRotations.put("tentacle1", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
frame0.modelRenderersRotations.put("body3", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
frame0.modelRenderersRotations.put("tentacle3", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
frame0.modelRenderersTranslations.put("body2", new Vector3f(16.0F, 16.0F, 0.0F));
frame0.modelRenderersTranslations.put("body5", new Vector3f(16.0F, 32.0F, 16.0F));
frame0.modelRenderersTranslations.put("body6", new Vector3f(16.0F, 16.0F, 32.0F));
frame0.modelRenderersTranslations.put("tentacle4", new Vector3f(5.0F, 4.0F, 5.0F));
frame0.modelRenderersTranslations.put("body1", new Vector3f(0.0F, 16.0F, 16.0F));
frame0.modelRenderersTranslations.put("body4", new Vector3f(32.0F, 16.0F, 16.0F));
frame0.modelRenderersTranslations.put("tentacle2", new Vector3f(5.0F, 4.0F, 27.0F));
frame0.modelRenderersTranslations.put("tentacle1", new Vector3f(27.0F, 4.0F, 27.0F));
frame0.modelRenderersTranslations.put("body3", new Vector3f(16.0F, 0.0F, 16.0F));
frame0.modelRenderersTranslations.put("tentacle3", new Vector3f(27.0F, 4.0F, 5.0F));
keyFrames.put(0, frame0);

KeyFrame frame2 = new KeyFrame();
frame2.modelRenderersRotations.put("body2", new Quaternion(0.0F, 0.0F, -0.034899496F, 0.99939084F));
frame2.modelRenderersRotations.put("body5", new Quaternion(0.0F, -0.034899496F, 0.0F, 0.99939084F));
frame2.modelRenderersRotations.put("body6", new Quaternion(0.0F, 0.0F, 0.034899496F, 0.99939084F));
frame2.modelRenderersRotations.put("body1", new Quaternion(-0.034899496F, 0.0F, 0.0F, 0.99939084F));
frame2.modelRenderersRotations.put("body4", new Quaternion(0.034899496F, 0.0F, 0.0F, 0.99939084F));
frame2.modelRenderersRotations.put("body3", new Quaternion(0.0F, 0.034899496F, 0.0F, 0.99939084F));
frame2.modelRenderersTranslations.put("body2", new Vector3f(16.0F, 16.0F, 0.0F));
frame2.modelRenderersTranslations.put("body5", new Vector3f(16.0F, 32.0F, 16.0F));
frame2.modelRenderersTranslations.put("body6", new Vector3f(16.0F, 16.0F, 32.0F));
frame2.modelRenderersTranslations.put("body1", new Vector3f(0.0F, 16.0F, 16.0F));
frame2.modelRenderersTranslations.put("body4", new Vector3f(32.0F, 16.0F, 16.0F));
frame2.modelRenderersTranslations.put("body3", new Vector3f(16.0F, 0.0F, 16.0F));
keyFrames.put(2, frame2);

KeyFrame frame32 = new KeyFrame();
frame32.modelRenderersRotations.put("body2", new Quaternion(0.0F, 0.0F, -0.034899496F, 0.99939084F));
frame32.modelRenderersRotations.put("body5", new Quaternion(0.0F, -0.034899496F, 0.0F, 0.99939084F));
frame32.modelRenderersRotations.put("body6", new Quaternion(0.0F, 0.0F, 0.034899496F, 0.99939084F));
frame32.modelRenderersRotations.put("tentacle4", new Quaternion(0.017164472F, 0.37108392F, 0.19297858F, 0.9081637F));
frame32.modelRenderersRotations.put("body1", new Quaternion(-0.034899496F, 0.0F, 0.0F, 0.99939084F));
frame32.modelRenderersRotations.put("body4", new Quaternion(0.034899496F, 0.0F, 0.0F, 0.99939084F));
frame32.modelRenderersRotations.put("body3", new Quaternion(0.0F, 0.034899496F, 0.0F, 0.99939084F));
frame32.modelRenderersTranslations.put("body2", new Vector3f(16.0F, 16.0F, 0.0F));
frame32.modelRenderersTranslations.put("body5", new Vector3f(16.0F, 32.0F, 16.0F));
frame32.modelRenderersTranslations.put("body6", new Vector3f(16.0F, 16.0F, 32.0F));
frame32.modelRenderersTranslations.put("tentacle4", new Vector3f(5.0F, 4.0F, 5.0F));
frame32.modelRenderersTranslations.put("body1", new Vector3f(0.0F, 16.0F, 16.0F));
frame32.modelRenderersTranslations.put("body4", new Vector3f(32.0F, 16.0F, 16.0F));
frame32.modelRenderersTranslations.put("body3", new Vector3f(16.0F, 0.0F, 16.0F));
keyFrames.put(32, frame32);

KeyFrame frame5 = new KeyFrame();
frame5.modelRenderersRotations.put("tentacle3", new Quaternion(0.1464428F, 0.037872683F, 0.24749911F, 0.957008F));
frame5.modelRenderersTranslations.put("tentacle3", new Vector3f(27.0F, 4.0F, 5.0F));
keyFrames.put(5, frame5);

KeyFrame frame37 = new KeyFrame();
frame37.modelRenderersRotations.put("tentacle1", new Quaternion(0.28282663F, -0.011061838F, 0.021076465F, 0.9588757F));
frame37.modelRenderersTranslations.put("tentacle1", new Vector3f(27.0F, 4.0F, 27.0F));
keyFrames.put(37, frame37);

KeyFrame frame7 = new KeyFrame();
frame7.modelRenderersRotations.put("body2", new Quaternion(0.0F, 0.0F, 0.034899496F, 0.99939084F));
frame7.modelRenderersRotations.put("body5", new Quaternion(0.0F, 0.034899496F, 0.0F, 0.99939084F));
frame7.modelRenderersRotations.put("body6", new Quaternion(0.0F, 0.0F, -0.034899496F, 0.99939084F));
frame7.modelRenderersRotations.put("tentacle4", new Quaternion(0.17536087F, -0.058334798F, -0.3102118F, 0.9325311F));
frame7.modelRenderersRotations.put("body1", new Quaternion(0.034899496F, 0.0F, 0.0F, 0.99939084F));
frame7.modelRenderersRotations.put("body4", new Quaternion(-0.034899496F, 0.0F, 0.0F, 0.99939084F));
frame7.modelRenderersRotations.put("body3", new Quaternion(0.0F, -0.034899496F, 0.0F, 0.99939084F));
frame7.modelRenderersTranslations.put("body2", new Vector3f(16.0F, 16.0F, 0.0F));
frame7.modelRenderersTranslations.put("body5", new Vector3f(16.0F, 32.0F, 16.0F));
frame7.modelRenderersTranslations.put("body6", new Vector3f(16.0F, 16.0F, 32.0F));
frame7.modelRenderersTranslations.put("tentacle4", new Vector3f(5.0F, 4.0F, 5.0F));
frame7.modelRenderersTranslations.put("body1", new Vector3f(0.0F, 16.0F, 16.0F));
frame7.modelRenderersTranslations.put("body4", new Vector3f(32.0F, 16.0F, 16.0F));
frame7.modelRenderersTranslations.put("body3", new Vector3f(16.0F, 0.0F, 16.0F));
keyFrames.put(7, frame7);

KeyFrame frame42 = new KeyFrame();
frame42.modelRenderersRotations.put("body2", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
frame42.modelRenderersRotations.put("body5", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
frame42.modelRenderersRotations.put("body6", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
frame42.modelRenderersRotations.put("tentacle4", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
frame42.modelRenderersRotations.put("body1", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
frame42.modelRenderersRotations.put("body4", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
frame42.modelRenderersRotations.put("tentacle2", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
frame42.modelRenderersRotations.put("tentacle1", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
frame42.modelRenderersRotations.put("body3", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
frame42.modelRenderersRotations.put("tentacle3", new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));
frame42.modelRenderersTranslations.put("body2", new Vector3f(16.0F, 16.0F, 0.0F));
frame42.modelRenderersTranslations.put("body5", new Vector3f(16.0F, 32.0F, 16.0F));
frame42.modelRenderersTranslations.put("body6", new Vector3f(16.0F, 16.0F, 32.0F));
frame42.modelRenderersTranslations.put("tentacle4", new Vector3f(5.0F, 4.0F, 5.0F));
frame42.modelRenderersTranslations.put("body1", new Vector3f(0.0F, 16.0F, 16.0F));
frame42.modelRenderersTranslations.put("body4", new Vector3f(32.0F, 16.0F, 16.0F));
frame42.modelRenderersTranslations.put("tentacle2", new Vector3f(5.0F, 4.0F, 27.0F));
frame42.modelRenderersTranslations.put("tentacle1", new Vector3f(27.0F, 4.0F, 27.0F));
frame42.modelRenderersTranslations.put("body3", new Vector3f(16.0F, 0.0F, 16.0F));
frame42.modelRenderersTranslations.put("tentacle3", new Vector3f(27.0F, 4.0F, 5.0F));
keyFrames.put(42, frame42);

KeyFrame frame9 = new KeyFrame();
frame9.modelRenderersRotations.put("tentacle1", new Quaternion(-0.22777246F, 0.0548936F, -0.22777246F, 0.9451064F));
frame9.modelRenderersTranslations.put("tentacle1", new Vector3f(27.0F, 4.0F, 27.0F));
keyFrames.put(9, frame9);

KeyFrame frame11 = new KeyFrame();
frame11.modelRenderersRotations.put("tentacle2", new Quaternion(-0.11272627F, 0.033284064F, -0.28121564F, 0.9524194F));
frame11.modelRenderersTranslations.put("tentacle2", new Vector3f(5.0F, 4.0F, 27.0F));
keyFrames.put(11, frame11);

KeyFrame frame12 = new KeyFrame();
frame12.modelRenderersRotations.put("body2", new Quaternion(0.0F, 0.0F, -0.034899496F, 0.99939084F));
frame12.modelRenderersRotations.put("body5", new Quaternion(0.0F, -0.034899496F, 0.0F, 0.99939084F));
frame12.modelRenderersRotations.put("body6", new Quaternion(0.0F, 0.0F, 0.034899496F, 0.99939084F));
frame12.modelRenderersRotations.put("body1", new Quaternion(-0.034899496F, 0.0F, 0.0F, 0.99939084F));
frame12.modelRenderersRotations.put("body4", new Quaternion(0.034899496F, 0.0F, 0.0F, 0.99939084F));
frame12.modelRenderersRotations.put("body3", new Quaternion(0.0F, 0.034899496F, 0.0F, 0.99939084F));
frame12.modelRenderersTranslations.put("body2", new Vector3f(16.0F, 16.0F, 0.0F));
frame12.modelRenderersTranslations.put("body5", new Vector3f(16.0F, 32.0F, 16.0F));
frame12.modelRenderersTranslations.put("body6", new Vector3f(16.0F, 16.0F, 32.0F));
frame12.modelRenderersTranslations.put("body1", new Vector3f(0.0F, 16.0F, 16.0F));
frame12.modelRenderersTranslations.put("body4", new Vector3f(32.0F, 16.0F, 16.0F));
frame12.modelRenderersTranslations.put("body3", new Vector3f(16.0F, 0.0F, 16.0F));
keyFrames.put(12, frame12);

KeyFrame frame13 = new KeyFrame();
frame13.modelRenderersRotations.put("tentacle3", new Quaternion(-0.027006121F, 0.2658703F, -0.09738093F, 0.9586974F));
frame13.modelRenderersTranslations.put("tentacle3", new Vector3f(27.0F, 4.0F, 5.0F));
keyFrames.put(13, frame13);

KeyFrame frame21 = new KeyFrame();
frame21.modelRenderersRotations.put("tentacle4", new Quaternion(-0.12596315F, 0.31988558F, 0.09724006F, 0.93399715F));
frame21.modelRenderersTranslations.put("tentacle4", new Vector3f(5.0F, 4.0F, 5.0F));
keyFrames.put(21, frame21);

KeyFrame frame23 = new KeyFrame();
frame23.modelRenderersRotations.put("tentacle2", new Quaternion(-0.18005528F, -0.025838263F, 0.18318246F, 0.9661038F));
frame23.modelRenderersRotations.put("tentacle1", new Quaternion(-0.15186395F, 0.18318865F, -0.110821724F, 0.96493405F));
frame23.modelRenderersTranslations.put("tentacle2", new Vector3f(5.0F, 4.0F, 27.0F));
frame23.modelRenderersTranslations.put("tentacle1", new Vector3f(27.0F, 4.0F, 27.0F));
keyFrames.put(23, frame23);

KeyFrame frame22 = new KeyFrame();
frame22.modelRenderersRotations.put("body2", new Quaternion(0.0F, 0.0F, 0.034899496F, 0.99939084F));
frame22.modelRenderersRotations.put("body5", new Quaternion(0.0F, 0.034899496F, 0.0F, 0.99939084F));
frame22.modelRenderersRotations.put("body6", new Quaternion(0.0F, 0.0F, -0.034899496F, 0.99939084F));
frame22.modelRenderersRotations.put("body1", new Quaternion(0.034899496F, 0.0F, 0.0F, 0.99939084F));
frame22.modelRenderersRotations.put("body4", new Quaternion(-0.034899496F, 0.0F, 0.0F, 0.99939084F));
frame22.modelRenderersRotations.put("body3", new Quaternion(0.0F, -0.034899496F, 0.0F, 0.99939084F));
frame22.modelRenderersTranslations.put("body2", new Vector3f(16.0F, 16.0F, 0.0F));
frame22.modelRenderersTranslations.put("body5", new Vector3f(16.0F, 32.0F, 16.0F));
frame22.modelRenderersTranslations.put("body6", new Vector3f(16.0F, 16.0F, 32.0F));
frame22.modelRenderersTranslations.put("body1", new Vector3f(0.0F, 16.0F, 16.0F));
frame22.modelRenderersTranslations.put("body4", new Vector3f(32.0F, 16.0F, 16.0F));
frame22.modelRenderersTranslations.put("body3", new Vector3f(16.0F, 0.0F, 16.0F));
keyFrames.put(22, frame22);

KeyFrame frame28 = new KeyFrame();
frame28.modelRenderersRotations.put("tentacle3", new Quaternion(-0.117272146F, -0.007892005F, 0.06667921F, 0.9908273F));
frame28.modelRenderersTranslations.put("tentacle3", new Vector3f(27.0F, 4.0F, 5.0F));
keyFrames.put(28, frame28);

}
}