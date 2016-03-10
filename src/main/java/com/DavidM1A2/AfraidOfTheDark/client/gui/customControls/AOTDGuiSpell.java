package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiComponent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiLabel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.client.Minecraft;

public class AOTDGuiSpell extends AOTDGuiPanel
{
	private Character myKey;

	public AOTDGuiSpell(int x, int y, int width, int height, boolean scissorEnabled, final Spell source)
	{
		super(x, y, width, height, scissorEnabled);

		myKey = AOTDPlayerData.get(entityPlayer).getSpellManager().keyFromSpell(source);

		AOTDGuiImage background = new AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/spellCrafting/spellBackground.png");
		this.add(background);

		AOTDGuiPanel spellNameContainer = new AOTDGuiPanel(5, 2, width - 15, 15, false);
		this.add(spellNameContainer);

		AOTDGuiLabel spellName = new AOTDGuiLabel(0, 0, ClientData.getTargaMSHandFontSized(30f));
		String string = source.getName();
		spellName.setText(string.substring(0, string.length() > 18 ? 18 : string.length() - 1));
		spellName.setTextColor(new float[] { 0.96f, 0.24f, 0.78f, 1.0f });
		spellNameContainer.addActionListener(new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, ActionType actionType)
			{
				if (actionType == ActionType.MouseHover)
				{
					Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(source.getName(), component.getXScaled(), component.getYScaled() - 10, 0xFFFFFFFF);
				}
			}
		});
		spellNameContainer.add(spellName);

		AOTDGuiButton edit = new AOTDGuiButton(11, 21, 22, 13, null, "afraidofthedark:textures/gui/spellCrafting/spellBackgroundEdit.png");
		edit.addActionListener(new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, ActionType actionType)
			{
				if (actionType == ActionType.MouseEnterBoundingBox)
				{
					component.darkenColor(0.1f);
				}
				else if (actionType == ActionType.MouseExitBoundingBox)
				{
					component.brightenColor(0.1f);
				}
				else if (actionType == ActionType.MouseHover)
				{
					Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Edit spell", component.getXScaled(), component.getYScaled() - 10, 0xFFFFFFFF);
				}
			}
		});
		this.add(edit);

		AOTDGuiButton delete = new AOTDGuiButton(38, 21, 22, 13, null, "afraidofthedark:textures/gui/spellCrafting/spellBackgroundDelete.png");
		delete.addActionListener(new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, ActionType actionType)
			{
				if (actionType == ActionType.MouseEnterBoundingBox)
				{
					component.darkenColor(0.1f);
				}
				else if (actionType == ActionType.MouseExitBoundingBox)
				{
					component.brightenColor(0.1f);
				}
				else if (actionType == ActionType.MouseHover)
				{
					Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Delete spell (Press delete twice to confirm)", component.getXScaled(), component.getYScaled() - 10, 0xFFFFFFFF);
				}
			}
		});
		this.add(delete);

		AOTDGuiButton keyBind = new AOTDGuiButton(67, 21, 22, 13, null, "afraidofthedark:textures/gui/spellCrafting/spellBackgroundKeyBind.png");
		keyBind.addActionListener(new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, ActionType actionType)
			{
				if (actionType == ActionType.MouseEnterBoundingBox)
				{
					component.darkenColor(0.1f);
				}
				else if (actionType == ActionType.MouseExitBoundingBox)
				{
					component.brightenColor(0.1f);
				}
				else if (actionType == ActionType.MouseHover)
				{
					Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Spell currently bound to: " + myKey, component.getXScaled(), component.getYScaled() - 10, 0xFFFFFFFF);
				}
			}
		});
		this.add(keyBind);
	}
}