/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.DeliveryMethods;

public class AOTDGuiSpellDeliveryMethod extends AOTDGuiSpellComponent
{
	private DeliveryMethods deliveryMethod;

	public AOTDGuiSpellDeliveryMethod(int x, int y, int width, int height, DeliveryMethods deliveryMethod)
	{
		super(x, y, width, height);
		this.deliveryMethod = deliveryMethod;

		this.background.setImageTexture("afraidofthedark:textures/gui/spellCrafting/deliveryMethodHolder.png");

		if (deliveryMethod != null)
		{
			this.icon.setImageTexture(deliveryMethod.getIcon());
			this.icon.setVisible(true);
		}
	}

	@Override
	public DeliveryMethods getType()
	{
		return this.deliveryMethod;
	}

	@Override
	public String getTypeNameFormatted()
	{
		return this.deliveryMethod != null ? this.deliveryMethod.getName() : "Empty";
	}

	public void setType(DeliveryMethods type)
	{
		this.deliveryMethod = type;
		if (type != null)
		{
			this.icon.setImageTexture(type.getIcon());
			this.icon.setVisible(true);
		}
		else
		{
			this.icon.setVisible(false);
		}
	}

	@Override
	public void updateHoverText()
	{
		this.setHoverText("Delivery Method (" + this.getTypeNameFormatted() + ")");
	}
}
