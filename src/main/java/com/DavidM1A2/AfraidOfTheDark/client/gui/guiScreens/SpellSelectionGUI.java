package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScreen;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.client.Minecraft;

public class SpellSelectionGUI extends AOTDGuiScreen
{
	public SpellSelectionGUI()
	{
		AOTDGuiPanel background = new AOTDGuiPanel((640 - 149) / 2, (360 - 256) / 2, 149, 256, false);
		
		AOTDGuiImage backgroundImage = new AOTDGuiImage(0, 0, 149, 256, "textures/gui/spellCrafting/magicMirror.png");
		background.add(backgroundImage);
		
		for (Spell spell : AOTDPlayerData.get(this.entityPlayer).getSpellManager().getSpellList())
		{
			this.addSpellContainer(spell);
		}
		
		this.getContentPane().add(background);
	}
	
	private void addSpellContainer(Spell spell)
	{
		
	}

	@Override
	public boolean inventoryToCloseGuiScreen() 
	{
		return true;
	}

	@Override
	public boolean drawGradientBackground() 
	{
		return true;
	}
}
