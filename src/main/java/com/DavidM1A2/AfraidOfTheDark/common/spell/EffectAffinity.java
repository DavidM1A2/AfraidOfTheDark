package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.awt.Color;

public class EffectAffinity
{
	public enum Affinities
	{
		Sorcery(new Color(204, 0, 153)), Nature(new Color(0, 102, 0)), Chaos(new Color(255, 51, 0)), Earth(new Color(102, 51, 0));

		private Color color;

		private Affinities(Color color)
		{
			this.color = color;
		}

		public Color getColor()
		{
			return this.color;
		}
	}

	private double sorcery = 0.0D;
	private double chaos = 0.0D;
	private double nature = 0.0D;
	private double earth = 0.0D;

	public EffectAffinity(double sorcery, double chaos, double nature, double earth)
	{
		this.sorcery = sorcery;
		this.chaos = chaos;
		this.nature = nature;
		this.earth = earth;
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

	public int getRed()
	{
		double color = 0;
		color = color + Affinities.Sorcery.color.getRed() * this.sorcery;
		color = color + Affinities.Chaos.color.getRed() * this.chaos;
		color = color + Affinities.Nature.color.getRed() * this.nature;
		color = color + Affinities.Earth.color.getRed() * this.earth;
		color = color / 4;
		return (int) Math.round(color);
	}

	public int getBlue()
	{
		double color = 0;
		color = color + Affinities.Sorcery.color.getBlue() * this.sorcery;
		color = color + Affinities.Chaos.color.getBlue() * this.chaos;
		color = color + Affinities.Nature.color.getBlue() * this.nature;
		color = color + Affinities.Earth.color.getBlue() * this.earth;
		color = color / 4;
		return (int) Math.round(color);
	}

	public int getGreen()
	{
		double color = 0;
		color = color + Affinities.Sorcery.color.getGreen() * this.sorcery;
		color = color + Affinities.Chaos.color.getGreen() * this.chaos;
		color = color + Affinities.Nature.color.getGreen() * this.nature;
		color = color + Affinities.Earth.color.getGreen() * this.earth;
		color = color / 4;
		return (int) Math.round(color);
	}
}
