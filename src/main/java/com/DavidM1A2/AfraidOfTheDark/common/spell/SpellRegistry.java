package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.IDeliveryMethod;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.IEffect;
import com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources.IPowerSource;

import scala.reflect.internal.Trees.This;

public class SpellRegistry 
{
	private static Map<String, IDeliveryMethod> deliveryMethods = new HashMap<String, IDeliveryMethod>();
	private static Map<String, IPowerSource> powerSources = new HashMap<String, IPowerSource>();
	private static Map<String, IEffect> effects = new HashMap<String, IEffect>();
	
	public static void registerDeliveryMethod(String name, IDeliveryMethod deliveryMethod)
	{
		deliveryMethods.put(name, deliveryMethod);
	}
	
	public static void registerPowerSource(String name, IPowerSource powerSource)
	{
		powerSources.put(name, powerSource);
	}
	
	public static void registerEffect(String name, IEffect effect)
	{
		effects.put(name, effect);
	}
	
	public static Collection<IDeliveryMethod> getRegisteredDeliveryMethods()
	{
		return SpellRegistry.deliveryMethods.values();
	}
	
	public static Collection<IPowerSource> getRegisteredPowerSources()
	{
		return SpellRegistry.powerSources.values();
	}
	
	public static Collection<IEffect> getRegisteredEffects()
	{
		return SpellRegistry.effects.values();
	}
	
	public static IDeliveryMethod getDeliveryMethod(String name)
	{
		return SpellRegistry.deliveryMethods.get(name);
	}
	
	public static IPowerSource getPowerSource(String name)
	{
		return SpellRegistry.powerSources.get(name);
	}
	
	public static IEffect getEffect(String name)
	{
		return SpellRegistry.effects.get(name);
	}
}
