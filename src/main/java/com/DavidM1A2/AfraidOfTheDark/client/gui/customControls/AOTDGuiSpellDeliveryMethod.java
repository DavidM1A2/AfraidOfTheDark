/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.DeliveryMethods;

public class AOTDGuiSpellDeliveryMethod extends AOTDGuiSpellComponent<DeliveryMethods>
{
	public AOTDGuiSpellDeliveryMethod(int x, int y, int width, int height, DeliveryMethods deliveryMethod)
	{
		this(x, y, width, height, deliveryMethod, false);
	}

	public AOTDGuiSpellDeliveryMethod(int x, int y, int width, int height, DeliveryMethods deliveryMethod, boolean showHighlightOnSelected)
	{
		super(x, y, width, height, deliveryMethod, showHighlightOnSelected);
		this.background.setImageTexture("afraidofthedark:textures/gui/spellCrafting/deliveryMethodHolder.png");
	}

	@Override
	public void updateHoverText()
	{
		this.setHoverText("Delivery Method (" + this.getTypeNameFormatted() + ")");
	}
}
