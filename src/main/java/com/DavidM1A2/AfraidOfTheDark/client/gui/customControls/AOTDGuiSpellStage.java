/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDGuiUtility;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiComponent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;

import net.minecraft.client.Minecraft;

public class AOTDGuiSpellStage extends AOTDGuiPanel
{
	private AOTDGuiButton addNewRow;
	private AOTDGuiButton removeRow;

	public AOTDGuiSpellStage(int x, int y, int width, int height, boolean scissorEnabled)
	{
		super(x, y, width, height, scissorEnabled);

		AOTDGuiImage background = new AOTDGuiImage(0, 0, width, height - 14, "afraidofthedark:textures/gui/spellCrafting/tabletSpellModule2.png");
		this.add(background);

		AOTDGuiImage deliveryMethod = new AOTDGuiImage(5, 5, height - 25, height - 25, "afraidofthedark:textures/gui/spellCrafting/tabletIconHolder.png");
		deliveryMethod.setColor(new float[]
		{ 0.8f, 0.8f, 1.0f, 1.0f });
		deliveryMethod.addActionListener(new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, ActionType actionType)
			{
				if (actionType == ActionType.MouseHover)
					Minecraft.getMinecraft().fontRendererObj.drawString("Delivery Method Slot", AOTDGuiUtility.getMouseX(), AOTDGuiUtility.getMouseY() - 10, 0xFFFFFFFF);
				else if (actionType == ActionType.MouseEnterBoundingBox)
				{
					component.darkenColor(0.1f);
					Minecraft.getMinecraft().thePlayer.playSound("afraidofthedark:spellCraftingButtonHover", 0.6f, 1.7f);
				}
				else if (actionType == ActionType.MouseExitBoundingBox)
					component.brightenColor(0.1f);
			}
		});
		this.add(deliveryMethod);

		AOTDActionListener effectHover = new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, ActionType actionType)
			{
				if (actionType == ActionType.MouseHover)
					Minecraft.getMinecraft().fontRendererObj.drawString("Effect Slot", AOTDGuiUtility.getMouseX(), AOTDGuiUtility.getMouseY() - 10, 0xFFFFFFFF);
				else if (actionType == ActionType.MouseEnterBoundingBox)
				{
					component.darkenColor(0.1f);
					Minecraft.getMinecraft().thePlayer.playSound("afraidofthedark:spellCraftingButtonHover", 0.6f, 1.7f);
				}
				else if (actionType == ActionType.MouseExitBoundingBox)
					component.brightenColor(0.1f);
			}
		};

		AOTDGuiImage effect1 = new AOTDGuiImage(25, 5, height - 25, height - 25, "afraidofthedark:textures/gui/spellCrafting/tabletIconHolder.png");
		effect1.addActionListener(effectHover);
		AOTDGuiImage effect2 = new AOTDGuiImage(45, 5, height - 25, height - 25, "afraidofthedark:textures/gui/spellCrafting/tabletIconHolder.png");
		effect2.addActionListener(effectHover);
		AOTDGuiImage effect3 = new AOTDGuiImage(65, 5, height - 25, height - 25, "afraidofthedark:textures/gui/spellCrafting/tabletIconHolder.png");
		effect3.addActionListener(effectHover);
		AOTDGuiImage effect4 = new AOTDGuiImage(85, 5, height - 25, height - 25, "afraidofthedark:textures/gui/spellCrafting/tabletIconHolder.png");
		effect4.addActionListener(effectHover);
		this.add(effect1);
		this.add(effect2);
		this.add(effect3);
		this.add(effect4);

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
