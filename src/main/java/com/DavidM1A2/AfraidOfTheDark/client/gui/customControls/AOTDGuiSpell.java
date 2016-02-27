package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDGuiUtility;
import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener.ActionType;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiComponent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;

import net.minecraft.client.Minecraft;

public class AOTDGuiSpell extends AOTDGuiPanel
{
	public AOTDGuiSpell(int x, int y, int width, int height, boolean scissorEnabled)
	{
		super(x, y, width, height, scissorEnabled);

		AOTDGuiImage background = new AOTDGuiImage(0, 0, width, height, "textures/gui/spellCrafting/spellBackground.png");
		this.add(background);
	}
}