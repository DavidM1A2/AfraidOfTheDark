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
	private AOTDGuiButton addNewRow;
	private AOTDGuiButton removeRow;

	public AOTDGuiSpell(int x, int y, int width, int height, boolean scissorEnabled)
	{
		super(x, y, width, height, scissorEnabled);

		AOTDGuiImage background = new AOTDGuiImage(0, 0, width, height - 14, "textures/gui/spellCrafting/tabletSpellModule2.png");
		this.add(background);

		this.addNewRow = new AOTDGuiButton(0, height - 15, 15, 15, null, "afraidofthedark:textures/gui/spellCrafting/add.png");
		this.addNewRow.addActionListener(new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, ActionType actionType)
			{
				if (component.isVisible())
					if (actionType == ActionType.MouseHover)
					{
						Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Add new spell stage", AOTDGuiUtility.getMouseX(), AOTDGuiUtility.getMouseY() - 10, 0xFFFFFFFF);
					}
					else if (actionType == ActionType.MouseEnterBoundingBox)
					{
						component.darkenColor(0.1f);
						Minecraft.getMinecraft().thePlayer.playSound("afraidofthedark:spellCraftingButtonHover", 0.6f, 1.7f);
					}
					else if (actionType == ActionType.MouseExitBoundingBox)
						component.brightenColor(0.1f);
			}
		});
		this.removeRow = new AOTDGuiButton(15, height - 15, 15, 15, null, "afraidofthedark:textures/gui/spellCrafting/delete.png");
		this.add(this.addNewRow);
		this.removeRow.addActionListener(new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, ActionType actionType)
			{
				if (component.isVisible())
					if (actionType == ActionType.MouseHover)
					{
						Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Remove spell stage", AOTDGuiUtility.getMouseX(), AOTDGuiUtility.getMouseY() - 10, 0xFFFFFFFF);
					}
					else if (actionType == ActionType.MouseEnterBoundingBox)
					{
						component.darkenColor(0.1f);
						Minecraft.getMinecraft().thePlayer.playSound("afraidofthedark:spellCraftingButtonHover", 0.6f, 1.7f);
					}
					else if (actionType == ActionType.MouseExitBoundingBox)
						component.brightenColor(0.1f);
			}
		});
		this.add(this.removeRow);
	}

	public void showPlus()
	{
		this.addNewRow.setVisible(true);
	}

	public void showMinus()
	{
		this.removeRow.setVisible(true);
	}

	public void hidePlus()
	{
		this.addNewRow.setVisible(false);
	}

	public void hideMinus()
	{
		this.removeRow.setVisible(false);
	}

	public void addActionListenerToNewRow(AOTDActionListener actionListener)
	{
		this.addNewRow.addActionListener(actionListener);
	}

	public void addActionListenerToRemoveRow(AOTDActionListener actionListener)
	{
		this.removeRow.addActionListener(actionListener);
	}
}
