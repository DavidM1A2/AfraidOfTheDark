/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources;

import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponentEnum;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

public enum PowerSources implements ISpellComponentEnum
{
	Self(1, Self.class, "afraidofthedark:textures/gui/spellCrafting/powerSources/self.png");

	private int id;
	private String textureLocation;
	private Class<? extends PowerSource> powerSourceClass;

	private PowerSources(int id, Class<? extends PowerSource> powerSourceClass, String textureLocation)
	{
		this.id = id;
		this.textureLocation = textureLocation;
		this.powerSourceClass = powerSourceClass;
	}

	public int getID()
	{
		return this.id;
	}

	public String getIcon()
	{
		return this.textureLocation;
	}

	@Override
	public String getName()
	{
		return this.toString();
	}

	@Override
	public PowerSource newInstance()
	{
		try
		{
			return this.powerSourceClass.newInstance();
		}
		catch (InstantiationException e)
		{
			LogHelper.error("Failed to create a new instance of a delivery method...");
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			LogHelper.error("Failed to create a new instance of a delivery method...");
			e.printStackTrace();
		}
		return null;
	}
}
