package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiLabel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

public class AOTDGuiSpell extends AOTDGuiPanel
{
	public AOTDGuiSpell(int x, int y, int width, int height, boolean scissorEnabled, Spell source)
	{
		super(x, y, width, height, scissorEnabled);

		AOTDGuiImage background = new AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/spellCrafting/spellBackground.png");
		this.add(background);

		AOTDGuiLabel spellName = new AOTDGuiLabel(2, 2, ClientData.getTargaMSHandFontSized(30f));
		spellName.setText(source.getName());
		spellName.setTextColor(new float[]
		{ 0.96f, 0.24f, 0.78f, 1.0f });
		this.add(spellName);

		AOTDGuiButton edit = new AOTDGuiButton(2, 10, 50, 50, null, "");
	}
}