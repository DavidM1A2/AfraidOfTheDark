package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScreen;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScrollBar;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScrollPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiSpell;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellRegistry;
import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.IDeliveryMethod;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.IEffect;

public class SpellSelectionGUI extends AOTDGuiScreen
{
	private AOTDGuiScrollBar scrollBar;
	private AOTDGuiScrollPanel scrollPanel;
	private List<AOTDGuiSpell> spells = new ArrayList<AOTDGuiSpell>();

	// Temp
	List<IEffect> effects = new LinkedList<IEffect>()
	{
		{
			add(SpellRegistry.getEffect("explosion"));
		}
	};
	LinkedHashMap<IDeliveryMethod, List<IEffect>> stages = new LinkedHashMap<IDeliveryMethod, List<IEffect>>()
	{
		{
			put(SpellRegistry.getDeliveryMethod("projectile"), effects);
		}
	};
	Spell temp = new Spell("Hello World, GGGGGGGGGGG", SpellRegistry.getPowerSource("projectile"), stages, UUID.randomUUID());

	public SpellSelectionGUI()
	{
		AOTDGuiPanel background = new AOTDGuiPanel((640 - 180) / 2, (360 - 256) / 2, 180, 256, false);

		AOTDGuiImage backgroundImage = new AOTDGuiImage(0, 0, 149, 256, "afraidofthedark:textures/gui/spellCrafting/magicMirror.png");
		background.add(backgroundImage);

		scrollBar = new AOTDGuiScrollBar(160, 30, 15, 200);

		scrollPanel = new AOTDGuiScrollPanel(25, 35, 100, 200, true, scrollBar);
		this.scrollPanel.setMaximumOffset(200);
		background.add(scrollPanel);
		background.add(scrollBar);

		for (Spell spell : AOTDPlayerData.get(this.entityPlayer).getSpellManager().getSpellList())
		{
			this.addSpellContainer(spell);
		}

		addSpellContainer(temp);

		this.getContentPane().add(background);
	}

	private void addSpellContainer(Spell spell)
	{
		AOTDGuiSpell guiSpell = new AOTDGuiSpell(0, 0, 100, 40, false, spell);
		this.scrollPanel.add(guiSpell);
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
