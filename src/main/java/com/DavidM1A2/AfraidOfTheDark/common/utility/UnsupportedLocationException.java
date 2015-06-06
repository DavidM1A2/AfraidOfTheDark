/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.utility;

public class UnsupportedLocationException extends RuntimeException
{
	public UnsupportedLocationException(int y1, int y2, int y3, int y4)
	{
		super("y1 = " + y1 + ", y2 = " + y2 + ", y3 = " + y3 + ", y4 = " + y4);
	}
}
