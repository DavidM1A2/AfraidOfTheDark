package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.awt.Color;

public class EffectAffinity
{
	public enum Affinities
	{
		Sorcery(new Color(70, 0, 255)),
		Nature(new Color(0, 255, 0)),
		Chaos(new Color(255, 15, 0)),
		Earth(new Color(130, 58, 0));

		private Color color;

		private Affinities(Color color)
		{
			this.color = color;
		}

		public Color getColor()
		{
			return this.color;
		}

		public String getName()
		{
			return this.toString();
		}
	}

	private double sorcery = 0.0D;
	private double chaos = 0.0D;
	private double nature = 0.0D;
	private double earth = 0.0D;

	private float red;
	private float green;
	private float blue;

	public EffectAffinity(double sorcery, double chaos, double nature, double earth)
	{
		this.sorcery = sorcery;
		this.chaos = chaos;
		this.nature = nature;
		this.earth = earth;
		this.forgeColor();
	}

	public Affinities getDominantAffinity()
	{
		double max = Math.max(sorcery, Math.max(chaos, Math.max(nature, earth)));
		if (max == earth)
			return Affinities.Earth;
		else if (max == chaos)
			return Affinities.Chaos;
		else if (max == nature)
			return Affinities.Nature;
		else if (max == sorcery)
			return Affinities.Sorcery;
		return null;
	}

	private void forgeColor()
	{
		this.red = 0;
		this.red = (float) (this.red + Affinities.Sorcery.color.getRed() * this.sorcery);
		this.red = (float) (this.red + Affinities.Chaos.color.getRed() * this.chaos);
		this.red = (float) (this.red + Affinities.Nature.color.getRed() * this.nature);
		this.red = (float) (this.red + Affinities.Earth.color.getRed() * this.earth);

		this.green = 0;
		this.green = (float) (this.green + Affinities.Sorcery.color.getGreen() * this.sorcery);
		this.green = (float) (this.green + Affinities.Chaos.color.getGreen() * this.chaos);
		this.green = (float) (this.green + Affinities.Nature.color.getGreen() * this.nature);
		this.green = (float) (this.green + Affinities.Earth.color.getGreen() * this.earth);

		this.blue = 0;
		this.blue = (float) (this.blue + Affinities.Sorcery.color.getBlue() * this.sorcery);
		this.blue = (float) (this.blue + Affinities.Chaos.color.getBlue() * this.chaos);
		this.blue = (float) (this.blue + Affinities.Nature.color.getBlue() * this.nature);
		this.blue = (float) (this.blue + Affinities.Earth.color.getBlue() * this.earth);

		float max = Math.max(red, Math.max(green, blue));
		float toAdd = 255 - max;

		this.red = this.red + toAdd;
		this.green = this.green + toAdd;
		this.blue = this.blue + toAdd;

		this.red = this.red / 255f;
		this.green = this.green / 255f;
		this.blue = this.blue / 255f;
	}

	public float getRed()
	{
		return this.red;
	}

	public float getGreen()
	{
		return this.green;
	}

	public float getBlue()
	{
		return this.blue;
	}

	@Override
	public String toString()
	{
		String toReturn = "";

		if (this.sorcery != 0)
			toReturn = toReturn + "Sorcery: " + this.sorcery * 100 + "% ";
		if (this.chaos != 0)
			toReturn = toReturn + "Chaos: " + this.chaos * 100 + "% ";
		if (this.nature != 0)
			toReturn = toReturn + "Nature: " + this.nature * 100 + "% ";
		if (this.earth != 0)
			toReturn = toReturn + "Earth: " + this.earth * 100 + "% ";
		if (toReturn.isEmpty())
			toReturn = "Neutral";

		return toReturn;
	}
}
